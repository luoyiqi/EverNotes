package feifei.library.supertoast;

import java.util.LinkedList;

/**
 * Manages the life of a SuperCardToast on orientation changes.
 */
class ManagerSuperCardToast {

	private static ManagerSuperCardToast mManagerSuperCardToast;
	private final LinkedList<SuperCardToast> mList;

	private ManagerSuperCardToast() {
		mList = new LinkedList<SuperCardToast>();
	}

	/**
	 * Singleton method to ensure all SuperCardToasts are passed through the
	 * same manager.
	 */
	protected static synchronized ManagerSuperCardToast getInstance() {
		if (mManagerSuperCardToast != null) {
			return mManagerSuperCardToast;
		} else {
			mManagerSuperCardToast = new ManagerSuperCardToast();
			return mManagerSuperCardToast;
		}
	}

	/**
	 * Add a SuperCardToast to the list.
	 */
	void add(SuperCardToast superCardToast) {
		mList.add(superCardToast);
	}

	/**
	 * Removes a SuperCardToast from the list.
	 */
	void remove(SuperCardToast superCardToast) {
		mList.remove(superCardToast);
	}

	/**
	 * Removes all SuperCardToasts and clears the list
	 */
	void cancelAllSuperActivityToasts() {
		for (SuperCardToast superCardToast : mList) {
			if (superCardToast.isShowing()) {
				superCardToast.getViewGroup().removeView(
						superCardToast.getView());
				superCardToast.getViewGroup().invalidate();
			}
		}
		mList.clear();

	}

	/**
	 * Used in SuperCardToast saveState().
	 */
	LinkedList<SuperCardToast> getList() {
		return mList;
	}

}
