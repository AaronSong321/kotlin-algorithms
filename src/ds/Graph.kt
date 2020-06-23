package my.ds

import java.lang.IndexOutOfBoundsException

abstract class Graph<T>(val vertexCount: Int){
    abstract fun addEdge(from: Int, to: Int, weight: Int)
    abstract fun getEdge(from: Int, to: Int): Int?

    // Breadth-first search the graph. Result should be provided in bfsResult property
    abstract fun bfs(start: Int)
    // Depth-first search the graph. Result should be provided in dfsResult property
    abstract fun dfs(start: Int)
    // Sort the graph topologically using dfs
    abstract fun topologicalSort(): List<Int>
    // Return the transposed graph of this graph
    abstract fun asTransposed(): Graph<T>
    // Calculates all strongly connected components of the graph
    abstract fun calculateStronglyConnectedComponents(): List<List<Int>>

    // Calculates one minimum spawn tree of a graph if it is connected, null otherwise.
    abstract fun calculateMinimumSpawnTree(): List<Edge>?
    abstract fun kruskalAlgorithm(): List<Edge>?
    abstract fun primAlgorithm(): List<Edge>?

    fun indices() = 0 until vertexCount
    fun checkVertex(u: Int) {
        if (u < 0 || u >= vertexCount)
            throw IndexOutOfBoundsException("Index $u out of bound $vertexCount")
    }

    enum class VertexColor { White, Gray, Black }
    companion object {
        const val Undefined = -1
        const val NoPath = Int.MAX_VALUE / 2
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