package com.example.fakecommerce;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DotenvLoadingTest {

    @Test
    void testDotenvIgnoresMissingFileGracefully() {
        assertDoesNotThrow(() -> {
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        });
    }

    @Test
    void testDotenvLoadsCustomFileAndSetsSystemProperties() throws IOException {
        Path tempDir = Files.createTempDirectory("dotenv-test");
        File envFile = new File(tempDir.toFile(), ".env");

        try (FileWriter writer = new FileWriter(envFile)) {
            writer.write("TEST_ENV_VAR=fakecommerce_test_val\n");
            writer.write("ANOTHER_TEST_VAR=12345\n");
        }

        Dotenv dotenv = Dotenv.configure()
                .directory(tempDir.toString())
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        assertEquals("fakecommerce_test_val", System.getProperty("TEST_ENV_VAR"));
        assertEquals("12345", System.getProperty("ANOTHER_TEST_VAR"));

        // Clean up
        System.clearProperty("TEST_ENV_VAR");
        System.clearProperty("ANOTHER_TEST_VAR");
        envFile.delete();
        tempDir.toFile().delete();
    }
}
