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

package app.krista.extensions.essentials.documents.files;

import app.krista.extension.executor.Invoker;
import app.krista.extension.util.InvokerAttributes;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Service
public class FileAttributes {

    public static final String FILE_NAME_PREFIX = "File Name Prefix";
    private final String fileNamePrefix;

    @Inject
    public FileAttributes(Invoker invoker) {
        this.fileNamePrefix = InvokerAttributes.getStringOrNull(invoker.getAttributes(), FILE_NAME_PREFIX);
    }

    public String getFileNamePrefix() {
        return fileNamePrefix;
    }
}
