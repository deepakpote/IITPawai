package net.mavericklabs.mitra.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import net.mavericklabs.mitra.model.BaseObject;

import java.util.List;

/**
 * Created by amoghpalnitkar on 27/11/16.
 */

public class SubjectAndGradeFragmentListAdapter extends ArrayAdapter<BaseObject> {

    private List<BaseObject> itemList;
    private Context context;
    private int resource;

    public SubjectAndGradeFragmentListAdapter(Context context, int resource, List<BaseObject> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.itemList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);
        }
        BaseObject object = itemList.get(position);
        CheckedTextView checkedTextView = (CheckedTextView) convertView.findViewById(android.R.id.text1);

        checkedTextView.setText(object.getCommonCode().getCodeNameForCurrentLocale());
        if(object.isChecked()) {
            checkedTextView.setChecked(true);
        } else {
            checkedTextView.setChecked(false);
        }
        return convertView;
    }
}
