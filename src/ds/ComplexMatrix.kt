package my.ds

import org.kotlinmath.*
import kotlin.math.*

class CoefficientPolynomial(val degree: Int, init: (Int) -> Complex = { ZERO }) {
    private val coefficient = Array<Complex>(degree) { init(it) }
    val regularOrder: Int
        get() {
            val d = ceil(log(degree.toDouble(), 2.toDouble()))
            return 2.toDouble().pow(d).toInt()
        }
    operator fun get(a: Int) = coefficient[a]
    operator fun set(a: Int, v: Complex) {
        coefficient[a] = v
    }

    fun extendTo(newDegree: Int): CoefficientPolynomial {
        return CoefficientPolynomial(newDegree) { if (it < degree) get(it) else ZERO }
    }
    fun extendToRegular(): CoefficientPolynomial {
        val f = regularOrder
        return if (f > degree) extendTo(f) else this
    }

    fun recursiveFft(targetDegree: Int = regularOrder): ValuePolynomial {
        val s = extendTo(targetDegree)
        if (targetDegree == 1)
            return ValuePolynomial(1) { s[0] }
        val delta = pow(E, 2.toDouble()*PI*I/targetDegree)
        var omega = complex(1.0, 0.0)
        val a0 = CoefficientPolynomial(targetDegree/2) { s[it*2] }
        val a1 = CoefficientPolynomial(targetDegree/2) { s[it*2+1] }
        val y0 = a0.recursiveFft()
        val y1 = a1.recursiveFft()
        val y = ValuePolynomial(targetDegree)
        for (k in 0 until targetDegree/2) {
            val yk = y1[k] * omega
            y[k] = y0[k] + yk
            y[k + targetDegree / 2] = y0[k] - yk
            omega *= delta
        }
        return y
    }

    override fun toString(): String {
        val s = StringBuilder("(")
        for ((index, a) in coefficient.withIndex()) {
            val p = a.asStringNoSmallPart("%.2f")
            s.append(if (index == 0) "$p, " else if (index != degree - 1) "$p(x^$index), " else "$p(x^$index))")
        }
        return s.toString()
    }

    operator fun plus(other: CoefficientPolynomial): CoefficientPolynomial {
        val r = max(degree, other.degree)
        val extendThis = extendTo(r)
        val extendOther = other.extendTo(r)
        return CoefficientPolynomial(r) { extendThis[it] + extendOther[it] }
    }
    operator fun times(other: CoefficientPolynomial): CoefficientPolynomial {
        val r = max(regularOrder, other.regularOrder)
        val fftThis = extendTo(r + r).iterativeFft()
        val fftOther = other.extendTo(r + r).iterativeFft()
        return (fftThis * fftOther).inverseRecursiveFft().truncate()
    }

    fun truncate(): CoefficientPolynomial {
        var lastNonZeroIndex = degree
        while (lastNonZeroIndex >= 0) {
            val co = coefficient[lastNonZeroIndex-1]
            if (abs(co.re) < 0.000001 && abs(co.im) < 0.000001)
                --lastNonZeroIndex
            else break
        }
        return CoefficientPolynomial(lastNonZeroIndex) { coefficient[it] }
    }

    fun iterativeFft(targetDegree: Int = regularOrder): ValuePolynomial {
        val ext = extendTo(targetDegree)
        val digit = ceil(log(targetDegree.toDouble(), 2.toDouble())).toInt()
        val copy = ValuePolynomial(targetDegree) { ext[bitReverse(it, digit)] }

        var m = 1
        for (s in 0 until digit) {
            m = m shl 1
            val deltaOmega = pow(E, 2.I * PI / m)
            for (k in 0 until targetDegree step m) {
                var omega = 1.R
                val halfM = m shr 1
                for (j in 0 until halfM) {
                    val t = omega * copy[k + j + halfM]
                    val u = copy[k + j]
                    copy[k + j] = u + t
                    copy[k + j + halfM] = u - t
                    omega *= deltaOmega
                }
            }
        }
        return copy
    }
}

fun bitReverse(a: Int, digit: Int): Int {
    var b = 0
    var d = a
    for (i in 0 until digit) {
        b = (b shl 1) + (d and 1)
        d = d shr 1
    }
    return b
}

fun coefficientPolyOf(vararg a: Complex): CoefficientPolynomial {
    return CoefficientPolynomial(a.size) { a[it] }
}

