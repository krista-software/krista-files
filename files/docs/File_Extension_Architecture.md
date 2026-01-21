# Krista Files Extension - Architecture Documentation

## Overview

The Krista Files Extension implements a comprehensive 5-layer architecture designed for file processing and video conversion operations. This document provides detailed analysis of the architectural layers, performance characteristics, error scenarios, and system dependencies.

## Architecture Layers

### 1. Extension Layer (Entry Point)
**Primary Components:**
- `FilesExtension.java` - Main extension class with @Extension annotation
- `FileAttributes.java` - Configuration management and validation

**Responsibilities:**
- Extension registration and lifecycle management
- Configuration validation and attribute management
- Custom UI tab registration
- Integration with Krista platform

**Key Annotations:**
- `@Extension(version = "2.0.5")` - Extension registration
- `@Java(version = Java.Version.JAVA_21)` - Java version requirement
- `@Field.Text(name = FileAttributes.FILE_NAME_PREFIX)` - Configuration field
- `@StaticResource(path = "docs", file = "docs")` - Documentation resources

### 2. Catalog Layer (API Definition)
**Primary Components:**
- `OperationsArea.java` - File processing catalog requests (5 operations)
- `VideoArea.java` - Video conversion catalog requests (5 operations)
- `CatalogTypes.java` - Common catalog operations and transformations

**Responsibilities:**
- API endpoint definition with @CatalogRequest annotations
- Request routing and parameter binding
- Input validation and error handling
- Business logic delegation to service layer

**Request Distribution:**
- **File Operations (5):** Create CSV, Read CSV, Get CSV As Table, Read CSV String, Create SV String
- **Video Operations (5):** MP4 to MP3/WAV/WAV MONO, Get Audio/Video Encoders

### 3. Service Layer (Business Logic)
**Primary Components:**
- `SeparatedValueDao.java` - Interface for CSV/TSV/PSV operations
- `SeparatedValueDaoImpl.java` - Implementation with OpenCSV integration
- `VideoConverterServiceImpl.java` - Video conversion with JAVE library

**Responsibilities:**
- Core business logic implementation
- Data transformation and processing
- Integration with external libraries
- Error handling and recovery

**External Library Integration:**
- **OpenCSV 5.8:** CSV processing with CSVReader/CSVWriter
- **JAVE 3.3.1:** Video conversion with Encoder class
- **Apache POI 5.2.3:** Spreadsheet processing
- **JSON Flattener 0.16.6:** Complex object flattening

### 4. Client Layer (File System Integration)
**Primary Components:**
- `KristaMediaClient.java` - Direct integration with Krista file system
- `KristaMediaClientUtil.java` - Utility wrapper with error handling

**Responsibilities:**
- File upload/download operations
- File format validation and security
- Automatic file compression for unsupported formats
- Integration with Krista FileRepository

**Security Features:**
- Blacklisted file extensions (html, php5, pht, phtml, shtml, asa, cer, asax, swf, xap, jsp, exe, js)
- Automatic ZIP compression for unsupported formats
- File validation and sanitization

### 5. Utility Layer (Support Components)
**Primary Components:**
- `Constants.java` - Configuration constants and GSON instance
- `SpreadsheetHelper.java` - Excel/CSV processing utilities
- `Errors.java` - Centralized error message constants

**Responsibilities:**
- Common utilities and helper functions
- Error message standardization
- Configuration management
- Data type conversions

## Architecture Diagrams

### System Architecture Overview

The Files Extension follows a strict 5-layer architecture with clear separation of concerns and dependency injection throughout. Each layer has specific responsibilities and communicates through well-defined interfaces.

