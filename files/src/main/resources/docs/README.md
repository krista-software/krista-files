# Krista Files Extension

## Overview

The Krista Files Extension enables powerful file processing and video conversion capabilities within the Krista platform. This extension provides comprehensive support for CSV file operations, separated value processing, and MP4 video-to-audio conversion.

## Key Features

✅ **CSV File Processing**
- Create CSV files from entity data
- Read and parse CSV files with field filtering
- Convert CSV data to table format
- Process CSV strings directly

✅ **Separated Value Support**
- CSV (Comma-Separated Values)
- TSV (Tab-Separated Values)
- PSV (Pipe-Separated Values)

✅ **Video Conversion**
- MP4 to MP3 audio conversion
- MP4 to WAV audio conversion
- MP4 to WAV MONO (single channel) conversion
- Audio and video encoder information

✅ **Advanced Features**
- Configurable file naming with prefix support
- Comprehensive error handling and validation
- Field filtering for selective data processing
- Support for complex entity structures

## Quick Start Guide

### 1. Configure the Extension
Set up the Files extension in your Krista environment with optional file naming preferences.

### 2. Choose Your Operation
Select from file operations (CSV processing) or video operations (MP4 conversion) based on your needs.

### 3. Process Your Data
Use the catalog requests to create, read, or convert files according to your workflow requirements.

## Documentation Structure

### 📚 Getting Started
- **[Extension Configuration](pages/ExtensionConfiguration.md)** - Set up and configure the Files extension
- **[Authentication](pages/Authentication.md)** - Authentication requirements (minimal for Files extension)

### 📄 File Operations
- **[Create CSV File](pages/CreateCSVFile.md)** - Generate CSV files from entity data
- **[Read CSV File](pages/ReadCSVFile.md)** - Parse and extract data from CSV files
- **[Get CSV As Table](pages/GetCSVAsTable.md)** - Convert CSV to table format
- **[Read CSV String](pages/ReadCSVString.md)** - Process CSV data from string input
- **[Create SV String](pages/CreateSVString.md)** - Generate separated value strings

### 🎥 Video Operations
- **[MP4 To MP3](pages/MP4ToMP3.md)** - Convert MP4 videos to MP3 audio
- **[MP4 To WAV](pages/MP4ToWAV.md)** - Convert MP4 videos to WAV audio
- **[MP4 To WAV MONO](pages/MP4ToWAVMONO.md)** - Convert MP4 to single-channel WAV

### 🔍 Query Operations
- **[Get Audio Encoders](pages/GetAudioEncoders.md)** - Retrieve available audio encoders
- **[Get Video Encoders](pages/GetVideoEncoders.md)** - Retrieve available video encoders

## Support & Resources

### Version Information
- **Extension Version**: 2.0.5
- **Krista APIs**: 1.0.89
- **Java Version**: 11+

### Key Dependencies
- **OpenCSV**: 5.8 (CSV processing)
- **JAVE**: 3.3.1 (video conversion)
- **Apache POI**: 5.2.3 (spreadsheet support)
- **JSON Flattener**: 0.16.6 (data transformation)

### Technical Specifications
- **Supported File Types**: CSV, TSV, PSV, MP4
- **Output Formats**: CSV, TSV, PSV, MP3, WAV
- **Maximum File Name Length**: 255 characters
- **Allowed File Name Characters**: Letters (A-Z, a-z), numbers (0-9), periods (.)

### Error Handling
The Files extension provides detailed, actionable error messages for all operations. Each catalog request includes comprehensive validation and error handling to guide users through successful file processing.

### Performance Considerations
- **CSV Files**: Recommended maximum size 50MB for optimal performance
- **MP4 Files**: Recommended maximum size 500MB to avoid timeout issues
- **Batch Processing**: Process large datasets in smaller chunks when possible

## Getting Help

For detailed information about specific operations, refer to the individual catalog request documentation pages listed above. Each page includes:
- Complete parameter specifications
- Validation rules and error handling
- Usage examples and best practices
- Troubleshooting guidance
- Technical implementation details

---

*This documentation follows the Krista Extension Documentation Guidelines for comprehensive, user-friendly technical documentation.*
