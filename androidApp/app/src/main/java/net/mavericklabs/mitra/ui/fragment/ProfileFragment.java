package net.mavericklabs.mitra.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.database.model.DbGrade;
import net.mavericklabs.mitra.database.model.DbSubject;
import net.mavericklabs.mitra.database.model.DbUser;
import net.mavericklabs.mitra.model.BaseObject;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.ui.activity.EditProfileActivity;
import net.mavericklabs.mitra.ui.adapter.ProfileActivityGradesAdapter;
import net.mavericklabs.mitra.ui.adapter.ProfileActivitySubjectsAdapter;
import net.mavericklabs.mitra.ui.custom.CropCircleTransformation;
import net.mavericklabs.mitra.utils.CommonCodeUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by amoghpalnitkar on 14/11/16.
 */

public class ProfileFragment extends Fragment {

    @BindView(R.id.profile_photo_image_view)
    ImageView profilePhotoImageView;

    @BindView(R.id.user_name)
    TextView userNameTextView;

    @BindView(R.id.designation_and_district_text)
    TextView designationAndDistrictTextView;

    @BindView(R.id.subject_recycler_view)
    RecyclerView subjectRecyclerView;

    @BindView(R.id.grade_recycler_view)
    RecyclerView gradeRecyclerView;

    public ProfileFragment() {
        super();
        //mandatory constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_profile,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        RealmResults<DbUser> dbUser = Realm.getDefaultInstance()
                .where(DbUser.class).findAll();
        if(dbUser.size() == 1) {
            DbUser user = dbUser.get(0);
            userNameTextView.setText(user.getName());
            CommonCode userType = CommonCodeUtils.getObjectFromCode(user.getUserType());
            CommonCode district = CommonCodeUtils.getObjectFromCode(user.getDistrict());
            designationAndDistrictTextView.setText(getString(R.string.designation_and_district,
                                                                userType.getCodeNameForCurrentLocale(),
                                                                district.getCodeNameForCurrentLocale()));
            subjectRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                                                        LinearLayoutManager.HORIZONTAL,false));
            RealmList<DbSubject> dbSubjects = user.getSubjects();
            List<BaseObject> subjectList = new ArrayList<>();
            for(DbSubject subject : dbSubjects) {
                subjectList.add(new BaseObject(CommonCodeUtils.
                                                    getObjectFromCode(subject.getSubjectCommonCode()),
                                                    false));
            }
            subjectRecyclerView.setAdapter(new ProfileActivitySubjectsAdapter(subjectList));

            gradeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                    LinearLayoutManager.HORIZONTAL,false));
            RealmList<DbGrade> dbGrades = user.getGrades();
            List<BaseObject> gradeList = new ArrayList<>();
            for(DbGrade grade : dbGrades) {
                gradeList.add(new BaseObject(CommonCodeUtils.getObjectFromCode(grade.getGradeCommonCode()),
                                        false));
            }
            gradeRecyclerView.setAdapter(new ProfileActivityGradesAdapter(gradeList));
        }
        Glide.with(this).
                load(R.drawable.placeholder_user).
                bitmapTransform(new CropCircleTransformation(getContext())).into(profilePhotoImageView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_action_edit,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_edit) {
            Intent editProfile = new Intent(getContext(), EditProfileActivity.class);
            startActivity(editProfile);
        }
        return super.onOptionsItemSelected(item);
    }
}
