package com.juxdun.secret.atys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.juxdun.secret.R;
import com.juxdun.secret.net.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息列表adapter
 * Created by Juxdun on 2015/2/28.
 */
public class TimelineMessageListAdapter extends BaseAdapter {

    public TimelineMessageListAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Message getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.timeline_list_cell, null);
            // 通过setTag绑定一个ListCell，就不用每次findViewById耗费资源
            convertView.setTag(new ListCell((TextView) convertView.findViewById(R.id.tvCellLabel)));
        }
        ListCell lc = (ListCell) convertView.getTag();

        Message msg = getItem(position);
        lc.getTvCellLabel().setText(msg.getMsg());

        return convertView;
    }

    /**
     * 添加List<Message>到adapter
     * @param data 列表
     */
    public void addAll(List<Message> data){
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 清空列表
     */
    public void clear(){
        data.clear();
        notifyDataSetChanged();
    }

    private List<Message> data = new ArrayList<Message>();
    private Context context = null;

    // 减少资源耗费
    private static class ListCell{
        public ListCell(TextView tvCellLabel){
            this.tvCellLabel = tvCellLabel;
        }

        private TextView tvCellLabel;
        public TextView getTvCellLabel() {
            return tvCellLabel;
        }
    }
}
