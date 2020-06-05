package my.algo.dp

class MatrixChainMultiplication(private val matrixChain: IntArray){
    private val chainLength = matrixChain.size
    private val matrixCount = matrixChain.size-1
    private var cost = Array<IntArray>(0) {IntArray(0) {0}}
    private var cutLocation = Array<IntArray>(0) {IntArray(0) {0}}
    fun solve(): Int{
        cost = Array<IntArray>(chainLength) {IntArray(chainLength) {Int.MAX_VALUE}}
        cutLocation = Array<IntArray>(chainLength) {IntArray(chainLength) { -1 }}
        for (i in 1..matrixCount){
            cost[i][i] = 0
        }
        for (curLength in 2..matrixCount){
            for (start in 1..matrixCount-curLength+1){
                val end = start + curLength - 1
                cost[start][end] = Int.MAX_VALUE
                for (cutPoint in start until end){
                    val cost1 = cost[start][cutPoint] + cost[cutPoint+1][end] + matrixChain[start-1]*matrixChain[cutPoint]*matrixChain[end]
                    if (cost1 < cost[start][end]){
                        cost[start][end] = cost1
                        cutLocation[start][end] = cutPoint
                    }
                }
            }
        }
        return cost[1][matrixCount]
    }

    private fun printOptimalParentesis(i: Int, j: Int){
        if (i == j){
            print("$i ")
        }
        else{
            print("(")
            printOptimalParentesis(i, cutLocation[i][j])
            printOptimalParentesis(cutLocation[i][j]+1, j)
            print(")")
        }
    }
    fun printSolution(){
        printOptimalParentesis(1, matrixCount)
        println()
    }
}

fun testMCM(){
    val a = MatrixChainMultiplication(intArrayOf(30, 35, 15, 5, 10, 20, 25))
    println(a.solve())
    a.printSolution()
}