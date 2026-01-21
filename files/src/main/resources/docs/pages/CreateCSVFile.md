# Create CSV File

## Overview

Creates a CSV file from entity data, converting structured data into a downloadable comma-separated values file with automatic file naming and timestamp generation.

## Request Details

- **Area**: Operations
- **Type**: CHANGE_SYSTEM
- **Retry Support**: ❌ No (File creation operations are not retryable due to potential duplicate file generation)

## Input Parameters

| Parameter Name | Type | Required | Description | Example |
|----------------|------|----------|-------------|---------|
| Entities | [ Composite ] | Yes | List of entity objects to convert to CSV format | `[{"name": "John", "age": 30}, {"name": "Jane", "age": 25}]` |

### Parameter Details

#### Entities
- **Purpose**: Source data to be converted into CSV format
- **Structure**: Array of composite objects (entities)
- **Data Transformation**: Entity properties become CSV columns, values become CSV rows
- **Nested Objects**: Automatically flattened using dot notation (e.g., `address.street`)
- **Arrays**: Primitive arrays are preserved, complex arrays are flattened
- **Null Values**: Handled gracefully in CSV output

## Output Parameters

| Parameter Name | Type | Description |
|----------------|------|-------------|
| CSV | File | Generated CSV file with entity data and automatic naming |

### Output Details

#### CSV File
- **Format**: Standard comma-separated values (.csv)
- **Encoding**: UTF-8
- **Headers**: Automatically generated from entity property names
- **Naming Convention**: 
  - With prefix: `[File Name Prefix] at DD-MM-YYYY HH.MM.SS AM.csv`
  - Without prefix: `File generated at DD-MM-YYYY HH.MM.SS AM.csv`

## Validation Rules

| Validation | Error Message | Resolution |
|------------|---------------|------------|
| Entities not null/empty | "No data provided for processing. Please ensure you have valid data entries before attempting to create a file." | Provide a non-empty list of entities |
| Valid entity structure | "Failed to process entity data" | Ensure entities contain valid property-value pairs |
| File name prefix format | "Invalid file name provided. File names can only contain letters (A-Z, a-z), numbers (0-9), and periods (.)" | Configure valid file name prefix in extension settings |

## Error Handling

### Input Errors (INPUT_ERROR)
**Cause**: Invalid or missing input parameters
**Common Scenarios**:
- Empty or null entities list
- Invalid entity structure
- Malformed composite objects

**Resolution**:
1. Verify entities parameter contains valid data
2. Check that each entity has proper property-value structure
3. Ensure entities are not null or empty

### Logic Errors (LOGIC_ERROR)
**Cause**: Business logic validation failures
**Common Scenarios**:
- Data transformation failures
- Invalid file name prefix configuration
- Entity flattening issues

**Resolution**:
1. Review entity data structure for compatibility
2. Check file name prefix configuration
3. Simplify complex nested structures if needed

### System Errors (SYSTEM_ERROR)
**Cause**: System-level failures during file creation
**Common Scenarios**:
- Insufficient disk space
- File system permission issues
- Memory limitations with large datasets

**Resolution**:
1. Check available disk space
2. Verify file system permissions
3. Process smaller datasets if memory issues occur
4. Contact system administrator for persistent issues

## Usage Examples

### Example 1: Basic Entity Data
**Input**:
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
```

**Output**:
```csv
name,age,email
John Doe,30,john@example.com
Jane Smith,25,jane@example.com
```

**Result**: CSV file named "File generated at 15-03-2024 02.30.45 PM.csv"

### Example 2: Complex Nested Data
**Input**:
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
```

**Output**:
```csv
employee.name,employee.department,contact.email,contact.phone,skills
John Doe,Engineering,john@company.com,555-0123,"[""Java"",""Python"",""SQL""]"
```

### Example 3: With File Name Prefix
**Configuration**: File Name Prefix = "EmployeeReport"

**Output File Name**: "EmployeeReport at 15-03-2024 02.30.45 PM.csv"

## Business Rules

1. **Automatic File Naming**: Files are automatically named with timestamp to prevent conflicts
2. **Data Flattening**: Nested objects are flattened using dot notation for CSV compatibility
3. **Header Generation**: CSV headers are automatically generated from entity property names
4. **UTF-8 Encoding**: All CSV files use UTF-8 encoding for international character support
5. **Comma Escaping**: Values containing commas are automatically quoted
6. **Empty Value Handling**: Null or undefined values are represented as empty cells

## Limitations

1. **File Size**: Large datasets may impact performance; recommended maximum 50MB
2. **Memory Usage**: Very large entity lists may require significant memory
3. **Complex Arrays**: Nested arrays are serialized as JSON strings
4. **Special Characters**: File names cannot contain special characters or spaces
5. **Concurrent Operations**: Multiple simultaneous file creation operations may impact performance

## Best Practices

### 1. Data Preparation
- Ensure consistent entity structure across all objects
- Validate data types before processing
- Handle null values appropriately in source data

### 2. Performance Optimization
- Process large datasets in smaller batches
- Use simple, flat entity structures when possible
- Monitor memory usage with very large datasets

### 3. File Management
- Configure meaningful file name prefixes
- Implement file cleanup procedures for generated files
- Consider file storage location and access permissions

### 4. Error Prevention
- Validate entity data before calling the request
- Test with small datasets first
- Handle edge cases in entity structure

## Common Use Cases

### 1. Data Export
```
Scenario: Export user data from database to CSV for analysis
Action: Query database entities and pass to Create CSV File
Result: Downloadable CSV file with user information
```

### 2. Report Generation
```
Scenario: Generate monthly sales report in CSV format
Action: Aggregate sales data into entities and create CSV
Result: Formatted CSV report with timestamp
```

### 3. Data Migration
```
Scenario: Prepare data for import into external system
Action: Transform internal data format to CSV for external consumption
Result: Standardized CSV file ready for import
```

## Related Catalog Requests

- [Read CSV File](pages/ReadCSVFile.md) - Parse and read CSV files
- [Create SV String](pages/CreateSVString.md) - Generate separated value strings
- [Get CSV As Table](pages/GetCSVAsTable.md) - Convert CSV to table format

## Technical Implementation

### Helper Class
- **Class**: CatalogTypes
- **Package**: app.krista.extensions.essentials.documents.files.catalog
- **Validation**: Entity data validation and file name generation
- **Service**: SeparatedValueDaoImpl.getSV()

### Data Processing Flow
1. Validate input entities for null/empty conditions
2. Convert entities to flattened map structure
3. Generate file name with timestamp
4. Create CSV file using OpenCSV library
5. Return Krista File object

### Telemetry Metrics
- FILE_CREATION_SUCCESS: Successful CSV file creation
- FILE_CREATION_ERROR: Failed CSV file creation
- ENTITY_PROCESSING_TIME: Time taken to process entities

## Troubleshooting

### Large File Processing Issues
**Cause**: Memory or performance issues with large datasets
**Solution**: 
1. Process data in smaller batches
2. Increase JVM memory allocation
3. Optimize entity structure

### File Name Conflicts
**Cause**: Multiple files created simultaneously
**Solution**: Timestamp-based naming prevents conflicts automatically

### Encoding Issues
**Cause**: Special characters not displaying correctly
**Solution**: Ensure UTF-8 encoding is supported in target application

## See Also

- [Extension Configuration](pages/ExtensionConfiguration.md) - Configure file name prefix
- [Read CSV File](pages/ReadCSVFile.md) - Read generated CSV files
- [Authentication](pages/Authentication.md) - Authentication requirements
