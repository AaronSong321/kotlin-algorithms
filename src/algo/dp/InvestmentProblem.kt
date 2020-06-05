package my.algo.dp

// introductions to algorithms, 15-10

class InvestmentProblem(private val rate: List<List<Double>>, private val capital: Double
                        , private val feeWithoutTransfer: Double, private val feeWithTransfer: Double){
    private val yearCount = rate.size
    private val investmentCount = if (rate.isNotEmpty()) rate[0].size else 0
    private val interest = Array<DoubleArray>(yearCount){ i -> DoubleArray(investmentCount) { j ->
        if (i == 0) capital * rate[0][j]
        else 0.0
    } }
    private val solution = Array<IntArray>(yearCount) { i -> IntArray(investmentCount) { j ->
        if (i == 0) j
        else -1
    } }
    private var indexOfSolutionLastYear = -1

    init{
        if (yearCount == 0){
            println("year count = 0")
        }
        if (feeWithTransfer <= feeWithoutTransfer){
            println("transfer fee is no larger than non-transfer fee")
        }
    }
    fun solve(): Double {
        for (year in 1 until yearCount){
            // this is done in O(investCount) rather than O(1)
            val maxCapitalOfLastYear = interest[year-1].max()!!
            val capitalIndex = interest[year-1].indexOf(maxCapitalOfLastYear)
            for (invest in 0 until investmentCount){
                // It doesn't matter if invest == capitalIndex
                val noTransfer = rate[year][invest] * interest[year-1][invest] - feeWithoutTransfer
                val withTransfer = rate[year][invest] * interest[year-1][capitalIndex] - feeWithTransfer
                if (noTransfer < withTransfer){
                    interest[year][invest] = withTransfer
                    solution[year][invest] = capitalIndex
                }
                else{
                    interest[year][invest] = noTransfer
                    solution[year][invest] = invest
                }
            }
        }
        val ans = interest[yearCount-1].max()!!
        indexOfSolutionLastYear = interest[yearCount-1].indexOf(ans)
        return ans
    }

    private fun p(year: Int, invest: Int){
        if (year != 0){
            val capitalIndexOfLastYear = solution[year][invest]
            p(year-1, capitalIndexOfLastYear)
        }
        print("$invest ")
    }
    fun printSolution(){
        p(yearCount-1, indexOfSolutionLastYear)
        println()
    }
}

fun testIP(){
    val question = InvestmentProblem(listOf(listOf(1.0,1.1,1.2), listOf(1.01,1.09,0.98),listOf(1.15,1.07,1.12))
        , 10000.0, 100.0, 960.0)
    println(question.solve())
    question.printSolution()
    val q2 = InvestmentProblem(listOf(listOf(1.0,1.1,1.2), listOf(1.01,1.09,0.98),listOf(1.15,1.07,1.12))
        , 10000.0, 100.0, 980.0)
    println(q2.solve())
    q2.printSolution()
}