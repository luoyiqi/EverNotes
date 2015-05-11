package feifei.library.supertoast;

import android.os.Parcelable;
import android.view.View;

public class OnClickWrapper implements SuperToast.OnClickListener {

	private final String mTag;
	private final SuperToast.OnClickListener mOnClickListener;
	private Parcelable mToken;

	// tag必须唯一
	public OnClickWrapper(String tag, SuperToast.OnClickListener onClickListener) {
		this.mTag = tag;
		this.mOnClickListener = onClickListener;
	}

	public String getTag() {
		return mTag;
	}

	public void setToken(Parcelable token) {
		this.mToken = token;
	}

	@Override
	public void onClick(View view, Parcelable token) {
		mOnClickListener.onClick(view, mToken);
	}

}