class ValuePolynomial(val degree: Int, init: (Int) -> Complex = { ZERO }) {
    private val values = Array(degree) { init(it) }

    operator fun get(a: Int) = values[a]
    operator fun set(a: Int, v: Complex) {
        values[a] = v
    }

    operator fun plus(other: ValuePolynomial) : ValuePolynomial {
        if (degree != other.degree)
            throw IllegalArgumentException("$degree and ${other.degree} mismatches.")
        return ValuePolynomial(degree) { get(it) + other[it] }
    }
    operator fun times(other: ValuePolynomial) : ValuePolynomial {
        if (degree != other.degree)
            throw IllegalArgumentException("$degree and ${other.degree} mismatches.")
        return ValuePolynomial(degree) { get(it) * other[it] }
    }

    private fun innerReverseRecursiveFft(divideByCount: Boolean = false) : CoefficientPolynomial {
        if (degree == 1)
            return CoefficientPolynomial(1) { this[0] }
        val delta = pow(E, -(2.toDouble()*PI*I/degree))
        var omega = complex(1.0, 0.0)
        val a0 = ValuePolynomial(degree/2) { this[it*2] }
        val a1 = ValuePolynomial(degree/2) { this[it*2+1] }
        val y0 = a0.innerReverseRecursiveFft()
        val y1 = a1.innerReverseRecursiveFft()
        val y = CoefficientPolynomial(degree)
        for (k in 0 until degree/2) {
            val yk = y1[k] * omega
            if (divideByCount) {
                y[k] = (y0[k] + yk) / degree
                y[k + degree / 2] = (y0[k] - yk) / degree
            }
            else {
                y[k] = (y0[k] + yk)
                y[k + degree / 2] = (y0[k] - yk)
            }
            omega *= delta
        }
        return y
    }
    fun inverseRecursiveFft(): CoefficientPolynomial {
        return innerReverseRecursiveFft(true)
    }

    override fun toString(): String {
        val s = StringBuilder("{")
        for ((index, a) in values.withIndex()) {
            val k = a.asStringNoSmallPart("%.2f")
            s.append(if (index != degree - 1) "$k, " else "$k}")
        }
        return s.toString()
    }

    fun inverseIterativeFft(): CoefficientPolynomial {
        throw NotImplementedError("Not finished yet")
//        val digit = ceil(log(degree.toDouble(), 2.toDouble())).toInt()
//        val copy = CoefficientPolynomial(degree) { values[it] }
//
//        var m = 1
//        for (s in 0 until digit) {
//            m = m shl 1
//            val deltaOmega = pow(E, (-2).I * PI / m)
//            for (k in 0 until degree step m) {
//                var omega = 1.R
//                val halfM = m shr 1
//                for (j in 0 until halfM) {
//                    val t = omega * copy[k + j + halfM]
//                    val u = copy[k + j]
//                    copy[k + j] = u + t
//                    copy[k + j + halfM] = u - t
//                    omega *= deltaOmega
//                }
//            }
//        }
//        return CoefficientPolynomial(degree) { copy[bitReverse(it, digit)] / degree }
    }
}

fun valuePolyOf(vararg a: Complex): ValuePolynomial {
    return ValuePolynomial(a.size) { a[it] }
}

fun List<CoefficientPolynomial>.product(): CoefficientPolynomial {
    return if (size > 2) slice(0 until size / 2).product() * slice(size / 2 until size).product()
    else get(0) * get(1)
}

fun calculatePolynomialFromZeroPoint(zeros: List<Complex>): CoefficientPolynomial {
    val oldOrder = zeros.size
    val d = ceil(log((oldOrder+1).toDouble(), 2.toDouble()))
    val newOrder = 2.toDouble().pow(d).toInt()
    val t = List(newOrder) {
        if (it < oldOrder) coefficientPolyOf(-zeros[it], 1.R)
        else coefficientPolyOf(1.R)
    }
    return t.product().truncate()
}

fun testFFT() {
    val c1 = coefficientPolyOf((-10).R, 1.R, (-1).R, 7.R)
    val c2 = coefficientPolyOf(3.R, (-6).R, 8.R)
    println(c1.recursiveFft().inverseRecursiveFft())
    println(c1.iterativeFft().inverseRecursiveFft())
    println(c1 * c2)
    println(c2.recursiveFft(8).inverseRecursiveFft().truncate())
    println(calculatePolynomialFromZeroPoint(listOf(3.R,6.R,3.R,8.R,7.R)))
}