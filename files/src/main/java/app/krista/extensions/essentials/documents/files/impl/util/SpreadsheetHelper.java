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

package app.krista.extensions.essentials.documents.files.impl.util;

import app.krista.model.base.FreeForm;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.poi.ss.usermodel.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

/**
 * Utility class for spreadsheet and CSV processing operations.
 *
 * <p>This helper class provides static methods for parsing headers, processing
 * spreadsheet data, and handling various file formats including CSV, XLS, and XLSX.
 * It includes robust error handling and support for different data types.</p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 *   <li><strong>Header Processing:</strong> Parse and validate spreadsheet headers</li>
 *   <li><strong>Data Conversion:</strong> Convert between different data formats</li>
 *   <li><strong>File Support:</strong> Handle CSV, XLS, and XLSX files</li>
 *   <li><strong>Type Safety:</strong> Proper handling of different cell data types</li>
 * </ul>
 *
 * @author Krista Extensions Team
 * @version 2.0.5
 * @since 1.0.0
 */
public class SpreadsheetHelper {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private SpreadsheetHelper() {
    }

    /**
     * Parses headers from a spreadsheet row and populates the headers list.
     *
     * <p>This method extracts header values from the specified row and handles
     * empty or null cells by generating default header names.</p>
     *
     * @param headers The list to populate with header names
     * @param row The spreadsheet row containing header data
     * @param maxColumnIndex The maximum column index to process
     * @throws IllegalArgumentException if parameters are invalid
     */
    public static void parseHeaders(List<String> headers, Row row, int maxColumnIndex) {
        if (headers == null) {
            throw new IllegalArgumentException("Failed to get headers. Headers list is not initialized");
        } else if (row == null) {
            throw new IllegalArgumentException("Failed to get headers. No rows found.");
        } else if (maxColumnIndex <= 0) {
            throw new IllegalArgumentException("Failed to get headers. Max column index is wrong.");
        }
        for (int i = 0, j = 0; i < maxColumnIndex; i++) {
            String cellValue = String.valueOf(row.getCell(i));
            headers.add(row.getCell(i) == null || cellValue.isBlank() ? "Untitled Header " + (++j) : cellValue);
        }
    }

    /**
     * Parses header array and creates header-to-index mapping.
     */
    public static Map<String, Integer> parseHeaders(String[] headerArray) {
        Map<String, Integer> headerMap = new LinkedHashMap<>();
        if (headerArray == null || headerArray.length <= 0) {
            throw new IllegalArgumentException("Failed to get headers. No headers found");
        }
        for (int i = 0; i < headerArray.length; i++) {
            String value = headerArray[i];
            headerMap.put(value.equals("") ? "Untitled Header " + (i + 1) : value, i);
        }
        return headerMap;
    }

    /**
     * Reads CSV data from BufferedReader and returns as list of rows.
     */
    public static List<List<String>> getColumns(BufferedReader reader) throws IOException {
        if (reader == null) {
            throw new IllegalStateException("Failed to get columns. Input stream is empty.");
        }
        String splitByCommaIgnoreInQuotes = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
        List<List<String>> columns = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String[] lineItems = line.split(splitByCommaIgnoreInQuotes); //splitting the line and adding its items in String[]
            List<String> row = new ArrayList<>(Arrays.asList(lineItems));
            columns.add(row); //adding the splitted line array to the ArrayList
        }
        return columns;
    }

    /**
     * Extracts unique column names from JSON array data.
     */
    public static Set<String> getColumns(JsonArray jsonData) {
        if (jsonData == null) {
            throw new IllegalStateException("Failed to get columns. Json array not found.");
        }
        Set<String> columns = new LinkedHashSet<>();
        for (int i = 0; i < jsonData.size(); i++) {
            JsonObject jsonObject = jsonData.get(i).getAsJsonObject();
            columns.addAll(jsonObject.keySet());
        }
        return columns;
    }

    /**
     * Converts spreadsheet row to FreeForm with field filtering and formula evaluation.
     */
    public static FreeForm getRow(int maxColumnIndex, List<String> headers, Row row, FormulaEvaluator formulaEvaluator, List<String> fields) {
        if (maxColumnIndex <= 0) {
            throw new IllegalArgumentException("Failed to get row. Invalid max column index.");
        } else if (headers == null || headers.isEmpty()) {
            throw new IllegalArgumentException("Failed to get row. Headers not found.");
        } else if (row == null) {
            throw new IllegalArgumentException("Failed to get row. No row to parse.");
        }
        FreeForm freeForm = new FreeForm();
        for (int i = 0; i < maxColumnIndex; i++) {
            Cell cell = row.getCell(i);
            String value = cell == null || cell.toString().isBlank() ? "" :
                    formulaEvaluator.evaluate(cell).getCellType() == CellType.NUMERIC && cell.toString().contains("E") ? new BigDecimal(cell.getNumericCellValue()).toPlainString() : cell.toString();
            if (fields == null || fields.stream().noneMatch(headers.get(i)::equalsIgnoreCase)) {
                freeForm.put(headers.get(i), "Text", value.replace("\"", ""));
            }
        }
        return freeForm;
    }

    /**
     * Writes JSON data to CSV format with proper comma handling and quoting.
     */
    public static void addColumns(PrintWriter printWriter, JsonArray jsonData, Set<String> columns) {
        if (printWriter == null) {
            throw new IllegalArgumentException("Failed to add columns. No print writer found.");
        } else if (jsonData == null) {
            throw new IllegalArgumentException("Failed to add columns. No data found.");
        } else if (columns == null || columns.isEmpty()) {
            throw new IllegalArgumentException("Failed to add columns. Columns not found.");
        }
        for (int i = 0; i < jsonData.size(); i++) {
            JsonObject jsonObject = jsonData.get(i).getAsJsonObject();
            StringBuilder sb = new StringBuilder();
            for (String key : columns) {
                String value = jsonObject.get(key) != null ? String.valueOf(jsonObject.get(key)) : "";
                if (value.contains(",")) {
                    value = value.replace("\"", "'");
                    value = "\"" + value + "\"";
                }
                sb.append(value).append(Constants.getDelimiter());
            }
            sb.deleteCharAt(sb.lastIndexOf(Constants.getDelimiter()));
            printWriter.write(sb.toString() + '\n');
        }
    }

    /**
     * Checks if spreadsheet row contains any non-empty data.
     */
    public static boolean isRowContainsData(Row row) {
        DataFormatter dataFormatter = new DataFormatter();
        if (row != null) {
            for (Cell cell : row) {
                if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
