package my.ds

open class DisjointSetNode<T>internal constructor(val element: T){
    internal var parent: DisjointSetNode<T>? = null
    internal var rank: Int = 0
}

open class DisjointSetForest<T>{
    open fun makeSet(element: T): DisjointSetNode<T> {
        val node = DisjointSetNode(element)
        node.parent = node
        return node
    }

    open fun union(n1: DisjointSetNode<T>, n2: DisjointSetNode<T>) : DisjointSetNode<T> {
        return link(findSet(n1), findSet(n2))
    }

    private fun link(n1: DisjointSetNode<T>, n2: DisjointSetNode<T>) : DisjointSetNode<T> {
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

    open fun findSet(node: DisjointSetNode<T>): DisjointSetNode<T> {
        if (node.parent != node)
            node.parent = findSet(node.parent!!)
        return node.parent!!
    }
}

// Class represents an instance of an offline minimum problem described in exercise 21-1 of Introduction to Algorithms
// Note that keyword start from 0
class OfflineMinimum(private val sequence: List<Operation>, private val keywordCount: Int){
    abstract class Operation
    class Insert(val num: Int): Operation()
    class ExtractMin(): Operation()
    private val extractMinCount = sequence.size - keywordCount
    private val elements = Array<DisjointSetNode<Int>?>(extractMinCount+1) { null }
    private val nodes = Array<DisjointSetNode<Int>?>(keywordCount) { null }
    private val forest = DisjointSetForest<Int>()
    private val extract = IntArray(extractMinCount) { Undefined }
    private val indexInElement = IntArray(keywordCount) { Undefined }

    private companion object {
        private const val Undefined = -1
    }

    fun solve(): Int {
        var lastSet: DisjointSetNode<Int>? = null
        var setIndex = 0
        for (operation in sequence) {
            if (operation is Insert){
                val i = operation.num
                if (lastSet == null){
                    lastSet = forest.makeSet(i)
                    elements[setIndex] = lastSet
                    nodes[i] = lastSet
                    indexInElement[i] = setIndex
                }
                else {
                    nodes[i] = forest.makeSet(i)
                    indexInElement[nodes[i]!!.element] = setIndex
                    forest.union(lastSet, nodes[i]!!)
                }
            }
            else if (operation is ExtractMin) {
                lastSet = null
                ++setIndex
            }
        }

        for (num in 0 until keywordCount){
            val node = forest.findSet(nodes[num]!!)
            val j = indexInElement[node.element]
            if (j != extractMinCount) {
                extract[j] = num
                var l = Undefined
                for (t in j+1..extractMinCount){
                    if (elements[t] != null) {
                        l = t
                        break
                    }
                }
                val e1 = elements[j]!!
                val e2 = elements[l]!!
                indexInElement[e1.element] = indexInElement[e2.element]
                elements[l] = forest.union(e1, e2)
                elements[j] = null
            }
        }
        return 0
    }

    fun printSolution(){
        for (e in extract)
            print("$e ")
        println()
    }
}

typealias I = OfflineMinimum.Insert
typealias E = OfflineMinimum.ExtractMin

fun testOfflineMinimumProblem(){
    val q1 = OfflineMinimum(listOf(I(3),I(0),I(7),E(),I(2), I(5),E(),I(6),E(),I(4),I(1)), 8)
    println(q1.solve())
    q1.printSolution()
}

class DepthDeterminationNode<T> internal constructor(val element: T) {
    internal var parent: DepthDeterminationNode<T>? = null
    internal var depthToParent = 0
}

class DepthDeterminationForest<T>(){
    fun makeTree(element: T): DepthDeterminationNode<T> {
        val t = DepthDeterminationNode<T>(element)
        t.parent = t
        return t
    }

    fun findDepth(node: DepthDeterminationNode<T>): Pair<DepthDeterminationNode<T>, Int> {
        if (node.parent != node) {
            val a = findDepth(node.parent!!)
            node.parent = a.first
            node.depthToParent += a.second
        }
        return Pair(node.parent!!, node.depthToParent)
    }

    fun graft(n1: DepthDeterminationNode<T>, n2: DepthDeterminationNode<T>) {
        n1.parent = n2
        n1.depthToParent = 1
    }
}

class DepthDeterminationProblem(private val keywordCount: Int, private val parentPairs: List<Pair<Int, Int>>){
    private val forest = DepthDeterminationForest<Int>()
    private val nodes = List(keywordCount) { forest.makeTree(it) }
    init {
        for (pair in parentPairs) {
            forest.graft(nodes[pair.first], nodes[pair.second])
        }
    }

    fun calculateDepth(element: Int): Int {
        return forest.findDepth(nodes[element]).second
    }
}

fun testDepthDetermination(){
    val q1 = DepthDeterminationProblem(12, listOf(Pair(3,2),Pair(4,2),Pair(5,3),Pair(7,3),Pair(10,9),Pair(0,9),Pair(11,9),Pair(1,6),Pair(9,4),Pair(8,6)))
    println(q1.calculateDepth(10))
    println(q1.calculateDepth(6))
    println(q1.calculateDepth(7))
}
