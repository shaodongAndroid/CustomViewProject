package com.intsig.scanner;

public class ScannerSDKIllegalAppException extends Exception
{
  private String mMsg;

  public ScannerSDKIllegalAppException(String msg)
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