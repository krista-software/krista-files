# MP4 To MP3

## Overview

Converts MP4 video files to MP3 audio format, extracting the audio track from video content and generating a downloadable MP3 file with automatic naming and timestamp generation.

## Request Details

- **Area**: Video
- **Type**: CHANGE_SYSTEM
- **Retry Support**: ❌ No (Video conversion operations are not retryable due to potential duplicate file generation and resource-intensive processing)

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| MP4 | File | Yes | MP4 video file to convert to MP3 audio | `presentation.mp4`, `meeting_recording.mp4` |

### Parameter Details

#### MP4
- **Purpose**: Source MP4 video file for audio extraction
- **Format**: Standard MP4 video file with .mp4 extension
- **Requirements**: Must contain valid video content with audio track
- **Size Limit**: Recommended maximum 500MB to avoid timeout issues
- **Audio Track**: Must have an audio track for successful conversion
- **Codecs**: Supports common MP4 video and audio codecs

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| MP3 | File | Generated MP3 audio file with extracted audio content |

### Output Details

#### MP3 File
- **Format**: Standard MP3 audio format (.mp3)
- **Quality**: Maintains original audio quality from source MP4
- **Encoding**: MP3 encoding with standard compression
- **Naming Convention**:
  - With prefix: `[File Name Prefix] at DD-MM-YYYY HH.MM.SS AM.mp3`
  - Without prefix: `File generated at DD-MM-YYYY HH.MM.SS AM.mp3`
- **Metadata**: Basic audio metadata preserved where possible

## Validation Rules

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| MP4 file not null | "File not found or inaccessible. Please verify the file exists, is not corrupted, and you have permission to access it." | Provide a valid MP4 file |
| Valid MP4 format | "Invalid MP4 file provided. Please ensure the file has a .mp4 extension and contains valid video content." | Upload a properly formatted MP4 file |
| Audio track present | "MP4 file does not contain an audio track. Please provide an MP4 file with audio content." | Use MP4 file with audio track |
| File accessibility | "Unable to process the MP4 file. This may be due to file corruption, unsupported codec, or encoding issues." | Check file integrity and format |

## Error Handling

### Input Errors (INPUT_ERROR)
**Cause**: Invalid or missing input parameters
**Common Scenarios**:
- Null or missing MP4 file
- Invalid file format
- Corrupted file upload
- File without .mp4 extension

**Resolution**:
1. Verify MP4 file is properly uploaded
2. Check file has .mp4 extension
3. Ensure file is not corrupted
4. Try uploading the file again

### Logic Errors (LOGIC_ERROR)
**Cause**: Video processing or conversion failures
**Common Scenarios**:
- MP4 file without audio track
- Unsupported video codec
- Corrupted video content
- Invalid file structure

**Resolution**:
1. Verify MP4 file contains audio track
2. Check video file plays correctly in media player
3. Try converting with different MP4 file
4. Ensure MP4 uses standard codecs

### System Errors (SYSTEM_ERROR)
**Cause**: System-level failures during conversion
**Common Scenarios**:
- Insufficient disk space for conversion
- Memory limitations with large files
- Video processing timeout
- Temporary directory access issues

**Resolution**:
1. Check available disk space
2. Try with smaller MP4 files
3. Verify system resources
4. Contact administrator for persistent issues

## Usage Examples

### Example 1: Basic MP4 to MP3 Conversion
**Input**:
```
MP4: presentation.mp4 (50MB video file with audio)
```

**Process**:
- Video file uploaded and validated
- Audio track extracted from MP4
- MP3 file generated with audio content

**Output**:
```
MP3: File generated at 15-03-2024 02.30.45 PM.mp3
```

### Example 2: Meeting Recording Conversion
**Input**:
```
MP4: team_meeting_2024.mp4 (200MB recording)
```

**Configuration**: File Name Prefix = "MeetingAudio"

**Output**:
```
MP3: MeetingAudio at 15-03-2024 02.30.45 PM.mp3
```

### Example 3: Large Video File
**Input**:
```
MP4: conference_session.mp4 (450MB video)
```

**Process**:
- Large file processing with extended timeout
- Audio extraction with quality preservation
- MP3 generation with compression

**Output**:
```
MP3: File generated at 15-03-2024 02.35.12 PM.mp3 (approximately 45MB)
```

## Business Rules

1. **Audio Track Requirement**: MP4 files must contain an audio track for conversion
2. **Automatic File Naming**: MP3 files are automatically named with timestamp
3. **Quality Preservation**: Original audio quality is maintained during conversion
4. **Format Standardization**: Output is always standard MP3 format
5. **File Size Optimization**: MP3 compression reduces file size compared to original
6. **Metadata Handling**: Basic audio metadata is preserved when possible

## Limitations

