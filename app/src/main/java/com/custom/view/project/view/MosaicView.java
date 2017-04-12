package com.custom.view.project.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.custom.view.project.activity.MosaicActivity;
import com.custom.view.project.util.BitmapUtil;
import com.custom.view.project.util.ImageViewUtil;
import com.intsig.scanner.ScannerSDK;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 少东 on 2017/2/4.
 */

public class MosaicView extends ViewGroup {

    private static final String TAG = "info";

    // default image inner padding, in dip pixels
    private static final int INNER_PADDING = 6;

    // default grid width, in dip pixels
    private static final int GRID_WIDTH = 20;

    private int TOP_TOOL_HEIGHT ;

    /**
     * 默认裁剪框的宽高
     */
    private static final int DEFAULT_FLOAT_DRAWABLE_W_H = 300 ;

    private  Context mContext ;

    public enum Effect {
        GRID, COLOR, BLUR,
    }

    public enum Mode {
        GRID, PATH, NORMAL
    }

//    private enum EditStatus {
//        CROP,MOSAIC
//    }

    private FloatDrawable mFloatDrawable ; // 裁剪框
    private Rect mFloatRect = new Rect();

    private Bitmap originalBitmap ;
    private Drawable originalDrawable ;

    private Bitmap baseLayerBitmap ;
    private Drawable baseLayerDrawable ;

    private Bitmap coverLayerBitmap ;
    private Drawable coverLayerDrawable ;

    private Bitmap mosaicLayerBitmap ;
    private Drawable mosaicLayerDrawable ;

    private int mScreenWidth ;

    private int mImageWidth , mImageHeight ;

    private int mMosaicColor = 0xFF4D4D4D;
    private int mPadding ;
    private int mGridWidth ;

    private String inPath ;
    private String outPath ; //文件输出路径

    private Path mTouchPath ;

    private List<Path> mTouchPaths;

    private Rect mImageRect = new Rect();
    private Rect mDrawableSrc = new Rect();
    private Rect mDrawableDst = new Rect();

    private Effect mEffect ;
    private Mode mMode ;

    private Paint mMosaicPaint ;
    public MosaicView(Context context) {
        this(context, null);
    }

