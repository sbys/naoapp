package com.example.yuanshuai.nao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yuanshuai.nao.R;
import com.example.yuanshuai.nao.model.Family;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanshuai on 2018/4/7.
 */

public class MyAdapter extends BaseAdapter {
    private List<Family> list=new ArrayList<Family>();
    private Context context;
    public MyAdapter(List list,Context context) {
        super();
        this.list=list;
        this.context=context;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.fitem,parent,false);
        TextView textView=(TextView)view.findViewById(R.id.text);
        textView.setText(list.get(position).getFname());
        return view;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
}
