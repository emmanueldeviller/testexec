package com.xelians.testexec;

import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);

    public static final String TEST = "**************** Testing ";
    public static final String FAIL = "Fail to complete ";

    private TestUtils() {
    }

    public static String getMethod(TestInfo testInfo) {
        return testInfo.getTestMethod().get().getName();
    }



}
