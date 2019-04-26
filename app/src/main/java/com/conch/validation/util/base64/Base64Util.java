package com.conch.validation.util.base64;




import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;



public class Base64Util {

	public static String bitmapToString(Bitmap bitmap , boolean destory) {
		ByteArrayOutputStream baos = null;
		String backString = "";
		try {
			baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
			backString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (null != baos) {
					baos.close();
					baos = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (destory){
				if (null!=bitmap && !bitmap.isRecycled()){
					bitmap.recycle();
					bitmap = null ;
				}
			}
		}
		return backString;
	}

	public static Bitmap stringToBitmap(String base64Data) {
		byte[] bytes = null;
		Bitmap bitmap = null ;
		try{
			bytes = Base64.decode(base64Data, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		}catch (Exception ex){
			ex.printStackTrace();
		}finally {
			if (null!=bytes){
				bytes = new byte[0] ;
				bytes = null ;
			}
			base64Data = "";
			base64Data = null ;
		}
		return bitmap ;
	}
 	
}


