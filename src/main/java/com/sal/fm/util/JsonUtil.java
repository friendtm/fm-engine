package com.sal.fm.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT); // Makes JSON pretty

    public static void saveToFile(String path, Object object) {
        try {
            mapper.writeValue(new File(path), object);
            System.out.println("Saved to: " + path);
        } catch (IOException e) {
            System.err.println("Error saving JSON: " + e.getMessage());
        }
    }

    public static <T> T loadFromFile(String path, Class<T> clazz) {
        try {
            return mapper.readValue(new File(path), clazz);
        } catch (IOException e) {
            System.err.println("Error loading JSON: " + e.getMessage());
            return null;
        }
    }
}
