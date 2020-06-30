package my.algo.greedy

import java.io.File
import java.util.*
import kotlin.math.*

class HuffmanCode(val degree: Int, private val frequency: List<Pair<Char, Double>>) {
    internal var root: Node? = null
    internal var encodeTree: Map<Char, String>? = null

    init {
        if (degree <= 1)
            throw IllegalArgumentException("degree out of range: $degree <= 1")
    }
    class Node internal constructor(val key: Char?, val frequency: Double) {
        var partialSolution: Int? = null
            internal set
        var children: Array<Node>? = null
            internal set
    }

    fun solve(): Node {
        val x = frequency.size
        val count = ceil((x-1).toDouble() / (degree-1).toDouble()).toInt() * (degree-1)+1
        val dummyCount = count - x
        val queue = PriorityQueue<Node> { a, b -> (a.frequency - b.frequency).sign.toInt() }
        for (e in frequency)
            queue.add(Node(e.first, e.second))
        for (e in 0 until dummyCount)
            queue.add(Node(null, 0.0))
        for (i in 0 until (count-1) / (degree-1)) {
            val c = Array(degree) { queue.poll()!!.apply { partialSolution = it } }
            val f = c.sumByDouble { it.frequency }
            queue.add(Node(null, f).apply { children = c })
        }
        root = queue.poll()!!
        return root!!
    }

    fun printSolution2() {
        val code = mutableListOf<Int>()
        encodeTree = mutableMapOf()
        recursivePrint(root!!, code)
    }
    private fun recursivePrint(node: Node, code: MutableList<Int>) {
        when {
            node.children != null -> {
                val isRoot = node.partialSolution == null
                if (!isRoot)
                    code.add(node.partialSolution!!)
                for (child in node.children!!)
                    recursivePrint(child, code)
                if (!isRoot)
                    code.removeAt(code.size-1)
            }
            node.key == null -> {

            }
            else -> {
                code.add(node.partialSolution!!)
                val hs = code.toHuffmanString()
                (encodeTree as MutableMap)[node.key] = hs
                println("${node.key} -> ${code.size} bits ($hs)")
                code.removeAt(code.size-1)
            }
        }
    }

    fun printSolution() {
        var length = 0
        val code = mutableListOf<Int>()
        val countBeforeDropChar = Stack<Int>()
        val nodes = Stack<Node>().apply { push(root) }
        val checkDrop = {
            val c = countBeforeDropChar.pop()-1
            if (c == 0)
                code.removeAt(code.size-1)
            else
                countBeforeDropChar.push(c)
        }


        while (nodes.size > 0) {
            val n = nodes.pop()!!
            when {
                n.children != null -> {
                    val cs = n.children!!
                    for (child in cs)
                        nodes.push(child)
                    if (n.partialSolution != null) {
                        code.add(n.partialSolution!!)
                        countBeforeDropChar.push(degree)
                        checkDrop()
                    }
                }
                n.key == null -> {
                    checkDrop()
                }
                else -> {
                    code.add(n.partialSolution!!)
                    println("${n.key} -> ${code.size} bits (${code.toHuffmanString()})")
                    code.removeAt(code.size-1)
                    checkDrop()
                }
            }
        }
    }

    fun generateEncodeTree(): String {
        return StringBuilder().apply { encodeTree!!.forEach {
                append("${it.key}${it.value}")
        }}.toString()
    }

    fun encode(text: String): String {
        return StringBuilder().apply { text.forEach {
            append(encodeTree!![it])
        }}.toString()
    }

    fun encodeToBytes(): ByteArray {
        return ByteArray(0)
    }
}

fun huffmanCodeOfFile(f: File) {

}

private fun Collection<Int>.toHuffmanString(): String {
    val s = StringBuilder()
    for ((index, a) in withIndex())
        s.append(if (index != size-1) "$a" else "$a")
    return s.toString()
}

fun testHuffmanCode() {
    val h = HuffmanCode(2, listOf(Pair('a',0.45),Pair('b',0.13),Pair('c',0.12),Pair('d',0.16),Pair('e',0.09),Pair('f',0.05)))
    h.solve()
    h.printSolution2()
    val et = h.generateEncodeTree()
    println(et)
//    h.printSolution()


}