package golf.tweede

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import java.lang.String
import kotlin.Array
import kotlin.Double
import kotlin.DoubleArray
import kotlin.doubleArrayOf
import kotlin.io.println

import uniffi.java_interop.doubleToStringRust
import uniffi.java_interop.doubleToStringRyu
import uniffi.java_interop.doubleArrayToStringRyu

open class Main {
    @State(Scope.Benchmark)
    open class BenchmarkState {
        var value: Double = Math.PI
        var array: DoubleArray = DoubleArray(1000000)

        @Setup
        fun setup() {
            for (i in array.indices) {
                array[i] = (i / 12f).toDouble()
            }
        }
    }

    // Benchmarks
    @Benchmark
    fun doubleToStringKotlinBenchmark(state: BenchmarkState): kotlin.String {
        return state.value.toString()
    }

    @Benchmark
    fun doubleArrayToStringKotlinBenchmark(state: BenchmarkState): kotlin.String {
        return String.join(" ", state.array.map { it.toString() })
    }
}

fun main(arg: Array<kotlin.String>) {
    println(Math.PI)
    println("JNI")
    println(JniInterface.doubleToStringRust(Math.PI))
    println(JniInterface.doubleToStringRyu(Math.PI))
    println("JNR-FFI")
    println(JnrInterface.doubleToStringRust(Math.PI))
    println(JnrInterface.doubleToStringRyu(Math.PI))
    println("Project Panama")
    println(Panama.doubleToStringRust(Math.PI))
    println(Panama.doubleToStringRyu(Math.PI))
    println("UniFFI")
    println(doubleToStringRust(Math.PI))
    println(doubleToStringRyu(Math.PI))

    val array = doubleArrayOf(1.0, 2.0, Math.PI)
    println(String.join(" ", array.map { it.toString() }))
    println(JniInterface.doubleArrayToStringRyu(array))
    println(JnrInterface.doubleArrayToStringRyu(array))
    println(Panama.doubleArrayToStringRyu(array))
    println(doubleArrayToStringRyu(array.toList()))
}
