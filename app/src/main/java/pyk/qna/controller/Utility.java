package pyk.qna.controller;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import pyk.qna.App;

public class Utility {
  public static String cleanEmail(String email) {
    return email.replace('@', 'a').replace('.', 'd');
  }
  
  public static Bitmap getBitmapFromDrawable(int resourceID) {
    return BitmapFactory.decodeResource(App.get().getResources(), resourceID);
  }
  
  public static Bitmap scaleDownBitmap(Bitmap bm) {
    final int MAX_WIDTH  = 250;
    final int MAX_HEIGHT = 250;
    int       width      = bm.getWidth();
    int       height     = bm.getHeight();
    
    if (width > height) {
      // landscape
      float ratio = (float) width / MAX_WIDTH;
      width = MAX_WIDTH;
      height = (int) (height / ratio);
    } else if (height > width) {
      // portrait
      float ratio = (float) height / MAX_HEIGHT;
      height = MAX_HEIGHT;
      width = (int) (width / ratio);
    } else {
      // square
      height = MAX_HEIGHT;
      width = MAX_WIDTH;
    }
    
    bm = Bitmap.createScaledBitmap(bm, width, height, true);
    return bm;
  }
  
  public static String bitmapToBase64(Bitmap bitmap) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
    byte[] b            = baos.toByteArray();
    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
    return encodedImage;
  }
  
  public static Bitmap base64ToBitmap(String base64) {
    byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
    Bitmap decodedByte   = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    return decodedByte;
  }
  
  public static Bitmap rotateBitmapIfNeeded(Uri uri, Bitmap bitmap) {
    Bitmap adjustedBitmap = bitmap;
    try {
      Cursor cursor = App.get().getContentResolver().query(uri, null, null, null, null);
      cursor.moveToFirst();
      int           idx  = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
      ExifInterface exif = new ExifInterface(cursor.getString(idx));
      int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                          ExifInterface.ORIENTATION_NORMAL);
      int rotationDegrees = 0;
      if (rotation == ExifInterface.ORIENTATION_ROTATE_90) {
        rotationDegrees = 90;
      } else if (rotation == ExifInterface.ORIENTATION_ROTATE_180) {
        rotationDegrees = 90;
      } else if (rotation == ExifInterface.ORIENTATION_ROTATE_270) {
        rotationDegrees = 90;
      }
      
      Matrix matrix = new Matrix();
      if (rotation != 0f) {
        matrix.preRotate(rotationDegrees);
      }
      adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                                           bitmap.getHeight(), matrix, true);
      
    } catch (IOException e) {
      Toast.makeText(App.get(), "Load image failed, try again.", Toast.LENGTH_SHORT).show();
      e.printStackTrace();
    }
    return adjustedBitmap;
  }
  
  public static void handlePermission(Activity activity) {
    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        0);
    }
  }
}
