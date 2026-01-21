# Troubleshooting Guide

This guide helps you resolve common issues when using the Krista Files Extension.

## Common Error Messages and Solutions

### File-Related Errors

#### "Invalid CSV file provided. Please ensure the file has a .csv extension..."

**Cause:** The uploaded file is not a valid CSV file or has formatting issues.

**Solutions:**
1. **Check file extension**: Ensure your file has a `.csv` extension
2. **Verify file format**: Open the file in a text editor to confirm it contains comma-separated values
3. **Check file integrity**: Ensure the file is not corrupted
4. **Validate CSV structure**: Make sure the file has proper headers and data rows

**Example of valid CSV format:**
```
Name,Age,Email
John Doe,30,john@example.com
Jane Smith,25,jane@example.com
```

#### "Invalid MP4 file provided. Please ensure the file has a .mp4 extension..."

**Cause:** The uploaded file is not a valid MP4 video file.

**Solutions:**
1. **Check file extension**: Ensure your file has a `.mp4` extension
2. **Verify video format**: Confirm the file is a valid MP4 video
3. **Check file size**: Ensure the file is not too large or corrupted
4. **Test playback**: Try playing the file in a media player to verify it works

#### "Invalid file name provided. File names can only contain letters..."

**Cause:** The file name contains invalid characters.

**Solutions:**
1. **Use only allowed characters**: Letters (A-Z, a-z), numbers (0-9), and periods (.)
2. **Remove special characters**: Avoid spaces, symbols, and special characters
3. **Check length**: Ensure the name is between 1-255 characters
4. **Example valid names**: `MyFile.csv`, `Report2024.mp4`, `Data.File.csv`

### Data Processing Errors

#### "No data provided for processing. Please ensure you have valid data entries..."

**Cause:** The input data is empty or null.

**Solutions:**
1. **Check data source**: Ensure your data source contains valid entries
2. **Verify data format**: Confirm the data is in the expected format
3. **Check for empty collections**: Make sure lists and arrays are not empty
4. **Validate data structure**: Ensure the data has the required fields

#### "The CSV string is empty or contains no data..."

**Cause:** The provided CSV string has no content.

**Solutions:**
1. **Check input string**: Ensure the CSV string is not empty
2. **Verify format**: Make sure the string contains valid CSV data
3. **Include headers**: Ensure the CSV string has header row
4. **Check encoding**: Verify the string encoding is correct

### Video Processing Errors

#### "Unable to retrieve audio encoders. This may be due to system configuration..."

**Cause:** System cannot access audio encoding libraries.

**Solutions:**
1. **Contact administrator**: This is typically a system configuration issue
2. **Check codec installation**: Verify required audio codecs are installed
3. **Restart service**: Try restarting the Krista service
4. **Check system logs**: Review system logs for additional error details

#### "Failed to convert video file. Please ensure the input file is a valid MP4..."

**Cause:** Video conversion process failed.

**Solutions:**
1. **Verify MP4 format**: Ensure the input file is a valid MP4
2. **Check file corruption**: Try playing the file to verify it's not corrupted
3. **Reduce file size**: Large files may cause conversion issues
4. **Check available space**: Ensure sufficient disk space for conversion
5. **Try different file**: Test with a known working MP4 file

## Best Practices

### File Upload Guidelines

1. **File Size Limits**
   - Keep CSV files under 50MB for optimal performance
   - MP4 files should be under 500MB to avoid timeout issues

2. **File Naming**
   - Use descriptive names without special characters
   - Include file extensions (.csv, .mp4)
   - Avoid very long file names

3. **CSV File Preparation**
   - Ensure consistent column headers
   - Use UTF-8 encoding when possible
   - Avoid empty rows in the middle of data
   - Quote fields containing commas or special characters

4. **MP4 File Preparation**
   - Use standard MP4 encoding (H.264 video, AAC audio)
   - Ensure files are not password protected
   - Test files in a media player before uploading

### Performance Optimization

1. **Large Data Sets**
   - Process data in smaller batches when possible
   - Use appropriate field filtering to reduce processing time
   - Consider file size limitations

2. **Error Prevention**
   - Validate data before processing
   - Use consistent data formats
   - Test with small samples first

## Getting Additional Help

If you continue to experience issues after following this guide:

1. **Check Release Notes**: Review the latest release notes for known issues
2. **Contact Support**: Provide specific error messages and steps to reproduce
3. **Include Details**: Share file types, sizes, and system configuration
4. **Log Information**: Include relevant log entries when reporting issues

## Frequently Asked Questions

**Q: Can I upload Excel files instead of CSV?**
A: No, only CSV files are supported. Convert Excel files to CSV format first.

**Q: What video formats other than MP4 are supported?**
A: Currently, only MP4 format is supported for video conversion operations.

**Q: Why do I get encoding errors with my CSV file?**
A: Try saving your CSV file with UTF-8 encoding, which is the most compatible format.

**Q: Can I process multiple files at once?**
A: The extension processes one file at a time. For multiple files, process them individually.

**Q: What should I do if video conversion takes too long?**
A: Large video files may take significant time to convert. Consider using smaller files or contact your administrator about timeout settings.
