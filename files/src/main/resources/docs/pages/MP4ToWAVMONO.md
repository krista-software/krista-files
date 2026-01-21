# MP4 To WAV MONO

## Overview

Converts MP4 video files to single-channel (mono) WAV audio format, extracting and mixing the audio track from video content into a single channel for specialized audio processing applications.

## Request Details

- **Area**: Video
- **Type**: CHANGE_SYSTEM
- **Retry Support**: ❌ No (Video conversion operations are not retryable due to potential duplicate file generation and resource-intensive processing)

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| MP4 | File | Yes | MP4 video file to convert to mono WAV audio | `speech.mp4`, `podcast_recording.mp4` |

### Parameter Details

#### MP4
- **Purpose**: Source MP4 video file for mono audio extraction
- **Format**: Standard MP4 video file with .mp4 extension
- **Requirements**: Must contain valid video content with audio track
- **Size Limit**: Recommended maximum 500MB to avoid timeout issues
- **Audio Track**: Must have an audio track for successful conversion
- **Channel Handling**: Stereo audio will be mixed down to single channel

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| WAV | File | Generated mono WAV audio file with single-channel audio content |

### Output Details

#### WAV File (Mono)
- **Format**: Standard WAV audio format (.wav) with single channel
- **Quality**: Uncompressed, high-quality mono audio (typically 16-bit, 44.1kHz)
- **Channels**: Single channel (mono) - stereo audio is mixed down
- **Naming Convention**:
  - With prefix: `[File Name Prefix] at DD-MM-YYYY HH.MM.SS AM.wav`
  - Without prefix: `File generated at DD-MM-YYYY HH.MM.SS AM.wav`
- **File Size**: Approximately half the size of stereo WAV files
- **Use Cases**: Speech processing, voice analysis, telephony applications

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
- Audio channel processing issues

**Resolution**:
1. Verify MP4 file contains audio track
2. Check video file plays correctly in media player
3. Try converting with different MP4 file
4. Ensure MP4 uses standard codecs

### System Errors (SYSTEM_ERROR)
**Cause**: System-level failures during conversion
**Common Scenarios**:
- Insufficient disk space for WAV files
- Memory limitations with large files
- Video processing timeout
- Audio channel mixing errors

**Resolution**:
1. Check available disk space
2. Try with smaller MP4 files
3. Verify system resources
4. Contact administrator for persistent issues

## Usage Examples

### Example 1: Speech Recording Conversion
**Input**:
```
MP4: speech_presentation.mp4 (80MB video with speech audio)
```

**Process**:
- Video file uploaded and validated
- Stereo audio track extracted and mixed to mono
- Single-channel WAV file generated

**Output**:
```
WAV: File generated at 15-03-2024 02.30.45 PM.wav (approximately 40MB mono)
```

### Example 2: Podcast Audio Extraction
**Input**:
```
MP4: podcast_episode.mp4 (200MB video recording)
```

**Configuration**: File Name Prefix = "PodcastAudio"

**Output**:
```
WAV: PodcastAudio at 15-03-2024 02.30.45 PM.wav (approximately 100MB mono)
```

### Example 3: Voice Analysis Preparation
**Input**:
```
MP4: interview_recording.mp4 (150MB interview video)
```

**Process**:
- Audio extracted and converted to mono for voice analysis
- Single channel optimized for speech processing
- Reduced file size for analysis software

**Output**:
```
WAV: File generated at 15-03-2024 02.32.18 PM.wav (approximately 75MB mono)
```

## Business Rules

1. **Audio Track Requirement**: MP4 files must contain an audio track for conversion
2. **Mono Channel Output**: All audio is mixed down to single channel regardless of source
3. **Automatic File Naming**: WAV files are automatically named with timestamp
4. **Quality Preservation**: Audio quality is maintained during mono conversion
5. **Channel Mixing**: Stereo audio is properly mixed to mono (not just left or right channel)
6. **File Size Reduction**: Mono files are approximately half the size of stereo equivalents

## Limitations

1. **Single Channel Output**: All spatial audio information is lost in mono conversion
2. **Processing Time**: Conversion time increases with file size and length
3. **Storage Requirements**: Still requires significant disk space (though less than stereo)
4. **Audio Track Dependency**: Cannot convert MP4 files without audio tracks
5. **Codec Support**: Limited to commonly supported MP4 codecs
6. **Channel Information Loss**: Stereo separation and spatial effects are lost

