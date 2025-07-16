package com.wh.jieba

import java.util.regex.Pattern

object CharacterUtil {
  var reSkip: Pattern = Pattern.compile("(\\d+\\.\\d+|[a-zA-Z0-9]+)")
  private val connectors = charArrayOf('+', '#', '&', '.', '_', '-')


  fun isChineseLetter(ch: Char): Boolean {
    if (ch.code in 0x4E00..0x9FA5) return true
    return false
  }


  fun isEnglishLetter(ch: Char): Boolean {
    if ((ch.code in 0x0041..0x005A) || (ch.code in 0x0061..0x007A)) return true
    return false
  }


  fun isDigit(ch: Char): Boolean {
    if (ch.code in 0x0030..0x0039) return true
    return false
  }


  fun isConnector(ch: Char): Boolean {
    for (connector in connectors) if (ch == connector) return true
    return false
  }


  fun ccFind(ch: Char): Boolean {
    if (isChineseLetter(ch)) return true
    if (isEnglishLetter(ch)) return true
    if (isDigit(ch)) return true
    if (isConnector(ch)) return true
    return false
  }


  /**
   * 全角 to 半角,大写 to 小写
   *
   * @param input
   * 输入字符
   * @return 转换后的字符
   */
  fun regularize(input: Char): Char {
    return when {
      input == 12288.toChar() -> 32.toChar()
      input in 65280.toChar()..65374.toChar() -> (input - 65248)
      input in 'A'..'Z' -> (input + 32)
      else -> input
    }
  }

//  @JvmStatic
//  fun regularize(input: Char): Char {
//    var input1 = input
//    if (input1.code == 12288) {
//      return 32
//    } else if (input1.code in 65281..65374) {
//      return (input1.code - 65248).toChar()
//    } else if (input1 in 'A'..'Z') {
//      return (32.let { input1 += it; input1 })
//    }
//    return input1
//  }
}
