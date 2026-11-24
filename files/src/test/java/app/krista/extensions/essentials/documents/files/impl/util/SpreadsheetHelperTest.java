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

import app.krista.model.base.FreeForm;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SpreadsheetHelperTest {

    @Test
    public void parseHeaders_ValidHeaders_ShouldReturnHeaderMap() {
        // Given
        String[] headers = {"name", "age", "city"};
        
        // When
        Map<String, Integer> result = SpreadsheetHelper.parseHeaders(headers);
        
        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(0, result.get("name"));
        assertEquals(1, result.get("age"));
        assertEquals(2, result.get("city"));
    }

    @Test
    public void parseHeaders_HeadersWithEmptyValues_ShouldCreateUntitledHeaders() {
        // Given
        String[] headers = {"name", "", "city"};
        
        // When
        Map<String, Integer> result = SpreadsheetHelper.parseHeaders(headers);
        
        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(0, result.get("name"));
        assertEquals(1, result.get("Untitled Header 2"));
        assertEquals(2, result.get("city"));
    }

    @Test
    public void getColumns_ValidBufferedReader_ShouldReturnColumns() throws IOException {
        // Given
        String csvData = "name,age,city\nJohn,25,NYC\nJane,30,LA";
        BufferedReader reader = new BufferedReader(new StringReader(csvData));
        
        // When
        List<List<String>> result = SpreadsheetHelper.getColumns(reader);
        
        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(Arrays.asList("name", "age", "city"), result.get(0));
        assertEquals(Arrays.asList("John", "25", "NYC"), result.get(1));
        assertEquals(Arrays.asList("Jane", "30", "LA"), result.get(2));
    }

    @Test
    public void getColumns_EmptyBufferedReader_ShouldReturnEmptyList() throws IOException {
        // Given
        BufferedReader reader = new BufferedReader(new StringReader(""));
        
        // When
        List<List<String>> result = SpreadsheetHelper.getColumns(reader);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getColumns_NullBufferedReader_ShouldThrowException() {
        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            SpreadsheetHelper.getColumns((BufferedReader) null);
        });
        assertEquals("Failed to get columns. Input stream is empty.", exception.getMessage());
    }

    @Test
    public void getColumns_ValidJsonArray_ShouldReturnColumnSet() {
        // Given
        JsonArray jsonArray = new JsonArray();
        JsonObject obj1 = new JsonObject();
        obj1.addProperty("name", "John");
        obj1.addProperty("age", 25);
        jsonArray.add(obj1);
        
        JsonObject obj2 = new JsonObject();
        obj2.addProperty("name", "Jane");
        obj2.addProperty("city", "LA");
        jsonArray.add(obj2);
        
        // When
        Set<String> result = SpreadsheetHelper.getColumns(jsonArray);
        
        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains("name"));
        assertTrue(result.contains("age"));
        assertTrue(result.contains("city"));
    }

    @Test
    public void getColumns_EmptyJsonArray_ShouldReturnEmptySet() {
        // Given
        JsonArray jsonArray = new JsonArray();
        
        // When
        Set<String> result = SpreadsheetHelper.getColumns(jsonArray);
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getColumns_NullJsonArray_ShouldThrowException() {
        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            SpreadsheetHelper.getColumns((JsonArray) null);
        });
        assertEquals("Failed to get columns. Json array not found.", exception.getMessage());
    }

    @Test
    public void getRow_ValidParameters_ShouldReturnFreeForm() {
        // Given
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("John");
        row.createCell(1).setCellValue(25);
        row.createCell(2).setCellValue("NYC");
        
        List<String> headers = Arrays.asList("name", "age", "city");
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        
        // When
        FreeForm result = SpreadsheetHelper.getRow(3, headers, row, evaluator, null);
        
        // Then
        assertNotNull(result);
        assertEquals("John", result.get("name"));
        assertEquals("25.0", result.get("age"));
        assertEquals("NYC", result.get("city"));
    }

    @Test
    public void getRow_WithFieldFiltering_ShouldFilterFields() {
        // Given
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("John");
        row.createCell(1).setCellValue(25);
        row.createCell(2).setCellValue("NYC");
        
        List<String> headers = Arrays.asList("name", "age", "city");
        List<String> fields = Arrays.asList("age");
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        
        // When
        FreeForm result = SpreadsheetHelper.getRow(3, headers, row, evaluator, fields);
        
        // Then
        assertNotNull(result);
        assertEquals("John", result.get("name"));
        assertNull(result.get("age")); // Filtered out
        assertEquals("NYC", result.get("city"));
    }

    @Test
    public void getRow_InvalidMaxColumnIndex_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            SpreadsheetHelper.getRow(0, Arrays.asList("name"), null, null, null);
        });
        assertEquals("Failed to get row. Invalid max column index.", exception.getMessage());
    }

    @Test
    public void getRow_NullHeaders_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            SpreadsheetHelper.getRow(1, null, null, null, null);
        });
        assertEquals("Failed to get row. Headers not found.", exception.getMessage());
    }

    @Test
    public void getRow_EmptyHeaders_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            SpreadsheetHelper.getRow(1, new ArrayList<>(), null, null, null);
        });
        assertEquals("Failed to get row. Headers not found.", exception.getMessage());
    }

    @Test
    public void getRow_NullRow_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            SpreadsheetHelper.getRow(1, Arrays.asList("name"), null, null, null);
        });
        assertEquals("Failed to get row. No row to parse.", exception.getMessage());
    }

    @Test
    public void addColumns_ValidParameters_ShouldWriteCSVData() {
        // Given
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        
        JsonArray jsonArray = new JsonArray();
        JsonObject obj1 = new JsonObject();
        obj1.addProperty("name", "John");
        obj1.addProperty("age", 25);
        jsonArray.add(obj1);
        
        Set<String> columns = new LinkedHashSet<>(Arrays.asList("name", "age"));
        
        // When
        SpreadsheetHelper.addColumns(printWriter, jsonArray, columns);
        printWriter.flush();
        
        // Then
        String result = stringWriter.toString();
        assertNotNull(result);
        assertTrue(result.contains("John"));
        assertTrue(result.contains("25"));
    }

    @Test
    public void addColumns_NullPrintWriter_ShouldThrowException() {
        // Given
        JsonArray jsonArray = new JsonArray();
        Set<String> columns = new HashSet<>();
        
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            SpreadsheetHelper.addColumns(null, jsonArray, columns);
        });
        assertEquals("Failed to add columns. No print writer found.", exception.getMessage());
    }

    @Test
    public void addColumns_NullJsonArray_ShouldThrowException() {
        // Given
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        Set<String> columns = new HashSet<>();
        
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            SpreadsheetHelper.addColumns(printWriter, null, columns);
        });
        assertEquals("Failed to add columns. No data found.", exception.getMessage());
    }

    @Test
    public void addColumns_NullColumns_ShouldThrowException() {
        // Given
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        JsonArray jsonArray = new JsonArray();
        
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            SpreadsheetHelper.addColumns(printWriter, jsonArray, null);
        });
        assertEquals("Failed to add columns. Columns not found.", exception.getMessage());
    }

    @Test
    public void addColumns_EmptyColumns_ShouldThrowException() {
        // Given
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        JsonArray jsonArray = new JsonArray();
        Set<String> columns = new HashSet<>();
        
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            SpreadsheetHelper.addColumns(printWriter, jsonArray, columns);
        });
        assertEquals("Failed to add columns. Columns not found.", exception.getMessage());
    }

    @Test
    public void isRowContainsData_RowWithData_ShouldReturnTrue() {
        // Given
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("John");
        row.createCell(1).setCellValue("");
        row.createCell(2).setCellValue("NYC");
        
        // When
        boolean result = SpreadsheetHelper.isRowContainsData(row);
        
        // Then
        assertTrue(result);
    }

    @Test
    public void isRowContainsData_EmptyRow_ShouldReturnFalse() {
        // Given
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("");
        row.createCell(1).setCellValue("");
        
        // When
        boolean result = SpreadsheetHelper.isRowContainsData(row);
        
        // Then
        assertFalse(result);
    }

    @Test
    public void isRowContainsData_NullRow_ShouldReturnFalse() {
        // When
        boolean result = SpreadsheetHelper.isRowContainsData(null);
        
        // Then
        assertFalse(result);
    }
}
