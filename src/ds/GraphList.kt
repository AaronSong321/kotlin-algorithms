package my.ds

import my.algo.commons.*
import java.util.*

open class GraphListNode<T>(val key: Int) {
    var element: T? = null
    internal var firstEdge: GraphListEdge<T>? = null
    internal var lastEdge: GraphListEdge<T>? = null // for insert purpose
    open operator fun iterator() = GraphListEdgeIterator(firstEdge)
    fun getSize(): Int {
        var degreeNumber = if (firstEdge == null) 0 else 1
        if (firstEdge != null) {
            var e = firstEdge!!.next
            while (e != null) {
                ++degreeNumber
                e = e.next
            }
        }
        return degreeNumber
    }
}

open class GraphListEdge<T>(val to: Int, val weight: Int = 1){
    internal var next: GraphListEdge<T>? = null
    internal var enable = true // for euler path
}

open class GraphListEdgeIterator<T>(private var edge: GraphListEdge<T>?): Iterator<GraphListEdge<T>>{
    override fun hasNext(): Boolean {
        return edge != null
    }

    override fun next(): GraphListEdge<T> {
        val n = edge!!
        edge = edge!!.next
        return n
    }

}

open class GraphList<T>(vertexCount: Int, edges: List<Triple<Int, Int, Int>>?): Graph<T>(vertexCount, edges){
    open val nodes = List(vertexCount) { GraphListNode<T>(it) }

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
        val f = nodes[from]
        val e = GraphListEdge<T>(to, weight)
        if (f.firstEdge == null){
            f.firstEdge = e
            f.lastEdge = e
        }
        else {
            f.lastEdge!!.next = e
            f.lastEdge = e
        }
    }
    override fun addEdge(from: Int, to: Int, weight: Int){
        addEdgeImpl(from, to, weight)
    }

    fun getEdge(from: Int, to: Int): Int? {
        for (e in nodes[from])
            if (e.to == to)
                return e.weight
        return null
    }


    class BfsResult(val vertexCount: Int, start: Int) {
        internal val color = Array(vertexCount) { if (it == start) VertexColor.Gray else VertexColor.White }
        val distance = IntArray(vertexCount) { if (it == start) 0 else Int.MAX_VALUE }
        val precedent = IntArray(vertexCount) { Undefined }
        internal val queue = LinkedList<Int>()
    }
    class DfsResult(val vertexCount: Int, start: Int){
        internal val color = Array(vertexCount) { if (it == start) VertexColor.Gray else VertexColor.White }
        val distance = IntArray(vertexCount) { if (it == start) 0 else Int.MAX_VALUE }
        val precedent = IntArray(vertexCount) { if (it == start) 0 else Int.MAX_VALUE }
        val finish = IntArray(vertexCount) { Undefined }
        internal var time = 0
        internal val topologicalSortedList = LinkedList<Int>()
    }
    var dfsResult: DfsResult? = null
        private set
    var bfsResult: BfsResult? = null
        private set

    override fun bfs(start: Int) {
        bfsResult = BfsResult(vertexCount, start)
        bfsVisit(start)
        for (u in 0 until vertexCount)
            if (bfsResult!!.color[u] == VertexColor.White) {
                bfsResult!!.color[u] = VertexColor.Gray
                bfsVisit(u)
            }
    }
    private fun bfsVisit(t: Int) {
        val r = bfsResult!!
        r.queue.push(t)
        r.distance[t] = 0
        while (!r.queue.isEmpty()){
            val u = r.queue.pop()
            for (edge in nodes[u]) {
                val v = edge.to
                if (r.color[v] == VertexColor.White){
                    r.color[v] = VertexColor.Gray
                    r.distance[v] = r.distance[u]+1
                    r.precedent[v] = u
                    r.queue.push(v)
                }
            }
            r.color[u] = VertexColor.Black
        }
    }

    override fun dfs(start: Int) {
        dfsResult = DfsResult(vertexCount, start)
        dfsVisit(start)
        for (u in 0 until vertexCount)
            if (dfsResult!!.color[u] == VertexColor.White)
                dfsVisit(u)
    }
    private fun dfsVisit(u: Int) {
        val r = dfsResult!!
        ++r.time
        r.color[u] = VertexColor.Gray
        r.distance[u] = r.time
        for (edge in nodes[u]) {
            val v = edge.to
            if (r.color[v] == VertexColor.White) {
                r.precedent[v] = u
                dfsVisit(v)
            }
        }
        r.color[u] = VertexColor.Black
        ++r.time
        r.finish[u] = r.time
        r.topologicalSortedList.add(u)
    }

    override fun asTransposed(): Graph<T> {
        val ans = GraphList<T>(vertexCount, null)
        for (v in 0 until vertexCount) {
            for (e in nodes[v]) {
                ans.addEdge(v, e.to, e.weight)
            }
        }
        return ans
    }

    override fun topologicalSort(): List<Int> {
        if (dfsResult == null)
            dfs(0)
        return dfsResult!!.topologicalSortedList.asReversed()
    }

    class SccResult(val vertexCount: Int){
        var index = 0
        val s = Stack<Int>()
        val vertexIndex = IntArray(vertexCount) { Undefined }
        val lowLink = IntArray(vertexCount) { Undefined }
        val onStack = BooleanArray(vertexCount) { false }
        val ans = MutableList(0) { MutableList<Int>(0) { 0 } }
    }
    var sccResult: SccResult? = null
        internal set

    override fun calculateStronglyConnectedComponents(): List<List<Int>> {
        sccResult = SccResult(vertexCount)
        for (v in 0 until vertexCount) {
            if (sccResult!!.vertexIndex[v] == Undefined)
                scc(v)
        }
        return sccResult!!.ans
    }
    private fun scc(v: Int) {
        val r = sccResult!!
        r.vertexIndex[v] = r.index
        r.lowLink[v] = r.index
        ++r.index
        r.s.push(v)
        r.onStack[v] = true

        for (u in nodes[v]) {
            val w = u.to
            if (r.vertexIndex[w] == Undefined) {
                scc(w)
                r.lowLink[v] = min(r.lowLink[v], r.lowLink[w])
            }
            else if (r.onStack[w]){
                r.lowLink[v] = min(r.lowLink[v], r.vertexIndex[w])
            }
        }

        if (r.lowLink[v] == r.vertexIndex[v]) {
            val oneScc = mutableListOf<Int>()
            do {
                val w = r.s.pop()
                r.onStack[w] = false
                oneScc.add(w)
            } while (w != v)
            r.ans.add(oneScc)
        }
    }

    fun cascadeMultiGraphToGraph(): GraphList<T> {
        val ans = GraphList<T>(vertexCount, null)
        for (u in 0 until vertexCount) {
            val edgeExists = BooleanArray(vertexCount) { u == it }
            for (e in nodes[u]) {
                val v = e.to
                if (!edgeExists[v]) {
                    ans.addEdge(u, v, e.weight)
                    edgeExists[v] = true
                }
            }
        }
        return ans
    }

    fun calculateDiameter(start: Int): Int {
        if (vertexCount <= 1)
            throw IllegalStateException("Graph has $vertexCount (at least 2) nodes to call calculateDiameter")
        val visited = BooleanArray(vertexCount) { it == start  }
        val distance = IntArray(vertexCount) { if (it == start) 0 else Undefined }
        val precedent = IntArray(vertexCount) { Undefined }

        val queue = LinkedList<Int>()
        queue.push(start)
        while (!queue.isEmpty()){
            val u = queue.pop()
            for (edge in nodes[u]) {
                val v = edge.to
                if (!visited[v]){
                    visited[v] = true
                    distance[v] = distance[u]+1
                    precedent[v] = u
                    queue.push(v)
                }
            }
        }
        return getMinimumBy(queue, 2, Comparator { a, b -> b - a }).sum()
    }

    fun calculateMinimumSpawnTree(): List<Edge> {
        return kruskalAlgorithm()
    }

    fun kruskalAlgorithm(): List<Edge> {
        val edgeSet = mutableListOf<Edge>()
        val forest = DisjointSetForest<Int>()
        val vertex = Array(vertexCount) { forest.makeSet(it) }
        val edgeHeap = FibHeap<Edge>()
        for (u in 0..vertexCount)
            for (e in nodes[u])
                edgeHeap.insert(Edge(u,e.to,e.weight))
        while (edgeHeap.size > 0) {
            val e = edgeHeap.extractMin()!!
            if (forest.findSet(vertex[e.from]) != forest.findSet(vertex[e.to])) {
                edgeSet.add(e)
                forest.union(vertex[e.from], vertex[e.to])
            }
        }
        return edgeSet.toList()
    }
    fun primAlgorithm(): List<Edge> {
        val edgeSet = mutableListOf<Edge>()
        val vertexHeap = FibHeap<PrimVertex>()
        val vertex = Array(vertexCount) { PrimVertex(it, Int.MAX_VALUE) }
        vertex[0].key = 0
        val heapNode = Array(vertexCount) { vertexHeap.insert(vertex[it]) }
        while (vertexHeap.size > 0) {
            val u = vertexHeap.extractMin()!!
            for (e in nodes[u.u]) {
                val toNode = vertex[e.to]
                if (toNode.onHeap && e.weight < toNode.key) {
                    vertexHeap.deleteNode(heapNode[toNode.u])
                    toNode.key = e.weight
                    heapNode[e.to] = vertexHeap.insert(toNode)
                    edgeSet.add(Edge(u.u, e.to, getEdge(u.u, e.to)!!))
                }
            }
        }
        return edgeSet.toList()
    }
}

fun testGraphList(){
    val q1 = GraphList<Int>(9, listOf(Triple(0,1,1),Triple(1,2,1),Triple(3,2,1),Triple(3,4,1)
    ,Triple(4,5,1),Triple(2,5,1),Triple(6,7,1),Triple(0,7,1),Triple(1,7,1)))
    q1.dfs(5)
    println(q1.topologicalSort())
    q1.bfs(0)
    println(q1.bfsResult!!.distance)
    val q2 = GraphList<Int>(8, listOf(Triple(0,1,1),Triple(1,2,1),Triple(2,3,1),Triple(3,7,1)
    ,Triple(3,2,1),Triple(4,0,1),Triple(1,4,1),Triple(4,5,1),Triple(5,6,1)
    ,Triple(6,5,1),Triple(6,7,1),Triple(7,7,1),Triple(2,6,1)))
    val scc = q2.calculateStronglyConnectedComponents()
    println(scc)

}
