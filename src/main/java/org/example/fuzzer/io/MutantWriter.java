package org.example.fuzzer.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class MutantWriter {

    public void write(Path outputDir, String fileName, String content) throws IOException {
        Files.createDirectories(outputDir);
        Files.writeString(outputDir.resolve(fileName), content, StandardCharsets.UTF_8);
    }
}