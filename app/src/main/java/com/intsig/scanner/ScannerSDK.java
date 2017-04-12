package com.intsig.scanner;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.util.Log;

import java.security.MessageDigest;

public class ScannerSDK
{
  public static final int ENHANCE_MODE_AUTO = -2;
  public static final int ENHANCE_MODE_NO_ENHANCE = -1;
  public static final int ENHANCE_MODE_LINEAR = 15;
  public static final int ENHANCE_MODE_MAGIC = 1;
  public static final int ENHANCE_MODE_GRAY = 10;
  public static final int ENHANCE_MODE_BLACK_WHITE = 16;
  private static final int LOG_DEBUG = 2;
  private static final int ERROR_EXPIRED = -1;
  private static final int ERROR_PACKAGENAME = -2;
  private static final int ERROR_SIGNATURE = -3;
  private int mDetectMode = 1;

  public ScannerSDK(Context context)
    throws ScannerSDK.IllegalAppException
  {
    String packageName = context.getPackageName();
    ScannerEngine.loadLibrary("/data/data/" + packageName + "/lib");

    int code = 0 ;
    //ScannerEngine.initEngine(context);
    Log.d("ScannerSDK", "code=" + code);
    if (code != 0)
    {
      if (code == -1)
        throw new IllegalAppException("time is Expired ");
      if (code == -2)
        throw new IllegalAppException("packagename is illegal");
      if (code == -3) {
        throw new IllegalAppException("signature is not right");
      }
      throw new IllegalAppException("unknown error, code=" + code);
    }
  }

  public int initThreadContext()
  {
    return ScannerEngine.initThreadContext();
  }

  public void destroyContext(int threadContext)
  {
    ScannerEngine.destroyThreadContext(threadContext);
  }

  public int decodeImageS(String path)
  {
    int code = ScannerEngine.decodeImageS(path);
    Log.d("decodeImageS", "code=" + code);
    if ((code <= 0) && (code > -5)) {
      code = 0;
    }
    return code;
  }

  public void releaseImage(int imageStruct)
  {
    ScannerEngine.releaseImageS(imageStruct);
  }

  public int[] detectBorder(int threadContext, int imageStruct)
  {
    int[] outputBorders = new int[8];
    int code = ScannerEngine.detectImageS(threadContext, imageStruct,
      outputBorders, this.mDetectMode);
    if (code < 0) {
      outputBorders = null;
    }
    return outputBorders;
  }

  public boolean trimImage(int threadContext, int imageStruct, int[] borders, int maxSide)
  {
    return ScannerEngine.trimImageS(threadContext, imageStruct, borders, this.mDetectMode, maxSide) >= 0;
  }

  public boolean enhanceImage(int threadContext, Bitmap src, int mode)
  {
    return ScannerEngine.enhanceImage(threadContext, src, mode);
  }

  public boolean enhanceImage(int threadContext, int imageStruct, int mode)
  {
    return ScannerEngine.enhanceImageS(threadContext, imageStruct, mode) >= 0;
  }

  public void saveImage(int imageStruct, String outFilePath, int quality)
  {
    ScannerEngine.encodeImageS(imageStruct, outFilePath, quality, false);
  }
  private int dumpImage(int imageStruct, String outFilePath) {
    return ScannerEngine.dumpImageS(imageStruct, outFilePath);
  }
  private String getSignature(Context context, String pkg) {
    try {
      PackageInfo packageInfo = context.getPackageManager().getPackageInfo(pkg, 64);
      Signature[] arrayOfSignature;
      if ((arrayOfSignature = packageInfo.signatures).length != 0) { Signature signature = arrayOfSignature[0];
        return stringMD5(signature.toByteArray()); }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static String stringMD5(byte[] str) {
    try {
      MessageDigest msgDigest = MessageDigest.getInstance("MD5");
      msgDigest.update(str);
      byte[] result = msgDigest.digest();
      return byteArrayToHex(result);
    } catch (Exception e) {
    }
    return null;
  }

  private static String byteArrayToHex(byte[] bytes)
  {
    char[] hexDigits = { 
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
      'A', 'B', 'C', 'D', 'E', 'F' };

    StringBuilder result = new StringBuilder();
    byte[] arrayOfByte = bytes; int j = bytes.length; for (int i = 0; i < j; i++) { byte b = arrayOfByte[i];
      result.append(hexDigits[(b >> 4 & 0xF)]);
      result.append(hexDigits[(b & 0xF)]);
    }
    return result.toString();
  }

  public static class IllegalAppException extends Exception
  {
    private String mMsg;

    public IllegalAppException(String msg)
    {
      this.mMsg = msg;
    }

    public String getMessage() {
      return this.mMsg;
    }

    public String toString()
    {
      return this.mMsg;
    }
  }
}