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

package app.krista.extensions.essentials.documents.files.impl;

import app.krista.extensions.essentials.documents.files.constants.Errors;
import app.krista.extensions.essentials.documents.files.util.KristaMediaClientUtil;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;

import javax.inject.Inject;
import java.io.File;
import java.util.UUID;

/**
 * Service implementation for video conversion operations.
 *
 * <p>This service provides video-to-audio conversion capabilities using the JAVE library.
 * It supports converting MP4 video files to various audio formats including MP3, WAV,
 * and WAV MONO (single channel) with configurable audio parameters.</p>
 *
 * <h3>Supported Conversions:</h3>
 * <ul>
 *   <li><strong>MP4 to MP3:</strong> Standard stereo audio conversion</li>
 *   <li><strong>MP4 to WAV:</strong> High-quality stereo audio conversion</li>
 *   <li><strong>MP4 to WAV MONO:</strong> Single-channel audio for voice applications</li>
 * </ul>
 *
 * <h3>Audio Configuration:</h3>
 * <ul>
 *   <li><strong>MP3:</strong> 128kbps bitrate, 44.1kHz sample rate, libmp3lame encoder</li>
 *   <li><strong>WAV:</strong> 128kbps bitrate, 44.1kHz sample rate, pcm_s16le encoder</li>
 *   <li><strong>WAV MONO:</strong> 64kbps bitrate, 16kHz sample rate, single channel</li>
 * </ul>
 *
 * @author Krista Extensions Team
 * @version 2.0.5
 * @since 1.0.0
 */
@Service
public class VideoConverterServiceImpl {

    public static final String FILE_FORMAT_MP4 = "mp4";
    public static final String FORMAT_MP3 = "mp3";
    public static final String FORMAT_WAV = "wav";
    public static final String ENCODER_MP3 = "libmp3lame";
    public static final String ENCODER_WAV = "pcm_s16le";
    public static final Integer DEFAULT_SAMPLE_RATE = 44100;
    public static final Integer DEFAULT_BIT_RATE = 128000;
    private static final Logger logger = LoggerFactory.getLogger(VideoConverterServiceImpl.class);
    private static final String FILE_TMP_INPUT = "input";
    private static final String FILE_TMP_OUTPUT = "output";
    private final KristaMediaClientUtil kristaMediaClientUtil;

    /**
     * Constructs a new VideoConverterServiceImpl with the specified media client utility.
     *
     * @param kristaMediaClientUtil The utility for handling file upload/download operations
     */
    @Inject
    public VideoConverterServiceImpl(KristaMediaClientUtil kristaMediaClientUtil) {
        this.kristaMediaClientUtil = kristaMediaClientUtil;
    }

    /**
     * Converts an MP4 video file to MP3 audio format.
     *
     * <p>Uses standard MP3 encoding with 128kbps bitrate and 44.1kHz sample rate.
     * The output maintains stereo channels for high-quality audio.</p>
     *
     * @param file The MP4 video file to convert
     * @return The converted MP3 audio file
     * @throws RuntimeException if conversion fails
     */
    public app.krista.model.base.File convertMp4ToMP3(final app.krista.model.base.File file) {
        return this.convert(file, FILE_FORMAT_MP4, FORMAT_MP3, FORMAT_MP3, null, ENCODER_MP3, DEFAULT_BIT_RATE, DEFAULT_SAMPLE_RATE);
    }

    /**
     * Converts an MP4 video file to WAV audio format.
     *
     * <p>Uses PCM encoding with 128kbps bitrate and 44.1kHz sample rate.
     * The output maintains stereo channels for uncompressed audio quality.</p>
     *
     * @param file The MP4 video file to convert
     * @return The converted WAV audio file
     * @throws RuntimeException if conversion fails
     */
    public app.krista.model.base.File convertMp4ToWav(final app.krista.model.base.File file) {
        return this.convert(file, FILE_FORMAT_MP4, FORMAT_WAV, FORMAT_WAV, null, ENCODER_WAV, DEFAULT_BIT_RATE, DEFAULT_SAMPLE_RATE);
    }

    /**
     * Converts an MP4 video file to WAV MONO audio format.
     *
     * <p>Uses PCM encoding with 64kbps bitrate, 16kHz sample rate, and single channel.
     * This format is optimized for voice applications and smaller file sizes.</p>
     *
     * @param file The MP4 video file to convert
     * @return The converted WAV MONO audio file
     * @throws RuntimeException if conversion fails
     */
    public app.krista.model.base.File convertMp4ToWavMono(final app.krista.model.base.File file) {
        return this.convert(file, FILE_FORMAT_MP4, FORMAT_WAV, FORMAT_WAV, 1, ENCODER_WAV, 64000, 16000);
    }

    /**
     * Retrieves the list of available audio encoders from the system.
     *
     * @return Array of available audio encoder names
     * @throws RuntimeException if unable to retrieve encoders
     */
    public String[] getAudioEncoder() {

        try {
            return new Encoder().getAudioEncoders();
        } catch (EncoderException cause) {
            throw new RuntimeException(Errors.AUDIO_ENCODER_ERROR, cause);
        }
    }

    /**
     * Retrieves the list of available video encoders from the system.
     *
     * @return Array of available video encoder names
     * @throws RuntimeException if unable to retrieve encoders
     */
    public String[] getVideoEncoders() {
        try {
            return new Encoder().getVideoEncoders();
        } catch (EncoderException cause) {
            throw new RuntimeException(Errors.VIDEO_ENCODER_ERROR, cause);
        }
    }

    /**
     * Core conversion method that handles video to audio conversion with specified parameters.
     */
    public app.krista.model.base.File convert(final app.krista.model.base.File kristaFile, final String inputFormat, final String outputFormat, final String outputExtension, final Integer channels, final String audioEncodr, final Integer bitRate, final Integer sampleRate) {
        final File tmpDir = new File(System.getProperty("java.io.tmpDir", "/tmp"));
        final String uuid = UUID.randomUUID().toString();

        final File input = kristaMediaClientUtil.downloadFileHandler(kristaFile);
        final File output = new File(tmpDir.getAbsolutePath() + File.separatorChar + uuid + "_" + FILE_TMP_OUTPUT + "." + outputExtension);

        try {

            //Audio Attributes
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec(audioEncodr);

            audio.setBitRate(bitRate);
            if (channels != null)
                audio.setChannels(channels);
            audio.setSamplingRate(sampleRate);

            //Encoding attributes
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setInputFormat(inputFormat);
            attrs.setOutputFormat(outputFormat);
            attrs.setAudioAttributes(audio);

            //Encode
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(input), output, attrs);

        } catch (Exception cause) {
            logger.error("Failed encode input file {} {}", cause.getMessage(), cause);
            throw new RuntimeException(Errors.VIDEO_CONVERSION_ERROR, cause);
        }

        return kristaMediaClientUtil.uploadFileHandler(output);

    }

}
