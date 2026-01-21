# Get CSV As Table

## Overview

Converts CSV file data into a structured table format with separate headers and rows, providing a more organized representation for display and processing purposes.

## Request Details

- **Area**: Operations
- **Type**: CHANGE_SYSTEM
- **Retry Support**: ✅ Yes (Read operations can be safely retried without side effects)

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| CSV | File | Yes | CSV file to convert to table format | `sales_data.csv` |
| fields | [ Text ] | No | List of field names to exclude from table | `["internal_id", "password"]` |

### Parameter Details

#### CSV
- **Purpose**: Source CSV file to be converted to table structure
- **Format**: Standard comma-separated values file with .csv extension
- **Requirements**: Must have header row with column names
- **Encoding**: UTF-8 recommended for best compatibility
- **Size Limit**: Recommended maximum 50MB for optimal performance

#### fields (Optional)
- **Purpose**: Specify columns to exclude from the table output
- **Behavior**: Listed field names will be filtered out of both headers and rows
- **Case Sensitivity**: Field matching is case-insensitive
- **Use Cases**: Remove sensitive data, simplify table display, focus on specific columns

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Response | Composite | Table structure with separate headers and rows arrays |

### Output Details

#### Response Structure
```json
{
  "headers": ["column1", "column2", "column3"],
  "rows": [
    ["value1", "value2", "value3"],
    ["value4", "value5", "value6"]
  ]
}
```

- **headers**: Array of column names from CSV header row
- **rows**: Array of arrays, each representing a data row
- **Filtering**: Both headers and corresponding row values are filtered based on fields parameter
- **Data Types**: All values are preserved as strings

## Validation Rules

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| CSV file not null | "File not found or inaccessible. Please verify the file exists, is not corrupted, and you have permission to access it." | Provide a valid CSV file |
| Valid CSV format | "Invalid CSV file provided. Please ensure the file has a .csv extension and contains properly formatted comma-separated values." | Upload a properly formatted CSV file |
| File accessibility | "Unable to parse the CSV file. This may be due to malformed CSV structure, invalid characters, or encoding issues." | Check file format and encoding |

## Error Handling

### Input Errors (INPUT_ERROR)
**Cause**: Invalid or missing input parameters
**Common Scenarios**:
- Null or missing CSV file
- Invalid file format
- Corrupted file upload

**Resolution**:
1. Verify CSV file is properly uploaded
2. Check file has .csv extension
3. Ensure file is not corrupted
4. Try uploading the file again

### Logic Errors (LOGIC_ERROR)
**Cause**: CSV parsing or table conversion failures
**Common Scenarios**:
- Malformed CSV structure
- Invalid characters in CSV
- Encoding issues
- Missing or malformed header row

**Resolution**:
1. Verify CSV has proper header row
2. Check for invalid characters or encoding issues
3. Ensure CSV follows standard format
4. Validate CSV structure in spreadsheet application

### System Errors (SYSTEM_ERROR)
**Cause**: System-level failures during processing
**Common Scenarios**:
- Insufficient memory for large files
- File system access issues
- Processing timeout for very large files

**Resolution**:
1. Try with smaller CSV files
2. Check system resources and memory
3. Contact administrator for persistent issues
4. Retry the operation

## Usage Examples

### Example 1: Basic Table Conversion
**Input CSV File** (`employees.csv`):
```csv
name,age,department,salary
John Doe,30,Engineering,75000
Jane Smith,25,Marketing,65000
Bob Johnson,35,Sales,70000
```

**Input Parameters**:
```
CSV: employees.csv
fields: (empty)
```

**Output**:
```json
{
  "headers": ["name", "age", "department", "salary"],
  "rows": [
    ["John Doe", "30", "Engineering", "75000"],
    ["Jane Smith", "25", "Marketing", "65000"],
    ["Bob Johnson", "35", "Sales", "70000"]
  ]
}
```

### Example 2: Table with Field Filtering
**Input CSV File** (`users.csv`):
```csv
username,email,password,role,status
jdoe,john@example.com,secret123,admin,active
jsmith,jane@example.com,pass456,user,active
bwilson,bob@example.com,mypass789,user,inactive
```

**Input Parameters**:
```
CSV: users.csv
fields: ["password"]
```

**Output**:
```json
{
  "headers": ["username", "email", "role", "status"],
  "rows": [
    ["jdoe", "john@example.com", "admin", "active"],
    ["jsmith", "jane@example.com", "user", "active"],
    ["bwilson", "bob@example.com", "user", "inactive"]
  ]
}
```

### Example 3: Complex Data with Quoted Values
**Input CSV File** (`products.csv`):
```csv
id,name,description,price,category
1,"Widget A","High-quality widget, best in class",29.99,electronics
2,"Widget B","Budget option",19.99,electronics
3,"Gadget X","Professional tool, heavy duty",99.99,tools
```