1. **File Size**: Large MP4 files (>500MB) may cause timeout issues
2. **Processing Time**: Conversion time increases with file size and length
3. **Audio Track Dependency**: Cannot convert MP4 files without audio tracks
4. **Codec Support**: Limited to commonly supported MP4 codecs
5. **Concurrent Processing**: Multiple simultaneous conversions may impact performance
6. **Memory Usage**: Large files require significant memory during processing

## Best Practices

### 1. File Preparation
- Verify MP4 file contains audio track before conversion
- Check file size and consider splitting very large files
- Ensure MP4 file is not corrupted
- Test file playback in media player first

### 2. Performance Optimization
- Use MP4 files under 500MB for best performance
- Avoid multiple simultaneous conversions
- Monitor system resources during conversion
- Consider batch processing for multiple files

### 3. Quality Considerations
- Use high-quality source MP4 files for better MP3 output
- Understand that MP3 compression may reduce audio quality
- Consider original audio bitrate when evaluating output quality

### 4. Error Prevention
- Validate MP4 file format and content before conversion
- Check available disk space before processing large files
- Test with small files first to verify system capability

## Common Use Cases

### 1. Meeting Audio Extraction
```
Scenario: Extract audio from recorded video meetings
Action: Convert MP4 meeting recordings to MP3 for audio-only distribution
Result: Smaller audio files for easy sharing and storage
```

### 2. Presentation Audio Creation
```
Scenario: Create audio version of video presentations
Action: Convert presentation MP4 files to MP3 for podcast distribution
Result: Audio-only content for podcast platforms
```

### 3. Training Material Processing
```
Scenario: Extract audio from training videos for mobile listening
Action: Convert training MP4 videos to MP3 for offline audio access
Result: Portable audio training materials
```

### 4. Content Repurposing
```
Scenario: Repurpose video content for audio-only platforms
Action: Convert video content to MP3 for radio or audio streaming
Result: Multi-format content distribution
```

## Related Catalog Requests

- [MP4 To WAV](pages/MP4ToWAV.md) - Convert MP4 to WAV audio format
- [MP4 To WAV MONO](pages/MP4ToWAVMONO.md) - Convert MP4 to single-channel WAV
- [Get Audio Encoders](pages/GetAudioEncoders.md) - Retrieve available audio encoders

## Technical Implementation

### Helper Class
- **Class**: VideoConverterServiceImpl
- **Package**: app.krista.extensions.essentials.documents.files.impl
- **Library**: JAVE (Java Audio Video Encoder) 3.3.1
- **Validation**: File format and audio track validation

### Conversion Process
1. Validate MP4 file is not null and accessible
2. Check file has .mp4 extension and valid content
3. Verify presence of audio track in video
4. Create temporary file for conversion process
5. Use JAVE library to extract audio and convert to MP3
6. Generate file name with timestamp
7. Return Krista File object with MP3 content

### Audio Encoding Settings
```java
// Internal conversion settings
AudioAttributes audioAttributes = new AudioAttributes();
audioAttributes.setCodec("mp3");
audioAttributes.setBitRate(128000); // 128 kbps
audioAttributes.setChannels(2); // Stereo
audioAttributes.setSamplingRate(44100); // 44.1 kHz
```

### Telemetry Metrics
- MP4_TO_MP3_SUCCESS: Successful conversion
- MP4_TO_MP3_ERROR: Failed conversion
- CONVERSION_TIME: Time taken for conversion
- FILE_SIZE_INPUT: Size of input MP4 file
- FILE_SIZE_OUTPUT: Size of output MP3 file

## Troubleshooting

### No Audio Track Error
**Cause**: MP4 file does not contain audio track
**Solution**:
1. Verify MP4 file has audio when played in media player
2. Check if video was recorded with audio enabled
3. Use different MP4 file with confirmed audio track

### Conversion Timeout
**Cause**: MP4 file too large or processing taking too long
**Solution**:
1. Use smaller MP4 files (under 500MB)
2. Check system resources and performance
3. Try conversion during off-peak hours
4. Contact administrator for timeout configuration

### Corrupted Output File
**Cause**: Issues during conversion process
**Solution**:
1. Verify source MP4 file is not corrupted
2. Check available disk space during conversion
3. Retry conversion with same file
4. Try with different source MP4 file

### Unsupported Codec Error
**Cause**: MP4 file uses unsupported video or audio codec
**Solution**:
1. Convert MP4 to standard format using video editing software
2. Check MP4 file codec information
3. Use MP4 files with common codecs (H.264 video, AAC audio)

## See Also

- [MP4 To WAV](pages/MP4ToWAV.md) - Convert to WAV format
- [Get Audio Encoders](pages/GetAudioEncoders.md) - Available audio encoders
- [Extension Configuration](pages/ExtensionConfiguration.md) - Configure file naming
- [Authentication](pages/Authentication.md) - Authentication requirements
