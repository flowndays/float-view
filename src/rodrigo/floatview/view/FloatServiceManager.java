package rodrigo.floatview.view;

import android.content.Context;
import android.content.Intent;

/**
 * Created with Intellij IDEA Author: Rodrigo Date: 14-4-18 Time: 下午1:47
 */
public final class FloatServiceManager {
	private static FloatServiceManager mFloatServiceManager;

	private FloatViewManger.Direction mDirection;
	private float mYPercent;
	private boolean mIsOpen;

	private FloatServiceManager() {
	}

	public static synchronized FloatServiceManager getInstance() {
		if (mFloatServiceManager == null) {
			mFloatServiceManager = new FloatServiceManager();
		}
		return mFloatServiceManager;
	}

	public void startFloatService(Context context, FloatViewManger.Direction direction, float directionY) {
		mIsOpen = true;
		mDirection = direction;
		mYPercent = directionY;
		startFloatService(context);
	}

	public void startFloatService(Context context) {
		if (mIsOpen) {
			Intent intent = new Intent(context, FloatService.class);
			intent.putExtra(FloatViewManger.DIRECTION, mDirection);
			intent.putExtra(FloatViewManger.Y_PERCENT, mYPercent);
			context.startService(intent);
		}
	}

	public void stopFloatService(Context context, boolean forceStop) {
		if (mIsOpen) {
			context.stopService(new Intent(context, FloatService.class));
			mIsOpen = forceStop ? (!mIsOpen) : mIsOpen;
		}
	}

	public void setDirection(FloatViewManger.Direction direction) {
		this.mDirection = direction;
	}

	public void setDirectionY(float directionY) {
		this.mYPercent = directionY;
	}
}
