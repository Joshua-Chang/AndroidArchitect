package hi.kotlin_dmeo

import java.util.*
import kotlin.Comparator

fun main() {
    IntroducionStrings()
    IntroducionDataClasses()
    IntroducionSAM()
    ConventionsComparison()
    ConventionsInRange()
    ConventionsRangeTo()
    ConventionsForLoop()
}

fun ConventionsForLoop() {
    iterateOverDateRange(MyDate(2015, 5, 11), MyDate(2015, 5, 15)) { println(it) }
}

fun MyDate.nextDay(): MyDate {
    val c = Calendar.getInstance()
    c.set(year, month, dayOfMonth)
    c.add(Calendar.DAY_OF_MONTH, 1)
    return MyDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE))
}

fun iterateOverDateRange(firstDate: MyDate, secondDate: MyDate, handler: (MyDate) -> Unit) {
    for (date in firstDate..secondDate) {
        handler(date)
    }
}

fun ConventionsRangeTo() {
    println(checkInRange2(MyDate(2015, 5, 11), MyDate(2015, 1, 1), MyDate(2018, 5, 11)))
}

operator fun MyDate.rangeTo(other: MyDate) = DateRange2(this, other)

class DateRange2(override val start: MyDate, override val endInclusive: MyDate) : ClosedRange<MyDate>/*RangeTo*/, Iterable<MyDate>/*forLoop*/, Iterator<MyDate>/*forLoop*/ {
    //for 可以循环遍历任何提供了迭代器的对象：
    //有iterator()函数（成员函数：继承Iterable、自带 / 扩展函数）
    //有next函数（成员或扩展）
    //有hasNext函数（成员或扩展）返回Boolean
    //且三个函数都为operator
    override operator fun iterator(): Iterator<MyDate> = DateRange2(start, endInclusive)
    var current: MyDate = start
    override operator fun next(): MyDate {
        val result = current
        println("next:$result")
        current = current.nextDay()// 3
        return result// 2
    }
    // 1
    override operator fun hasNext(): Boolean {
        print("$current hasNext:->")
        return current <= endInclusive
    }


//        return object :Iterator<MyDate>{
//            var current: MyDate = start
//            override operator fun hasNext(): Boolean=current <= endInclusive
//            override operator fun next(): MyDate {
//                val result = current
//                current = current.nextDay()
//                return result
//            }
//        }
//
//        return DateIterator(this)
}

class DateIterator(val dateRange: DateRange2) : Iterator<MyDate> {
    var current: MyDate = dateRange.start
    override fun next(): MyDate {
        val result = current
        current = current.nextDay()
        return result
    }
    override fun hasNext(): Boolean = current <= dateRange.endInclusive
}

fun checkInRange2(date: MyDate, first: MyDate, last: MyDate): Boolean {
    return date in first..last
}


class DateRange(val start: MyDate, val endInclusive: MyDate) {
    operator fun contains(item: MyDate): Boolean = start <= item && item <= endInclusive
}

fun checkInRange(date: MyDate, first: MyDate, last: MyDate): Boolean {
    return date in DateRange(first, last)
}

fun ConventionsInRange() {
    println(checkInRange(MyDate(2015, 5, 11), MyDate(2015, 1, 1), MyDate(2018, 5, 11)))
}

data class Point(val x: Int, val y: Int) {
    fun line() {
        println("____")
    }
}

//一元运算
operator fun Point.unaryMinus() = Point(-x, -y)
operator fun Point.unaryPlus() = Point(+x, +y)
operator fun Point.not() = Point(y, x)
operator fun Point.inc() = Point(x + 1, y + 1);
operator fun Point.dec() = Point(x - 1, y - 1);

//二元运算
operator fun Point.plus(point: Point) = Point(point.x + x, point.y + y)
//operator fun Point.plus(dx:Int,dy:Int) = Point(dx + x, dy + y)
operator fun Point.minus(point: Point) = Point(point.x - x, point.y - y)

operator fun Point.invoke() = line()

operator fun Point.compareTo(point: Point): Int = x - point.x
var point = Point(10, 20)
var point2 = Point(0, 18)
fun ConventionsComparison() {
    println(-point)
    println(+point)
    println(!point)
    println(++point)
    println(--point)
    println(point++)
    println(point--)

    println(point + point2)
    point()
    println(point > point2)

    println(MyDate(1992, 11, 23) > MyDate(1992, 11, 25))
}

data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {
    //    override fun compareTo(other: MyDate): Int {
//        return if (year == other.year) {
//            if (month == other.month) {
//                dayOfMonth - other.dayOfMonth
//            } else {
//                month - other.month
//            }
//        } else {
//            year - other.year
//        }
//    }
    override fun compareTo(other: MyDate): Int {
        return when {
            year != other.year -> year - other.year
            month != other.month -> month - other.month
            else -> dayOfMonth - other.dayOfMonth
        }
    }
}

fun compare(date1: MyDate, date2: MyDate) = date1 < date2

interface IntPredicate {
    fun accept(int: Int): Boolean
}

fun IntroducionSAM() {
    val isEven = object : IntPredicate {
        override fun accept(int: Int): Boolean {
            return int % 2 == 0
        }
    }
    val runnable = Runnable { println("This runs in a runnable") }
}

fun IntroducionDataClasses() {

}

private fun IntroducionStrings() {
    val text = """
    |Tell me and I forget.
    |Teach me and I remember.
    |Involve me and I learn.
    |(Benjamin Franklin)
    """.trimMargin()
    print(text)

    val month = "(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)"

    fun getPattern(): String = """\d{2}\ ${month} \d{4}"""
    println("13 JUN 1992".matches(getPattern().toRegex()))
}

fun getList(): List<Int> {
    val arrayList = arrayListOf(1, 5, 2)
    Collections.sort(arrayList, object : Comparator<Int> {
        override fun compare(o1: Int, o2: Int): Int = o2 - o1
    })
    return arrayList
}