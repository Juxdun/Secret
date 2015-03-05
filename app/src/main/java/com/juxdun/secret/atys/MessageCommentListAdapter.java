package com.juxdun.secret.atys;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.juxdun.secret.R;
import com.juxdun.secret.net.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论的列表adapter
 * Created by Juxdun on 2015/2/28.
 */
public class MessageCommentListAdapter extends BaseAdapter {

    public MessageCommentListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Comment getItem(int position) {
        return comments.get(position);
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

        Comment comm = getItem(position);
        lc.getTvCellLabel().setText(comm.getContent());

        return convertView;
    }

    private List<Comment> comments = new ArrayList<Comment>();
    private Context context = null;

    /**
     * 添加列表数据到adapter
     * @param data 列表数据
     */
    public void addAll(List<Comment> data){
        comments.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 清空列表数据
     */
    public void clear(){
        comments.clear();
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

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
