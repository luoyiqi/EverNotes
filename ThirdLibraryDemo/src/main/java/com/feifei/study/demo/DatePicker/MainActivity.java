package com.feifei.study.demo.DatePicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.evernote.demo.DemoActivityMain;
import com.evernote.demo.DemoJava;
import com.feifei.study.R;
import com.feifei.study.demo.RefreshView.RefreshDemo;
import com.feifei.study.demo.RefreshView.RefreshGridActivity;
import com.feifei.study.demo.RefreshView.RefreshListActivity;
import com.feifei.study.demo.RefreshView.RefreshSlideActivity;
import com.feifei.study.demo.SlidingMenu.ExampleListActivity;
import com.feifei.study.demo.SuperToast.ToastActivity;

import java.util.Date;

public class MainActivity extends Activity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
    }

    //还有一种快速方法就是给每个activity加上main-action，在manifest里面
    public void demo (View view) {
        switch (view.getId ()) {
            case R.id.button1:
                startActivity (new Intent (this, ExampleListActivity.class));
                break;
            case R.id.button2:
                startActivity (new Intent (this, ToastActivity.class));
                break;
            case R.id.button3:
                startActivity (new Intent (this, RefreshGridActivity.class));
                break;
            case R.id.button4:
                startActivity (new Intent (this, RefreshListActivity.class));
                break;
            case R.id.button5:
                startActivity (new Intent (this, RefreshDemo.class));
                break;
            case R.id.button6:
                DatePickerUtil.dialogPicker (this, new DatePickerUtil.ObtainCityListener () {
                    @Override
                    public void obtainCity (String city) {
                        Tools.toast (MainActivity.this, city);
                    }
                });
                break;

            case R.id.button7:
                DatePickerUtil.dialogDate (MainActivity.this, new DatePickerUtil.ObtainDateListener () {
                    @Override
                    public void obtainDate (Date start, Date end) {
                        Tools.toast (MainActivity.this, start.toString () + "和" + end.toString ());
                    }
                });
                break;
            case R.id.button8:
                DatePickerUtil.dialogCity (MainActivity.this, new DatePickerUtil.ObtainCityListener () {
                    @Override
                    public void obtainCity (String city) {
                        Tools.toast (MainActivity.this, city);
                    }
                });
                break;
            case R.id.button9:
                startActivity (new Intent (this, DemoActivityMain.class));
                break;
            case R.id.button10:
                startActivity (new Intent (this, DemoJava.class));
            case R.id.button11:
                startActivity (new Intent (this, RefreshSlideActivity.class));
                break;
        }
    }

}
