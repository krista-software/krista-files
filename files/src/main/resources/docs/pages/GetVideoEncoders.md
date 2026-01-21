# Get Video Encoders

## Overview

Retrieves a list of available video encoders supported by the system for video processing operations, providing information about codec capabilities and supported video formats.

## Request Details

- **Area**: Video
- **Type**: QUERY_SYSTEM
- **Retry Support**: ✅ Yes (Query operations can be safely retried without side effects)

## Input Parameters

This catalog request requires no input parameters.

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Response | [ Text ] | Array of available video encoder names and formats |

### Output Details

#### Response Array
- **Structure**: List of text strings representing available video encoders
- **Content**: Encoder names, codec identifiers, and supported formats
- **Format**: Human-readable encoder descriptions
- **System Dependent**: Results may vary based on system configuration and installed codecs
- **Use Cases**: Determine available options for video processing operations

## Validation Rules

This request has no input validation requirements as it accepts no parameters.

## Error Handling

### System Errors (SYSTEM_ERROR)
**Cause**: System-level failures during encoder detection
**Common Scenarios**:
- Video system initialization failure
- Codec library access issues
- System resource limitations
- Graphics driver issues

**Resolution**:
1. Retry the operation
2. Check system video configuration
3. Verify JAVE library installation
4. Check graphics drivers and video system
5. Contact administrator for persistent issues

## Usage Examples

### Example 1: Basic Video Encoder Query
**Input**: (No parameters required)

**Output**:
```json
[
  "h264 - H.264 / AVC / MPEG-4 AVC / MPEG-4 part 10",
  "h265 - H.265 / HEVC (High Efficiency Video Coding)",
  "mpeg4 - MPEG-4 part 2",
  "mpeg2video - MPEG-2 video",
  "libx264 - libx264 H.264 / AVC / MPEG-4 AVC / MPEG-4 part 10",
  "libx265 - libx265 H.265 / HEVC",
  "vp8 - On2 VP8",
  "vp9 - Google VP9"
]
```

### Example 2: System with Limited Video Support
**Input**: (No parameters required)

**Output**:
```json
[
  "h264 - H.264 / AVC / MPEG-4 AVC / MPEG-4 part 10",
  "mpeg4 - MPEG-4 part 2",
  "mpeg2video - MPEG-2 video"
]
```

### Example 3: Professional Video System
**Input**: (No parameters required)

**Output**:
```json
[
  "h264 - H.264 / AVC / MPEG-4 AVC / MPEG-4 part 10",
  "h265 - H.265 / HEVC (High Efficiency Video Coding)",
  "mpeg4 - MPEG-4 part 2",
  "mpeg2video - MPEG-2 video",
  "libx264 - libx264 H.264 / AVC / MPEG-4 AVC / MPEG-4 part 10",
  "libx265 - libx265 H.265 / HEVC",
  "vp8 - On2 VP8",
  "vp9 - Google VP9",
  "av1 - AOMedia Video 1",
  "prores - Apple ProRes",
  "dnxhd - Avid DNxHD",
  "mjpeg - Motion JPEG"
]
```

## Business Rules

1. **System Dependent**: Available encoders depend on system configuration and installed libraries
2. **Real-time Detection**: Encoder list is generated dynamically based on current system state
3. **Format Support**: Each encoder supports specific video formats, resolutions, and quality settings
4. **Compatibility**: Not all encoders may be suitable for all video processing operations
5. **Performance Variation**: Different encoders have varying performance and quality characteristics

## Limitations

1. **System Dependency**: Results vary based on system configuration and hardware
2. **Library Requirements**: Requires JAVE library and underlying codec support
3. **Dynamic Results**: Encoder availability may change with system updates
4. **Hardware Acceleration**: Some encoders may require specific hardware support
5. **License Restrictions**: Some encoders may have licensing limitations
6. **Performance Impact**: Video encoders are typically resource-intensive

## Best Practices

### 1. Encoder Assessment
- Use this query to understand video processing capabilities
- Document available encoders for operational planning
- Test encoder compatibility with your video content
- Consider hardware requirements for different encoders

### 2. System Planning
- Query encoders during system setup and configuration
- Plan video workflows based on available encoder capabilities
- Consider performance implications of different encoder choices
- Monitor encoder availability after system updates

### 3. Quality and Performance
- Understand quality differences between encoders
- Consider encoding speed vs. quality trade-offs
- Test encoder performance with representative video content
- Plan resource allocation for video processing operations

### 4. Compatibility Considerations
- Verify encoder support for target video formats
- Consider playback compatibility requirements
- Test output compatibility with target systems
- Plan fallback options for unsupported encoders

## Common Use Cases

### 1. System Capability Assessment
```
Scenario: Determine video processing capabilities before implementation
Action: Query available video encoders to plan video workflows
Result: List of supported video formats for system planning
```

