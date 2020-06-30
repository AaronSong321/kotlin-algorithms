package my.ds

import kotlin.math.log

// Template Fibonacci Heap that takes a lambda for comparison
class FibonacciHeap<T>(private val comparator: (T, T) -> Int) {
    class Node<E> internal constructor(key: E, private val comparator: (E, E) -> Int) {
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

        operator fun compareTo(other: Node<E>): Int {
            return if (markedAsNegativeInfinity) {
                if (other.markedAsNegativeInfinity)
                    0
                else
                    Int.MIN_VALUE
            } else {
                if (other.markedAsNegativeInfinity)
                    Int.MAX_VALUE
                else
                    comparator(key, other.key)
            }
        }
    }
    var minNode: Node<T>? = null
        internal set
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
        val newNode = Node(element, comparator)
        addToRootList(newNode)
        ++size
        return newNode
    }

    fun clear() {
        minNode = null
        size = 0
    }

    fun minimum(): T? = minNode?.key

    fun union(other: FibonacciHeap<T>): FibonacciHeap<T> {
        val ans = FibonacciHeap(comparator)
        when {
            size == 0 && other.size == 0 -> { }
            size == 0 -> {
                ans.size = other.size
                ans.minNode = other.minNode
            }
            other.size == 0 -> {
                ans.size = size
                ans.minNode = minNode
            }
            else -> {
                ans.size = size + other.size
                ans.minNode = if (minNode!! < other.minNode!!) minNode else other.minNode
                minNode!!.left!!.right = other.minNode
                other.minNode!!.left!!.right = minNode
                other.minNode!!.left = minNode!!.left
                minNode!!.left = other.minNode!!.left
            }
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
        if (comparator(node.key, k) <= 0) {
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

fun <T> fibonacciHeapOf(comparator: (T, T) -> Int, vararg elements: T): FibonacciHeap<T> {
    val ans = FibonacciHeap(comparator)
    for (e in elements){
        ans.add(e)
    }
    return ans
}

fun <T> union(a: FibonacciHeap<T>, b: FibonacciHeap<T>): FibonacciHeap<T> {
    return a.union(b)
}

fun testFibonacciHeap() {
    val f1 = fibonacciHeapOf( { a,b -> a-b }, 1,7,-3,5)
    val node58 = f1.insert(58)
    f1.decreaseKey(node58, -10)
    println(f1.extractMin())
    val node100 = f1.insert(100)
    f1.deleteNode(node100)

    val f2 = fibonacciHeapOf({ a,b -> a-b }, 1, 7, -3, 5)
    while (f2.size > 0)
        println(f2.extractMin())
}