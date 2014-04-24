package rodrigo.interest.alert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA. User: Rodrigo Date: 14-4-4 Time: 下午6:22
 */
public class ConfigListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Config> configs;

	public ConfigListAdapter(Context context, ArrayList<Config> configs) {
		this.context = context;
		this.configs = configs;
	}

	@Override
	public int getCount() {
		return configs.size();
	}

	@Override
	public Object getItem(int position) {
		return configs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			View view = LayoutInflater.from(context).inflate(R.layout.config_list_item, parent, false);
			holder.checkBox = (CheckBox) view.findViewById(R.id.enable);
			holder.title = (TextView) view.findViewById(R.id.name);
			holder.description = (TextView) view.findViewById(R.id.description);
		} else {

		}
		return null;
	}

	private static class ViewHolder {
		CheckBox checkBox;
		TextView title;
		TextView description;
	}
}
