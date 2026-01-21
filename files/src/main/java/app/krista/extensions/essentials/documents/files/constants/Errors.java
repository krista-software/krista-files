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

package app.krista.extensions.essentials.documents.files.constants;

public class Errors {
    private Errors() {
    }

    // CSV File Errors
    public static final String INVALID_CSV = "Invalid CSV file provided. Please ensure the file has a .csv extension and contains properly formatted comma-separated values. Check that the file is not corrupted and contains valid data.";

    // MP4 File Errors
    public static final String INVALID_MP4 = "Invalid MP4 file provided. Please ensure the file has a .mp4 extension and is a valid video file. The file should not be corrupted and must contain video content that can be processed.";

    // File Name Errors
    public static final String INVALID_FILE_NAME = "Invalid file name provided. File names can only contain letters (A-Z, a-z), numbers (0-9), and periods (.). Special characters and spaces are not allowed. Please use a name between 1-255 characters.";

    // Data Errors
    public static final String EMPTY_DATA = "No data provided for processing. Please ensure you have valid data entries before attempting to create a file.";
    public static final String FILE_NOT_FOUND = "File not found or inaccessible. Please verify the file exists, is not corrupted, and you have permission to access it.";
    public static final String EMPTY_FILE = "The provided file is empty or contains no readable data. Please check the file content and ensure it has valid data before processing.";

    // CSV Processing Errors
    public static final String CSV_PARSING_ERROR = "Unable to parse the CSV file. This may be due to malformed CSV structure, invalid characters, or encoding issues. Please verify the file format and try again.";
    public static final String EMPTY_CSV_STRING = "The CSV string is empty or contains no data. Please provide a valid CSV string with headers and data rows.";

    // Video Processing Errors
    public static final String AUDIO_ENCODER_ERROR = "Unable to retrieve audio encoders. This may be due to system configuration issues or missing codec libraries. Please contact your system administrator.";
    public static final String VIDEO_ENCODER_ERROR = "Unable to retrieve video encoders. This may be due to system configuration issues or missing codec libraries. Please contact your system administrator.";
    public static final String VIDEO_CONVERSION_ERROR = "Failed to convert video file. Please ensure the input file is a valid MP4 video and try again. If the problem persists, the file may be corrupted or in an unsupported format.";

    // General Errors
    public static final String EMPTY_ERROR_MESSAGE = "An error occurred but no specific error message was provided. Please try the operation again or contact support if the issue persists.";
}
