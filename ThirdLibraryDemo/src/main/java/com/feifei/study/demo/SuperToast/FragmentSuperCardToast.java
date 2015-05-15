package com.feifei.study.demo.SuperToast;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.feifei.study.R;

import feifei.library.supertoast.OnClickWrapper;
import feifei.library.supertoast.OnDismissWrapper;
import feifei.library.supertoast.Style;
import feifei.library.supertoast.SuperActivityToast;
import feifei.library.supertoast.SuperCardToast;
import feifei.library.supertoast.SuperToast;
import feifei.library.supertoast.Wrappers;

/**
 * SuperCardToast这个类最全了，比SuperActivityToast样式变了，加了一个点击或滑动删除toast的功能
 * card有个消失的效果是因为layout中有占位
 * 全局的是SuperToast，不依赖activity，使用参考demo()方法
 */
public class FragmentSuperCardToast extends Fragment {

    Spinner mAnimationSpinner;
    Spinner mDurationSpinner;
    Spinner mBackgroundSpinner;
    Spinner mTextsizeSpinner;
    Spinner mDismissFunctionSpinner;
    RadioGroup mTypeRadioGroup;
    CheckBox mImageCheckBox;
    CheckBox mDismissCheckBox;

    DummyOperation mDummyOperation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toast,
                container, false);
        // Wrappers添加监听器是为了适应旋转
        Wrappers wrappers = new Wrappers();
        wrappers.add(onClickWrapper);
        wrappers.add(onDismissWrapper);
        //想让监听也适应旋转则也要传进去
        SuperCardToast.onRestoreState(savedInstanceState, getActivity(),
                wrappers);

        mAnimationSpinner = (Spinner) view.findViewById(R.id.animationSpinner);
        mTypeRadioGroup = (RadioGroup) view.findViewById(R.id.type_radiogroup);
        mDurationSpinner = (Spinner) view.findViewById(R.id.duration_spinner);
        mBackgroundSpinner = (Spinner) view
                .findViewById(R.id.background_spinner);
        mTextsizeSpinner = (Spinner) view.findViewById(R.id.textsize_spinner);
        mDismissFunctionSpinner = (Spinner) view
                .findViewById(R.id.dismissfunction_spinner);
        mImageCheckBox = (CheckBox) view.findViewById(R.id.imageCheckBox);
        mDismissCheckBox = (CheckBox) view.findViewById(R.id.dismiss_checkbox);
        final Button showButton = (Button) view.findViewById(R.id.showButton);
        showButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showSuperCardToast();
            }
        });
        //这里长按跳转是为了测试该toast依赖activity
        showButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(getActivity(), UndoBar.class));
                return false;
            }
        });
        showButton.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                SuperActivityToast superActivityToast = new SuperActivityToast(getActivity(), SuperToast.Type.BUTTON);
                superActivityToast.setAnimations(SuperToast.Animations.POPUP);
