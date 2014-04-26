package rodrigo.floatview.view;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

/**
 * Created with Intellij IDEA Author: Rodrigo Date: 14-4-9 Time: 下午12:47
 */
public class FloatViewManger implements IFloatViewState {
	public static final int MESSAGE_STATE_CHANGE = 0x00;
	public static final int MESSAGE_ADSORB = 0x01;
	public static final String DIRECTION = "DIRECTION";
	public static final String Y_PERCENT = "Y_PERCENT";

	public enum Direction {
		LEFT, RIGHT
	}

	private Point mScreen = new Point();

	private float mFirstDownX = 0F;
	private float mFirstDownY = 0F;
	private int mCurrentX = 0;
	private int mCurrentY = 0;
	private int mClickTimeout = ViewConfiguration.getPressedStateDuration() + ViewConfiguration.getTapTimeout();
	private boolean mTouchable = true;
	private boolean mClickable = true;

	/**
	 * A state pattern works here, including the following three states:
	 * StateInactiveState, the float view is inactive, will be activated when
	 * clicked or dragged. StateActiveFolded, active and folded, will change to
	 * inactive automatically in 2 seconds non-operation. StateActiveSpread,
	 * active and spread, will change to StateActiveFolded automatically in 10
	 * seconds non-operation.
	 */
	private IFloatViewState mState;
	private IFloatViewState mStateInactive;
	private IFloatViewState mStateActiveFolded;
	private IFloatViewState mStateActiveSpread;

	private Direction mCurrentDirection;// Stick to left or right.

	private FloatViewGroup mFloatViewGroup;

	/**
	 * Call back of the buttons in spreading layout.
	 */
	public interface IItemClick {
		void onItemClick(int id);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				Runnable runnable = (Runnable) msg.obj;
				runnable.run();
				break;
			case MESSAGE_ADSORB:
				setClickable(false);
				setTouchable(false);

				/*
				 * Repeatedly send Message to this Handler, to update the
				 * position of the Floating View. To add more animation effects,
				 * use property animation instead.
				 */
				if (mCurrentX <= mScreen.x / 2) {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							if (mCurrentX > 0) {
								mCurrentX -= 20;
								mFloatViewGroup.updateFloatView(mCurrentX, mCurrentY);
								postDelayed(this, 5);
							} else {
								mCurrentDirection = Direction.LEFT;
								setClickable(true);
								setTouchable(true);
							}
						}
					}, 10);
				} else {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							if (mCurrentX < mScreen.x) {
								mCurrentX += 20;
								mFloatViewGroup.updateFloatView(mCurrentX, mCurrentY);
								postDelayed(this, 5);
							} else {
								mCurrentDirection = Direction.RIGHT;
								setClickable(true);
								setTouchable(true);
							}
						}
					}, 10);
				}
				break;
			default:
				break;
			}
		}
	};

	private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int rawX = (int) event.getRawX();
			int rawY = (int) event.getRawY();
			mCurrentX = rawX - mFloatViewGroup.getWidth() / 2;
			mCurrentY = rawY - mFloatViewGroup.getHeight() / 2;

			int touchSlop = 5;// Minimal position diversity to be regarded as
								// dragging. This number is chosen from
								// experiments.
			boolean moveOrClick = (Math.abs(rawX - mFirstDownX) >= touchSlop)
					&& (Math.abs(rawY - mFirstDownY) >= touchSlop);

			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				mFirstDownX = (int) event.getRawX();
				mFirstDownY = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				if (mTouchable && moveOrClick) {
					mFloatViewGroup.updateFloatView(mCurrentX, mCurrentY);
					onDrag();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (mClickable) {
					float time = event.getEventTime() - event.getDownTime();
					if ((!moveOrClick) && time < mClickTimeout) {
						onClick();
					}
				}
				break;
			}
			return false;
		}
	};

	public FloatViewManger(Context context, Direction direction, float yPercent, IItemClick iItemClick) {
		init(context, direction, yPercent, iItemClick);
		initStates();
	}

	private void init(Context context, Direction direction, float yPercent, IItemClick iItemClick) {
		mCurrentDirection = direction;

		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getSize(mScreen);

		mFloatViewGroup = new FloatViewGroup(context, windowManager, mCurrentDirection, yPercent, iItemClick);
		mFloatViewGroup.setOnTouchListener(mTouchListener);
	}

	private void initStates() {
		mStateInactive = new StateInactive(this);
		mStateActiveFolded = new StateActiveFolded(this);
		mStateActiveSpread = new StateActiveSpread(this);
		mState = mStateInactive;
	}

	synchronized void setState(IFloatViewState state) {
		mState = state;
		onInit();
	}

	IFloatViewState getStateActiveSpread() {
		return mStateActiveSpread;
	}

	IFloatViewState getStateActiveFolded() {
		return mStateActiveFolded;
	}

	IFloatViewState getStateInactive() {
		return mStateInactive;
	}

	public void setBackgroundState(boolean active) {
		mFloatViewGroup.setBackground(active);
	}

	Handler getHandler() {
		return mHandler;
	}

	void setTouchable(boolean canTouch) {
		mTouchable = canTouch;
	}

	void setClickable(boolean canClick) {
		mClickable = canClick;
	}

	public void showFloatView() {
		mFloatViewGroup.showFloatView();
	}

	public void destroyView() {
		mFloatViewGroup.removeFloatView();
		mFloatViewGroup.removeSpreadLayout();
	}

	@Override
	public void onInit() {
		mState.onInit();
	}

	@Override
	public void onDrag() {
		mState.onDrag();
	}

	@Override
	public void onClick() {
		mState.onClick();
	}

	void fold() {
		mFloatViewGroup.fold(mCurrentDirection);
	}

	void spread() {
		mFloatViewGroup.spread(mCurrentDirection);
	}

	public void setPortrait(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getSize(mScreen);
	}

	public void setLandScape(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getSize(mScreen);
	}
}