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
}