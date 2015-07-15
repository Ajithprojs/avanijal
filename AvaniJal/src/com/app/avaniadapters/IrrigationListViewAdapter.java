package com.app.avaniadapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.avanstart.R;

import java.util.ArrayList;

/**
 * Created by vsivaraj on 05-07-2015.
 */
public class IrrigationListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    ArrayList<String> valve;
    Context mContext;

    public IrrigationListViewAdapter(Context mContext, ArrayList<String> valve) {
        this.valve = valve;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return valve.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root = convertView;
        ViewHolder mViewHolder;
        if (convertView == null) {
            root = inflater.inflate(R.layout.activity_irrigation_list_view, null);
            mViewHolder = new ViewHolder();
            mViewHolder.txtTitle = (TextView) root.findViewById(R.id.irrigation_listview_txt);
            mViewHolder.editText = (EditText) root.findViewById(R.id.irrigation_listview_edit);
            mViewHolder.delete = (ImageButton) root.findViewById(R.id.delete_valve);
            mViewHolder.delete.setTag(position);
            root.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) root.getTag();
        }
        mViewHolder.txtTitle.setText(valve.get(position));
        mViewHolder.delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                new AlertDialog.Builder(mContext).setTitle("Alert Dialog")
                        .setMessage("Are you sure you want to delete this valve ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Integer index = (Integer) v.getTag();
                                valve.remove(index.intValue());
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert).show();

            }
        });
        return root;
    }

    static class ViewHolder {
        TextView txtTitle;
        EditText editText;
        ImageButton delete;
    }
}
