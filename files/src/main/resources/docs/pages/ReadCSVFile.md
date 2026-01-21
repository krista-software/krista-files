# Read CSV File

## Overview

Reads and parses CSV files, converting them into structured entity data with optional field filtering to exclude specific columns from the output.

## Request Details

- **Area**: Operations
- **Type**: CHANGE_SYSTEM
- **Retry Support**: ✅ Yes (Read operations can be safely retried without side effects)

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| CSV | File | Yes | CSV file to read and parse | `employees.csv` |
| fields | [ Text ] | No | List of field names to exclude from output | `["password", "ssn", "internal_id"]` |

### Parameter Details

#### CSV
- **Purpose**: Source CSV file to be parsed and converted to entities
- **Format**: Standard comma-separated values file with .csv extension
- **Requirements**: Must have header row with column names
- **Encoding**: UTF-8 recommended for best compatibility
- **Size Limit**: Recommended maximum 50MB for optimal performance

#### fields (Optional)
- **Purpose**: Specify columns to exclude from the output
- **Behavior**: Listed field names will be filtered out of the result entities
- **Case Sensitivity**: Field matching is case-insensitive
- **Use Cases**: Remove sensitive data, reduce output size, filter unnecessary columns

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Response | [ Composite ] | Array of entity objects representing CSV rows |

### Output Details

#### Response Entities
- **Structure**: Each CSV row becomes a composite entity
- **Properties**: CSV column headers become entity property names
- **Data Types**: All values are treated as text/string type
- **Filtering**: Excludes any fields specified in the fields parameter
- **Empty Values**: Empty CSV cells become empty string properties

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
**Cause**: CSV parsing or processing failures
**Common Scenarios**:
- Malformed CSV structure
- Invalid characters in CSV
- Encoding issues
- Missing header row

**Resolution**:
1. Verify CSV has proper header row
2. Check for invalid characters or encoding issues
3. Ensure CSV follows standard format (comma-separated, quoted strings)
4. Try opening CSV in spreadsheet application to verify format

### System Errors (SYSTEM_ERROR)
**Cause**: System-level failures during file processing
**Common Scenarios**:
- Insufficient memory for large files
- File system access issues
- Temporary directory problems

**Resolution**:
1. Try with smaller CSV files
2. Check system resources and memory
3. Contact administrator for persistent issues
4. Retry the operation after some time

## Usage Examples

### Example 1: Basic CSV Reading
**Input CSV File** (`employees.csv`):
```csv
name,age,department,email
John Doe,30,Engineering,john@company.com
Jane Smith,25,Marketing,jane@company.com
Bob Johnson,35,Sales,bob@company.com
```

**Input Parameters**:
```
CSV: employees.csv
fields: (empty)
```

**Output**:
```json
[
  {
    "name": "John Doe",
    "age": "30", 
    "department": "Engineering",
    "email": "john@company.com"
  },
  {
    "name": "Jane Smith",
    "age": "25",
    "department": "Marketing", 
    "email": "jane@company.com"
  },
  {
    "name": "Bob Johnson",
    "age": "35",
    "department": "Sales",
    "email": "bob@company.com"
  }
]
```

### Example 2: CSV Reading with Field Filtering
**Input CSV File** (`users.csv`):
```csv
username,email,password,role,created_date
jdoe,john@example.com,secret123,admin,2024-01-15
jsmith,jane@example.com,pass456,user,2024-01-16
```

**Input Parameters**:
```
CSV: users.csv
fields: ["password"]
```

**Output**:
```json
[
  {
    "username": "jdoe",
    "email": "john@example.com",
    "role": "admin",
    "created_date": "2024-01-15"
  },
  {
    "username": "jsmith", 
    "email": "jane@example.com",
    "role": "user",
    "created_date": "2024-01-16"
  }
]
```

### Example 3: Complex CSV with Quoted Values
**Input CSV File** (`products.csv`):
```csv
name,description,price,tags
"Widget A","High-quality widget, best in class",29.99,"electronics,gadgets"
"Widget B","Budget-friendly option",19.99,"electronics,budget"
```

