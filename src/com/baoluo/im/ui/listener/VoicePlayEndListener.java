package com.baoluo.im.ui.listener;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;

import com.baoluo.community.support.voice.RecordHelp;
import com.baoluo.community.util.L;

/**
 * IM语音消息 播放结束事件
 * Created by tao.lai on 2015/10/24 0024.
 */
public class VoicePlayEndListener implements MediaPlayer.OnCompletionListener{
    private AnimationDrawable animate;

    public VoicePlayEndListener(AnimationDrawable animationDrawable) {
        this.animate = animationDrawable;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        L.i("VoicePlayEndListener", "im voice msg play end.");
        RecordHelp.getInstance().stopPlayRecord();
        this.animate.stop();
        this.animate.selectDrawable(0);
    }
}