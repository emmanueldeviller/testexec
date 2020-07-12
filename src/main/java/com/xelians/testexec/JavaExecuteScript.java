/*
 * Copyright French Prime minister Office/SGMAP/DINSIC/Vitam Program (2015-2020)
 *
 * contact.vitam@culture.gouv.fr
 *
 * This software is a computer program whose purpose is to implement a digital archiving back-office system managing
 * high volumetry securely and efficiently.
 *
 * This software is governed by the CeCILL 2.1 license under French law and abiding by the rules of distribution of free
 * software. You can use, modify and/ or redistribute the software under the terms of the CeCILL 2.1 license as
 * circulated by CEA, CNRS and INRIA at the following URL "https://cecill.info".
 *
 * As a counterpart to the access to the source code and rights to copy, modify and redistribute granted by the license,
 * users are provided only with a limited warranty and the software's author, the holder of the economic rights, and the
 * successive licensors have only limited liability.
 *
 * In this respect, the user's attention is drawn to the risks associated with loading, using, modifying and/or
 * developing or reproducing the software by the user in light of its specific status of free software, that may mean
 * that it is complicated to manipulate, and that also therefore means that it is reserved for developers and
 * experienced professionals having in-depth computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling the security of their systems and/or data
 * to be ensured and, more generally, to use and operate it in the same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had knowledge of the CeCILL 2.1 license and that you
 * accept its terms.
 */
package com.xelians.testexec;

import com.google.common.collect.Lists;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class JavaExecuteScript used to execute the shell script in java
 */
public class JavaExecuteScript {
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaExecuteScript.class);

    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(2, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    /**
     * Return status when execute the shell script scan-clamav.sh for scanning the file
     *
     * @param cmd              the command line that will be executed
     * @param arg              the file to scan
     * @param timeoutScanDelay in ms
     * @return The return value of the cmd or 3 if the execution failed
     */
    public static ExecutionOutput executeCommand(String cmd, String arg, long timeoutScanDelay) {
        return exec(cmd, arg, timeoutScanDelay);
    }

    private static ExecutionOutput exec(String scriptPath, String arg, long timeoutScanDelay) {
        LOGGER.info(scriptPath + " " + arg + " " + timeoutScanDelay);

        ProcessBuilder processBuilder = new ProcessBuilder(Lists.newArrayList(scriptPath, arg));
        Future<String> outFuture = null;
        Future<String> errFuture = null;

        try {
            Process process = processBuilder.start();
            outFuture = EXECUTOR.submit(new StreamGobbler(process.getInputStream()));
            errFuture = EXECUTOR.submit(new StreamGobbler(process.getErrorStream()));

            boolean status = process.waitFor(timeoutScanDelay, TimeUnit.MILLISECONDS);
            if (!status) process.destroy();

            return new ExecutionOutput(process.exitValue(),
                    processBuilder.command(),
                    getFutureValue(outFuture, ""),
                    getFutureValue(errFuture, ""));

        } catch (Exception e) {
            return new ExecutionOutput(e,
                    processBuilder.command(),
                    getFutureValue(outFuture, ""),
                    getFutureValue(errFuture, ""));
        }
    }

    private static String getFutureValue(Future<String> future, String value) {
        try {
            return future.get(1000L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return value;
        }
    }
    
}

