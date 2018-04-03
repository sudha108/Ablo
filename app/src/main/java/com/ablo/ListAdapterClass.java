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

public class ListAdapterClass extends BaseAdapter {

    Context context;
    List<User> valueList;

    public ListAdapterClass(List<User> listValue, Context context)
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
        ViewItem viewItem = null;

        if(convertView == null)
        {
            viewItem = new ViewItem();

            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.listview_item, null);

            viewItem.TextViewUserName = (TextView)convertView.findViewById(R.id.listname);
            viewItem.TextViewisonline = (TextView)convertView.findViewById(R.id.listonline);

            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItem) convertView.getTag();
        }

        viewItem.TextViewUserName.setText(valueList.get(position).usersName);
        viewItem.TextViewisonline.setText(valueList.get(position).isonline);


        return convertView;
    }
}

class ViewItem
{
    TextView TextViewUserName;
    TextView TextViewisonline;
}