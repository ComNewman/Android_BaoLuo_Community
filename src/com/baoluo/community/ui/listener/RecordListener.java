package com.baoluo.community.ui.listener;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public abstract class RecordListener implements OnCompletionListener {
	@Override
	public void onCompletion(MediaPlayer mp) {
		recordPlayComplete();
	}
	
	public abstract void recordPlayComplete();
}
