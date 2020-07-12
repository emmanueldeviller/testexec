package com.xelians.testexec;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Emmanuel Deviller
 */
public class TestInit implements BeforeAllCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestInit.class);

    public static final String TEST_RESOURCES = "src/test/resources/";
    public static final String TEST_RESULTS = "target/test-results/";
    public static final String TEST = "**************** Testing ";
    public static final String FAIL = "Fail to complete ";

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        Path testDir = Paths.get(TEST_RESULTS);
        if (Files.notExists(testDir)) {
            LOGGER.info("Creating test results directory: " + TEST_RESULTS);
            Files.createDirectories(testDir);
        }
    }

}
