package my.algo.dp

import java.util.*

class NeatType(private val words: IntArray, private val m: Int){
    private val count = words.size
    private val length = Array<IntArray>(count) {
        IntArray(count) { inf }
    }
    private val cost = Array<IntArray>(count) {
        IntArray(count) { inf }
    }
    private val cutPosition = Array(count){
        IntArray(count) { dontCut }
    }
    private var text: Array<String>? = null

    constructor(words: IntArray, m: Int, texts: Array<String>): this(words, m){
        text = texts
    }

    companion object {
        private const val inf = Int.MAX_VALUE / 3
        private const val dontCut = -1
        private const val gibberish = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" // print these characters when no words are provided
    }

    fun solve(): Int {
        // calculate length of consecutive words
        for (i in 0 until count){
            for (j in 0 until count){
                length[i][j] =
                    if (i > j) inf
                    else if (i == j) words[i]
                    else length[i][j-1] + words[j] + 1
            }
        }

        // calculate cost of a line if possible (m >= characters needed)
        for (i in 0 until count){
            for (j in i until count){
                var extraSpace = m - length[i][j]
                if (extraSpace >= 0){
                    length[i][j] =
                        if (j == count-1) 0
                        else extraSpace * extraSpace * extraSpace
                }
                else
                    length[i][j] = inf
            }
        }

        // dp: cut words into sections
        for (chainLength in 1..count){
            for (start in 0..count-chainLength){
                val end = start + chainLength - 1
                cost[start][end] = length[start][end]

                var minimum = cost[start][end]
                for (cutPoint in start until end){ // cutPoint is placed in the former line
                    if (cost[start][cutPoint] != inf && cost[cutPoint+1][end] != inf){
                        var p = cost[start][cutPoint] + cost[cutPoint+1][end]
                        if (p < minimum){
                            minimum = p
                            cost[start][end] = p
                            cutPosition[start][end] = cutPoint
                        }
                    }
                }
            }
        }
        return cost[0][count-1]
    }

    private fun getWord(index: Int): String {
        return if (text != null) text!![index]
        else String(CharArray(words[index]) { gibberish[it % gibberish.length] })
    }

    fun printSolution(){
        var st = Stack<Pair<Int, Int>>()
        st.push(Pair(0, count-1))
        var ans = MutableList<String>(0) { "" }
        while (st.size > 0){
            var top = st.pop()
            val cutPoint = cutPosition[top.first][top.second]
            if (cutPoint == dontCut){
                var line = getWord(top.first)
                for (nextWord in top.first+1..top.second){
                    line += " ${getWord(nextWord)}"
                }
                ans.add(line)
            }
            else {
                st.push(Pair(top.first, cutPoint))
                st.push(Pair(cutPoint+1, top.second))
            }
        }
        ans.asReversed().forEach { println(it) }
    }
}

fun testNT(){
    val question = NeatType(intArrayOf(10, 15, 15, 20, 20, 20, 47, 9), 80)
    println(question.solve())
    question.printSolution()
}