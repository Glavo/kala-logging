package org.glavo.kala.logging.benchmark;

import kala.logging.util.MessageFormatter;
import org.openjdk.jmh.annotations.*;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;


/*
 * Result:
 * MessageFormatBenchmark.compiledJDKMessageFormat          79.465 ns/op
 * MessageFormatBenchmark.compiledKalaMessageFormatter      52.088 ns/op
 * MessageFormatBenchmark.jdkMessageFormat                 236.535 ns/op
 * MessageFormatBenchmark.kalaMessageFormatter1             71.296 ns/op
 * MessageFormatBenchmark.kalaMessageFormatter2             89.468 ns/op
 * MessageFormatBenchmark.printfFormat                     128.105 ns/op
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 4, jvmArgsAppend = {"-XX:+UseG1GC", "-Xms1g", "-Xmx1g"})
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 3)
@State(Scope.Benchmark)
public class MessageFormatBenchmark {
    private static final String JDK_FORMAT = "Hello {0}! Goodbye {1}!";
    private static final String KALA_FORMAT = "Hello {}! Goodbye {}!";
    private static final String KALA_FORMAT2 = "Hello {0}! Goodbye {1}!";
    private static final String PRINTF_FORMAT = "Hello %s! Goodbye %s!";

    private static final MessageFormat compiledJDKFormat = new MessageFormat("Hello {0}! Goodbye {1}!");
    private static final MessageFormatter compiledKalaFormatter = MessageFormatter.parse("Hello {}! Goodbye {}!");

    @Param({"Glavo", "Foo"})
    String name1;
    @Param({"Administrator", "Guest", "Docker"})
    String name2;

    @Benchmark
    public String printfFormat() {
        return String.format("Hello %s! Goodbye %s!", name1, name2);
    }

    @Benchmark
    public String jdkMessageFormat() {
        return MessageFormat.format("Hello {0}! Goodbye {1}!", name1, name2);
    }

    @Benchmark
    public String compiledJDKMessageFormat() {
        return compiledJDKFormat.format(new Object[]{name1, name2});
    }

    @Benchmark
    public String kalaMessageFormatter1() {
        return MessageFormatter.format("Hello {}! Goodbye {}!", name1, name2);
    }

    @Benchmark
    public String kalaMessageFormatter2() {
        return MessageFormatter.format("Hello {0}! Goodbye {1}!", name1, name2);
    }

    @Benchmark
    public String compiledKalaMessageFormatter() {
        return compiledKalaFormatter.format(new Object[]{name1, name2});
    }
}
