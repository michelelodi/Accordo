package com.accordo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Base64;


public class ImageUtils {

    public static Bitmap base64ToBitmap(Base64 encodedImage) {

        byte[] decodedString = Base64.getDecoder().decode(String.valueOf(encodedImage));
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
