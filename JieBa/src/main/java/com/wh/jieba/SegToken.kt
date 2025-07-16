package com.wh.jieba

class SegToken(
  var word: String,
  var startOffset: Int,
  var endOffset: Int
) {
  override fun toString(): String {
    return "[$word, $startOffset, $endOffset]"
  }
}
