package hi.kotlin_dmeo

//for 可以循环遍历任何提供了迭代器的对象
class StudentScoreRange(override val start: Score, override val endInclusive: Score) : ClosedRange<Score> {
    //有一个成员函数或者扩展函数 iterator()，它的返回类型
    //返回类型有next、hasNext

    operator fun iterator(): Iterator<Score> {
        return object : Iterator<Score> {
            var current: Score = start
            override fun next(): Score {
                val result = current
                current = current.nextScore()
                return result
            }

            override fun hasNext(): Boolean {
                return current >= endInclusive
            }
        }
    }
}
private fun Score.nextScore(): Score {
    return when {
        math > 0 -> Score(CN, math - 1)
        else -> Score(CN - 1, 100)
    }
}

operator fun Score.rangeTo(score: Score): StudentScoreRange = StudentScoreRange(this, score)

class Score(val CN: Int, var math: Int) : Comparable<Score> {
    override fun compareTo(other: Score): Int {
        return when {
            CN == other.CN -> math - other.math
            else -> CN - other.CN
        }
    }
}


fun intervalScore(start: Score, end: Score, detail: (Score) -> Unit) {
    for (it in start..end) {
        detail(it)
    }
}



fun main() {
    intervalScore(Score(99, 10), Score(98, 90)) {
        println("${it.CN}-${it.math}")
    }
    println()
}