package hi.kotlin_dmeo

import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
fun main() {
//    println(functionLearn(101))
//    Person().test1()
//    Person.test2()
//    println(NumUtil.double(2))
//    test5()

//    val listOf = listOf(1, 2, 3)
//    val sum = listOf.sum { println("${it}") }
//    println(sum)
//    val listOf1 = listOf("2", "3", "4")
//    println(listOf1.toIntSum()(2))
//
//    testClosure(1)(2){
//        println(it)
//    }
//    test11()
//    literal()


//    foo(1) { println("foo") }
//    foo(quz = { println("foo") })
//    foo(bar = 2) { println("foo") }
//    foo { println("foo") }
//    foo2(bar = 1) { println("foo2") }
//    foo2(bar = 3,baz = 1) { println("foo2") }
//    foo2(1,2){}
//
//    //当调用函数，携带全部参数时，不会产生歧义
//    foo3(bar = 3,baz = 1) { println("foo3") }
//    foo3(1,2){println("foo3")}
//    //当调用函数，携带部分参数时，会产生歧义，不可省略参数名
//    foo3(bar = 2){println("foo3")}

    testHigher_order()
}


/**
 * 函数最后一个参数为表达式，有两种调用方式
 * 1、可省略小括号
 * 2、作为带名称的参数
 */
fun foo(bar: Int = 5, baz: Int = 9, quz: () -> Unit) {
    print("$bar \t $baz\n")
    quz()
}


fun foo2(bar: Int, baz: Int = 9, quz: () -> Unit) {
    quz()
}

/**
 * 调用有默认参数的函数时，默认参数可以省略;非默认参数绝对不能省略
 * case：当函数有多个参数，且默认参数在非默认参数之前，
 * 当调用函数，携带全部参数时，不会产生歧义，可以省略参数名
 * 当调用函数，携带部分参数时，会产生歧义，不可省略参数名
 */
fun foo3(baz: Int = 9, bar: Int, quz: () -> Unit) {
    quz()
}


fun testArgs() {
    testVararg('a')
    val world = charArrayOf('w', 'o', 'r', 'l', 'd')
    testVararg('h', 'e', 'l', 'l', 'o', ' ', *world)
    /**
     * 将已有数组当可变参数传入时要* 代表伸展操作符
     */
    testVararg(*world)
}

fun testVararg(vararg string: Char): String {
    val stringBuffer = StringBuffer()
    for (s in string) {
        stringBuffer.append(s)
    }
    return stringBuffer.toString()
}


fun functionLearn(days: Int): Boolean {
    return days > 100
}

class Person {
    fun test1() {
        println("成员方法")
    }

    //类方法类似与Java的类方法 通过半生对象
    companion object {
        fun test2() {
            println("类方法")
        }
    }
}

/**
 * object + 类名 ：静态类
 */
object NumUtil {
    fun double(num: Int): Double {
        return num.toDouble()
    }
}

fun test() {
    println("no param")
}

val test1 = { println("no param") }
fun test2(a: Int, b: Int): Int {
    return a + b
}

//：（参数类型）->返回类型={参数->函数题}
val test3: (Int, Int) -> Int = { a, b -> a + b }
val test4 = { a: Int, b: Int -> a + b }

@RequiresApi(Build.VERSION_CODES.N)
fun test5() {
    val arrayOf = arrayOf(1, 2, 3, 4, 5)
    println(arrayOf.filter { it > 3 }.component2())
    val map = mapOf("key1" to "value1", "key2" to "value2", "key3" to "value3", "key4" to "value4", "key5" to "value5")
    map.forEach { k, _ -> println(k) }
    map.forEach { (_, v) -> println(v) }
}

fun testHigher_order() {
    val list = listOf(1, 2, 3, 4, 5)
//    public inline fun println(message: Int) {
//        System.out.println(message)
//    }
    list.sum { println(it) }//当lambda作为参数，且只有一个参数时，可用it代替此参数
    val listString = listOf("1", "2", "3")
    val toIntSum = listString.toIntSum()(2)

}

/**
 * .sum扩展
 * 函数作为参数 callback:(Int)->Unit
 */
fun List<Int>.sum(callback: (Int) -> Unit): Int {
    var result = 0
    for (v in this) {
        result += v
        callback(v)
    }
    return result
}

/**
 * 函数作为返回值
 */
fun List<String>.toIntSum(): (scale: Int) -> Float {
    println("第一层函数")
    return fun(scale): Float {
        var result = 0f
        for (v in this) {
            result += v.toInt() * scale
        }
        return result
    }
}

fun testClosure(v1: Int): (v2: Int, (Int) -> Unit) -> Unit {
    return fun(v2, printer: (Int) -> Unit) {
        printer(v1 + v2)
    }
}

/**
 * 解构声明
 */
data class Result(val msg: String, val code: Int)

fun test11() {
    val result = Result("success", 200)
    val (msg, code) = result
    println(msg + code)
}

/**
 * 匿名方法
 * Anonymous
 */
val fun1 = fun(x: Int, y: Int): Int = x + y
val fun2 = fun(x: Int, y: Int): Int { return x + y }

/**
 * 方法字面值
 */
fun literal() {
    var temp: ((Int) -> Boolean)? = null //变量temp为(Int)->Boolean类型
    //{num->(num>10)}方法字面值
    temp = { num -> (num > 10) }
    println(temp(11))
}

/**
 * 比Java多出的 方法作用域：
 * 1、不需要创建一个类来保存一个方法
 * 2、也可以声明在局部作用域（方法内的方法）
 * 3、扩展方法
 */
fun magic(): Int {
    //外部方法
    fun foo(v: Int): Int {//局部方法可以访问外部方法（即闭包）的局部变量
        return v * v;
    }

    val v1 = (0..100).random()//局部变量
    return foo(v1)
}

val arr = arrayOf(1, 2, 3, 4, 5)
fun testIt() {
    arr.filter { it < 5 }.component1()
}

fun containsEven(collection: Collection<Int>): Boolean = collection.any { it / 2 > 0 }
