package top.writerpass.jiebajvm

import top.writerpass.jiebajvm.resources.getExternalStorageDirectory
import top.writerpass.jiebajvm.resources.getInputStream
import top.writerpass.kmplibrary.utils.println
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.Locale
import kotlin.math.ln
import kotlin.math.min

class WordDictionary private constructor() {
    private val freqs: MutableMap<String?, Double> = HashMap() // 加载中间文件的话，这里也要改

    lateinit var trie: Element // 生成中间文件的时候要修改这个变量的引用为element
    private var minFreq = Double.MAX_VALUE
    private var total = 0.0
    private var element: Element? = null
    private val dicLineBuild = StringBuilder()
    private val TAB = "\t"
    private val SHARP = "#"
    private val SLASH = "/"
    private val DOLLAR = "$"

    /**
     * 预处理，生成中间文件
     * @param assetManager
     */
    private fun preProcess() {
        val result = this.loadDict()

        if (result) {
            val arr = ArrayList<Element?>()
            arr.add(element)
            saveDictToFile(arr)
        } else {
            "Error".println()
        }
    }

    /**
     * d/b/c/	g/	f/e/	#/	j/	#/	h/	#/	#/
     */
    private fun restoreElement(
        elemArray: ArrayList<Element>,
        strArray: List<String>?,
        startIndex: Int
    ) {
        var startIndex = startIndex
        if (elemArray.isEmpty()) {
            return
        }

        val newElemArray = ArrayList<Element>()

        for (i in elemArray.indices) {
            val strCluster = strArray!![startIndex]
            val strList =
                strCluster.split(SLASH.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val e = elemArray[i]
            // #/
            if (strList.size == 1 && strList[0].equals(SHARP, ignoreCase = true)) {
                e.nodeState = 1
                e.storeSize = 0
            } else { //  f/e/
                e.childrenMap.clear()
                for (s in strList) {
                    val isWord = s.length == 2
                    val ch = s[0]
                    val childElem = Element(ch)
                    childElem.nodeState = if (isWord) 1 else 0

                    e.childrenMap[ch] = childElem
                    e.storeSize++

                    newElemArray.add(childElem)
                }
            }

            startIndex++
        }

        restoreElement(newElemArray, strArray, startIndex)
    }


    /**
     * ROOT
     * b/  -- c$/   --  d/
     * e$/f/ -- #/   --  g/
     * h$/ ---- #/  ---- i$/
     * #/  --------- #/
     * @param elementArray
     */
    private fun saveDictToFile(elementArray: ArrayList<Element?>) {
        if (elementArray.isEmpty()) {
            "saveDictToFile final str: $dicLineBuild".println()

            try {
                val file: File = File(getExternalStorageDirectory(), MAIN_PROCESSED)

                if (!file.exists()) {
                    file.createNewFile()
                }

                val fos = FileOutputStream(file)

                // 第一行是字典数据
                dicLineBuild.append("\r\n")

                // 第二行： 最小频率 TAB 单词1 TAB 频率 TAB 单词2 TAB 频率 ...
                dicLineBuild.append(minFreq)

                for ((key, value) in freqs) {
                    dicLineBuild.append(TAB)
                    dicLineBuild.append(key)
                    dicLineBuild.append(TAB)
                    dicLineBuild.append(value)
                }

                fos.write(dicLineBuild.toString().toByteArray())

                fos.close()
                String.format("字典中间文件生成成功，存储在%s", file.absolutePath).println()
            } catch (e: Exception) {
                "字典中间文件生成失败！".println()
                e.printStackTrace()
            }

            return
        }

        val childArray: ArrayList<Element?> = arrayListOf()
        // elementArray有几个元素，就要添加TAB分割的几个数据段，每个数据段是该Element的子节点的字+"/"，比如 e/f/ TAB #/ TAB g/
        // 如果从根节点到当前节点的路径表示一个词，那么在后面添加$符号,如  e$/f/ TAB #/ TAB g/
        for (i in elementArray.indices) {
            val element = elementArray[i]

            // e/f/
            if (element!!.hasNextNode()) {
                for ((key, value) in element.childrenMap) {
                    dicLineBuild.append(key)

                    if (value.nodeState == 1) {
                        dicLineBuild.append(DOLLAR) // 从根节点到当前节点的路径表示一个词，那么在后面添加$符号,如  e$/f/ TAB #/ TAB g/
                    }

                    dicLineBuild.append(SLASH)

                    // 将该节点的所有子节点入列表，供下一次递归
                    childArray.add(value)
                }
            } else { // #/
                dicLineBuild.append(SHARP)
                dicLineBuild.append(SLASH)
            }

            // TAB
            dicLineBuild.append(TAB)
        }

        saveDictToFile(childArray)
    }


    private fun loadDict(): Boolean {
        element = Element(0.toChar()) // 创建一个根Element，只有一个，其他的Element全是其子孙节点
        var `is`: InputStream? = null
        try {
            val start = System.currentTimeMillis()
            `is` = getInputStream(MAIN_DICT)

            val br = BufferedReader(InputStreamReader(`is`, Charset.forName("UTF-8")))

            val s = System.currentTimeMillis()
            while (br.ready()) {
                val line = br.readLine()
                val tokens =
                    line.split("[\t ]+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                if (tokens.size < 2) continue

                val word = tokens[0] // eg:一两千块
                val freq = tokens[1].toDouble()
                total += freq
                val trimmedword = addWord(word) // 将一个单词的每个字递归的插入字典树  eg:一两千块
                freqs[trimmedword] = freq // 并统计单词首个字的频率
            }

            // normalize
            for (entry in freqs.entries) {
                entry.setValue((ln(entry.value / total)))
                minFreq = min(entry.value, minFreq)
            }
            String.format(
                "main dict load finished, time elapsed %d ms",
                System.currentTimeMillis() - s
            ).println()
        } catch (e: IOException) {
            String.format("%s load failure!", MAIN_DICT).println()
            return false
        } finally {
            try {
                `is`?.close()
            } catch (e: IOException) {
                String.format("%s close failure!", MAIN_DICT).println()
                return false
            }
        }

        return true
    }


    /**
     * 将一个单词的每个字递归的插入字典树
     * @param word
     * @return
     */
    private fun addWord(word: String?): String? {
        if (null != word && "" != word.trim { it <= ' ' }) {
            val key = word.trim { it <= ' ' }.lowercase(Locale.getDefault())
            element!!.fillElement(key.toCharArray())
            return key
        } else return null
    }


    fun containsWord(word: String?): Boolean {
        return freqs.containsKey(word)
    }


    fun getFreq(key: String?): Double? {
        return if (containsWord(key)) freqs[key]
        else minFreq
    }


    private fun getStrArrayFromFile(): List<String>? {
        val strArray: List<String>
        val tabReg = TAB.toRegex()

        getInputStream(OUTFILE).bufferedReader().use { buffer ->
            val dictLine = buffer.readLine()
            if (dictLine == null) {
                "dictLine == null".println()
            } else {
                "else".println()
            }
            strArray = dictLine
                .split(tabReg)
                .dropLastWhile { it.isEmpty() }
                .toList()

            // 第二行是：最小频率 TAB 单词1 TAB 频率 TAB 单词2 TAB 频率 ...
            val freqLine = buffer.readLine()
            val strArray2 =
                listOf(*freqLine.split(tabReg).dropLastWhile { it.isEmpty() }
                    .toTypedArray())
            minFreq = strArray2[0].toDouble()

            val wordCnt = (strArray2.size - 1) / 2

            // freqs.put操作需要3秒才能完成，所以放在一个线程中异步进行，在map加载完成之前调用分词会不那么准确，但是不会报错
            for (i in 0 until wordCnt) {
                freqs[strArray2[2 * i + 1]] = strArray2[2 * i + 2].toDouble()
            }

        }

        return strArray
    }

    suspend fun initDictionary() {
        val start = System.currentTimeMillis()

        // 加载字典文件
        getStrArrayFromFile()?.let { strArray ->
            trie = Element(0.toChar())
            val elemArr = ArrayList<Element>()
            elemArr.add(trie)

            restoreElement(elemArr, strArray, 0)

            val end = System.currentTimeMillis()
            String.format("restoreElement takes %d ms", end - start).println()
        } ?: {
            "getStrArrayFromFile failed, stop".println()
            error("getStrArrayFromFile failed, stop")
        }
    }

    companion object {
        private const val MAIN_DICT = "jieba/dict.txt"
        private const val MAIN_PROCESSED = "dict_processed.txt"
        private const val OUTFILE = "jieba/$MAIN_PROCESSED"

        fun getInstance(): WordDictionary {
            return WordDictionary()
        }
    }
}
