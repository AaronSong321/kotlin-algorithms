package my.algo.dp

class LongestCommonSubstring(private val a: String, private val b: String){
    private val len1 = a.length
    private val len2 = b.length
    private val num = Array<IntArray>(len1+1) { i ->
        if (i == 0) IntArray(len2+1) { 0 }
        else IntArray(len2+1) { 0 }
    }
    fun solve(): Int {
        for (i in 0 until len1){
            for (j in 0 until len2){
                if (a[i] == b[j]){
                    num[i+1][j+1] = num[i][j] + 1
                }
                else if (num[i][j+1] > num[i+1][j]){
                    num[i+1][j+1] = num[i][j+1]
                }
                else{
                    num[i+1][j+1] = num[i+1][j]
                }
            }
        }
        return num[len1][len2]
    }

    fun printSolution(){
        var ans = CharArray(num[len1][len2]) { '\u0000' }
        var index = num[len1][len2]-1
        var i = len1-1
        var j = len2-1
        while (i >= 0 && j >= 0){
            if (num[i+1][j] == num[i+1][j+1]){
                --j
            }
            else if (num[i][j+1] == num[i+1][j+1]){
                --i
            }
            else {
                ans[index--] = a[i]
                --i
                --j
            }
        }
        println(String(ans))
    }
}

fun testLCS(){
    var question = LongestCommonSubstring("accggtcgagtgcgcggaagccggccgaa".toUpperCase(), "gtcgttcggaatgccgttgctctgtaaa".toUpperCase())
    println(question.solve())
    question.printSolution()
}

class LongestSubPalindrome(private val s: String){
    private val delegateQuestion = LongestCommonSubstring(s, s.reversed())
    fun solve(): Int = delegateQuestion.solve()
    fun printSolution(){
        delegateQuestion.printSolution()
    }
}

fun testLSP(){
    val q1 = LongestCommonSubstring("racebcarz", "zracbecar")
    println(q1.solve())
    q1.printSolution()
    val question = LongestSubPalindrome("aicmbohttephobiaz")
    println(question.solve())
    question.printSolution()
}
