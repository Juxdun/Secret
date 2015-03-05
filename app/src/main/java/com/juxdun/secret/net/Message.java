package com.juxdun.secret.net;

/**
 * 实体类
 * 消息
 * Created by Juxdun on 2015/2/28.
 */
public class Message {

    public Message(String msgId, String msg, String phone_md5) {
        this.msgId = msgId;
        this.msg = msg;
        this.phone_md5 = phone_md5;
    }

    private String msgId=null,msg=null,phone_md5=null;

    public String getMsgId() {
        return msgId;
    }

    public String getMsg() {
        return msg;
    }

    public String getPhone_md5() {
        return phone_md5;
    }
}
