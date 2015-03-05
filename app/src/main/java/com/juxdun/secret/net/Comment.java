package com.juxdun.secret.net;

/**
 * 实体类
 * 评论
 * Created by Juxdun on 2015/2/28.
 */
public class Comment {
    public Comment(String content, String phone_md5) {
        this.content = content;
        this.phone_md5 = phone_md5;
    }

    private String content, phone_md5;
    public String getContent() {
        return content;
    }

    public String getPhone_md5() {
        return phone_md5;
    }
}
