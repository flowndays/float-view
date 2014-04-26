package rodrigo.floatview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import rodrigo.floatview.view.FloatViewManger;

/**
 * Created with IntelliJ IDEA. User: Rodrigo Date: 14-4-22 Time: 下午8:12
 */
public class FloatViewActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.float_view_demo);

		findViewById(R.id.start_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startFloatService(FloatViewManger.Direction.LEFT, 0.3f);
			}
		});

		findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopFloatService();
			}
		});
	}

	/**
	 * open the floating view,
	 * 
	 * @param direction
	 *            left or right
	 * @param directionY
	 *            default Y position represented by percentage of the screen
	 *            height.
	 */
	public void startFloatService(FloatViewManger.Direction direction, float directionY) {
		Intent intent = new Intent(this, FloatService.class);
		intent.putExtra(FloatViewManger.DIRECTION, direction);
		intent.putExtra(FloatViewManger.Y_PERCENT, directionY);
		startService(intent);
	}

	/**
	 * stop the service, the floating view will be removed from the screen if it
	 * is shown.
	 * 
	 */
	public void stopFloatService() {
		stopService(new Intent(this, FloatService.class));
	}

}
