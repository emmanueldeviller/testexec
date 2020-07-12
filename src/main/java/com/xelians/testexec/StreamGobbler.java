/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
