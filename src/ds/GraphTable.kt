package my.ds

open class GraphTable<T>(vertexCount: Int, init: (Int, Int) -> Int = { _, _ -> 0 })
        : Graph<T>(vertexCount){
    private var table = Matrix(vertexCount, vertexCount, init)

    // implement this function to prevent calling `addEdge` from `init` function
    // because non-final functions should not be called in `init`
    private fun addEdgeImpl(from: Int, to: Int, weight: Int){
        table[from, to] = weight
    }
    override fun addEdge(from: Int, to: Int, weight: Int) {
        addEdgeImpl(from, to, weight)
    }

    override fun getEdge(from: Int, to: Int): Int? {
        val v = table[from, to]
        return if (v == 0) null else v
    }

    override fun bfs(start: Int) {
        TODO("Not yet implemented")
    }

    override fun dfs(start: Int) {
        TODO("Not yet implemented")
    }

    override fun asTransposed(): Graph<T> {
        return GraphTable(vertexCount) { u, v -> table[v, u] }
    }

    override fun topologicalSort(): List<Int> {
        TODO("Not yet implemented")
    }

    override fun calculateStronglyConnectedComponents(): List<List<Int>> {
        TODO("Not yet implemented")
    }

    fun calculateSquareGraph(): GraphTable<T> {
        return GraphTable(vertexCount) { r, c -> table[r, c] }
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
        for (i in indices())
            if (isUniversalSink[i]) {
                someoneIsNotFalse = i
                break
            }
        if (someoneIsNotFalse == Undefined)
            return Undefined

        var hasUniversalSink = true
        for (i in indices())
            if (i != someoneIsNotFalse && !(table[i, someoneIsNotFalse] != 0 && table[someoneIsNotFalse, i] == 0)) {
                hasUniversalSink = false
                break
            }
        return if (hasUniversalSink) someoneIsNotFalse else Undefined
    }

    override fun calculateMinimumSpawnTree(): List<Edge>? {
        return primAlgorithm()
    }

    override fun kruskalAlgorithm(): List<Edge>? {
        val edgeSet = mutableListOf<Edge>()
        val forest = DisjointSetForest<Int>()
        val vertex = Array(vertexCount) { forest.makeSet(it) }
        val edgeHeap = FibHeap<Edge>()
        for (u in indices())
            for (v in indices())
                if (table[u, v] != 0)
                    edgeHeap.insert(Edge(u, v, table[u, v]))
        while (edgeHeap.size > 0) {
            val e = edgeHeap.extractMin()!!
            if (forest.findSet(vertex[e.from]) != forest.findSet(vertex[e.to])) {
                edgeSet.add(e)
                forest.union(vertex[e.from], vertex[e.to])
            }
        }
        return if (edgeSet.size == vertexCount-1) edgeSet else null
    }
    override fun primAlgorithm(): List<Edge>? {
        val edgeSet = mutableListOf<Edge>()
        val vertexHeap = FibHeap<PrimVertex>()
        val vertex = Array(vertexCount) { PrimVertex(it, Int.MAX_VALUE) }
        vertex[0].key = 0
        val heapNode = Array(vertexCount) { vertexHeap.insert(vertex[it]) }
        while (vertexHeap.size > 0) {
            val u = vertexHeap.extractMin()!!
            u.onHeap = false
            for (v in indices()) {
                val weight = table[u.u, v]
                if (weight != 0) {
                    val toNode = vertex[v]
                    if (toNode.onHeap && weight < toNode.key) {
                        vertexHeap.deleteNode(heapNode[toNode.u])
                        toNode.key = weight
                        heapNode[v] = vertexHeap.insert(toNode)
                        edgeSet.add(Edge(u.u, v, getEdge(u.u, v)!!))
                    }
                }
            }
        }
        return if (edgeSet.size == vertexCount-1) edgeSet else null
    }

    class AllPathResult(val pathLength: AMatrix, val precedent: AMatrix){
        operator fun component1() = pathLength
        operator fun component2() = precedent
    }
    var allPathResult: AllPathResult? = null
    fun floydWarshallAlgorithm() {
        val d = Matrix(vertexCount, vertexCount) { i,j -> if (i==j) 0 else if (table[i,j]==0) NoPath else table[i,j] }
        val precedent = Matrix(vertexCount, vertexCount) { i, j-> if (i == j || table[i, j] == 0) Undefined else i }
        for (k in 1..vertexCount){
            for (i in indices())
                for (j in indices()){
                    val direct = d[i,j]
                    var addPath = d[i,k-1] + d[k-1,j]
                    addPath = if (addPath > NoPath) NoPath else addPath
                    if (direct <= addPath) {
                        d[i,j]=direct
                    }
                    else {
                        d[i,j]=addPath
                        precedent[i,j]=precedent[k-1,j]
                    }
                }
        }
        allPathResult = AllPathResult(d, precedent)
    }

    fun calculateTransitiveClosure(): BooleanMatrix {
        val t = BooleanMatrix(vertexCount, vertexCount) { i,j -> i == j || table[i, j] != 0 }
        var k = 1
        while (k <= vertexCount) {
            for (i in indices())
                for (j in indices())
                    t[i,j] = t[i,j] || t[i,k-1] && t[k-1,j]
            ++k
        }
        return t
    }

}

fun testGraphTable(){
//    val g1 = GraphTable<Int>(4)
//    g1.addEdge(0,1,1)
//    g1.addEdge(0,2,1)
//    g1.addEdge(1,2,1)
//    g1.addEdge(3,0,1)
//    g1.addEdge(3,2,1)
//    println(g1.calculateUniversalSink())
//
    val g2 = GraphTable<Int>(5)
    g2.addEdge(0,1,3)
    g2.addEdge(0,2,8)
    g2.addEdge(0,4,-4)
    g2.addEdge(1,3,1)
    g2.addEdge(1,4,7)
    g2.addEdge(2,1,4)
    g2.addEdge(3,0,2)
    g2.addEdge(3,2,-5)
    g2.addEdge(4,3,6)
    g2.floydWarshallAlgorithm()
    val (t1, t2) = g2.allPathResult!!

//    val g3 = GraphTable<Int>(6)
//    g3.addEdge(0,4,-1)
//    g3.addEdge(1,0,1)
//    g3.addEdge(1,3,2)
//    g3.addEdge(2,1,2)
//    g3.addEdge(2,5,-8)
//    g3.addEdge(3,0,-4)
//    g3.addEdge(3,4,3)
//    g3.addEdge(4,1,7)
//    g3.addEdge(5,1,5)
//    g3.addEdge(5,2,10)
//    g3.floydWarshallAlgorithm()

    val g4 = GraphTable<Int>(10)
    g4.addEdge(0,1,1)
    g4.addEdge(1,3,1)
    g4.addEdge(3,6,1)
    g4.addEdge(6,8,1)
    val g41 = (g4.calculateTransitiveClosure())


    val gWithNegativeLoop5 = GraphTable<Int>(5)
    gWithNegativeLoop5.addEdge(0,3,-1)
    gWithNegativeLoop5.addEdge(3,4,-2)

    val gWithNegativeLoop6 = GraphTable<Int>(5)
    gWithNegativeLoop6.addEdge(0,2,-1)
    gWithNegativeLoop6.addEdge(2,3,-1)
}