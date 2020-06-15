package my.ds

class OfflineLCSNode<T>(element: T) {
    internal var parent: OfflineLCSNode<T>? = null
    internal var rank: Int = 0
    internal var ancestor: OfflineLCSNode<T>? = null
    internal var pigmented = false
}

open class OfflineLCAForest<T>() {
    open fun makeSet(element: T): OfflineLCSNode<T> {
        val node = OfflineLCSNode(element)
        node.parent = node
        return node
    }

    open fun union(n1: OfflineLCSNode<T>, n2: OfflineLCSNode<T>) : OfflineLCSNode<T> {
        return link(findSet(n1), findSet(n2))
    }

    private fun link(n1: OfflineLCSNode<T>, n2: OfflineLCSNode<T>) : OfflineLCSNode<T> {
        if (n1.rank > n2.rank) {
            n2.parent = n1
            return n1
        }
        else {
            n1.parent = n2
            if (n1.rank == n2.rank)
                ++n2.rank
            return n2
        }
    }

    open fun findSet(node: OfflineLCSNode<T>): OfflineLCSNode<T> {
        if (node.parent != node)
            node.parent = findSet(node.parent!!)
        return node.parent!!
    }
}

class OfflineLCSProb(private val keywordCount: Int, private val pairs: List<Pair<Int, Int>>){
    private val nodes = List(keywordCount) { OfflineLCSNode<Int>(it) }
    fun lca(node: OfflineLCSNode<Int>) {
        TODO("Not implemented yet")
    }
}

class OfflineLCSProblem<T>(private val pairs: List<Pair<T, T>>){

}