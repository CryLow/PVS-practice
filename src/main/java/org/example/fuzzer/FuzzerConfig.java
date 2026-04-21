package org.example.fuzzer;

import java.nio.file.Path;

public record FuzzerConfig(
        Path inputFile,
        Path outputDir,
        int maxFiles
) {}