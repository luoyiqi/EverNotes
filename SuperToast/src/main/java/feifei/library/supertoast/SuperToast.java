package feifei.library.supertoast;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.ECLAIR)
@SuppressLint({ "RtlHardcoded", "InflateParams" })
public class SuperToast {

	// 点击事件
	public interface OnClickListener {
		public void onClick(View view, Parcelable token);
	}

	// toast消失事件
	public interface OnDismissListener {
		public void onDismiss(View view);

	}

	public static class Background {
		public static final int BLACK = Style.getBackground(Style.BLACK);
		public static final int BLUE = Style.getBackground(Style.BLUE);
		public static final int GRAY = Style.getBackground(Style.GRAY);
		public static final int GREEN = Style.getBackground(Style.GREEN);
		public static final int ORANGE = Style.getBackground(Style.ORANGE);
		public static final int PURPLE = Style.getBackground(Style.PURPLE);
		public static final int RED = Style.getBackground(Style.RED);
		public static final int WHITE = Style.getBackground(Style.WHITE);
	}

	public enum Animations {
		FADE, FLYIN, SCALE, POPUP
	}

	public static class Icon {

		public static class Dark {
			public static final int EDIT = (R.drawable.icon_dark_edit);
			public static final int EXIT = (R.drawable.icon_dark_exit);
			public static final int INFO = (R.drawable.icon_dark_info);
			public static final int REDO = (R.drawable.icon_dark_redo);
			public static final int REFRESH = (R.drawable.icon_dark_refresh);
			public static final int SAVE = (R.drawable.icon_dark_save);
			public static final int SHARE = (R.drawable.icon_dark_share);
			public static final int UNDO = (R.drawable.icon_dark_undo);

		}

		public static class Light {
			public static final int EDIT = (R.drawable.icon_light_edit);
			public static final int EXIT = (R.drawable.icon_light_exit);
			public static final int INFO = (R.drawable.icon_light_info);
			public static final int REDO = (R.drawable.icon_light_redo);
			public static final int REFRESH = (R.drawable.icon_light_refresh);
			public static final int SAVE = (R.drawable.icon_light_save);
			public static final int SHARE = (R.drawable.icon_light_share);
			public static final int UNDO = (R.drawable.icon_light_undo);
		}

	}

	public static class Duration {
		public static final int VERY_SHORT = (1500);
		public static final int SHORT = (2000);
		public static final int MEDIUM = (2750);
		public static final int LONG = (3500);
		public static final int EXTRA_LONG = (4500);
	}

	public static class TextSize {
		public static final int EXTRA_SMALL = (12);
		public static final int SMALL = (14);
		public static final int MEDIUM = (16);
		public static final int LARGE = (18);
	}

	public enum Type {
		STANDARD,
		// 圆形转转圈
		PROGRESS,
		// 水平进度条
		PROGRESS_HORIZONTAL,
		// 按钮
		BUTTON
	}

	public enum IconPosition {
		LEFT, RIGHT, TOP, BOTTOM
	}

	private Animations mAnimations = Animations.FADE;
	private final Context mContext;
	private int mGravity = Gravity.BOTTOM | Gravity.CENTER;
	private int mDuration = Duration.SHORT;
	private int mTypefaceStyle;
	private int mBackground;
	private int mXOffset = 0;
	private int mYOffset = 0;
	private final LinearLayout mRootLayout;
	private OnDismissListener mOnDismissListener;
	private final TextView mMessageTextView;
	private final View mToastView;
	private final WindowManager mWindowManager;
	private WindowManager.LayoutParams mWindowManagerParams;

