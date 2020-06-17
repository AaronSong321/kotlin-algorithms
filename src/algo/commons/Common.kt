package my.algo.commons

import java.util.*
import kotlin.Comparator

fun max(arr: IntArray): Int{
    var ans = Int.MIN_VALUE
    for (n in arr){
        if (ans < n)
            ans = n
    }
    return ans
}
fun min(vararg a: Int): Int{
    var ans = Int.MAX_VALUE
    for (b in a){
        if (b < ans)
            ans = b
    }
    return ans
}

fun <T> getMinimumBy(collection: Collection<T>, num: Int, comparator: Comparator<T>): Collection<T> {
    val pq = PriorityQueue<T>(comparator.reversed())
    for ((index, e) in collection.withIndex()) {
        if (index < num) {
            pq.add(e)
        }
        else {
            if (comparator.reversed().compare(e, pq.peek()!!) > 0) {
                pq.poll()
                pq.add(e)
            }
        }
    }
    val ans = List<T>(num) { pq.poll()!! }
    return ans.reversed()
}

fun print(a: Array<*>) {
    print("[")
    for ((index, content) in a.withIndex())
        print(if (index == a.size-1) "$content]" else "$content, ")
}
fun println(a: Array<*>) {
    print(a)
    println()
}
fun print(a: IntArray) {
    print("IntArray[")
    for ((index, content) in a.withIndex())
        print(if (index == a.size-1) "$content]" else "$content, ")
}
fun println(a: IntArray) {
    print(a)
    println()
}
fun print(a: BooleanArray) {
    print("BooleanArray[")
    for ((index, content) in a.withIndex())
        print(if (index == a.size-1) "$content]" else "$content, ")
}
fun println(a: BooleanArray) {
    print(a)
    println()
}
fun print(a: DoubleArray) {
    print("DoubleArray[")
    for ((index, content) in a.withIndex())
        print(if (index == a.size-1) "$content]" else "$content, ")
}
fun println(a: DoubleArray) {
    print(a)
    println()
}