```mermaid
graph TB
    subgraph "1. Extension Layer"
        FE[FilesExtension.java<br/>@Extension, @Java, @StaticResource]
        FA[FileAttributes.java<br/>Configuration Management]
    end

    subgraph "2. Catalog Layer"
        OA[OperationsArea.java<br/>5 File Operations]
        VA[VideoArea.java<br/>5 Video Operations]
        CT[CatalogTypes.java<br/>Common Operations]
    end

    subgraph "3. Service Layer"
        SVD[SeparatedValueDao<br/>Interface]
        SVDI[SeparatedValueDaoImpl<br/>CSV/TSV/PSV Processing]
        VCS[VideoConverterServiceImpl<br/>MP4 Conversion]
    end

    subgraph "4. Client Layer"
        KMC[KristaMediaClient<br/>File System Integration]
        KMCU[KristaMediaClientUtil<br/>Utility Wrapper]
    end

    subgraph "5. Utility Layer"
        CONST[Constants.java<br/>Configuration]
        SH[SpreadsheetHelper.java<br/>Processing Utilities]
        ERR[Errors.java<br/>Error Messages]
    end

    subgraph "External Dependencies"
        OPENCSV[OpenCSV 5.8<br/>CSV Processing]
        JAVE[JAVE 3.3.1<br/>Video Conversion]
        POI[Apache POI 5.2.3<br/>Spreadsheet Support]
        JSONF[JSON Flattener 0.16.6<br/>Data Transformation]
        KRISTA[Krista SDK<br/>File Repository]
    end

    %% Layer connections
    FE --> OA
    FE --> VA
    FA --> OA
    FA --> VA

    OA --> CT
    VA --> CT
    OA --> SVDI
    VA --> VCS

    SVDI --> SVD
    SVDI --> KMC
    VCS --> KMCU

    KMC --> KRISTA
    KMCU --> KMC

    SVDI --> SH
    SVDI --> CONST
    SVDI --> ERR
    VCS --> ERR
    CT --> ERR

    %% External library connections
    SVDI --> OPENCSV
    SVDI --> POI
    SVDI --> JSONF
    VCS --> JAVE

    %% Styling
    classDef extensionLayer fill:#e1f5fe
    classDef catalogLayer fill:#f3e5f5
    classDef serviceLayer fill:#e8f5e8
    classDef clientLayer fill:#fff3e0
    classDef utilityLayer fill:#fce4ec
    classDef external fill:#f5f5f5

    class FE,FA extensionLayer
    class OA,VA,CT catalogLayer
    class SVD,SVDI,VCS serviceLayer
    class KMC,KMCU clientLayer
    class CONST,SH,ERR utilityLayer
    class OPENCSV,JAVE,POI,JSONF,KRISTA external
```

### Data Flow Architecture

```mermaid
sequenceDiagram
    participant User
    participant KristaPlatform as Krista Platform
    participant Extension as FilesExtension
    participant Catalog as Catalog Layer
    participant Service as Service Layer
    participant Client as Client Layer
    participant FileRepo as File Repository
    participant ExtLib as External Libraries

    User->>KristaPlatform: Request File Operation
    KristaPlatform->>Extension: Route Request
    Extension->>Catalog: Validate & Process

    alt File Operations
        Catalog->>Service: SeparatedValueDaoImpl
        Service->>ExtLib: OpenCSV/Apache POI
        Service->>Client: File Processing
        Client->>FileRepo: Upload/Download
    else Video Operations
        Catalog->>Service: VideoConverterServiceImpl
        Service->>ExtLib: JAVE Library
        Service->>Client: File Conversion
        Client->>FileRepo: Upload/Download
    end

    FileRepo-->>Client: File Handle
    Client-->>Service: Processed File
    Service-->>Catalog: Result/Error
    Catalog-->>Extension: Response
    Extension-->>KristaPlatform: Final Result
    KristaPlatform-->>User: Response
```

### Component Dependencies

