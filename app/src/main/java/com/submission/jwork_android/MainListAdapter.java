package com.submission.jwork_android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.submission.jwork_android.constructor.Job;
import com.submission.jwork_android.constructor.Recruiter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<Recruiter> _listDataHeader;
    private HashMap<Recruiter, ArrayList<Job>> _listDataChild;

    public MainListAdapter(Context context, ArrayList<Recruiter> listDataHeader,
                           HashMap<Recruiter, ArrayList<Job>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final Recruiter headerTitle = (Recruiter) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.layout_recruiter, null);
        }

        TextView ListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        ListHeader.setTypeface(null, Typeface.BOLD);
        ListHeader.setText("Recruiter " + headerTitle.getName());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Job childText = (Job) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout_job, null);
        }
        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        String s = childText.getName() + ", Fee : " + childText.getFee();
        txtListChild.setText(s);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
