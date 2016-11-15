package net.mavericklabs.mitra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.listener.OnDialogFragmentDismissedListener;
import net.mavericklabs.mitra.utils.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 14/11/16.
 */

public class ProfileFragment extends Fragment implements OnDialogFragmentDismissedListener{

    private boolean isAdditionalViewExpanded;

    @BindView(R.id.more_or_less_button)
    Button moreOrLessButtun;
    @BindView(R.id.more_layout)
    LinearLayout moreLayout;
    @BindView(R.id.subject_recycler_view)
    RecyclerView subjectRecyclerView;
    @BindView(R.id.grade_recycler_view) RecyclerView gradeRecyclerView;
    @BindView(R.id.more_image)
    ImageView moreImage;
    @BindView(R.id.less_image) ImageView lessImage;
    @BindView(R.id.grade_placeholder_text)
    TextView gradePlaceholderTextView;
    @BindView(R.id.subject_placeholder_text) TextView subjectPlaceholderTextView;
    @BindView(R.id.i_am_spinner)
    Spinner iAmSpinner;
    @BindView(R.id.district_spinner) Spinner districtSpinner;

    @OnClick(R.id.add_grades)
    void showAddGradesMenu() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(android.R.id.content,new SubjectAndGradeFragment(),"grade_fragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.add_subjects)
    void showAddSubjectsMenu() {
        Logger.d("adding subjects..");
    }

    @OnClick(R.id.more_or_less_button)
    void expandOrCollapse() {
        if(isAdditionalViewExpanded) {
            moreOrLessButtun.setText(R.string.more);
            moreLayout.setVisibility(View.GONE);
            isAdditionalViewExpanded = false;
            moreImage.setVisibility(View.VISIBLE);
            lessImage.setVisibility(View.GONE);
        } else {
            moreOrLessButtun.setText(R.string.less);
            moreLayout.setVisibility(View.VISIBLE);
            isAdditionalViewExpanded = true;
            moreImage.setVisibility(View.GONE);
            lessImage.setVisibility(View.VISIBLE);
        }
    }

    public ProfileFragment() {
        super();
        //mandatory constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
    }

    @Override
    public void onDialogFragmentDismissed() {
        Logger.d("dialog dismissed..");
    }
}
