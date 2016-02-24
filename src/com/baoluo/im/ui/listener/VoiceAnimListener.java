package com.baoluo.im.ui.listener;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;

import com.baoluo.community.support.voice.RecordHelp;

/**
 * IM 语音消息
 * Created by tao.lai on 2015/10/24 0024.
 */
public class VoiceAnimListener implements View.OnClickListener {
    private AnimationDrawable anim;
    private String voicePath;

    public VoiceAnimListener(String voicePath,AnimationDrawable anim) {
        this.anim = anim;
        this.voicePath = voicePath;
    }

    @Override
    public void onClick(View v) {
        if (RecordHelp.getInstance().isPlaying()) {
            RecordHelp.getInstance().stopPlayRecord();
            this.anim.stop();
            this.anim.selectDrawable(0);
        } else {
            this.anim.start();
            RecordHelp.getInstance().playRecord(this.voicePath,new VoicePlayEndListener(this.anim));
        }

    }
}
