package rodrigo.floatview.view;

/**
 * Created with Intellij IDEA Author: Rodrigo Date: 14-4-10 Time: 下午2:13
 */
public class StateInactive implements IFloatViewState {
	private FloatViewManger mFloatView;

	StateInactive(FloatViewManger floatView) {
		mFloatView = floatView;
	}

	@Override
	public void onInit() {
		mFloatView.setClickable(true);
		mFloatView.setBackground(false);
	}

	@Override
	public void onDrag() {
		mFloatView.setState(mFloatView.getStateActiveFolded());
	}

	@Override
	public void onClick() {
		mFloatView.setState(mFloatView.getStateActiveSpread());
		mFloatView.setTouchable(false);
		mFloatView.spread();
	}
}
