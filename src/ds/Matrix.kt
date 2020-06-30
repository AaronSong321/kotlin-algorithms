package my.ds

import java.lang.IllegalArgumentException

interface AMatrix {
    val row: Int
    val col: Int
    operator fun get(r: Int, c: Int): Int
    operator fun set(r: Int, c: Int, v: Int)
    operator fun plus(o: AMatrix): Matrix {
        if (row != o.row)
            throw IllegalArgumentException("left.row $row != right.row ${o.row}")
        if (col != o.col)
            throw IllegalArgumentException("left.col $col != right.col ${o.col}")
        return Matrix(row, col) { r, c -> o[r,c] + get(r,c) }
    }
    operator fun minus(o: AMatrix): Matrix {
        if (row != o.row)
            throw IllegalArgumentException("left.row $row != right.row ${o.row}")
        if (col != o.col)
            throw IllegalArgumentException("left.col $col != right.col ${o.col}")
        return Matrix(row, col) { r, c -> o[r,c] - get(r,c) }
    }
    operator fun unaryMinus(): Matrix {
        return Matrix(row, col) { r, c -> -get(r, c) }
    }
    fun transpose() = Matrix(col, row) { r, c -> get(c, r) }
    fun assign(o: AMatrix) {
        if (row != o.row)
            throw IllegalArgumentException("left.row $row != right.row ${o.row}")
        if (col != o.col)
            throw IllegalArgumentException("left.col $col != right.col ${o.col}")
        for (r in 0 until row)
            for (c in 0 until col)
                set(r, c, o[r, c])
    }

    operator fun times(o: AMatrix): Matrix {
        if (col != o.row)
            throw IllegalArgumentException("left.col $col != right.row ${o.row}")
        return matrixMultiplyPlain(o)
    }
    fun matrixMultiplyPlain(o: AMatrix): Matrix {
        val ans = Matrix(row, o.col)
        for (i in 0 until row)
            for (k in 0 until o.col){
                var t = 0
                for (j in 0 until col)
                    t += get(i, j) * o[j, k]
                ans[i, k] = t
            }
        return ans
    }

    fun getSlices(a: IntRange, b: IntRange): MatrixDelegate {
        return MatrixDelegate(this, a.first, b.first, a.last-a.first, b.last-b.first)
    }
    operator fun get(rows: IntRange, cols: IntRange): MatrixDelegate = getSlices(rows, cols)

    fun toLatexString(): String {
        val s = StringBuilder("$$\\left[\\begin{array}{")
        s.append(CharArray(col) {'l'} )
        s.append("} ")
        for (i in 0 until row) {
            for (j in 0 until col) {
                val g = if (get(i,j) < Int.MAX_VALUE/3) get(i,j).toString() else "\\infty"
                s.append(if (j != col-1) "$g & " else "$g \\\\ ")
            }
        }
        s.append("\\end{array}\\right]$$")
        return s.toString()
    }

    fun equalsPrivate(o: Any?): Boolean {
        if (o == null || o !is AMatrix)
            return false
        if (row != o.row || col != o.col)
            return false
        for (i in 0 until row)
            for (j in 0 until col)
                if (get(i,j) != o[i,j])
                    return false
        return true
    }
    override operator fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}

open class Matrix(r: Int, c: Int, init: (Int, Int) -> Int = { _,_ -> 0 }): AMatrix {
    override val row: Int = r
    override val col: Int = c
    constructor(o: Matrix): this(o.row, o.col, {r, c->o[r, c]})
    private val a = IntArray(r * c) {
        init(it / col, it - it / col * col)
    }
    override operator fun get(r: Int, c: Int): Int = a[r*col+c]
    override operator fun set(r: Int, c: Int, v: Int) {
        a[r*col+c] = v
    }

    override fun equals(other: Any?): Boolean = equalsPrivate(other)
    override fun hashCode(): Int {
        return ((row.hashCode() shl 16) and (col.hashCode() shl 8)) and get(row-1, col-1).hashCode()
    }
}

open class MatrixDelegate(val m: AMatrix, val rowOffset: Int, val colOffset: Int, override val row: Int, override val col: Int): AMatrix {
    override operator fun get(r: Int, c: Int): Int = m[r+rowOffset, c+colOffset]
    override operator fun set(r: Int, c: Int, v: Int) {
        m[r+rowOffset, c+colOffset] = v
    }

