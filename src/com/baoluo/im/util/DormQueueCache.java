package com.baoluo.im.util;

import java.util.ArrayList;
import java.util.List;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.im.entity.AttendDorm;
import com.baoluo.im.entity.AttendDormQueue;

public class DormQueueCache {
	private static final String TAG = "DormQueueCache";

	private List<String> dormQueue;
	private static DormQueueCache instance;

	public static DormQueueCache getInstance() {
		if (instance == null) {
			instance = new DormQueueCache();
		}
		return instance;
	}

	public void initQueue() {
		if (dormQueue == null) {
			dormQueue = new ArrayList<String>();
		}
		new GetTask(UrlHelper.DORM_ATTEND_QUEUE,
				new UpdateViewHelper.UpdateViewListener(AttendDormQueue.class) {
					@Override
					public void onComplete(Object obj) {
						dormQueue.clear();
						AttendDormQueue queues = (AttendDormQueue)obj;
						if (queues.getItems() != null) {
							for (AttendDorm dorm : queues.getItems()) {
								dormQueue.add(dorm.getId());
							}
						}
					}
				});
	}

	public void refleshQueue(List<AttendDorm> dorms) {
		clear();
		for (AttendDorm dorm : dorms) {
			dormQueue.add(dorm.getId());
		}
	}

	public void remove(String dormId) {
		if (dormQueue.contains(dormId)) {
			dormQueue.remove(dormId);
		}
	}

	public void add(String dormId) {
		if (!dormQueue.contains(dormId)) {
			dormQueue.add(dormId);
		}
	}

	public void clear() {
		if (dormQueue.size() > 0) {
			dormQueue.clear();
		}
	}

	public boolean queuedDorm(String dormId) {
		if (dormQueue.contains(dormId)) {
			clear();
			return true;
		}
		return false;
	}
}
