# Get Audio Encoders

## Overview

Retrieves a list of available audio encoders supported by the system for video-to-audio conversion operations, providing information about codec capabilities and supported formats.

## Request Details

- **Area**: Video
- **Type**: QUERY_SYSTEM
- **Retry Support**: ✅ Yes (Query operations can be safely retried without side effects)

## Input Parameters

This catalog request requires no input parameters.

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Response | [ Text ] | Array of available audio encoder names and formats |

### Output Details

#### Response Array
- **Structure**: List of text strings representing available audio encoders
- **Content**: Encoder names, codec identifiers, and supported formats
- **Format**: Human-readable encoder descriptions
- **System Dependent**: Results may vary based on system configuration and installed codecs
- **Use Cases**: Determine available options for audio conversion operations

## Validation Rules

This request has no input validation requirements as it accepts no parameters.

## Error Handling

### System Errors (SYSTEM_ERROR)
**Cause**: System-level failures during encoder detection
**Common Scenarios**:
- Audio system initialization failure
- Codec library access issues
- System resource limitations

**Resolution**:
1. Retry the operation
2. Check system audio configuration
3. Verify JAVE library installation
4. Contact administrator for persistent issues

## Usage Examples

### Example 1: Basic Encoder Query
**Input**: (No parameters required)

**Output**:
```json
[
  "mp3 - MPEG Audio Layer 3",
  "aac - Advanced Audio Coding", 
  "wav - Waveform Audio File Format",
  "pcm_s16le - PCM signed 16-bit little-endian",
  "pcm_s24le - PCM signed 24-bit little-endian",
  "flac - Free Lossless Audio Codec",
  "ogg - Ogg Vorbis"
]
```

### Example 2: System with Limited Codecs
**Input**: (No parameters required)

**Output**:
```json
[
  "mp3 - MPEG Audio Layer 3",
  "wav - Waveform Audio File Format", 
  "pcm_s16le - PCM signed 16-bit little-endian"
]
```

### Example 3: Professional Audio System
**Input**: (No parameters required)

**Output**:
```json
[
  "mp3 - MPEG Audio Layer 3",
  "aac - Advanced Audio Coding",
  "wav - Waveform Audio File Format",
  "pcm_s16le - PCM signed 16-bit little-endian",
  "pcm_s24le - PCM signed 24-bit little-endian",
  "pcm_s32le - PCM signed 32-bit little-endian",
  "flac - Free Lossless Audio Codec",
  "alac - Apple Lossless Audio Codec",
  "ogg - Ogg Vorbis",
  "opus - Opus Interactive Audio Codec"
]
```

## Business Rules

1. **System Dependent**: Available encoders depend on system configuration and installed libraries
2. **Real-time Detection**: Encoder list is generated dynamically based on current system state
3. **Format Support**: Each encoder supports specific audio formats and quality settings
4. **Compatibility**: Not all encoders may be suitable for all conversion operations
5. **Performance Variation**: Different encoders have varying performance characteristics

## Limitations

1. **System Dependency**: Results vary based on system configuration
2. **Library Requirements**: Requires JAVE library and underlying codec support
3. **Dynamic Results**: Encoder availability may change with system updates
4. **Format Restrictions**: Each encoder has specific format and quality limitations
5. **Performance Impact**: Some encoders may be more resource-intensive than others

## Best Practices

### 1. Encoder Selection
- Use this query to determine available options before conversion
- Select appropriate encoder based on quality and compatibility requirements
- Consider target application requirements when choosing encoders
- Test encoder compatibility with your specific use cases

### 2. System Planning
- Query encoders during system setup and configuration
- Document available encoders for operational procedures
- Plan conversion workflows based on available encoder capabilities
- Monitor encoder availability after system updates

### 3. Quality Considerations
- Understand quality differences between encoders (lossy vs lossless)
- Consider file size implications of different encoder choices
- Match encoder selection to intended use case requirements
- Test output quality with different encoders for your content

### 4. Performance Optimization
- Consider encoder performance characteristics for batch operations
- Plan resource allocation based on encoder requirements
- Test conversion times with different encoders
- Balance quality and performance based on operational needs

## Common Use Cases

### 1. System Capability Assessment
```
Scenario: Determine audio conversion capabilities before implementation
Action: Query available audio encoders to plan conversion workflows
Result: List of supported audio formats for system planning
```

