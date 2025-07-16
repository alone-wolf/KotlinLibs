/**
 *
 * IK 中文分词  版本 5.0
 * IK Analyzer release 5.0
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 源代码由林良益(linliangyi2005@gmail.com)提供
 * 版权声明 2012，乌龙茶工作室
 * provided by Linliangyi and copyright 2012 by Oolong studio
 *
 */
package com.wh.jieba

/**
 * 表示一次词典匹配的命中
 */
class Hit {
  //该HIT当前状态，默认未匹配
  private var hitState = UNMATCH

  //记录词典匹配过程中，当前匹配到的词典分支节点
  var matchedElement: Element? = null

  /*
 * 词段开始位置
 */
  var begin: Int = 0

  /*
 * 词段的结束位置
 */
  var end: Int = 0


  val isMatch: Boolean
    /**
     * 判断是否完全匹配
     */
    get() = (this.hitState and MATCH) > 0

  /**
   *
   */
  fun setMatch() {
    this.hitState = this.hitState or MATCH
  }

  val isPrefix: Boolean
    /**
     * 判断是否是词的前缀
     */
    get() = (this.hitState and PREFIX) > 0

  /**
   *
   */
  fun setPrefix() {
    this.hitState = this.hitState or PREFIX
  }

  val isUnmatch: Boolean
    /**
     * 判断是否是不匹配
     */
    get() = this.hitState == UNMATCH

  /**
   *
   */
  fun setUnmatch() {
    this.hitState = UNMATCH
  }

  companion object {
    //Hit不匹配
    private const val UNMATCH = 0x00000000

    //Hit完全匹配
    private const val MATCH = 0x00000001

    //Hit前缀匹配
    private const val PREFIX = 0x00000010
  }
}
