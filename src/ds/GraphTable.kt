package my.ds

open class GraphTableNode<T>(val key: Int){
    var element: T? = null
}

open class GraphTable<T, NodeType: GraphTableNode<T>>(vertexCount: Int, edges: List<Triple<Int, Int, Int>>?)
        : Graph<T>(vertexCount, edges){
    open val nodes = List(vertexCount) { GraphTableNode<T>(it) }
    private val table = Matrix(vertexCount, vertexCount)

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
        table[from, to] = weight
    }
    override fun addEdge(from: Int, to: Int, weight: Int) {
        addEdgeImpl(from, to, weight)
    }

    fun getEdge(from: Int, to: Int): Int {
        return table[from, to]
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
                ans.table[v, u] = table[u, v]
        return ans
    }

    override fun topologicalSort(): List<Int> {
        TODO("Not yet implemented")
    }

    override fun calculateStronglyConnectedComponents(): List<List<Int>> {
        TODO("Not yet implemented")
    }

    fun calculateSquareGraph(): GraphTable<T, NodeType> {
        val ans = GraphTable<T, NodeType>(vertexCount, null)
        ans.table.assign(ans.table * ans.table)
        return ans
    }

    fun calculateUniversalSink(): Int {
        if (vertexCount == 1)
            return 0
        if (vertexCount == 2){
            return if (table[0, 1] != 0)
                if (table[1, 0] != 0) Undefined
                else 1
            else
                if (table[1, 0] != 0) 0
                else Undefined
        }
        val isUniversalSink = BooleanArray(vertexCount) { true }
        var a = 0
        var b = 1
        val range = (2 until vertexCount).iterator()
        do {
            val c = range.next()
            if (table[a, b] != 0) {
                isUniversalSink[a] = false
                a = c
            }
            else {
                isUniversalSink[b] = false
                b = c
            }
        } while (range.hasNext())

        var someoneIsNotFalse = Undefined
        for (i in 0 until vertexCount)
            if (isUniversalSink[i]) {
                someoneIsNotFalse = i
                break
            }
        if (someoneIsNotFalse == Undefined)
            return Undefined

        var hasUniversalSink = true
        for (i in 0 until vertexCount)
            if (i != someoneIsNotFalse && !(table[i, someoneIsNotFalse] != 0 && table[someoneIsNotFalse, i] == 0)) {
                hasUniversalSink = false
                break
            }
        return if (hasUniversalSink) someoneIsNotFalse else Undefined
    }
}

fun testGraphTable(){
    val g1 = GraphTable<Int, GraphTableNode<Int>>(4, null)
    g1.addEdge(0,1,1)
    g1.addEdge(0,2,1)
    g1.addEdge(1,2,1)
    g1.addEdge(3,0,1)
    g1.addEdge(3,2,1)
    println(g1.calculateUniversalSink())
}