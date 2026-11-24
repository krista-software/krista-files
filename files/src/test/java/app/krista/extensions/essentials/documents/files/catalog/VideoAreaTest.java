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

import app.krista.extensions.essentials.documents.files.impl.VideoConverterServiceImpl;
import app.krista.model.base.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VideoAreaTest {

    @Mock
    private VideoConverterServiceImpl converterService;

    private VideoArea videoArea;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        videoArea = new VideoArea();
        // Use reflection to inject the mock since VideoArea uses @Inject
        try {
            java.lang.reflect.Field field = VideoArea.class.getDeclaredField("converterService");
            field.setAccessible(true);
            field.set(videoArea, converterService);
        } catch (Exception e) {
            fail("Failed to inject mock converter service: " + e.getMessage());
        }
    }

    @Test
    public void getAudioEncoders_ShouldReturnEncodersList() {
        // Given
        String[] mockEncoders = {"mp3", "wav", "aac", "flac"};
        when(converterService.getAudioEncoder()).thenReturn(mockEncoders);
        
        // When
        List<String> result = videoArea.getAudioEncoders();
        
        // Then
        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.contains("mp3"));
        assertTrue(result.contains("wav"));
        assertTrue(result.contains("aac"));
        assertTrue(result.contains("flac"));
    }

    @Test
    public void getAudioEncoders_EmptyArray_ShouldReturnEmptyList() {
        // Given
        String[] mockEncoders = {};
        when(converterService.getAudioEncoder()).thenReturn(mockEncoders);
        
        // When
        List<String> result = videoArea.getAudioEncoders();
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void getVideoEncoders_ShouldReturnEncodersList() {
        // Given
        String[] mockEncoders = {"h264", "h265", "vp8", "vp9"};
        when(converterService.getVideoEncoders()).thenReturn(mockEncoders);
        
        // When
        List<String> result = videoArea.getVideoEncoders();
        
        // Then
        assertNotNull(result);
        assertEquals(4, result.size());
        assertTrue(result.contains("h264"));
        assertTrue(result.contains("h265"));
        assertTrue(result.contains("vp8"));
        assertTrue(result.contains("vp9"));
    }

    @Test
    public void getVideoEncoders_EmptyArray_ShouldReturnEmptyList() {
        // Given
        String[] mockEncoders = {};
        when(converterService.getVideoEncoders()).thenReturn(mockEncoders);
        
        // When
        List<String> result = videoArea.getVideoEncoders();
        
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void convertMp4ToMP3_ValidFile_ShouldReturnConvertedFile() {
        // Given
        File inputFile = new File(1, "video", "mp4", "123");
        File expectedOutputFile = new File(2, "audio", "mp3", "456");
        when(converterService.convertMp4ToMP3(any(File.class))).thenReturn(expectedOutputFile);
        
        // When
        File result = videoArea.mP4ToMP3(inputFile);
        
        // Then
        assertNotNull(result);
        assertEquals(expectedOutputFile, result);
        assertEquals("mp3", result.getExtension());
    }

    @Test
    public void convertMp4ToMP3_ServiceThrowsException_ShouldPropagateException() {
        // Given
        File inputFile = new File(1, "video", "mp4", "123");
        when(converterService.convertMp4ToMP3(any(File.class)))
                .thenThrow(new RuntimeException("Conversion failed"));
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            videoArea.mP4ToMP3(inputFile);
        });
        assertEquals("Conversion failed", exception.getMessage());
    }

    @Test
    public void convertMp4ToWav_ValidFile_ShouldReturnConvertedFile() {
        // Given
        File inputFile = new File(1, "video", "mp4", "123");
        File expectedOutputFile = new File(2, "audio", "wav", "456");
        when(converterService.convertMp4ToWav(any(File.class))).thenReturn(expectedOutputFile);
        
        // When
        File result = videoArea.mP4ToWAV(inputFile);
        
        // Then
        assertNotNull(result);
        assertEquals(expectedOutputFile, result);
        assertEquals("wav", result.getExtension());
    }

    @Test
    public void convertMp4ToWav_ServiceThrowsException_ShouldPropagateException() {
        // Given
        File inputFile = new File(1, "video", "mp4", "123");
        when(converterService.convertMp4ToWav(any(File.class)))
                .thenThrow(new RuntimeException("WAV conversion failed"));
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            videoArea.mP4ToWAV(inputFile);
        });
        assertEquals("WAV conversion failed", exception.getMessage());
    }

    @Test
    public void convertMp4ToWavMono_ValidFile_ShouldReturnConvertedFile() {
        // Given
        File inputFile = new File(1, "video", "mp4", "123");
        File expectedOutputFile = new File(2, "audio", "wav", "456");
        when(converterService.convertMp4ToWavMono(any(File.class))).thenReturn(expectedOutputFile);
        
        // When
        File result = videoArea.mP4ToWAVMONO(inputFile);
        
        // Then
        assertNotNull(result);
        assertEquals(expectedOutputFile, result);
        assertEquals("wav", result.getExtension());
    }

    @Test
    public void convertMp4ToWavMono_ServiceThrowsException_ShouldPropagateException() {
        // Given
        File inputFile = new File(1, "video", "mp4", "123");
        when(converterService.convertMp4ToWavMono(any(File.class)))
                .thenThrow(new RuntimeException("WAV Mono conversion failed"));
        
        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            videoArea.mP4ToWAVMONO(inputFile);
        });
        assertEquals("WAV Mono conversion failed", exception.getMessage());
    }

}
