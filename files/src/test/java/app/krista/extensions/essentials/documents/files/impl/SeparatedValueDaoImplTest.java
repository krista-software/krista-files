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

package app.krista.extensions.essentials.documents.files.impl;

import app.krista.extensions.essentials.documents.files.constants.Errors;
import app.krista.model.base.FreeForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SeparatedValueDaoImplTest {

    private SeparatedValueDaoImpl separatedValueDao;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        separatedValueDao = new SeparatedValueDaoImpl();
    }

    @Test
    public void readCSV_ValidFile_ShouldReturnFreeFormList() throws IOException {
        // Given
        File csvFile = createTestCSVFile("name,age,city\nJohn,25,NYC\nJane,30,LA");
        
        // When
        List<FreeForm> result = separatedValueDao.readCSV(csvFile, null);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        FreeForm firstRow = result.get(0);
        assertEquals("John", firstRow.get("name"));
        assertEquals("25", firstRow.get("age"));
        assertEquals("NYC", firstRow.get("city"));

        FreeForm secondRow = result.get(1);
        assertEquals("Jane", secondRow.get("name"));
        assertEquals("30", secondRow.get("age"));
        assertEquals("LA", secondRow.get("city"));
    }

    @Test
    public void readCSV_WithFieldFiltering_ShouldReturnFilteredData() throws IOException {
        // Given
        File csvFile = createTestCSVFile("name,age,city\nJohn,25,NYC\nJane,30,LA");
        List<String> fields = Arrays.asList("age");
        
        // When
        List<FreeForm> result = separatedValueDao.readCSV(csvFile, fields);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        FreeForm firstRow = result.get(0);
        assertEquals("John", firstRow.get("name"));
        assertNull(firstRow.get("age")); // Filtered out
        assertEquals("NYC", firstRow.get("city"));
    }

    @Test
    public void readCSV_EmptyFile_ShouldThrowException() throws IOException {
        // Given
        File emptyFile = createTestCSVFile("");

        // When & Then - Empty file will cause NullPointerException during processing
        assertThrows(Exception.class, () -> {
            separatedValueDao.readCSV(emptyFile, null);
        });
    }

    @Test
    public void readCSV_NullFile_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            separatedValueDao.readCSV(null, null);
        });
        assertEquals(Errors.FILE_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void readSVAsTable_ValidFile_ShouldReturnTableStructure() throws IOException {
        // Given
        File csvFile = createTestCSVFile("name,age\nJohn,25\nJane,30");

        // When
        FreeForm result = separatedValueDao.readSVAsTable(csvFile);

        // Then
        assertNotNull(result);
        assertTrue(result.containsKey("Data"));

        @SuppressWarnings("unchecked")
        List<List<String>> data = (List<List<String>>) result.get("Data");
        assertNotNull(data);
        assertEquals(3, data.size()); // header + 2 data rows
        assertEquals(Arrays.asList("name", "age"), data.get(0)); // header row
        assertEquals(Arrays.asList("John", "25"), data.get(1));
        assertEquals(Arrays.asList("Jane", "30"), data.get(2));
    }

    @Test
    public void getSVString_ValidData_ShouldReturnCSVString() {
        // Given
        List<Map<String, Object>> data = createTestMapData();
        
        // When
        String result = separatedValueDao.getSVString(data, "CSV");
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("name,age"));
        assertTrue(result.contains("John,25"));
        assertTrue(result.contains("Jane,30"));
    }

    @Test
    public void getSVString_TSVFormat_ShouldReturnTSVString() {
        // Given
        List<Map<String, Object>> data = createTestMapData();
        
        // When
        String result = separatedValueDao.getSVString(data, "TSV");
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("name\tage"));
        assertTrue(result.contains("John\t25"));
        assertTrue(result.contains("Jane\t30"));
    }

    @Test
    public void getSVString_PSVFormat_ShouldReturnPSVString() {
        // Given
        List<Map<String, Object>> data = createTestMapData();
        
        // When
        String result = separatedValueDao.getSVString(data, "PSV");
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("name|age"));
        assertTrue(result.contains("John|25"));
        assertTrue(result.contains("Jane|30"));
    }

    @Test
    public void getSVString_EmptyData_ShouldThrowException() {
        // Given
        List<Map<String, Object>> emptyData = new ArrayList<>();
        
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            separatedValueDao.getSVString(emptyData, "CSV");
        });
        assertEquals("Entities are null or empty", exception.getMessage());
    }

    @Test
    public void getSVString_NullData_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            separatedValueDao.getSVString(null, "CSV");
        });
        assertEquals("Entities are null or empty", exception.getMessage());
    }

    @Test
    public void readCSVString_ValidString_ShouldReturnFreeFormList() {
        // Given
        String csvString = "name,age,city\nJohn,25,NYC\nJane,30,LA";
        
        // When
        List<FreeForm> result = separatedValueDao.readCSVString(csvString, null);
        
        // Then
        assertNotNull(result);
        assertEquals(2, result.size());

        FreeForm firstRow = result.get(0);
        assertEquals("John", firstRow.get("name"));
        assertEquals("25", firstRow.get("age"));
        assertEquals("NYC", firstRow.get("city"));
    }

    @Test
    public void readCSVString_EmptyString_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            separatedValueDao.readCSVString("", null);
        });
        assertEquals(Errors.EMPTY_CSV_STRING, exception.getMessage());
    }

    @Test
    public void readCSVString_NullString_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            separatedValueDao.readCSVString(null, null);
        });
        assertEquals(Errors.EMPTY_CSV_STRING, exception.getMessage());
    }

    @Test
    public void getSV_ValidData_ShouldCreateFile() throws IOException {
        // Given
        List<Map<String, Object>> data = createTestMapData();
        String fileName = tempDir.resolve("test.csv").toString();
        
        // When
        File result = separatedValueDao.getSV(data, fileName);
        
        // Then
        assertNotNull(result);
        assertTrue(result.exists());
        assertTrue(result.length() > 0);
    }

    // Helper methods
    private File createTestCSVFile(String content) throws IOException {
        File csvFile = tempDir.resolve("test.csv").toFile();
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write(content);
        }
        return csvFile;
    }

    private List<Map<String, Object>> createTestMapData() {
        List<Map<String, Object>> data = new ArrayList<>();
        
        Map<String, Object> row1 = new HashMap<>();
        row1.put("name", "John");
        row1.put("age", 25);
        data.add(row1);
        
        Map<String, Object> row2 = new HashMap<>();
        row2.put("name", "Jane");
        row2.put("age", 30);
        data.add(row2);
        
        return data;
    }
}
