package golf.tweede

import jnr.ffi.LibraryLoader
import jnr.ffi.Pointer

class JnrInterface {
    interface RustLib {
        fun doubleToStringRust(value: Double): Pointer
        fun doubleToStringRyu(value: Double): Pointer
        fun doubleArrayToStringRyu(array: DoubleArray, len: Int): Pointer
        fun freeString(string: Pointer)
    }

    companion object {
        private val lib: RustLib

        init {
            System.setProperty("jnr.ffi.library.path", "src/main/rust/target/release")
            lib = LibraryLoader.create(RustLib::class.java).load("java_interop") // load library
        }

        /**
         * Grab a string from a pointer, and free the original Rust string from the
         * pointer
         *
         * @param pointer which points a string
         * @return the string
         */
        private fun pointerToString(pointer: Pointer): String {
            val string = pointer.getString(0)
            lib.freeString(pointer)
            return string
        }

        fun doubleToStringRust(value: Double): String = pointerToString(lib.doubleToStringRust(value))
        fun doubleToStringRyu(value: Double): String = pointerToString(lib.doubleToStringRyu(value))
        fun doubleArrayToStringRyu(array: DoubleArray): String = pointerToString(lib.doubleArrayToStringRyu(array, array.size))
    }
}

fun main() {
    println(JnrInterface.doubleToStringRust(Math.PI))
    println(JnrInterface.doubleToStringRyu(Math.PI))
    println(JnrInterface.doubleArrayToStringRyu(doubleArrayOf(1.0, 2.0, Math.PI)))
}