```mermaid
graph LR
    subgraph "Dependency Injection (HK2)"
        DI[HK2 Container<br/>@Service, @Inject]
    end

    subgraph "Core Components"
        FE[FilesExtension<br/>@Extension]
        FA[FileAttributes<br/>@Service]
        OA[OperationsArea<br/>@Domain]
        VA[VideoArea<br/>@Domain]
        CT[CatalogTypes<br/>@Service]
    end

    subgraph "Service Implementations"
        SVDI[SeparatedValueDaoImpl<br/>@Service]
        VCS[VideoConverterServiceImpl<br/>@Service]
        KMC[KristaMediaClient<br/>@Service]
        KMCU[KristaMediaClientUtil<br/>@Service]
    end

    subgraph "External Dependencies"
        FR[FileRepository<br/>Krista SDK]
        GSON[GSON<br/>JSON Processing]
        OPENCSV[OpenCSV<br/>CSV Processing]
        JAVE[JAVE<br/>Video Conversion]
        POI[Apache POI<br/>Spreadsheet]
        JF[JSON Flattener<br/>Data Transform]
    end

    %% Dependency injection relationships
    DI -.-> FE
    DI -.-> FA
    DI -.-> OA
    DI -.-> VA
    DI -.-> CT
    DI -.-> SVDI
    DI -.-> VCS
    DI -.-> KMC
    DI -.-> KMCU

    %% Component dependencies
    FE --> FA
    OA --> SVDI
    OA --> CT
    VA --> VCS
    CT --> KMC
    SVDI --> KMC
    VCS --> KMCU
    KMCU --> KMC

    %% External library dependencies
    KMC --> FR
    SVDI --> OPENCSV
    SVDI --> POI
    SVDI --> JF
    VCS --> JAVE
    CT --> GSON

    %% Styling
    classDef core fill:#e3f2fd
    classDef service fill:#e8f5e8
    classDef external fill:#fff3e0
    classDef di fill:#f3e5f5

    class FE,FA,OA,VA,CT core
    class SVDI,VCS,KMC,KMCU service
    class FR,GSON,OPENCSV,JAVE,POI,JF external
    class DI di
```

## Dependency Injection Pattern

The extension uses HK2 (Habitat for Jersey 2) dependency injection framework:

**Key Annotations:**
- `@Service` - Marks classes as injectable services
- `@Inject` - Injection points for dependencies
- `@Extension` - Extension registration with Krista platform
- `@Domain` - Catalog area registration

**Injection Hierarchy:**
```
FilesExtension (Entry Point)
├── FileAttributes (@Inject)
├── OperationsArea (@Inject SeparatedValueDaoImpl, CatalogTypes)
├── VideoArea (@Inject VideoConverterServiceImpl)
├── SeparatedValueDaoImpl (@Service)
├── VideoConverterServiceImpl (@Service, @Inject KristaMediaClientUtil)
├── KristaMediaClient (@Service, @Inject FileRepository)
└── KristaMediaClientUtil (@Service, @Inject KristaMediaClient)
```

## Request Processing Flow

### File Operations Flow
1. **Request Reception:** Krista platform routes request to OperationsArea
2. **Validation:** Input parameters validated against @Field annotations
3. **Service Delegation:** Request delegated to SeparatedValueDaoImpl
4. **Processing:** OpenCSV/Apache POI libraries process data
5. **File Management:** KristaMediaClient handles file upload/download
6. **Response:** Processed data returned as FreeForm entities or File objects

### Video Operations Flow
1. **Request Reception:** Krista platform routes request to VideoArea
2. **Validation:** MP4 file format validation
3. **Service Delegation:** Request delegated to VideoConverterServiceImpl
4. **Conversion:** JAVE library performs video-to-audio conversion
5. **File Management:** Temporary files managed in /tmp directory
6. **Response:** Converted audio file returned

## Performance Characteristics

### Memory Usage Patterns
- **Base Requirements:** 256MB minimum for extension operation
- **CSV Processing:** +50MB per 10,000 rows processed
- **Video Conversion:** +500MB per concurrent conversion operation
- **Large File Processing:** Memory usage scales linearly with file size
- **Field Filtering:** Reduces memory usage by excluding unnecessary data

