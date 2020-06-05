package basic
import java.io.File
import java.lang.ArithmeticException
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

data class Customer(val name: String, val email:String)

fun foo(a: Int = 0, b: String = ""){

}

fun filterAList(){
    val list = listOf(-1, 2, 18)
    val positives = list.filter { x -> x > 0 }
    val negatives = list.filter { it < 0 }
}

fun mapDemo(){
    val map = mapOf("a" to 1, "b" to 2, "c" to 3)
    println(map["key"])
    val map2 = mutableMapOf("a" to 1)
    map2["key"] = 7
    val p: String by lazy {
        "${map["a"]}"
    }
}

fun String.spaceToCamelCase(){

}

object Resource {
    val name = "Name"
}

fun ifNotNullShorthand(){
    val files = File("E:\\Aaron").listFiles()
    println(files?.size)
    println(files?.size?:"empty")
    val values = mapOf("email" to "22222@911.com", "email2" to "www.lenovo.com")
    val email = values["email"]?:throw IllegalStateException("Email is missing!")
    val emails = listOf("a.com", "b.com")
    val mainEmail = emails.firstOrNull()?:""
    val value = 7
    value?.let{
        println(value)
    }
    //val mapped = value?.let { transformValue(it) }?:defaultValue
}

fun transform(color: String): Int{
    return when (color){
        "Red"->0
        "Green"->1
        "Blue"->2
        else -> throw IllegalArgumentException("Invalid color param value $color")
    }
}

fun test(){
    val result = try{
        10
    } catch (e:ArithmeticException){
        throw IllegalStateException(e)
    }
    println("result is $result")
}

fun arrayOfMinusOnes(size: Int): IntArray{
    return IntArray(size).apply{
        fill(-1)
    }
}

fun theAnswer() = 42

class Turtle{
    fun penDown(){}
    fun penUp(){}
    fun turn(degrees: Double) {}
    fun forward(pixels: Double) {}
}
fun turtleMangle(){
    val myTurtle = Turtle()
    with(myTurtle){
        penDown()
        for (i in 1..4){
            forward(100.0)
            turn(90.0)
        }
        penUp()
    }
}

//val myRectangle: Rectangle().apply{
//    length = 4
//    breadth = 5
//    color = 0xFAFAFAFA
//}

fun nullableBoolean(){
    val b: Boolean? = true
    if (b == true){

    }else{

    }
}

fun swapVaraibles(){
    var a = 1
    var b = 2
    a = b.also { b = a }
}


