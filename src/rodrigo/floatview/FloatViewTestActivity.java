package rodrigo.floatview;

import rodrigo.floatview.view.FloatServiceManager;
import rodrigo.floatview.view.FloatViewManger;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Created with IntelliJ IDEA. User: Rodrigo Date: 14-4-22 Time: 下午8:12
 */
public class FloatViewTestActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.float_view_test);

		findViewById(R.id.start_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startFloatView();
			}
		});

		findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				closeFloatView();
			}
		});
	}

	private void closeFloatView() {
		FloatServiceManager.getInstance().stopFloatService(this, true);
	}

	public void startFloatView() {
		FloatServiceManager.getInstance().startFloatService(this, FloatViewManger.Direction.LEFT, 0.3f);
	}
}
