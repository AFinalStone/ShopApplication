package com.shi.xianglixiangqin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.model.FunctionTypeModel;

import java.util.List;

/**
 * Created by SHI on 2016/8/27 17:31
 */
public class FunctionTypeAdapter extends MyBaseAdapter<FunctionTypeModel>{

    public FunctionTypeAdapter(Context mContext, List listData) {
        super(mContext, listData);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.item_adapter_function_type, null);
            holder = new ViewHolder();
            holder.iv_functionType = (ImageView) convertView
                    .findViewById(R.id.iv_functionType);
            holder.tv_functionType = (TextView) convertView
                    .findViewById(R.id.tv_functionType);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        FunctionTypeModel currentFunctionTypeModel = listData.get(position);
        holder.iv_functionType.setImageResource(currentFunctionTypeModel.getImageUrl());
        holder.tv_functionType.setText(currentFunctionTypeModel.getName());
        return convertView;
    }

    private class ViewHolder {
        /** 功能图标 **/
        ImageView iv_functionType;
        /** 功能名称 **/
        TextView tv_functionType;
    }
}
