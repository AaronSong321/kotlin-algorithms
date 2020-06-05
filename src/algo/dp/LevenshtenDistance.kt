package my.algo.dp

import java.util.Collections.min

fun max(arr: IntArray): Int{
    var ans = Int.MIN_VALUE
    for (n in arr){
        if (ans < n)
            ans = n
    }
    return ans
}
fun min(vararg a: Int): Int{
    var ans = Int.MAX_VALUE
    for (b in a){
        if (b < ans)
            ans = b
    }
    return ans
}

class LevenshtenDistance(val source: String, val dest: String, val cost: IntArray){
    enum class Operation{
        None,
        Copy,
        Replace,
        Delete,
        Insert,
        Twiddle,
        Kill
    }

    // partial init here
    private var minCost = Array<IntArray>(source.length+1) { i ->
        if (i == source.length) IntArray(dest.length+1) { j -> (dest.length - j) * cost[Operation.Insert.ordinal] }
        else IntArray(dest.length+1) { Int.MAX_VALUE }
    }
    private var operation = Array<Array<Operation>>(source.length+1) { i ->
        if (i == source.length) {
            Array(dest.length+1) { j ->
                if (j == dest.length) Operation.None
                else Operation.Insert
            }
        }
        else {
            Array(dest.length+1) { Operation.None }
        }
    }

    fun solve(): Int{
        // init
        for (s in source.length downTo 0 step 1){
            var kil = cost[Operation.Kill.ordinal]
            var del = cost[Operation.Delete.ordinal] * (source.length-s)
            operation[s][dest.length] =
                if (kil < del) Operation.Kill
                else Operation.Delete
            minCost[s][dest.length] = min(kil, del)
        }
        minCost[source.length][dest.length] = 0
        //init finished

        for (s in source.length-1 downTo 0 step 1){
            for (d in dest.length-1 downTo 0 step 1){
                var cop = Int.MAX_VALUE
                var rep = Int.MAX_VALUE
                var twi = Int.MAX_VALUE
                // kill operation is only used in init when s != source.length d == dest.length

                if (source[s] == dest[d]){
                    cop = cost[Operation.Copy.ordinal] + minCost[s+1][d+1]// index "source" begins at 1, but the string begins at 1, so s-1
                    // d-1 means the same
                }
                if (s < source.length-1 && d < dest.length-1 && source[s] == dest[d+1] && source[s+1] == dest[d]){
                    twi = cost[Operation.Twiddle.ordinal] + minCost[s+2][d+2]
                }
                if (source[s] != dest[d]){
                    rep = cost[Operation.Replace.ordinal] + minCost[s+1][d+1]
                }
                val ins = cost[Operation.Insert.ordinal] + minCost[s][d+1]
                val del = cost[Operation.Delete.ordinal] + minCost[s+1][d]
                var minimum = min(del, cop, rep, ins, twi)
                if (s >= 4 && d >= 4)
                    println()
                // the following binary expression is guaranteed to be true
                // if (minimum < minCost[s][d])
                minCost[s][d] = minimum
                operation[s][d] = when(minimum){
                    del -> Operation.Delete
                    cop -> Operation.Copy
                    rep -> Operation.Replace
                    ins -> Operation.Insert
                    twi -> Operation.Twiddle
                    else -> Operation.None
                }
            }
        }
        return minCost[0][0]
    }

    fun printSolution(){
        var s = 0
        var d = 0

        var totalCost = minCost[0][0]
        while (s <= source.length && d <= dest.length){
            if (s == source.length && d == dest.length)
                break
            var c = " (line $s, row $d): total cost ${totalCost - minCost[s][d] + cost[operation[s][d].ordinal]}"
            if (s < source.length && d < dest.length && operation[s][d] == Operation.Twiddle){
                println("Twiddle ${source[s]} with ${source[s+1]}$c")
                s += 2
                d += 2
            }
            else {
                var i = false
                println(when(operation[s][d]) {
                    Operation.Replace -> "Replace ${source[s++]} with ${dest[d++]}$c"
                    Operation.Insert -> "Insert ${dest[d++]}$c:"
                    Operation.Delete -> "Delete ${source[s++]}$c"
                    Operation.Copy -> "Copy ${source[s++]}$c"
                    Operation.Kill -> {
                        s = source.length
                        "Kill$c"
                    }
                    else -> "Nop"
                })
                if (i)
                    break
            }
        }
    }
}

fun testLD(){
    val question = LevenshtenDistance("acgatttt", "caag", intArrayOf(0,0,3,4,4,3,5))
    println(question.solve())
    question.printSolution()
}