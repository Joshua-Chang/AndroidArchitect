package hi.kotlin_dmeo

fun main() {
//    Coke().taste()
//    Coke().price(Sweet())
//    BlueColor(Blue()).printColor()
test12()
}

//范型接口
interface Drinks<T> {
    fun taste(): T
    fun price(t: T)
}

class Sweet {
    val price = 5
}

class Coke : Drinks<Sweet> {
    override fun taste(): Sweet {
        println("sweet")
        return Sweet()
    }

    override fun price(t: Sweet) {
        println("coke price=${t.price}")
    }
}

//范型类
abstract class Color<T>(var t: T/*范型字段*/) {
    abstract fun printColor()
}

class Blue {
    val color = "blue"
}

class BlueColor(b: Blue) : Color<Blue>(b) {
    override fun printColor() {
        println("color:${t.color}")
    }
}

//范型方法
fun <T> fromJson(json: String, tClass: Class<T>): T? {
    val t: T? = tClass.newInstance()
    return t
}

//范型约束
//extends
fun <T : Comparable<T>?> sort(list: List<T>?): Unit {}

fun test12() {
    sort(listOf(1, 2, 3))
    val listString= listOf("A","B","C")
    val list= test(listString,"B")
    println(list)
}

fun <T>test(list: List<T>,threshold:T):List<T>
where T:CharSequence,T:Comparable<T> {
    return list.filter { it>threshold }.map { it }
}

//范型out(extends) in(super)
fun sumOfList(list: List<out Number>):Number{
    var result=0f;
    for (number in list) {
        result+=number.toFloat()
    }
    return result
}