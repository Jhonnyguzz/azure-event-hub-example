package com.azure.eventhub.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class Utils {

    public static String getBase64StringFile(String stringPath) {
        try {
            Path p = Path.of(stringPath);
            byte[] fileInBytes = Files.readAllBytes(p);
            return Base64.getEncoder().encodeToString(fileInBytes);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static void writeFile(List<String> base64File, String filename) {
        try {
            String fullFile = String.join("", base64File);
            byte[] decodeFile = Base64.getDecoder().decode(fullFile);
            Files.write(Path.of(filename), decodeFile);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Split large String into 250kb chunks
     * 256_000 = 250kb
     * 262_144 = 256kb
     * 204_800 = 200kb
     * @param base64file file in Base64 Encode
     * @return List of string chunks in Base64
     */
    public static List<String> splitStringInto250kb(String base64file) {
        AtomicInteger splitCounter = new AtomicInteger(0);
        Collection<String> splittedStrings = base64file
                .chars()
                .mapToObj(character -> String.valueOf((char) character))
                .collect(Collectors.groupingBy(stringChar -> splitCounter.getAndIncrement() / 256_000, Collectors.joining()))
                .values();
        return new ArrayList<>(splittedStrings);
    }

}
