package golf.tweede;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import org.openjdk.jmh.annotations.Benchmark;

public class JnrInterface {
    public interface RustLib {
        Pointer doubleToStringRust(double value);

        Pointer doubleToStringRyu(double value);

        Pointer doubleArrayToStringRyu(double[] array, int len);

        void freeString(Pointer string);
    }

    private static final RustLib lib;

    static {
        System.setProperty("jnr.ffi.library.path", "src/main/rust/target/release");
        lib = LibraryLoader.create(RustLib.class).load("java_interop"); // load library
    }

    /**
     * Grab a string from a pointer, and free the original Rust string from the
     * pointer
     * 
     * @param pointer which points a string
     * @return the string
     */
    private static String pointerToString(Pointer pointer) {
        String string = pointer.getString(0);
        lib.freeString(pointer);
        return string;
    }

    public static String doubleToStringRust(double value) {
        return pointerToString(lib.doubleToStringRust(value));
    }

    public static String doubleToStringRyu(double value) {
        return pointerToString(lib.doubleToStringRyu(value));
    }

    public static String doubleArrayToStringRyu(double[] array) {
        return pointerToString(lib.doubleArrayToStringRyu(array, array.length));
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
