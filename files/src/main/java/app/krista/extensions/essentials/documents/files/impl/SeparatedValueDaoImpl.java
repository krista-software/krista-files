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

package app.krista.extensions.essentials.documents.files.impl;

import app.krista.extensions.essentials.documents.files.constants.Errors;
import app.krista.extensions.essentials.documents.files.impl.util.Constants;
import app.krista.extensions.essentials.documents.files.impl.util.SpreadsheetHelper;
import app.krista.extensions.essentials.documents.files.service.SeparatedValueDao;
import app.krista.model.base.FreeForm;
import com.google.gson.JsonArray;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jvnet.hk2.annotations.Service;

import java.io.*;
import java.util.*;

/**
 * Implementation of {@link SeparatedValueDao} for processing separated value files.
 *
 * <p>This service provides comprehensive support for creating, reading, and processing
 * CSV, TSV, and PSV files. It handles data conversion between various formats including
 * entity collections, file objects, and table structures.</p>
 *
 * <h3>Key Features:</h3>
 * <ul>
 *   <li><strong>File Creation:</strong> Generate CSV files from entity data</li>
 *   <li><strong>File Reading:</strong> Parse CSV files with optional field filtering</li>
 *   <li><strong>Table Conversion:</strong> Convert CSV data to table format</li>
 *   <li><strong>String Processing:</strong> Handle CSV data as strings</li>
 * </ul>
 *
 * <p>The implementation uses Apache POI for spreadsheet processing and OpenCSV
 * for CSV parsing, providing robust handling of various file formats and edge cases.</p>
 *
 * @author Krista Extensions Team
 * @version 2.0.5
 * @since 1.0.0
 */
