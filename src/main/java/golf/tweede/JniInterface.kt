package golf.tweede

import java.nio.file.Paths

class JniInterface {
    external fun doubleToStringRust(v: Double): String
    external fun doubleToStringRyu(v: Double): String
    external fun doubleArrayToStringRyu(v: DoubleArray): String

    init {
        val p = Paths.get("src/main/rust/target/release/libjava_interop.so")
        System.load(p.toAbsolutePath().toString()) // load library
    }
}

fun main() {
    val jni = JniInterface()
    println(jni.doubleToStringRust(Math.PI))
    println(jni.doubleToStringRyu(Math.PI))
    println(jni.doubleArrayToStringRyu(doubleArrayOf(1.0, 2.0, Math.PI)))
}
