package my.algo.dp

import my.ds.*

// g is list of pairs of (weight, value)
class KnapsackProblem(private val g: List<Pair<Int, Int>>, private val weight: Int) {
    private val n = g.size
    private val table = Matrix(n+1, weight+1)
    fun solve(): Int {
        for (i in 1..n) {
            for (w in 1..weight) {
                table[i, w] = when {
                    g[i-1].first <= w -> {
                        val takeItemI = g[i-1].second + table[i-1, w-g[i-1].first]
                        val dontTakeItemI = table[i-1, w]
                        if (takeItemI < dontTakeItemI) dontTakeItemI
                        else takeItemI
                    }
                    else -> table[i-1, w]
                }
            }
        }
        return table[n, weight]
    }

    fun printSolution() {
        val c = mutableListOf<Int>()
        var b = weight
        for (a in n downTo 1) {
            if (table[a, b] != table[a-1, b]) {
                c.add(a-1)
                b -= g[a-1].first
            }
        }
        println(c.asReversed())
    }
}

fun testKnapsack() {
    // test data copied from https://en.wikipedia.org/wiki/Knapsack_problem
    // answer should be 1270
    val g1 = listOf(23 to 505, 26 to 352, 20 to 458, 18 to 220, 32 to 354,
    27 to 414, 29 to 498, 26 to 545, 30 to 473, 27 to 543)
    val k1 = KnapsackProblem(g1, 67)
    println(k1.solve())
    k1.printSolution()
}