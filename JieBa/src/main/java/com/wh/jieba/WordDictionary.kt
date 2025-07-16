package com.wh.jieba

import android.content.res.AssetManager
import android.os.Environment
import android.util.Log
import com.wh.jieba.Utility.LOGTAG
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.Arrays
import java.util.Locale
import kotlin.concurrent.Volatile
import kotlin.math.ln
import kotlin.math.min

class WordDictionary private constructor(assetManager: AssetManager) {
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

  init {
    val start = System.currentTimeMillis()

    // 加载字典文件
    val strArray = getStrArrayFromFile(assetManager)

    if (strArray == null) {
      Log.d(LOGTAG, "getStrArrayFromFile failed, stop")
    } else {
      trie = Element(0.toChar())
      val elemArr = ArrayList<Element>()
      elemArr.add(trie)

      restoreElement(elemArr, strArray, 0)

      val end = System.currentTimeMillis()
      Log.d(LOGTAG, String.format("restoreElement takes %d ms", end - start))
    }
  }

  /**
   * 预处理，生成中间文件
   * @param assetManager
   */
  private fun preProcess(assetManager: AssetManager) {
    val result = this.loadDict(assetManager)

    if (result) {
      val arr = ArrayList<Element?>()
      arr.add(element)
      saveDictToFile(arr)
    } else {
      Log.e(LOGTAG, "Error")
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
    if (elemArray.size <= 0) {
      return
    }

    val newElemArray = ArrayList<Element>()

    for (i in elemArray.indices) {
      val strCluster = strArray!![startIndex]
      val strList = strCluster.split(SLASH.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

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
    if (elementArray.size <= 0) {
      Log.d(LOGTAG, "saveDictToFile final str: $dicLineBuild")

      try {
        val file = File(Environment.getExternalStorageDirectory(), MAIN_PROCESSED)

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

        Log.d(LOGTAG, String.format("字典中间文件生成成功，存储在%s", file.absolutePath))
      } catch (e: Exception) {
        Log.d(LOGTAG, "字典中间文件生成失败！")
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


  private fun loadDict(assetManager: AssetManager): Boolean {
    element = Element(0.toChar()) // 创建一个根Element，只有一个，其他的Element全是其子孙节点
    var `is`: InputStream? = null
    try {
      val start = System.currentTimeMillis()
      `is` = assetManager.open(MAIN_DICT)

      val br = BufferedReader(InputStreamReader(`is`, Charset.forName("UTF-8")))

      val s = System.currentTimeMillis()
      while (br.ready()) {
        val line = br.readLine()
        val tokens = line.split("[\t ]+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

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
      Log.d(
        LOGTAG, String.format(
          "main dict load finished, time elapsed %d ms",
          System.currentTimeMillis() - s
        )
      )
    } catch (e: IOException) {
      Log.e(LOGTAG, String.format("%s load failure!", MAIN_DICT))
      return false
    } finally {
      try {
        `is`?.close()
      } catch (e: IOException) {
        Log.e(LOGTAG, String.format("%s close failure!", MAIN_DICT))
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

  private fun getStrArrayFromFile(assetManager: AssetManager): List<String>? {
    val strArray: List<String>

    val `is`: InputStream?
    try {
      `is` = assetManager.open(OUTFILE)

      val br = BufferedReader(InputStreamReader(`is`, Charset.forName("UTF-8")))

      // 第一行是字典文件
      val dictLine = br.readLine()
      strArray = listOf(*dictLine.split(TAB.toRegex()).dropLastWhile { it.isEmpty() }
        .toTypedArray())

      // 第二行是：最小频率 TAB 单词1 TAB 频率 TAB 单词2 TAB 频率 ...
      val freqLine = br.readLine()
      val strArray2 = listOf(*freqLine.split(TAB.toRegex()).dropLastWhile { it.isEmpty() }
        .toTypedArray())
      minFreq = strArray2[0].toDouble()

      val wordCnt = (strArray2.size - 1) / 2

      // freqs.put操作需要3秒才能完成，所以放在一个线程中异步进行，在map加载完成之前调用分词会不那么准确，但是不会报错
      Thread {
        for (i in 0 until wordCnt) {
          freqs[strArray2[2 * i + 1]] = strArray2[2 * i + 2].toDouble()
        }
      }.start()

      br.close()
      `is`.close()

      return strArray
    } catch (e: Exception) {
      e.printStackTrace()
    }

    return null
  }

  companion object {
    private const val MAIN_DICT = "jieba/dict.txt"
    private const val MAIN_PROCESSED = "dict_processed.txt"
    private const val OUTFILE = "jieba/$MAIN_PROCESSED"

    fun getInstance(assetManager: AssetManager): WordDictionary {
      return WordDictionary(assetManager)
    }
  }
}
