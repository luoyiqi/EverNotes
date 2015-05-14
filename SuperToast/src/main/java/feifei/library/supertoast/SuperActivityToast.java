/**
 * Copyright 2014 John Persano
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package feifei.library.supertoast;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.LinkedList;

import feifei.library.supertoast.SuperToast.Animations;
import feifei.library.supertoast.SuperToast.IconPosition;
import feifei.library.supertoast.SuperToast.Type;

public class SuperActivityToast {

    private static final String TAG = "SuperActivityToast";

    private static final String ERROR_ACTIVITYNULL = " - You cannot pass a null Activity as a parameter.";
    private static final String ERROR_NOTBUTTONTYPE = " - is only compatible with BUTTON type SuperActivityToasts.";
    private static final String ERROR_NOTPROGRESSHORIZONTALTYPE = " - is only compatible with PROGRESS_HORIZONTAL type SuperActivityToasts.";
    private static final String ERROR_NOTEITHERPROGRESSTYPE = " - is only compatible with PROGRESS_HORIZONTAL or PROGRESS type SuperActivityToasts.";

    /*
     * Bundle tag with a hex as a string so it can't interfere with other tags
     * in the bundle
     */
    private static final String BUNDLE_TAG = "0x532e412e542e";

    private Activity mActivity;
    private Animations mAnimations = Animations.FADE;
    private boolean mIsIndeterminate;
    private boolean mIsTouchDismissible;
    private boolean isProgressIndeterminate;
    private boolean showImmediate;
    private Button mButton;
    private IconPosition mIconPosition;
    private int mDuration = SuperToast.Duration.SHORT;
    private int mBackground = Style.getBackground(Style.GRAY);
    private int mButtonIcon = SuperToast.Icon.Dark.UNDO;
    private int mDividerColor = Color.LTGRAY;
    private int mIcon;
    private int mTypefaceStyle = Typeface.NORMAL;
    private int mButtonTypefaceStyle = Typeface.BOLD;
    private LayoutInflater mLayoutInflater;
    private LinearLayout mRootLayout;
    private OnDismissWrapper mOnDismissWrapper;
    private OnClickWrapper mOnClickWrapper;
    private Parcelable mToken;
    private ProgressBar mProgressBar;
    private String mOnClickWrapperTag;
    private String mOnDismissWrapperTag;
    private TextView mMessageTextView;
    private Type mType = Type.STANDARD;
    private View mDividerView;
    private ViewGroup mViewGroup;
    private View mToastView;

    public SuperActivityToast(Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException(TAG + ERROR_ACTIVITYNULL);
        }
        this.mActivity = activity;
        mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewGroup = (ViewGroup) activity.findViewById(android.R.id.content);
        mToastView = mLayoutInflater.inflate(R.layout.supertoast, mViewGroup,
                false);
        mMessageTextView = (TextView) mToastView
                .findViewById(R.id.message_textview);
        mRootLayout = (LinearLayout) mToastView.findViewById(R.id.root_layout);
    }

    public SuperActivityToast(Activity activity, Style style) {
        if (activity == null) {
            throw new IllegalArgumentException(TAG + ERROR_ACTIVITYNULL);
        }
        this.mActivity = activity;
        mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewGroup = (ViewGroup) activity.findViewById(android.R.id.content);
        mToastView = mLayoutInflater.inflate(R.layout.supertoast, mViewGroup,
                false);
        mMessageTextView = (TextView) mToastView
                .findViewById(R.id.message_textview);
        mRootLayout = (LinearLayout) mToastView.findViewById(R.id.root_layout);
        this.setStyle(style);

    }

    public SuperActivityToast(Activity activity, Type type) {
        if (activity == null) {
            throw new IllegalArgumentException(TAG + ERROR_ACTIVITYNULL);
        }
        this.mActivity = activity;
        this.mType = type;
        mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewGroup = (ViewGroup) activity.findViewById(android.R.id.content);

        // 枚举类可以直接写变量
        switch (type) {
            case STANDARD:
                mToastView = mLayoutInflater.inflate(R.layout.supertoast,
                        mViewGroup, false);
                break;
            case BUTTON:
                //TODO 这里改过要用的布局
                mToastView = mLayoutInflater.inflate(
                        R.layout.superactivitytoast_button2, mViewGroup, false);
                mButton = (Button) mToastView.findViewById(R.id.button);
                mDividerView = mToastView.findViewById(R.id.divider);
                mButton.setOnClickListener(mButtonListener);
                break;
            case PROGRESS:
                mToastView = mLayoutInflater.inflate(
                        R.layout.superactivitytoast_progresscircle, mViewGroup,
                        false);
                mProgressBar = (ProgressBar) mToastView
                        .findViewById(R.id.progress_bar);
                break;
            case PROGRESS_HORIZONTAL:
                mToastView = mLayoutInflater.inflate(
                        R.layout.superactivitytoast_progresshorizontal, mViewGroup,
                        false);
                mProgressBar = (ProgressBar) mToastView
                        .findViewById(R.id.progress_bar);
                break;
            default:
                mToastView = mLayoutInflater.inflate(R.layout.supertoast,
                        mViewGroup, false);
                break;
        }
        mMessageTextView = (TextView) mToastView
                .findViewById(R.id.message_textview);
        mRootLayout = (LinearLayout) mToastView.findViewById(R.id.root_layout);
    }

    public SuperActivityToast(Activity activity, Type type, Style style) {
        if (activity == null) {
            throw new IllegalArgumentException(TAG + ERROR_ACTIVITYNULL);
        }
        this.mActivity = activity;
        this.mType = type;
        mLayoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewGroup = (ViewGroup) activity.findViewById(android.R.id.content);
        // 枚举类可以直接写变量
        switch (type) {
            case STANDARD:
                mToastView = mLayoutInflater.inflate(R.layout.supertoast,
                        mViewGroup, false);
                break;
            case BUTTON:
                mToastView = mLayoutInflater.inflate(
                        R.layout.superactivitytoast_button, mViewGroup, false);
                mButton = (Button) mToastView.findViewById(R.id.button);
                mDividerView = mToastView.findViewById(R.id.divider);
                mButton.setOnClickListener(mButtonListener);
                break;
            case PROGRESS:
                mToastView = mLayoutInflater.inflate(
                        R.layout.superactivitytoast_progresscircle, mViewGroup,
                        false);
                mProgressBar = (ProgressBar) mToastView
                        .findViewById(R.id.progress_bar);
                break;
            case PROGRESS_HORIZONTAL:
                mToastView = mLayoutInflater.inflate(
                        R.layout.superactivitytoast_progresshorizontal, mViewGroup,
                        false);
                mProgressBar = (ProgressBar) mToastView
                        .findViewById(R.id.progress_bar);
                break;
            default:
                mToastView = mLayoutInflater.inflate(R.layout.supertoast,
                        mViewGroup, false);
                break;
        }
        mMessageTextView = (TextView) mToastView
                .findViewById(R.id.message_textview);
        mRootLayout = (LinearLayout) mToastView.findViewById(R.id.root_layout);
        this.setStyle(style);
    }

    public void show() {
        ManagerSuperActivityToast.getInstance().add(this);
    }

    public Type getType() {
        return mType;
    }

    public void setText(CharSequence text) {
        mMessageTextView.setText(text);
    }

    public CharSequence getText() {
        return mMessageTextView.getText();
    }

    public void setTypefaceStyle(int typeface) {
        mTypefaceStyle = typeface;
        mMessageTextView.setTypeface(mMessageTextView.getTypeface(), typeface);
    }

    public int getTypefaceStyle() {
        return mTypefaceStyle;
    }

    public void setTextColor(int textColor) {
        mMessageTextView.setTextColor(textColor);
    }

    public int getTextColor() {
        return mMessageTextView.getCurrentTextColor();
    }

    public void setTextSize(int textSize) {
        mMessageTextView.setTextSize(textSize);
    }

    private void setTextSizeFloat(float textSize) {
        mMessageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public float getTextSize() {
        return mMessageTextView.getTextSize();
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public int getDuration() {
        return this.mDuration;
    }

    /**
     * If true will show the {@value #TAG} for an indeterminate time period and
     * ignore any set duration.
     */
    public void setIndeterminate(boolean isIndeterminate) {
        this.mIsIndeterminate = isIndeterminate;
    }

    public boolean isIndeterminate() {
        return this.mIsIndeterminate;
    }

    @SuppressWarnings("deprecation")
    public void setIcon(int iconResource, IconPosition iconPosition) {
        this.mIcon = iconResource;
        this.mIconPosition = iconPosition;
        switch (mIconPosition) {
            case BOTTOM:
                mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, null,
                        mActivity.getResources().getDrawable(iconResource));
                break;
            case LEFT:
                mMessageTextView
                        .setCompoundDrawablesWithIntrinsicBounds(mActivity
                                        .getResources().getDrawable(iconResource), null,
                                null, null);
                break;
            case RIGHT:
                mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, mActivity.getResources().getDrawable(iconResource),
                        null);
                break;
            case TOP:
                mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                        mActivity.getResources().getDrawable(iconResource), null,
                        null);
                break;
            default:
                mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, null,
                        mActivity.getResources().getDrawable(iconResource));
                break;
        }
    }

    public IconPosition getIconPosition() {
        return this.mIconPosition;
    }

    public int getIconResource() {
        return this.mIcon;
    }

    public void setBackground(int background) {
        this.mBackground = background;
        mRootLayout.setBackgroundResource(background);
    }

    public int getBackground() {
        return this.mBackground;
    }

    public void setAnimations(Animations animations) {
        this.mAnimations = animations;
    }

    public Animations getAnimations() {
        return this.mAnimations;
    }

    /**
     * If true will show the {@value #TAG} without animation.
     *
     * @param showImmediate
     *            boolean
     */
    public void setShowImmediate(boolean showImmediate) {
        this.showImmediate = showImmediate;
    }

    public boolean getShowImmediate() {
        return this.showImmediate;
    }

    // 触摸toast消失
    public void setTouchToDismiss(boolean touchDismiss) {
        this.mIsTouchDismissible = touchDismiss;
        if (touchDismiss) {
            mToastView.setOnTouchListener(mTouchDismissListener);
        } else {
            mToastView.setOnTouchListener(null);
        }
    }

    public boolean isTouchDismissible() {
        return this.mIsTouchDismissible;
    }

    public void setOnDismissWrapper(OnDismissWrapper onDismissWrapper) {
        this.mOnDismissWrapper = onDismissWrapper;
        this.mOnDismissWrapperTag = onDismissWrapper.getTag();
    }

    protected OnDismissWrapper getOnDismissWrapper() {
        return this.mOnDismissWrapper;
    }

    private String getOnDismissWrapperTag() {
        return this.mOnDismissWrapperTag;
    }

    public void dismiss() {
        ManagerSuperActivityToast.getInstance().removeSuperToast(this);
    }

    public void setOnClickWrapper(OnClickWrapper onClickWrapper) {
        if (mType != Type.BUTTON) {
            Log.e(TAG, "setOnClickListenerWrapper()" + ERROR_NOTBUTTONTYPE);
        }
        this.mOnClickWrapper = onClickWrapper;
        this.mOnClickWrapperTag = onClickWrapper.getTag();
    }

    /**
     * Sets an OnClickWrapper with a parcelable object to the button in a BUTTON
     */
    public void setOnClickWrapper(OnClickWrapper onClickWrapper,
                                  Parcelable token) {
        if (mType != Type.BUTTON) {
            Log.e(TAG, "setOnClickListenerWrapper()" + ERROR_NOTBUTTONTYPE);
        }
        onClickWrapper.setToken(token);
        this.mToken = token;
        this.mOnClickWrapper = onClickWrapper;
        this.mOnClickWrapperTag = onClickWrapper.getTag();
    }

    /**
     * Used in orientation change recreation.
     */
    private Parcelable getToken() {
        return this.mToken;
    }

    /**
     * Used in orientation change recreation.
     */
    private String getOnClickWrapperTag() {
        return this.mOnClickWrapperTag;
    }

    @SuppressWarnings("deprecation")
    public void setButtonIcon(int buttonIcon) {
        if (mType != Type.BUTTON) {
            Log.e(TAG, "setButtonIcon()" + ERROR_NOTBUTTONTYPE);
        }
        this.mButtonIcon = buttonIcon;
        if (mButton != null) {
            mButton.setCompoundDrawablesWithIntrinsicBounds(mActivity
                    .getResources().getDrawable(buttonIcon), null, null, null);
        }
    }

    @SuppressWarnings("deprecation")
    public void setButtonIcon(int buttonIcon, CharSequence buttonText) {
        if (mType != Type.BUTTON) {
            Log.w(TAG, "setButtonIcon()" + ERROR_NOTBUTTONTYPE);
        }
        this.mButtonIcon = buttonIcon;
        if (mButton != null) {
            mButton.setCompoundDrawablesWithIntrinsicBounds(mActivity
                    .getResources().getDrawable(buttonIcon), null, null, null);
            mButton.setText(buttonText);
        }
    }

    public int getButtonIcon() {
        return this.mButtonIcon;
    }

    /**
     * Sets the divider color of a BUTTON
     */
    public void setDividerColor(int dividerColor) {
        if (mType != Type.BUTTON) {
            Log.e(TAG, "setDivider()" + ERROR_NOTBUTTONTYPE);
        }
        this.mDividerColor = dividerColor;
        if (mDividerView != null) {
            mDividerView.setBackgroundColor(dividerColor);
        }
    }

    public int getDividerColor() {
        return this.mDividerColor;
    }

    public void setButtonText(CharSequence buttonText) {
        if (mType != Type.BUTTON) {
            Log.e(TAG, "setButtonText()" + ERROR_NOTBUTTONTYPE);
        }
        if (mButton != null) {
            mButton.setText(buttonText);
        }
    }

    public CharSequence getButtonText() {
        if (mButton != null) {
            return mButton.getText();
        } else {
            Log.e(TAG, "getButtonText()" + ERROR_NOTBUTTONTYPE);
            return "";
        }
    }

    public void setButtonTypefaceStyle(int typefaceStyle) {
        if (mType != Type.BUTTON) {
            Log.e(TAG, "setButtonTypefaceStyle()" + ERROR_NOTBUTTONTYPE);
        }
        if (mButton != null) {
            mButtonTypefaceStyle = typefaceStyle;
            mButton.setTypeface(mButton.getTypeface(), typefaceStyle);
        }
    }

    public int getButtonTypefaceStyle() {
        return this.mButtonTypefaceStyle;
    }

    public void setButtonTextColor(int buttonTextColor) {
        if (mType != Type.BUTTON) {
            Log.e(TAG, "setButtonTextColor()" + ERROR_NOTBUTTONTYPE);
        }
        if (mButton != null) {
            mButton.setTextColor(buttonTextColor);
        }
    }

    public int getButtonTextColor() {
        if (mButton != null) {
            return mButton.getCurrentTextColor();
        } else {
            Log.e(TAG, "getButtonTextColor()" + ERROR_NOTBUTTONTYPE);
            return 0;
        }
    }

    public void setButtonTextSize(int buttonTextSize) {
        if (mType != Type.BUTTON) {
            Log.e(TAG, "setButtonTextSize()" + ERROR_NOTBUTTONTYPE);
        }
        if (mButton != null) {
            mButton.setTextSize(buttonTextSize);
        }
    }

    private void setButtonTextSizeFloat(float buttonTextSize) {
        mButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, buttonTextSize);
    }

    public float getButtonTextSize() {
        if (mButton != null) {
            return mButton.getTextSize();
        } else {
            Log.e(TAG, "getButtonTextSize()" + ERROR_NOTBUTTONTYPE);
            return 0.0f;
        }
    }

    /**
     * Sets the progress of the progressbar in a PROGRESS_HORIZONTAL
     */
    public void setProgress(int progress) {
        if (mType != Type.PROGRESS_HORIZONTAL) {
            Log.e(TAG, "setProgress()" + ERROR_NOTPROGRESSHORIZONTALTYPE);
        }
        if (mProgressBar != null) {
            mProgressBar.setProgress(progress);
        }
    }

    public int getProgress() {
        if (mProgressBar != null) {
            return mProgressBar.getProgress();
        } else {
            Log.e(TAG, "getProgress()" + ERROR_NOTPROGRESSHORIZONTALTYPE);
            return 0;
        }
    }

    public void setMaxProgress(int maxProgress) {
        if (mType != Type.PROGRESS_HORIZONTAL) {
            Log.e(TAG, "setMaxProgress()" + ERROR_NOTPROGRESSHORIZONTALTYPE);
        }
        if (mProgressBar != null) {
            mProgressBar.setMax(maxProgress);
        }
    }

    public int getMaxProgress() {
        if (mProgressBar != null) {
            return mProgressBar.getMax();
        } else {
            Log.e(TAG, "getMaxProgress()" + ERROR_NOTPROGRESSHORIZONTALTYPE);
            return 0;
        }
    }

    /**
     * Sets an indeterminate value to the progressbar of a PROGRESS
     */
    public void setProgressIndeterminate(boolean isIndeterminate) {
        if (mType != Type.PROGRESS_HORIZONTAL && mType != Type.PROGRESS) {
            Log.e(TAG, "setProgressIndeterminate()"
                    + ERROR_NOTEITHERPROGRESSTYPE);
        }
        this.isProgressIndeterminate = isIndeterminate;
        if (mProgressBar != null) {
            mProgressBar.setIndeterminate(isIndeterminate);
        }
    }

    public boolean getProgressIndeterminate() {
        return this.isProgressIndeterminate;
    }

    public TextView getTextView() {
        return mMessageTextView;
    }

    public View getView() {
        return mToastView;
    }

    public boolean isShowing() {
        return mToastView != null && mToastView.isShown();
    }

    public Activity getActivity() {
        return mActivity;
    }

    public ViewGroup getViewGroup() {
        return mViewGroup;
    }

    private LinearLayout getRootLayout() {
        return mRootLayout;
    }

    private void setStyle(Style style) {
        this.setAnimations(style.animations);
        this.setTypefaceStyle(style.typefaceStyle);
        this.setTextColor(style.textColor);
        this.setBackground(style.background);
        if (this.mType == Type.BUTTON) {
            this.setDividerColor(style.dividerColor);
            this.setButtonTextColor(style.buttonTextColor);
        }
    }

    public static SuperActivityToast create(Activity activity,
                                            CharSequence textCharSequence, int durationInteger) {
        final SuperActivityToast superActivityToast = new SuperActivityToast(
                activity);
        superActivityToast.setText(textCharSequence);
        superActivityToast.setDuration(durationInteger);
        return superActivityToast;
    }

    public static SuperActivityToast create(Activity activity,
                                            CharSequence textCharSequence, int durationInteger,
                                            Animations animations) {
        final SuperActivityToast superActivityToast = new SuperActivityToast(
                activity);
        superActivityToast.setText(textCharSequence);
        superActivityToast.setDuration(durationInteger);
        superActivityToast.setAnimations(animations);
        return superActivityToast;
    }

    public static SuperActivityToast create(Activity activity,
                                            CharSequence textCharSequence, int durationInteger, Style style) {
        final SuperActivityToast superActivityToast = new SuperActivityToast(
                activity);
        superActivityToast.setText(textCharSequence);
        superActivityToast.setDuration(durationInteger);
        superActivityToast.setStyle(style);
        return superActivityToast;
    }

    public static void cancelAllSuperActivityToasts() {
        ManagerSuperActivityToast.getInstance().cancelAllSuperActivityToasts();
    }

    public static void clearSuperActivityToastsForActivity(Activity activity) {
        ManagerSuperActivityToast.getInstance()
                .cancelAllSuperActivityToastsForActivity(activity);
    }

    /**
     * Saves pending/showing to a bundle.
     */
    public static void onSaveState(Bundle bundle) {
        ReferenceHolder[] list = new ReferenceHolder[ManagerSuperActivityToast
                .getInstance().getList().size()];
        LinkedList<SuperActivityToast> lister = ManagerSuperActivityToast
                .getInstance().getList();
        for (int i = 0; i < list.length; i++) {
            list[i] = new ReferenceHolder(lister.get(i));
        }
        bundle.putParcelableArray(BUNDLE_TAG, list);
        SuperActivityToast.cancelAllSuperActivityToasts();
    }

    /**
     * Recreates pending/showing from orientation change.
     */
    public static void onRestoreState(Bundle bundle, Activity activity) {
        if (bundle == null) {
            return;
        }
        Parcelable[] savedArray = bundle.getParcelableArray(BUNDLE_TAG);
        int i = 0;
        if (savedArray != null) {
            for (Parcelable parcelable : savedArray) {
                i++;
                new SuperActivityToast(activity, (ReferenceHolder) parcelable,
                        null, i);
            }
        }
    }

    /**
     * Recreates pending/showing from orientation change and reattaches any
     * OnClickWrappers/OnDismissWrappers.
     */
    public static void onRestoreState(Bundle bundle, Activity activity,
                                      Wrappers wrappers) {
        if (bundle == null) {
            return;
        }
        Parcelable[] savedArray = bundle.getParcelableArray(BUNDLE_TAG);
        int i = 0;
        if (savedArray != null) {
            for (Parcelable parcelable : savedArray) {
                i++;
                new SuperActivityToast(activity, (ReferenceHolder) parcelable,
                        wrappers, i);
            }
        }
    }

    /**
     * 屏幕旋转之后重建
     */
    @SuppressLint("RtlHardcoded")
    private SuperActivityToast(Activity activity,
                               ReferenceHolder referenceHolder, Wrappers wrappers, int position) {
        SuperActivityToast superActivityToast;
        if (referenceHolder.mType == Type.BUTTON) {
            superActivityToast = new SuperActivityToast(activity, Type.BUTTON);
            superActivityToast.setButtonText(referenceHolder.mButtonText);
            superActivityToast
                    .setButtonTextSizeFloat(referenceHolder.mButtonTextSize);
            superActivityToast
                    .setButtonTextColor(referenceHolder.mButtonTextColor);
            superActivityToast.setButtonIcon(referenceHolder.mButtonIcon);
            superActivityToast.setDividerColor(referenceHolder.mDivider);
            superActivityToast
                    .setButtonTypefaceStyle(referenceHolder.mButtonTypefaceStyle);
            int screenSize = activity.getResources().getConfiguration().screenLayout
                    & Configuration.SCREENLAYOUT_SIZE_MASK;
            /*
			 * Changes the size of the BUTTON type SuperActivityToast to mirror
			 * Gmail app
			 */
            if (screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
                //TODO 这个地方改了边距
//				layoutParams.bottomMargin = (int) activity.getResources()
//						.getDimension(R.dimen.buttontoast_hover);
//				layoutParams.rightMargin = (int) activity.getResources()
//						.getDimension(R.dimen.buttontoast_x_padding);
//				layoutParams.leftMargin = (int) activity.getResources()
//						.getDimension(R.dimen.buttontoast_x_padding);
                superActivityToast.getRootLayout()
                        .setLayoutParams(layoutParams);
            }
			/* Reattach any OnClickWrappers by matching tags sent through parcel */
            if (wrappers != null) {
                for (OnClickWrapper onClickWrapper : wrappers
                        .getOnClickWrappers()) {
                    if (onClickWrapper.getTag().equalsIgnoreCase(
                            referenceHolder.mClickListenerTag)) {
                        superActivityToast.setOnClickWrapper(onClickWrapper,
                                referenceHolder.mToken);
                    }
                }
            }
        } else if (referenceHolder.mType == Type.PROGRESS) {
			/* PROGRESS {@value #TAG} should be managed by the developer */
            return;

        } else if (referenceHolder.mType == Type.PROGRESS_HORIZONTAL) {
			/*
			 * PROGRESS_HORIZONTAL {@value #TAG} should be managed by the
			 * developer
			 */
            return;
        } else {
            superActivityToast = new SuperActivityToast(activity);
        }
		/* Reattach any OnDismissWrappers by matching tags sent through parcel */
        if (wrappers != null) {
            for (OnDismissWrapper onDismissWrapper : wrappers
                    .getOnDismissWrappers()) {
                if (onDismissWrapper.getTag().equalsIgnoreCase(
                        referenceHolder.mDismissListenerTag)) {
                    superActivityToast.setOnDismissWrapper(onDismissWrapper);
                }
            }
        }
        superActivityToast.setAnimations(referenceHolder.mAnimations);
        superActivityToast.setText(referenceHolder.mText);
        superActivityToast.setTypefaceStyle(referenceHolder.mTypefaceStyle);
        superActivityToast.setDuration(referenceHolder.mDuration);
        superActivityToast.setTextColor(referenceHolder.mTextColor);
        superActivityToast.setTextSizeFloat(referenceHolder.mTextSize);
        superActivityToast.setIndeterminate(referenceHolder.mIsIndeterminate);
        superActivityToast.setIcon(referenceHolder.mIcon,
                referenceHolder.mIconPosition);
        superActivityToast.setBackground(referenceHolder.mBackground);
        superActivityToast
                .setTouchToDismiss(referenceHolder.mIsTouchDismissible);
		/*
		 * Do not use show animation on recreation of that waspreviously showing
		 */
        if (position == 1) {
            superActivityToast.setShowImmediate(true);
        }
        superActivityToast.show();
    }

    /* This OnTouchListener handles the setTouchToDismiss() function */
    @SuppressLint("ClickableViewAccessibility")
    private final OnTouchListener mTouchDismissListener = new OnTouchListener() {
        int timesTouched;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
			/* Hack to prevent repeat touch events causing erratic behavior */
            if (timesTouched == 0) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    dismiss();
                }
            }
            timesTouched++;
            return false;
        }
    };

    /* This OnClickListener handles the button click event */
    private final View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mOnClickWrapper != null) {
                mOnClickWrapper.onClick(view, mToken);
            }
            dismiss();
			/* Make sure the button cannot be clicked multiple times */
            mButton.setClickable(false);
        }
    };

    /**
     * Parcelable class that saves all data on orientation change
     */
    private static class ReferenceHolder implements Parcelable {

        Animations mAnimations;
        boolean mIsIndeterminate;
        boolean mIsTouchDismissible;
        float mTextSize;
        float mButtonTextSize;
        IconPosition mIconPosition;
        int mDuration;
        int mTextColor;
        int mIcon;
        int mBackground;
        int mTypefaceStyle;
        int mButtonTextColor;
        int mButtonIcon;
        int mDivider;
        int mButtonTypefaceStyle;
        Parcelable mToken;
        String mText;
        String mButtonText;
        String mClickListenerTag;
        String mDismissListenerTag;
        Type mType;

        public ReferenceHolder(SuperActivityToast superActivityToast) {
            mType = superActivityToast.getType();
            if (mType == Type.BUTTON) {
                mButtonText = superActivityToast.getButtonText().toString();
                mButtonTextSize = superActivityToast.getButtonTextSize();
                mButtonTextColor = superActivityToast.getButtonTextColor();
                mButtonIcon = superActivityToast.getButtonIcon();
                mDivider = superActivityToast.getDividerColor();
                mClickListenerTag = superActivityToast.getOnClickWrapperTag();
                mButtonTypefaceStyle = superActivityToast
                        .getButtonTypefaceStyle();
                mToken = superActivityToast.getToken();
            }
            if (superActivityToast.getIconResource() != 0
                    && superActivityToast.getIconPosition() != null) {
                mIcon = superActivityToast.getIconResource();
                mIconPosition = superActivityToast.getIconPosition();
            }
            mDismissListenerTag = superActivityToast.getOnDismissWrapperTag();
            mAnimations = superActivityToast.getAnimations();
            mText = superActivityToast.getText().toString();
            mTypefaceStyle = superActivityToast.getTypefaceStyle();
            mDuration = superActivityToast.getDuration();
            mTextColor = superActivityToast.getTextColor();
            mTextSize = superActivityToast.getTextSize();
            mIsIndeterminate = superActivityToast.isIndeterminate();
            mBackground = superActivityToast.getBackground();
            mIsTouchDismissible = superActivityToast.isTouchDismissible();
        }

        public ReferenceHolder(Parcel parcel) {
            mType = Type.values()[parcel.readInt()];
            if (mType == Type.BUTTON) {
                mButtonText = parcel.readString();
                mButtonTextSize = parcel.readFloat();
                mButtonTextColor = parcel.readInt();
                mButtonIcon = parcel.readInt();
                mDivider = parcel.readInt();
                mButtonTypefaceStyle = parcel.readInt();
                mClickListenerTag = parcel.readString();
                mToken = parcel.readParcelable(((Object) this).getClass()
                        .getClassLoader());
            }
            boolean hasIcon = parcel.readByte() != 0;
            if (hasIcon) {
                mIcon = parcel.readInt();
                mIconPosition = IconPosition.values()[parcel.readInt()];
            }
            mDismissListenerTag = parcel.readString();
            mAnimations = Animations.values()[parcel.readInt()];
            mText = parcel.readString();
            mTypefaceStyle = parcel.readInt();
            mDuration = parcel.readInt();
            mTextColor = parcel.readInt();
            mTextSize = parcel.readFloat();
            mIsIndeterminate = parcel.readByte() != 0;
            mBackground = parcel.readInt();
            mIsTouchDismissible = parcel.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(mType.ordinal());
            if (mType == Type.BUTTON) {
                parcel.writeString(mButtonText);
                parcel.writeFloat(mButtonTextSize);
                parcel.writeInt(mButtonTextColor);
                parcel.writeInt(mButtonIcon);
                parcel.writeInt(mDivider);
                parcel.writeInt(mButtonTypefaceStyle);
                parcel.writeString(mClickListenerTag);
                parcel.writeParcelable(mToken, 0);
            }
            if (mIcon != 0 && mIconPosition != null) {
                parcel.writeByte((byte) 1);
                parcel.writeInt(mIcon);
                parcel.writeInt(mIconPosition.ordinal());
            } else {
                parcel.writeByte((byte) 0);
            }
            parcel.writeString(mDismissListenerTag);
            parcel.writeInt(mAnimations.ordinal());
            parcel.writeString(mText);
            parcel.writeInt(mTypefaceStyle);
            parcel.writeInt(mDuration);
            parcel.writeInt(mTextColor);
            parcel.writeFloat(mTextSize);
            parcel.writeByte((byte) (mIsIndeterminate ? 1 : 0));
            parcel.writeInt(mBackground);
            parcel.writeByte((byte) (mIsTouchDismissible ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @SuppressWarnings({"unused", "rawtypes"})
        public static final Creator CREATOR = new Creator() {
            public ReferenceHolder createFromParcel(Parcel parcel) {
                return new ReferenceHolder(parcel);
            }

            public ReferenceHolder[] newArray(int size) {
                return new ReferenceHolder[size];
            }
        };
    }

}
