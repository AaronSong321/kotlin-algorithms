package my.ds

import java.lang.IllegalArgumentException

open class Matrix(val row: Int, val col: Int) {
    private val a = Array(row * col) { 0 }
    open operator fun get(r: Int, c: Int): Int = a[r*row+c]
    open operator fun set(r: Int, c: Int, v: Int) {
        a[r*row+c] = v
    }
    operator fun plus(o: Matrix): Matrix{
        if (row != o.row)
            throw IllegalArgumentException("left.row $row != right.row ${o.row}")
        if (col != o.col)
            throw IllegalArgumentException("left.col $col != right.col ${o.col}")
        val ans = Matrix(row, col)
        for (r in 0 until row)
            for (c in 0 until col)
                ans[r, c] = o[r, c] + this[r, c]
        return ans
    }

    operator fun times(o: Matrix): Matrix {
        if (col != o.row)
            throw IllegalArgumentException("left.col $col != right.row ${o.row}")
        val ans = Matrix(row, o.col)

        return ans
    }
}

open class MatrixDelegate(private val m: Matrix, val rowOffset: Int, val colOffset: Int) {
    open operator fun get(r: Int, c: Int): Int = m[r+rowOffset, c+colOffset]
    open operator fun set(r: Int, c: Int, v: Int) {
        m[r+rowOffset, c+colOffset] = v
    }
}