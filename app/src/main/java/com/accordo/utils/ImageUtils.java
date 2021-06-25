package com.accordo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class ImageUtils {

    public static Bitmap base64ToBitmap(String encodedImage) {
        byte[] decodedString = android.util.Base64.decode(encodedImage, android.util.Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
