package com.baoluo.im.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baoluo.community.support.http.MyAsyncTask;
import com.baoluo.community.util.BitmapUtil;
import com.baoluo.community.util.L;
import com.baoluo.dao.GroupDb;
import com.baoluo.dao.helper.GroupHelper;
import com.baoluo.event.NotifyGroupUpdate;
import com.baoluo.event.NotifyMessageUpdate;
import com.baoluo.im.entity.GroupInfo;

import de.greenrobot.event.EventBus;

/**
 * 生成多个群头像
 * @author tao.lai
 *
 */
public class GenerateGroupsAvatarTask extends MyAsyncTask<Void, Long, Integer>{
	private static final String TAG = "GenerateGroupsAvatarTask";
	
	private Map<String,GroupInfo> map;
	
	public GenerateGroupsAvatarTask(List<GroupInfo> groups) {
		map = new HashMap<String, GroupInfo>();
		if (groups.size() > 0) {
			for (GroupInfo i : groups) {
				map.put(i.getId(), i);
			}
		}
		this.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
	}

	@Override
	protected Integer doInBackground(Void... params) {
		int rs = 0;
		List<GroupDb> list = GroupHelper.getInstance().getAllGroupDb();
		if(list.size() > 0){
			rs = 1;
		}
		for(int i = 0; i < list.size(); i++){
			GroupDb gd = list.get(i);
		    GroupInfo gi = map.get(gd.getGroupId());
		    String groupFile = BitmapUtil.getGroupAvatar(gi.getUsers());
		    gd.setGroupAvatar(groupFile);
		    L.i(TAG, "groupId="+gd.getId()+",gd.="+gd.getGroupAvatar());
		    GroupHelper.getInstance().insertGroup(gd);
		}
		return rs;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if(result == 1){
			EventBus.getDefault().post(new NotifyMessageUpdate());
			EventBus.getDefault().post(new NotifyGroupUpdate());
		}
	}
}
