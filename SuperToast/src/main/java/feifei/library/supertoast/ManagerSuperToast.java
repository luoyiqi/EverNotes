package feifei.library.supertoast;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;

/* Manages the life of a SuperToast. Initially copied from the Crouton library */
public class ManagerSuperToast extends Handler {

	/* Potential messages for the handler to send * */
	private static final class Messages {

		/* Hexadecimal numbers that represent acronyms for the operation * */
		private static final int DISPLAY_SUPERTOAST = 0x445354;
		private static final int ADD_SUPERTOAST = 0x415354;
		private static final int REMOVE_SUPERTOAST = 0x525354;

	}

	private static ManagerSuperToast mManagerSuperToast;

	private final Queue<SuperToast> mQueue;

	/* Private method to create a new list if the manager is being initialized */
	private ManagerSuperToast() {
		mQueue = new LinkedBlockingQueue<SuperToast>();
	}

	// 单例模式
	protected static synchronized ManagerSuperToast getInstance() {
		if (mManagerSuperToast != null) {
			return mManagerSuperToast;
		} else {
			mManagerSuperToast = new ManagerSuperToast();
			return mManagerSuperToast;
		}
	}

	protected void add(SuperToast superToast) {
		mQueue.add(superToast);
		this.showNextSuperToast();
	}

	private void showNextSuperToast() {
		if (mQueue.isEmpty()) {
			return;
		}
		// 出队
		final SuperToast superToast = mQueue.peek();
		// Show SuperToast if none are showing (not sure why this works but it
		// does)
		if (!superToast.isShowing()) {
			final Message message = obtainMessage(Messages.ADD_SUPERTOAST);
			message.obj = superToast;
			sendMessage(message);
		} else {
			sendMessageDelayed(superToast, Messages.DISPLAY_SUPERTOAST,
					getDuration(superToast));
		}
	}

	/* Show/dismiss a SuperToast after a specific duration */
	private void sendMessageDelayed(SuperToast superToast, final int messageId,
			final long delay) {
		Message message = obtainMessage(messageId);
		message.obj = superToast;
		sendMessageDelayed(message, delay);
	}

	/* Get duration and add one second to compensate for show/hide animations */
	private long getDuration(SuperToast superToast) {
		long duration = superToast.getDuration();
		duration += 1000;
		return duration;
	}

	@Override
	public void handleMessage(Message message) {
		final SuperToast superToast = (SuperToast) message.obj;
		switch (message.what) {
		case Messages.DISPLAY_SUPERTOAST:
			showNextSuperToast();
			break;
		case Messages.ADD_SUPERTOAST:
			displaySuperToast(superToast);
			break;
		case Messages.REMOVE_SUPERTOAST:
			removeSuperToast(superToast);
			break;
		default: {
			super.handleMessage(message);
			break;
		}
		}
	}

	/* Displays a SuperToast */
	private void displaySuperToast(SuperToast superToast) {
		if (superToast.isShowing()) {
			/* If the SuperToast is already showing do not show again */
			return;
		}
		final WindowManager windowManager = superToast.getWindowManager();
		final View toastView = superToast.getView();
		final WindowManager.LayoutParams params = superToast
				.getWindowManagerParams();
		if (windowManager != null) {
			windowManager.addView(toastView, params);
		}
		sendMessageDelayed(superToast, Messages.REMOVE_SUPERTOAST,
				superToast.getDuration() + 500);

	}

	/* Hide and remove the SuperToast */
	protected void removeSuperToast(SuperToast superToast) {
		final WindowManager windowManager = superToast.getWindowManager();
		final View toastView = superToast.getView();
		if (windowManager != null) {
			mQueue.poll();
			windowManager.removeView(toastView);
			sendMessageDelayed(superToast, Messages.DISPLAY_SUPERTOAST, 500);
			if (superToast.getOnDismissListener() != null) {
				superToast.getOnDismissListener().onDismiss(
						superToast.getView());
			}
		}
	}

	/* Cancels/removes all showing pending SuperToasts */
	protected void cancelAllSuperToasts() {
		removeMessages(Messages.ADD_SUPERTOAST);
		removeMessages(Messages.DISPLAY_SUPERTOAST);
		removeMessages(Messages.REMOVE_SUPERTOAST);
		for (SuperToast superToast : mQueue) {
			if (superToast.isShowing()) {
				superToast.getWindowManager().removeView(superToast.getView());
			}
		}
		mQueue.clear();
	}

}