**Output**:
```json
{
  "headers": ["id", "name", "description", "price", "category"],
  "rows": [
    ["1", "Widget A", "High-quality widget, best in class", "29.99", "electronics"],
    ["2", "Widget B", "Budget option", "19.99", "electronics"],
    ["3", "Gadget X", "Professional tool, heavy duty", "99.99", "tools"]
  ]
}
```

## Business Rules

1. **Header Preservation**: CSV header row becomes the headers array
2. **Row Order**: Data rows maintain their original order from the CSV
3. **Field Filtering**: Applies to both headers and corresponding row values
4. **Data Type Consistency**: All values are preserved as strings
5. **Empty Cell Handling**: Empty CSV cells become empty strings in table
6. **Quote Processing**: Quoted CSV values are properly unquoted in table output

## Limitations

1. **File Size**: Large CSV files (>50MB) may impact performance
2. **Memory Usage**: Entire CSV and table structure loaded into memory
3. **Data Types**: No automatic type conversion; all values remain as strings
4. **Encoding**: Non-UTF-8 files may have character display issues
5. **Structure**: Cannot handle multi-level headers or complex table structures

## Best Practices

### 1. File Preparation
- Ensure CSV has clear, descriptive header row
- Use UTF-8 encoding for international characters
- Validate CSV format before processing
- Keep file sizes reasonable for performance

### 2. Table Display Optimization
- Use field filtering to show only relevant columns
- Consider column order for better readability
- Handle long text values appropriately in display

### 3. Performance Considerations
- Process large files in smaller chunks if needed
- Use field filtering to reduce memory usage
- Monitor system resources with large datasets

### 4. Data Security
- Filter out sensitive columns using fields parameter
- Validate file contents before processing
- Implement proper access controls

## Common Use Cases

### 1. Data Visualization
```
Scenario: Display CSV data in a web table or grid component
Action: Convert CSV to table format for structured display
Result: Organized headers and rows ready for UI rendering
```

### 2. Report Generation
```
Scenario: Create formatted reports from CSV data
Action: Convert CSV to table and apply formatting
Result: Structured data ready for report templates
```

### 3. Data Analysis Preparation
```
Scenario: Prepare CSV data for analysis tools
Action: Convert to table format with field filtering
Result: Clean, structured data for analysis processing
```

### 4. API Response Formatting
```
Scenario: Return CSV data through API in structured format
Action: Convert CSV to table for consistent API response
Result: Standardized table format for API consumers
```

## Related Catalog Requests

- [Read CSV File](pages/ReadCSVFile.md) - Parse CSV into entity objects
- [Create CSV File](pages/CreateCSVFile.md) - Generate CSV files from data
- [Read CSV String](pages/ReadCSVString.md) - Process CSV data from string input

## Technical Implementation

### Helper Class
- **Class**: SeparatedValueDaoImpl
- **Package**: app.krista.extensions.essentials.documents.files.impl
- **Validation**: File format and accessibility validation
- **Service**: getCSVAsTable() method

### Processing Flow
1. Validate CSV file is not null and accessible
2. Parse CSV file using OpenCSV library
3. Extract header row as headers array
4. Convert data rows to arrays of strings
5. Apply field filtering to both headers and rows
6. Return composite object with headers and rows

### Data Structure
```java
// Internal representation
Map<String, Object> tableStructure = new HashMap<>();
tableStructure.put("headers", headersList);
tableStructure.put("rows", rowsList);
```

### Telemetry Metrics
- TABLE_CONVERSION_SUCCESS: Successful CSV to table conversion
- TABLE_CONVERSION_ERROR: Failed conversion
- FIELD_FILTER_APPLIED: Field filtering was used
- ROW_COUNT: Number of rows processed

## Troubleshooting

### Table Structure Issues
**Cause**: Inconsistent CSV structure or malformed headers
**Solution**:
1. Verify CSV has proper header row
2. Check for consistent number of columns across rows
3. Validate CSV format in spreadsheet application

### Field Filtering Not Working
**Cause**: Field names don't match CSV headers exactly
**Solution**:
1. Check exact spelling of field names in fields parameter
2. Verify case sensitivity (filtering is case-insensitive)
3. Ensure field names match CSV column headers

### Memory Issues with Large Tables
**Cause**: CSV file too large for available memory
**Solution**:
1. Use field filtering to reduce table size
2. Process smaller CSV files
3. Increase system memory allocation

### Display Formatting Issues
**Cause**: Special characters or encoding problems
**Solution**:
1. Ensure CSV uses UTF-8 encoding
2. Check for special characters in data
3. Validate quote handling in CSV

## See Also

- [Read CSV File](pages/ReadCSVFile.md) - Parse CSV into entities
- [Create CSV File](pages/CreateCSVFile.md) - Generate CSV files
- [Extension Configuration](pages/ExtensionConfiguration.md) - Configure the extension
- [Authentication](pages/Authentication.md) - Authentication requirements
