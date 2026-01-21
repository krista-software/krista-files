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

package app.krista.extensions.essentials.documents.files.catalog;

import app.krista.extension.impl.anno.Attribute;
import app.krista.extension.impl.anno.CatalogRequest;
import app.krista.extension.impl.anno.Domain;
import app.krista.extension.impl.anno.Field;
import app.krista.extensions.essentials.documents.files.FileAttributes;
import app.krista.extensions.essentials.documents.files.constants.Errors;
import app.krista.extensions.essentials.documents.files.impl.SeparatedValueDaoImpl;
import app.krista.model.base.File;
import app.krista.model.base.FreeForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Operations Area catalog class for file processing operations.
 *
 * <p>This class provides catalog requests for file operations within the Files extension,
 * specifically focusing on CSV and separated value file processing. It handles creation,
 * reading, and manipulation of CSV, TSV, and PSV files.</p>
 *
 * <h3>Available Operations:</h3>
 * <ul>
 *   <li><strong>Create CSV File:</strong> Generate CSV files from entity data</li>
 *   <li><strong>Read CSV File:</strong> Parse CSV files into entity collections</li>
 *   <li><strong>Get CSV As Table:</strong> Convert CSV data to table format</li>
 *   <li><strong>Read CSV String:</strong> Parse CSV data from string input</li>
 *   <li><strong>Create SV String:</strong> Generate separated value strings (CSV/TSV/PSV)</li>
 * </ul>
 *
 * <p>All operations include comprehensive error handling and validation to ensure
 * data integrity and provide meaningful error messages to users.</p>
 *
 * @author Krista Extensions Team
 * @version 2.0.5
 * @since 1.0.0
 */
@Domain(id = "catEntryDomain_49c18799-c16b-4d33-98ac-f048f446a152",
        name = "Documents",
        ecosystemId = "catEntryEcosystem_84b53163-327b-4b1b-8c96-9334d292f9f5",
        ecosystemName = "Essentials",
        ecosystemVersion = "62a0d1ce-bc00-402c-a740-16103817e96a")
public class OperationsArea {
    private static final Logger logger = LoggerFactory.getLogger(OperationsArea.class);
    private final SeparatedValueDaoImpl separatedValueDao;
    private final FileAttributes attributes;
    private final CatalogTypes catalogTypes;

    @Inject
    public OperationsArea(SeparatedValueDaoImpl separatedValueDao, FileAttributes attributes, CatalogTypes catalogTypes) {
        this.separatedValueDao = separatedValueDao;
        this.attributes = attributes;
        this.catalogTypes = catalogTypes;
    }

    @CatalogRequest(
            id = "localDomainRequest_7e3f83c2-387b-4eef-bca7-088937c007aa",
            name = "Create CSV File",
            description = "Create CSV File From Entity.",
            area = "Operations",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "CSV", type = "File", required = false)
    public List<File> createCSVFile(
            @Field.Desc(name = "Entities", type = "[ Composite ]") List<FreeForm> entities) {
        try {
            String fileName = CatalogTypes.toFileName(attributes.getFileNamePrefix());
            return catalogTypes.toFile(fileName, separatedValueDao.getSV(CatalogTypes.toListMap(entities), fileName));
        } catch (Exception cause) {
            logger.error("Error while creating CSV: {}", cause.getMessage());
            return Collections.emptyList();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_4c56cc9f-6d99-4f53-b4a0-42e766c792d9",
            name = "Read CSV File",
            description = "Read CSV File",
            area = "Operations",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Desc(name = "Response", type = "[ Composite ]")
    public List<FreeForm> readCSVFile(
            @Field(name = "CSV", type = "File") List<File> cSV,
            @Field.Desc(name = "fields", type = "[ Text ]", required = false) List<String> fields) {
        if(cSV == null || cSV.isEmpty() || !isValidCsv(cSV.getFirst())) {
            throw new IllegalArgumentException(Errors.INVALID_CSV);
        }
        try {
            return separatedValueDao.readCSV(catalogTypes.fromFile(cSV.getFirst()), fields);
        } catch (Exception cause) {
            logger.error("Error while reading CSV: {}", cause.getMessage());
            return CatalogTypes.getErrorMessage(cause.getMessage());
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_bea204b4-7642-4654-befa-6a4439f45c63",
            name = "Get CSV As Table",
            description = "Get CSV As Table",
            area = "Operations",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "Response", type = "FreeForm", required = false)
    public FreeForm getCSVAsTable(
            @Field.File(name = "CSV" , attributes = {@Attribute(name = "visualWidth", value = "S")}) File cSV) {
        if(!isValidCsv(cSV)) {
            throw new IllegalArgumentException(Errors.INVALID_CSV);
        }
        try {
            return separatedValueDao.readSVAsTable(catalogTypes.fromFile(cSV));
        } catch (Exception cause) {
            logger.error("Error while converting CSV as a table: {}", cause.getMessage());
            return CatalogTypes.getErrorMessage(cause.getMessage()).getFirst();
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_51b1f7d6-5ea1-4b05-b6b3-345e1bfb592d",
            name = "Read CSV String",
            description = "Read CSV String.",
            area = "Operations",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.Desc(name = "Response", type = "[FreeForm]", required = false)
    public List<FreeForm> readCSVString(
            @Field(name = "CSV", type = "Paragraph") String cSV,
            @Field.Desc(name = "Fields", type = "[ Text ]", required = false) List<String> fields) {
        try {
            return separatedValueDao.readCSVString(cSV, fields);
        } catch (Exception cause) {
            logger.error("Error while reading CSV string: {}", cause.getMessage());
            return CatalogTypes.getErrorMessage(cause.getMessage());
        }
    }

    @CatalogRequest(
            id = "localDomainRequest_3193a81b-c4a6-4d0f-8d11-1e23fb6cf8ed",
            name = "Create SV String",
            description = "Create SV String",
            area = "Operations",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field(name = "CSV", type = "Paragraph", required = false, attributes = {@Attribute(name = "visualWidth", value = "L")})
    public String createSVString(
            @Field.Desc(name = "Entities", type = "[ Composite ]") List<Map<String, Object>> entities,
            @Field.PickOne(name = "FieldType", values = {"CSV","TSV","PSV"}, attributes = {@Attribute(name = "visualWidth", value = "S")}) String fieldType) {
        try {
            return separatedValueDao.getSVString(entities, fieldType);
        } catch (Exception cause) {
            logger.error("Error while creating SV string: {}", cause.getMessage());
            return cause.getMessage();
        }
    }

    private boolean isValidCsv(File kristaFile) {
        logger.info("Validating CSV file: {}", kristaFile);
        if(kristaFile == null || kristaFile.getFileName() == null || kristaFile.getFileName().isBlank()) {
            return false;
        }
        String fileName = kristaFile.getFileName();
        String fileExtension = kristaFile.getExtension() == null || kristaFile.getExtension().isBlank() ? fileName.substring(fileName.lastIndexOf('.') + 1) : kristaFile.getExtension();
        return fileExtension.equalsIgnoreCase("csv");
    }

}