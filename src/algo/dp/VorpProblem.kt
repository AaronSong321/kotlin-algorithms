package my.algo.dp

// introductions to algorithms, 15-12
// this is a modified (means easier) backpack problem

class VorpProblem(private val positionCount: Int, private val playerCount: Int, private val capital: Int
                  , private val cost: List<List<Int>>, private val vorp: List<List<Int>>){
    private val curVorp = Array<IntArray>(positionCount) { IntArray(capital+1) { 0 } }
    private val playerChosen = Array<IntArray>(positionCount) { IntArray(capital+1) { -1 } }
    fun solve(): Int {
        for (moneyLeft in 0..capital){
            for (player in 0 until playerCount){
                if (cost[0][player] <= moneyLeft && curVorp[0][moneyLeft] < vorp[0][player]){
                    curVorp[0][moneyLeft] = vorp[0][player]
                    playerChosen[0][moneyLeft] = player
                }
            }
        }
        for (position in 1 until positionCount){
            for (moneyLeft in 0..capital){
                curVorp[position][moneyLeft] = curVorp[position-1][moneyLeft]
                for (player in 0 until playerCount){
                    if (cost[position][player] <= moneyLeft){
                        val newVorp = curVorp[position-1][moneyLeft - cost[position][player]] + vorp[position][player]
                        if (newVorp > curVorp[position][moneyLeft]){
                            curVorp[position][moneyLeft] = newVorp
                            playerChosen[position][moneyLeft] = player
                        }
                    }
                }
            }
        }
        return curVorp[positionCount-1][capital]
    }

    private fun p(position: Int, moneyLeft: Int){
        val player = playerChosen[position][moneyLeft]
        if (position != 0){
            p(position-1, moneyLeft - cost[position][player])
        }
        print("$player ")
    }
    fun printSolution(){
        p(positionCount-1, capital)
    }
}

fun testVorp(){
    val q1 = VorpProblem(3, 3, 32
        , listOf(listOf(10,11,12),listOf(9,8,10),listOf(13,14,13))
        , listOf(listOf(7,8,9),listOf(7,8,9),listOf(7,8,9)))
    println(q1.solve())
    q1.printSolution()
}