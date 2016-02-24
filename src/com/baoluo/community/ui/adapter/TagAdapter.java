package com.baoluo.community.ui.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.baoluo.community.ui.R;

public class TagAdapter extends BaseAdapter {

	private Context context = null;
	private List<String> datas = null;
	private List<String> selectedDatas = new ArrayList<String>();

	private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();

	public TagAdapter(Context context, List<String> datas) {
		this.datas = datas;
		this.context = context;
		configCheckMap(false);
	}

	public void configCheckMap(boolean bool) {

		for (int i = 0; i < datas.size(); i++) {
			isCheckMap.put(i, bool);
		}

	}

	@Override
	public int getCount() {
		return datas == null ? 0 : datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewGroup layout = null;

		/**
		 * 进行ListView 的优化
		 */
		if (convertView == null) {
			layout = (ViewGroup) LayoutInflater.from(context).inflate(
					R.layout.item_tags_lv, parent, false);
		} else {
			layout = (ViewGroup) convertView;
		}
		TextView tvTitle = (TextView) layout.findViewById(R.id.tvTitle);
		tvTitle.setText(datas.get(position));
		CheckBox cbCheck = (CheckBox) layout.findViewById(R.id.cbCheckBox);
		// TODO
		cbCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				isCheckMap.put(position, isChecked);
				if (isChecked) {
					if(!selectedDatas.contains(datas.get(position))){
						selectedDatas.add(datas.get(position));
					}
				} else {
					selectedDatas.remove(datas.get(position));
				}
			}
		});

		if (isCheckMap.get(position) == null) {
			isCheckMap.put(position, false);
		}
		if(!isCheckMap.get(position)){
			selectedDatas.remove(datas.get(position));
		}
		cbCheck.setChecked(isCheckMap.get(position));

		ViewHolder holder = new ViewHolder();

		holder.cbCheck = cbCheck;

		holder.tvTitle = tvTitle;

		layout.setTag(holder);

		return layout;
	}

	public Map<Integer, Boolean> getIsCheckMap() {
		return isCheckMap;
	}

	public void setIsCheckMap(Map<Integer, Boolean> isCheckMap) {
		this.isCheckMap = isCheckMap;
	}

	public List<String> getSelectedDatas() {
		return this.selectedDatas;
	}

	public static class ViewHolder {

		public TextView tvTitle = null;

		public CheckBox cbCheck = null;
		public Object data = null;

	}
}
