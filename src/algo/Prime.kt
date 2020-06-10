package my.algo

fun calPrimes(range: Int): List<Int> {
    val primes = MutableList<Int>(0) { 0 }
    val visited = BooleanArray(range+1) { false }
    for (a in 2..range){
        if (!visited[a])
            primes.add(a)
        for (b in primes){
            val composite = a * b
            if (composite > range)
                break
            visited[composite] = true
            if (a % b == 0)
                break
        }
    }
    return primes.toList()
}

fun testEulerSift(){
    val prime10e7 = calPrimes(10_000_000)
    var i = 0
    while (i < prime10e7.size){
        print("${prime10e7[i++]} ")
        if (i % 100 == 0)
            println()
    }
}