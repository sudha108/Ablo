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

public class ListAdapterClass3  extends BaseAdapter {

    Context context;
    List<RunApps> valueList;

    public ListAdapterClass3(List<RunApps> listValue, Context context)
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
        ViewItem3 viewItem = null;

        if(convertView == null)
        {
            viewItem = new ViewItem3();

            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.listview_item3, null);

            viewItem.TextViewRunAppName = (TextView)convertView.findViewById(R.id.listappname);
            viewItem.TextViewRunTime = (TextView)convertView.findViewById(R.id.listruntime);

            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItem3) convertView.getTag();
        }

        viewItem.TextViewRunAppName.setText(valueList.get(position).runappname);
        viewItem.TextViewRunTime.setText(valueList.get(position).runtime);

        return convertView;
    }
}

class ViewItem3
{
    TextView TextViewRunAppName;
    TextView TextViewRunTime;


}
