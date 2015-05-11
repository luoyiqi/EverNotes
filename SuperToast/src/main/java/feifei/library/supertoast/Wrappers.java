package feifei.library.supertoast;

import java.util.ArrayList;
import java.util.List;

/**
 * 适应旋转的处理，onRestoreState()和onSaveInstanceState()
 */
public class Wrappers {

	private final List<OnClickWrapper> onClickWrapperList = new ArrayList<OnClickWrapper>();
	private final List<OnDismissWrapper> onDismissWrapperList = new ArrayList<OnDismissWrapper>();
	public void add(OnClickWrapper onClickWrapper) {
		onClickWrapperList.add(onClickWrapper);
	}
	public void add(OnDismissWrapper onDismissWrapper) {
		onDismissWrapperList.add(onDismissWrapper);
	}
	public List<OnClickWrapper> getOnClickWrappers() {
		return onClickWrapperList;
	}
	public List<OnDismissWrapper> getOnDismissWrappers() {
		return onDismissWrapperList;
	}
}

