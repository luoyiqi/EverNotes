package com.feifei.study.demo.DatePicker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feifei.study.R;

import java.util.Calendar;
import java.util.Date;

import feifei.library.datepicker.city.CityPicker;
import feifei.library.datepicker.city.CityPicker2;
import feifei.library.datepicker.date.DatePickerDialog;

/**
 * 自定义日期选择器样式的dialog
 */
public class DatePickerUtil {


    public interface ObtainDateListener {
        public void obtainDate(Date start, Date end);
    }

    /**
     * @param context
     * @param obtainDateListener
     * @notice 获取日期选择器，从7天前到今天
     */
    public static void dialogDate(final Context context,
                                  final ObtainDateListener obtainDateListener) {
        final Calendar startcalendar = Calendar.getInstance();
        final Calendar endcalendar = Calendar.getInstance();
        final Dialog dialog_search = new Dialog(context,
                R.style.dialog_white_style);
        View dialogSearch = LayoutInflater.from(context).inflate(
                R.layout.feifei_dialog_date, null);
        final Button timeStart = (Button) dialogSearch
                .findViewById(R.id.dialog_search_start);
        final Button timeEnd = (Button) dialogSearch
                .findViewById(R.id.dialog_search_end);
        startcalendar.set(com.feifei.study.demo.DatePicker.Tools.getYear(), Tools.getMonth() - 1,
                Tools.getDay() - 7);
        // endcalendar.set(TimeUtil.getYear(), TimeUtil.getMonth() - 1,
        // TimeUtil.getDay());
        timeStart.setText(startcalendar.get(Calendar.YEAR) + "."
                + (startcalendar.get(Calendar.MONTH) + 1) + "."
                + (startcalendar.get(Calendar.DAY_OF_MONTH)));
        timeEnd.setText(endcalendar.get(Calendar.YEAR) + "."
                + (endcalendar.get(Calendar.MONTH) + 1) + "."
                + (endcalendar.get(Calendar.DAY_OF_MONTH)));

        final Button searchButton = (Button) dialogSearch
                .findViewById(R.id.dialog_search_confirm);
        // dialog_search.setCanceledOnTouchOutside(false);
        if (Tools.isPad(context) && Tools.isLandscape(context)) {
            dialog_search.setContentView(
                    dialogSearch,
                    new ViewGroup.LayoutParams((int) (Tools
                            .getWidth(context) * 0.5),
                            ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            dialog_search.setContentView(
                    dialogSearch,
                    new ViewGroup.LayoutParams((int) (Tools
                            .getWidth(context) * 0.8),
                            ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        timeStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog = DatePickerDialog
                        .newInstance(new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(
                                    DatePickerDialog datePickerDialog,
                                    int year, int month, int day) {
                                timeStart.setText(year + "." + (month + 1)
                                        + "." + day);
                                startcalendar.set(year, month, day);
                            }
                        }, startcalendar.get(Calendar.YEAR), startcalendar
                                .get(Calendar.MONTH), startcalendar
                                .get(Calendar.DAY_OF_MONTH), true);

                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.show(
                        ((Activity) context).getFragmentManager(), "datepicker");
            }
        });

        timeEnd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog2 = DatePickerDialog
                        .newInstance(new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(
                                    DatePickerDialog datePickerDialog,
                                    int year, int month, int day) {
                                timeEnd.setText(year + "." + (month + 1) + "."
                                        + day);
                                endcalendar.set(year, month, day);
                            }
                        }, endcalendar.get(Calendar.YEAR), endcalendar
                                .get(Calendar.MONTH), endcalendar
                                .get(Calendar.DAY_OF_MONTH), true);

                datePickerDialog2.setYearRange(1985, 2028);
                datePickerDialog2.show(
                        ((Activity) context).getFragmentManager(), "datepicker");
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (startcalendar.getTime().before(endcalendar.getTime())) {
                        dialog_search.dismiss();
                        if (obtainDateListener != null) {
                            obtainDateListener.obtainDate(
                                    startcalendar.getTime(),
                                    endcalendar.getTime());

                        }
                    } else {
                        Tools.toast(context, "日期顺序错误，请重试");
                    }
                } catch (Exception e) {
                    Tools.toast(context, "请选择日期");
                }
            }
        });
        dialog_search.show();
    }

    public interface ObtainCityListener {
        public void obtainCity(String city);
    }

    /**
     * @param context
     * @param obtainCityListener ：获取城市的listener
     * @notice 城市选择器
     */
    public static void dialogCity(final Context context,
                                  final ObtainCityListener obtainCityListener) {
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                final Dialog dialog = new Dialog(context, R.style.dialog_white_style);
                View pickerCity = LayoutInflater.from(context).inflate(
                        R.layout.feifei_dialog_city, null);
                final CityPicker cityPicker = (CityPicker) pickerCity
                        .findViewById(R.id.yuntu_dialog_city_include);
                TextView textView = (TextView) pickerCity.findViewById(R.id.yuntu_dialog_city_button);
                textView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        dialog.dismiss();
                        if (obtainCityListener != null) {
                            obtainCityListener.obtainCity(cityPicker.getCity());
                        }
                    }
                });
                dialog.setContentView(
                        pickerCity,
                        new LinearLayout.LayoutParams((int) (Tools
                                .getWidth(context) * 0.8),
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                dialog.show();
            }
        });
    }

    public static void dialogPicker(final Context context,
                                    final ObtainCityListener obtainCityListener) {
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                final Dialog dialog = new Dialog(context, R.style
                        .dialog_white_style);
                View pickerCity = LayoutInflater.from(context).inflate(
                        R.layout.feifei_dialog_city2, null);
                final CityPicker2 cityPicker2 = (CityPicker2) pickerCity
                        .findViewById(R.id.yuntu_dialog_city_include);
                TextView textView = (TextView) pickerCity.findViewById(R.id.yuntu_dialog_city_button);
                textView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        dialog.dismiss();
                        if (obtainCityListener != null) {
                            obtainCityListener.obtainCity(cityPicker2.getCity());
                        }
                    }
                });
                dialog.setContentView(
                        pickerCity,
                        new LinearLayout.LayoutParams((int) (Tools
                                .getWidth(context) * 0.8),
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                dialog.show();
            }
        });
    }


}
