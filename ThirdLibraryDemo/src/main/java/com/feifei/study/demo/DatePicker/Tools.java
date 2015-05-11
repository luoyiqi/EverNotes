package com.feifei.study.demo.DatePicker;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feifei.study.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Tools {

    /**
     * @param context
     * @param dp
     * @return int
     * @notice DP转换为PX
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * @param context
     * @param px
     * @return int
     * @notice PX转换为DP
     */
    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * @param context
     * @return int
     * @notice 获取屏幕的宽度
     */
    public static int getWidth(Context context) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(mDisplayMetrics);
        return mDisplayMetrics.widthPixels;
    }

    /**
     * @param context
     * @return int
     * @notice 获取屏幕的高度
     */
    public static int getHeight(Context context) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(mDisplayMetrics);
        return mDisplayMetrics.heightPixels;
    }

    /**
     * @param context
     * @return float
     * @notice 获取屏幕的密度
     */
    public static float getDensity(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(metric);
        return metric.density;
    }


    /**
     * @param context
     * @return boolean
     * @notice 判断是否为平板
     */
    public static boolean isPad(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        return screenInches >= 7.0 ? true : false;
    }

    /**
     * @param context
     * @return boolean
     * @notice 判断当前屏幕是否是横屏
     */
    public static boolean isLandscape(Context context) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return 年份int
     * @notice 获取年份
     */
    public static int getYear() {
        String Time = null;
        SimpleDateFormat newSimpleDate = new SimpleDateFormat("yyyy",
                Locale.getDefault());
        Time = newSimpleDate.format(new Date());
        return Integer.valueOf(Time);
    }

    /**
     * @return 月份int
     * @notice 获取月份
     */
    public static int getMonth() {
        String Time = null;
        SimpleDateFormat newSimpleDate = new SimpleDateFormat("MM",
                Locale.getDefault());
        Time = newSimpleDate.format(new Date());
        return Integer.valueOf(Time);
    }

    /**
     * @return 日期int
     * @notice 获取日期
     */
    public static int getDay() {
        String Time = null;
        SimpleDateFormat newSimpleDate = new SimpleDateFormat("dd",
                Locale.getDefault());
        Time = newSimpleDate.format(new Date());
        return Integer.valueOf(Time);
    }

    public static void toast(final Context context, final String string) {
        int dp10 = Tools.dp2px(context, 10);
        int dp5 = Tools.dp2px(context, 5);
        final int IMAGEID = 0x000012;

        RelativeLayout layout = new RelativeLayout(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(dp10, dp10, dp10, dp10);
        layout.setPadding(dp10, dp10, dp10, dp10);
        layout.setBackgroundResource(R.drawable.white_blue_line);
        layout.setLayoutParams(params);

        TextView textView = new TextView(context);
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        // 设置marginleft
        params3.setMargins(dp10, 0, 0, 0);
        params3.addRule(RelativeLayout.CENTER_VERTICAL);
        // 蓝色
        textView.setTextColor(Color.parseColor("#0b6aff"));
        textView.setTextSize(18);
        textView.setPadding(0, 0, dp10, 0);
        textView.setMaxLines(5);
        textView.setLayoutParams(params3);
        layout.addView(textView);
        Toast toast = new Toast(context);
        textView.setText("" + string);

        toast.setView(layout);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
