# Create SV String

## Overview

Generates separated value strings from entity data, supporting multiple separator formats including CSV (comma), TSV (tab), and PSV (pipe) with automatic header generation and data formatting.

## Request Details

- **Area**: Operations
- **Type**: CHANGE_SYSTEM
- **Retry Support**: ✅ Yes (String generation operations can be safely retried without side effects)

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| Entities | [ Composite ] | Yes | List of entity objects to convert to separated value format | `[{"name": "John", "age": 30}, {"name": "Jane", "age": 25}]` |
| separator | Text | Yes | Character to use as field separator | `","` (CSV), `"\t"` (TSV), `"|"` (PSV) |

### Parameter Details

#### Entities
- **Purpose**: Source data to be converted into separated value format
- **Structure**: Array of composite objects (entities)
- **Data Transformation**: Entity properties become columns, values become rows
- **Nested Objects**: Automatically flattened using dot notation (e.g., `address.street`)
- **Arrays**: Primitive arrays are preserved, complex arrays are flattened
- **Null Values**: Handled gracefully in output string

#### separator
- **Purpose**: Defines the character used to separate fields in the output
- **Common Values**:
  - `","` - Comma-separated values (CSV)
  - `"\t"` - Tab-separated values (TSV)
  - `"|"` - Pipe-separated values (PSV)
  - `";"` - Semicolon-separated values
- **Custom Separators**: Any single character can be used as separator
- **Escaping**: Values containing the separator are automatically quoted

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| Response | Text | Generated separated value string with headers and data rows |

### Output Details

#### Response String
- **Format**: Multi-line string with header row followed by data rows
- **Line Endings**: Uses system default line endings (\n or \r\n)
- **Headers**: Automatically generated from entity property names
- **Quoting**: Values containing separators or special characters are quoted
- **Encoding**: UTF-8 text string

## Validation Rules

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| Entities not null/empty | "No data provided for processing. Please ensure you have valid data entries before attempting to create a separated value string." | Provide a non-empty list of entities |
| Separator not null/empty | "Separator character is required. Please specify a valid separator (e.g., comma, tab, pipe)." | Provide a valid separator character |
| Valid entity structure | "Failed to process entity data for separated value generation." | Ensure entities contain valid property-value pairs |

## Error Handling

### Input Errors (INPUT_ERROR)
**Cause**: Invalid or missing input parameters
**Common Scenarios**:
- Empty or null entities list
- Null or empty separator
- Invalid entity structure

**Resolution**:
1. Verify entities parameter contains valid data
2. Ensure separator is not null or empty
3. Check that each entity has proper property-value structure

### Logic Errors (LOGIC_ERROR)
**Cause**: Data processing or formatting failures
**Common Scenarios**:
- Entity flattening issues
- Data type conversion problems
- Special character handling issues

**Resolution**:
1. Review entity data structure for compatibility
2. Simplify complex nested structures if needed
3. Check for special characters in data values

### System Errors (SYSTEM_ERROR)
**Cause**: System-level failures during string generation
**Common Scenarios**:
- Memory limitations with large datasets
- String processing timeout
- Internal formatting errors

**Resolution**:
1. Process smaller datasets if memory issues occur
2. Check system resources
3. Retry the operation
4. Contact support for persistent issues

## Usage Examples

### Example 1: Basic CSV String Generation
**Input Parameters**:
```
Entities: [
  {
    "name": "John Doe",
    "age": 30,
    "email": "john@example.com"
  },
  {
    "name": "Jane Smith",
    "age": 25, 
    "email": "jane@example.com"
  }
]
separator: ","
```

**Output**:
```
name,age,email
John Doe,30,john@example.com
Jane Smith,25,jane@example.com
```

### Example 2: Tab-Separated Values (TSV)
**Input Parameters**:
```
Entities: [
  {
    "product": "Widget A",
    "price": 29.99,
    "category": "Electronics"
  },
  {
    "product": "Widget B",
    "price": 19.99,
    "category": "Electronics"
  }
]
separator: "\t"
```

**Output**:
```
product	price	category
Widget A	29.99	Electronics
Widget B	19.99	Electronics
```

### Example 3: Pipe-Separated Values (PSV)
**Input Parameters**:
```
Entities: [
  {
    "id": 1,
    "description": "High-quality widget, best in class",
    "status": "active"
  },
  {
    "id": 2,
    "description": "Budget option",
    "status": "inactive"
  }
]
separator: "|"
```

**Output**:
```
id|description|status
1|"High-quality widget, best in class"|active
2|Budget option|inactive
```

### Example 4: Complex Nested Data
**Input Parameters**:
```
Entities: [
  {
    "employee": {
      "name": "John Doe",
      "department": "Engineering"
    },
    "contact": {
      "email": "john@company.com",
      "phone": "555-0123"
    },
    "skills": ["Java", "Python", "SQL"]
  }
]
separator: ","
```

