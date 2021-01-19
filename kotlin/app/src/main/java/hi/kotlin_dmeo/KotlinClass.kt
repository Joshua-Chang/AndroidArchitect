package hi.kotlin_dmeo

import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList

fun main() {
//    Dog(11)
//    printType(Shop().isClose)
//    val test = Test()
//    test.setup()
//    test.test()

//    testStudent()
    val initDemo = InitDemo("Tom")
    val initDemo2 = InitDemo(2, "Jim")
    val childDemo2 = ChildDemo2("aa")
    ChildDemo3("Lucy")

    val address = Address("BeiJin", 101)
    var (name, code) = address
    println(address.component1() == name)
    println(address.component2() == code)
}

/**
 * 主构造方法
 */
class KotlinClass /*constructor可以省略*/(name: String) {
    constructor(view: TextView, name: String) : this(name) {
        println("次构造方法，必须先调用主构造方法初始化。$name")
    }

    constructor(view: TextView, name: String, index: Int) : this(name) {
        println("$name")
    }
}

/**
 * 类默认是final的 ,
 * 类open才可以被继承
 * open-override情况下成员父子覆盖，否则不能父子成员同名
 * 方法open才能被复写 override
 * 变量open才能被修改 override
 */
open class Animal(age: Int) {
    init {//初始化代码块
        println("init " + age)
    }

    open fun eat() {}
    open val foot: Int = 0
}

class Dog/*(age: Int)*/ : Animal/*(age)*/ {
    constructor(age: Int) : super(age)

    override fun eat() {
        super.eat()
    }

    override val foot = 4

    //val声明final变量，1声明为空 2设置getter 3在构造方法内初始化
//    val simple:Int?=null
    val simple: Int?
        get() {
            return 0
        }
}

class Shop {
    val name: String = "Android"
    var address: String? = null
    val isClose: Boolean
        get() = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 11

    /**
     * get field set value
     */
    var score: Float = 0.0f
        //val不允许set var才可以
        get() = if (field < 0.2f) 0.2f else field * 1.5f
        set(value) = println(value)
    var counter = 0
        set(value) {
            if (value > 0) field = value
        }

}

/**
 * lateinit 对属性的延迟初始化 常用于注入
 */
class Test {
    lateinit var shop: Shop
    fun setup() {
        shop = Shop()
        shop.address = "Beijing"
    }

    fun test() {
//        ::表示创建成员或者类的引用
        if (::shop.isInitialized) println(shop.address)
    }
}

abstract class Printer {
    abstract fun print()
}

class FilePrinter : Printer() {
    override fun print() {
        println("file printer")
    }
}

interface Study {
    val time: Int//变量也需要在 子类构造方法声明 或复写getter
    fun discuss()
    fun learnCourses() {//接口方法可以有自己的实现
        println("abc")
    }
}

class StudyAS : Study {
    override val time: Int
        get() = TODO("Not yet implemented")

    override fun discuss() {
        TODO("Not yet implemented")
    }
}

interface A {
    fun foo() {
        println("A")
    }
}

interface B {
    fun foo() {
        println("B")
    }
}

/**
 * 多实现
 */
class D : A, B {
    override fun foo() {
        super<A>.foo()//用于区分
    }
}

/**
 * 数据类 1、主构造方法必须有参数 2、不能open、不能抽象类、不能继承
 */
data class Address(val name: String, val number: Int) {
    var city: String = ""
    fun print() {
        println(city)
    }
}

open class Address2(name: String) {
    open fun print() {

    }
}

class Shop2 {
    var address: Address2? = null
    fun addAddress(address2: Address2) {
        this.address = address2
    }
}

/**
 * 对象表达式
 */
fun test3() {
    //如果超类型有一个构造方法，则必须传递适当的构造方法参数给它
    Shop2().addAddress(object : Address2("Android") {
        //对象表达式
        override fun print() {
            super.print()
        }
    })
    //只需要一个对象 不需要一个新的类
    val adHoc = object {
        var x: Int = 0
        var y: Int = 0
    }
    println(adHoc.x + adHoc.y)
}

object DataUtil {
    fun <T> isEmpty(list: ArrayList<T>?): Boolean {
        return list?.isEmpty() ?: false
    }
}

fun testDataUtil() {
    val list = arrayListOf("1")
    println(DataUtil.isEmpty(list))
}

/**
 * 伴生对象
 */
class Student(val name: String) {
    companion object {
        val student = Student("android")
        fun study() {
            println("English")
        }
    }
}

fun testStudent() {
    println(Student.student)
    Student.study()
}

//在构造函数前面有注解或可见行修饰符时，constructor不可省略
class Customer public constructor(name: String) { /*……*/ }

/**
 * 主构造方法：是类头的一部分、在类名后、不含代码 可省略constructor
 * 主构造方法的参数可以：1、val/var声明属性 2、在init中调用
 * init可有多个，实例初始化时按顺序执行
 * 初始化块中的代码实际上是主构造方法的一部分，优于次构造方法执行
 */
//class InitDemo constructor(name: String){
//class InitDemo(name: String){
open class InitDemo(val name: String) {
    init {
        println("hhah")
    }

    val firstProp = "First Prop: $name".also { println(it) }

    init {
        println("First Initializer block that prints $name")
    }

    val secondProp = "Second Prop:${name.length}".also(::println)

    init {
        println("Second Initializer block that prints ${name.length}")
    }

    /**
     * 当一个类已经有主构造方法时，每个次构造方法都必须和主构造方法存在委托或间接委托关系
     * 使用this来委托到相应的构造方法
     */
    constructor(index: Int, name: String) : this(name) {
        println(name)
    }
}

open class InitDemo2 {}

//1、子类没有主构造方法 ：父类没有
class ChildDemo1 : InitDemo {
    constructor(name: String) : super(name)
}

//2、子类有主构造方法 ：父类
class ChildDemo2(name: String) : InitDemo(name) {
}

class ChildDemo3(name: String) : InitDemo2() {
}

class Client(val personalInfo: PersonalInfo?)
class PersonalInfo(val email: String?)
interface Mailer {
    fun sendMessage(email: String, message: String)
}

fun sendMessageToClient(
        client: Client?, message: String?, mailer: Mailer
) {
    val email = client?.personalInfo?.email ?: return
    message?.let { mailer.sendMessage(email, it) }
}

data class RationalNumber(val numerator: Int, val denominator: Int)

fun Int.r(): RationalNumber {
    return RationalNumber(this, 1)
}
fun Pair<Int,Int>.r():RationalNumber=RationalNumber(first,second)