    override fun equals(other: Any?): Boolean = equalsPrivate(other)
    override fun hashCode(): Int {
        return ((row.hashCode() shl 16) and (col.hashCode() shl 8)) and get(row-1, col-1).hashCode()
    }
}

fun println(a: AMatrix) {
    println("Matrix(${a.row}, ${a.col})")
    for (i in 0 until a.row) {
        print(if (i == 0) "[" else " ")
        for (j in 0 until a.col) {
            print(if (j != a.col-1) "${a[i,j]}, " else "${a[i,j]}")
        }
        if (i == a.row-1) println("]")
        else println()
    }
}

interface IBooleanMatrix {
    val row: Int
    val col: Int
    operator fun get(r: Int, c: Int): Boolean
    operator fun set(r: Int, c: Int, v: Boolean)
    operator fun unaryMinus(): BooleanMatrix {
        return BooleanMatrix(row, col) { r, c -> !get(r, c) }
    }
    fun transpose() = BooleanMatrix(col, row) { r, c -> get(c, r) }
    fun assign(o: IBooleanMatrix) {
        if (row != o.row)
            throw IllegalArgumentException("left.row $row != right.row ${o.row}")
        if (col != o.col)
            throw IllegalArgumentException("left.col $col != right.col ${o.col}")
        for (r in 0 until row)
            for (c in 0 until col)
                set(r, c, o[r, c])
    }

    fun getSlices(a: IntRange, b: IntRange): BooleanMatrixDelegate {
        return BooleanMatrixDelegate(this, a.first, b.first, a.last-a.first, b.last-b.first)
    }
    operator fun get(rows: IntRange, cols: IntRange): BooleanMatrixDelegate = getSlices(rows, cols)

    fun toLatexString(): String {
        val s = StringBuilder("$$\\left[\\begin{array}{")
        s.append(CharArray(col) {'l'} )
        s.append("} ")
        for (i in 0 until row) {
            for (j in 0 until col) {
                val g = get(i, j)
                s.append(if (j != col-1) "$g & " else "$g \\\\ ")
            }
        }
        s.append("\\end{array}\\right]$$")
        return s.toString()
    }

    fun equalsPrivate(o: Any?): Boolean {
        if (o == null || o !is IBooleanMatrix)
            return false
        if (row != o.row || col != o.col)
            return false
        for (i in 0 until row)
            for (j in 0 until col)
                if (get(i,j) != o[i,j])
                    return false
        return true
    }
    override operator fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}

class BooleanMatrix(r: Int, c: Int, init: (Int, Int) -> Boolean = { _,_ -> false }): IBooleanMatrix {
    override val row: Int = r
    override val col: Int = c
    constructor(o: IBooleanMatrix): this(o.row, o.col, {r, c->o[r, c]})
    private val a = BooleanArray(r * c) {
        init(it / col, it - it / col * col)
    }
    override operator fun get(r: Int, c: Int): Boolean = a[r*col+c]
    override operator fun set(r: Int, c: Int, v: Boolean) {
        a[r*col+c] = v
    }
    override fun equals(other: Any?): Boolean = equalsPrivate(other)
    override fun hashCode(): Int {
        return ((row.hashCode() shl 16) and (col.hashCode() shl 8)) and get(row-1, col-1).hashCode()
    }
}

open class BooleanMatrixDelegate(val m: IBooleanMatrix, val rowOffset: Int, val colOffset: Int, override val row: Int, override val col: Int): IBooleanMatrix {
    override operator fun get(r: Int, c: Int): Boolean = m[r+rowOffset, c+colOffset]
    override operator fun set(r: Int, c: Int, v: Boolean) {
        m[r+rowOffset, c+colOffset] = v
    }
    override fun equals(other: Any?): Boolean = equalsPrivate(other)
    override fun hashCode(): Int {
        return ((row.hashCode() shl 16) and (col.hashCode() shl 8)) and get(row-1, col-1).hashCode()
    }
}

fun println(a: IBooleanMatrix) {
    println("BooleanMatrix(${a.row}, ${a.col})")
    for (i in 0 until a.row) {
        print(if (i == 0) "[" else " ")
        for (j in 0 until a.col) {
            val g = if (a[i, j]) "T" else "F"
            print(if (j != a.col-1) "$g, " else g)
        }
        if (i == a.row-1) println("]")
        else println()
    }
}
