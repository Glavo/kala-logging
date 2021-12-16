package org.glavo.kala.logging.benchmark;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws RunnerException {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HHmmss");

        final OptionsBuilder builder = new OptionsBuilder();
        /*
        for (String arg : args) {
            builder.include(arg);
        }
         */
        builder.include("MessageFormatBenchmark");
        Options opt = builder
                .output("build/" + format.format(new Date()) + ".log")
                .build();
        new Runner(opt).run();
    }
}