## Best Practices

### 1. File Preparation
- Verify MP4 file contains audio track before conversion
- Consider whether mono conversion is appropriate for your content
- Check source audio quality for best results
- Ensure MP4 file is not corrupted

### 2. Use Case Optimization
- Use mono conversion for speech and voice content
- Consider stereo conversion for music and complex audio
- Optimize for target application requirements (telephony, analysis, etc.)
- Test output with target systems before batch processing

### 3. Performance Optimization
- Use MP4 files under 500MB for best performance
- Avoid multiple simultaneous conversions
- Monitor system resources during conversion
- Process during off-peak hours for large files

### 4. Quality Considerations
- Understand that mono conversion loses stereo separation
- Use high-quality source MP4 files for best results
- Consider whether mono format meets your quality requirements

## Common Use Cases

### 1. Speech Processing
```
Scenario: Prepare audio for speech recognition or analysis
Action: Convert speech videos to mono WAV for processing software
Result: Single-channel audio optimized for speech processing
```

### 2. Telephony Applications
```
Scenario: Extract audio for telephony or communication systems
Action: Convert MP4 recordings to mono WAV for phone systems
Result: Compatible mono audio for telephony applications
```

### 3. Voice Analysis
```
Scenario: Prepare recordings for voice analysis and research
Action: Convert interview or speech videos to mono for analysis
Result: Standardized mono audio for voice analysis tools
```

### 4. Podcast Processing
```
Scenario: Extract mono audio from video podcasts
Action: Convert video podcast to mono WAV for audio-only distribution
Result: Smaller audio files suitable for podcast platforms
```

## Related Catalog Requests

- [MP4 To WAV](pages/MP4ToWAV.md) - Convert MP4 to stereo WAV audio
- [MP4 To MP3](pages/MP4ToMP3.md) - Convert MP4 to compressed MP3 audio
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
5. Use JAVE library to extract audio and convert to mono WAV
6. Mix stereo channels to single mono channel
7. Generate file name with timestamp
8. Return Krista File object with mono WAV content

### Audio Encoding Settings
```java
// Internal conversion settings
AudioAttributes audioAttributes = new AudioAttributes();
audioAttributes.setCodec("pcm_s16le"); // 16-bit PCM
audioAttributes.setChannels(1); // Mono (single channel)
audioAttributes.setSamplingRate(44100); // 44.1 kHz
// Channel mixing applied automatically
```

### Channel Mixing Algorithm
- **Stereo to Mono**: (Left Channel + Right Channel) / 2
- **Multi-channel**: All channels mixed and normalized to single channel
- **Quality Preservation**: No quality loss during mixing process

### Telemetry Metrics
- MP4_TO_WAV_MONO_SUCCESS: Successful conversion
- MP4_TO_WAV_MONO_ERROR: Failed conversion
- CONVERSION_TIME: Time taken for conversion
- FILE_SIZE_INPUT: Size of input MP4 file
- FILE_SIZE_OUTPUT: Size of output mono WAV file

## Troubleshooting

### Channel Mixing Issues
**Cause**: Problems with stereo to mono conversion
**Solution**:
1. Verify source MP4 has proper audio channels
2. Check that audio plays correctly in media player
3. Try with different source MP4 file
4. Ensure source audio is not corrupted

### No Audio Track Error
**Cause**: MP4 file does not contain audio track
**Solution**:
1. Verify MP4 file has audio when played in media player
2. Check if video was recorded with audio enabled
3. Use different MP4 file with confirmed audio track

### File Size Expectations
**Cause**: Output file size different than expected
**Solution**:
1. Understand mono files are approximately half the size of stereo
2. Factor in uncompressed WAV format for size calculations
3. Plan for approximately 5MB per minute of mono audio

### Quality Concerns
**Cause**: Mono audio quality seems reduced
**Solution**:
1. Understand that mono conversion loses stereo separation
2. Check source audio quality and bitrate
3. Consider whether mono format is appropriate for content
4. Test with high-quality source files

## See Also

- [MP4 To WAV](pages/MP4ToWAV.md) - Convert to stereo WAV format
- [MP4 To MP3](pages/MP4ToMP3.md) - Convert to compressed MP3 format
- [Get Audio Encoders](pages/GetAudioEncoders.md) - Available audio encoders
- [Extension Configuration](pages/ExtensionConfiguration.md) - Configure file naming
- [Authentication](pages/Authentication.md) - Authentication requirements
