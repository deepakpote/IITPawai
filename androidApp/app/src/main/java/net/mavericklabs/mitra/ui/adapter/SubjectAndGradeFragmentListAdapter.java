package net.mavericklabs.mitra.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import net.mavericklabs.mitra.model.CommonCode;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.resource;

/**
 * Created by root on 27/11/16.
 */

public class SubjectAndGradeFragmentListAdapter extends ArrayAdapter<SubjectAndGradeFragmentListAdapter.SubjectAndGradeObject> {

    private List<SubjectAndGradeObject> itemList;
    private Context context;
    private int resource;

    public SubjectAndGradeFragmentListAdapter(Context context, int resource, List<SubjectAndGradeObject> objects) {
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
        SubjectAndGradeObject object = itemList.get(position);
        CheckedTextView checkedTextView = (CheckedTextView) convertView.findViewById(android.R.id.text1);

        checkedTextView.setText(object.commonCode.getCodeNameForCurrentLocale());
        if(object.isChecked()) {
            checkedTextView.setChecked(true);
        } else {
            checkedTextView.setChecked(false);
        }
        return convertView;
    }

    public static class SubjectAndGradeObject implements Serializable{
        private CommonCode commonCode;
        private boolean isChecked;

        public SubjectAndGradeObject(CommonCode commonCode, boolean isChecked) {
            this.commonCode = commonCode;
            this.isChecked = isChecked;
        }

        public CommonCode getCommonCode() {
            return commonCode;
        }

        public void setCommonCode(CommonCode commonCode) {
            this.commonCode = commonCode;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }
}