### Processing Speed Benchmarks
- **CSV Creation:** ~1,000 rows per second (varies by entity complexity)
- **CSV Reading:** ~2,000 rows per second (with field filtering optimization)
- **Video Conversion:** Real-time to 2x speed (depends on file size and codec)
- **String Operations:** ~5,000 rows per second for CSV string processing
- **Table Conversion:** ~1,500 rows per second for display optimization

### Concurrency Characteristics
- **Thread Safety:** All services designed as thread-safe
- **Video Conversion:** Recommended 1-2 concurrent conversions per instance
- **CSV Operations:** Can handle higher concurrency levels (5-10 concurrent)
- **Memory Contention:** Multiple large operations may cause memory issues
- **Resource Management:** Automatic cleanup of temporary files and resources

## Performance Limitations

### File Size Constraints
- **CSV Files:** Maximum 50MB for optimal performance (100K+ rows may cause issues)
- **MP4 Files:** Maximum 500MB to avoid timeout issues (2GB+ may fail)
- **String Operations:** Maximum 10MB string size for optimal performance
- **Temporary Space:** Ensure 2x largest file size available in /tmp directory

### System Dependencies
- **Codec Libraries:** Requires system codec libraries for video conversion
- **Temporary Storage:** /tmp directory must be available with sufficient space
- **Network Connectivity:** Required for Krista file system integration
- **Java Version:** Java 21+ required for optimal performance

### Known Limitations
- **Video Input:** Only MP4 format supported for conversion operations
- **CSV Requirements:** Must be properly formatted with consistent delimiters
- **File Names:** Limited to alphanumeric characters and periods only
- **Extension Validation:** Strict file extension checking for security

## Error Scenarios and Handling

### Error Categories

#### 1. Input Validation Errors
**File Format Errors:**
- Invalid CSV file format or extension
- Invalid MP4 file format or corrupted video
- Unsupported file types (automatically zipped)

**Data Validation Errors:**
- Empty or null entity collections
- Invalid file name prefix (non-alphanumeric characters)
- Missing required parameters

#### 2. Processing Errors
**CSV Processing:**
- Malformed CSV structure or delimiter inconsistency
- Encoding issues (non-UTF-8)
- Memory exhaustion with large files

**Video Conversion:**
- Missing audio track in MP4 file
- Codec compatibility issues
- System resource limitations (CPU, memory, disk space)

#### 3. System Errors
**File System:**
- Insufficient disk space in /tmp directory
- File permission issues
- Network connectivity problems

**External Dependencies:**
- Missing codec libraries
- OpenCSV parsing failures
- JAVE conversion engine errors

### Error Handling Strategy

#### Centralized Error Messages
All error messages defined in `Errors.java` constants:
- User-friendly, actionable error descriptions
- Consistent error formatting across all operations
- Detailed troubleshooting guidance included

#### Error Recovery Mechanisms
- **Automatic Retry:** Built-in retry logic for transient failures
- **Graceful Degradation:** Partial processing when possible
- **Resource Cleanup:** Automatic cleanup of temporary files
- **Logging:** Comprehensive error logging with SLF4J

#### Error Response Format
```java
// Example error handling pattern
try {
    return processFile(file);
} catch (Exception cause) {
    logger.error("Processing failed: {}", cause.getMessage());
    throw new IllegalArgumentException(Errors.PROCESSING_ERROR, cause);
}
```

## Security Architecture

### File Security Measures
**Blacklisted Extensions:**
- Executable files: exe, js
- Web files: html, php5, pht, phtml, shtml, asa
- Certificate files: cer, asax
- Plugin files: swf, xap, jsp

**Security Processing:**
- Automatic ZIP compression for blacklisted file types
- File extension validation before processing
- Content-type verification for uploaded files
- Secure temporary file handling with automatic cleanup

### Access Control
**Authentication:**
- Integration with Krista platform authentication
- No external OAuth or API keys required
- Session management handled by Krista platform

**Authorization:**
- File access controlled by Krista permissions
- User-level access control for file operations
- Audit trail for all file operations

