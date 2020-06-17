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
}

open class Matrix(override val row: Int, override val col: Int, gen: (Int, Int) -> Int = { _,_ -> 0 }): AMatrix {
    private val a = Array(row * col) { gen(row, col) }
    override operator fun get(r: Int, c: Int): Int = a[r*col+c]
    override operator fun set(r: Int, c: Int, v: Int) {
        a[r*row+c] = v
    }
}

open class MatrixDelegate(val m: AMatrix, val rowOffset: Int, val colOffset: Int, override val row: Int, override val col: Int): AMatrix {
    override operator fun get(r: Int, c: Int): Int = m[r+rowOffset, c+colOffset]
    override operator fun set(r: Int, c: Int, v: Int) {
        m[r+rowOffset, c+colOffset] = v
    }
}

fun testMatrix() {

}