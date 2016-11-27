package net.mavericklabs.mitra.ui.fragment;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import net.mavericklabs.mitra.listener.OnDialogFragmentDismissedListener;
import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.ui.adapter.SubjectAndGradeFragmentListAdapter;
import net.mavericklabs.mitra.utils.CommonCodeGroup;
import net.mavericklabs.mitra.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by root on 13/11/16.
 */

public class SubjectAndGradeFragment extends BaseDialogFragment {

    private OnDialogFragmentDismissedListener onDialogFragmentDismissedListener;

    public SubjectAndGradeFragment() {
        //mandatory constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDialogFragmentDismissedListener) {
            Logger.d("on attached is instanceof listener..");
            onDialogFragmentDismissedListener = ((OnDialogFragmentDismissedListener)context);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d("on create view called..");
        return inflater.inflate(R.layout.fragment_subject_and_grade,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.grade_s);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_next_fragment) {
            List<SubjectAndGradeFragmentListAdapter.SubjectAndGradeObject> checkedItems = new ArrayList<>();
            for (SubjectAndGradeFragmentListAdapter.SubjectAndGradeObject object : objects) {
                if(object.isChecked()) {
                    checkedItems.add(object);
                }
            }
            onDialogFragmentDismissedListener.onDialogFragmentDismissed(checkedItems);
            dismiss();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
