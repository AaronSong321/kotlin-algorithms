package my.algo.dp

import java.util.*

class RodCut(private val rodValue: IntArray, private val length: Int){
    private var maxValue: IntArray = IntArray(0)
    private var cutLocation: IntArray = IntArray(0)

    fun solve(): Int{
        maxValue = IntArray(length+1) { if (it < rodValue.size) rodValue[it] else 0 }
        cutLocation = IntArray(length+1) { 0 }
        for (j in 1..length){
            var value = Int.MIN_VALUE
            for (i in 1..j){
                val v = maxValue[i] + maxValue[j-i]
                if (value < v){
                    value = v
                    cutLocation[j] = i
                }
            }
            maxValue[j] = value
        }
        return maxValue[length]
    }

    fun printSolution(){
        var st = Stack<Int>()
        var ans = MutableList<Int>(0) { it }
        st.push(length)
        while (st.size > 0){
            var curL = st.pop()
            if (cutLocation[curL] != 0 && curL - cutLocation[curL] != 0){
                st.add(cutLocation[curL])
                st.add(curL - cutLocation[curL])
            }
            else{
                ans.add(curL)
            }
        }
        for (len in ans){
            print("$len ")
        }
        println()
    }
}

fun testRodCut(){
    val p1 = RodCut(intArrayOf(0, 1, 5, 8, 9, 10, 17, 17, 20, 24, 30), 25)
    println(p1.solve())
    p1.printSolution()

}