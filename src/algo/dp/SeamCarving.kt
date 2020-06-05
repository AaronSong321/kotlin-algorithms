package my.algo.dp

import java.util.*

class SeamCarving(private val m: Int, private val n: Int, private val damage: Array<IntArray>){
    private val cost = Array(m) { IntArray(n) { Int.MAX_VALUE } }
    private val direction = Array(m) { IntArray(n) { -2 } }
    private var indexOfSolutionLastRow = -1
    private var minCostValue = Int.MAX_VALUE

    fun solve(): Int{
        for (col in 0 until n){
            cost[0][col] = damage[0][col]
        }
        for (row in 1 until m){
            for (col in 0 until n){
                var leftCost = if (col != 0) cost[row-1][col-1] + damage[row][col] else Int.MAX_VALUE
                var rightCost = if (col != n-1) cost[row-1][col+1] + damage[row][col] else Int.MAX_VALUE
                var middleCost = cost[row-1][col] + damage[row][col]
                if (rightCost < cost[row][col]){
                    cost[row][col] = rightCost
                    direction[row][col] = 1
                }
                if (leftCost < cost[row][col]){
                    cost[row][col] = leftCost
                    direction[row][col] = -1
                }
                if (middleCost < cost[row][col]){
                    cost[row][col] = middleCost
                    direction[row][col] = 0
                }

                if (row == m-1 && cost[row][col] < minCostValue){
                    minCostValue = cost[row][col]
                    indexOfSolutionLastRow = col
                }
            }
        }
        return minCostValue
    }

    fun printSolution(){
        var st = Stack<Pair<Int, Int>>()
        var col = indexOfSolutionLastRow
        for (row in m-1 downTo 1){
            st.push(Pair(row, col))
            col += direction[row][col]
        }
        st.push(Pair(0, col))
        var cost = 0
        while (st.size > 0){
            val p = st.pop()
            cost += damage[p.first][p.second]
            println("[${p.first}][${p.second}]: $cost")
        }
    }
}

fun testSC(){
    val question = SeamCarving(3, 7, arrayOf(
        intArrayOf(7,6,5,4,3,2,1),
        intArrayOf(10,12,14,16,18,20,22),
        intArrayOf(90,50,70,40,80,60,50)
    ))
    println(question.solve())
    question.printSolution()
}