**Output**:
```
employee.name,employee.department,contact.email,contact.phone,skills
John Doe,Engineering,john@company.com,555-0123,"[""Java"",""Python"",""SQL""]"
```

## Business Rules

1. **Automatic Header Generation**: Headers are automatically generated from entity property names
2. **Data Flattening**: Nested objects are flattened using dot notation for compatibility
3. **Quote Handling**: Values containing separators are automatically quoted
4. **Array Serialization**: Arrays are serialized as JSON strings when necessary
5. **Empty Value Handling**: Null or undefined values are represented as empty fields
6. **Character Escaping**: Special characters are properly escaped in output

## Limitations

1. **Memory Usage**: Large entity lists may require significant memory
2. **String Size**: Very large datasets may produce extremely long strings
3. **Complex Arrays**: Nested arrays are serialized as JSON strings
4. **Separator Conflicts**: Values containing the separator character are quoted
5. **Line Ending Consistency**: Uses system default line endings

## Best Practices

### 1. Data Preparation
- Ensure consistent entity structure across all objects
- Validate data types before processing
- Handle null values appropriately in source data
- Consider flattening complex nested structures manually for better control

### 2. Separator Selection
- Use comma (`,`) for standard CSV format
- Use tab (`\t`) for TSV when data contains commas
- Use pipe (`|`) for PSV when data contains commas and tabs
- Avoid separators that commonly appear in your data

### 3. Performance Optimization
- Process large datasets in smaller batches
- Use simple, flat entity structures when possible
- Monitor memory usage with very large datasets

### 4. Output Handling
- Consider line ending requirements for target systems
- Validate output format before using in external systems
- Handle special characters appropriately for target applications

## Common Use Cases

### 1. Data Export
```
Scenario: Export database records to separated value format
Action: Convert entity data to CSV/TSV/PSV string for download
Result: Formatted string ready for file creation or transmission
```

### 2. API Response Formatting
```
Scenario: Return data in separated value format through API
Action: Generate SV string from entity data for API response
Result: Structured text data for API consumers
```

### 3. Report Generation
```
Scenario: Create text-based reports in separated value format
Action: Convert report data to SV string with appropriate separator
Result: Formatted report data ready for display or processing
```

### 4. Data Integration
```
Scenario: Prepare data for import into external systems
Action: Generate SV string with target system's required separator
Result: Compatible data format for external system import
```

## Related Catalog Requests

- [Create CSV File](pages/CreateCSVFile.md) - Generate CSV files from entity data
- [Read CSV String](pages/ReadCSVString.md) - Parse CSV data from string input
- [Read CSV File](pages/ReadCSVFile.md) - Parse CSV files into entities

## Technical Implementation

### Helper Class
- **Class**: SeparatedValueDaoImpl
- **Package**: app.krista.extensions.essentials.documents.files.impl
- **Validation**: Entity data and separator validation
- **Service**: getSV() method with OpenCSV StringWriter

### Processing Flow
1. Validate entities parameter is not null or empty
2. Validate separator parameter is not null or empty
3. Flatten entity data and extract headers
4. Generate separated value string using OpenCSV
5. Apply proper quoting and escaping
6. Return formatted string

### String Generation
```java
// Internal string generation
StringWriter stringWriter = new StringWriter();
CSVWriter csvWriter = new CSVWriter(stringWriter, separatorChar);
csvWriter.writeNext(headers);
for (String[] row : dataRows) {
    csvWriter.writeNext(row);
}
return stringWriter.toString();
```

### Telemetry Metrics
- SV_STRING_GENERATION_SUCCESS: Successful string generation
- SV_STRING_GENERATION_ERROR: Failed generation
- ENTITY_COUNT: Number of entities processed
- OUTPUT_STRING_SIZE: Size of generated string

## Troubleshooting

### Large String Generation Issues
**Cause**: Memory or performance issues with large datasets
**Solution**:
1. Process data in smaller batches
2. Increase JVM memory allocation
3. Optimize entity structure to reduce complexity

### Separator Character Issues
**Cause**: Data values contain the separator character
**Solution**:
1. Choose a different separator character
2. Verify automatic quoting is working correctly
3. Pre-process data to handle separator conflicts

### Encoding Issues
**Cause**: Special characters not displaying correctly
**Solution**:
1. Ensure UTF-8 encoding is used throughout the process
2. Check for invalid characters in entity data
3. Validate output string encoding

### Nested Data Flattening Issues
**Cause**: Complex nested structures causing formatting problems
**Solution**:
1. Simplify entity structure before processing
2. Manually flatten complex objects
3. Review dot notation field names in output

## See Also

- [Create CSV File](pages/CreateCSVFile.md) - Generate CSV files
- [Read CSV String](pages/ReadCSVString.md) - Parse CSV strings
- [Extension Configuration](pages/ExtensionConfiguration.md) - Configure the extension
- [Authentication](pages/Authentication.md) - Authentication requirements
