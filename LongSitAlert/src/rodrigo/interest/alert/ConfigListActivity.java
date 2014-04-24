package rodrigo.interest.alert;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA. User: Rodrigo Date: 14-4-4 Time: 下午4:31
 */
public class ConfigListActivity extends Activity {
	private ListView mConfigListView;
	private ArrayList<Config> mConfigs;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_list);
		readView();

		mConfigs = readData();

		showData(mConfigs);
	}

	private void showData(ArrayList<Config> configs) {

	}

	private ArrayList<Config> readData() {
		return null;
	}

	private void readView() {
		mConfigListView = (ListView) findViewById(R.id.config_list);
	}
}