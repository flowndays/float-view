package rodrigo.floatview.view;

import android.os.Handler;

/**
 * Active but folded state. Automatically changed to inactive state if no
 * interaction happened in 2 seconds. Change to StateActiveSpread if clicked.
 * Created with Intellij IDEA
 * Author: * Rodrigo Date: 14-4-10 Time: 下午2:14
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
		mFloatView.setBackgroundState(true);
        autoInactive();
    }

    private void autoInactive() {
        mFloatView.getHandler().sendMessageDelayed(
                mFloatView.getHandler().obtainMessage(FloatViewManger.MESSAGE_STATE_CHANGE, mRunnable), 2000);
    }

    @Override
	public void onDrag() {
		mHandler.removeMessages(FloatViewManger.MESSAGE_ADSORB);
		mHandler.removeMessages(FloatViewManger.MESSAGE_STATE_CHANGE);
        autoAdsorb();
        autoInactive();
        mFloatView.setClickable(false);
	}

    private void autoAdsorb() {
        mFloatView.getHandler().sendMessageDelayed(
                mFloatView.getHandler().obtainMessage(FloatViewManger.MESSAGE_ADSORB), 200);
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
