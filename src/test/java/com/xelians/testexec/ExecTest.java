package com.xelians.testexec;

import static com.xelians.testexec.TestInit.FAIL;
import static com.xelians.testexec.TestInit.TEST;
import static com.xelians.testexec.TestUtils.getMethod;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Emmanuel Deviller
 */
@ExtendWith(TestInit.class)
public class ExecTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecTest.class);

    @Test
    public void testTrue() {
        assertEquals("true", "true");
    }

    @Test
    public void testExecOne() throws IOException {
        LOGGER.info("************* testExecOne");

        try {
            Path script = Paths.get(TestInit.TEST_RESOURCES + "scan-clamav.sh");
            Path path = Paths.get(TestInit.TEST_RESOURCES + "files/csvsip_fntc.zip");

            LOGGER.info("path: " + path);
            ExecutionOutput eo = JavaExecuteScript.executeCommand(script.toString(), path.toString(), 30000);
            LOGGER.info(eo.toString());
        }
        catch (Exception ex) {
            LOGGER.warn(FAIL, ex);
            fail(FAIL);
        }
    }

    @Test
    public void testExecBadOne() throws IOException {
        LOGGER.info("************* testExecBadOne");

        try {
            Path script = Paths.get(TestInit.TEST_RESOURCES + "scan-clamav.sh");
            Path path = Paths.get(TestInit.TEST_RESOURCES + "files/eicar_com.zip");

            LOGGER.info("path: " + path);
            ExecutionOutput eo = JavaExecuteScript.executeCommand(script.toString(), path.toString(), 30000);
            LOGGER.info(eo.toString());
        }
        catch (Exception ex) {
            LOGGER.warn(FAIL, ex);
            fail(FAIL);
        }
    }

    @Test
    public void testExecAll() throws IOException {
        LOGGER.info("************* testExecAll");

        try {

            Path script = Paths.get(TestInit.TEST_RESOURCES + "scan-clamav.sh");
            Path path = Paths.get(TestInit.TEST_RESOURCES + "files");

            Files.walk(path).filter(Files::isRegularFile).forEach(p -> {
                ExecutionOutput eo = JavaExecuteScript.executeCommand(script.toString(), p.toString(), 30000);
                LOGGER.info(eo.toString());
            });
        }
        catch (Exception ex) {
            LOGGER.warn(FAIL, ex);
            fail(FAIL);
        }
    }

    @Test
    public void testExecParallel() throws IOException {
        LOGGER.info("************* testExecParallel");
        
        try {

            Path script = Paths.get(TestInit.TEST_RESOURCES + "scan-clamav.sh");
            Path path = Paths.get(TestInit.TEST_RESOURCES + "files");

            Files.walk(path).parallel().filter(Files::isRegularFile).forEach(p -> {
                ExecutionOutput eo = JavaExecuteScript.executeCommand(script.toString(), p.toString(), 30000);
                LOGGER.info(eo.toString());
            });
        }
        catch (Exception ex) {
            LOGGER.warn(FAIL, ex);
            fail(FAIL);
        }
    }

}
