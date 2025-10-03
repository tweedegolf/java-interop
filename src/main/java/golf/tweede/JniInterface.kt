package golf.tweede

import golf.tweede.Main.BenchmarkState
import org.openjdk.jmh.annotations.Benchmark
import java.nio.file.Paths


open class JniInterface {
    companion object {
        init {
            val p = Paths.get("src/main/rust/target/release/libjava_interop.so")
            System.load(p.toAbsolutePath().toString()) // load library
        }

        @JvmStatic
        external fun doubleToStringRust(v: Double): String
        @JvmStatic
        external fun doubleToStringRyu(v: Double): String
        @JvmStatic
        external fun doubleArrayToStringRyu(v: DoubleArray): String
    }

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
        return doubleArrayToStringRyu(state.array)
    }
}

fun main() {
    println(JniInterface.doubleToStringRust(Math.PI))
    println(JniInterface.doubleToStringRyu(Math.PI))
    println(JniInterface.doubleArrayToStringRyu(doubleArrayOf(1.0, 2.0, Math.PI)))
}