	@SuppressLint("InflateParams")
	public SuperToast(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("context不能为空");
		}
		this.mContext = context;
		// 是Y轴的偏移量
		mYOffset = context.getResources().getDimensionPixelSize(
				R.dimen.toast_hover);
		final LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mToastView = layoutInflater.inflate(R.layout.supertoast, null);
		mWindowManager = (WindowManager) mToastView.getContext()
				.getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		mRootLayout = (LinearLayout) mToastView.findViewById(R.id.root_layout);
		mMessageTextView = (TextView) mToastView
				.findViewById(R.id.message_textview);
	}

	// 通过style设置toast
	public SuperToast(Context context, Style style) {
		if (context == null) {
			throw new IllegalArgumentException("context不能为空");
		}
		this.mContext = context;
		mYOffset = context.getResources().getDimensionPixelSize(
				R.dimen.toast_hover);
		final LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mToastView = layoutInflater.inflate(R.layout.supertoast, null);
		mWindowManager = (WindowManager) mToastView.getContext()
				.getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		mRootLayout = (LinearLayout) mToastView.findViewById(R.id.root_layout);
		mMessageTextView = (TextView) mToastView
				.findViewById(R.id.message_textview);
		this.setStyle(style);

	}

	// 队列，使用WindowManager可以使他独立于activity显示
	public void show() {
		mWindowManagerParams = new WindowManager.LayoutParams();
		mWindowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mWindowManagerParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		mWindowManagerParams.format = PixelFormat.TRANSLUCENT;
		mWindowManagerParams.windowAnimations = getAnimation();
		mWindowManagerParams.type = WindowManager.LayoutParams.TYPE_TOAST;
		mWindowManagerParams.gravity = mGravity;
		mWindowManagerParams.x = mXOffset;
		mWindowManagerParams.y = mYOffset;
		ManagerSuperToast.getInstance().add(this);
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

	public float getTextSize() {
		return mMessageTextView.getTextSize();
	}

	// 不能超过默认的最大时间
	public void setDuration(int duration) {
		if (duration > Duration.EXTRA_LONG) {
			Log.e("log", "设置的时间超过4.5秒了");
			this.mDuration = Duration.EXTRA_LONG;
		} else {
			this.mDuration = duration;
		}

	}

	public int getDuration() {
		return this.mDuration;
	}

	@SuppressWarnings("deprecation")
	// 相当于XML中写drawableLeft等等
	public void setIcon(int iconResource, IconPosition iconPosition) {
		switch (iconPosition) {
		case BOTTOM:
			mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					null, null,
					mContext.getResources().getDrawable(iconResource));
			break;
		case LEFT:
			mMessageTextView
					.setCompoundDrawablesWithIntrinsicBounds(mContext
							.getResources().getDrawable(iconResource), null,
							null, null);
			break;
		case RIGHT:
			mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					null, mContext.getResources().getDrawable(iconResource),
					null);
			break;
		case TOP:
			mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					mContext.getResources().getDrawable(iconResource), null,
					null);
			break;
		default:
			mMessageTextView.setCompoundDrawablesWithIntrinsicBounds(null,
					null, null,
					mContext.getResources().getDrawable(iconResource));
			break;
		}
	}

	public void setBackground(int background) {
		this.mBackground = background;
		mRootLayout.setBackgroundResource(background);
	}

	public int getBackground() {
		return this.mBackground;

	}

	public void setGravity(int gravity, int xOffset, int yOffset) {
		this.mGravity = gravity;
		this.mXOffset = xOffset;
		this.mYOffset = yOffset;
	}

	public void setAnimations(Animations animations) {
		this.mAnimations = animations;
	}

	public Animations getAnimations() {
		return this.mAnimations;
	}

	public void setOnDismissListener(OnDismissListener onDismissListener) {
		this.mOnDismissListener = onDismissListener;
	}

	public OnDismissListener getOnDismissListener() {
		return mOnDismissListener;

	}

	public void dismiss() {
		ManagerSuperToast.getInstance().removeSuperToast(this);
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

	public WindowManager getWindowManager() {
		return mWindowManager;
	}

	public WindowManager.LayoutParams getWindowManagerParams() {
		return mWindowManagerParams;
	}

	private int getAnimation() {
		if (mAnimations == Animations.FLYIN) {
			return android.R.style.Animation_Translucent;
		} else if (mAnimations == Animations.SCALE) {
			return android.R.style.Animation_Dialog;
		} else if (mAnimations == Animations.POPUP) {
			return android.R.style.Animation_InputMethod;
		} else {
			return android.R.style.Animation_Toast;
		}
	}

	private void setStyle(Style style) {
		this.setAnimations(style.animations);
		this.setTypefaceStyle(style.typefaceStyle);
		this.setTextColor(style.textColor);
		this.setBackground(style.background);
	}

	// 在静态方法里面获取对象
	public static SuperToast create(Context context,
			CharSequence textCharSequence, int durationInteger) {
		SuperToast superToast = new SuperToast(context);
		superToast.setText(textCharSequence);
		superToast.setDuration(durationInteger);
		return superToast;

	}

	public static SuperToast create(Context context,
			CharSequence textCharSequence, int durationInteger,
			Animations animations) {
		SuperToast superToast = new SuperToast(context);
		superToast.setText(textCharSequence);
		superToast.setDuration(durationInteger);
		superToast.setAnimations(animations);
		return superToast;

	}

	@TargetApi(Build.VERSION_CODES.ECLAIR)
	public static SuperToast create(Context context,
			CharSequence textCharSequence, int durationInteger, Style style) {
		SuperToast superToast = new SuperToast(context);
		superToast.setText(textCharSequence);
		superToast.setDuration(durationInteger);
		superToast.setStyle(style);
		return superToast;

	}

	/**
	 * Dismisses and removes all showing/pending
	 */
	public static void cancelAllSuperToasts() {
		ManagerSuperToast.getInstance().cancelAllSuperToasts();
	}
}
