package my.ds

abstract class Graph<T>(val vertexCount: Int, edges: List<Triple<Int, Int, Int>>?){
    abstract fun addEdge(from: Int, to: Int, weight: Int)
    abstract fun bfs(start: Int)
    abstract fun dfs(start: Int)
    abstract fun topologicalSort(): List<Int>
    abstract fun asTransposed(): Graph<T>
    abstract fun calculateStronglyConnectedComponents(): List<List<Int>>

    enum class VertexColor { White, Gray, Black }
    companion object {
        const val Undefined = -1
    }

    data class Edge(val from: Int, val to: Int, val weight: Int)
        : Comparable<Edge> {
        override fun compareTo(other: Edge): Int {
            return weight.compareTo(other.weight)
        }
    }

    data class PrimVertex(val u: Int, var key: Int): Comparable<PrimVertex> {
        var onHeap = true
        override fun compareTo(other: PrimVertex): Int {
            return key.compareTo(other.key)
        }
    }
}