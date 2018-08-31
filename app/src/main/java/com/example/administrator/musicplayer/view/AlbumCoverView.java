package com.example.administrator.musicplayer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.musicplayer.R;
import com.example.administrator.musicplayer.application.AppCache;
import com.example.administrator.musicplayer.bean.Music;

/**
 * Created by Administrator on 2017/5/27.
 */

public class AlbumCoverView extends View {
    private Handler mHandler = new Handler();
    private Drawable mTopLine;
    private int mTopLineSize;
    private Drawable mCoverBorder;
    private Point mDisPoint = new Point();
    private Point mCoverCenterPoint = new Point();
    private Point mNeedlePoint = new Point();
    private Bitmap mDisBitmap;
    private Bitmap mNeedleBitmap;
    private Point mCoverBorderPoint = new Point();
    private int mCoverBorderWidth;
    private Bitmap mCenterFace;
    private int disOffsetY;
    private Bitmap mDefaultFace;
    private float mDisRotation = 0.0f;
    private float mNeedleRotation = -30f;
    private Point mCenterCanvasPoint;
    private float mDisRotationPlus = 0.1f;

    public AlbumCoverView(Context context) {
        this(context, null);
    }

    public AlbumCoverView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlbumCoverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initSize();
    }


    public void init() {
        //顶部透明线
        mTopLine = getResources().getDrawable(R.drawable.play_pager_topline);
        //中间透明圆盘
        mCoverBorder = getResources().getDrawable(R.drawable.play_page_cover_border_shape);
        //黑胶
        mDisBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.play_page_disc);
        //指针
        mNeedleBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.play_page_needle);
        mDefaultFace = BitmapFactory.decodeResource(getResources(), R.mipmap.play_page_default_cover);
        setCenterFace();
    }

    /**
     * 设置封面
     */
    public void setCenterFace() {
        if (AppCache.getInstence().isOnNet()) {//如果在播放网络音乐
            mCenterFace = resizeImage(AppCache.getInstence().getNetFace(), mDefaultFace.getWidth(), mDefaultFace.getHeight());
            return;
        }
        try {
            //中心封面
            Music music = AppCache.getInstence().getCurrentMusic();
            if (music == null||music.getImagePath().equals("00")) {
                mCenterFace = mDefaultFace;
            } else {
                Bitmap face = BitmapFactory.decodeFile(music.getImagePath());
                mCenterFace = resizeImage(face, mDefaultFace.getWidth(), mDefaultFace.getHeight());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSize() {
        mNeedlePoint.x = getWidth() / 2 - mNeedleBitmap.getWidth() / 6;
        mNeedlePoint.y = -50;
        mCoverBorderWidth = 10;
        mTopLineSize = dp2px(1);
        disOffsetY = mNeedleBitmap.getHeight() / 2;
        mDisPoint.y = disOffsetY;
        mDisPoint.x = (getWidth() - mDisBitmap.getWidth()) / 2;
        mCoverBorderPoint.x = mDisBitmap.getWidth() + mCoverBorderWidth;
        mCoverBorderPoint.y = mCoverBorderPoint.x;
        mCoverCenterPoint.x = getWidth() - mCoverBorder.getIntrinsicWidth();
        mCoverCenterPoint.y = mDisPoint.y;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制中心封面
        setCircle(mCenterFace, canvas);
        //绘制顶部虚线
        mTopLine.setBounds(0, 0, getWidth(), mTopLineSize);
        mTopLine.draw(canvas);
        //绘制黑色胶皮外侧半透明边框
        mCoverBorder.setBounds(mDisPoint.x - mCoverBorderWidth, mDisPoint.y - mCoverBorderWidth, mCoverBorderPoint.x + mDisPoint.x, mCoverBorderPoint.y + mDisPoint.y);
        mCoverBorder.draw(canvas);
        //绘制圆盘
        Matrix disMatrix = new Matrix();
        disMatrix.setRotate(mDisRotation, mCenterCanvasPoint.x, mCenterCanvasPoint.y);
        disMatrix.preTranslate(mDisPoint.x, mDisPoint.y);
        canvas.drawBitmap(mDisBitmap, disMatrix, new Paint());
        //绘制指针
        Matrix needleMatrix = new Matrix();
        needleMatrix.setRotate(mNeedleRotation, mNeedlePoint.x + 46, 0);
        needleMatrix.preTranslate(mNeedlePoint.x, mNeedlePoint.y);
        canvas.drawBitmap(mNeedleBitmap, needleMatrix, new Paint());
    }


    /**
     * dp 转px
     * @param dpValue
     * @return
     */
    private int dp2px(float dpValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (scale * dpValue + 0.5f);
    }


    /**
     * 旋转的方法
     */
    public void roate() {
        mHandler.removeCallbacks(mDisRotationRunnable);
        mHandler.post(mDisRotationRunnable);
    }


    private Runnable mDisRotationRunnable = new Runnable() {
        @Override
        public void run() {
            if (AppCache.getInstence().isPlaying()||AppCache.getInstence().isOnNet()) {
                mDisRotation += mDisRotationPlus;
                if (mDisRotation >= 360) {
                    mDisRotation = 0;
                }
                if (mNeedleRotation <= 0) {
                    mNeedleRotation += 0.5f;
                }
            } else {
                if (mNeedleRotation >= -30f) {
                    mNeedleRotation -= 0.5f;
                }
            }
            invalidate();
            mHandler.postDelayed(this, 1);

        }
    };

    public void setCircle(Bitmap bitmap, Canvas canvas) {
        int minBitMap = Math.min(bitmap.getWidth(), bitmap.getHeight());
        int mRadius = minBitMap / 2;
        mCenterCanvasPoint = new Point();
        mCenterCanvasPoint.x = getWidth() / 2;
        mCenterCanvasPoint.y = mDisPoint.y + mDisBitmap.getWidth() / 2;
        Point bitmapPoint = new Point();
        bitmapPoint.x = mCenterCanvasPoint.x - mRadius;
        bitmapPoint.y = mCenterCanvasPoint.y - mRadius;
        Matrix matrix = new Matrix();
        //设置位置
        matrix.setRotate(mDisRotation, mCenterCanvasPoint.x, mCenterCanvasPoint.y);
        matrix.preTranslate(bitmapPoint.x, bitmapPoint.y);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        /**
         * 创建着色器 设置着色模式
         * TileMode的取值有三种：
         *  CLAMP 拉伸  REPEAT 重复   MIRROR 镜像
         */
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //设置矩阵
        shader.setLocalMatrix(matrix);
        paint.setShader(shader);
        canvas.drawCircle(mCenterCanvasPoint.x, mCenterCanvasPoint.y, mRadius, paint);
    }

    /**
     * 根据指定大小缩放图片
     *
     * @param source
     * @param w
     * @param h
     * @return
     */
    public Bitmap resizeImage(Bitmap source, int w, int h) {
        int width = source.getWidth();
        int height = source.getHeight();
        float scaleWidth = ((float) w) / width;
        float scaleHeight = ((float) h) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(source, 0, 0, width, height, matrix, true);
    }

    /**
     * 换封面
     */
    public void changeFace() {
        setCenterFace();
        invalidate();
    }

    /**
     * 重置旋转角度
     */
    public void resetRotate() {
        mDisRotation = 0;
    }
}
