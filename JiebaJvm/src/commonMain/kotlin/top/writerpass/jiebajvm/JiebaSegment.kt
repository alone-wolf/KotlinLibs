package top.writerpass.jiebajvm


import top.writerpass.jiebajvm.CharacterUtil.ccFind
import top.writerpass.jiebajvm.CharacterUtil.regularize
import top.writerpass.jiebajvm.viterbi.FinalSeg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import top.writerpass.kmplibrary.coroutine.withContextDefault
import top.writerpass.kmplibrary.coroutine.withContextIO
import top.writerpass.kmplibrary.utils.println


class JiebaSegment private constructor() {
    private lateinit var wordDict: WordDictionary
    private lateinit var finalSeg: FinalSeg
    private val _ready = MutableStateFlow(false)
    val ready: StateFlow<Boolean> = _ready.asStateFlow()

    suspend fun initSegment() {
        wordDict = WordDictionary.getInstance()
        wordDict.initDictionary()
        finalSeg = FinalSeg.getInstance()
        _ready.emit(true)
    }

    enum class SegMode {
        //    INDEX,
        SEARCH
    }

    suspend fun divideString(query: String): List<String> {
        return withContextDefault {
            while (!_ready.value) {
                delay(SLEEP_TIME.toLong())
            }
            val start = System.currentTimeMillis()
            val lst = process(query, SegMode.SEARCH)
            val end = System.currentTimeMillis()
            String.format("getDivideList takes %d ms", end - start).println()

            val resultLst = ArrayList<String>()

            for (st in lst) {
                resultLst.add(st.word)
            }
            resultLst
        }
    }


//    fun getDividedString(query: String): ArrayList<String> {
//        while (!_ready.value) {
//            try {
//                Thread.sleep(SLEEP_TIME.toLong())
//            } catch (e: InterruptedException) {
//                e.printStackTrace()
//            }
//        }
//
//        val start = System.currentTimeMillis()
//
//        val lst = process(query, SegMode.SEARCH)
//
//        val end = System.currentTimeMillis()
//        String.format("getDivideList takes %d ms", end - start).println()
//
//        val resultLst = ArrayList<String>()
//
//        for (st in lst) {
//            resultLst.add(st.word)
//        }
//
//        return resultLst
//    }

