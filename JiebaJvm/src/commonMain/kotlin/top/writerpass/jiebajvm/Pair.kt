package top.writerpass.jiebajvm

class Pair<K>(var key: K, freq: Double) {
  var freq: Double = 0.0

  init {
    this.freq = freq
  }

  override fun toString(): String {
    return "Candidate [key=$key, freq=$freq]"
  }
}
