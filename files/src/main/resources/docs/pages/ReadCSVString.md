# Read CSV String

## Overview

Parses CSV data from a string input, converting comma-separated text directly into structured entity data without requiring file upload.

## Request Details

- **Area**: Operations
- **Type**: CHANGE_SYSTEM
- **Retry Support**: ✅ Yes (String parsing operations can be safely retried without side effects)

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| csvString | Text | Yes | CSV data as a string with headers and rows | `"name,age\nJohn,30\nJane,25"` |
| fields | [ Text ] | No | List of field names to exclude from output | `["password", "internal_id"]` |

### Parameter Details

#### csvString
- **Purpose**: CSV data in string format to be parsed into entities
- **Format**: Standard CSV format with newline-separated rows
- **Requirements**: Must include header row with column names
- **Line Endings**: Supports both `\n` and `\r\n` line endings
- **Encoding**: UTF-8 text string
- **Size Considerations**: Large strings may impact performance

#### fields (Optional)
- **Purpose**: Specify columns to exclude from the output entities
- **Behavior**: Listed field names will be filtered out of the result
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
| CSV string not null/empty | "No CSV data provided for processing. Please provide valid CSV content as a string." | Provide a non-empty CSV string |
| Valid CSV format | "Unable to parse the CSV string. Please ensure the CSV data is properly formatted with headers and comma-separated values." | Check CSV string format and structure |
| Header row present | "CSV string must contain a header row with column names." | Ensure first row contains column headers |

## Error Handling

### Input Errors (INPUT_ERROR)
**Cause**: Invalid or missing input parameters
**Common Scenarios**:
- Null or empty csvString parameter
- Invalid CSV string format
- Missing header row

**Resolution**:
1. Verify csvString parameter is not null or empty
2. Check CSV string has proper header row
3. Ensure CSV format is valid with comma separation

### Logic Errors (LOGIC_ERROR)
**Cause**: CSV parsing or processing failures
**Common Scenarios**:
- Malformed CSV structure in string
- Invalid characters or encoding issues
- Inconsistent column counts across rows

**Resolution**:
1. Verify CSV string follows standard format
2. Check for proper comma separation and quoting
3. Ensure consistent number of columns across all rows
4. Validate header row format

### System Errors (SYSTEM_ERROR)
**Cause**: System-level failures during string processing
**Common Scenarios**:
- Memory issues with very large strings
- String processing timeout
- Internal parsing errors

**Resolution**:
1. Try with smaller CSV strings
2. Check system resources
3. Retry the operation
4. Contact support for persistent issues

## Usage Examples

### Example 1: Basic CSV String Parsing
**Input Parameters**:
```
csvString: "name,age,department\nJohn Doe,30,Engineering\nJane Smith,25,Marketing\nBob Johnson,35,Sales"
fields: (empty)
```

**Output**:
```json
[
  {
    "name": "John Doe",
    "age": "30",
    "department": "Engineering"
  },
  {
    "name": "Jane Smith", 
    "age": "25",
    "department": "Marketing"
  },
  {
    "name": "Bob Johnson",
    "age": "35", 
    "department": "Sales"
  }
]
```

### Example 2: CSV String with Field Filtering
**Input Parameters**:
```
csvString: "username,email,password,role\njdoe,john@example.com,secret123,admin\njsmith,jane@example.com,pass456,user"
fields: ["password"]
```

**Output**:
```json
[
  {
    "username": "jdoe",
    "email": "john@example.com",
    "role": "admin"
  },
  {
    "username": "jsmith",
    "email": "jane@example.com", 
    "role": "user"
  }
]
```

### Example 3: CSV String with Quoted Values
**Input Parameters**:
```
csvString: "id,name,description,price\n1,\"Widget A\",\"High-quality widget, best in class\",29.99\n2,\"Widget B\",\"Budget option\",19.99"
fields: (empty)
```

**Output**:
```json
[
  {
    "id": "1",
    "name": "Widget A",
    "description": "High-quality widget, best in class",
    "price": "29.99"
  },
  {
    "id": "2",
    "name": "Widget B",
    "description": "Budget option",
    "price": "19.99"
  }
]
```

### Example 4: Multi-line CSV String
**Input Parameters**:
```
csvString: "product,category,in_stock\nLaptop,Electronics,true\nDesk,Furniture,false\nPhone,Electronics,true"
fields: (empty)
```

**Output**:
```json
[
  {
    "product": "Laptop",
    "category": "Electronics", 
    "in_stock": "true"
  },
  {
    "product": "Desk",
    "category": "Furniture",
    "in_stock": "false"
  },
  {
    "product": "Phone",
    "category": "Electronics",
    "in_stock": "true"
  }
]
```

## Business Rules

