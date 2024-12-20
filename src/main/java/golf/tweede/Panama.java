package golf.tweede;

import golf.tweede.gen.java_interop_h;

import org.openjdk.jmh.annotations.Benchmark;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Panama {
    static {
        Path p = Paths.get("src/main/rust/target/release/libjava_interop.so");
        System.load(p.toAbsolutePath().toString()); // load library
    }

    /**
     * Grab a string from a MemorySegment, and free the original Rust string from
     * the pointer
     * 
     * @param segment which contains a string
     * @return the string
     */
    private static String segmentToString(MemorySegment segment) {
        String string = segment.getString(0);
        java_interop_h.freeString(segment);
        return string;
    }

    public static String doubleToStringRust(double value) {
        return segmentToString(java_interop_h.doubleToStringRust(value));
    }

    public static String doubleToStringRyu(double value) {
        return segmentToString(java_interop_h.doubleToStringRyu(value));
    }

    public static String doubleArrayToStringRyu(double[] array) {
        String output;
        try (Arena offHeap = Arena.ofConfined()) {
            // allocate off-heap memory for input array
            MemorySegment segment = offHeap.allocateFrom(ValueLayout.JAVA_DOUBLE, array);
            output = segmentToString(java_interop_h.doubleArrayToStringRyu(segment, array.length));
        } // release memory for input array

        return output;
    }

    // Benchmarks

    @Benchmark
    public String doubleToStringRustBenchmark(Main.BenchmarkState state) {
        return doubleToStringRust(state.value);
    }

    @Benchmark
    public String doubleToStringRyuBenchmark(Main.BenchmarkState state) {
        return doubleToStringRyu(state.value);
    }

    @Benchmark
    public String doubleArrayToStringRyuBenchmark(Main.BenchmarkState state) {
        return doubleArrayToStringRyu(state.array);
    }
}
