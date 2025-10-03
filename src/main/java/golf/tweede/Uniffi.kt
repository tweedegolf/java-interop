package golf.tweede

import uniffi.java_interop.doubleToStringRust
import uniffi.java_interop.doubleToStringRyu
import uniffi.java_interop.doubleArrayToStringRyu

fun main() {
    println(doubleToStringRust(Math.PI))
    println(doubleToStringRyu(Math.PI))
    println(doubleArrayToStringRyu(doubleArrayOf(1.0, 2.0, Math.PI).toList()))
}
