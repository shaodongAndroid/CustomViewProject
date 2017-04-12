package com.intsig.scanner;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.HashMap;

public class ScannerEngine
{
  public static final int DEFAULT_JPG_QUALITY = 80;
  public static final int ENHANCE_MODE_LINEAR = 0;
  public static final int ENHANCE_MODE_MAGIC = 1;
  public static final int ENHANCE_MODE_ECONOMY = 2;
  public static final int ENHANCE_MODE_GRAY = 10;
  public static final int ENHANCE_MODE_BW = 11;
  public static final int ENHANCE_MODE_BLACKBOARD = 12;
  public static final int ENHANCE_MODE_COLOR = 13;
  public static final int ENHANCE_MODE_MAGIC_LITE = 14;
  public static final int ENHANCE_MODE_COLOR_2 = 15;
  public static final int ENHANCE_MODE_BW_2 = 16;
  public static final int ENHANCE_MODE_MAGIC_2 = 17;
  public static final int PREGRESS_DOWNSCALE = 1;
  public static final int PREGRESS_DETECT = 2;
  public static final int PREGRESS_TRIM = 3;
  public static final int PREGRESS_ENHANCE = 4;
  public static final int PREGRESS_SHARP = 5;
  public static final int PREGRESS_ADJUST = 6;
  public static final int IN_PROGRESS_DEWARP = 3;
  public static final int IN_PROGRESS_ENHANCE = 4;
  public static final int IN_PROGRESS_DETECT_RECT = 2;
  public static final int DETECT_MODE_NORMAL = 1;
  public static final int DETECT_MODE_PROJECTOR = 2;
  public static final int DETECT_MODE_CREDIT_CARD = 3;
  private static final String TAG = "ScannerEngine";
  static HashMap<Integer, Progress> mCallbacks = new HashMap();
  static int step = 10;

  public static boolean mInitialize = false;
  public static final int COLOR_RGB = 2;
  public static final int COLOR_RGBA_8888 = 3;
  public static final int ACTIVE_CODE_LEN = 20;

  static boolean onProcess(int context, int sessionId, int progress)
  {
    Progress listener = (Progress)mCallbacks.get(Integer.valueOf(context));
    if (listener == null) {
      return true;
    }
    int p = progress / step;
    if ((p == listener.progress) && (progress != 100)) {
      return true;
    }
    listener.progress = p;
    return listener.listener.onProcess(sessionId, progress);
  }

  public static void loadLibrary(String path) {
    if (!mInitialize)
      try {
        if (path != null)
          System.load(path + "/libcth.so");
        else {
          System.loadLibrary("cth");
        }

        mInitialize = true;
      } catch (Exception e) {
        mInitialize = false;
        e.printStackTrace();
      }
  }

  public static native int initEngine(Context paramContext);

  public static native int detectBorder(int paramInt1, String paramString, int[] paramArrayOfInt, int paramInt2);

  public static native int calculateNewSize(int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt1, int[] paramArrayOfInt2);

  public static native int isValidRect(int[] paramArrayOfInt, int paramInt1, int paramInt2);

  public static native int trimFile(int paramInt1, String paramString1, int[] paramArrayOfInt, int paramInt2, String paramString2, int paramInt3, int paramInt4);

  public static int trimFile(int context, String jpgfile, int[] borders, String out, int quality)
  {
    return trimFile(context, jpgfile, borders, 1, out, quality, 0);
  }

  public static native int trimBitmap(int paramInt1, Bitmap paramBitmap1, int[] paramArrayOfInt, Bitmap paramBitmap2, int paramInt2, int paramInt3);

  public static native int enhanceFile(int paramInt1, String paramString1, int paramInt2, String paramString2, int paramInt3);

  public static native int enhanceBitmap(int paramInt1, Bitmap paramBitmap, int paramInt2);

  public static native int setLogLevel(int paramInt);

  public static int setProcessListener(int context, ScannerProcessListener listener)
  {
    if (listener != null)
      mCallbacks.put(Integer.valueOf(context), new Progress(listener));
    else
      mCallbacks.remove(Integer.valueOf(context));
    return setProgress(context, listener != null);
  }

  public static native int setProgress(int paramInt, boolean paramBoolean);