//                superActivityToast.setBackground(SuperToast.Background.BLACK);
                superActivityToast.setButtonText("撤销");
                superActivityToast.setButtonTextSize(20);
                superActivityToast.setButtonTextColor(Color.parseColor("#ff9933"));
                superActivityToast.setButtonIcon(R.drawable.icon_light_undo);
                superActivityToast.setText("已删除");
                superActivityToast.setTextColor(Color.WHITE);
                superActivityToast.setTextSize(20);
                superActivityToast.show();
                return false;
            }
        });
        return view;

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDummyOperation != null) {
            if (mDummyOperation.getStatus() == AsyncTask.Status.PENDING
                    || mDummyOperation.getStatus() == AsyncTask.Status.RUNNING) {
                mDummyOperation.cancel(true);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SuperCardToast.onSaveState(outState);
    }

    private void showSuperCardToast() {
        final SuperCardToast superCardToast;
        //通过RadioGroup获取值
        switch (mTypeRadioGroup.getCheckedRadioButtonId()) {
            case R.id.toast_radiobutton:
                superCardToast = new SuperCardToast(getActivity(),
                        SuperToast.Type.STANDARD);
                superCardToast.setAnimations(SuperToast.Animations.POPUP);
                break;
            case R.id.button_radiobutton:
                superCardToast = new SuperCardToast(getActivity(),
                        SuperToast.Type.BUTTON);
                superCardToast.setAnimations(SuperToast.Animations.SCALE);
                superCardToast.setOnClickWrapper(onClickWrapper);
                break;
            case R.id.progress_radiobutton:
                superCardToast = new SuperCardToast(getActivity(),
                        SuperToast.Type.PROGRESS);
                superCardToast.setAnimations(SuperToast.Animations.FADE);
                break;
            case R.id.hprogress_radiobutton:
                superCardToast = new SuperCardToast(getActivity(),
                        SuperToast.Type.PROGRESS_HORIZONTAL);
                superCardToast.setAnimations(SuperToast.Animations.FLYIN);
                superCardToast.setIndeterminate(true);
                mDummyOperation = new DummyOperation(superCardToast);
                mDummyOperation.execute();
                break;
            default:
                superCardToast = new SuperCardToast(getActivity(),
                        SuperToast.Type.STANDARD);
                break;
        }
        //通过spinner获取值，spinner的数据在string数组里面，XML使用entries属性，还有android:spinnerMode="dropdown"
        switch (mAnimationSpinner.getSelectedItemPosition()) {
            case 0:
                superCardToast.setAnimations(SuperToast.Animations.FADE);
                break;
            case 1:
                superCardToast.setAnimations(SuperToast.Animations.FLYIN);
                break;
            case 2:
                superCardToast.setAnimations(SuperToast.Animations.POPUP);
                break;
            case 3:
                superCardToast.setAnimations(SuperToast.Animations.SCALE);
                break;
            case 4:
                //加了一个土豆toast的demo
                superCardToast.setAnimations(SuperToast.Animations.BOTTOM);
                break;
        }
        switch (mDurationSpinner.getSelectedItemPosition()) {
            case 0:
                superCardToast.setDuration(SuperToast.Duration.SHORT);
                break;
            case 1:
                superCardToast.setDuration(SuperToast.Duration.MEDIUM);
                break;
            case 2:
                superCardToast.setDuration(SuperToast.Duration.LONG);
                break;
        }
        switch (mBackgroundSpinner.getSelectedItemPosition()) {
            case 0:
                superCardToast.setBackground(SuperToast.Background.BLACK);
                break;
            case 1:
                superCardToast.setBackground(SuperToast.Background.GRAY);
                break;
            case 2:
                superCardToast.setBackground(SuperToast.Background.GREEN);
                break;
            case 3:
                superCardToast.setBackground(SuperToast.Background.BLUE);
                break;
            case 4:
                superCardToast.setBackground(SuperToast.Background.RED);
                break;
            case 5:
                superCardToast.setBackground(SuperToast.Background.PURPLE);
                break;
            case 6:
                superCardToast.setBackground(SuperToast.Background.ORANGE);
                break;
        }
        switch (mTextsizeSpinner.getSelectedItemPosition()) {
            case 0:
                superCardToast.setTextSize(SuperToast.TextSize.SMALL);
                break;
            case 1:
                superCardToast.setTextSize(SuperToast.TextSize.SMALL);
                break;
            case 2:
                superCardToast.setTextSize(SuperToast.TextSize.LARGE);
                break;
        }
        //设置消除toast的方式
        switch (mDismissFunctionSpinner.getSelectedItemPosition()) {
            case 0:
                break;
            case 1:
                superCardToast.setSwipeToDismiss(true);
                break;
            case 2:
                superCardToast.setTouchToDismiss(true);
                break;
        }
        if (mImageCheckBox.isChecked()) {
            superCardToast.setIcon(SuperToast.Icon.Dark.INFO, SuperToast.IconPosition.LEFT);
        }
        if (mDismissCheckBox.isChecked()) {
            superCardToast.setOnDismissWrapper(onDismissWrapper);
        }
        superCardToast.show();
    }

    private final OnClickWrapper onClickWrapper = new OnClickWrapper(
            "onclickwrapper_one", new SuperToast.OnClickListener() {

        @Override
        public void onClick(View v, Parcelable token) {
            SuperToast superToast = new SuperToast(v.getContext());
            superToast.setText("onClick!");
            superToast.setDuration(SuperToast.Duration.VERY_SHORT);
            superToast.setBackground(SuperToast.Background.BLUE);
            superToast.setTextColor(Color.WHITE);
            superToast.show();
        }

    });

    private final OnDismissWrapper onDismissWrapper = new OnDismissWrapper(
            "ondismisswrapper_one", new SuperToast.OnDismissListener() {

        @Override
        public void onDismiss(View view) {
            SuperToast superToast = new SuperToast(view.getContext());
            superToast.setText("onDismiss!");
            superToast.setDuration(SuperToast.Duration.VERY_SHORT);
            superToast.setBackground(SuperToast.Background.RED);
            superToast.setTextColor(Color.WHITE);
            superToast.show();
        }
    });

    private class DummyOperation extends AsyncTask<Void, Integer, Void> {
        SuperCardToast mSuperCardToast;

        //通过构造函数传参
        public DummyOperation(SuperCardToast superCardToast) {
            this.mSuperCardToast = superCardToast;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < 11; i++) {
                try {
                    Thread.sleep(250);
                    onProgressUpdate(i * 10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            if (mSuperCardToast != null) {
                mSuperCardToast.dismiss();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            if (mSuperCardToast != null) {
                mSuperCardToast.setProgress(progress[0]);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            SuperCardToast.cancelAllSuperCardToasts();
        }
    }

    //这是一个演示
    public void demo1() {
        //这是一种写法，多个set方法
        SuperToast superToast = new SuperToast(getActivity());
        superToast.setAnimations(SuperToast.Animations.FADE);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setBackground(SuperToast.Background.BLACK);
        superToast.setTextSize(SuperToast.TextSize.SMALL);
        superToast.setTextColor(Color.WHITE);
        superToast.setIcon(R.mipmap.ic_launcher,
                SuperToast.IconPosition.LEFT);
        superToast.setOnDismissListener(new SuperToast.OnDismissListener() {
            @Override
            public void onDismiss(View view) {
                Log.e("SuperToast", "On Dismiss");
            }
        });
        superToast.show();
    }

    //这是另一种方法，使用Style类，里面很多的枚举变量
    public void demo2() {
        Style customStyle = new Style();
        customStyle.animations = SuperToast.Animations.POPUP;
        customStyle.background = SuperToast.Background.PURPLE;
        customStyle.textColor = Color.WHITE;
        customStyle.buttonTextColor = Color.LTGRAY;
        customStyle.dividerColor = Color.WHITE;

        SuperToast superToast = new SuperToast(getActivity(), customStyle);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setText("这是另一种写法");
        superToast.show();
    }

    public void demo3() {
        SuperCardToast superCardToast = new SuperCardToast(getActivity(), Style.getStyle(Style.GREEN));
        superCardToast.setDuration(SuperToast.Duration.SHORT);
        superCardToast.setText("这是另一种写法");
        superCardToast.show();
    }

    //类似系统Toast的写法
    public void demo4() {
        SuperToast.create(getActivity(), "你好", SuperToast.Duration.MEDIUM, Style.getStyle(Style.BLUE, SuperToast.Animations.POPUP)).show();
    }
}