package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.entity.MineTrack;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.RoundImageView;
import com.baoluo.community.util.StrUtils;

/**
 * 足迹适配器
 * 
 * @author xiangyang.fu
 * 
 */
public class MineTrackAdapter extends BaseAdapter {

	private List<MineTrack> tracks;
	private LayoutInflater inflater;
	final int VIEW_TYPE = 2;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;

	public MineTrackAdapter(List<MineTrack> tracks, Context mContext) {
		super();
		this.tracks = tracks;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {

		return tracks == null ? 0 : tracks.size();
	}

	@Override
	public int getViewTypeCount() {

		return VIEW_TYPE;
	}

	@Override
	public int getItemViewType(int position) {
		int type = tracks.get(position).getIsSelf();
		switch (type) {
		case 1:
			return TYPE_1;
		case 2:
			return TYPE_2;
		default:
			return TYPE_1;
		}
	}

	@Override
	public Object getItem(int position) {

		return tracks.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MineTrack track = tracks.get(position);
		ViewHolderSelf holderSelf = null;
		ViewHolderAttend holderAttend = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case TYPE_1:
				convertView = inflater.inflate(R.layout.item_mine_track_attend,
						parent, false);
				holderAttend = new ViewHolderAttend();
				holderAttend.head = (RoundImageView) convertView
						.findViewById(R.id.riv_mine_track_attend);
				holderAttend.month = (TextView) convertView
						.findViewById(R.id.tv_mine_track__month);
				holderAttend.owner = (TextView) convertView
						.findViewById(R.id.tv_mine_track_attend_nick);
				holderAttend.integral = (TextView) convertView
						.findViewById(R.id.tv_mine_track_attend_integral);
				holderAttend.content = (TextView) convertView
						.findViewById(R.id.tv_mine_track_attend_content);
				holderAttend.date = (TextView) convertView
						.findViewById(R.id.tv_mine_track_attend_date);
				convertView.setTag(holderAttend);
				break;
			case TYPE_2:
				convertView = inflater.inflate(R.layout.item_mine_track_self,
						parent, false);
				holderSelf = new ViewHolderSelf();
				holderSelf.head = (RoundImageView) convertView
						.findViewById(R.id.riv_mine_track_self);
				holderSelf.month = (TextView) convertView
						.findViewById(R.id.tv_mine_track_self_month);
				holderSelf.owner = (TextView) convertView
						.findViewById(R.id.tv_mine_track_self_nick);
				holderSelf.integral = (TextView) convertView
						.findViewById(R.id.tv_mine_track_self_integral);
				holderSelf.content = (TextView) convertView
						.findViewById(R.id.tv_mine_track_self_content);
				holderSelf.date = (TextView) convertView
						.findViewById(R.id.tv_mine_track_self_date);
				convertView.setTag(holderSelf);
				break;
			}
		} else {
			switch (type) {
			case TYPE_1:
				holderAttend = (ViewHolderAttend) convertView.getTag();
				break;
			case TYPE_2:
				holderSelf = (ViewHolderSelf) convertView.getTag();
				break;
			}
		}
		switch (type) {
		case TYPE_1:
			if (!StrUtils.isEmpty(track.getUserFace())) {
				GlobalContext.getInstance().imageLoader.displayImage(
						track.getUserFace(), holderAttend.head,
						GlobalContext.options, null);
			} else {
				holderAttend.head.setImageResource(R.drawable.head_default);
			}
			holderAttend.month.setText(track.getMonth());
			holderAttend.owner.setText(track.getUserName());
			holderAttend.content.setText(track.getTitle());
			holderAttend.integral.setText(StrUtils.setPointColor(track.getDesc(),"#ff7f7f"));
			holderAttend.date.setText(track.getCreateTime());

			break;
		case TYPE_2:
			if (!StrUtils.isEmpty(track.getUserFace())) {
				GlobalContext.getInstance().imageLoader.displayImage(
						track.getUserFace(), holderSelf.head,
						GlobalContext.options, null);
			} else {
				holderAttend.head.setImageResource(R.drawable.head_default);
			}
			holderSelf.month.setText(track.getMonth());
			holderSelf.owner.setText(track.getUserName());
			holderSelf.content.setText(track.getTitle());
			holderSelf.integral.setText(StrUtils.setPointColor(track.getDesc(),"#ff7f7f"));
			holderSelf.date.setText(track.getCreateTime());
			break;
		}

		return convertView;
	}

	class ViewHolderSelf {
		RoundImageView head;
		TextView month;
		TextView owner;
		TextView content;
		TextView integral;
		TextView date;
	}

	class ViewHolderAttend {
		RoundImageView head;
		TextView month;
		TextView owner;
		TextView content;
		TextView integral;
		TextView date;
	}

}
