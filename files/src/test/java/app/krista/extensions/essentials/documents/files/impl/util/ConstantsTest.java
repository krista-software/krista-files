/*
 * Files Extension for Krista
 * Copyright (C) 2025 Krista Software
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>. 
 */

package app.krista.extensions.essentials.documents.files.impl.util;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConstantsTest {

    @Test
    public void getDelimiter_ShouldReturnComma() {
        // When
        String delimiter = Constants.getDelimiter();
        
        // Then
        assertNotNull(delimiter);
        assertEquals(",", delimiter);
    }

    @Test
    public void gson_ShouldBeInitialized() {
        // When
        Gson gson = Constants.GSON;
        
        // Then
        assertNotNull(gson);
    }

    @Test
    public void gson_ShouldSerializeObject() {
        // Given
        TestObject testObject = new TestObject("test", 123);
        
        // When
        String json = Constants.GSON.toJson(testObject);
        
        // Then
        assertNotNull(json);
        assertTrue(json.contains("test"));
        assertTrue(json.contains("123"));
    }

    @Test
    public void gson_ShouldDeserializeObject() {
        // Given
        String json = "{\"name\":\"test\",\"value\":123}";
        
        // When
        TestObject result = Constants.GSON.fromJson(json, TestObject.class);
        
        // Then
        assertNotNull(result);
        assertEquals("test", result.name);
        assertEquals(123, result.value);
    }

    @Test
    public void gson_ShouldHandleNullValues() {
        // Given
        TestObject testObject = new TestObject(null, 0);
        
        // When
        String json = Constants.GSON.toJson(testObject);
        
        // Then
        assertNotNull(json);
        // Gson by default excludes null values unless configured otherwise
    }

    @Test
    public void gson_ShouldHandleEmptyStrings() {
        // Given
        TestObject testObject = new TestObject("", 0);
        
        // When
        String json = Constants.GSON.toJson(testObject);
        
        // Then
        assertNotNull(json);
        assertTrue(json.contains("\"\""));
    }

    @Test
    public void gson_ShouldHandleSpecialCharacters() {
        // Given
        TestObject testObject = new TestObject("test with \"quotes\" and \n newlines", 123);
        
        // When
        String json = Constants.GSON.toJson(testObject);
        
        // Then
        assertNotNull(json);
        assertTrue(json.contains("\\\""));
        assertTrue(json.contains("\\n"));
    }

    @Test
    public void gson_ShouldHandleUnicodeCharacters() {
        // Given
        TestObject testObject = new TestObject("测试 🎉 émojis", 123);
        
        // When
        String json = Constants.GSON.toJson(testObject);
        TestObject result = Constants.GSON.fromJson(json, TestObject.class);
        
        // Then
        assertNotNull(json);
        assertNotNull(result);
        assertEquals("测试 🎉 émojis", result.name);
    }

    @Test
    public void gson_ShouldHandleLargeNumbers() {
        // Given
        TestObject testObject = new TestObject("test", Integer.MAX_VALUE);
        
        // When
        String json = Constants.GSON.toJson(testObject);
        TestObject result = Constants.GSON.fromJson(json, TestObject.class);
        
        // Then
        assertNotNull(json);
        assertNotNull(result);
        assertEquals(Integer.MAX_VALUE, result.value);
    }

    @Test
    public void gson_ShouldHandleNegativeNumbers() {
        // Given
        TestObject testObject = new TestObject("test", -999);
        
        // When
        String json = Constants.GSON.toJson(testObject);
        TestObject result = Constants.GSON.fromJson(json, TestObject.class);
        
        // Then
        assertNotNull(json);
        assertNotNull(result);
        assertEquals(-999, result.value);
    }

    // Helper class for testing
    private static class TestObject {
        public String name;
        public int value;

        public TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        // Default constructor for Gson
        public TestObject() {}
    }
}
