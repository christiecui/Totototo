package chris.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class ImageUtils {
	
    /**
     * 获取图片
     * @param imagePath
     * 			图片路径
     * @param minSideLength
     * 			需要的长或宽取较小的一个值，根据较小的值进行缩放，原图不设置为-1
     * @param pixels
     * 			最大的像素值，原图不设置为-1
     * @return
     */
    public static Bitmap getBitmap(String imagePath, int minSideLength, int pixels) {
    	return getBitmap(imagePath, minSideLength, pixels, Config.RGB_565);
    }
    
    /**
     * 获取图片
     * @param imagePath
     * 			图片路径
     * @param minSideLength
     * 			需要的长或宽取较小的一个值，根据较小的值进行缩放，原图不设置为-1
     * @param pixels
     * 			最大的像素值，原图不设置为-1
     * @param config
     * 			decode时的设置
     * @return
     */
    public static Bitmap getBitmap(String imagePath, int minSideLength, int pixels, Config config) {
        Bitmap bm = null;
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = config;
        BitmapFactory.decodeFile(imagePath, opts);

        // inSampleSize为图片要缩小的倍数
        opts.inSampleSize = computeSampleSize(opts, minSideLength, pixels);
        opts.inJustDecodeBounds = false;
        try {
        	bm = tryDecodeBitmap(imagePath, opts, 0);
        } catch (OutOfMemoryError err) {
        } catch (Exception e) {
        }
        return bm;
    }
    
    /**
     * 根据长宽的值或最大像素值，计算图片需要缩小的倍数
     * @param options
     * 			decode时的设置
     * @param minSideLength
     * 			需要的长或宽取较小的一个值，根据较小的值进行缩放，原图不设置为-1
     * @param maxNumOfPixels
     * 			最大的像素值，原图不设置为-1
     * @return
     * 		图片需要缩小的倍数
     * 		没有设置会返回1
     */
    private static int computeSampleSize(Options options, int minSideLength, int maxNumOfPixels) {

        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        // 缩小的倍数小于8时，取<=的偶数，等于1的话不用缩放还是返回1
        // 因为根据API中Options.inSampleSize的注释偶数倍缩放解码相对更简单更快
        // 大于8时，取>=的8的倍数，如15取8，16和17都取16
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }
    
    private static int computeInitialSampleSize(Options options, int minSideLength, int maxNumOfPixels) {

        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));

        // 这个128没有用处，不会被返回，没设置还是返回1
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
        		Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
    
    /**
     * 尝试decode图片，出现OOM最多尝试5次
     * @param path 
     * 			图片路径
     * @param opts 
     * 			decode的设置
     * @param tryTime 
     * 			尝试的次数
     * @return decode出来的Bitmap，失败返回null
     */
    public static Bitmap tryDecodeBitmap(String path, Options opts, int tryTime) {
    	if (tryTime > 3) {
    		return null;
    	}
        try {
            return BitmapFactory.decodeFile(path, opts);
        } catch (OutOfMemoryError e) {
            opts.inSampleSize += 1;
            return tryDecodeBitmap(path, opts, tryTime + 1);
        }
    }
    
    public static Bitmap tryDecodeResourceBitmap(Resources res,int id,Options opts,int tryTime){
    	
    	if(tryTime > 3){
    		return null;
    	}
    	
    	try{
    		return BitmapFactory.decodeResource(res, id, opts);
    	}catch(OutOfMemoryError e){
    		opts.inSampleSize += 1;
    		return tryDecodeResourceBitmap(res, id, opts, tryTime + 1);
    	}
    }
    
    
    //获得圆角图片的方法  
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){  
          
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap  
                .getHeight(), Config.ARGB_8888);  
        Canvas canvas = new Canvas(output);  
   
        final int color = 0xff424242;  
        final Paint paint = new Paint();  
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
        final RectF rectF = new RectF(rect);  
   
        paint.setAntiAlias(true);  
        canvas.drawARGB(0, 0, 0, 0);  
        paint.setColor(color);  
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
   
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
        canvas.drawBitmap(bitmap, rect, rect, paint);  
   
        return output;  
    }
    
    public static Bitmap getBitmap (byte[] data) {
        if (null == data) {
            return null;
        }
        
        Options opts = new Options ();
        opts.inSampleSize = 1;
        opts.inJustDecodeBounds = false;
        
        return tryDecodeBitmap (data, opts);
    }
    
    public static Bitmap tryDecodeBitmap (byte[] data, Options opts) {
    	if (null == opts) {
            opts = new Options ();
        }
    	return tryDecodeBitmap (data, opts, 0);
    }
    
    public static Bitmap tryDecodeBitmap (byte[] data, Options opts, int tryTime) {
    	if (tryTime > 5) {
    		return null;
    	}
    	
        try {
            return BitmapFactory.decodeByteArray (data, 0, data.length, opts);
        } catch (OutOfMemoryError e) {
            opts.inSampleSize += 1;
            return tryDecodeBitmap (data, opts, tryTime + 1);
        }
    }

    /* Drawable转Bitmap接口 */
    public static Bitmap drawableToBitmap (Drawable drawable) {
    	/* 取 Drawable的长宽 */
    	int w = drawable.getIntrinsicWidth ();
    	int h = drawable.getIntrinsicHeight ();

    	/* 取 Drawable的颜色格式 */
    	Config config = ((drawable.getOpacity () != PixelFormat.OPAQUE) ? Config.ARGB_8888 : Config.RGB_565);

    	/* 建立对应 Bitmap */
    	Bitmap b = Bitmap.createBitmap (w, h, config);

    	/* 建立对应 Bitmap的画布 */
    	Canvas canvas = new Canvas (b);
    	drawable.setBounds (0, 0, w, h);

    	/* 把 Drawable内容画到画布中 */
    	drawable.draw (canvas);

    	return b;
    }
    
    public static Bitmap transform(Matrix scaler, Bitmap source,
			int targetWidth, int targetHeight, boolean scaleUp, boolean recycle) {
		int deltaX = source.getWidth() - targetWidth;
		int deltaY = source.getHeight() - targetHeight;
		if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
			/*
			 * In this case the bitmap is smaller, at least in one dimension,
			 * than the target. Transform it by placing as much of the image as
			 * possible into the target and leaving the top/bottom or left/right
			 * (or both) black.
			 */
			Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight,
					Config.ARGB_8888);
			Canvas c = new Canvas(b2);

			int deltaXHalf = Math.max(0, deltaX / 2);
			int deltaYHalf = Math.max(0, deltaY / 2);
			Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf
					+ Math.min(targetWidth, source.getWidth()), deltaYHalf
					+ Math.min(targetHeight, source.getHeight()));
			int dstX = (targetWidth - src.width()) / 2;
			int dstY = (targetHeight - src.height()) / 2;
			Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight
					- dstY);
			c.drawBitmap(source, src, dst, null);
			if (recycle) {
				source.recycle();
			}
			return b2;
		}
		float bitmapWidthF = source.getWidth();
		float bitmapHeightF = source.getHeight();

		float bitmapAspect = bitmapWidthF / bitmapHeightF;
		float viewAspect = (float) targetWidth / targetHeight;

		if (bitmapAspect > viewAspect) {
			float scale = targetHeight / bitmapHeightF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		} else {
			float scale = targetWidth / bitmapWidthF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		}

		Bitmap b1;
		if (scaler != null) {
			// this is used for minithumb and crop, so we want to filter here.
			b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
					source.getHeight(), scaler, true);
		} else {
			b1 = source;
		}

		if (recycle && b1 != source) {
			source.recycle();
		}

		int dx1 = Math.max(0, b1.getWidth() - targetWidth);
		int dy1 = Math.max(0, b1.getHeight() - targetHeight);

		Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth,
				targetHeight);

		if (b2 != b1) {
			if (recycle || b1 != source) {
				b1.recycle();
			}
		}

		return b2;
	}
    
    /**
     * 转换图片成圆形
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap getRoundBitmap(Bitmap bitmap,int dstWidth,int dstHeight) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float left = 0,right,bottom;
            if (width <= height) {
                    bottom = width;
                    right = width;
                    height = width;
            } else {
                    float clip = (width - height) / 2;
                    left = clip;
                    right = width - clip;
                    bottom = height;
                    width = height;
            }
             
            Bitmap output = Bitmap.createBitmap(dstWidth,
            		dstHeight, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
             
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect src = new Rect((int)left,0, (int)right, (int)bottom);
            final Rect dst = new Rect(0,0, dstWidth,dstHeight);

            paint.setAntiAlias(true);
             
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(dstWidth / 2, dstHeight / 2, dstWidth / 2, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, src, dst, paint);
            return output;
    }
    
}
