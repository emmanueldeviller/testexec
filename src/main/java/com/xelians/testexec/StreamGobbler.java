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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.queue.CircularFifoQueue;

public class StreamGobbler implements Callable<String> {

    private final InputStream inputStream;

    public StreamGobbler(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public String call() {
        CircularFifoQueue<String> lastLines = new CircularFifoQueue<>(250);
        List<String> firstLines = new ArrayList<>();

        try {
            // We must never close the underlaying inputstream
            new BufferedReader(new InputStreamReader(inputStream, UTF_8)).lines().forEach(line -> {
                if (firstLines.size() < 250) {
                    firstLines.add(line);
                }
                else {
                    lastLines.add(line);
                }
            });
         } catch (Exception e) {
            return Stream.concat(firstLines.stream(), lastLines.stream())
                    .collect(Collectors.joining(" | ")) + "|" + e.getMessage();
        }
       
        return Stream.concat(firstLines.stream(), lastLines.stream()).collect(Collectors.joining(" | "));
    }

}