1. **Header Requirement**: CSV string must start with a header row containing column names
2. **Data Type Handling**: All CSV values are treated as text/string in output entities
3. **Field Filtering**: Specified fields are completely excluded from output
4. **Case Insensitive Filtering**: Field name matching for filtering is case-insensitive
5. **Empty Cell Handling**: Empty CSV cells become empty string properties
6. **Quote Processing**: Quoted values in CSV are properly unquoted in output
7. **Line Ending Support**: Handles both Unix (\n) and Windows (\r\n) line endings

## Limitations

1. **String Size**: Very large CSV strings may impact performance and memory usage
2. **Memory Usage**: Entire CSV string is processed in memory
3. **Data Types**: All output values are strings; no automatic type conversion
4. **Encoding**: String must be properly encoded UTF-8 text
5. **Complex Structures**: Cannot handle nested CSV structures or multiple tables

## Best Practices

### 1. String Preparation
- Ensure CSV string has proper header row
- Use standard CSV format with comma separation
- Quote values containing commas or special characters
- Use consistent line endings throughout the string

### 2. Performance Optimization
- Keep CSV strings reasonably sized for performance
- Use field filtering to reduce output size
- Process large datasets in smaller chunks

### 3. Data Security
- Use field filtering to exclude sensitive columns
- Validate CSV string contents before processing
- Sanitize input data to prevent injection issues

### 4. Error Prevention
- Test CSV string format with small samples first
- Validate header row format and column names
- Handle special characters and encoding properly

## Common Use Cases

### 1. API Data Processing
```
Scenario: Process CSV data received through API calls
Action: Parse CSV string from API response into entities
Result: Structured data ready for further processing
```

### 2. Dynamic Data Import
```
Scenario: Import CSV data pasted by users into text fields
Action: Convert user-provided CSV string to entity format
Result: Imported data available for system processing
```

### 3. Configuration Data Processing
```
Scenario: Process CSV configuration data stored as strings
Action: Parse CSV configuration into structured entities
Result: Configuration data ready for application use
```

### 4. Data Transformation
```
Scenario: Transform CSV data between different system formats
Action: Parse CSV string and convert to target entity structure
Result: Data transformed for target system consumption
```

## Related Catalog Requests

- [Read CSV File](pages/ReadCSVFile.md) - Parse CSV files into entities
- [Create SV String](pages/CreateSVString.md) - Generate separated value strings
- [Get CSV As Table](pages/GetCSVAsTable.md) - Convert CSV to table format

## Technical Implementation

### Helper Class
- **Class**: SeparatedValueDaoImpl
- **Package**: app.krista.extensions.essentials.documents.files.impl
- **Validation**: String format and content validation
- **Service**: readCSVString() method with OpenCSV

### Processing Flow
1. Validate csvString parameter is not null or empty
2. Parse CSV string using OpenCSV StringReader
3. Extract header row and validate format
4. Process data rows and convert to entities
5. Apply field filtering if specified
6. Return entity list

### String Processing
```java
// Internal string processing
StringReader stringReader = new StringReader(csvString);
CSVReader csvReader = new CSVReader(stringReader);
List<String[]> allRows = csvReader.readAll();
```

### Telemetry Metrics
- CSV_STRING_PARSE_SUCCESS: Successful CSV string parsing
- CSV_STRING_PARSE_ERROR: Failed parsing
- FIELD_FILTER_APPLIED: Field filtering was used
- STRING_SIZE: Size of processed CSV string

## Troubleshooting

### CSV String Parsing Errors
**Cause**: Malformed CSV format in string
**Solution**:
1. Check CSV string format manually
2. Verify proper comma separation
3. Ensure header row is present and properly formatted
4. Check for invalid characters or encoding issues

### Field Filtering Not Working
**Cause**: Field names don't match CSV headers
**Solution**:
1. Check exact spelling of field names
2. Verify case sensitivity (filtering is case-insensitive)
3. Ensure field names match CSV column headers exactly

### Memory Issues with Large Strings
**Cause**: CSV string too large for available memory
**Solution**:
1. Process smaller CSV strings
2. Use field filtering to reduce memory usage
3. Split large CSV data into smaller chunks

### Line Ending Issues
**Cause**: Inconsistent or invalid line endings in CSV string
**Solution**:
1. Normalize line endings to \n or \r\n
2. Check for mixed line ending formats
3. Ensure consistent formatting throughout string

## See Also

- [Read CSV File](pages/ReadCSVFile.md) - Parse CSV files
- [Create SV String](pages/CreateSVString.md) - Generate separated value strings
- [Extension Configuration](pages/ExtensionConfiguration.md) - Configure the extension
- [Authentication](pages/Authentication.md) - Authentication requirements