## Scalability Considerations

### Horizontal Scaling
- **Stateless Design:** All services are stateless and can be scaled horizontally
- **Load Distribution:** Multiple extension instances can handle concurrent requests
- **Resource Isolation:** Each request processed independently

### Vertical Scaling
- **Memory Scaling:** Increase JVM heap size for larger file processing
- **CPU Scaling:** Video conversion benefits from additional CPU cores
- **Storage Scaling:** Temporary storage requirements scale with file sizes

### Performance Optimization Strategies
- **Field Filtering:** Reduce memory usage by excluding unnecessary fields
- **Batch Processing:** Process large datasets in smaller chunks
- **Streaming:** Use streaming for very large file processing
- **Caching:** Cache frequently accessed configuration and metadata

## Deployment Architecture

### Extension Packaging
- **JAR Distribution:** Extension packaged as single JAR file
- **Dependency Bundling:** All external libraries included
- **Resource Inclusion:** Documentation and UI resources embedded
- **Version Management:** Semantic versioning with Gradle automation

### Runtime Environment
- **Java Requirements:** Java 21+ runtime environment
- **Memory Allocation:** 512MB minimum, 2GB recommended
- **Temporary Storage:** Writable /tmp directory with sufficient space
- **Network Access:** Connectivity to Krista file repository

### Configuration Management
- **Extension Configuration:** File name prefix (optional, alphanumeric + periods)
- **System Properties:** Temporary directory location configurable
- **Environment Variables:** Codec library paths and system settings
- **Runtime Parameters:** JVM heap size and garbage collection settings

## Monitoring and Observability

### Logging Strategy
- **SLF4J Framework:** Configurable logging levels
- **Structured Logging:** Consistent log format across all components
- **Error Tracking:** Detailed error logs with stack traces
- **Performance Metrics:** Execution time and resource usage logging

### Key Metrics to Monitor
- **Request Volume:** Number of catalog requests per time period
- **Processing Time:** Average and percentile response times
- **Error Rates:** Error frequency by operation type
- **Resource Usage:** Memory, CPU, and disk space utilization
- **File Sizes:** Distribution of processed file sizes

### Health Checks
- **Extension Status:** Extension registration and availability
- **Dependency Health:** External library and service availability
- **Resource Availability:** Disk space and memory availability
- **Configuration Validation:** Extension configuration integrity

## Maintenance and Operations

### Regular Maintenance Tasks
- **Temporary File Cleanup:** Regular cleanup of /tmp directory
- **Log Rotation:** Manage log file sizes and retention
- **Dependency Updates:** Keep external libraries updated
- **Performance Monitoring:** Regular performance baseline reviews

### Troubleshooting Procedures
1. **Check System Resources:** Verify memory, disk space, and CPU availability
2. **Validate Configuration:** Ensure extension configuration is correct
3. **Review Logs:** Examine error logs for specific failure patterns
4. **Test Dependencies:** Verify external library functionality
5. **Isolate Issues:** Test with smaller files to identify bottlenecks

### Backup and Recovery
- **Configuration Backup:** Extension configuration and settings
- **Documentation Backup:** Static resources and documentation
- **Dependency Management:** Version control for external libraries
- **Rollback Procedures:** Safe rollback to previous extension versions

## Future Architecture Considerations

### Potential Enhancements
- **Additional File Formats:** Support for Excel, JSON, XML processing
- **Advanced Video Formats:** Support for additional video input formats
- **Streaming Processing:** Large file streaming for memory efficiency
- **Caching Layer:** Redis or similar for frequently accessed data

### Scalability Improvements
- **Microservice Architecture:** Split into specialized microservices
- **Event-Driven Processing:** Asynchronous processing for large operations
- **Distributed Processing:** Cluster-based processing for very large files
- **API Gateway:** Centralized API management and rate limiting

This architecture provides a robust, scalable foundation for file processing and video conversion operations within the Krista platform, with clear separation of concerns, comprehensive error handling, and strong security measures.