    public MosaicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MosaicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context ;
        initData();
        mFloatDrawable = new FloatDrawable(context);
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        Log.d(TAG, "MosaicView: ---------mScreenWidth = "+mScreenWidth);
    }

    private void initData() {
        TOP_TOOL_HEIGHT = dp2Px(50) ;
        mEffect = Effect.GRID;
//        mPadding = dp2Px(INNER_PADDING);
        mPadding = 0;
        mGridWidth = dp2Px(GRID_WIDTH);

        mMode = Mode.NORMAL;

        mTouchPaths = new ArrayList<>();

        mMosaicPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMosaicPaint.setStyle(Paint.Style.STROKE);
        mMosaicPaint.setStrokeJoin(Paint.Join.ROUND);
        mMosaicPaint.setStrokeCap(Paint.Cap.ROUND);
        mMosaicPaint.setPathEffect(new CornerPathEffect(10));
        mMosaicPaint.setStrokeWidth(10);
    }

    public void setMode(Mode mode){
        this.mMode = mode ;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: -----------onMeasure()");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG, "---------------onLayout()");
        if(mImageWidth <= 0 || mImageHeight <= 0){
            return ;
        }

        int contentWidth = r - l ;
        int contentHeight = b - t ;

        Log.d(TAG, "onLayout: contentWidth = "+contentWidth);
        Log.d(TAG, "onLayout: contentHeight = "+contentHeight);
        int viewWidth = contentWidth - mPadding * 2;
        int viewHeight = contentHeight - mPadding * 2;

        float widthRatio = viewWidth * 1.0f / mImageWidth;
        float heightRatio = viewHeight * 1.0f / mImageHeight;

        Log.d(TAG, "onLayout: widthRatio = "+widthRatio);
        Log.d(TAG, "onLayout: heightRatio = "+heightRatio);

        float ratio = widthRatio < heightRatio ? widthRatio : heightRatio;
        Log.d(TAG, "onLayout: ratio = "+ratio);

        int realWidth = (int) (mImageWidth * ratio);
        int realHeight = (int) (mImageHeight * ratio);

        Log.d(TAG, "onLayout: realWidth = "+realWidth);
        Log.d(TAG, "onLayout: realHeight = "+realHeight);

        int imageLeft = (contentWidth - realWidth) / 2;
        int imageTop = (contentHeight - realHeight) / 2;
        int imageRight = imageLeft + realWidth;
        int imageBottom = imageTop + realHeight;

//        mImageRect.set(imageLeft, imageTop, imageRight, imageBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: -------------onDraw()");
        if(baseLayerBitmap == null){
            return ;
        }
        if(baseLayerDrawable.getIntrinsicWidth() == 0 || baseLayerDrawable.getIntrinsicHeight() == 0){
            return ;
        }

        configureBounds(baseLayerDrawable);

//        if(mBaseDrawable != null && bmBaseLayer != null && !bmBaseLayer.isRecycled()) {
        if(baseLayerDrawable != null){
            baseLayerDrawable.draw(canvas);
        }

        if(mosaicLayerDrawable != null){
            configureBounds(mosaicLayerDrawable);
            mosaicLayerDrawable.draw(canvas);
        }

        if(mMode == Mode.NORMAL){
            canvas.save();
            canvas.clipRect(mFloatRect, Region.Op.DIFFERENCE);
            canvas.drawColor(Color.parseColor("#a0000000"));
            canvas.restore();
            mFloatDrawable.draw(canvas);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (mMode == Mode.GRID) {
//            onGridEvent(action, x, y);
        } else if (mMode == Mode.PATH) {
            mImageRect = ImageViewUtil.getBitmapRectCenterInside(baseLayerBitmap, this);
            onPathEvent(action, x, y);
        } else {
            return super.dispatchTouchEvent(event);
        }

        return true;
    }

    private void onPathEvent(int action, int x, int y) {
        if(mImageWidth <= 0 || mImageHeight <= 0){
            return ;
        }

        if(x < mImageRect.left || x > mImageRect.right || y < mImageRect.top || y > mImageRect.bottom){
            return ;
        }

        Log.d(TAG, "onPathEvent: ------------mImageRect 1 = "+mImageRect.width());
        Log.d(TAG, "onPathEvent: ------------mImageRect 2 = "+(mImageRect.right - mImageRect.left));

        float ratio = mImageRect.width() *  1.0f / mImageWidth;
        Log.d(TAG, "onPathEvent: ------------ratio = "+ratio);
        x = (int) ((x - mImageRect.left) / ratio);
        y = (int) ((y - mImageRect.top) / ratio);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mTouchPath = new Path();
                mTouchPath.moveTo(x, y);
                mTouchPaths.add(mTouchPath);
                break;
            case MotionEvent.ACTION_MOVE:
                if(mTouchPath == null){
                    mTouchPath = new Path();
                }
                mTouchPath.lineTo(x, y);
                updatePathMosaic();
                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                break;
        }

    }

    private void updatePathMosaic() {
        if (mosaicLayerBitmap != null) {
            mosaicLayerBitmap.recycle();
            mosaicLayerBitmap = null ;
        }

        mosaicLayerBitmap = Bitmap.createBitmap(mImageWidth, mImageHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(mosaicLayerBitmap);

        for(Path path : mTouchPaths){
            canvas.drawPath(path, mMosaicPaint);
        }

        mosaicLayerDrawable = new BitmapDrawable(getResources(), mosaicLayerBitmap);
    }

    public void clearPath(){
        if(mTouchPaths.isEmpty()){
            return ;
        }
        mTouchPaths.clear();
        updatePathMosaic();
        invalidate();
    }

    private final int EDGE_LT = 1;//左上
    private final int EDGE_RT = 2;//右上
    private final int EDGE_LB = 3;//左下
    private final int EDGE_RB = 4;//右下
    private final int EDGE_MOVE_IN = 5;//里面移动
    private final int EDGE_MOVE_OUT = 6;//外面移动

    private float oldX, oldY ;

    private int mStatus ;

    private int currentEdge ;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Log.d(TAG, "onTouchEvent: getLeft = "+getLeft());
        Log.d(TAG, "onTouchEvent: getRight = "+getRight());
        Log.d(TAG, "onTouchEvent: getTop = "+getTop());
        Log.d(TAG, "onTouchEvent: getBottom = "+getBottom());

        Log.d(TAG, "onTouchEvent: mFloatRect.left = "+mFloatRect.left);
        Log.d(TAG, "onTouchEvent: mFloatRect.right = "+mFloatRect.right);
        Log.d(TAG, "onTouchEvent: mFloatRect.top = "+mFloatRect.top);
        Log.d(TAG, "onTouchEvent: mFloatRect.bottom = "+mFloatRect.bottom);
        if(mMode != Mode.NORMAL){
            return false;
        }
        Log.d(TAG, "onTouchEvent: -----------onTouchEvent()");
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                oldX = (int) event.getX();
                oldY = (int) event.getY();

                currentEdge = getTouchEdge((int)oldX, (int)oldY);
                Log.d(TAG, "onTouchEvent: -----------currentEdge = "+currentEdge);
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (event.getX() - oldX);
                int dy = (int) (event.getY() - oldY);

                oldX = event.getX();
                oldY = event.getY();

                if(!(dx == 0 && dy == 0)){
                    switch (currentEdge){
                        case EDGE_LT:
                            mFloatRect.set(mFloatRect.left + dx, mFloatRect.top + dy, mFloatRect.right, mFloatRect.bottom);
                            break;
                        case EDGE_RT:
                            mFloatRect.set(mFloatRect.left, mFloatRect.top + dy, mFloatRect.right + dx, mFloatRect.bottom);
                            break;
                        case EDGE_LB:
                            mFloatRect.set(mFloatRect.left + dx, mFloatRect.top, mFloatRect.right, mFloatRect.bottom + dy);
                            break;
                        case EDGE_RB:
                            mFloatRect.set(mFloatRect.left, mFloatRect.top, mFloatRect.right + dx, mFloatRect.bottom + dy);
                            break;
                        case EDGE_MOVE_IN:
//                        mFloatRect.set(mFloatRect.left + dx, mFloatRect.top + dy, mFloatRect.right + dx, mFloatRect.bottom + dy);
                            mFloatRect.offset(dx, dy);
                            break;
                        case EDGE_MOVE_OUT:
                            break;
                    }

                    if(currentEdge != EDGE_MOVE_IN){
                        if(mFloatRect.left < 0){
                            mFloatRect.left = getLeft() ;
                        }

                        if(mFloatRect.right > getRight()){
                            mFloatRect.right = getRight() ;
                        }

                        if(mFloatRect.top < 0){
                            mFloatRect.top = 0 ;
                        }

                        if(mFloatRect.bottom > getBottom()){
                            mFloatRect.bottom = getBottom() ;
                        }
                    }

                    mFloatRect.sort();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                checkBounds();
                break;
        }

        return true;
    }

    protected void checkBounds() {
        int newLeft = mFloatRect.left;
        int newTop = mFloatRect.top;

        boolean isChange = false;
        if (mFloatRect.left < getLeft()) {
            newLeft = getLeft();
            isChange = true;
        }

        if (mFloatRect.top < (getTop() - TOP_TOOL_HEIGHT)) {
            newTop = getTop() - TOP_TOOL_HEIGHT;
            isChange = true;
        }

        if (mFloatRect.right > getRight()) {
            newLeft = getRight() - mFloatRect.width();
            isChange = true;
        }

        if (mFloatRect.bottom > (getBottom() - TOP_TOOL_HEIGHT)) {
            newTop = (getBottom() - TOP_TOOL_HEIGHT) - mFloatRect.height();
            isChange = true;
        }
        mFloatRect.offsetTo(newLeft, newTop);
        if (isChange) {
            invalidate();
        }
    }

    private int getTouchEdge(int oldX, int oldY) {
        if(oldX >= mFloatRect.left && oldX <= (mFloatRect.left + mFloatDrawable.getFloatWidth())
                && oldY >= mFloatRect.top && oldY <= (mFloatRect.top + mFloatDrawable.getFloatHeight())){
            return EDGE_LT ;
        } else if(oldX >= (mFloatRect.right - mFloatDrawable.getFloatWidth()) && oldX <= mFloatRect.right
                && oldY >= mFloatRect.top && oldY <= (mFloatRect.top + mFloatDrawable.getFloatHeight())){
            return EDGE_RT ;
        } else if(oldX >= mFloatRect.left && oldX <= (mFloatRect.left + mFloatDrawable.getFloatWidth())
                && oldY >= (mFloatRect.bottom - mFloatDrawable.getFloatHeight()) && oldY <= mFloatRect.bottom){
            return EDGE_LB ;
        } else if(oldX >= (mFloatRect.right - mFloatDrawable.getFloatWidth()) && oldX <= mFloatRect.right
                && oldY >= (mFloatRect.bottom- mFloatDrawable.getFloatHeight()) && oldY <= mFloatRect.bottom){
            return EDGE_RB ;
        } else if(mFloatRect.contains(oldX, oldY)){
            return EDGE_MOVE_IN ;
        }
        return EDGE_MOVE_OUT;
    }

    private float oriRationWH ; // 原始宽高比率

    private boolean isFirst = true ;
    private void configureBounds(Drawable drawable) {
        if(isFirst){
            oriRationWH = drawable.getIntrinsicWidth() * 1.0f / drawable.getIntrinsicHeight();

            int w = getWidth() ;
            int h = (int) (w * (drawable.getIntrinsicHeight() * 1.0f / drawable.getIntrinsicWidth()));

            int left = 0;//(getWidth()-w)/2;
            int top = (getHeight() - h) / 2;
            int right = left + w;
            int bottom = top + h;

            mDrawableSrc.set(left, top, right, bottom);
            mDrawableDst.set(mDrawableSrc);

            int floatWidth = dp2Px(DEFAULT_FLOAT_DRAWABLE_W_H);
            int floatHeight = dp2Px(DEFAULT_FLOAT_DRAWABLE_W_H);

            if(floatWidth > getWidth()){
                floatWidth = getWidth() ;
                floatHeight = floatWidth;
            }

            if(floatHeight > getHeight()){
                floatHeight = getHeight();
                floatWidth = floatHeight ;
            }

            int floatLeft = (getWidth() - floatWidth) / 2 ;
            int floatTop = (getHeight() - floatHeight) / 2 ;

            mFloatRect.set(floatLeft, floatTop, floatLeft + floatWidth, floatTop + floatHeight);
            isFirst = false ;
        }

        Rect displayRect = ImageViewUtil.getBitmapRectCenterInside(((BitmapDrawable)baseLayerDrawable).getBitmap(), this);
        if (displayRect == null) {
            displayRect = ImageViewUtil.getBitmapRectCenterInside(baseLayerBitmap, this);
            drawable.setBounds(displayRect);
        } else {
            drawable.setBounds(displayRect);
        }
//        Log.d(TAG, "configureBounds: floatLeft = "+displayRect.left);
//        Log.d(TAG, "configureBounds: floatTop = "+displayRect.top);
//        Log.d(TAG, "configureBounds: right = "+displayRect.right);
//        Log.d(TAG, "configureBounds: bottom = "+displayRect.bottom);
        mFloatDrawable.setBounds(mFloatRect);
    }

    /**
     * 处理图片
     * @param scannerSDK
     */
    public void doPretreatment(ScannerSDK scannerSDK) {
        if (originalBitmap == null) {
            return;
        }
        int threadContext = scannerSDK.initThreadContext();
        baseLayerBitmap = originalBitmap.copy(originalBitmap.getConfig(), true);
        scannerSDK.enhanceImage(threadContext, baseLayerBitmap, ScannerSDK.ENHANCE_MODE_BLACK_WHITE);
        scannerSDK.destroyContext(threadContext);
    }

    public void reFreshView(){
        baseLayerDrawable = new BitmapDrawable(getResources(), baseLayerBitmap);

        coverLayerBitmap = getCoverLayer();
        coverLayerDrawable = new BitmapDrawable(getResources(), coverLayerBitmap);

        requestLayout();
//        invalidate();
    }

    /**
     * 设置图片
     * @param path 图片路径
     * @param type 获取图片的方式 拍照 从图库选择
     * @param isRotate 是否旋转
     */
    public void setImagePath(String path, int type, boolean isRotate){
        File file = new File(path);
        if(file == null || !file.exists()){
            Log.w(TAG, "setImagePath: file not exists");
            return ;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true ;
        BitmapFactory.decodeFile(path, options);

        if(options.outWidth > 4096 || options.outHeight > 4096){
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        originalBitmap = getPathBitmap(file);

        if(isRotate){
            int degree = BitmapUtil.readPictureDegree(path);
            Log.d(TAG, "setImagePath: degree = "+degree);
            if(degree != 0){
                originalBitmap = rotateBitmap(originalBitmap, degree);
            }
        }

        if(originalBitmap == null){
            return ;
        }

        mImageWidth = originalBitmap.getWidth() ;
        mImageHeight = originalBitmap.getHeight() ;

        if(type == MosaicActivity.TYPE_PICK){
            inPath = path ;
            String fileName = file.getName();
            String filePath = file.getParent();

            int index = fileName.lastIndexOf(".") ;
            String name = fileName.substring(0, index);

            String newName = name + "_mosaic";
            fileName.replace(name, newName);

            outPath = filePath + File.separator + fileName ;
            Log.d(TAG, "setImagePath: -------outPath = "+outPath);
        }
//        originalDrawable = new BitmapDrawable(getResources(), originalBitmap);

//        baseLayerBitmap = originalBitmap ;
//        baseLayerDrawable = new BitmapDrawable(getResources(), originalBitmap);
//        coverLayerBitmap = getCoverLayer();
//        coverLayerDrawable = new BitmapDrawable(getResources(), coverLayerBitmap);
//        invalidate();
    }

    public void setCropImagePath(int action, String path){
        File file = new File(path);
        if(file == null || !file.exists()){
            Log.w(TAG, "setImagePath: file not exists");
            return ;
        }
        originalBitmap = getPathBitmap(file);

        if(originalBitmap == null){
            return ;
        }

        mImageWidth = originalBitmap.getWidth() ;
        mImageHeight = originalBitmap.getHeight() ;

        if(action == MosaicActivity.TYPE_PICK){
            inPath = path ;
            String fileName = file.getName();
            String filePath = file.getParent();

            int index = fileName.lastIndexOf(".") ;
            String name = fileName.substring(0, index);

            String newName = name + "_mosaic";
            fileName.replace(name, newName);

            outPath = filePath + File.separator + fileName ;
            Log.d(TAG, "setImagePath: -------outPath = "+outPath);
        }

        baseLayerBitmap = originalBitmap ;
        baseLayerDrawable = new BitmapDrawable(getResources(), baseLayerBitmap);
        notifyRefresh();
//        originalDrawable = new BitmapDrawable(getResources(), originalBitmap);
    }
    public void notifyRefresh() {
        Uri uri = Uri.fromFile(new File(inPath));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(mContext.getContentResolver()
                    .openInputStream(uri), null, options);
            if (options.outWidth >= 4096 || options.outHeight >= 4096) {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        requestLayout();
        invalidate();
    }

    public void userOriginalImage(){

    }

    private Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        if(bitmap == null){
            return bitmap;
        }

        Matrix matrix = new Matrix();

        matrix.setRotate(degree, bitmap.getWidth(), bitmap.getHeight());
        
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
        bitmap.recycle();

        return bitmap1 ;
    }

    private int rotateDegree ;

    public void rotateLeftBitmap(){
        baseLayerBitmap = rotateBitmap(baseLayerBitmap, -90);
        baseLayerDrawable = new BitmapDrawable(getResources(), baseLayerBitmap);

        mImageWidth = baseLayerBitmap.getWidth();
        mImageHeight = baseLayerBitmap.getHeight();

        isFirst = true ;

        rotateDegree += -90 ;

        refreshRotateBitmap();
    }

    public void rotateRightBitmap(){

        baseLayerBitmap = rotateBitmap(baseLayerBitmap, 90);
        baseLayerDrawable = new BitmapDrawable(getResources(), baseLayerBitmap);

        mImageWidth = baseLayerBitmap.getWidth();
        mImageHeight = baseLayerBitmap.getHeight();

        Log.d(TAG, "rotateRightBitmap: mImageWidth = "+mImageWidth);
        Log.d(TAG, "rotateRightBitmap: mImageHeight = "+mImageHeight);

        isFirst = true ;

        rotateDegree += 90 ;

        refreshRotateBitmap();
    }

    /**
     * requestLayout() 只有在布局发生变化后才走onDraw()方法，所以这里手动调用invalidate()
     */
    private void refreshRotateBitmap(){
        coverLayerBitmap = getCoverLayer();
        coverLayerDrawable = new BitmapDrawable(getResources(), coverLayerBitmap);
        requestLayout();
        invalidate();
    }

    /**
     * 图片已经是处理过得图片无需再进行处理
     * @param file
     * @return
     */
    private Bitmap getPathBitmap(File file) {
        Bitmap bitmap = null;
        Uri uri = Uri.fromFile(file);
        try {
            bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bitmap ;
    }

    public Bitmap getCoverLayer(){
        Bitmap bitmap = null;
        if(mEffect == Effect.GRID){
            bitmap = getGridMosaic();
        } else if(mEffect == Effect.COLOR){
            bitmap = getColorMosaic();
        } else if(mEffect == Effect.BLUR){
            bitmap = getBlurMosaic() ;
        }
        return bitmap;
    }

    private Bitmap getBlurMosaic() {
        if (mImageWidth <= 0 || mImageHeight <= 0) {
            return null;
        }

        if (baseLayerBitmap == null) {
            return null;
        }
        Bitmap bitmap = null;
//        BitmapUtil.blur(baseLayerBitmap)
        return bitmap;
    }

    private Bitmap getColorMosaic() {
        if (mImageWidth <= 0 || mImageHeight <= 0) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(mImageWidth, mImageHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Rect rect = new Rect(0, 0, mImageWidth, mImageHeight);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mMosaicColor);
        canvas.drawRect(rect, paint);
//        canvas.save();
        return bitmap;
    }

    private Bitmap getGridMosaic() {
        if(mImageWidth <= 0 || mImageHeight <= 0){
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(mImageWidth, mImageHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);

        int horCount = (int) Math.ceil(mImageWidth * 1.0f / mGridWidth);
        int verCount = (int) Math.ceil(mImageHeight * 1.0f / mGridWidth);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        for (int horIndex = 0; horIndex < horCount; ++horIndex) {
            for (int verIndex = 0; verIndex < verCount; ++verIndex) {
                int l = mGridWidth * horIndex;
                int t = mGridWidth * verIndex;
                int r = l + mGridWidth;
                if (r > mImageWidth) {
                    r = mImageWidth;
                }
                int b = t + mGridWidth;
                if (b > mImageHeight) {
                    b = mImageHeight;
                }
                //int color = bmBaseLayer.getPixel(l, t);
                Rect rect = new Rect(l, t, r, b);
                paint.setColor(Color.WHITE);
                canvas.drawRect(rect, paint);
            }
        }
//        canvas.save();
        return bitmap;
    }

    public boolean saveWithPath(String path, boolean isSaveDirect){
        if(isSaveDirect){
            path = outPath ;
        }

        Bitmap bitmap ;
        if(baseLayerDrawable == null){
            return false ;
        }

        Rect displayRect = ImageViewUtil.getBitmapRectCenterInside(baseLayerBitmap, this);

        int actualImageWidth = baseLayerBitmap.getWidth();
        float scaleFactorWidth = actualImageWidth * 1.0f / displayRect.width() ;

        int actualImageHeight = baseLayerBitmap.getHeight() ;
        float scaleFactorHeight = actualImageHeight * 1.0f / displayRect.height();

        final float cropWindowX = mFloatRect.left - displayRect.left;
        final float cropWindowY = mFloatRect.top - displayRect.top;
        final float cropWindowWidth = mFloatRect.width();
        final float cropWindowHeight = mFloatRect.height();

        float actualCropX = cropWindowX * scaleFactorWidth;
        float actualCropY = cropWindowY * scaleFactorHeight;
        float actualCropWidth = cropWindowWidth * scaleFactorWidth;
        float actualCropHeight = cropWindowHeight * scaleFactorHeight;
        if (actualCropX < 0) {
            actualCropWidth += actualCropX;
            actualCropX = 0;
        }

        if (actualCropY < 0) {
            actualCropHeight += actualCropY;
            actualCropY = 0;
        }

        if (actualCropX + actualCropWidth > baseLayerBitmap.getWidth()) {
            actualCropWidth = baseLayerBitmap.getWidth() - actualCropX;
        }

        if (actualCropY + actualCropHeight > baseLayerBitmap.getHeight()) {
            actualCropHeight = baseLayerBitmap.getHeight() - actualCropY;
        }

        bitmap = Bitmap.createBitmap(baseLayerBitmap,
                (int) actualCropX,
                (int) actualCropY,
                (int) actualCropWidth,
                (int) actualCropHeight);

        FileOutputStream fos ;
        try {
            fos = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true ;
    }

    private int dp2Px(int dp){
      int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
        return value ;
    }
}