**Output**:
```json
[
  {
    "name": "Widget A",
    "description": "High-quality widget, best in class",
    "price": "29.99",
    "tags": "electronics,gadgets"
  },
  {
    "name": "Widget B",
    "description": "Budget-friendly option", 
    "price": "19.99",
    "tags": "electronics,budget"
  }
]
```

## Business Rules

1. **Header Requirement**: CSV files must have a header row with column names
2. **Data Type Handling**: All CSV values are treated as text/string in output entities
3. **Field Filtering**: Specified fields are completely excluded from output
4. **Case Insensitive Filtering**: Field name matching for filtering is case-insensitive
5. **Empty Cell Handling**: Empty CSV cells become empty string properties
6. **Quote Handling**: Quoted values in CSV are properly unquoted in output

## Limitations

1. **File Size**: Large CSV files (>50MB) may impact performance
2. **Memory Usage**: Entire CSV is loaded into memory during processing
3. **Data Types**: All output values are strings; no automatic type conversion
4. **Encoding**: Non-UTF-8 files may have character encoding issues
5. **Complex Structures**: Cannot handle nested CSV structures or multiple tables

## Best Practices

### 1. File Preparation
- Ensure CSV has proper header row
- Use UTF-8 encoding for international characters
- Quote values containing commas or special characters
- Validate CSV format before uploading

### 2. Performance Optimization
- Keep CSV files under 50MB when possible
- Use field filtering to reduce output size
- Process large files in smaller chunks if needed

### 3. Data Security
- Use field filtering to exclude sensitive columns
- Validate file contents before processing
- Implement proper access controls for CSV files

### 4. Error Prevention
- Test CSV format with small sample files first
- Handle encoding issues by saving CSV as UTF-8
- Verify header row matches expected column names

## Common Use Cases

### 1. Data Import
```
Scenario: Import customer data from external system
Action: Upload CSV file and read into entities for processing
Result: Structured customer data ready for system integration
```

### 2. Report Processing
```
Scenario: Process monthly sales report CSV
Action: Read CSV with field filtering to exclude internal columns
Result: Clean sales data for analysis and visualization
```

### 3. Data Migration
```
Scenario: Migrate data from legacy system via CSV export
Action: Read CSV files and convert to modern entity format
Result: Migrated data in structured format for new system
```

## Related Catalog Requests

- [Create CSV File](pages/CreateCSVFile.md) - Generate CSV files from entity data
- [Get CSV As Table](pages/GetCSVAsTable.md) - Convert CSV to table format
- [Read CSV String](pages/ReadCSVString.md) - Process CSV data from string input

## Technical Implementation

### Helper Class
- **Class**: SeparatedValueDaoImpl
- **Package**: app.krista.extensions.essentials.documents.files.impl
- **Validation**: File format and accessibility validation
- **Service**: readCSV() method with Apache POI and OpenCSV

### Processing Flow
1. Validate CSV file is not null and accessible
2. Convert Krista File to Java File object
3. Parse CSV using OpenCSV library
4. Apply field filtering if specified
5. Convert rows to FreeForm entities
6. Return entity list

### Telemetry Metrics
- CSV_READ_SUCCESS: Successful CSV file reading
- CSV_READ_ERROR: Failed CSV file reading
- FIELD_FILTER_APPLIED: Field filtering was used
- PROCESSING_TIME: Time taken to process CSV

## Troubleshooting

### CSV Parsing Errors
**Cause**: Malformed CSV structure or encoding issues
**Solution**:
1. Open CSV in text editor to check format
2. Verify proper comma separation and quoting
3. Check for special characters or encoding issues
4. Re-save CSV with UTF-8 encoding

### Memory Issues with Large Files
**Cause**: CSV file too large for available memory
**Solution**:
1. Split large CSV into smaller files
2. Increase JVM memory allocation
3. Use field filtering to reduce memory usage

### Field Filtering Not Working
**Cause**: Field names don't match CSV headers
**Solution**:
1. Check exact spelling of field names
2. Verify case sensitivity (filtering is case-insensitive)
3. Ensure field names match CSV column headers exactly

## See Also

- [Create CSV File](pages/CreateCSVFile.md) - Generate CSV files
- [Extension Configuration](pages/ExtensionConfiguration.md) - Configure the extension
- [Authentication](pages/Authentication.md) - Authentication requirements
