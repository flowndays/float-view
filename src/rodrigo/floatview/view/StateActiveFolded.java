package rodrigo.floatview.view;

import android.os.Handler;

/**
 * Created with Intellij IDEA Author: Rodrigo Date: 14-4-10 Time: 下午2:14
 */
public class StateActiveFolded implements IFloatViewState {
	private FloatViewManger mFloatView;
	private Handler mHandler;
	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			mFloatView.setState(mFloatView.getStateInactive());
		}
	};

	StateActiveFolded(FloatViewManger floatView) {
		mFloatView = floatView;
		mHandler = mFloatView.getHandler();
	}

	@Override
	public void onInit() {
		mFloatView.setBackground(true);
		mFloatView.getHandler().sendMessageDelayed(
				mFloatView.getHandler().obtainMessage(FloatViewManger.MESSAGE_STATE_CHANGE, mRunnable), 2000);
	}

	@Override
	public void onDrag() {
		mHandler.removeMessages(FloatViewManger.MESSAGE_ADSORB);
		mHandler.removeMessages(FloatViewManger.MESSAGE_STATE_CHANGE);
		mFloatView.getHandler()
				.sendMessageDelayed(mFloatView.getHandler().obtainMessage(FloatViewManger.MESSAGE_ADSORB), 500);
		mFloatView.getHandler().sendMessageDelayed(
				mFloatView.getHandler().obtainMessage(FloatViewManger.MESSAGE_STATE_CHANGE, mRunnable), 2000);
		mFloatView.setClickable(false);
	}

	@Override
	public void onClick() {
		mHandler.removeMessages(FloatViewManger.MESSAGE_ADSORB);
		mHandler.removeMessages(FloatViewManger.MESSAGE_STATE_CHANGE);
		mFloatView.setTouchable(false);
		mFloatView.setState(mFloatView.getStateActiveSpread());
		mFloatView.spread();
	}
}
