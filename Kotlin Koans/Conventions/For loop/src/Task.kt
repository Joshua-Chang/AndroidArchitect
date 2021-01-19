class DateRange(val start: MyDate, val end: MyDate) : Iterator<MyDate> {
    operator fun iterator(): Iterator<MyDate> =DateRange(start, end)
    var current:MyDate=start
    override operator fun next():MyDate{
        val result=current
        current=current.followingDate()
        return result
    }

    override fun hasNext(): Boolean =current<=end

}

fun iterateOverDateRange(firstDate: MyDate, secondDate: MyDate, handler: (MyDate) -> Unit) {
    for (date in firstDate..secondDate) {
        handler(date)
    }
}