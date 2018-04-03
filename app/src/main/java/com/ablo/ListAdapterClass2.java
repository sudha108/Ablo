package com.ablo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sudhams on 20/02/18.
 */

public class ListAdapterClass2  extends BaseAdapter {

    Context context;
    List<Applications> valueList;

    public ListAdapterClass2(List<Applications> listValue, Context context)
    {
        this.context = context;
        this.valueList = listValue;
    }

    @Override
    public int getCount()
    {
        return this.valueList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return this.valueList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewItem2 viewItem = null;

        if(convertView == null)
        {
            viewItem = new ViewItem2();

            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.listview_item2, null);

            viewItem.TextViewUserName = (TextView)convertView.findViewById(R.id.applistname);
            viewItem.TextViewLimitTime = (TextView)convertView.findViewById(R.id.listlimit);

            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItem2) convertView.getTag();
        }

        viewItem.TextViewUserName.setText(valueList.get(position).appname);
        viewItem.TextViewLimitTime.setText(valueList.get(position).limittime);

        return convertView;
    }
}

class ViewItem2
{
    TextView TextViewUserName;
    TextView TextViewLimitTime;


}
