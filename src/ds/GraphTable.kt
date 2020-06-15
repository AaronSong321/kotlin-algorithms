package my.ds

open class GraphTableNode<T>(val key: Int){
    var element: T? = null
}

open class GraphTable<T, NodeType: GraphTableNode<T>>(vertexCount: Int, edges: List<Triple<Int, Int, Int>>?): Graph<T>(vertexCount, edges){
    open val nodes = List(vertexCount) { GraphTableNode<T>(it) }
    private val table = Array<IntArray>(vertexCount) { IntArray(vertexCount) { Undefined } }

    init {
        if (edges != null){
            for (triple in edges) {
                addEdgeImpl(triple.first, triple.second, triple.third)
            }
        }
    }

    // implement this function to prevent calling `addEdge` from `init` function
    // because non-final functions should not be called in `init`
    private fun addEdgeImpl(from: Int, to: Int, weight: Int){
        table[from][to] = weight
    }
    override fun addEdge(from: Int, to: Int, weight: Int) {
        addEdgeImpl(from, to, weight)
    }

    override fun bfs(start: Int) {
        TODO("Not yet implemented")
    }

    override fun dfs(start: Int) {
        TODO("Not yet implemented")
    }

    override fun asTransposed(): Graph<T> {
        val ans = GraphTable<T, NodeType>(vertexCount, null)
        for (u in 0 until vertexCount)
            for (v in 0 until vertexCount)
                ans.table[v][u] = table[u][v]
        return ans
    }

    override fun topologicalSort(): List<Int> {
        TODO("Not yet implemented")
    }

    override fun calculateStronglyConnectedComponents(): List<List<Int>> {
        TODO("Not yet implemented")
    }
}