package golf.tweede;

import org.openjdk.jmh.annotations.Benchmark;

import java.nio.file.Path;
import java.nio.file.Paths;

public class JniInterface {
    public static native String doubleToStringRust(double v);

    public static native String doubleToStringRyu(double v);

    public static native String doubleArrayToStringRyu(double[] v);

    static {
        Path p = Paths.get("src/main/rust/target/release/libjava_interop.so");
        System.load(p.toAbsolutePath().toString()); // load library
    }

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
