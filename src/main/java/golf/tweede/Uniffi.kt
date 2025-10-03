package golf.tweede

import golf.tweede.Main.BenchmarkState
import org.openjdk.jmh.annotations.Benchmark
import uniffi.java_interop.doubleArrayToStringRyu
import uniffi.java_interop.doubleToStringRust
import uniffi.java_interop.doubleToStringRyu

open class Uniffi {
    @Benchmark
    fun doubleToStringRustBenchmark(state: BenchmarkState): String {
        return doubleToStringRust(state.value)
    }

    @Benchmark
    fun doubleToStringRyuBenchmark(state: BenchmarkState): String {
        return doubleToStringRyu(state.value)
    }

    @Benchmark
    fun doubleArrayToStringRyuBenchmark(state: BenchmarkState): String {
        return doubleArrayToStringRyu(state.array.toList())
    }
}

fun main() {
    println(doubleToStringRust(Math.PI))
    println(doubleToStringRyu(Math.PI))
    println(doubleArrayToStringRyu(doubleArrayOf(1.0, 2.0, Math.PI).toList()))
}

