package my.algo

data class Match(val location: Int){
    override fun toString(): String {
        return location.toString();
    }
}

class MatchGroup(val locations: MutableList<Match>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return locations != (other as MatchGroup).locations
    }

    override fun hashCode(): Int {
        return locations.hashCode()
    }

    override fun toString(): String {
        var ans = "MatchGroup["
        var i = 0
        while (i < locations.size){
            ans += locations[i]
            if (i != locations.size - 1){
                ans += ", "
            }
            ++i
        }
        ans += "]"
        return ans
    }
    val getMatchCount: Int = locations?.size?:0
}

class KmpMatch(val pattern: String, val text: String){
    private val next = IntArray(pattern.length + 1)
    //private val matchNum = IntArray(text.length)
    private val result = MatchGroup(mutableListOf<Match>())

    private fun calNext(){
        val patternLength = pattern.length
        next[0] = -1
        var k = -1
        var j = 0
        while (j < patternLength - 1){
            if (k == -1 || pattern[j] == pattern[k]){
                ++j
                ++k
                if (pattern[j] == pattern[k]){
                    next[j] = next[k]
                } else {
                    next[j] = k
                }
            }
            else{
                k = next[k]
            }
        }
        println(next.contentToString())
    }

    private fun calResult(){
        val len1 = text.length
        val len2 = pattern.length
        var i = 0
        var j = 0
        while (i < text.length && j < pattern.length){
            if (j == -1 || text[i] == pattern[j]) {
                ++i
                ++j
                if (j == pattern.length){
                    result.locations.add(Match(i-j))
                    j = next[j]
                }
            } else {
                j = next[j]
            }
        }
    }

    init{
        calNext()
        calResult()
    }

    fun getResult(): String = result.toString()
}

fun testKMP(){
    val pattern = "abcdeabcf"
    val target = "abcabcabcdeabcfabcdeabcf"
    println(KmpMatch(pattern, target).getResult())
}