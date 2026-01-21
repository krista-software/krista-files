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

package app.krista.extensions.essentials.documents.files.util;

import app.krista.extensions.essentials.documents.files.client.KristaMediaClient;
import app.krista.model.base.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KristaMediaClientUtilTest {

    @Mock
    private KristaMediaClient kristaMediaClient;

    private KristaMediaClientUtil kristaMediaClientUtil;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        kristaMediaClientUtil = new KristaMediaClientUtil(kristaMediaClient);
    }

    @Test
    public void downloadFileHandler_ValidFile_ShouldReturnJavaFile() throws IOException {
        // Given
        File kristaFile = new File(1, "test", "txt", "123");
        java.io.File expectedFile = tempDir.resolve("test.txt").toFile();
        expectedFile.createNewFile();
        
        when(kristaMediaClient.toJavaFile(any(File.class))).thenReturn(expectedFile);
        
        // When
        java.io.File result = kristaMediaClientUtil.downloadFileHandler(kristaFile);
        
        // Then
        assertNotNull(result);
        assertEquals(expectedFile, result);
        assertTrue(result.exists());
    }

    @Test
    public void downloadFileHandler_NullFile_ShouldThrowException() throws IOException {
        // Given
        when(kristaMediaClient.toJavaFile(null))
                .thenThrow(new IOException("Null file"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            kristaMediaClientUtil.downloadFileHandler(null);
        });
        assertTrue(exception.getCause() instanceof IOException);
    }

    @Test
    public void downloadFileHandler_ClientThrowsIOException_ShouldPropagateException() throws IOException {
        // Given
        File kristaFile = new File(1, "test", "txt", "123");
        when(kristaMediaClient.toJavaFile(any(File.class)))
                .thenThrow(new IOException("Download failed"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            kristaMediaClientUtil.downloadFileHandler(kristaFile);
        });
        assertTrue(exception.getCause() instanceof IOException);
        assertEquals("Download failed", exception.getCause().getMessage());
    }

    @Test
    public void uploadFileHandler_ValidFile_ShouldReturnKristaFile() throws IOException {
        // Given
        java.io.File javaFile = tempDir.resolve("test.txt").toFile();
        javaFile.createNewFile();
        File expectedKristaFile = new File(1, "test", "txt", "123");
        
        when(kristaMediaClient.toKristaFile(any(java.io.File.class))).thenReturn(expectedKristaFile);
        
        // When
        File result = kristaMediaClientUtil.uploadFileHandler(javaFile);
        
        // Then
        assertNotNull(result);
        assertEquals(expectedKristaFile, result);
    }

    @Test
    public void uploadFileHandler_NullFile_ShouldThrowException() throws IOException {
        // Given
        when(kristaMediaClient.toKristaFile(null))
                .thenThrow(new IOException("Null file"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            kristaMediaClientUtil.uploadFileHandler(null);
        });
        assertTrue(exception.getCause() instanceof IOException);
    }

    @Test
    public void uploadFileHandler_ClientThrowsIOException_ShouldPropagateException() throws IOException {
        // Given
        java.io.File javaFile = tempDir.resolve("test.txt").toFile();
        javaFile.createNewFile();
        when(kristaMediaClient.toKristaFile(any(java.io.File.class)))
                .thenThrow(new IOException("Upload failed"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            kristaMediaClientUtil.uploadFileHandler(javaFile);
        });
        assertTrue(exception.getCause() instanceof IOException);
        assertEquals("Upload failed", exception.getCause().getMessage());
    }

    @Test
    public void uploadFileHandler_NonExistentFile_ShouldHandleGracefully() throws IOException {
        // Given
        java.io.File nonExistentFile = tempDir.resolve("nonexistent.txt").toFile();
        when(kristaMediaClient.toKristaFile(any(java.io.File.class)))
                .thenThrow(new IOException("File not found"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            kristaMediaClientUtil.uploadFileHandler(nonExistentFile);
        });
        assertTrue(exception.getCause() instanceof IOException);
        assertEquals("File not found", exception.getCause().getMessage());
    }

    @Test
    public void downloadFileHandler_LargeFile_ShouldHandleCorrectly() throws IOException {
        // Given
        File kristaFile = new File(1, "largefile", "bin", "123");
        java.io.File expectedFile = tempDir.resolve("largefile.bin").toFile();
        expectedFile.createNewFile();
        
        when(kristaMediaClient.toJavaFile(any(File.class))).thenReturn(expectedFile);
        
        // When
        java.io.File result = kristaMediaClientUtil.downloadFileHandler(kristaFile);
        
        // Then
        assertNotNull(result);
        assertEquals(expectedFile, result);
        assertTrue(result.exists());
    }

    @Test
    public void uploadFileHandler_DifferentFileTypes_ShouldHandleCorrectly() throws IOException {
        // Given
        java.io.File csvFile = tempDir.resolve("data.csv").toFile();
        csvFile.createNewFile();
        File expectedKristaFile = new File(1, "data", "csv", "123");
        
        when(kristaMediaClient.toKristaFile(any(java.io.File.class))).thenReturn(expectedKristaFile);
        
        // When
        File result = kristaMediaClientUtil.uploadFileHandler(csvFile);
        
        // Then
        assertNotNull(result);
        assertEquals(expectedKristaFile, result);
        assertEquals("csv", result.getExtension());
    }

    @Test
    public void downloadFileHandler_SpecialCharactersInFileName_ShouldHandleCorrectly() throws IOException {
        // Given
        File kristaFile = new File(1, "file with spaces & symbols", "txt", "123");
        java.io.File expectedFile = tempDir.resolve("file_with_spaces_symbols.txt").toFile();
        expectedFile.createNewFile();
        
        when(kristaMediaClient.toJavaFile(any(File.class))).thenReturn(expectedFile);
        
        // When
        java.io.File result = kristaMediaClientUtil.downloadFileHandler(kristaFile);
        
        // Then
        assertNotNull(result);
        assertEquals(expectedFile, result);
        assertTrue(result.exists());
    }
}
