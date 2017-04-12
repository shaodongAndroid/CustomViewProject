package com.intsig.scanner;

class ScannerEngineProgress
{
  ScannerEngine.ScannerProcessListener listener;
  int progress;

  public ScannerEngineProgress(ScannerEngine.ScannerProcessListener listener)
  {
    this.listener = listener;
    this.progress = 0;
  }
}