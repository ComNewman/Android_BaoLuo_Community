package com.baoluo.im.task;

import com.baoluo.community.support.http.MyAsyncTask;
import com.baoluo.community.util.BitmapUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.dao.GroupDb;
import com.baoluo.dao.helper.GroupHelper;
import com.baoluo.event.NotifyGroupUpdate;
import com.baoluo.event.NotifyMessageUpdate;
import com.baoluo.im.entity.GroupInfo;
import com.baoluo.im.util.FileUtil;

import de.greenrobot.event.EventBus;

/**
 * 生成单个群头像
 * 
 * @author tao.lai
 * 
 */
public class GenerateGroupAvatarTask extends MyAsyncTask<Void, Long, Integer> {
	private static final String TAG = "GenerateGroupAvatarTask";

	private GroupInfo groupInfo;

	public GenerateGroupAvatarTask(GroupInfo groupInfo) {
		this.groupInfo = groupInfo;
		this.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
	}

	@Override
	protected Integer doInBackground(Void... params) {
		int rs = 0;
		GroupDb gd = GroupHelper.getInstance().getGroupDbByGid(
				groupInfo.getId());
		if (StrUtils.isEmpty(gd.getGroupId())) {
			rs = 1;
		}
		String groupAvatar = BitmapUtil.getGroupAvatar(groupInfo.getUsers());
		FileUtil.delFile(gd.getGroupAvatar());
		L.i(TAG, "groupAvatar=" + groupAvatar);
		gd.setGroupAvatar(groupAvatar);
		GroupHelper.getInstance().insertGroup(gd);
		return rs;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if (result == 1) {
			EventBus.getDefault().post(new NotifyMessageUpdate());
			EventBus.getDefault().post(new NotifyGroupUpdate());
		}
	}
}
