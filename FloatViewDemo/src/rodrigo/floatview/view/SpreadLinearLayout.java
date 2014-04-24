package rodrigo.floatview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Custom LinearLayout to implement property animation.
 * Created with IntelliJ IDEA. User: Rodrigo Date: 14-4-10 Time: 上午11:59
 */
public class SpreadLinearLayout extends LinearLayout {
	public SpreadLinearLayout(Context context) {
		super(context);
	}

	public SpreadLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setPaddingRight(int paddingRight) {
		setPadding(getPaddingLeft(), getPaddingTop(), paddingRight, getPaddingBottom());
	}

	public void setPaddingLeft(int paddingLeft) {
		setPadding(paddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());
	}
}
