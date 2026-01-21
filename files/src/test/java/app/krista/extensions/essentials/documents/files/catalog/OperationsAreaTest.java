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

package app.krista.extensions.essentials.documents.files.catalog;

import app.krista.extensions.essentials.documents.files.FileAttributes;
import app.krista.extensions.essentials.documents.files.constants.Errors;
import app.krista.extensions.essentials.documents.files.impl.SeparatedValueDaoImpl;
import app.krista.model.base.File;
import app.krista.model.base.FreeForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OperationsAreaTest {

    @Mock
    private SeparatedValueDaoImpl separatedValueDao;

    @Mock
    private FileAttributes fileAttributes;

    @Mock
    private CatalogTypes catalogTypes;

    private OperationsArea operationsArea;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        operationsArea = new OperationsArea(separatedValueDao, fileAttributes, catalogTypes);
    }



    @Test
    public void readCSVFile_ValidFile_ShouldReturnFreeFormList() throws Exception {
        // Given
        List<File> csvFiles = List.of(new File(1, "test", "csv", "123"));
        List<String> fields = null;
        List<FreeForm> expectedEntities = createTestFreeFormList();
        
        when(separatedValueDao.readCSV(any(java.io.File.class), isNull())).thenReturn(expectedEntities);
        when(catalogTypes.fromFile(any(File.class))).thenReturn(new java.io.File("test.csv"));
        
        // When
        List<FreeForm> result = operationsArea.readCSVFile(csvFiles, fields);
        
        // Then
        assertNotNull(result);
        assertEquals(expectedEntities.size(), result.size());
    }

    @Test
    public void readCSVFile_NullFile_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operationsArea.readCSVFile(null, null);
        });
        assertEquals(Errors.INVALID_CSV, exception.getMessage());
    }

    @Test
    public void readCSVFile_EmptyFileList_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operationsArea.readCSVFile(Collections.emptyList(), null);
        });
        assertEquals(Errors.INVALID_CSV, exception.getMessage());
    }

    @Test
    public void readCSVString_ValidString_ShouldReturnFreeFormList() {
        // Given
        String csvString = "name,age\nJohn,25\nJane,30";
        List<String> fields = null;
        List<FreeForm> expectedEntities = createTestFreeFormList();
        
        when(separatedValueDao.readCSVString(eq(csvString), isNull())).thenReturn(expectedEntities);
        
        // When
        List<FreeForm> result = operationsArea.readCSVString(csvString, fields);
        
        // Then
        assertNotNull(result);
        assertEquals(expectedEntities.size(), result.size());
    }

    @Test
    public void createSVString_ValidEntities_ShouldReturnString() {
        // Given
        List<Map<String, Object>> entities = createTestMapList();
        String fieldType = "CSV";
        String expectedResult = "name,age\nJohn,25\nJane,30";
        
        when(separatedValueDao.getSVString(eq(entities), eq(fieldType))).thenReturn(expectedResult);
        
        // When
        String result = operationsArea.createSVString(entities, fieldType);
        
        // Then
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void createSVString_TSVFieldType_ShouldReturnTSVString() {
        // Given
        List<Map<String, Object>> entities = createTestMapList();
        String fieldType = "TSV";
        String expectedResult = "name\tage\nJohn\t25\nJane\t30";
        
        when(separatedValueDao.getSVString(eq(entities), eq(fieldType))).thenReturn(expectedResult);
        
        // When
        String result = operationsArea.createSVString(entities, fieldType);
        
        // Then
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    @Test
    public void createSVString_PSVFieldType_ShouldReturnPSVString() {
        // Given
        List<Map<String, Object>> entities = createTestMapList();
        String fieldType = "PSV";
        String expectedResult = "name|age\nJohn|25\nJane|30";
        
        when(separatedValueDao.getSVString(eq(entities), eq(fieldType))).thenReturn(expectedResult);
        
        // When
        String result = operationsArea.createSVString(entities, fieldType);
        
        // Then
        assertNotNull(result);
        assertEquals(expectedResult, result);
    }

    // Helper methods
    private List<FreeForm> createTestFreeFormList() {
        List<FreeForm> entities = new ArrayList<>();
        
        FreeForm entity1 = new FreeForm();
        entity1.put("name", "Text", "John");
        entity1.put("age", "Number", 25);
        entities.add(entity1);
        
        FreeForm entity2 = new FreeForm();
        entity2.put("name", "Text", "Jane");
        entity2.put("age", "Number", 30);
        entities.add(entity2);
        
        return entities;
    }

    private List<Map<String, Object>> createTestMapList() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        
        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "John");
        map1.put("age", 25);
        mapList.add(map1);
        
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "Jane");
        map2.put("age", 30);
        mapList.add(map2);
        
        return mapList;
    }
}
