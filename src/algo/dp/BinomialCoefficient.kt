package my.algo.dp

import my.algo.commons.min

// A binomial coefficient C(n, k) can be defined as the coefficient of X^k in the expansion of (1 + X)^n.
// A binomial coefficient C(n, k) also gives the number of ways, disregarding order, that k objects can be chosen from among n objects;
// more formally, the number of k-element subsets (or k-combinations) of an n-element set.

//   C(n, k) = C(n-1, k-1) + C(n-1, k)
//   C(n, 0) = C(n, n) = 1
fun binomialCoefficient(n: Int, k: Int): Int {
    val c = IntArray(k+1) { if (it == 0) 1 else 0 }
    for (i in 1..n) {
        for (j in min(i, k) downTo 0) {
            c[j] += c[j-1]
        }
    }
    return c[k]
}