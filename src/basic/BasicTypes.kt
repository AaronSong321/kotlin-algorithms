package my.demo

fun arraysDemo(){
    var asc = Array(5) { i -> (i * i).toString() }
    asc.forEach {println(it)}
    asc.get(3)
    asc.set(7, "what")

    var intArray1 = intArrayOf(1,2,3)
    val intArray2 = arrayOf(10, 30)
    
}

