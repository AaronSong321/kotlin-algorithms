package my.ds

import java.util.*


fun <T> GraphList<T>.partitionBoxers(): Pair<List<Int>, List<Int>>? {
    val color = IntArray(vertexCount) { 0 }
    val queue = LinkedList<Int>()

    val bv = lambda1@ fun(u: Int): Boolean {
        queue.push(u)
        while (!queue.isEmpty()){
            val t = queue.pop()
            for (edge in nodes[t]) {
                val v = edge.to
                if (color[v] == 0){
                    color[v] = if (color[u] == 1) 2 else 1
                    queue.push(v)
                }
                else if (color[v] == color[u])
                    return@lambda1 false
            }
        }
        return@lambda1 true
    }
    for (u in 0 until vertexCount) {
        if (color[u] == 0) {
            color[u] = 1
            if (!bv(u))
                return null
        }
    }
    return (0 until vertexCount).partition { color[it] == 1 }
}

private fun <T> graphTableDfsVisit(g:GraphTable<T, GraphTableNode<T>>, u: Int, s: Stack<Int>) {
    for (v in 0 until g.vertexCount) {
        if (g.getEdge(u, v) != 0) {
            g.addEdge(u, v, 0)
            graphTableDfsVisit(g, v, s)
        }
    }
    s.push(u)
}

private var eulerPathVisited: BooleanArray? = null
private fun <T> GraphList<T>.dfsEuler1(u: Int) {
    eulerPathVisited!![u] = true

}
fun <T> GraphList<T>.calculateEulerPath() {
    val inDegree = IntArray(vertexCount) { 0 }
    val outDegree = IntArray(vertexCount) { 0 }
    for (u in 0 until vertexCount)
        for (e in nodes[u]) {
            ++outDegree[u]
            ++inDegree[e.to]
        }

    
    val outVer = (0 until vertexCount).filter { outDegree[it]-inDegree[it] == 1 }
    val inVer = (0 until vertexCount).filter { inDegree[it]-outDegree[it] == 1 }
    eulerPathVisited = BooleanArray(vertexCount) { false }
    if (inVer.size == 1 && outVer.size == 1) {

    }
    TODO("not finished yet")
}
