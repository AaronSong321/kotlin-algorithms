package my.ds

import my.algo.commons.*
import java.util.*
import kotlin.math.*

class Flow(val n: Int) {
    private val capacity = Array(n) { DoubleArray(n) }
    private var flow = Array<DoubleArray>(0) {DoubleArray(0)}
    private var height: IntArray = IntArray(0)
    private var excess: DoubleArray = DoubleArray(0)
    private var seen: IntArray = IntArray(0)
    private var excessVertices: Queue<Int> = LinkedList<Int>()

    private fun push(u: Int, v: Int) {
        val d = min(excess[u], capacity[u][v] - flow[u][v])
        flow[u][v] += d
        flow[v][u] -= d
        excess[u] -= d
        excess[v] += d
        if (!d.approximate(0.0) || excess[v] == d)
            excessVertices.add(v)
    }

    private fun relabel(u: Int) {
        var d = IntInf
        for (i in 0 until n)
            if (capacity[u][i] - flow[u][i] > 0)
                d = min(d, height[i])
        if (d < IntInf)
            height[u] = d + 1
    }

    private fun discharge(u: Int) {
        while (excess[u] > 0) {
            if (seen[u] < n) {
                val v = seen[u]
                if (capacity[u][v] - flow[u][v] > 0 && height[u] > height[v])
                    push(u, v)
                else
                    ++seen[u]
            }
            else {
                relabel(u)
                seen[u] = 0
            }
        }
    }

    fun calc(s: Int = 0, t: Int = n-1): Double {
        height = IntArray(n) { if (it == s) n else 0 }
        flow = Array(n) { DoubleArray(n) }
        excess = DoubleArray(n) { if (it == s) Double.MAX_VALUE / 2 else 0.0 }
        seen = IntArray(n)
        excessVertices = LinkedList<Int>()

        for (i in 1 until n)
            push(s, i)
        while (!excessVertices.isEmpty()) {
            val u = excessVertices.remove()
            if (u != s && u != t)
                discharge(u)
        }

        var maxFlow = 0.0
        for (i in 0 until n)
            maxFlow += flow[0][i]
        return maxFlow
    }

    fun addEdge(u: Int, v: Int, c: Double) {
        if (capacity[u][v] != 0.0)
            throw IllegalArgumentException("capacity[$u][$v] = ${capacity[u][v]} != 0")
        if (c <= 0)
            throw IllegalArgumentException("$c <= 0")
        capacity[u][v] = c
    }
}

fun testFlow() {
    val g = Flow(6)
    g.addEdge(0, 1, 16.0)
    g.addEdge(0, 2, 13.0)
    g.addEdge(1, 2, 10.0)
    g.addEdge(2, 1, 4.0)
    g.addEdge(1, 3, 12.0)
    g.addEdge(2, 4, 14.0)
    g.addEdge(3, 2, 9.0)
    g.addEdge(3, 5, 20.0)
    g.addEdge(4, 3, 7.0)
    g.addEdge(4, 5, 4.0)
    println(g.calc(0, 5)) // should be 23
}