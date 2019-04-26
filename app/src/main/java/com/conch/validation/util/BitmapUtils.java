package com.conch.validation.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;

/**
 * Created by FHXJR
 * on 2018/6/22/022.
 * 生成Bitmap
 * 处理Bitmap
 *
 * @author Administrator
 */

public enum BitmapUtils {
    /**
     *
     */
    INS;
    @SuppressLint("NewApi")
    public  byte[] getPixelsBGR(Bitmap image) {
        int bytes  ;
        ByteBuffer buffer = null;
        byte[] temp = null ;
        byte[] pixels = null ;
        try{
            bytes = image.getByteCount();
            buffer  = ByteBuffer.allocate(bytes);
            image.copyPixelsToBuffer(buffer);
            temp = buffer.array();
            pixels  = new byte[(temp.length / 4) * 3];
            // Copy pixels into place
            for (int i = 0; i < temp.length / 4; i++) {
                pixels[i * 3] = temp[i * 4 + 2];
                pixels[i * 3 + 1] = temp[i * 4 + 1];
                pixels[i * 3 + 2] = temp[i * 4];
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if (null!=buffer){
                buffer.clear() ;
            }
            temp = null ;
        }
        return pixels;

    }

    /**
     * 摄像头的预览回调byte转为bitmap
     * @param data 字节流
     * @return bm
     */
    public Bitmap cameraByteToBitmap(byte[] data, int width, int height) {
        Bitmap bm = null;
        ByteArrayOutputStream baos = null;
        byte[] jdata = null;
        YuvImage yuvimage = null;
         Matrix matrix = null;
        try {
            yuvimage = new YuvImage(data, ImageFormat.NV21, width, height, null);
            baos = new ByteArrayOutputStream();
            //这里 80 是图片质量，取值范围 0-100，100为品质最高
            yuvimage.compressToJpeg(new Rect(0, 0, width, height), 80, baos);
            jdata = baos.toByteArray();
            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.RGB_565;   //默认8888
            options.inSampleSize = 2;
            SoftReference<Bitmap> softRef = new SoftReference<Bitmap>(BitmapFactory.decodeByteArray(jdata, 0, jdata.length, options));//方便回收
            bm =  softRef.get();
            // 旋转90度
            matrix = new Matrix();
            matrix.setRotate(90);
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            jdata = null;
            yuvimage = null;
            matrix = null ;
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                    baos = null ;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LogUtils.e("Bitmap:" , "原始大小:"+ bm.getByteCount()/1024+"kb");
//        bm = ThumbnailUtils.extractThumbnail(bm,bm.getWidth(),bm.getHeight());
        return bm;
    }
    private Bitmap compressImage(Bitmap image) {
        Bitmap bitmap = null;
        ByteArrayOutputStream baos = null;
        ByteArrayInputStream isBm = null ;
        try{
            baos = new ByteArrayOutputStream();
            //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            int options = 100 * 1024;
            //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            long baoSize =  baos.toByteArray().length/1024 ;
            while ( baoSize>options) {
                //重置baos即清空baos
                baos.reset();
                //这里压缩options%，把压缩后的数据存放到baos中
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);
                //每次都减少10
                options -= 10;
            }
            //把压缩后的数据baos存放到ByteArrayInputStream中
            LogUtils.e("Bitmap:" , "压缩大小:"+ baos.toByteArray().length/1024+"kb");
            isBm  = new ByteArrayInputStream(baos.toByteArray());
            //把ByteArrayInputStream数据生成图片
            bitmap  = BitmapFactory.decodeStream(isBm, null, null);
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if (null!=image &&!image.isRecycled()){
                image.recycle();
                image = null ;
            }
            if (null!=baos){
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null!=isBm){
                try {
                    isBm.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmap;
    }
    public Bitmap cameraByteToBitmap(byte[] data, int width, int height , int rotateValue) {
        Bitmap bm = null;
        ByteArrayOutputStream baos = null;
        YuvImage yuvimage = null;
        Matrix matrix = null ;
        byte[] jdata = null;
        try {
            yuvimage = new YuvImage(data, ImageFormat.NV21, width, height, null);
            baos = new ByteArrayOutputStream();
            //这里 80 是图片质量，取值范围 0-100，100为品质最高
            yuvimage.compressToJpeg(new Rect(0, 0, width, height), 80, baos);
            jdata = baos.toByteArray();
            //将data byte型数组转换成bitmap文件
            bm = BitmapFactory.decodeByteArray(jdata, 0, jdata.length);
            if (bm != null) {
                matrix = new Matrix();
                matrix.setRotate(rotateValue);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null != yuvimage) {
                yuvimage = null;
            }
            if (null != jdata) {
                jdata = null;
            }
            matrix = null;
        }
        return bm;
    }


    /**
     * 旋转图片
     *
     * @param degree
     * @param srcBitmap
     * @return
     */
    private Bitmap rotateBimap(float degree, Bitmap srcBitmap) {
        if (null == srcBitmap)
            return srcBitmap;
        Bitmap bitmap = null;
        Matrix matrix = null ;
        try {
            matrix = new Matrix();
            matrix.setRotate(degree);
            bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            srcBitmap.recycle();
            matrix = null ;
        }
        return bitmap;
    }

    /**
     * 保存图片
     * @param bmp
     */
    public  void saveImage(Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Bitmap转化为 byte[]
     * @param bitmap
     * @return
     */
    public  byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 正常的bitmap回收
     *
     * @param bitmap
     */
    public  void recycle(Bitmap bitmap) {
        if (null != bitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * @param path
     * @return
     */
    public  byte[] imageTobyte(String path) {
        byte[] data = null;
        InputStream in = null ;
        // 读取图片字节数组
        try {
            in = new FileInputStream(path);
            data = new byte[in.available()];
            in.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null!=in){
                try {
//                    in.reset();
                    in.close();
                    in = null ;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return data;
    }
    /**
     * 得到本地或者网络上的bitmap url - 网络或者本地图片的绝对路径,比如:
     *
     * A.网络路径: url="http://blog.foreverlove.us/girl2.png" ;
     *
     * B.本地路径:url="file://mnt/sdcard/photo/image.png";
     *
     * C.支持的图片格式 ,png, jpg,bmp,gif等等
     *
     * @param url
     * @return
     */
    /**
     * file转bitmap(进行压缩,防止内存泄漏)
     */
    public Bitmap fileToBitmap(String imagePath, int kb) {
        Bitmap bitmap = null;
        FileInputStream fileInputStream = null ;
        try {
            File file = new File(imagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            fileInputStream = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fileInputStream, null, options);
            //将bitmap进行压缩防止内存泄漏
            bitmap = compressImage(bitmap, kb);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fileInputStream.close();
                fileInputStream = null ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 无回调有返回值的压缩方法
     *
     * @param image
     * @param kb
     * @return
     */
    public Bitmap compressImage(Bitmap image, int kb) {
        ByteArrayOutputStream baos = null;
        ByteArrayInputStream isBm = null;
        int options = 100;
        Bitmap bitmap = null;
        try {
            baos = new ByteArrayOutputStream();
            //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            //循环判断如果压缩后图片是否大于设定的kb,大于继续压缩
            while (baos.toByteArray().length / 1024 > kb) {
                //重置baos即清空baos
                baos.reset();
                //这里压缩options%，把压缩后的数据存放到baos中
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);
                options -= 10;//每次都减少10
            }
            //把压缩后的数据baos存放到ByteArrayInputStream中
            isBm = new ByteArrayInputStream(baos.toByteArray());
            //把ByteArrayInputStream数据生成图片
            bitmap = BitmapFactory.decodeStream(isBm, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (null != baos) {
                try {
                    baos.reset();
                    baos.close();
                    baos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != isBm) {
                try {
                    isBm.close();
                    isBm = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * byte转Bitmap
     *
     * @param bytes
     * @return
     */
    public Bitmap byte2Bitmap(byte[] bytes) {
        Bitmap stitchBmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return stitchBmp;
    }

    public Bitmap cutPicture(Bitmap src, int x, int y, int width, int height) {
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int newX = 0;
        int newY = 0;
        int newW = 0;
        int newH = 0;
        int w = width / 4;
        if (x > w ) {
            newX = x - w;
            if (x+5*w > srcWidth) {
                newW = srcWidth-newX;
            }else {
                newW = w*6;
            }

        }else {
            newX = 0;
            newW = width + 2*x;
        }
        int h = height/3;
        if (y > h ) {
            newY = y - h;
            if (y+h*4 > srcHeight) {
                newH = srcHeight-newY;
            }else {
                newH = h*5;
            }
        }else {
            newY = 0;
            newH = height + 2*y;
        }
        return Bitmap.createBitmap(src,newX,newY,newW,newH);
    }

    /**
     * 对图片byte[]进行压缩
     * @param buffer
     * @return
     */
    public byte[] compressBitmap(byte[] buffer,int scale) {
        if (buffer == null || buffer.length == 0) {
            return null;
        }
        ByteArrayOutputStream baos = null;
        try {
            // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
            int outWidth = options.outWidth;
            if (scale > 1) {
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                return baos.toByteArray();
            } else {
                return buffer;
            }
        } catch (Exception e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                return null;
            }
        }
    }
    /**
     *
     * @param bitMap
     * @param maxSize 多少kb
     */
    public Bitmap compressBitmap(Bitmap bitMap, int maxSize) {
        ByteArrayOutputStream baos = null ;
        Bitmap rebackBitmap = null ;
        try{
            baos = new ByteArrayOutputStream();
            bitMap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] b = baos.toByteArray();
            //将字节换成KB
            double mid = b.length/1024;
            //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
            if (mid > maxSize) {
                //获取bitmap大小 是允许最大大小的多少倍
                double i = mid / maxSize;
                //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
                rebackBitmap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i),
                        bitMap.getHeight() / Math.sqrt(i));
            }
        }catch (Exception ex){

        }finally {
            if (null!=baos){
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null!=bitMap && !bitMap.isRecycled()){
                bitMap.recycle();
                bitMap = null ;
            }
        }
        return rebackBitmap ;
    }
    /***
     * 图片的缩放方法
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    private Bitmap zoomImage(Bitmap bgimage, double newWidth,
                             double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }


}
