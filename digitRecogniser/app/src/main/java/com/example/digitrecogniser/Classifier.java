package com.example.digitrecogniser;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Classifier {
    public static final int IMG_WIDTH = 28;
    public static final int IMG_HEIGHT = 28;

    private final Interpreter mInterpreter;

    public Classifier() {
        mInterpreter = new Interpreter(loadModelFile(activity), options);

        mImageData = ByteBuffer.allocateDirect(

                4 * BATCH_SIZE * IMG_HEIGHT * IMG_WIDTH * NUM_CHANNEL);

        mImageData.order(ByteOrder.nativeOrder());
    }


    public Result classify(Bitmap bitmap) {

        convertBitmapToByteBuffer(bitmap);

        long startTime = SystemClock.uptimeMillis();

        mInterpreter.run(mImageData, mResult);

        long endTime = SystemClock.uptimeMillis();

        long timeCost = endTime - startTime;

        Log.v(LOG_TAG, "classify(): result = " + Arrays.toString(mResult[0])

                + ", timeCost = " + timeCost);

        return new Result(mResult[0],timeCost);

    }


    private void convertBitmapToByteBuffer(Bitmap bitmap) {

        if (mImageData == null) {

            return;

        }
        mImageData.rewind();
        bitmap.getPixels(mImagePixels, 0, bitmap.getWidth(), 0, 0,

                bitmap.getWidth(), bitmap.getHeight());



        int pixel = 0;

        for (int i = 0; i < IMG_WIDTH; ++i) {

            for (int j = 0; j < IMG_HEIGHT; ++j) {

                int value = mImagePixels[pixel++];

                mImageData.putFloat(convertPixel(value));

            }

        }

    }
}
