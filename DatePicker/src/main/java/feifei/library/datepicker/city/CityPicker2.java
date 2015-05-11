package feifei.library.datepicker.city;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.ArrayList;

import feifei.library.datepicker.R;


/**
 * @author 裴智飞
 * @date 2014-8-6
 * @date 上午10:47:29
 * @file CityPicker.java
 * @content 城市选择器，拷贝"feifei-json/area.json"到assert下面
 */
public class CityPicker2 extends LinearLayout {
    /**
     * 滑动控件
     */
    private ScrollerNumberPicker provincePicker;
    /**
     * 选择监听
     */
    private OnSelectingListener onSelectingListener;
    /**
     * 刷新界面
     */
    private static final int REFRESH_VIEW = 0x001;
    /**
     * 临时日期
     */
    private int tempProvinceIndex = -1;
    private final ArrayList<String> province_list = new ArrayList<String>();

    public CityPicker2(Context context, AttributeSet attrs) {
        super(context, attrs);
        getaddressinfo();
    }

    public CityPicker2(Context context) {
        super(context);
        getaddressinfo();
    }

    // 获取城市信息
    private void getaddressinfo() {
        // province_list = "";
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.picker_city_dialog2,
                this);
        // 获取控件引用
        provincePicker = (ScrollerNumberPicker) findViewById(R.id.province);
        province_list.add("1");
        province_list.add("2");
        province_list.add("3");
        province_list.add("4");
        province_list.add("5");
        province_list.add("6");
        province_list.add("7");
        provincePicker.setData(province_list);
        provincePicker.setDefault(1);
        provincePicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                System.out.println("id-->" + id + "text----->" + text);
                if (text.equals("") || text == null) {
                    return;
                }
                if (tempProvinceIndex != id) {
                    int lastDay = Integer.valueOf(provincePicker.getListSize());
                    if (id > lastDay) {
                        provincePicker.setDefault(lastDay - 1);
                    }
                }
                tempProvinceIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    if (onSelectingListener != null) {
                        onSelectingListener.selected(true);
                    }
                    break;
            }
        }

    };

    public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
        this.onSelectingListener = onSelectingListener;
    }

    public String getCity() {
        return provincePicker.getSelectedText();
    }

    public interface OnSelectingListener {

        public void selected(boolean selected);
    }
}