### 2. Video Processing Planning
```
Scenario: Plan video conversion and processing operations
Action: Check available encoders to design video processing workflows
Result: Informed planning for video processing capabilities
```

### 3. Compatibility Verification
```
Scenario: Verify system supports required video formats
Action: Check if specific video encoders are available
Result: Confirmation of video format support for integration
```

### 4. Troubleshooting Support
```
Scenario: Diagnose video processing issues
Action: Query available encoders to identify missing codec support
Result: Information for troubleshooting video processing problems
```

## Related Catalog Requests

- [Get Audio Encoders](pages/GetAudioEncoders.md) - Retrieve available audio encoders
- [MP4 To MP3](pages/MP4ToMP3.md) - Video-to-audio conversion using video decoders
- [MP4 To WAV](pages/MP4ToWAV.md) - Video processing for audio extraction
- [MP4 To WAV MONO](pages/MP4ToWAVMONO.md) - Video processing for mono audio

## Technical Implementation

### Helper Class
- **Class**: VideoConverterServiceImpl
- **Package**: app.krista.extensions.essentials.documents.files.impl
- **Library**: JAVE (Java Audio Video Encoder) 3.3.1
- **Detection**: Uses JAVE video encoder detection capabilities

### Encoder Detection Process
1. Initialize JAVE video encoder detection system
2. Query available video encoders from system
3. Format encoder information for user-friendly display
4. Return list of available encoders with descriptions

### System Integration
```java
// Internal encoder detection
Encoder encoder = new Encoder();
EncoderInfo encoderInfo = encoder.getInfo();
List<String> videoEncoders = encoderInfo.getVideoEncoders();
```

### Encoder Information Format
- **Name**: Codec identifier (e.g., "h264", "h265", "vp9")
- **Description**: Human-readable format description
- **Capabilities**: Implicit quality, resolution, and format support

### Telemetry Metrics
- VIDEO_ENCODER_QUERY_SUCCESS: Successful encoder query
- VIDEO_ENCODER_QUERY_ERROR: Failed encoder query
- ENCODER_COUNT: Number of available video encoders
- QUERY_RESPONSE_TIME: Time taken to detect encoders

## Troubleshooting

### No Video Encoders Returned
**Cause**: System video configuration issues or missing codec libraries
**Solution**:
1. Verify JAVE library is properly installed
2. Check system video configuration and drivers
3. Ensure video codec libraries are available
4. Verify graphics system is functioning
5. Restart the Krista service
6. Contact administrator for codec installation

### Limited Encoder Options
**Cause**: Minimal codec installation or restricted system configuration
**Solution**:
1. Install additional video codec libraries if needed
2. Check system permissions for codec access
3. Verify JAVE library version and video capabilities
4. Consider system upgrade for additional codec support
5. Check hardware acceleration support

### Encoder Query Timeout
**Cause**: System performance issues or video system problems
**Solution**:
1. Retry the operation after a brief delay
2. Check system resources and performance
3. Verify video system and graphics drivers
4. Check for video system conflicts
5. Contact support for persistent timeout issues

### Hardware-Specific Issues
**Cause**: Missing hardware acceleration or driver problems
**Solution**:
1. Update graphics drivers to latest version
2. Check hardware acceleration settings
3. Verify system meets encoder hardware requirements
4. Test with software-only encoders
5. Contact hardware vendor for driver support

## Encoder Types and Characteristics

### Modern High-Efficiency Encoders
- **H.265/HEVC**: Next-generation compression, excellent quality, high CPU usage
- **AV1**: Open source, excellent compression, very high CPU usage
- **VP9**: Google's codec, good compression, moderate CPU usage

### Standard Encoders
- **H.264/AVC**: Widely compatible, good quality, moderate CPU usage
- **MPEG-4**: Older standard, good compatibility, moderate quality

### Specialized Encoders
- **ProRes**: Professional video, excellent quality, large file sizes
- **DNxHD**: Professional video, broadcast quality, large file sizes
- **MJPEG**: Motion JPEG, fast encoding, large file sizes

### Legacy Encoders
- **MPEG-2**: Older standard, good compatibility, larger file sizes
- **VP8**: Predecessor to VP9, moderate compression

## Performance Considerations

### CPU-Intensive Encoders
- H.265/HEVC, AV1, VP9 require significant processing power
- Consider system load and processing time requirements
- May benefit from hardware acceleration when available

### Balanced Encoders
- H.264/AVC provides good balance of quality, compatibility, and performance
- MPEG-4 offers reasonable performance for older systems

### Fast Encoders
- MJPEG provides fast encoding but larger file sizes
- Consider for real-time or low-latency applications

## See Also

- [Get Audio Encoders](pages/GetAudioEncoders.md) - Query audio encoder capabilities
- [MP4 To MP3](pages/MP4ToMP3.md) - Video processing for audio extraction
- [Extension Configuration](pages/ExtensionConfiguration.md) - Configure the extension
- [Authentication](pages/Authentication.md) - Authentication requirements
