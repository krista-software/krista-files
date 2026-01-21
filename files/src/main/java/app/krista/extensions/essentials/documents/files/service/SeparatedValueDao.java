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

package app.krista.extensions.essentials.documents.files.service;

import app.krista.model.base.FreeForm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SeparatedValueDao {

    /**
     * Returns {@link File} with given data
     * @param data data to added in the csv file
     * @param fileName name of the file
     * @return {@link File}
     * @throws FileNotFoundException if created file not found
     * @throws IllegalArgumentException if data is empty
     */
    File getSV(List<Map<String, Object>> data, String fileName) throws FileNotFoundException;

    /**
     * Returns {@link List<FreeForm>} from the given file
     * @param file file to be read
     * @param fields fields which wants to omit
     * @return {@link List<FreeForm>}
     * @throws IllegalArgumentException if file is null
     */
    List<FreeForm> readCSV(File file, List<String> fields) throws IOException;

    /**
     * Returns {@link Map} from the given file
     * @param file file to be read
     * @return {@link Map}
     * @throws IllegalArgumentException if file is null
     */
    Map<String, Object> readSVAsTable(File file) throws IOException;

    /**
     * Returns {@link String} from the given list of entities
     * @param entities entities to be read
     * @param fieldType filetype is the type of sv file. ex. csv, tsv, psv
     * @return {@link String}
     */
    String getSVString(List<Map<String, Object>> entities, String fieldType);

    /**
     * Returns {@link List<FreeForm>} from the given string
     * @param csv csv string to be read
     * @param fields fields which wants to omit
     * @return {@link List<FreeForm>}
     * @throws IllegalArgumentException if csv string is null or empty
     */
    List<FreeForm> readCSVString(String csv, List<String> fields);
}
