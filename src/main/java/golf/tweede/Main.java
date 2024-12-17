package golf.tweede;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.stream.DoubleStream;

public class Main {
    public static void main(String[] arg) {
        System.out.println(Math.PI);
        System.out.println(JniInterface.doubleToStringRust(Math.PI));
        System.out.println(JniInterface.doubleToStringRyu(Math.PI));
        System.out.println(JnrInterface.lib.doubleToStringRust(Math.PI));
        System.out.println(JnrInterface.lib.doubleToStringRyu(Math.PI));

        double[] array = {1, 2, Math.PI};
        System.out.println(String.join(" ", DoubleStream.of(array).mapToObj(Double::toString).toArray(String[]::new)));
        System.out.println(JniInterface.doubleArrayToStringRyu(array));
        System.out.println(JnrInterface.pointerToString(JnrInterface.lib.doubleArrayToStringRyu(array, array.length)));
    }

    @State(Scope.Benchmark)
    public static class MyState {
        public double value = Math.PI;
        public double[] array = new double[1_000_000];

        @Setup
        public void setup() {
            for (int i = 0; i < array.length; i++) {
                array[i] = i / 12f;
            }
        }
    }

    @Benchmark
    public String doubleToStringJavaBenchmark(MyState state) {
        return Double.toString(state.value);
    }

    @Benchmark
    public String doubleArrayToStringJavaBenchmark(MyState state) {
        return String.join(" ", DoubleStream.of(state.array).mapToObj(Double::toString).toArray(String[]::new));
    }
}
