# MP4 To WAV

## Overview

Converts MP4 video files to WAV audio format, extracting the audio track from video content and generating a high-quality, uncompressed WAV file with automatic naming and timestamp generation.

## Request Details

- **Area**: Video
- **Type**: CHANGE_SYSTEM
- **Retry Support**: ❌ No (Video conversion operations are not retryable due to potential duplicate file generation and resource-intensive processing)

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| MP4 | File | Yes | MP4 video file to convert to WAV audio | `interview.mp4`, `music_video.mp4` |

### Parameter Details

#### MP4
- **Purpose**: Source MP4 video file for high-quality audio extraction
- **Format**: Standard MP4 video file with .mp4 extension
- **Requirements**: Must contain valid video content with audio track
- **Size Limit**: Recommended maximum 500MB to avoid timeout issues
- **Audio Track**: Must have an audio track for successful conversion
- **Quality Consideration**: Higher quality source produces better WAV output

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| WAV | File | Generated WAV audio file with uncompressed audio content |

### Output Details

#### WAV File
- **Format**: Standard WAV audio format (.wav)
- **Quality**: Uncompressed, high-quality audio (typically 16-bit, 44.1kHz)
- **Channels**: Stereo (2 channels) preserved from source
- **Naming Convention**:
  - With prefix: `[File Name Prefix] at DD-MM-YYYY HH.MM.SS AM.wav`
  - Without prefix: `File generated at DD-MM-YYYY HH.MM.SS AM.wav`
- **File Size**: Larger than MP3 due to uncompressed format
- **Compatibility**: Compatible with all audio editing and playback software

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
- Invalid audio stream

**Resolution**:
1. Verify MP4 file contains audio track
2. Check video file plays correctly in media player
3. Try converting with different MP4 file
4. Ensure MP4 uses standard codecs

### System Errors (SYSTEM_ERROR)
**Cause**: System-level failures during conversion
**Common Scenarios**:
- Insufficient disk space for large WAV files
- Memory limitations with large files
- Video processing timeout
- Temporary directory access issues

**Resolution**:
1. Check available disk space (WAV files are large)
2. Try with smaller MP4 files
3. Verify system resources
4. Contact administrator for persistent issues

## Usage Examples

### Example 1: Basic MP4 to WAV Conversion
**Input**:
```
MP4: interview.mp4 (100MB video file with audio)
```

**Process**:
- Video file uploaded and validated
- Audio track extracted from MP4
- High-quality WAV file generated

**Output**:
```
WAV: File generated at 15-03-2024 02.30.45 PM.wav (approximately 120MB)
```

### Example 2: Music Video Conversion
**Input**:
```
MP4: music_performance.mp4 (300MB high-quality video)
```

**Configuration**: File Name Prefix = "AudioTrack"

**Output**:
```
WAV: AudioTrack at 15-03-2024 02.30.45 PM.wav (approximately 350MB)
```

### Example 3: Professional Audio Extraction
**Input**:
```
MP4: studio_recording.mp4 (450MB professional recording)
```

**Process**:
- High-quality source processed
- Uncompressed audio extraction
- Professional-grade WAV output

**Output**:
```
WAV: File generated at 15-03-2024 02.35.12 PM.wav (approximately 500MB)
```

## Business Rules

1. **Audio Track Requirement**: MP4 files must contain an audio track for conversion
2. **Uncompressed Output**: WAV format provides uncompressed, high-quality audio
3. **Automatic File Naming**: WAV files are automatically named with timestamp
4. **Quality Preservation**: Maximum audio quality is maintained during conversion
5. **Stereo Preservation**: Stereo audio channels are preserved in output
6. **Large File Generation**: WAV files are significantly larger than compressed formats

## Limitations

1. **File Size**: WAV files are much larger than compressed formats (MP3, AAC)
2. **Processing Time**: Conversion time increases with file size and length
3. **Storage Requirements**: Requires significant disk space for output files
4. **Audio Track Dependency**: Cannot convert MP4 files without audio tracks
5. **Codec Support**: Limited to commonly supported MP4 codecs
6. **Memory Usage**: Large files require significant memory during processing

## Best Practices

### 1. File Preparation
- Verify MP4 file contains high-quality audio track
- Check available disk space before conversion (WAV files are large)
- Ensure MP4 file is not corrupted
- Consider source audio quality for best results

### 2. Storage Management
- Plan for large WAV file sizes (typically 10MB per minute of audio)
- Ensure adequate disk space for conversion process
- Consider file cleanup procedures for temporary files
- Monitor storage usage during batch conversions

### 3. Performance Optimization
- Use MP4 files under 500MB for best performance
- Avoid multiple simultaneous conversions
- Monitor system resources during conversion
- Process during off-peak hours for large files

### 4. Quality Considerations
- Use high-quality source MP4 files for best WAV output
- Understand that WAV preserves original audio quality
- Consider whether uncompressed format is necessary for your use case

## Common Use Cases

### 1. Professional Audio Editing
```
Scenario: Extract high-quality audio for professional editing
Action: Convert MP4 recordings to WAV for audio editing software
Result: Uncompressed audio ready for professional editing
```

### 2. Music Production
```
Scenario: Extract audio tracks from music videos for remixing
Action: Convert music video MP4 to WAV for music production
Result: High-quality audio tracks for music production software
```

### 3. Archival Audio Storage
```
Scenario: Create archival-quality audio from video recordings
Action: Convert important video recordings to WAV for long-term storage
Result: High-quality audio archives for preservation
```

### 4. Audio Analysis
```
Scenario: Extract audio for detailed analysis and processing
Action: Convert MP4 to WAV for audio analysis software
Result: Uncompressed audio suitable for detailed analysis
```

## Related Catalog Requests

- [MP4 To MP3](pages/MP4ToMP3.md) - Convert MP4 to compressed MP3 audio
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
5. Use JAVE library to extract audio and convert to WAV
6. Generate file name with timestamp
7. Return Krista File object with WAV content

### Audio Encoding Settings
```java
// Internal conversion settings
AudioAttributes audioAttributes = new AudioAttributes();
audioAttributes.setCodec("pcm_s16le"); // 16-bit PCM
audioAttributes.setChannels(2); // Stereo
audioAttributes.setSamplingRate(44100); // 44.1 kHz
// No compression for WAV format
```

### Telemetry Metrics
- MP4_TO_WAV_SUCCESS: Successful conversion
- MP4_TO_WAV_ERROR: Failed conversion
- CONVERSION_TIME: Time taken for conversion
- FILE_SIZE_INPUT: Size of input MP4 file
- FILE_SIZE_OUTPUT: Size of output WAV file

## Troubleshooting

### Large File Size Issues
**Cause**: WAV files are much larger than compressed formats
**Solution**:
1. Ensure adequate disk space before conversion
2. Consider using MP3 format if file size is a concern
3. Monitor available storage during conversion
4. Plan for approximately 10MB per minute of audio

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

### Disk Space Errors
**Cause**: Insufficient disk space for large WAV output
**Solution**:
1. Check available disk space before conversion
2. Clean up temporary files and old conversions
3. Use smaller source files or consider MP3 format
4. Increase available storage capacity

## See Also

- [MP4 To MP3](pages/MP4ToMP3.md) - Convert to compressed MP3 format
- [MP4 To WAV MONO](pages/MP4ToWAVMONO.md) - Convert to single-channel WAV
- [Get Audio Encoders](pages/GetAudioEncoders.md) - Available audio encoders
- [Extension Configuration](pages/ExtensionConfiguration.md) - Configure file naming
- [Authentication](pages/Authentication.md) - Authentication requirements
