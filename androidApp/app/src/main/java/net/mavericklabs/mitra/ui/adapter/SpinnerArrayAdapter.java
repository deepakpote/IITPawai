/*
 *
 *  * ************************************************************************
 *  *
 *  *  MAVERICK LABS CONFIDENTIAL
 *  *  __________________
 *  *
 *  *   [2015] Maverick Labs
 *  *   All Rights Reserved.
 *  *
 *  *  NOTICE:  All information contained herein is, and remains
 *  *  the property of Maverick Labs and its suppliers,
 *  *  if any.  The intellectual and technical concepts contained
 *  *  herein are proprietary to Maverick Labs
 *  *  and its suppliers and may be covered by U.S. and Foreign Patents,
 *  *  patents in process, and are protected by trade secret or copyright law.
 *  *  Dissemination of this information or reproduction of this material
 *  *  is strictly forbidden unless prior written permission is obtained
 *  *  from Maverick Labs.
 *  * /
 *
 */

package net.mavericklabs.mitra.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.utils.Logger;

import java.util.List;

/**
 * Created by vishakha on 24/11/16.
 */

public class SpinnerArrayAdapter extends ArrayAdapter<CommonCode> {
    private Context context;
    List<CommonCode> codes;

    public SpinnerArrayAdapter(Context context, int resource, List<CommonCode> objects) {
        super(context, resource, objects);
        this.context = context;
        codes = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Logger.d("view position " + position);
        return getCustomView(position, convertView, parent, R.layout.custom_spinner_item_header);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //Logger.d("dropdownview position " + position);
        return getCustomView(position, convertView, parent, R.layout.custom_spinner_dropdown_item);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent, int layoutResourceID) {
        View row = convertView;
        CodeHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceID, parent, false);

            holder = new CodeHolder();
            holder.textView = (TextView)row.findViewById(android.R.id.text1);

            row.setTag(holder);
        }
        else {
            holder = (CodeHolder) row.getTag();
        }

        CommonCode commonCode = codes.get(position);
        holder.textView.setText(commonCode.getCodeNameForCurrentLocale());

        return row;
    }

    static class CodeHolder
    {
        TextView textView;
    }
}
