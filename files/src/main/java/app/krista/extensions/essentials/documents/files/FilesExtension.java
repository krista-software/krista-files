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

package app.krista.extensions.essentials.documents.files;


import app.krista.extension.impl.anno.*;
import app.krista.extensions.essentials.documents.files.constants.Errors;

import java.util.Map;

/**
 * Krista Files Extension - Main extension class for file processing and video conversion capabilities.
 *
 * <p>This extension provides comprehensive support for:</p>
 * <ul>
 *   <li>CSV file operations (create, read, parse)</li>
 *   <li>Separated value processing (CSV, TSV, PSV)</li>
 *   <li>MP4 video-to-audio conversion (MP3, WAV, WAV MONO)</li>
 *   <li>Audio and video encoder information retrieval</li>
 * </ul>
 *
 * <p>The extension supports configurable file naming with optional prefix support
 * and includes comprehensive error handling and validation.</p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 *   <li><strong>CSV File Processing:</strong> Create CSV files from entity data, read and parse CSV files with field filtering</li>
 *   <li><strong>Video Conversion:</strong> Convert MP4 files to various audio formats using JAVE library</li>
 *   <li><strong>Advanced Features:</strong> Field filtering, complex entity structures, comprehensive validation</li>
 * </ul>
 *
 * <h3>Dependencies:</h3>
 * <ul>
 *   <li>OpenCSV 5.8 - CSV processing</li>
 *   <li>JAVE 3.3.1 - Video conversion</li>
 *   <li>Apache POI 5.2.3 - Spreadsheet support</li>
 *   <li>JSON Flattener 0.16.6 - Data transformation</li>
 * </ul>
 *
 * @author Krista Extensions Team
 * @version 2.0.5
 * @since 1.0.0
 */
@Java(version = Java.Version.JAVA_21)
@Field.Text(name = FileAttributes.FILE_NAME_PREFIX, required = false)
@Extension(version = "2.0.6")
@StaticResource(path = "docs", file = "docs")
public final class FilesExtension {

    /**
     * Provides custom tabs for the extension's user interface.
     *
     * <p>This method is called by the Krista platform to register custom tabs
     * that will be displayed in the extension's interface. The Files extension
     * provides a Documentation tab that links to the static documentation.</p>
     *
     * @return A map containing tab names and their corresponding paths
     */
    @InvokerRequest(InvokerRequest.Type.CUSTOM_TABS)
    public Map<String, String> getCustomTabs() {
        return Map.of("Documentation", "static/docs");
    }

    /**
     * Validates the extension's configuration attributes.
     *
     * <p>This method validates the file name prefix attribute to ensure it meets
     * the required format constraints. The file name prefix must contain only
     * alphanumeric characters and periods, with a length between 1-255 characters.</p>
     *
     * @param connectionAttributes Map containing the extension's configuration attributes
     * @throws IllegalArgumentException if the file name prefix is invalid
     */
    @InvokerRequest(InvokerRequest.Type.VALIDATE_ATTRIBUTES)
    public void validateAttributes(Map<String, Object> connectionAttributes) {
        String filename = (String) connectionAttributes.get(FileAttributes.FILE_NAME_PREFIX);
        String REGEX_PATTERN = "^[A-Za-z0-9.]{1,255}$";
        if (validateString(filename) && !filename.matches(REGEX_PATTERN)) {
            throw new IllegalArgumentException(Errors.INVALID_FILE_NAME);
        }
    }

    /**
     * Validates if a string is not null, empty, or blank.
     *
     * @param string The string to validate
     * @return true if the string is valid (not null, not empty, not blank), false otherwise
     */
    private Boolean validateString(String string) {
        return string != null && !string.isEmpty() && !string.isBlank();
    }
}
