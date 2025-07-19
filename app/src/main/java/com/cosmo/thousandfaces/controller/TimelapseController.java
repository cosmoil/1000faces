package com.cosmo.thousandfaces.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;

import com.cosmo.thousandfaces.model.ImageModel;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;

public class TimelapseController {

    public interface ExportCallback {
        void onSuccess(String path);
        void onFailure(String error);

    }

    private static final String TAG = "TimelapseController";
    private static final String MIME_TYPE = "video/avc";
    private static final int FRAME_RATE = 5;
    private static final int IFRAME_INTERVAL = 1;
    private static final int TIMEOUT_USEC = 10000;
    private static final int WIDTH = 720;
    private static final int HEIGHT = 960;
    private static final int BIT_RATE = 2_000_000;

    public void exportTimelapse(Context context, List<ImageModel> imageList, String filename, ExportCallback callback) {
        new Thread(() -> {
            MediaCodec mediaCodec = null;
            MediaMuxer mediaMuxer = null;
            Surface inputSurface = null;

            // ⬇ Neuer Dateiname statt hartkodiert
            File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename + ".mp4");

            try {
                MediaFormat mediaFormat = MediaFormat.createVideoFormat(MIME_TYPE, WIDTH, HEIGHT);
                mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
                mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, BIT_RATE);
                mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
                mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL);

                mediaCodec = MediaCodec.createEncoderByType(MIME_TYPE);
                mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
                inputSurface = mediaCodec.createInputSurface();
                mediaCodec.start();

                mediaMuxer = new MediaMuxer(outputFile.getAbsolutePath(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

                MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
                boolean muxerStarted = false;
                int trackIndex = -1;

                long frameDurationUs = 1_000_000L / FRAME_RATE;
                long presentationTimeUs = 0;

                for (ImageModel image : imageList) {
                    Bitmap bitmap = BitmapFactory.decodeFile(image.getImagePath());
                    if (bitmap == null) continue;

                    Bitmap scaled = Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, true);
                    Canvas canvas = inputSurface.lockCanvas(null);
                    canvas.drawColor(Color.BLACK);
                    canvas.drawBitmap(scaled, 0, 0, null);
                    inputSurface.unlockCanvasAndPost(canvas);
                    scaled.recycle();

                    Thread.sleep(1000 / FRAME_RATE);

                    boolean endOfStream = false;
                    while (!endOfStream) {
                        int outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
                        if (outputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                            break;
                        } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                            if (muxerStarted) {
                                throw new IllegalStateException("Format changed twice");
                            }
                            MediaFormat newFormat = mediaCodec.getOutputFormat();
                            trackIndex = mediaMuxer.addTrack(newFormat);
                            mediaMuxer.start();
                            muxerStarted = true;
                        } else if (outputBufferIndex >= 0) {
                            ByteBuffer encodedData = mediaCodec.getOutputBuffer(outputBufferIndex);
                            if (encodedData == null) {
                                throw new RuntimeException("encoderOutputBuffer " + outputBufferIndex + " was null");
                            }

                            if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                                bufferInfo.size = 0;
                            }

                            if (bufferInfo.size != 0 && muxerStarted) {
                                encodedData.position(bufferInfo.offset);
                                encodedData.limit(bufferInfo.offset + bufferInfo.size);
                                bufferInfo.presentationTimeUs = presentationTimeUs;
                                mediaMuxer.writeSampleData(trackIndex, encodedData, bufferInfo);
                                presentationTimeUs += frameDurationUs;
                            }

                            mediaCodec.releaseOutputBuffer(outputBufferIndex, false);

                            if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                                endOfStream = true;
                            }
                        }
                    }
                }

                mediaCodec.signalEndOfInputStream();

                // Final drain
                boolean endOfStream = false;
                while (!endOfStream) {
                    int outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
                    if (outputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                        break;
                    } else if (outputBufferIndex >= 0) {
                        ByteBuffer encodedData = mediaCodec.getOutputBuffer(outputBufferIndex);
                        if (encodedData != null && bufferInfo.size > 0 && muxerStarted) {
                            encodedData.position(bufferInfo.offset);
                            encodedData.limit(bufferInfo.offset + bufferInfo.size);
                            mediaMuxer.writeSampleData(trackIndex, encodedData, bufferInfo);
                        }
                        mediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                        if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                            endOfStream = true;
                        }
                    }
                }

                callback.onSuccess(outputFile.getAbsolutePath());

            } catch (Exception e) {
                Log.e(TAG, "Export failed", e);
                callback.onFailure("Export error: " + e.getMessage());
            } finally {
                if (mediaCodec != null) {
                    try {
                        mediaCodec.stop();
                        mediaCodec.release();
                    } catch (Exception e) {
                        Log.e(TAG, "Error releasing codec", e);
                    }
                }
                if (mediaMuxer != null) {
                    try {
                        mediaMuxer.stop();
                        mediaMuxer.release();
                    } catch (Exception e) {
                        Log.e(TAG, "Error releasing muxer", e);
                    }
                }
                if (inputSurface != null) {
                    inputSurface.release();
                }
            }
        }).start();
    }
}