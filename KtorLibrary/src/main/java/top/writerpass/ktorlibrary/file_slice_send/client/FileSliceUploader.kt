package top.writerpass.ktorlibrary.file_slice_send.client

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import top.writerpass.kotlinlibrary.map.ObservableConcurrentMap
import top.writerpass.kotlinlibrary.coroutine.withContextDefault
import top.writerpass.kotlinlibrary.digest.calcSHA256String
import top.writerpass.kotlinlibrary.file.operator.FileShardTool
import top.writerpass.kotlinlibrary.utils.log
import top.writerpass.kotlinlibrary.utils.println
import java.io.File

class FileSliceUploader(
    private val requester: FileSliceUploadRequester,
    private val sourceFile: File
) {
    // 需要线程安全的改进
    private var lock: Boolean = false

    private val _currentState = MutableStateFlow<UploadSteps>(UploadSteps.Idle)
    val currentState = _currentState.asStateFlow()

    fun UploadSteps.setAsCurrentState() {
        _currentState.value = this
    }

    val eachSliceState = ObservableConcurrentMap<Int, SliceState>(10)

    fun start() {
        if (lock.not()) {
            UploadSteps.Info().setAsCurrentState()
            lock = true
        } else "on lock, can't start another transportation".log("client:")
    }

    private suspend fun handleStateIdle(state: UploadSteps.Idle) {
        "Step1: idle, waiting for start, next step is info".log("client:")
    }

    private suspend fun handleStateInfo(state: UploadSteps.Info) {
        "Step2: info".log("client:")
        requester.info()
            .onFailure {
                "Step2: info error, ${it.localizedMessage}".log("client:")
                it.printStackTrace()
                UploadSteps.Finish(false)
                    .setAsCurrentState()
            }
            .onSuccess { infoResponse ->
                "Step2: info success, $infoResponse".log("client:")
                UploadSteps.Prepare(
                    file = sourceFile,
                    info = infoResponse
                ).setAsCurrentState()
            }
    }

    private suspend fun handleStatePrepare(state: UploadSteps.Prepare) {
        "Step3: prepare".log("client:")
        val info = state.info
        val fileSize = sourceFile.length()
        val sliceFullSize = if (fileSize < info.sliceThreshold) {
            fileSize
        } else {
            info.maxSliceSize
        }
        val threadNum = info.threadNum
        requester.prepare(
            fileSize = fileSize,
            filename = sourceFile.name,
            maxFragmentSize = sliceFullSize
        )
            .onFailure {
                "Step3: prepare error, ${it.localizedMessage}".log("client:")
                it.printStackTrace()
                UploadSteps.Finish(false)
                    .setAsCurrentState()
            }.onSuccess { prepareResponse ->
                if (prepareResponse.quickSendMatch) {
                    UploadSteps.Finish(true).setAsCurrentState()
                } else {
                    val uuid = prepareResponse.uuid
                    UploadSteps.SendLoop(
                        retryTimes = 0,
                        sliceFullSize = sliceFullSize,
                        threadNum = threadNum,
                        subStep = UploadSubSteps.Send(),
                        uuid = uuid
                    ).setAsCurrentState()
                }
            }
    }

    private suspend fun handleStateSendLoop(state: UploadSteps.SendLoop) {
//        val sliceFullSize = state.sliceFullSize
        val tryTimes = state.retryTimes
//        val uuid = state.uuid
//        val threadNum = state.threadNum
        "Step4-${tryTimes}: send-loop".log("client:")
        when (state.subStep) {
            is UploadSubSteps.Send -> handleStateSendLoopSend(state)
            is UploadSubSteps.Status -> handleStateSendLoopStatus(state)
        }
    }

    private suspend fun handleStateSendLoopSend(state: UploadSteps.SendLoop) {
        val sliceFullSize = state.sliceFullSize
        val tryTimes = state.retryTimes
        val uuid = state.uuid
        val threadNum = state.threadNum
        "Step4-${state.retryTimes}: send-loop_send".log("client:")
        eachSliceState.flow.collect { it ->
            it.all { (_, sliceState) ->
                sliceState.status == SliceUploadStatus.Sent
            }.also { isAllSent ->
                if (isAllSent) {
                    state.copy(
                        subStep = UploadSubSteps.Status()
                    ).setAsCurrentState()
                }
            }
        }

        FileShardTool(
            file = sourceFile,
            sliceSize = sliceFullSize
        )
            .printInfo { status ->
                val mutableMap = mutableMapOf<Int, SliceState>()
                (0 until status.sliceFullNum.toInt()).forEach {
                    mutableMap[it] = SliceState.pending(
                        index = it,
                        size = status.sliceSize.toInt()
                    )
                }
                if (status.hasLastSlice) {
                    mutableMap[status.sliceFullNum.toInt()] =
                        SliceState.pending(
                            index = status.sliceFullNum.toInt(),
                            size = status.sliceLastSize.toInt()
                        )
                }
                eachSliceState.putAll(mutableMap)
            }
            .reader(threadNum)
            .readAll(CoroutineScope(Dispatchers.IO)) { index, _, byteArray ->
                eachSliceState.update(index) { state ->
                    state?.hashing()
                }
                val hash = withContextDefault {
                    byteArray.calcSHA256String()
                }
                eachSliceState.update(index) { state ->
                    state?.sending(hash)
                }
                requester.send(
                    uuid = uuid,
                    index = index,
                    bytes = byteArray,
                    hash = hash,
                    onUploadProgress = { a, b ->
                        eachSliceState.update(index) { state ->
                            state?.sendingWithProgress(a)
                        }
                    }
                ).onFailure {
                    it.localizedMessage.log("client:")
                    eachSliceState.update(index) { state ->
                        state?.error(it.localizedMessage)
                    }
                }.onSuccess {
                    eachSliceState.update(index) { state ->
                        state?.sent()
                    }
                }
            }
    }

    private suspend fun handleStateSendLoopStatus(state: UploadSteps.SendLoop) {
        "Step4-${state.retryTimes}: send-loop_status".log("client:")
    }

    private suspend fun handleStateFinish(state: UploadSteps.Finish) {
        "Step5: finish".log("client:")
        "finish".log("client:")
        lock = false
    }

    suspend fun handleState(): Unit = currentState.collect { state ->
        when (state) {
            is UploadSteps.Idle -> handleStateIdle(state)
            is UploadSteps.Info -> handleStateInfo(state)
            is UploadSteps.Prepare -> handleStatePrepare(state)
            is UploadSteps.SendLoop -> handleStateSendLoop(state)
            is UploadSteps.Finish -> handleStateFinish(state)
        }
    }
}

//private fun <T : Any, U> ConcurrentMap<T, U>.update(k: T, block: (U?) -> U?) {
//    val item = get(k)
//    val item1 = block(item)
//    if (item1 != null) {
//        set(k, item1)
//    }
//}