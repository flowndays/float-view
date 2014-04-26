package rodrigo.floatview.view;

import android.os.Handler;

/**
 * Active and spread state. Automatically change to StateActiveFolded if no interaction happened in 10 seconds.
 * Created with Intellij IDEA Author: Rodrigo Date: 14-4-10 Time: 下午2:14
 */
public class StateActiveSpread implements IFloatViewState {
	private FloatViewManger mFloatView;
	private Handler mHandler;
	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			mFloatView.setState(mFloatView.getStateActiveFolded());
			mFloatView.setTouchable(true);
			mFloatView.fold();
		}
	};

	StateActiveSpread(FloatViewManger floatView) {
		mFloatView = floatView;
		mHandler = mFloatView.getHandler();
	}

	@Override
	public void onInit() {
		mFloatView.setBackgroundState(true);
		mFloatView.getHandler().sendMessageDelayed(
				mFloatView.getHandler().obtainMessage(FloatViewManger.MESSAGE_STATE_CHANGE, mRunnable), 10000);
	}

	@Override
	public void onDrag() {
		mHandler.removeMessages(FloatViewManger.MESSAGE_STATE_CHANGE);
		mFloatView.getHandler().sendMessageDelayed(
				mFloatView.getHandler().obtainMessage(FloatViewManger.MESSAGE_STATE_CHANGE, mRunnable), 10000);
	}

	@Override
	public void onClick() {
		mHandler.removeMessages(FloatViewManger.MESSAGE_STATE_CHANGE);
		mFloatView.setState(mFloatView.getStateActiveFolded());
		mFloatView.setTouchable(true);
		mFloatView.fold();
	}
}
