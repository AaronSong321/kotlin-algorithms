package my.ds

import kotlin.math.*

class FibHeap<T: Comparable<T>>{
    class Node<E: Comparable<E>>(var key: E){
        var firstChild: Node<E>? = null
        var degree: Int = 0
        var parent: Node<E>? = null
        var mark = false
        var left: Node<E>? = null
        var right: Node<E>? = null

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
    }
    private var minNode: Node<T>? = null
    private var rootListSize: Int = 0
    var size: Int = 0
        private set

    fun addToRootList(node: Node<T>) {
        if (minNode == null){
            node.left = node
            node.right = node
            minNode = node
        }
        else {
            minNode!!.linkNodeToLeft(node)
            if (node.key < minNode!!.key)
                minNode = node
        }
        node.parent = null
        ++rootListSize
    }
    fun removeFromRootList(node: Node<T>) {
        if (node.right == node){
            minNode = null
        }
        else {
            node.right!!.left = node.left
            node.left!!.right = node.right
        }
        --rootListSize
    }


    fun add(element: T): Boolean {
        insert(element)
        return true
    }
    fun insert(element: T) {
        val newNode = Node(element)
        addToRootList(newNode)
        ++size
    }

    fun clear() {
        minNode = null
        rootListSize = 0
        size = 0
    }

    fun minimum(): T? = minNode?.key
    fun union(other: FibHeap<T>): FibHeap<T> {
        val ans = FibHeap<T>()
        if (rootListSize == 0){
            ans.rootListSize = other.rootListSize
            ans.size = other.size
            ans.minNode = other.minNode
        }
        else if (other.rootListSize == 0){
            ans.rootListSize = rootListSize
            ans.size = size
            ans.minNode = minNode
        }
        else {
            ans.rootListSize = rootListSize + other.rootListSize
            ans.size = size + other.size
            ans.minNode = if (minNode!!.key < other.minNode!!.key) minNode else other.minNode
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
        for (i in 0 until rootListSize){
            var x = w
            w = w.right!!
            var d = x.degree
            while(a[d] != null){
                var y = a[d]!!
                if (y.key < x.key){
                    val z = x
                    x = y
                    y = z
                }
                heapLink(y, x)
                a[d] = null
                ++d
            }
            a[d] = x
        }
        minNode = null
        rootListSize = 0
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

        --rootListSize
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
    for (i in 0 until f1.size){
        println(f1.extractMin())
    }
}