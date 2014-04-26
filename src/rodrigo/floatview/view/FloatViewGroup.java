package rodrigo.floatview.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import rodrigo.floatview.R;

/**
 * The Float View include a rounded floating button and two hidden layouts: mSpreadLeft and mSpreadRight.
 * mFloatButton can be dragged to anywhere in the screen, but it will automatically move to left or right of the screen
 * according to the X position. If it moves to the left, then mSpreadLeft will be activated and set Visible when mFloatButton is clicked, and vise versa.
 * Created with Intellij IDEA
 * Author: Rodrigo Date: 14-4-14 Time: 下午12:53
 */
public class FloatViewGroup {
	private class SpreadViews {
		private Button mBtnPerson;
		private Button mBtnCommunity;
		private Button mBtnQuit;
		private SpreadLinearLayout mSpreadContent;
	}

	private SpreadViews mLeftSpreadView = new SpreadViews();
	private SpreadViews mRightSpreadView = new SpreadViews();

	private ViewGroup mSpreadLeft;
	private ViewGroup mSpreadRight;
	private ImageView mFloatButton;

	private static int spreadWidth;
	private WindowManager.LayoutParams mLayoutParams;
	private WindowManager.LayoutParams mSpreadLayoutParams;
	private int mScreenWidth;
	private int mScreenHeight;

	private FloatViewManger.IItemClick mItemClick;
	private WindowManager mWindowManager;

    /**
     * hard coded buttons, should be configurable in future.
     */
	public static final int ID_PERSON = 0;
	public static final int ID_COMMUNITY = 1;
	public static final int ID_QUIT = 2;

	public static final int ANIMATION_DURATION = 500;

