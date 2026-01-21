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

import app.krista.extensions.essentials.documents.files.util.KristaMediaClientUtil;
import app.krista.model.base.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VideoConverterServiceImplTest {

    private VideoConverterServiceImpl videoConverterServiceImpl;
    @Mock
    private KristaMediaClientUtil kristaMediaClientUtil;

    @BeforeEach
    public void setUp() {
        // Initialize mocks before each test
        MockitoAnnotations.openMocks(this);
        videoConverterServiceImpl = new VideoConverterServiceImpl(kristaMediaClientUtil);
    }


    @Test
    public void convertValidMp4ToWav_ShouldSucceed() throws IOException {
        // Given
        app.krista.model.base.File inputFile = new File(100, "test", "mp4","1234");
        app.krista.model.base.File outputFile = new File(100, "test", "wav","1234");
        java.io.File mockFile = new java.io.File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Arabic_Kuthu_Halamithi.mp4")).getFile());
        when(kristaMediaClientUtil.downloadFileHandler(inputFile)).thenReturn(mockFile);
        when(kristaMediaClientUtil.uploadFileHandler(any(java.io.File.class))).thenReturn(outputFile);

        // When
        File file = videoConverterServiceImpl.convertMp4ToWav(inputFile);

        // Then
        assertNotNull(file, "Output file should not be null");
        assertEquals("wav", file.getExtension(), "File extension should be 'wav'");
    }

    @Test
    public void convertValidMp4ToMp3_ShouldSucceed() throws IOException {
        // Given
        app.krista.model.base.File inputFile = new File(100, "test", "mp4","1234");
        app.krista.model.base.File outputFile = new File(100, "test", "mp3","1234");
        java.io.File mockFile = new java.io.File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Arabic_Kuthu_Halamithi.mp4")).getFile());
        when(kristaMediaClientUtil.downloadFileHandler(inputFile)).thenReturn(mockFile);
        when(kristaMediaClientUtil.uploadFileHandler(any(java.io.File.class))).thenReturn(outputFile);

        // When
        File file = videoConverterServiceImpl.convertMp4ToMP3(inputFile);

        // Then
        assertNotNull(file, "Output file should not be null");
        assertEquals("mp3", file.getExtension(), "File extension should be 'mp3'");
    }

    @Test
    public void convertValidMp4ToWavMono_ShouldSucceed() throws IOException {
        // Given
        app.krista.model.base.File inputFile = new File(100, "test", "mp4","1234");
        app.krista.model.base.File outputFile = new File(100, "test", "wav","1234");
        java.io.File mockFile = new java.io.File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Arabic_Kuthu_Halamithi.mp4")).getFile());
        when(kristaMediaClientUtil.downloadFileHandler(inputFile)).thenReturn(mockFile);
        when(kristaMediaClientUtil.uploadFileHandler(any(java.io.File.class))).thenReturn(outputFile);

        // When
        File file = videoConverterServiceImpl.convertMp4ToWavMono(inputFile);

        // Then
        assertNotNull(file, "Output file should not be null");
        assertEquals("wav", file.getExtension(), "File extension should be 'wav'");
    }

    @Test
    public void convertInvalidMp4ToWavMono_ShouldThrowException() throws IOException {
        // Given
        app.krista.model.base.File inputFile = new File(100, "test", "mp4","1234");
        // Use an MP3 file instead of MP4 to cause conversion failure
        java.io.File mockFile = new java.io.File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("Arabic_Kuthu_Halamithi_mp3.mp3")).getFile());
        when(kristaMediaClientUtil.downloadFileHandler(inputFile)).thenReturn(mockFile);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            videoConverterServiceImpl.convertMp4ToWavMono(inputFile);
        });
    }

    @Test
    public void getAudioEncodings_ShouldReturnNonEmptyArray() {
        // When
        final String[] audioEncoder = videoConverterServiceImpl.getAudioEncoder();

        // Then
        assertNotNull(audioEncoder, "Audio encoders array should not be null");
        assertTrue(audioEncoder.length > 0, "Audio encoders array should not be empty");
    }

    @Test
    public void getVideoEncodings_ShouldReturnNonEmptyArray() {
        // When
        final String[] videoEncoders = videoConverterServiceImpl.getVideoEncoders();

        // Then
        assertNotNull(videoEncoders, "Video encoders array should not be null");
        assertTrue(videoEncoders.length > 0, "Video encoders array should not be empty");
    }
}
