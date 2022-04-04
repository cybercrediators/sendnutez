package send.nutez.Prediction;

import android.content.ContentResolver;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

/**
 * helper class to retrieve predictions from the ncnn yolo predictor
 */
public class Predictor {

    public List<String> getPredictions() {
        return null;
    }
    private HashMap<String, List<Float>> lastPredictedClassed = null;

    public Predictor() {
        lastPredictedClassed = new HashMap<String, List<Float>>();
    }

    /**
     * return the last predicted classes
     * @return
     */
    public HashMap<String, List<Float>> getLastPredictedClasses() {
        return lastPredictedClassed;
    }

    /**
     * return the bitmap of a selected image Uri
     * @param selectedImage
     * @param contentResolver
     * @return
     */
    public Bitmap getPicture(Uri selectedImage, ContentResolver contentResolver) {
        //String[] filePathColumn = {MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()};
        //Cursor cursor = contentResolver.query(selectedImage, null, null, null,null);
        ////Cursor cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null);
        //if (cursor == null) {
        //    return null;
        //}
        //cursor.moveToFirst();
        //int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        //String picturePath = cursor.getString(columnIndex);
        //cursor.close();
        //File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        //Log.e("P", f.toString());
        Bitmap bitmap = BitmapFactory.decodeFile(selectedImage.getPath());
        if (bitmap == null) {
            return null;
        }
        int rotate = readPictureDegree(selectedImage.getPath());
        return rotateBitmapByDegree(bitmap, rotate);
    }

    /**
     * read the given pictures degree
     * @param path
     * @return
     */
    public int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * rotate a given bitmap by a given degree
     * @param bm
     * @param degree
     * @return
     */
    public Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * draw the box coordinates on a given bitmap
     */
    protected Bitmap drawBoxRects(Bitmap mutableBitmap, Box[] results) {
        if (results == null || results.length <= 0) {
            return mutableBitmap;
        }
        Canvas canvas = new Canvas(mutableBitmap);
        final Paint boxPaint = new Paint();
        boxPaint.setAlpha(200);
        boxPaint.setStyle(Paint.Style.STROKE);
        boxPaint.setStrokeWidth(4 * mutableBitmap.getWidth() / 800.0f);
        boxPaint.setTextSize(30 * mutableBitmap.getWidth() / 800.0f);
        for (Box box : results) {
            boxPaint.setColor(box.getColor());
            boxPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(box.getLabel() + String.format(Locale.US, " %.3f", box.getScore()), box.x0 + 3, box.y0 + 30 * mutableBitmap.getWidth() / 1000.0f, boxPaint);
            boxPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(box.getRect(), boxPaint);
        }
        return mutableBitmap;
    }

    /**
     * detect food on a given bitmap with the given
     * score and non-max-suppression threshold
     * and draw the rectangles onto the bitmap itself
     * @param img
     * @param threshold
     * @param nms_threshold
     * @return
     */
    public Bitmap detectFromImage(Bitmap img, double threshold, double nms_threshold) {
        Bitmap mutableBitmap = null;
        float[] enetMasks = null;
        Box[] result = getResults(img, threshold, nms_threshold);
        if (result != null) {
            lastPredictedClassed.clear();
            for (Box b: result) {
                Log.e("RESULT", "RESULTS: " + b.getLabel() + " " + b.getScore());
                if (lastPredictedClassed.containsKey(b.getLabel())) {
                    List<Float> scores = lastPredictedClassed.get(b.getLabel());
                    scores.add(b.getScore());
                    lastPredictedClassed.put(b.getLabel(), scores);
                }
                else {
                    List<Float> scores = new ArrayList<Float>();
                    scores.add(b.getScore());
                    lastPredictedClassed.put(b.getLabel(), scores);
                }
            }
        }
        mutableBitmap = drawBoxRects(img, result);
        return mutableBitmap;
    }

    /**
     * return a list of predicted classes, boxes and scores for a given bitmap
     * @param img
     * @param threshold
     * @param nms_threshold
     * @return
     */
    public Box[] getResults(Bitmap img, double threshold, double nms_threshold) {
        Box[] result = YOLOv5.detect(img, threshold, nms_threshold);
        //Log.e("SIZEOFCLASSES1", "" + lastPredictedClassed.toString());
        //Log.e("SIZEOFCLASSES", "" +result.length);
        return result;
    }
}
