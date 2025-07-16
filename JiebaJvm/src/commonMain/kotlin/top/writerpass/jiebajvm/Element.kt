package top.writerpass.jiebajvm

import java.io.Serializable
import java.util.Arrays

/**
 * 词典树分段，表示词典树的一个分枝
 */
class Element internal constructor(// 当前节点上存储的字符
  private var nodeChar: Char
) : Comparable<Element>, Serializable {
  // Map存储结构
  var childrenMap: HashMap<Char, Element> = hashMapOf()

  // 数组方式存储结构
  private var childrenArray: Array<Element?>? = null

  // 当前节点存储的Segment数目
  // storeSize <=ARRAY_LENGTH_LIMIT ，使用数组存储， storeSize >ARRAY_LENGTH_LIMIT
  // ,则使用Map存储
  var storeSize: Int = 0

  // 当前DictSegment状态 ,默认 0 , 1表示从根节点到当前节点的路径表示一个词
  var nodeState: Int = 0


  /*
     * 判断是否有下一个节点
     */
  fun hasNextNode(): Boolean {
    return this.storeSize > 0
  }

  /**
   * 匹配词段
   *
   * @param charArray:CharArray
   * @param begin
   * @param length
   * @param searchHit
   * @return Hit
   */
  /**
   * 匹配词段
   *
   * @param charArray
   * @return Hit
   */
  /**
   * 匹配词段
   *
   * @param charArray
   * @param begin
   * @param length
   * @return Hit
   */
  @JvmOverloads
  fun match(
    charArray: CharArray,
    begin: Int = 0,
    length: Int = charArray.size,
    searchHit: Hit? = null
  ): Hit {
    var searchHit = searchHit
    if (searchHit == null) {
      // 如果hit为空，新建
      searchHit = Hit()
      // 设置hit的其实文本位置
      searchHit.begin = begin
    } else {
      // 否则要将HIT状态重置
      searchHit.setUnmatch()
    }
    // 设置hit的当前处理位置
    searchHit.end = begin

    val keyChar = charArray[begin]
    var ds: Element? = null

    // 引用实例变量为本地变量，避免查询时遇到更新的同步问题
    val elementArray = this.childrenArray
    val segmentMap: Map<Char, Element>? = this.childrenMap

    // STEP1 在节点中查找keyChar对应的DictSegment
    if (elementArray != null) {
      // 在数组中查找
      val keyElement = Element(keyChar)
      val position = Arrays.binarySearch(elementArray, 0, this.storeSize, keyElement)
      if (position >= 0) {
        ds = elementArray[position]
      }
    } else if (segmentMap != null) {
      // 在map中查找
      ds = segmentMap[keyChar]
    }

    // STEP2 找到DictSegment，判断词的匹配状态，是否继续递归，还是返回结果
    if (ds != null) {
      if (length > 1) {
        // 词未匹配完，继续往下搜索
        return ds.match(charArray, begin + 1, length - 1, searchHit)
      } else if (length == 1) {
        // 搜索最后一个char

        if (ds.nodeState == 1) {
          // 添加HIT状态为完全匹配
          searchHit.setMatch()
        }
        if (ds.hasNextNode()) {
          // 添加HIT状态为前缀匹配
          searchHit.setPrefix()
          // 记录当前位置的DictSegment
          searchHit.matchedElement = ds
        }
        return searchHit
      }
    }
    // STEP3 没有找到DictSegment， 将HIT设置为不匹配
    return searchHit
  }


  /**
   * 加载填充词典片段
   *
   * @param charArray
   */
  fun fillElement(charArray: CharArray) {
    this.fillElement(charArray, 0, charArray.size)
  }

  /**
   * 递归将一个词按每个字加入字典树
   *
   * @param charArray  eg: 一两千块
   * @param begin
   * @param length
   */
  @Synchronized
  private fun fillElement(charArray: CharArray, begin: Int, length: Int) {
    // 获取字典表中的汉字对象
    val beginChar = charArray[begin] // eg: 一

    // 字典中没有该字，则将其添加入字典
//        if (!Utility.totalCharSet.contains(beginChar)) {
//            Utility.totalCharSet.add(beginChar);
//        }
    val keyChar = beginChar

    // 搜索当前节点的存储，查询对应keyChar的keyChar，如果没有则创建
    val ds = lookforOrCreateSegment(keyChar)
    if (length > 1) {
      // 词元还没有完全加入词典树
      ds.fillElement(charArray, begin + 1, length - 1)
    } else if (length == 1) {
      // 已经是词元的最后一个char,设置当前节点状态为enabled，
      // enabled=1表明一个完整的词，enabled=0表示从词典中屏蔽当前词
      ds.nodeState = 1
    }
  }


  /**
   * 查找本节点下对应的keyChar的segment *
   *
   * @param keyChar eg:一
   * @return
   */
  private fun lookforOrCreateSegment(keyChar: Char): Element {
    var ds: Element? = null

    // 获取Map容器，如果Map未创建,则创建Map
    val segmentMap = orCreateChildrenMap
    // 搜索Map
    ds = segmentMap!![keyChar]
    if (ds == null) {
      // 构造新的segment
      ds = Element(keyChar)
      segmentMap[keyChar] = ds
      // 当前节点存储segment数目+1
      storeSize++
    }

    return ds

    //        if (this.storeSize <= ARRAY_LENGTH_LIMIT) {
//            // 获取数组容器，如果数组未创建则创建数组
//            Element[] elementArray = getChildrenArray();
//            // 搜寻数组
//            Element keyElement = new Element(keyChar); // eg:一
//            int position = Arrays.binarySearch(elementArray, 0, this.storeSize, keyElement);
//            if (position >= 0) {
//                ds = elementArray[position];
//            }
//
//            // 遍历数组后没有找到对应的segment
//            if (ds == null) {
//                ds = keyElement;
//                if (this.storeSize < ARRAY_LENGTH_LIMIT) {
//                    // 数组容量未满，使用数组存储
//                    elementArray[this.storeSize] = ds;
//                    // segment数目+1
//                    this.storeSize++;
//                    Arrays.sort(elementArray, 0, this.storeSize);
//
//                } else {
//                    // 数组容量已满，切换Map存储
//                    // 获取Map容器，如果Map未创建,则创建Map
//                    Map<Character, Element> segmentMap = getOrCreateChildrenMap();
//                    // 将数组中的segment迁移到Map中
//                    migrate(elementArray, segmentMap);
//                    // 存储新的segment
//                    segmentMap.put(keyChar, ds);
//                    // segment数目+1 ， 必须在释放数组前执行storeSize++ ， 确保极端情况下，不会取到空的数组
//                    this.storeSize++;
//                    // 释放当前的数组引用
//                    this.childrenArray = null;
//                }
//
//            }
//
//        }
//        else {
//            // 获取Map容器，如果Map未创建,则创建Map
//            Map<Character, Element> segmentMap = getOrCreateChildrenMap();
//            // 搜索Map
//            ds = (Element) segmentMap.get(keyChar);
//            if (ds == null) {
//                // 构造新的segment
//                ds = new Element(keyChar);
//                segmentMap.put(keyChar, ds);
//                // 当前节点存储segment数目+1
//                this.storeSize++;
//            }
//        }
  }


  /**
   * 获取数组容器 线程同步方法
   */
  private fun getChildrenArray(): Array<Element?>? {
    if (this.childrenArray == null) {
      synchronized(this) {
        if (this.childrenArray == null) {
          this.childrenArray = arrayOfNulls(ARRAY_LENGTH_LIMIT)
        }
      }
    }
    return this.childrenArray
  }


  private val orCreateChildrenMap: MutableMap<Char, Element>?
    /**
     * 获取Map容器 线程同步方法
     */
    get() {
      if (this.childrenMap == null) {
        synchronized(this) {
          if (this.childrenMap == null) {
            this.childrenMap = HashMap(ARRAY_LENGTH_LIMIT * 2, 0.8f)
          }
        }
      }
      return this.childrenMap
    }

  val childMap: Map<Char, Element>?
    get() = this.childrenMap


  /**
   * 将数组中的segment迁移到Map中
   *
   * @param elementArray
   */
  private fun migrate(elementArray: Array<Element>, segmentMap: MutableMap<Char, Element>) {
    for (element in elementArray) {
      segmentMap[element.nodeChar] = element
    }
  }


  /**
   * 实现Comparable接口
   *
   * @param other
   * @return int
   */
  override fun compareTo(other: Element): Int {
    // 对当前节点存储的char进行比较
    return nodeChar.compareTo(other.nodeChar)
  }

  companion object {
    private const val serialVersionUID = 10086L

    // 数组大小上限
    private const val ARRAY_LENGTH_LIMIT = 3
  }
}