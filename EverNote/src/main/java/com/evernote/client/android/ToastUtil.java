package com.evernote.client.android;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.evernote.sdk.R;

/**
 * 裴智飞 2014-7-28 下午8:12:26
 * 自定义布局的toast，纯代码写布局，线程里面显示需要自己加runOnUiThread()，高级功能请用ToastCustom
 * toast的时间只有2.5和3秒两种，所以时间没必要作为一个选项去定义，想改系统toast时间需要反射
 * http://blog.csdn.net/jiangwei0910410003/article/details/25540037
 * toast是使用了队列的，使用WindowManager是可以独立于Activity显示的
 * 自己写的toast可以自定义动画：http://blog.csdn.net/zhangweiwtmdbf/article/details/30031015
 */

public class ToastUtil {
    //这里没有公开是为了方便
    private final static int IMAGE_NOIMAGE = 0;

    /**
     * 显示纯文字消息
     *
     * @param context
     * @param string
     */
    public static void toast (Context context, CharSequence string) {
        toastShow (context, string, IMAGE_NOIMAGE);
    }

    public static void toast (Context context, int string) {
        toastShow (context, context.getResources ().getString (string), IMAGE_NOIMAGE);
    }

    /**
     * imageType 0：无；1：正常；2：成功；3：警告
     * 显示带图标的消息，图标自定义，CharSequence支持样式
     *
     * @param context
     * @param string
     * @param imageType 0：无；1：正常；2：成功；3：警告
     */
    public static void toast (Context context, CharSequence string, final int imageType) {
        toastShow (context, string, imageType);
    }

    public static void toast (Context context, int string, final int imageType) {
        toastShow (context, context.getResources ().getString (string), imageType);
    }

    @SuppressWarnings("ResourceType")
    private static void toastShow (final Context context, final CharSequence string,
                                   final int imageType) {
        Toastlayout toastLayout = Toastlayout.toastlayout (context);
        RelativeLayout layout = toastLayout.getLayout ();
        TextView textView = toastLayout.getTextView ();
        ImageView imageView = toastLayout.getImageView ();
        //这里没有使用makeToast来生成实例
        Toast toast = new Toast (context);
//        textView.setText("" + string);//这种不能生成样式文本
        textView.setText (TextUtils.isEmpty (string) ? "" : string);
        imageView.setVisibility (View.GONE);
        toast.setView (layout);
        toast.setGravity (Gravity.BOTTOM, 0, 200);
        toast.setDuration (Toast.LENGTH_SHORT);
        toast.show ();
    }
}

//之前两个Toast的布局一样，写一个通用类可以重用，但涉及到静态
class Toastlayout {
    Context context;
    RelativeLayout layout;
    ImageView imageView;
    TextView textView;

    public RelativeLayout getLayout () {
        return layout;
    }

    public ImageView getImageView () {
        return imageView;
    }

    public TextView getTextView () {
        return textView;
    }

    public Toastlayout (Context context) {
        this.context = context;
        //相对布局需要的id，最好使用xml中的id
        final int IMAGEID = 0x000012;

        //设置布局
        layout = new RelativeLayout (context);
        RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams (
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParam.addRule (RelativeLayout.CENTER_VERTICAL);
        layoutParam.addRule (RelativeLayout.CENTER_HORIZONTAL);
        layout.setGravity (Gravity.CENTER);
        layout.setPadding (12, 12, 12, 12);
//        layout.setBackgroundResource(R.drawable.white_blue_line);
        layout.setBackgroundResource (R.drawable.toast_dark);
        layout.setLayoutParams (layoutParam);

        //设置图片
        imageView = new ImageView (context);
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams (
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        imageParams.setMargins (5, 0, 0, 0);
        imageParams.addRule (RelativeLayout.CENTER_VERTICAL);
        imageView.setLayoutParams (imageParams);
        //noinspection ResourceType
        imageView.setId (IMAGEID);

        //设置文本
        textView = new TextView (context);
        RelativeLayout.LayoutParams textParam = new RelativeLayout.LayoutParams (
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParam.setMargins (5, 0, 0, 0);
        textParam.addRule (RelativeLayout.CENTER_VERTICAL);
        textParam.addRule (RelativeLayout.RIGHT_OF, IMAGEID);
        textView.setTextColor (Color.parseColor ("#0b6aff"));
        textView.setTextSize (20);
        textView.setPadding (0, 0, 5, 0);
        textView.setMaxLines (3);
        textView.setLayoutParams (textParam);
        layout.addView (imageView);
        layout.addView (textView);
    }

    //静态类也是可以用的
    public static Toastlayout toastlayout (Context context) {
        Toastlayout toastlayout = new Toastlayout (context);
        return toastlayout;
    }

}

