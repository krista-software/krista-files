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

import app.krista.extensions.essentials.documents.files.client.KristaMediaClient;
import app.krista.extensions.essentials.documents.files.constants.Errors;
import app.krista.extensions.essentials.documents.files.impl.util.Constants;
import app.krista.extensions.essentials.documents.files.util.KristaMediaClientUtil;
import app.krista.model.base.FreeForm;
import com.github.wnameless.json.flattener.JsonFlattener;
import org.apache.commons.lang3.StringUtils;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.wnameless.json.flattener.FlattenMode.KEEP_PRIMITIVE_ARRAYS;

/**
 * Service class providing common catalog operations and data transformations.
 *
 * <p>This service handles file conversions, data transformations, and utility
 * operations used across different catalog request implementations in the Files extension.</p>
 *
 * @author Krista Extensions Team
 * @version 2.0.5
 * @since 1.0.0
 */
@Service
public class CatalogTypes {

    private final KristaMediaClient kristaMediaClient;
    private final Logger logger = LoggerFactory.getLogger(CatalogTypes.class);

    @Inject
    private CatalogTypes(KristaMediaClient kristaMediaClient) {
        this.kristaMediaClient = kristaMediaClient;
    }

    /**
     * Converts a Krista file to a Java File object.
     *
     * <p>This method downloads the Krista file and converts it to a standard
     * Java File object for local processing operations.</p>
     *
     * @param kristaFile The Krista file to convert
     * @return The converted Java File object
     * @throws IOException if file conversion fails
     * @throws IllegalArgumentException if kristaFile is null
     */
    public File fromFile(app.krista.model.base.File kristaFile) throws IOException {
        if (kristaFile == null) {
            throw new IllegalArgumentException(Errors.EMPTY_FILE);
        }
        return kristaMediaClient.toJavaFile(kristaFile);
    }

    /**
     * Converts a Java File object to a Krista file list.
     *
     * <p>This method uploads the Java file and wraps it in a list for
     * compatibility with catalog request return types.</p>
     *
     * @param fileName The name for the file
     * @param file The Java File object to convert
     * @return List containing the converted Krista file
     * @throws IllegalArgumentException if fileName is invalid or file is null
     */
    public List<app.krista.model.base.File> toFile(String fileName, File file) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException(Errors.INVALID_FILE_NAME);
        } else if (file == null) {
            throw new IllegalArgumentException(Errors.FILE_NOT_FOUND);
        }
        app.krista.model.base.File kristaFile = uploadFile(file);
        return List.of(kristaFile);
    }

    /**
     * Uploads Java file to Krista file system with error handling.
     */
    private app.krista.model.base.File uploadFile(File file) {
        try {
            return kristaMediaClient.toKristaFile(file);
        } catch (IOException exception) {
            logger.error("Error while uploading file: {}", exception.getCause());
            throw new RuntimeException(exception);
        }
    }

    /**
     * Converts FreeForm entities to cleaned map list for CSV generation.
     */
    public static List<Map<String, Object>> toListMap(List<FreeForm> entities) {
        if (entities == null || entities.isEmpty()) {
            throw new IllegalArgumentException("No data to generate desired file");
        }
        List<Map<String, Object>> dataMapList = convertDataToList(entities);
        List<Map<String, Object>> cleanDataMapList = cleaningKeys(dataMapList);
        checkMissingKeys(cleanDataMapList);
        return cleanDataMapList;
    }

    /**
     * Flattens FreeForm entities to map structure using JSON flattener.
     */
    private static List<Map<String, Object>> convertDataToList(List<FreeForm> entities) {
        List<Map<String, Object>> dataMapList = new LinkedList<>();
        for (Object element : entities) {
            dataMapList.add(new JsonFlattener(Constants.GSON.toJson(element))
                    .withFlattenMode(KEEP_PRIMITIVE_ARRAYS).flattenAsMap());
        }
        return dataMapList;
    }

    /**
     * Generates timestamped filename with optional prefix.
     */
    public static String toFileName(String fileNamePrefix) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss aa");
        return KristaMediaClientUtil.TEMP_DIR +
                (fileNamePrefix == null || fileNamePrefix.isBlank() ? "File generated " : fileNamePrefix) + " at " + dateFormat.format(new Date()) + ".csv";
    }

    /**
     * Creates FreeForm list containing error message for display.
     */
    public static List<FreeForm> getErrorMessage(String errorMessage) {
        if (errorMessage == null || errorMessage.isBlank()) {
            throw new IllegalArgumentException(Errors.EMPTY_ERROR_MESSAGE);
        }
        List<FreeForm> freeFormList = new ArrayList<>();
        FreeForm freeForm = new FreeForm();
        freeForm.put("Error Message", "Text", errorMessage);
        freeFormList.add(freeForm);
        return freeFormList;
    }

    /**
     * Cleans and filters map keys, removing metadata and geolocation fields.
     */
    private static List<Map<String, Object>> cleaningKeys(List<Map<String, Object>> dataMapList) {
        List<Map<String, Object>> arrayList = new LinkedList<>();
        dataMapList.forEach((map) -> {
            Map<String, Object> withinMap = new LinkedHashMap<>();
            // Filtering out element whose key does not start with 'meta'
            List<String> list = map.keySet().stream().filter(key -> !key.startsWith("meta") && !key.contains("primaryKey")).collect(Collectors.toList());
            // Filtering out element that comes with geolocation and not useful
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).contains("latitude")) {
                    list.subList(i, i + 2).clear();
                    list.subList(i + 1, i + 4).clear();
                    break;
                }
            }
            // Putting all such elements and removing parent of each key
            list.forEach(key -> {
                String[] keyFields = key.split("\\.");
                if (keyFields.length == 3 && keyFields[2].equalsIgnoreCase("address"))
                    withinMap.put(StringUtils.capitalize(keyFields[keyFields.length - 2]), map.get(key));
                else
                    withinMap.put(StringUtils.capitalize(keyFields[keyFields.length - 1]), map.get(key));
            });
            arrayList.add(withinMap);
        });
        return arrayList;
    }

    /**
     * Ensures all maps have consistent keys by adding missing keys with empty values.
     */
    private static void checkMissingKeys(List<Map<String, Object>> dataMapList) {
        int listMaxSize = dataMapList.stream().mapToInt(Map::size).max().isPresent() ? dataMapList.stream().mapToInt(Map::size).max().getAsInt() : 0;
        final Set<String> keys = dataMapList.stream().filter(map -> map.keySet().size() == listMaxSize).findFirst().get().keySet();
        dataMapList.stream().filter(map -> map.keySet().size() != listMaxSize).forEach(map -> {
            for (String key : keys) {
                if (!map.containsKey(key))
                    map.put(key, "");
            }
        });
    }
}