  public static native int scaleImage(String paramString1, int paramInt1, float paramFloat, int paramInt2, String paramString2);

  private static native boolean verifySN(String paramString1, String paramString2, String paramString3, int paramInt);

  public static native int adjustImage(int paramInt1, String paramString1, int paramInt2, int paramInt3, int paramInt4, String paramString2, int paramInt5);

  public static synchronized native int adjustBitmap(int paramInt1, Bitmap paramBitmap, int paramInt2, int paramInt3, int paramInt4);

  public static native int adjustBound(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int paramInt);

  public static native int decodeImageS(String paramString, int paramInt);

  public static int decodeImageS(String path)
  {
    return decodeImageS(path, 2);
  }

  public static native int decodeYUVImageS(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3);

  public static native int encodeImageS(int paramInt1, String paramString, int paramInt2, boolean paramBoolean);

  public static native int releaseImageS(int paramInt);

  public static native int dumpImageS(int paramInt, String paramString);

  public static native byte[] getImageSPixels(int paramInt);

  public static native int detectImageS(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);

  public static native int trimImageS(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4);

  public static int trimImageS(int context, int imageStruct, int[] borders)
  {
    return trimImageS(context, imageStruct, borders, 1, 0);
  }

  public static native int enhanceImageS(int paramInt1, int paramInt2, int paramInt3);

  public static native int adjustImageS(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

  public static native int rotateAndScaleImageS(int paramInt1, int paramInt2, float paramFloat);

  public static native int initThreadContext();

  public static native int destroyThreadContext(int paramInt);

  public static native String GetVersion();

  public static int encodeImageS(int imageStruct, String out, int quality)
  {
    return encodeImageS(imageStruct, out, quality, true);
  }

  public static boolean trimImageFile(int context, String jpgFilename)
  {
    return trimFile(context, jpgFilename, null, null, 80) >= 0;
  }
  public static boolean trimImageFile(int context, String jpgFilename, int[] border) {
    return trimFile(context, jpgFilename, border, null, 80) >= 0;
  }
  public static boolean enhanceImageFile(int context, String jpgFilename, int quality) {
    return enhanceFile(context, jpgFilename, quality, null, 80) >= 0;
  }
  public static boolean enhanceImage(int context, Bitmap bmp, int quality) {
    return enhanceBitmap(context, bmp, quality) >= 0;
  }

  public static int drawDewarpProgressImage(int context, Bitmap b, int[] corner_xy, Bitmap b_out, int frame, int max_frame)
  {
    return trimBitmap(context, b, corner_xy, b_out, frame, max_frame);
  }
  public static int[] nativeDewarpImagePlaneForSize(int context, int width, int height, int[] corner_xy) {
    int[] outSize = new int[2];
    int ret = calculateNewSize(context, width, height, corner_xy, outSize);
    if (ret < 0)
      return null;
    return outSize;
  }
  public static int[] detectBoundLinesFromImageFile3(int context, String jpgFilename) {
    int[] borders = new int[8];
    int ret = detectBorder(context, jpgFilename, borders, 1);
    if (ret < 0) {
      return null;
    }
    return borders;
  }

  public static boolean verifySN(String machineCode, String privateKey, String snCode) {
    if ((snCode == null) || (snCode.length() < 20))
    {
      return false;
    }
    try {
      return verifySN(machineCode, privateKey, snCode, 3);
    } catch (UnsatisfiedLinkError localUnsatisfiedLinkError) {
    }
    return false;
  }

  public static int scaleImage(String filename, String out, float sw, int rotation, int quality)
  {
    return scaleImage(filename, rotation, sw, quality, out);
  }

  public static void setProcessDelay(int delay)
  {
    step = delay;
  }

  public static native int detectColorImageMode(int paramInt1, int paramInt2);

  public static native int EnableMultiCoreSpeedUp(int paramInt1, int paramInt2);

  public static native int decodeImageData(byte[] paramArrayOfByte, int paramInt);

  static class Progress
  {
    ScannerEngine.ScannerProcessListener listener;
    int progress;

    public Progress(ScannerEngine.ScannerProcessListener listener)
    {
      this.listener = listener;
      this.progress = 0;
    }
  }

  public static abstract interface ScannerProcessListener
  {
    public abstract boolean onProcess(int paramInt1, int paramInt2);
  }
}