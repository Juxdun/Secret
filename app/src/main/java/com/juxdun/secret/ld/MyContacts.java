package com.juxdun.secret.ld;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.juxdun.secret.Config;
import com.juxdun.secret.tools.MD5Tool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Juxdun on 2015/2/25.
 */
public class MyContacts {

    /**
     * 取联系人数据，包装成JSON字符串数组
     * 格式参照文档
     *
     * @param context
     * @return 联系人的JSON字符串
     */
    public static String getContactsJSONString(Context context) {
        Cursor cursor = context.getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);
        JSONArray jsonArr = new JSONArray();
        JSONObject obj;

        String phoneNum;
        while (cursor.moveToNext()) {
            phoneNum = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
            if (phoneNum.startsWith("+86")) {
                phoneNum = phoneNum.substring(3);
            }
            // 添加到json数组
            obj = new JSONObject();
            try {
                obj.put(Config.KEY_PHONE_MD5, MD5Tool.md5(phoneNum));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArr.put(obj);
        }
        System.out.println(jsonArr.toString());
        cursor.close();
        return jsonArr.toString();
    }
}
