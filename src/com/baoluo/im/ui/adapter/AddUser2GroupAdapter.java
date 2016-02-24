package com.baoluo.im.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.CommonAdapter;
import com.baoluo.community.ui.adapter.ViewHolder;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.im.entity.ContactsInfo;

public class AddUser2GroupAdapter extends CommonAdapter<ContactsInfo>{
	
	private static final String TAG = "AddUser2GroupAdapter";
	
    private List<ContactsInfo> mDatas ;
    private List<String> selUids;
    private List<String> selectedUsers;
    private boolean hasInitial = false;
    
    private OnClickListener onClickListener;
    
	public AddUser2GroupAdapter(Context context, List<ContactsInfo> mDatas,List<String> selectedUsers,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		this.mDatas = mDatas;
		if(selectedUsers != null && selectedUsers.size() > 0){
			hasInitial = true;
			this.selectedUsers = selectedUsers;
		}
		selUids = new ArrayList<String>();
	}
	
	@Override
	public void convert(ViewHolder helper,final ContactsInfo item) {
		if(!StrUtils.isEmpty(item.getFace())){
			helper.setNetImageByUrl(R.id.iv_avatar, item.getFace());
		}else{
			helper.setImageResource(R.id.iv_avatar, R.drawable.default_head);
		}
		//helper.setImageResource(R.id.iv_avatar, R.drawable.bitboy);
		helper.setText(R.id.tv_user, item.getAccountName());
		int section = item.getSortLetters().charAt(0);
		int position = mDatas.indexOf(item);
		TextView tvCatalog = (TextView) helper.getView(R.id.tv_catalog);
		CheckBox cb = (CheckBox) helper.getView(R.id.cb_user);
		LinearLayout ll = (LinearLayout) helper.getView(R.id.ll_item);
		if (position == getPositionForSection(section)) {
			tvCatalog.setVisibility(View.VISIBLE);
			tvCatalog.setText(item.getSortLetters());
		} else {
			tvCatalog.setVisibility(View.GONE);
		}
		if(selUids.contains(item.getAccountID())){
			cb.setChecked(true);
		}else{
			cb.setChecked(false);
		}
		if (hasInitial && selectedUsers.contains(item.getAccountID())) {
			cb.setChecked(true);
			cb.setEnabled(false);
			cb.setClickable(false);
		}
		onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (hasInitial && selectedUsers.contains(item.getAccountID())) {
					L.i(TAG, "default selected user ,can't be edit");
					return;
				}
				if(selUids.contains(item.getAccountID())){
					selUids.remove(item.getAccountID());
					itemOnClickListener.doRemove(item.getAccountID());
					L.i(TAG, "do remove");
				}else{
					selUids.add(item.getAccountID());
					itemOnClickListener.doAdd(item);
					L.i(TAG, "do add ");
				}
				L.i(TAG, "uid:"+item.getAccountID()+",selUids.size="+selUids.size());
			}
		};
		cb.setOnClickListener(onClickListener);
		ll.setOnClickListener(onClickListener);
	}
	
	private ItemOnClickListener itemOnClickListener;
	public void setItemOnClickListener(ItemOnClickListener itemOnClickListener){
		this.itemOnClickListener = itemOnClickListener;
	}
	
	public List<String> getSelUids(){
		return this.selUids;
	}
	
	public interface ItemOnClickListener{
		public void doAdd(ContactsInfo c);
		public void doRemove(String uid);
	}
	
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount()-1; i++) {
			String sortStr = mDatas.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}
}