    private fun process(query: String, mode: SegMode): List<SegToken> {
        while (!_ready.value) {
            try {
                Thread.sleep(SLEEP_TIME.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        val tokens: MutableList<SegToken> = ArrayList()
        var sb = StringBuilder()
        var offset = 0
        for (i in query.indices) {
            val ch = regularize(query[i])
            if (ccFind(ch)) sb.append(ch)
            else {
                if (sb.isNotEmpty()) {
                    // process
                    if (mode == SegMode.SEARCH) {
                        for (word in sentenceProcess(sb.toString())) {
                            tokens.add(
                                SegToken(
                                    word,
                                    offset,
                                    word.length.let { offset += it; offset })
                            )
                        }
                    } else {
                        for (token in sentenceProcess(sb.toString())) {
                            if (token.length > 2) {
                                var gram2: String
                                var j = 0
                                while (j < token.length - 1) {
                                    gram2 = token.substring(j, j + 2)
                                    if (wordDict.containsWord(gram2)) tokens.add(
                                        SegToken(
                                            gram2,
                                            offset + j,
                                            offset + j + 2
                                        )
                                    )
                                    ++j
                                }
                            }
                            if (token.length > 3) {
                                var gram3: String
                                var j = 0
                                while (j < token.length - 2) {
                                    gram3 = token.substring(j, j + 3)
                                    if (wordDict.containsWord(gram3)) tokens.add(
                                        SegToken(
                                            gram3,
                                            offset + j,
                                            offset + j + 3
                                        )
                                    )
                                    ++j
                                }
                            }
                            tokens.add(
                                SegToken(
                                    token,
                                    offset,
                                    token.length.let { offset += it; offset })
                            )
                        }
                    }
                    sb = StringBuilder()
                    offset = i
                }
                if (wordDict.containsWord(query.substring(i, i + 1))) tokens.add(
                    SegToken(
                        query.substring(
                            i,
                            i + 1
                        ), offset, ++offset
                    )
                )
                else tokens.add(SegToken(query.substring(i, i + 1), offset, ++offset))
            }
        }

        if (sb.isNotEmpty()) if (mode == SegMode.SEARCH) {
            for (token in sentenceProcess(sb.toString())) {
                tokens.add(SegToken(token, offset, token.length.let { offset += it; offset }))
            }
        } else {
            for (token in sentenceProcess(sb.toString())) {
                if (token.length > 2) {
                    var gram2: String
                    var j = 0
                    while (j < token.length - 1) {
                        gram2 = token.substring(j, j + 2)
                        if (wordDict.containsWord(gram2)) tokens.add(
                            SegToken(
                                gram2,
                                offset + j,
                                offset + j + 2
                            )
                        )
                        ++j
                    }
                }
                if (token.length > 3) {
                    var gram3: String
                    var j = 0
                    while (j < token.length - 2) {
                        gram3 = token.substring(j, j + 3)
                        if (wordDict.containsWord(gram3)) tokens.add(
                            SegToken(
                                gram3,
                                offset + j,
                                offset + j + 3
                            )
                        )
                        ++j
                    }
                }
                tokens.add(SegToken(token, offset, token.length.let { offset += it; offset }))
            }
        }

        return tokens
    }

    private fun createDAG(sentence: String): Map<Int, MutableList<Int>> {
        val dag: MutableMap<Int, MutableList<Int>> = HashMap()
        val trie = wordDict.trie
        val chars = sentence.toCharArray()
        val N = chars.size
        var i = 0
        var j = 0
        while (i < N) {
            val hit = trie.match(chars, i, j - i + 1)
            if (hit.isPrefix || hit.isMatch) {
                if (hit.isMatch) {
                    if (!dag.containsKey(i)) {
                        val value: MutableList<Int> = ArrayList()
                        dag[i] = value
                        value.add(j)
                    } else dag[i]!!.add(j)
                }
                j += 1
                if (j >= N) {
                    i += 1
                    j = i
                }
            } else {
                i += 1
                j = i
            }
        }
        i = 0
        while (i < N) {
            if (!dag.containsKey(i)) {
                val value: MutableList<Int> = ArrayList()
                value.add(i)
                dag[i] = value
            }
            ++i
        }
        return dag
    }

    /**
     * 计算有向无环图的一条最大路径，从后向前，利用贪心算法，每一步只需要找出到达该字符的最大概率字符作为所选择的路径
     *
     * @param sentence
     * @param dag
     * @return
     */
    private fun calc(sentence: String, dag: Map<Int, MutableList<Int>>): Map<Int, Pair<Int>?> {
        val N = sentence.length
        val route = HashMap<Int, Pair<Int>?>()
        route[N] = Pair(0, 0.0)
        for (i in N - 1 downTo -1 + 1) {
            var candidate: Pair<Int>? = null
            for (x in dag[i]!!) {
                val freq = wordDict.getFreq(sentence.substring(i, x + 1))!! + route[x + 1]!!.freq
                if (null == candidate) {
                    candidate = Pair(x, freq)
                } else if (candidate.freq < freq) {
                    candidate.freq = freq
                    candidate.key = x
                }
            }
            route[i] = candidate
        }
        return route
    }

    /*
       *
       */
    private fun sentenceProcess(sentence: String): List<String> {
        val tokens: MutableList<String> = ArrayList()
        val N = sentence.length

        val start = System.currentTimeMillis()
        // 将一段文字转换成有向无环图，该有向无环图包含了跟字典文件得出的所有可能的单词切分
        val dag = createDAG(sentence)

        val route = calc(sentence, dag)

        var x = 0
        var y = 0
        var buf: String
        var sb = StringBuilder()
        while (x < N) { // 遍历一遍贪心算法生成的最小路径分词结果，对单蹦个的字符看看能不能粘合成一个词汇
            y = route[x]!!.key + 1
            val lWord = sentence.substring(x, y)
            if (y - x == 1) sb.append(lWord)
            else {
                if (sb.isNotEmpty()) {
                    buf = sb.toString()
                    sb = StringBuilder()
                    if (buf.length == 1) { // 如果两个单词之间只有一个单蹦个的字符，添加
                        tokens.add(buf)
                    } else {
                        if (wordDict.containsWord(buf)) { // 如果连续单蹦个的字符粘合成的一个单词在字典树里，作为一个单词添加
                            tokens.add(buf)
                        } else {
                            finalSeg.cut(
                                buf,
                                tokens
                            ) // 如果连续单蹦个的字符粘合成的一个单词不在字典树里，使用维特比算法计算每个字符BMES如何选择使得概率最大
                        }
                    }
                }
                tokens.add(lWord)
            }
            x = y
        }
        buf = sb.toString()
        if (buf.isNotEmpty()) { // 处理余下的部分
            if (buf.length == 1) {
                tokens.add(buf)
            } else {
                if (wordDict.containsWord(buf)) {
                    tokens.add(buf)
                } else {
                    finalSeg.cut(buf, tokens)
                }
            }
        }
        return tokens
    }

    companion object {
        private const val SLEEP_TIME = 100

        fun getInstance(): JiebaSegment {
            return JiebaSegment()
        }
    }
}
