package com.custom.view.project.util;

import android.content.Context;

/**
 * Created by 少东 on 2017/2/15.
 */

public class ScreenUtils {

    public static int getScreenWidth(Context context){
      return  context.getResources().getDisplayMetrics().widthPixels ;
    }

}
