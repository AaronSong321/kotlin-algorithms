package my.algo.dp

class StringSplitWithDuplication(private val l: Int, private val cutLocation: List<Int>){
    private val substringCount = cutLocation.size+1
    private val cutCount = cutLocation.size
    private val cost = Array(substringCount) { i -> IntArray(substringCount) { j ->
        if (i == j) 0
        else if (i+1 == j) getSubstringLength(i, j)
        else Int.MAX_VALUE
    } }
    private val cutSolution = Array(substringCount) { IntArray(substringCount) { -1 } }
    private fun getSubstringLength(from: Int, to: Int): Int =
        getLengthBeforeCutLocation(to) - getLengthBeforeCutLocation(from-1)
    private fun getLengthBeforeCutLocation(cutIndex: Int) : Int =
        if (cutIndex == cutCount) l
        else if (cutIndex == -1) 0
        else cutLocation[cutIndex]

    fun solve(): Int {
        for (chainLength in 2..substringCount){
            for (start in 0..substringCount-chainLength){
                val end = start + chainLength - 1
                for (cutPoint in start until end){
                    var t = cost[start][cutPoint] + cost[cutPoint+1][end] + getSubstringLength(start, end)
                    if (t < cost[start][end]){
                        cost[start][end] = t
                        cutSolution[start][end] = cutPoint
                    }
                }
            }
        }
        return cost[0][substringCount-1]
    }

    private fun p(start: Int, end: Int){
        if (start == end){
            print("$start")
        }
        else if (start+1 == end){
            print("($start $end)")
        }
        else{
            var cutPoint = cutSolution[start][end]
            print("(")
            p(start, cutPoint)
            p(cutPoint+1, end)
            print(")")
        }
    }
    fun printSolution(){
        p(0, substringCount-1)
        println()
    }
}

fun testSSWD(){
    val question = StringSplitWithDuplication(20, listOf(2, 8, 10))
    println(question.solve())
    question.printSolution()
}