	private View.OnClickListener mPersonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mItemClick != null) {
				mItemClick.onItemClick(ID_PERSON);
			}
		}
	};
	private View.OnClickListener mCommunityListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mItemClick != null) {
				mItemClick.onItemClick(ID_COMMUNITY);
			}
		}
	};
	private View.OnClickListener mQuitListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (mItemClick != null) {
				mItemClick.onItemClick(ID_QUIT);
			}
		}
	};

	public FloatViewGroup(Context context, WindowManager windowManager, FloatViewManger.Direction direction,
			float yPercent, FloatViewManger.IItemClick iItemClick) {
		init(context, windowManager, iItemClick);
		initLayoutParams(direction, yPercent);
		initSpreadLayout(context);
	}

    private void init(Context context, WindowManager windowManager, FloatViewManger.IItemClick iItemClick) {
		mItemClick = iItemClick;
		mWindowManager = windowManager;
		Point size = new Point();
		windowManager.getDefaultDisplay().getSize(size);
		mScreenWidth = size.x;
		mScreenHeight = size.y;
		mFloatButton = (ImageView) LayoutInflater.from(context).inflate(R.layout.lib_view_floatview, null);
	}

	private void initLayoutParams(FloatViewManger.Direction direction, float yPercent) {
		mLayoutParams = new WindowManager.LayoutParams();
		mLayoutParams.format = PixelFormat.RGBA_8888;
		mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
		mLayoutParams.x = (direction == FloatViewManger.Direction.RIGHT) ? mScreenWidth : 0;
		if (yPercent <= 0 || yPercent > 1) {
			mLayoutParams.y = 0;
		} else {
			mLayoutParams.y = (int) (mScreenHeight * yPercent);
		}
		mLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
	}

	private void initSpreadLayout(Context context) {
		mSpreadLayoutParams = new WindowManager.LayoutParams();
		mSpreadLayoutParams.format = PixelFormat.RGBA_8888;
		mSpreadLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		mSpreadLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		mSpreadLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mSpreadLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		mSpreadRight = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.lib_view_floatview_spread_right, null);
		mSpreadRight.setVisibility(View.INVISIBLE);
		mWindowManager.addView(mSpreadRight, mSpreadLayoutParams);

		mSpreadLeft = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.lib_view_floatview_spread_left, null);
		mSpreadLeft.setVisibility(View.INVISIBLE);
		mWindowManager.addView(mSpreadLeft, mSpreadLayoutParams);

		readViews(mLeftSpreadView, mSpreadLeft);
		readViews(mRightSpreadView, mSpreadRight);
		setActions(mLeftSpreadView);
		setActions(mRightSpreadView);
	}

	private void readViews(SpreadViews spreadView, View parent) {
		spreadView.mBtnPerson = (Button) parent.findViewById(R.id.personage);
		spreadView.mBtnCommunity = (Button) parent.findViewById(R.id.community);
		spreadView.mBtnQuit = (Button) parent.findViewById(R.id.quit);
		spreadView.mSpreadContent = (SpreadLinearLayout) parent.findViewById(R.id.spread_content);
	}

	public void setBackground(boolean active) {
		if (mFloatButton != null) {
			mFloatButton.setBackgroundResource(active ? R.drawable.lib_floatview_logo_press
					: R.drawable.lib_floatview_logo_selector);
		}
	}

	private void showSpreadLayout(FloatViewManger.Direction direction) {
		ViewGroup layout = (direction == FloatViewManger.Direction.RIGHT) ? mSpreadRight : mSpreadLeft;
		layout.setVisibility(View.VISIBLE);

		setPositions(direction, layout);
	}

	private void setPositions(FloatViewManger.Direction direction, ViewGroup layout) {
		mSpreadLayoutParams.gravity = (direction == FloatViewManger.Direction.RIGHT) ? Gravity.TOP | Gravity.RIGHT
				: Gravity.TOP | Gravity.LEFT;
		mSpreadLayoutParams.x = 0;
		mSpreadLayoutParams.y = mLayoutParams.y;

		mWindowManager.updateViewLayout(layout, mSpreadLayoutParams);
		mWindowManager.updateViewLayout(mFloatButton, mLayoutParams);
	}

	private void setActions(SpreadViews spreadView) {
		spreadView.mBtnPerson.setOnClickListener(mPersonListener);
		spreadView.mBtnCommunity.setOnClickListener(mCommunityListener);
		spreadView.mBtnQuit.setOnClickListener(mQuitListener);
	}

	private void hideSpreadLayout() {
		mSpreadLeft.setVisibility(View.GONE);
		mSpreadRight.setVisibility(View.GONE);
	}

	public void removeSpreadLayout() {
		if (mSpreadLeft.getParent() != null) {
			mWindowManager.removeViewImmediate(mSpreadLeft);
		}
		if (mSpreadRight.getParent() != null) {
			mWindowManager.removeViewImmediate(mSpreadRight);
		}
	}

	public void updateFloatView(int fX, int fY) {
		mLayoutParams.x = fX;
		mLayoutParams.y = fY;
		if (mFloatButton != null) {
			mWindowManager.updateViewLayout(mFloatButton, mLayoutParams);
		}
	}

	public void showFloatView() {
		mWindowManager.addView(mFloatButton, mLayoutParams);
	}

	public void removeFloatView() {
		if (mFloatButton != null && mFloatButton.getParent() != null) {
			mWindowManager.removeViewImmediate(mFloatButton);
			mFloatButton = null;
		}
	}

	public void spread(FloatViewManger.Direction direction) {
		if (spreadWidth == 0) {
			spreadWidth = mSpreadLeft.getWidth();
		}

		if (direction == FloatViewManger.Direction.LEFT) {
			spreadFromLeft(direction);
		} else {
			spreadFromRight(direction);
		}
	}

	private void spreadFromRight(FloatViewManger.Direction direction) {
		showSpreadLayout(direction);
		ObjectAnimator spreadOut = ObjectAnimator
				.ofInt(mRightSpreadView.mSpreadContent, "paddingLeft", -spreadWidth, 0);
		spreadOut.setDuration(ANIMATION_DURATION);
		spreadOut.start();
	}

	private void spreadFromLeft(FloatViewManger.Direction direction) {
		showSpreadLayout(direction);
		mLeftSpreadView.mSpreadContent.setPaddingRight(-spreadWidth);
		ObjectAnimator spreadOut = ObjectAnimator
				.ofInt(mLeftSpreadView.mSpreadContent, "paddingRight", -spreadWidth, 0);
		spreadOut.setDuration(ANIMATION_DURATION);
		spreadOut.start();
	}

	public void fold(FloatViewManger.Direction direction) {
		if (direction == FloatViewManger.Direction.LEFT) {
			foldFromLeft();
		} else {
			foldFromRight();
		}
	}

	private void foldFromRight() {
		ObjectAnimator spreadOut = ObjectAnimator
				.ofInt(mRightSpreadView.mSpreadContent, "paddingLeft", 0, -spreadWidth);
		spreadOut.setDuration(ANIMATION_DURATION);
		spreadOut.start();
		spreadOut.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				hideSpreadLayout();
			}
		});
	}

	private void foldFromLeft() {
		ObjectAnimator spreadOut = ObjectAnimator
				.ofInt(mLeftSpreadView.mSpreadContent, "paddingRight", 0, -spreadWidth);
		spreadOut.setDuration(ANIMATION_DURATION);
		spreadOut.start();
		spreadOut.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				hideSpreadLayout();
			}
		});
	}

	public void setOnTouchListener(View.OnTouchListener touchListener) {
		mFloatButton.setOnTouchListener(touchListener);
	}

	public int getWidth() {
		return mFloatButton.getMeasuredWidth();
	}

	public int getHeight() {
		return mFloatButton.getMeasuredHeight();
	}
}
