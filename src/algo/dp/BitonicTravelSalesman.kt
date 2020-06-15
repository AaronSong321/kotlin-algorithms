package my.algo.dp

import kotlin.math.*

data class Ordinate(val x: Double, val y: Double){

}
fun eulerDistance(a: Ordinate, b: Ordinate): Double{
    var x1 = (a.x-b.x) * (a.x-b.x)
    var x2 = (a.y-b.y) * (a.y-b.y)
    return sqrt(x1+x2)
}

class BitonicTravelSalesman(private val vertexes: Array<Ordinate>){
    init {
        vertexes.sortBy { it.x }
    }
    private val order = vertexes.size
    private val distance = Array<DoubleArray>(order) { i -> DoubleArray(order) { j -> eulerDistance(vertexes[i], vertexes[j]) } }
    private val cost = Array<DoubleArray>(order) { DoubleArray(order) { Double.MAX_VALUE } }
    private val cut = Array(order) { IntArray(order) { -1 } }

    fun solve(): Double {
        cost[0][0] = 0.0
        cost[1][0] = distance[1][0]
        for (i in 1 until order){
            for (j in 0..i){
                if (j == i-1){
                    for (k in 0 until j){
                        var v = cost[j][k] + distance[i][k] // i to 1, 1 to k, direct k->j
                        if (v < cost[i][j]){
                            cost[i][j] = v
                            cut[i][j] = k
                        }
                    }
                }
                else if (j == i){
                    for (k in 0 until j){
                        var v = cost[i][k] + distance[i][k] // i to 1, 1 to k, direct k->i
                        if (v < cost[i][j]){
                            cost[i][j] = v
                            cut[i][j] = k
                        }
                    }
                }
                else{
                    cost[i][j] = cost[i-1][j] + distance[i-1][i]
                }
            }
        }
        return cost[order-1][order-1]
    }

    private fun p(from: Int, to: Int){
        if (from == 1 && to == 0){
            println("1 -> 0")
        }
        else if (to == from-1){
            var k = cut[from][to]
            p(from, k)
            println("$k -> $to")
        }
        else if (to == from){
            var k = cut[from][to]
            p(from, k)
            println("$k -> $to")
        }
        else{
            println("$from -> ${from-1}")
            p(from-1, to)
        }
    }
    fun printSolution() {
        p(order-1, order-1)
        TODO("not finished yet")
    }
}

fun testBTS(){
    val question = BitonicTravelSalesman(arrayOf(Ordinate(0.0,6.0), Ordinate(1.0,0.0), Ordinate(2.0,3.0),Ordinate(5.0,4.0), Ordinate(6.0,1.0), Ordinate(7.0,5.0), Ordinate(8.0,2.0)))
    println(question.solve())
    question.printSolution()
}