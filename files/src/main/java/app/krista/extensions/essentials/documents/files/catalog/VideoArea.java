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
import app.krista.extensions.essentials.documents.files.constants.Errors;
import app.krista.extensions.essentials.documents.files.impl.VideoConverterServiceImpl;
import app.krista.model.base.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

@Domain(id = "catEntryDomain_49c18799-c16b-4d33-98ac-f048f446a152",
        name = "Documents",
        ecosystemId = "catEntryEcosystem_84b53163-327b-4b1b-8c96-9334d292f9f5",
        ecosystemName = "Essentials",
        ecosystemVersion = "c39c410f-48bc-4515-b4b6-76b3c4d47940")

public class VideoArea {
    private static final Logger logger = LoggerFactory.getLogger(VideoArea.class);
    @Inject
    private VideoConverterServiceImpl converterService;

    @CatalogRequest(
            id = "localDomainRequest_1199afef-f0b5-40ab-8aea-88f0d71f7054",
            name = "Get Audio Encoders",
            description = "Returns a list of audio encoders available",
            area = "Video",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "Encoders", type = "[ Text ]", required = false)
    public List<String> getAudioEncoders() {
        logger.info("Getting audio encoders");
        return List.of(this.converterService.getAudioEncoder());
    }

    @CatalogRequest(
            id = "localDomainRequest_6e486138-f4d0-468e-bc2d-005642af7c0e",
            name = "Get Video Encoders",
            description = "Returns a list of video encoders available",
            area = "Video",
            type = CatalogRequest.Type.QUERY_SYSTEM)
    @Field.Desc(name = "Encoders", type = "[ Text ]", required = false)
    public List<String> getVideoEncoders() {
        logger.info("Getting video encoders...");
        return List.of(this.converterService.getVideoEncoders());
    }

    @CatalogRequest(
            id = "localDomainRequest_4c1f1572-bb73-40a2-95d8-f52f11df3927",
            name = "MP4 To MP3",
            description = "Convert MP4 to MP3 Files",
            area = "Video",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.File(name = "Output", required = false, attributes = {@Attribute(name = "visualWidth", value = "S"), @Attribute(name = "toolTip", value = "'MP3 File containing output from input MP4 file'")})
    public File mP4ToMP3(
            @Field.File(name = "Input", attributes = {@Attribute(name = "visualWidth", value = "S"), @Attribute(name = "toolTip", value = "'MP4 file'")}) File input) {
        logger.info("Converting file {} to MP3", input.getFileName());
        if(!isValidMp4(input)) {
            throw new IllegalArgumentException(Errors.INVALID_MP4);
        }
        return this.converterService.convertMp4ToMP3(input);
    }

    @CatalogRequest(
            id = "localDomainRequest_ced6871c-b06d-47e1-a5c6-d12c80d8e344",
            name = "MP4 to WAV",
            description = "Convert MP4 to WAV Files",
            area = "Video",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.File(name = "Output", required = false, attributes = {@Attribute(name = "visualWidth", value = "S"), @Attribute(name = "toolTip", value = "'Wav File containing output from input MP4 file'")})
    public File mP4ToWAV(
            @Field.File(name = "Input", attributes = {@Attribute(name = "visualWidth", value = "S"), @Attribute(name = "toolTip", value = "'MP4 file'")}) File input) {
        logger.info("Converting file {} to WAV", input.getFileName());
        if(!isValidMp4(input)) {
            throw new IllegalArgumentException(Errors.INVALID_MP4);
        }
        return this.converterService.convertMp4ToWav(input);
    }

    @CatalogRequest(
            id = "localDomainRequest_bb14c9c1-5e53-453c-8751-7767e7e83853",
            name = "MP4 To WAV MONO",
            description = "Convert MP4 to WAV File with 1 channel",
            area = "Video",
            type = CatalogRequest.Type.CHANGE_SYSTEM)
    @Field.File(name = "Output", required = false, attributes = {@Attribute(name = "visualWidth", value = "S"), @Attribute(name = "toolTip", value = "'Mono Wav File containing output from input MP4 file'")})
    public File mP4ToWAVMONO(
            @Field.File(name = "Input", attributes = {@Attribute(name = "visualWidth", value = "S"), @Attribute(name = "toolTip", value = "'MP4 file'")}) File input) {
        logger.info("Converting file {} to WAV (MONO)", input.getFileName());
        if(!isValidMp4(input)) {
            throw new IllegalArgumentException(Errors.INVALID_MP4);
        }
        return this.converterService.convertMp4ToWavMono(input);
    }

    private boolean isValidMp4(File kristaFile) {
        logger.info("Validating MP4 file: {}", kristaFile);
        if(kristaFile == null || kristaFile.getFileName() == null || kristaFile.getFileName().isBlank()) {
            return false;
        }
        String fileName = kristaFile.getFileName();
        String fileExtension = kristaFile.getExtension() == null || kristaFile.getExtension().isBlank() ? fileName.substring(fileName.lastIndexOf('.') + 1) : kristaFile.getExtension();
        return fileExtension.equalsIgnoreCase("mp4");
    }

}
