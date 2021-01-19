package hi.kotlin_dmeo

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes

fun main() {
    val mutableListOf = mutableListOf(1, 2, 3)
    mutableListOf.swap(0, 2)
    println(mutableListOf)

    val mutableListOf1 = mutableListOf("A", "B", "C")
    mutableListOf1.swap1(0, 2)
    println(mutableListOf1)

    println("hello".lastChar)
    Jump.print("dfaj")
    testApply()
}

//扩展方法
fun MutableList<Int>.swap(index1: Int, index2: Int) {
    var temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
}

//范型
fun <T> MutableList<T>.swap1(index1: Int, index2: Int) {
    var temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
}

//扩展属性
val String.lastChar: Char get() = this.get(this.length - 1)

//伴生对象扩展
class Jump {
    companion object {}
}

fun Jump.Companion.print(string: String) {
    println("Jump.Companion.print:$string")
}

/*    常见扩展  */
/**
 * let
 */
fun testLet(string: String?) {
    string?.let {//避免null
        println(it.length)
    }
    string?.let {//限制作用域
        val str2 = "let"
        println(it + str2)
    }
}
/**
 * run
 */
data class Room(val address: String, val price: Float, val size: Float)

fun testRun(room: Room) {
    room.run { //不再需要room.
        println("Room:$address,$price,$size")
    }
}

/**
 * apply
 * 类似与Android的builder
 */

fun testApply() {
    ArrayList<String>().apply {//返回的仍为该类型的实例
        add("1")
        add("2")
        add("3")
        println("$this")
    }.let { println(it) }
}
/**---------案例:使用Kotlin扩展为控件绑定监听器减少模板代码---------**/
//为Activity添加find扩展方法，用于通过资源id获取控件
fun <T:View>Activity.find(@IdRes id:Int):T{
    return findViewById(id)
}
//为Int添加onClick扩展方法，用于为资源id对应的控件添加onClick监听
fun Int.onClick(activity: Activity,click:()->Unit){
    activity.find<View>(this).apply {
        setOnClickListener{
            click()
        }
    }
}

