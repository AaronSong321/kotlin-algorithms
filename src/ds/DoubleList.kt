package my.ds

import java.lang.IllegalStateException

class DoubleList<T>(): MutableCollection<T>{
    class Node<T>(var element: T){
        var next: Node<T>? = null
        var previous: Node<T>? = null
    }

    var root: Node<T>? = null
        private set
    override var size: Int = 0
        private set

    override fun add(element: T): Boolean{
        if (root == null){
            root = Node(element)
            root!!.next = root
            root!!.previous = root
        }
        else {
            val rootPre = root!!.previous!!
            val newNode = Node(element)
            rootPre.next = newNode
            newNode.next = root!!
            root!!.previous = newNode
            newNode.previous = rootPre
        }
        ++size
        return true
    }

    override fun contains(element: T): Boolean {
        if (root == null)
            return false
        var node = root
        while (true){
            if (node!!.element == element)
                return true
            node = node.next
            if (node == root)
                return false
        }
    }
    override fun containsAll(elements: Collection<T>): Boolean {
        if (root == null) return false
        var node = root
        val match = BooleanArray(elements.size) { false }
        while (true){
            val elemOfThisNode = node!!.element
            var index = 0
            for (elem in elements){
                if (!match[index] && elem == elemOfThisNode){
                    match[index] = true
                    break
                }
                ++index
            }
            node = node.next
            if (node == root)
                break
        }
        return !match.contains(false)
    }

    fun indexOf(element: T): Int {
        var node = root
        for (index in 0 until size){
            if (node!!.element == element)
                return index
            node = node.next
        }
        return -1
    }

    override fun isEmpty(): Boolean = size != 0

    operator fun get(index: Int): T{
        if (index <= 0 || index >= size) {
            throw IndexOutOfBoundsException("index $index out of range $size")
        }
        var n = root!!
        for (i in 0 until index){
            n = n.next!!
        }
        return n.element
    }
    operator fun set(index: Int, elem: T) {
        if (index <= 0 || index >= size) {
            throw IndexOutOfBoundsException("index $index out of range $size")
        }
        var n = root!!
        for (i in 0 until index){
            n = n.next!!
        }
        n.element = elem
    }

    class DoubleListIterator<T>(private val origin: DoubleList<T>): MutableIterator<T>{
        private var node: Node<T>? = origin.root
        private val firstNode: Node<T>? = origin.root
        private var index = 0
        private var lastNodeReturned: Node<T>? = null

        override operator fun hasNext(): Boolean {
            return node != null && (node!! != firstNode!! || lastNodeReturned == null)
        }
        override operator fun next(): T {
            val t = node!!.element
            lastNodeReturned = node
            node = node!!.next
            ++index
            return t
        }
        fun nextIndex(): Int {
            lastNodeReturned = node
            node = node!!.next
            return ++index
        }
        fun hasPrevious(): Boolean {
            return (node != null) && node!!.previous != firstNode!!
        }
        fun previous(): T {
            val t = node!!.element
            lastNodeReturned = node
            node = node!!.previous
            --index
            return t
        }
        fun previousIndex(): Int {
            lastNodeReturned = node
            node = node!!.previous
            return --index
        }
        override fun remove() {
            if (lastNodeReturned == null)
                throw IllegalStateException()
            else
                origin.removeNode(lastNodeReturned!!)
        }
    }
    override operator fun iterator(): DoubleListIterator<T> {
        return DoubleListIterator<T>(this)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        var addCount = 0
        for (elem in elements){
            if (add(elem))
                ++addCount
        }
        return addCount != 0
    }

    override fun clear() {
        root = null
        size = 0
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var deleteCount = 0
        for (elem in elements){
            if (remove(elem))
                ++deleteCount
        }
        return deleteCount != 0
    }

    internal fun removeNode(node: Node<T>) {
        if (node == root){
            if (node.next == node){
                root = null
            }
            else{
                node.next!!.previous = node.previous
                node.previous!!.next = node.next
                root = node.next
            }
        }
        else {
            node.next!!.previous = node.previous
            node.previous!!.next = node.next
        }
        --size
    }

    override fun remove(element: T): Boolean {
        var node = root
        for (index in 0 until size){
            if (node!!.element == element){
                removeNode(node)
                --size
                return true
            }
            node = node.next
        }
        return false
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        if (root == null)
            return false
        var deleteCount = 0
        var node = root!!
        for (index in 0..size){
            if (!elements.contains(node.element)){
                removeNode(node)
                ++deleteCount
            }
            node = node.next!!
        }
        return deleteCount != 0
    }

    fun copy(): DoubleList<T> {
        val ans = DoubleList<T>()
        for (element in this){
            ans.add(element)
        }
        return ans
    }
    operator fun plus(element: T): DoubleList<T> {
        add(element)
        return this
    }
    operator fun minus(element: T): DoubleList<T> {
        remove(element)
        return this
    }
    operator fun plus(col: Iterable<T>): DoubleList<T> {
        addAll(col)
        return this
    }
    operator fun minus(col: Iterable<T>): DoubleList<T> {
        removeAll(col)
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other == null)
            return false
        if (this === other)
            return true
        if (other is DoubleList<*> && size == other.size) {
            if (size == 0)
                return true // if this: <T>, other: <E> and T != E ??
            var n1 = root!!
            var n2 = other.root!!
            for (index in 0 until size){
                if (n2.element != n1.element)
                    return false
                n1 = n1.next!!
                n2 = n2.next!!
            }
            return true
        }
        else
            return false
    }

    override fun hashCode(): Int {
        return if (size == 0) 0.hashCode()
        else size.hashCode() shl 32 + root!!.previous!!.element.hashCode()
    }
}

fun <T> doubleListOf(vararg elements: T): DoubleList<T> {
    var ans = DoubleList<T>()
    for (elem in elements){
        ans.add(elem)
    }
    return ans
}

fun testDoubleList(){
//    val a = mutableListOf(1, 2, 3, 4, 7, 8, 9)

    val dl = doubleListOf(1, 7, -3, 5)
    dl.retainAll(1..5)
    for (e in dl)
        println(e)
    println(dl == dl.copy())
}