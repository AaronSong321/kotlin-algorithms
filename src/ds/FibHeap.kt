package my.ds

import java.lang.IllegalArgumentException
import kotlin.math.*

class FibHeap<T: Comparable<T>>{
    class Node<E: Comparable<E>>(key: E): Comparable<Node<E>>{
        var key: E = key
            internal set
        internal var firstChild: Node<E>? = null
            private set
        internal var degree: Int = 0
            private set
        internal var parent: Node<E>? = null
        internal var mark = false
        internal var left: Node<E>? = null
        internal var right: Node<E>? = null
        internal var markedAsNegativeInfinity = false

        internal fun linkNodeToLeft(node: Node<E>) {
            left!!.right = node
            node.left = left
            left = node
            node.right = this
        }
        internal fun addChild(node: Node<E>) {
            if (firstChild == null) {
                node.right = node
                node.left = node
                firstChild = node
            }
            else {
                firstChild!!.linkNodeToLeft(node)
            }
            ++degree
            node.parent = this
            node.mark = false
        }
        internal fun removeChild(node: Node<E>) {
            if (node.right == node) {
                firstChild = null
            }
            else {
                node.left!!.right = node.right
                node.right!!.left = node.left
                if (firstChild == node)
                    firstChild = node.right
            }
            --degree
            // do not mark this node here
        }

        override operator fun compareTo(other: Node<E>): Int {
            return if (markedAsNegativeInfinity) {
                if (other.markedAsNegativeInfinity)
                    0
                else
                    Int.MIN_VALUE
            } else {
                if (other.markedAsNegativeInfinity)
                    Int.MAX_VALUE
                else
                    key.compareTo(other.key)
            }
        }
    }
    var minNode: Node<T>? = null
        private set
    var size: Int = 0
        private set

    private fun addToRootList(node: Node<T>) {
        if (minNode == null){
            node.left = node
            node.right = node
            minNode = node
        }
        else {
            minNode!!.linkNodeToLeft(node)
            if (node < minNode!!)
                minNode = node
        }
        node.parent = null
    }
    private fun removeFromRootList(node: Node<T>) {
        if (node.right == node){
            minNode = null
        }
        else {
            node.right!!.left = node.left
            node.left!!.right = node.right
        }
    }


    fun add(element: T): Boolean {
        insert(element)
        return true
    }
    fun insert(element: T): Node<T> {
        val newNode = Node(element)
        addToRootList(newNode)
        ++size
        return newNode
    }

    fun clear() {
        minNode = null
        size = 0
    }

    fun minimum(): T? = minNode?.key
    fun union(other: FibHeap<T>): FibHeap<T> {
        val ans = FibHeap<T>()
        if (size == 0){
            ans.size = other.size
            ans.minNode = other.minNode
        }
        else if (other.size == 0){
            ans.size = size
            ans.minNode = minNode
        }
        else {
            ans.size = size + other.size
            ans.minNode = if (minNode!! < other.minNode!!) minNode else other.minNode
            minNode!!.left!!.right = other.minNode
            other.minNode!!.left!!.right = minNode
            other.minNode!!.left = minNode!!.left
            minNode!!.left = other.minNode!!.left
        }
        clear()
        other.clear()
        return ans
    }

    companion object {
        private const val psi = 1.61803
        private fun d(input: Double): Double {
            return log(input, psi)
        }
        private fun D(input: Int): Int {
            return d(input.toDouble()).toInt()
        }
    }

    // detach y from the root list of the heap and make y a child of x
    private fun heapLink(y: Node<T>, x: Node<T>) {
        removeFromRootList(y)
        x.addChild(y)
    }
    private fun consolidate(){
        val asize = D(size)+1
        val a = Array<Node<T>?>(asize) { null }
        var w = minNode!!
        val firstNodeToTraverse = minNode!!
        do{
            var x = w
            w = w.right!!
            var d = x.degree
            while(a[d] != null){
                var y = a[d]!!
                if (y < x){
                    val z = x
                    x = y
                    y = z
                }
                heapLink(y, x)
                a[d] = null
                ++d
            }
            a[d] = x
        } while (w != firstNodeToTraverse)
        minNode = null
        for (i in 0 until asize) {
            if (a[i] != null){
                addToRootList(a[i]!!)
            }
        }
    }
    fun extractMin(): T? {
        val m = minNode ?: return null
        if (m.firstChild != null){
            var child = m.firstChild!!
            val fc = m.firstChild!!
            do {
                val nextChild = child.right!!
                addToRootList(child)
                child = nextChild
            } while (child != fc)
        }
        --size
        if (m.right == m){
            minNode = null
        }
        else {
            m.left!!.right = m.right
            m.right!!.left = m.left
            minNode = m.right
            consolidate()
        }
        return m.key
    }

    private fun afterDecrease(node: Node<T>) {
        val y = node.parent
        if (y != null && node < y) {
            cut(node, y)
            cascadingCut(y)
        }
        if (node < minNode!!) {
            minNode = node
        }
    }
    fun decreaseKey(node: Node<T>, k: T) {
        if (node.key <= k) {
            throw IllegalArgumentException("function FibHeap::decreaseKey requires k $k smaller than node.key ${node.key}")
        }
        node.key = k
        afterDecrease(node)
    }
    private fun cut(x: Node<T>, y: Node<T>) {
        y.removeChild(x)
        addToRootList(x)
        x.mark = false
    }
    private fun cascadingCut(y: Node<T>) {
        val z = y.parent
        if (z != null){
            if (!y.mark) {
                y.mark = true
            }
            else {
                cut(y, z)
                cascadingCut(z)
            }
        }
    }

    fun deleteNode(x: Node<T>) {
        x.markedAsNegativeInfinity = true
        afterDecrease(x)
        extractMin()
    }
}

fun <T:Comparable<T>> fibHeapOf(vararg elements: T): FibHeap<T> {
    val ans = FibHeap<T>()
    for (e in elements){
        ans.add(e)
    }
    return ans
}

fun <T:Comparable<T>> union(a: FibHeap<T>, b: FibHeap<T>): FibHeap<T> {
    return a.union(b)
}

fun testFibHeap(){
    val f1 = fibHeapOf(1, 7, -3, 5)
    val node58 = f1.insert(58)
    f1.decreaseKey(node58, -10)
    println(f1.extractMin())
    val node100 = f1.insert(100)
    f1.deleteNode(node100)

    val f2 = fibHeapOf(1, 7, -3, 5)
    while (f2.size > 0)
        println(f2.extractMin())
}
