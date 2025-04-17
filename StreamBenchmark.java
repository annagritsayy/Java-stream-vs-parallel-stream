package org.example;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class StreamBenchmark {

    private List<Integer> data;

    @Setup(Level.Invocation)
    public void setUp() {
        Random random = new Random();
        data = IntStream.range(0, 10_000_000)
                .map(i -> random.nextInt(100) + 1)
                .boxed()
                .collect(Collectors.toList());
    }

    @Benchmark
    public int sumStream() {
        return data.stream().mapToInt(Integer::intValue).sum();
    }

    @Benchmark
    public double averageStream() {
        return data.stream().mapToInt(Integer::intValue).average().orElse(0);
    }

    @Benchmark
    public double stdDevStream() {
        double avg = averageStream();
        return Math.sqrt(data.stream()
                .mapToDouble(i -> Math.pow(i - avg, 2))
                .average().orElse(0));
    }

    @Benchmark
    public List<Integer> multiplyByTwoStream() {
        return data.stream().map(i -> i * 2).collect(Collectors.toList());
    }

    @Benchmark
    public List<Integer> filterStream() {
        return data.stream()
                .filter(i -> i % 2 == 0 && i % 3 == 0)
                .collect(Collectors.toList());
    }

    @Benchmark
    public int sumParallelStream() {
        return data.parallelStream().mapToInt(Integer::intValue).sum();
    }

    @Benchmark
    public double averageParallelStream() {
        return data.parallelStream().mapToInt(Integer::intValue).average().orElse(0);
    }

    @Benchmark
    public double stdDevParallelStream() {
        double avg = averageParallelStream();
        return Math.sqrt(data.parallelStream()
                .mapToDouble(i -> Math.pow(i - avg, 2))
                .average().orElse(0));
    }

    @Benchmark
    public List<Integer> multiplyByTwoParallelStream() {
        return data.parallelStream().map(i -> i * 2).collect(Collectors.toList());
    }

    @Benchmark
    public List<Integer> filterParallelStream() {
        return data.parallelStream()
                .filter(i -> i % 2 == 0 && i % 3 == 0)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        StreamBenchmark benchmark = new StreamBenchmark();
        benchmark.setUp();

        System.out.println("Sum (Stream): " + benchmark.sumStream());
        System.out.println("Average (Stream): " + benchmark.averageStream());
        System.out.println("Standard Deviation (Stream): " + benchmark.stdDevStream());
        System.out.println("Multiply by 2 (Stream): " + benchmark.multiplyByTwoStream().subList(0, 10));
        System.out.println("Filtered (Stream): " + benchmark.filterStream().subList(0, 10));

        System.out.println("Sum (Parallel Stream): " + benchmark.sumParallelStream());
        System.out.println("Average (Parallel Stream): " + benchmark.averageParallelStream());
        System.out.println("Standard Deviation (Parallel Stream): " + benchmark.stdDevParallelStream());
        System.out.println("Multiply by 2 (Parallel Stream): " + benchmark.multiplyByTwoParallelStream().subList(0, 10));
        System.out.println("Filtered (Parallel Stream): " + benchmark.filterParallelStream().subList(0, 10));
    }
}



