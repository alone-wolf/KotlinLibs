package com.wh.jieba.viterbi

import android.content.res.AssetManager
import android.util.Log
import com.wh.jieba.CharacterUtil
import com.wh.jieba.Node
import com.wh.jieba.Pair
import com.wh.jieba.Utility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.Collections
import java.util.Locale
import java.util.Vector


class FinalSeg internal constructor() {
    suspend fun loadsModel(assetManager: AssetManager) {
        withContext(Dispatchers.IO) {
            loadModel(assetManager)
        }
    }

    private fun loadModel(assetManager: AssetManager) {
        val s = System.currentTimeMillis()
        prevStatus['B'] = charArrayOf('E', 'S')
        prevStatus['M'] = charArrayOf('M', 'B')
        prevStatus['S'] = charArrayOf('S', 'E')
        prevStatus['E'] = charArrayOf('B', 'M')

        start['B'] = -0.26268660809250016
        start['E'] = -3.14e+100
        start['M'] = -3.14e+100
        start['S'] = -1.4652633398537678

        trans['B'] = hashMapOf(
            'E' to -0.510825623765990,
            'M' to -0.916290731874155
        )

        trans['E'] = hashMapOf(
            'B' to -0.5897149736854513,
            'S' to -0.8085250474669937
        )

        trans['M'] = hashMapOf(
            'E' to -0.33344856811948514,
            'M' to -1.2603623820268226
        )

        trans['S'] = hashMapOf(
            'B' to -0.7211965654669841,
            'S' to -0.6658631448798212
        )

        val assetInputStream = assetManager.open(PROB_EMIT)
        val inputStreamReader = InputStreamReader(assetInputStream, StandardCharsets.UTF_8)
        val assetBufferedReader = BufferedReader(inputStreamReader)

        val values = hashMapOf<Char, Double>()
        while (assetBufferedReader.ready()) {
            val line = assetBufferedReader.readLine()
            val tokens = line.split("\t".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (tokens.size == 1) {
                values.clear()
                emit[tokens[0][0]] = values
            } else {
                values[tokens[0][0]] = tokens[1].toDouble()
            }
        }

        assetBufferedReader.close()
        inputStreamReader.close()
        assetInputStream.close()
        Log.d(
            Utility.LOGTAG, String.format(
                Locale.getDefault(), "FinalSeg model load finished, time elapsed %d ms.",
                System.currentTimeMillis() - s
            )
        )
    }


    fun cut(sentence: String, tokens: MutableList<String>) {
        var chinese = StringBuilder()
        var other = StringBuilder()
        for (element in sentence) {
            if (CharacterUtil.isChineseLetter(element)) { // 遇到一个汉字，就把之前累积的非汉字处理一下加入最终结果
                if (other.isNotEmpty()) {
                    processOtherUnknownWords(other.toString(), tokens)
                    other = StringBuilder()
                }
                chinese.append(element)
            } else {
                if (chinese.isNotEmpty()) { // 遇到一个非汉字符号，就把之前累加的单蹦个汉字处理一下加入最终结果
                    viterbi(chinese.toString(), tokens) // 处理一串单蹦个汉字的方法是维特比算法
                    chinese = StringBuilder()
                }
                other.append(element)
            }
        }

        if (chinese.isNotEmpty()) // 处理余下的汉字
            viterbi(chinese.toString(), tokens)
        else {  // 处理余下的非汉字字符
            processOtherUnknownWords(other.toString(), tokens)
        }
    }


    /**
     * 利用维特比算法计算对于一串单蹦个的字符，每个字符到下一个字符如何跳转，以实现整条路径的概率最大
     * 例如：我  去   五  道   口
     * B   B   B   B   B
     * M   M   M   M   M
     * E   E   E   E   E
     * S   S   S   S   S
     * @param sentence
     * @param tokens
     */
    fun viterbi(sentence: String, tokens: MutableList<String>) {
        val v = Vector<MutableMap<Char, Double>>()
        var path: MutableMap<Char?, Node?> = HashMap()

        v.add(HashMap())
        val MIN_FLOAT = -3.14e100
        for (state in states) {
            var emP = emit[state]!![sentence[0]]
            if (null == emP) emP = MIN_FLOAT
            v[0][state] = start[state]!! + emP
            path[state] = Node(state, null)
        }

        for (i in 1 until sentence.length) {
            val vv: MutableMap<Char, Double> = HashMap()
            v.add(vv)
            val newPath: MutableMap<Char?, Node?> = HashMap()
            for (y in states) {
                var emp = emit[y]!![sentence[i]]
                if (emp == null) emp = MIN_FLOAT
                var candidate: Pair<Char>? = null
                for (y0 in prevStatus[y]!!) {
                    var tranp = trans[y0]!![y]
                    if (null == tranp) tranp = MIN_FLOAT
                    tranp += (emp + v[i - 1][y0]!!)
                    if (null == candidate) candidate = com.wh.jieba.Pair(y0, tranp)
                    else if (candidate.freq <= tranp) {
                        candidate.freq = tranp
                        candidate.key = y0
                    }
                }
                vv[y] = candidate!!.freq
                newPath[y] = Node(y, path[candidate.key])
            }
            path = newPath
        }
        val probE = v[sentence.length - 1]['E']!!
        val probS = v[sentence.length - 1]['S']!!
        val posList = Vector<Char>(sentence.length)
        var win: Node?
        win = if (probE < probS) path['S']
        else path['E']

        while (win != null) {
            posList.add(win.value)
            win = win.parent
        }
        posList.reverse()

        var begin = 0
        var next = 0
        for (i in sentence.indices) {
            val pos = posList[i]
            if (pos == 'B') begin = i
            else if (pos == 'E') {
                tokens.add(sentence.substring(begin, i + 1))
                next = i + 1
            } else if (pos == 'S') {
                tokens.add(sentence.substring(i, i + 1))
                next = i + 1
            }
        }
        if (next < sentence.length) tokens.add(sentence.substring(next))
    }


    private fun processOtherUnknownWords(other: String, tokens: MutableList<String>) {
        val mat = CharacterUtil.reSkip.matcher(other)
        var offset = 0
        while (mat.find()) {
            if (mat.start() > offset) {
                tokens.add(other.substring(offset, mat.start()))
            }
            tokens.add(mat.group())
            offset = mat.end()
        }
        if (offset < other.length) tokens.add(other.substring(offset))
    }

    companion object {
        //    private var singleInstance: FinalSeg? = null
        private const val PROB_EMIT = "jieba/prob_emit.txt"
        private val states = charArrayOf('B', 'M', 'E', 'S')
        private var emit: MutableMap<Char, Map<Char, Double>> = hashMapOf()
        private var start: MutableMap<Char, Double> = hashMapOf()
        private var trans: MutableMap<Char, Map<Char, Double>> = hashMapOf()
        private var prevStatus: MutableMap<Char, CharArray> = hashMapOf()


        suspend fun getsInstance(assetManager: AssetManager): FinalSeg {
            return withContext(Dispatchers.IO) {
                return@withContext FinalSeg().apply {
                    loadsModel(assetManager)
                }
            }
        }

//    @JvmStatic
//    @Synchronized
//    fun getInstance(assetManager: AssetManager): FinalSeg {
//      return FinalSeg().apply {
//        loadModel(assetManager)
//      }
////      if (null == singleInstance) {
////        singleInstance = FinalSeg()
////        singleInstance!!.loadModel(assetManager)
////      }
////      return singleInstance
//    }
    }
}
