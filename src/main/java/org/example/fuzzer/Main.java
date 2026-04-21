package org.example.fuzzer;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Usage: java -jar mutation-fuzzer.jar <input.java> <outputDir> <count>");
            System.err.println("Example: java -jar mutation-fuzzer.jar samples/V6001TypicalCases.java samples/out 10");
            System.exit(1);
        }

        Path inputFile = Path.of(args[0]);
        Path outputDir = Path.of(args[1]);
        int maxFiles   = Integer.parseInt(args[2]);

        FuzzerConfig config = new FuzzerConfig(inputFile, outputDir, maxFiles);
        new MutationEngine().run(config);
    }
}