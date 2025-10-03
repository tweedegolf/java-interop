package golf.tweede

import golf.tweede.Main.BenchmarkState
import golf.tweede.gen.java_interop_h
import org.openjdk.jmh.annotations.Benchmark
import java.lang.foreign.Arena
import java.lang.foreign.MemorySegment
import java.lang.foreign.ValueLayout


open class Panama {
    companion object {
        /**
         * Grab a string from a MemorySegment, and free the original Rust string from
         * the pointer
         *
         * @param segment which contains a string
         * @return the string
         */
        private fun segmentToString(segment: MemorySegment): String {
            val string = segment.getString(0)
            java_interop_h.freeString(segment)
            return string
        }

        fun doubleToStringRust(value: Double): String = segmentToString(java_interop_h.doubleToStringRust(value))

        fun doubleToStringRyu(value: Double): String = segmentToString(java_interop_h.doubleToStringRyu(value))

        fun doubleArrayToStringRyu(array: DoubleArray): String {
            val output: String
            Arena.ofConfined().use { offHeap ->
                // allocate off-heap memory for input array
                val segment = offHeap.allocateFrom(ValueLayout.JAVA_DOUBLE, *array)
                output = segmentToString(java_interop_h.doubleArrayToStringRyu(segment, array.size.toLong()))
            }
            return output
        }
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
    println(Panama.doubleToStringRust(Math.PI))
    println(Panama.doubleToStringRyu(Math.PI))
    println(Panama.doubleArrayToStringRyu(doubleArrayOf(1.0, 2.0, Math.PI)))
}
