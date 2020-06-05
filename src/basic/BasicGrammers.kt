package my.demo

import kotlin.text.*

fun sum(a: Int, b: Int): Int {
    return a + b
}
fun sum2(a: Int, b: Int) = a + b
fun sum3(a: Int, b: Int): Unit {
    println("sum of $a and $b is ${a + b}")
}

var a: Int = 1
var b = 2
var x = 5

fun incrementX() {
    x += 1
}

fun strignTemplates(){
    var a = 1
    var s1 = "a is $a"
    a = 2
    var s2 = "${s1.replace("is", "was")}, but now is $a"
}

fun maxOf(a: Int, b: Int): Int {
    if (a > b) {
        return a
    } else {
        return b
    }
}

fun maxOf2(a: Int, b: Int) = if (a > b) a else b

fun parseInt(str: String): Int? {
    return 1
}

fun printProduct(arg1: String, arg2: String) {
    val x = parseInt(arg1);
    val y = parseInt(arg2);
    if (x != null && y != null){
        println(x * y)
    }
    else{
        println("'$arg1' or '$arg2' is not a number")
    }
}

fun getStringLength(obj: Any): Int? {
    if (obj is String){
        return obj.length
    }
    return null
}
fun getStringLength2(obj: Any): Int? = if (obj is String && obj.length > 0) obj.length else null

// for loop
fun loopDemo(){
    val items = listOf("apple", "banana", "kiwifruit")
    for (item in items){
        println(item)
    }
    for (index in items.indices){
        println("item at $index is ${items[index]}")
    }
}

// while loop
fun whileLoopDemo(){
    val items = listOf("apple", "banana", "kiwifruit")
    var index = 0
    while (index < items.size){
        println("item at $index is ${items[index]}")
        ++index
    }
}

// ranges
fun rangesDemo(){
    val x = 10
    val y = 9
    if (x in 1..y+1){
        println("fits in range")
    }

    val list = listOf("a", "b", "c")
    if (-1 !in 0..list.lastIndex){
        println("-1 is out of range")
    }
    if (list.size !in list.indices){
        println("List size is out of valid list indices range, too")
    }

    if (x in 1..5){
        println(x)
    }

    for (x in 1..10 step 2){
        print(x)
    }
    println()
    for (x in 9 downTo 0 step 3){
        print(x)
    }
}

fun collectionsDemo(){
    val items = listOf("closet", "apple")
    for (item in items){
        println(item)
    }

    when{
        "orange" in items -> println("juicy")
        "apple" in items -> println("apple is fine too")
    }

    val fruits = listOf("banana", "avocado", "apple", "kiwifruit")
    fruits
            .filter { it.startsWith("a") }
            .sortedBy { it }
            .map { it.toUpperCase() }
            .forEach { println(it) }
}

