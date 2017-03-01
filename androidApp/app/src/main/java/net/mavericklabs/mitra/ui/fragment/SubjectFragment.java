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
import android.widget.CheckedTextView;
import android.widget.ListView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.listener.OnDialogFragmentDismissedListener;
import net.mavericklabs.mitra.model.BaseObject;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.ui.adapter.SubjectAndGradeFragmentListAdapter;
import net.mavericklabs.mitra.utils.CommonCodeGroup;
import net.mavericklabs.mitra.utils.EditProfileDialogFragment;
import net.mavericklabs.mitra.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by amoghpalnitkar on 12/3/16.
 */

public class SubjectFragment extends DialogFragment {
    private OnDialogFragmentDismissedListener onDialogFragmentDismissedListener;
    private List<BaseObject> objects;
    private List<Integer> selectedSubjectCodeIds;

    @BindView(R.id.subject_or_grade_list_view)
    ListView subjectListView;


    public SubjectFragment() {

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

        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.subject_s);
        }


        selectedSubjectCodeIds = getArguments().getIntegerArrayList("selected_subject_code_ids");
        objects = getSubjectsList();
        subjectListView.setAdapter(new SubjectAndGradeFragmentListAdapter(getContext(),android.R.layout.simple_list_item_multiple_choice,objects));
        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView textView = (CheckedTextView) view.findViewById(android.R.id.text1);
                if (!textView.isChecked()) {
                    textView.setChecked(true);
                    objects.get(i).setChecked(true);
                } else {
                    textView.setChecked(false);
                    objects.get(i).setChecked(false);
                }
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_action_next_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_next_fragment) {
            List<BaseObject> checkedItems = new ArrayList<>();
            for (BaseObject object : objects) {
                if(object.isChecked()) {
                    checkedItems.add(object);
                }
            }
            if(((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.my_profile_title));
            }
            onDialogFragmentDismissedListener.onDialogFragmentDismissed(checkedItems, EditProfileDialogFragment.ADD_SUBJECT);
            dismiss();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(((AppCompatActivity)getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.my_profile_title));
        }

    }


    private List<BaseObject> getSubjectsList() {
        List<BaseObject> objectList = new ArrayList<>();
        RealmResults<CommonCode> subjectListResult = Realm.getDefaultInstance().where(CommonCode.class)
                .equalTo("codeGroupID", CommonCodeGroup.SUBJECTS).findAll();

        List<CommonCode>  subjectsList = new ArrayList<>(subjectListResult);

        for(CommonCode commonCode : subjectsList) {
            BaseObject object= new BaseObject(commonCode,false);
            for(Integer selectedSubjectCodeId : selectedSubjectCodeIds) {
                if(object.getCommonCode().getCodeID().equals(selectedSubjectCodeId)) {
                    object.setChecked(true);
                }
            }
            objectList.add(object);
        }
        return objectList;
    }
}
