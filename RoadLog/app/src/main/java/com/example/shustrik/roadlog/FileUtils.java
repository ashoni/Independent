package com.example.shustrik.roadlog;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {
    public static boolean saveToFile(File f, byte[] data) {
        try {
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(data);
            fos.close();
            return true;
        } catch (IOException e) {
            Log.w("ANNA", "Can't save file: " + e.getMessage());
            return false;
        }
    }

    public static File getOutputMediaFile(String albumName) {
        File mediaStorageDir = getAlbumStorageDir(albumName);
        if (!mediaStorageDir.exists()) {
            return null;
        }
        // Create a media file name

        return new File(buildFileName(mediaStorageDir.getPath()));
    }

    private static String buildFileName(String path) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        return String.format("%s%sIMG_%s.jpg", path, File.separator, timeStamp);
    }

    public static File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.exists() && !file.mkdirs()) {
            Log.w("ANNA", "Directory not created");
        }
        return file;
    }

    public static void galleryAddPic(String filePath, Context context) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(filePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);

//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.DATA, f.getPath());
//        values.put(MediaStore.Images.Media.DATE_TAKEN, f.lastModified());
////        values.put(MediaStore.Images.Media.ORIENTATION, orient);
//
//
//        Uri mImageCaptureUri = getApplicationContext().getContentResolver()
//                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
//
//        // to notify change
//        getApplicationContext().getContentResolver().notifyChange(
//                Uri.parse("file://" + f.getPath()), null);
    }

}

//
//    public static final String insertImage(ContentResolver cr,
//                                           Bitmap source,
//                                           String title,
//                                           String description) {
//
//        ContentValues values = new ContentValues();
//        values.put(Images.Media.TITLE, title);
//        values.put(Images.Media.DISPLAY_NAME, title);
//        values.put(Images.Media.DESCRIPTION, description);
//        values.put(Images.Media.MIME_TYPE, "image/jpeg");
//        // Add the date meta data to ensure the image is added at the front of the gallery
//        values.put(Images.Media.DATE_ADDED, System.currentTimeMillis());
//        values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
//
//        Uri url = null;
//        String stringUrl = null;    /* value to be returned */
//
//        try {
//            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
//            if (source != null) {
//                OutputStream imageOut = cr.openOutputStream(url);
//                try {
//                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
//                } finally {
//                    imageOut.close();
//                }
//
//                long id = ContentUris.parseId(url);
//                // Wait until MINI_KIND thumbnail is generated.
//                Bitmap miniThumb = Images.Thumbnails.getThumbnail(cr, id, Images.Thumbnails.MINI_KIND, null);
//                // This is for backward compatibility.
//                storeThumbnail(cr, miniThumb, id, 50F, 50F,Images.Thumbnails.MICRO_KIND);
//            } else {
//                cr.delete(url, null, null);
//                url = null;
//            }
//        } catch (Exception e) {
//            if (url != null) {
//                cr.delete(url, null, null);
//                url = null;
//            }
//        }
//
//        if (url != null) {
//            stringUrl = url.toString();
//        }
//
//        return stringUrl;
//    }
//
//    /**
//     * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
//     * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
//     * meta data. The StoreThumbnail method is private so it must be duplicated here.
//     * @see android.provider.MediaStore.Images.Media (StoreThumbnail private method)
//     */
//    private static final Bitmap storeThumbnail(
//            ContentResolver cr,
//            Bitmap source,
//            long id,
//            float width,
//            float height,
//            int kind) {
//
//        // create the matrix to scale it
//        Matrix matrix = new Matrix();
//
//        float scaleX = width / source.getWidth();
//        float scaleY = height / source.getHeight();
//
//        matrix.setScale(scaleX, scaleY);
//
//        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
//                source.getWidth(),
//                source.getHeight(), matrix,
//                true
//        );
//
//        ContentValues values = new ContentValues(4);
//        values.put(Images.Thumbnails.KIND,kind);
//        values.put(Images.Thumbnails.IMAGE_ID,(int)id);
//        values.put(Images.Thumbnails.HEIGHT,thumb.getHeight());
//        values.put(Images.Thumbnails.WIDTH,thumb.getWidth());
//
//        Uri url = cr.insert(Images.Thumbnails.EXTERNAL_CONTENT_URI, values);
//
//        try {
//            OutputStream thumbOut = cr.openOutputStream(url);
//            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
//            thumbOut.close();
//            return thumb;
//        } catch (FileNotFoundException ex) {
//            return null;
//        } catch (IOException ex) {
//            return null;
//        }
//    }