### 2. Dynamic Encoder Selection
```
Scenario: Select optimal encoder based on system capabilities
Action: Query encoders and choose best option for specific conversion
Result: Informed encoder selection for optimal results
```

### 3. Compatibility Verification
```
Scenario: Verify system supports required audio formats
Action: Check if specific encoders are available for target formats
Result: Confirmation of format support for integration planning
```

### 4. Troubleshooting Support
```
Scenario: Diagnose audio conversion issues
Action: Query available encoders to identify missing codec support
Result: Information for troubleshooting conversion problems
```

## Related Catalog Requests

- [Get Video Encoders](pages/GetVideoEncoders.md) - Retrieve available video encoders
- [MP4 To MP3](pages/MP4ToMP3.md) - Convert MP4 to MP3 using available encoders
- [MP4 To WAV](pages/MP4ToWAV.md) - Convert MP4 to WAV using available encoders
- [MP4 To WAV MONO](pages/MP4ToWAVMONO.md) - Convert MP4 to mono WAV

## Technical Implementation

### Helper Class
- **Class**: VideoConverterServiceImpl
- **Package**: app.krista.extensions.essentials.documents.files.impl
- **Library**: JAVE (Java Audio Video Encoder) 3.3.1
- **Detection**: Uses JAVE encoder detection capabilities

### Encoder Detection Process
1. Initialize JAVE encoder detection system
2. Query available audio encoders from system
3. Format encoder information for user-friendly display
4. Return list of available encoders with descriptions

### System Integration
```java
// Internal encoder detection
Encoder encoder = new Encoder();
EncoderInfo encoderInfo = encoder.getInfo();
List<String> audioEncoders = encoderInfo.getAudioEncoders();
```

### Encoder Information Format
- **Name**: Codec identifier (e.g., "mp3", "aac", "wav")
- **Description**: Human-readable format description
- **Capabilities**: Implicit quality and format support information

### Telemetry Metrics
- AUDIO_ENCODER_QUERY_SUCCESS: Successful encoder query
- AUDIO_ENCODER_QUERY_ERROR: Failed encoder query
- ENCODER_COUNT: Number of available audio encoders
- QUERY_RESPONSE_TIME: Time taken to detect encoders

## Troubleshooting

### No Encoders Returned
**Cause**: System audio configuration issues or missing codec libraries
**Solution**:
1. Verify JAVE library is properly installed
2. Check system audio configuration
3. Ensure codec libraries are available
4. Restart the Krista service
5. Contact administrator for codec installation

### Limited Encoder Options
**Cause**: Minimal codec installation or restricted system configuration
**Solution**:
1. Install additional codec libraries if needed
2. Check system permissions for codec access
3. Verify JAVE library version and capabilities
4. Consider system upgrade for additional codec support

### Encoder Query Timeout
**Cause**: System performance issues or codec detection problems
**Solution**:
1. Retry the operation after a brief delay
2. Check system resources and performance
3. Verify audio system is functioning properly
4. Contact support for persistent timeout issues

### Inconsistent Results
**Cause**: System configuration changes or codec updates
**Solution**:
1. Re-query encoders after system changes
2. Document encoder availability for reference
3. Test conversion operations with available encoders
4. Update operational procedures based on current capabilities

## Encoder Types and Characteristics

### Lossless Encoders
- **WAV/PCM**: Uncompressed, highest quality, largest file size
- **FLAC**: Compressed lossless, good quality, moderate file size
- **ALAC**: Apple's lossless format, good quality, moderate file size

### Lossy Encoders
- **MP3**: Widely compatible, good compression, moderate quality
- **AAC**: Modern compression, better quality than MP3 at same bitrate
- **OGG/Vorbis**: Open source, good compression and quality
- **Opus**: Modern codec, excellent quality and compression

### Specialized Encoders
- **PCM variants**: Different bit depths and sample rates
- **Format-specific**: Encoders optimized for specific applications

## See Also

- [Get Video Encoders](pages/GetVideoEncoders.md) - Query video encoder capabilities
- [MP4 To MP3](pages/MP4ToMP3.md) - Use MP3 encoder for conversion
- [MP4 To WAV](pages/MP4ToWAV.md) - Use WAV encoder for conversion
- [Extension Configuration](pages/ExtensionConfiguration.md) - Configure the extension
- [Authentication](pages/Authentication.md) - Authentication requirements
