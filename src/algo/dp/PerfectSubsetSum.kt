package my.algo.dp

// Given an array arr[] of integers and an integer K, the task is to print all subsets
// of the given array with the sum equal to the given target K.
class PerfectSubsetSum(private val arr: IntArray, private val sum: Int) {
    private val size = arr.size
    private val canReachSum = Array(size) {
        BooleanArray(sum+1) { j -> j == 0 }
    }
    fun solve() {
        if (size == 0 || sum < 0)
            return
        if (arr[0] <= sum)
            canReachSum[0][arr[0]] = true
        for (i in 1 until size)
            for (j in 0..sum)
                canReachSum[i][j] =
                    if (arr[i] <= j) canReachSum[i-1][j] || canReachSum[i-1][j-arr[i]]
                    else canReachSum[i-1][j]
        if (!canReachSum[size-1][sum]) {
            println("No solution")
            return
        }
        findPath(size-1, sum, mutableListOf())

    }

    private fun findPath(i: Int, partialSum: Int, path: MutableList<Int>) {
        if (i == 0 && partialSum != 0 && canReachSum[0][partialSum]){
            path.add(arr[i])
            printPath(path)
            return
        }
        if (i == 0 && partialSum == 0) {
            printPath(path)
            return
        }
        if (canReachSum[i-1][partialSum]) {
            val co = mutableListOf<Int>().apply { addAll(path) }
            findPath(i-1, partialSum, co)
        }
        if (partialSum >= arr[i] && canReachSum[i-1][partialSum-arr[i]]) {
            path.add(arr[i])
            findPath(i-1, partialSum-arr[i], path)
        }
    }

    private fun printPath(p: Collection<Int>) {
        p.forEach { print("$it ") }
        println()
    }
}

fun testPerfectSubsetSum() {
    val p1 = PerfectSubsetSum(intArrayOf(1, 2, 3, 4, 5), 10)
    p1.solve()
}