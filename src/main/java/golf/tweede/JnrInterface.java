package golf.tweede;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import org.openjdk.jmh.annotations.Benchmark;

public class JnrInterface {
    public interface RustLib {
        String doubleToStringRust(double value); // These also leak a bit of memory
        String doubleToStringRyu(double value);
        // String doubleArrayToStringRyu(double[] array, int len); // This is bad and leaks lots of memory
        Pointer doubleArrayToStringRyu(double[] array, int len); // This allows us to free the memory
        void freeString(Pointer string);
    }

    /**
     * Grab a string from a pointer, and free the original Rust string from the pointer
     * @param pointer which points a string
     * @return the string
     */
    public static String pointerToString(Pointer pointer) {
        String string = pointer.getString(0);
        lib.freeString(pointer);
        return string;
    }

    public static RustLib lib;

    static {
        System.setProperty("jnr.ffi.library.path", "src/main/rust/target/release");
        lib = LibraryLoader.create(RustLib.class).load("java_interop"); // load library
    }

    @Benchmark
    public String doubleToStringRustBenchmark(Main.MyState state) {
        return lib.doubleToStringRust(state.value);
    }

    @Benchmark
    public String doubleToStringRyuBenchmark(Main.MyState state) {
        return lib.doubleToStringRyu(state.value);
    }

    @Benchmark
    public String doubleArrayToStringRyuBenchmark(Main.MyState state) {
        return pointerToString(lib.doubleArrayToStringRyu(state.array, state.array.length));
    }
}
