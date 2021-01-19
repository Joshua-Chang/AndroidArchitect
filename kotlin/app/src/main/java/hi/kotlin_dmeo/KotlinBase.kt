package hi.kotlin_dmeo

import kotlin.math.abs

fun main() {
    println("---main---")
//    baseType()
//    arrayType()
    collectionType()
//    collectionSort()
}

fun collectionType() {
    val list = listOf("one", "two", "one")
    println(list)
    val set = setOf("one", "two", "one")//不可重复
    println(set)
    val mutableListOf = mutableListOf(1, 2, 3, 4)
    mutableListOf.add(5)
    mutableListOf.removeAt(1)
    mutableListOf[0] = 9
    println(mutableListOf)

    val hello = mutableSetOf("H", "e", "l", "l", "o")//自动过滤重复元素
    hello.remove("o")
    println(hello)
    hello += setOf("w", "o", "r", "l", "d")//集合相加，前面的必须为可变集合
    println(hello)

    val map = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key4" to 4, "key5" to 5)
    println(map.keys)
    println(map.values)
    if ("key2" in map) println("Value by key key2:${map["key2"]}")
    if (1 in map.values) println("1 is in the map")
    if (map.containsKey("key1")) {
        println()
    }
    if (map.containsValue(1)) {
        println()
    }

    /**
     * Q1：两个具有相同键值对，但顺序不同的map相等吗？为什么？
     * 无论键值对的顺序如何，包含相同键值对的两个 Map 是相等的
     */
    val anotherMap = mapOf("key2" to 2, "key1" to 1, "key3" to 3, "key4" to 4, "key5" to 5)
    println("anotherMap == numberMap:${anotherMap == map}")
    anotherMap.equals(map)
    /**
     * Q2：两个具有相同元素，但顺序不同的list相等吗？为什么？
     */
}

fun collectionSort() {
    val number3 = mutableListOf(1, 2, 3, 4)
    number3.shuffle()//洗牌
    println(number3)
    number3.sort()
    println(number3)
    number3.sortDescending()
    println(number3)


    data class Language(var name: String, var score: Int)

    val languageList = mutableListOf<Language>()
    languageList.add(Language("Java", 80))
    languageList.add(Language("Kotlin", 90))
    languageList.add(Language("Dart", 99))
    languageList.add(Language("C", 80))
    //使用sortBy进行排序，适合单条件排序
    languageList.sortBy { it.score }
    println(languageList)
    languageList.sortWith(compareBy({ it.score }, { it.name }))
    println(languageList)
}

fun arrayType() {
    val array = arrayOf(1, 2, 3)
//    val arrayOfNulls = arrayOfNulls<Int>(3)
//    val array1 = Array(5) { i: Int -> (i * i).toString() }
//    val intArrayOf = intArrayOf(1, 2, 3)//[1,2,3]
//    val intArray = IntArray(3)//[0,0,0]
//    val intArray1 = IntArray(2) { 99 }//[99,99]
//    val intArray2 = IntArray(3) { it * 1 }
    for (item in array) {
        println(item)
    }
    for (i in array.indices) {
        println("$i=${array[i]}")
    }
    for ((index, item) in array.withIndex()) {
        println("$index:$item")
    }
    array.forEach { println(it) }
    array.forEachIndexed { index: Int, i: Int -> println("$index:$i") }
    array.forEachIndexed { index, i -> println("$index:$i") }
}

fun baseType() {
    val num1 = 1.68
    val num2 = 2
    val num3 = 2f
    val int1 = 3
    println("num1:$num1 num2:$num2 num3:$num3 int1:$int1")
    abs(num1)
    num1.toInt()
    printType(num1)
    printType(num2)
    printType(num3)
}

fun printType(param: Any) {
    println("$param is ${param::class.simpleName} type")
}

