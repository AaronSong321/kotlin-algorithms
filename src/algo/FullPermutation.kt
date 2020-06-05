package my.algo

data class CantorExpansion(val constantGroup: IntArray){
    val power: Int = constantGroup.size - 1
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CantorExpansion

        if (!constantGroup.contentEquals(other.constantGroup)) return false

        return true
    }

    override fun hashCode(): Int {
        return constantGroup.contentHashCode()
    }

    fun getValue(): Int{
        var ans = 0
        for (index in 0..power){
            ans += Factorial[index] * constantGroup[index]
        }
        return ans
    }
}

object Factorial{
    private val values = mutableListOf<Int>(1)
    fun getFactorial(num: Int): Int{
        if (num >= values.size){
            for (index in values.size..num){
                values.add(values[index-1] * index)
            }
        }
        return values[num]
    }
    operator fun get(num: Int): Int{
        return getFactorial(num)
    }
}

class FullPermutation private constructor(val order: Int){
    var numbers = emptyList<Int>()
        private set
    var location = -1
        private set
    constructor(order: Int, numbers: List<Int>): this(order){
        this.numbers = numbers
        var sum = 1
        for (i in 0 until order+1){
            var count = 0
            for (j in i+1 until order+1){
                if (numbers[j] < numbers[i])
                    ++count
            }
            sum += count * Factorial[order-i]
        }
        location = sum
    }
    constructor(numbers: List<Int>): this(numbers.size - 1, numbers)
    constructor(order: Int, loc: Int): this(order){
        val vis = BooleanArray(order + 2) { false }
        val ans = MutableList<Int>(order + 1) { 0 }
        var k = loc - 1
        for (i in 0 until order+1){
            var t = k / Factorial[order - i]
            var j = 1
            while (j <= order+1){
                if (!vis[j]){
                    if (t == 0) break
                    --t
                }
                ++j
            }
            ans[i] = j
            vis[j] = true
            k %= Factorial[order-i]
        }
        numbers = ans.toList()
        location = loc
    }
    fun getNumbers(): String{
        var ans = ""
        for (n in numbers)
            ans += "$n "
        return ans
    }
}


fun testFP(){
    println(FullPermutation(listOf(3,5,7,4,1,2,9,6,8)).location)
    println(FullPermutation(4, 16).getNumbers())
}