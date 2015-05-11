package feifei.library.supertoast;

import android.view.View;

public class OnDismissWrapper implements SuperToast.OnDismissListener {

	private final String mTag;
	private final SuperToast.OnDismissListener mOnDismissListener;

	public OnDismissWrapper(String tag,
							SuperToast.OnDismissListener onDismissListener) {
		this.mTag = tag;
		this.mOnDismissListener = onDismissListener;
	}

	public String getTag() {
		return mTag;
	}

	@Override
	public void onDismiss(View view) {
		mOnDismissListener.onDismiss (view);
	}

}
