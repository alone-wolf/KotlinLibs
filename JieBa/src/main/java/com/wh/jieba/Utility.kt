package com.wh.jieba

import android.content.Context
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object Utility {
  const val LOGTAG: String = "logtag"

  // 公用字典表，存储单词的第一个字
  //    public static final Set<Character> totalCharSet = new HashSet<>(16, 0.95f);
  fun writeElemToFile(context: Context, element: Element?) {
    try {
      val fos = context.openFileOutput("test.dat", Context.MODE_PRIVATE)
      val oos = ObjectOutputStream(fos)
      oos.writeObject(element)

      oos.close()
      fos.close()

      Log.d(LOGTAG, "writeElemToFile success")
    } catch (e: IOException) {
      e.printStackTrace()
    }
  }

  fun readElemFromFile(context: Context): Element? {
    Log.d(LOGTAG, "start readElemFromFile")
    val start = System.currentTimeMillis()
    try {
      val `is`: InputStream = context.openFileInput("test.dat")
      val ois = ObjectInputStream(`is`)
      val tc = ois.readObject() as Element
      println(tc)

      ois.close()
      `is`.close()

      val end = System.currentTimeMillis()
      Log.d(LOGTAG, String.format("readElemFromFile takes %d ms", end - start))

      Log.d(LOGTAG, "end readElemFromFile")

      return tc
    } catch (e: Exception) {
      e.printStackTrace()
    }

    return null
  }
}
