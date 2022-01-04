package net.minestom.jmh.palette;

import net.minestom.server.instance.palette.Palette;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1500, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1500, timeUnit = TimeUnit.MILLISECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class PaletteGetBenchmark {

    @Param({"4", "8", "16"})
    public int dimension;

    private Palette palette;

    @Setup
    public void setup() {
        palette = Palette.newPalette(dimension, 15, 4, 1);

        int value = 0;
        final int dimension = palette.dimension();
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                for (int z = 0; z < dimension; z++) {
                    palette.set(x, y, z, value++);
                }
            }
        }
    }

    @Benchmark
    public void read(Blackhole blackHole) {
        final int dimension = palette.dimension();
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                for (int z = 0; z < dimension; z++) {
                    blackHole.consume(palette.get(x, y, z));
                }
            }
        }
    }

    @Benchmark
    public void readAll(Blackhole blackHole) {
        palette.getAll((x, y, z, value) -> blackHole.consume(value));
    }
}