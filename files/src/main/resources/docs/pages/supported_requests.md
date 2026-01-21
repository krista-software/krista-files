# Supported Requests

## Catalog Requests

The Krista Files Extension supports the following catalog requests:

> **Note**: All requests include comprehensive error handling with detailed, actionable error messages. If you encounter any issues, refer to the [Troubleshooting Guide](troubleshooting.md) for solutions.

### Operations Area

#### **Create CSV File**

- Description: The extension supports the creation of a CSV file, for a given list of entities.
- Input Parameters:

| **Parameter Name** | **Parameter Type** | **Mandatory** | **Example**                                   |
|--------------------|--------------------|---------------|-----------------------------------------------|
| Entities           | List<Entity>       | Yes           | {Name: name1, Age: 12},{Name: name2, Age: 23} |


- **Output Parameters**:

| **Parameter Name** | **Parameter Type** |
|--------------------|--------------------|
| CSV                | File               |

**Common Errors:**
- "No data provided for processing" - Ensure the Entities parameter contains valid data
- "Invalid file name" - Check that file name contains only letters, numbers, and periods



#### **Read CSV File**
- Description: The extension supports parsing of the CSV file. It returns a list of entities.
- Input Parameters:

| **Parameter Name** | **Parameter Type** | **Mandatory** | **Description and Example**                                             |
|--------------------|--------------------|---------------|-------------------------------------------------------------------------|
| CSV                | File               | Yes           | Test.csv                                                                |
| Fields             | List<Text>         | Yes           | Specify the fields of entities that you want to ignore For example Name |

- **Output Parameters**:

| **Parameter Name** | **Parameter Type** |
|--------------------|--------------------|
| Response           | List<Entity>       |

**Common Errors:**
- "Invalid CSV file provided" - Ensure the file has .csv extension and valid CSV format
- "File not found or inaccessible" - Check that the file exists and is not corrupted



#### **Read CSV String**

- Description: The extension supports parsing of the CSV string. It returns a list of unknown entities.
- Input Parameters:

| **Parameter Name** | **Parameter Type** | **Mandatory** | **Description and Example**                                                   |
|--------------------|--------------------|---------------|-------------------------------------------------------------------------------|
| CSV                | Paragraph          | Yes           | CSV string For example: Test.csv                                              |
| Fields             | List<Text>         | No            | Specify the fields of the CSV string that you want to ignore For example Name |


- **Output Parameters**:

| **Parameter Name** | **Parameter Type** |
|--------------------|--------------------|
| Response           | List<Entity>       |



#### **Create SV String**
- Description: The extension supports generating a SV string, for a given list of unknown entities.

- Input Parameters :

| **Parameter Name** | **Parameter Type** | **Mandatory** | **Description and Example**                                                                 |
|--------------------|--------------------|---------------|---------------------------------------------------------------------------------------------|
| FieldType          | Pick One           | Yes           | CSV,TSV,PSV                                                                                 |
| Entities           | List<entity>       | Yes           | Specify the input of CSV string  For example: {Name: name1, Age: 12},{Name: name2, Age: 23} |


- **Output Parameters**:

| **Parameter Name** | **Parameter Type** |
|--------------------|--------------------|
| CSV                | Paragraph          |


#### **Get CSV as Table**

- Description: The extension supports generating tables, for a given CSV file.
-  Input Parameters :

| **Parameter Name** | **Parameter Type** | **Mandatory**   | **Description and Example**                                                |
|--------------------|--------------------|-----------------|----------------------------------------------------------------------------|
| CSV                | File               | Yes             | Test.csv                                                                   |

- **Output Parameters**:

| **Parameter Name** | **Parameter Type** |
|--------------------|--------------------|
| Response           | Entity             |

### Video Area

#### **Get Audio Encoders**

- Description: This request will return the list of audio encoders available.
- Input Parameters: NA

- **Output Parameters**:

| **Parameter Name** | **Parameter Type** |
|--------------------|--------------------|
| Encoders           | List&lt;Text>      |

#### **Get Video Encoders**

- Description: This request will return the list of video encoders available.
- Input Parameters: NA

- **Output Parameters**:

| **Parameter Name** | **Parameter Type** |
|--------------------|--------------------|
| Encoders           | List&lt;Text>      |

#### **MP4 To MP3**

- Description: This request will convert the MP4 format video file into MP3 format audio file.
-  Input Parameters :

| **Parameter Name** | **Parameter Type** | **Mandatory** | **Description and Example** |
|--------------------|--------------------|---------------|-----------------------------|
| Input              | File               | Yes           | VideoFile.mp4               |

- **Output Parameters**:

| **Parameter Name** | **Parameter Type** |
|--------------------|--------------------|
| Output             | File               |


#### **MP4 To WAV**

- Description: This request will convert the MP4 format video file into WAV format audio file.
-  Input Parameters :

| **Parameter Name** | **Parameter Type** | **Mandatory**   | **Description and Example** |
|--------------------|--------------------|-----------------|-----------------------------|
| Input              | File               | Yes             | VideoFile.mp4               |

- **Output Parameters**:

| **Parameter Name** | **Parameter Type** |
|--------------------|--------------------|
| Output             | File               |


#### **MP4 To WAV MONO**

- Description: This request will convert the MP4 format video file into WAV MONO format audio file.
-  Input Parameters :

| **Parameter Name** | **Parameter Type** | **Mandatory** | **Description and Example** |
|--------------------|--------------------|---------------|-----------------------------|
| Input              | File               | Yes           | VideoFile.mp4               |

- **Output Parameters**:

| **Parameter Name** | **Parameter Type** |
|--------------------|--------------------|
| Output             | File               |
