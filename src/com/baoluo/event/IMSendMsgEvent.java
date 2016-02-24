package com.baoluo.event;

import com.baoluo.im.entity.Msg;

/**
 * Created by tao.lai on 2015/9/24 0024.
 */
public class IMSendMsgEvent {

    private Msg msg;
    private String fileLocalUrl;

    public IMSendMsgEvent(Msg msg) {
        this.msg = msg;
    }

    public IMSendMsgEvent(Msg msg,String fileLocalUrl) {
        this.msg = msg;
        this.fileLocalUrl = fileLocalUrl;
    }

    public Msg getMsg() {
        return msg;
    }

    public String getFileLocalUrl(){
        return fileLocalUrl;
    }
}