@Service
public class SeparatedValueDaoImpl implements SeparatedValueDao {
    @Override
    public File getSV(List<Map<String, Object>> data, String fileName) throws FileNotFoundException {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException(Errors.EMPTY_DATA);
        }
        JsonArray jsonData = Constants.GSON.fromJson(Constants.GSON.toJson(data), JsonArray.class);
        return writeInFile(fileName, jsonData);
    }

    /**
     * Reads CSV file and converts to FreeForm entities with optional field filtering.
     */
    @Override
    public List<FreeForm> readCSV(File file, List<String> fields) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException(Errors.FILE_NOT_FOUND);
        }
        List<FreeForm> columns = new ArrayList<>();
        Workbook workbook = getWorkbook(file);
        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        Sheet sheet = workbook.getSheetAt(0);
        int maxCellIndex = getMaxCellIndex(sheet);
        List<String> headers = getHeaders(sheet, maxCellIndex);
        addColumns(columns, sheet, maxCellIndex, headers, formulaEvaluator, fields);
        handleEmptySheetData(columns, headers);
        return columns;
    }

    /**
     * Reads separated value file and returns data as table structure.
     */
    @Override
    public FreeForm readSVAsTable(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException(Errors.EMPTY_FILE);
        }
        final FreeForm response = new FreeForm();
        final List<List<String>> records = new ArrayList<>();
        try {
            CSVReader csvReader = new CSVReader(new FileReader(file));
            List<String[]> lines = csvReader.readAll();
            for (final String[] line : lines) {
                records.add(Arrays.asList(line));
            }
        } catch (CsvException csvException) {
            throw new IllegalArgumentException(Errors.CSV_PARSING_ERROR, csvException.getCause());
        }
        response.put("Data","[[ Text ]]" ,records);
        return response;
    }

    /**
     * Converts entity list to separated value string format (CSV/TSV/PSV).
     */
    @Override
    public String getSVString(List<Map<String, Object>> entities, String fieldType) {
        if (entities == null || entities.isEmpty()) {
            throw new IllegalArgumentException("Entities are null or empty");
        }
        if (fieldType == null || fieldType.isBlank()) {
            throw new IllegalArgumentException("Field type is null or blank");
        }
        StringBuilder result = new StringBuilder();
        String separator = getSeparator(fieldType);
        result.append(String.join(separator, entities.get(0).keySet())).append("\n");
        entities.stream().map(Map::values).forEach(values -> {
            values.forEach(value -> {
                if (value.toString().contains(","))
                    result.append("\"").append(value).append("\"").append(separator);
                else
                    result.append(value).append(separator);
            });
            int index = result.lastIndexOf(separator);
            result.replace(index, index + 1, "\n");
        });
        return result.toString();
    }




    /**
     * Parses CSV string and converts to FreeForm entities with field filtering.
     */
    @Override
    public List<FreeForm> readCSVString(String csv, List<String> fields) {
        if (csv == null || csv.isBlank()) {
            throw new IllegalArgumentException(Errors.EMPTY_CSV_STRING);
        }
        List<String> row = List.of(csv.split("\n"));
        List<List<String>> entries = new ArrayList<>();

        String splitByCommaIgnoreInQuotes = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
        row.forEach(entry -> entries.add(List.of(entry.split(splitByCommaIgnoreInQuotes))));

        List<FreeForm> freeForms = new ArrayList<>();

        List<String> keys = entries.get(0);
        List<List<String>> rows = entries.subList(1, entries.size());

        rows.forEach(entry -> {
            FreeForm freeForm = new FreeForm();
            for (int i = 0; i < keys.size(); i++) {
                if (fields == null || fields.stream().noneMatch(keys.get(i)::equalsIgnoreCase)) {
                    freeForm.put(keys.get(i), "Text", entry.get(i).replaceAll("\"", ""));
                }
            }
            freeForms.add(freeForm);
        });
        return freeForms;
    }

    /**
     * Handles empty sheet by creating default FreeForm with headers.
     */
    private void handleEmptySheetData(List<FreeForm> columns, List<String> headers) {
        if (columns.isEmpty()) {
            FreeForm freeForm = new FreeForm();
            for (String header : headers) {
                freeForm.put(header, "Text", "");
            }
            columns.add(freeForm);
        }
    }

    /**
     * Processes sheet rows and adds data to columns list.
     */
    private void addColumns(List<FreeForm> columns, Sheet sheet, int maxCellIndex, List<String> headers, FormulaEvaluator formulaEvaluator, List<String> fields) {
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (SpreadsheetHelper.isRowContainsData(row)) {
                columns.add(SpreadsheetHelper.getRow(maxCellIndex, headers, row, formulaEvaluator, fields));
            }
        }
    }

    /**
     * Extracts headers from first row of sheet.
     */
    private List<String> getHeaders(Sheet sheet, int maxCellIndex) {
        List<String> headers = new ArrayList<>();
        SpreadsheetHelper.parseHeaders(headers, sheet.getRow(0), maxCellIndex);
        return headers;
    }

    /**
     * Finds maximum cell index across all rows in sheet.
     */
    private int getMaxCellIndex(Sheet sheet) {
        int maxCellIndex = 0;
        for (Row row : sheet) {
            maxCellIndex = Math.max(row.getLastCellNum(), maxCellIndex);
        }
        return maxCellIndex;
    }

    /**
     * Returns appropriate separator based on field type (CSV/TSV/PSV).
     */
    private String getSeparator(String fieldType) {
        if (fieldType.equalsIgnoreCase("tsv")) {
            return "\t";
        } else if (fieldType.equalsIgnoreCase("csv")) {
            return ",";
        } else if (fieldType.equalsIgnoreCase("psv")) {
            return "|";
        } else {
            throw new IllegalArgumentException("Invalid field type - " + fieldType);
        }
    }

    /**
     * Writes JSON data to CSV file with headers and data rows.
     */
    private File writeInFile(String fileName, JsonArray jsonData) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(fileName);
        Set<String> columns = SpreadsheetHelper.getColumns(jsonData);
        Iterator<String> colIterator = columns.iterator();
        StringBuilder sb = new StringBuilder();
        while (colIterator.hasNext()) {
            sb.append(colIterator.next()).append(Constants.getDelimiter());
        }
        sb.deleteCharAt(sb.lastIndexOf(Constants.getDelimiter()));
        printWriter.write(sb.toString() + '\n');
        SpreadsheetHelper.addColumns(printWriter, jsonData, columns);
        printWriter.close();
        return new File(fileName);
    }

    /**
     * Creates workbook from CSV file by parsing headers and data.
     */
    private Workbook getWorkbook(File file) throws IOException {
        String regEx = Constants.getDelimiter();
        FileInputStream inputStream = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String[] headerArray = reader.readLine().split(regEx, -10);
        Map<String, Integer> headerMap = SpreadsheetHelper.parseHeaders(headerArray);
        List<List<String>> columns = SpreadsheetHelper.getColumns(reader);
        return getSheets(headerMap, columns);
    }

    /**
     * Creates Excel workbook with headers and data rows.
     */
    private Workbook getSheets(Map<String, Integer> headerMap, List<List<String>> columns) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
            headerRow.createCell(entry.getValue()).setCellValue(entry.getKey());
        }
        for (List<String> row : columns) {
            Row currentRow = sheet.createRow(rowNum++);
            int columnIndex = 0;
            for (String cell : row) {
                currentRow.createCell(columnIndex++).setCellValue(cell);
            }
        }
        return workbook;
    }

}
