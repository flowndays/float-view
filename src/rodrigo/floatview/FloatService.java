package rodrigo.floatview;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.IBinder;
import android.widget.Toast;
import rodrigo.floatview.view.FloatViewGroup;
import rodrigo.floatview.view.FloatViewManger;

/**
 * Created with Intellij IDEA Author: Rodrigo Date: 14-4-9 Time: 下午12:46
 */
public class FloatService extends Service implements FloatViewManger.IItemClick {
	private static final String CONFIG_CHANGED = "android.intent.action.CONFIGURATION_CHANGED";

	private FloatViewManger mFloatView;

	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter filter = new IntentFilter();
		filter.addAction(CONFIG_CHANGED);
		this.registerReceiver(mBroadcastReceiver, filter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initFloatView(intent);
		return START_REDELIVER_INTENT;
	}

	private void initFloatView(Intent intent) {
		if (mFloatView == null) {
			mFloatView = new FloatViewManger(getApplication(),
					(FloatViewManger.Direction) intent.getSerializableExtra(FloatViewManger.DIRECTION),
					intent.getFloatExtra(FloatViewManger.Y_PERCENT, 0.2f), this);
			mFloatView.showFloatView();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mFloatView != null) {
			mFloatView.destroyView();
		}
		unregisterReceiver(mBroadcastReceiver);
	}

	@Override
	public void onItemClick(int id) {
		switch (id) {
		case FloatViewGroup.ID_PERSON:
			Toast.makeText(this, "person clicked", Toast.LENGTH_SHORT).show();
			break;
		case FloatViewGroup.ID_COMMUNITY:
			Toast.makeText(this, "community clicked", Toast.LENGTH_SHORT).show();
			break;
		case FloatViewGroup.ID_QUIT:
			Toast.makeText(this, "person clicked", Toast.LENGTH_SHORT).show();
			mFloatView.destroyView();
			stopSelf();
			break;
		default:
			break;
		}
	}

	public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent myIntent) {
			if (myIntent.getAction().equals(CONFIG_CHANGED)) {
				if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
					mFloatView.setLandScape(context);
				} else {
					mFloatView.setPortrait(context);
				}
			}
		}
	};
}
