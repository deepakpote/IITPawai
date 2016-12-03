package net.mavericklabs.mitra.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.mavericklabs.mitra.api.RestClient;
import net.mavericklabs.mitra.api.model.BaseModel;
import net.mavericklabs.mitra.api.model.RegisterUser;
import net.mavericklabs.mitra.api.model.RegisterUserResponse;
import net.mavericklabs.mitra.database.model.DbGrade;
import net.mavericklabs.mitra.database.model.DbSubject;
import net.mavericklabs.mitra.database.model.DbUser;
import net.mavericklabs.mitra.listener.OnDialogFragmentDismissedListener;
import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.BaseObject;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.ui.adapter.ProfileActivityGradesAdapter;
import net.mavericklabs.mitra.ui.adapter.ProfileActivitySubjectsAdapter;
import net.mavericklabs.mitra.ui.adapter.SpinnerArrayAdapter;
import net.mavericklabs.mitra.ui.adapter.SubjectAndGradeFragmentListAdapter;
import net.mavericklabs.mitra.ui.custom.CropCircleTransformation;
import net.mavericklabs.mitra.ui.fragment.GradeFragment;
import net.mavericklabs.mitra.ui.fragment.SubjectFragment;
import net.mavericklabs.mitra.utils.CommonCodeGroup;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.MitraSharedPreferences;
import net.mavericklabs.mitra.utils.StringUtils;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements OnDialogFragmentDismissedListener {

    private boolean isAdditionalViewExpanded;

    @BindView(R.id.more_or_less_button) Button moreOrLessButtun;
    @BindView(R.id.more_layout) LinearLayout moreLayout;
    @BindView(R.id.subject_recycler_view) RecyclerView subjectRecyclerView;
    @BindView(R.id.grade_recycler_view) RecyclerView gradeRecyclerView;
    @BindView(R.id.more_image) ImageView moreImage;
    @BindView(R.id.less_image) ImageView lessImage;
    @BindView(R.id.grade_placeholder_text) TextView gradePlaceholderTextView;
    @BindView(R.id.subject_placeholder_text) TextView subjectPlaceholderTextView;
    @BindView(R.id.i_am_spinner) Spinner userTypeSpinner;
    @BindView(R.id.district_spinner) Spinner districtSpinner;
    @BindView(R.id.profile_photo_image_view) ImageView profilePhotoImageView;
    @BindView(R.id.name_edit_text) EditText nameEditText;

    private Uri imageCaptureUri;
    private final int PICK_PROFILE = 0;
    private Context context;
    private String otp;
    private SpinnerArrayAdapter districtAdapter;
    private List<BaseObject> selectedGradesList = new ArrayList<>();
    private List<BaseObject> selectedSubjectsList = new ArrayList<>();
    private boolean isGradeFragmentOpen;
    private boolean isSubjectFragmentOpen;

    @OnClick(R.id.profile_photo_image_view)
    void pickProfilePhoto() {
        File file = new File(Environment.getExternalStorageDirectory(),
                "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageCaptureUri = Uri.fromFile(file);

        try {
            Intent pickIntent = new Intent();
            pickIntent.setType("image/*");
            pickIntent.setAction(Intent.ACTION_PICK);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageCaptureUri);

            Intent chooserIntent = Intent.createChooser(pickIntent, "Choose profile photo");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
            startActivityForResult(chooserIntent, PICK_PROFILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.add_grades)
    void showAddGradesMenu() {
        isGradeFragmentOpen = true;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment gradeFragment = new GradeFragment();
        Bundle bundle = new Bundle();
        List<BaseObject> gradesList = getGradesList();
        bundle.putSerializable("grades_list", (Serializable) gradesList);
        gradeFragment.setArguments(bundle);

        fragmentTransaction.add(android.R.id.content,gradeFragment,"ADD_GRADES");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.add_subjects)
    void showAddSubjectsMenu() {
        isSubjectFragmentOpen = true;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        List<BaseObject> subjectsList = getSubjectsList();
        Bundle bundle = new Bundle();
        bundle.putSerializable("subjects_list", (Serializable) subjectsList);

        Fragment subjectFragment = new SubjectFragment();
        subjectFragment.setArguments(bundle);
        fragmentTransaction.add(android.R.id.content,subjectFragment,"ADD_SUBJECT");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        RealmResults<CommonCode> subjectList = Realm.getDefaultInstance().where(CommonCode.class)
                .equalTo("codeGroupID", CommonCodeGroup.SUBJECTS).findAll();

        Logger.d("subjectList size: " + subjectList.size());


        otp = MitraSharedPreferences.readFromPreferences(getApplicationContext(), "OTP", "");
        Logger.d(" otp "  + otp);

        this.context = getApplicationContext();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        subjectRecyclerView.setHasFixedSize(true);
        subjectRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        subjectRecyclerView.setAdapter(new ProfileActivitySubjectsAdapter(selectedSubjectsList));

        gradeRecyclerView.setHasFixedSize(true);
        gradeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        gradeRecyclerView.setAdapter(new ProfileActivityGradesAdapter(selectedGradesList));

        RealmResults<CommonCode> iAmList = Realm.getDefaultInstance().where(CommonCode.class)
                .equalTo("codeGroupID", CommonCodeGroup.USER_TYPE).findAll();
        List<CommonCode> userTypeList = new ArrayList<>(iAmList);

        RealmResults<CommonCode> districtList = Realm.getDefaultInstance().where(CommonCode.class)
                .equalTo("codeGroupID", CommonCodeGroup.DISTRICT).findAll();
        Logger.d(" list " + districtList.size());
        List<CommonCode> districts = new ArrayList<>(districtList);

        //Header - not a valid value
        districts.add(0, new CommonCode("0", "0",getString(R.string.select), getString(R.string.select), 0));
        userTypeList.add(0,new CommonCode("0","0",getString(R.string.select),getString(R.string.select),0));

        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(EditProfileActivity.this,R.layout.custom_spinner_item_header,userTypeList);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);

        districtAdapter = new SpinnerArrayAdapter(EditProfileActivity.this,
                R.layout.custom_spinner_dropdown_item, districts);
        districtSpinner.setAdapter(districtAdapter);

        Glide.with(this).load(R.drawable.placeholder_user).bitmapTransform(new CropCircleTransformation(getApplicationContext())).into(profilePhotoImageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_next,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_next) {

            if(isValidInformation()) {
                String phoneNumber = UserDetailUtils.getMobileNumber(getApplicationContext());
                RegisterUser user = new RegisterUser(nameEditText.getText().toString() ,otp, phoneNumber, getSelectedDistrictID(), getSelectedUserTypeId());
                final DbUser dbUser = new DbUser(nameEditText.getText().toString(),getSelectedUserTypeId(),getSelectedDistrictID());

                if(!selectedGradesList.isEmpty()) {
                    List<String> gradeCodeList = getGradeCodeList();

                    //format grades to store into database
                    RealmList<DbGrade> dbGrades = new RealmList<>();
                    for(String commonCode : gradeCodeList) {
                        dbGrades.add(new DbGrade(commonCode));
                    }
                    dbUser.setGrades(dbGrades);

                    //format grades to be sent to server
                    String grades = StringUtils.stringify(gradeCodeList);
                    user.setGradeCodeIds(grades);
                }

                if(!selectedSubjectsList.isEmpty()) {
                    List<String> subjectCodeList = getSubjectCodeList();

                    //format subject to store into database
                    RealmList<DbSubject> dbSubjects = new RealmList<>();
                    for(String commonCode : subjectCodeList) {
                        dbSubjects.add(new DbSubject(commonCode));
                    }
                    dbUser.setSubjects(dbSubjects);

                    //format subject to be sent to server
                    String subjects = StringUtils.stringify(subjectCodeList);
                    user.setSubjectCodeIds(subjects);
                }

                RestClient.getApiService("").registerUser(user).enqueue(new Callback<BaseModel<RegisterUserResponse>>() {
                    @Override
                    public void onResponse(Call<BaseModel<RegisterUserResponse>> call, Response<BaseModel<RegisterUserResponse>> response) {
                        if(response.isSuccessful()) {
                            if(response.body().getData() != null) {

                                RegisterUserResponse serverResponse = response.body().getData().get(0);
                                dbUser.setId(serverResponse.getUserID());

                                //write current user to database
                                Realm realm = Realm.getDefaultInstance();
                                realm.beginTransaction();
                                realm.copyToRealm(dbUser);
                                realm.commitTransaction();
                                

                                //store userId,token in shared preferences
                                String token = serverResponse.getToken();
                                UserDetailUtils.saveUserId(serverResponse.getUserID(), getApplicationContext());
                                UserDetailUtils.saveToken(token,getApplicationContext());

                                //move to next activity
                                Intent verifyOtp = new Intent(EditProfileActivity.this,HomeActivity.class);
                                startActivity(verifyOtp);
                                finishAffinity();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseModel<RegisterUserResponse>> call, Throwable t) {
                        Logger.d(" on fail");
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(),"Please enter required fields.",Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<String> getSubjectCodeList() {
        List<String> subjectCodeList = new ArrayList<>();
        for (BaseObject subjectObject : selectedSubjectsList) {
            subjectCodeList.add(subjectObject.getCommonCode().getCodeID());
        }
        return subjectCodeList;
    }

    private List<String> getGradeCodeList() {
        List<String> gradeCodeList = new ArrayList<>();
        for(BaseObject gradeObject : selectedGradesList) {
            gradeCodeList.add(gradeObject.getCommonCode().getCodeID());
        }
        return gradeCodeList;
    }

    @Override
    public void onDialogFragmentDismissed(List<BaseObject> checkedItemsList) {
        if(isGradeFragmentOpen) {
            isGradeFragmentOpen = false;
            selectedGradesList.clear();
            for(BaseObject checkedItem : checkedItemsList) {
                selectedGradesList.add(checkedItem);
            }
            if(!selectedGradesList.isEmpty()) {
                gradePlaceholderTextView.setVisibility(View.GONE);
                gradeRecyclerView.getAdapter().notifyDataSetChanged();
            }
        } else if (isSubjectFragmentOpen) {
            isSubjectFragmentOpen = false;
            selectedSubjectsList.clear();
            for(BaseObject checkedItem : checkedItemsList) {
                selectedSubjectsList.add(checkedItem);
            }
            if(!selectedSubjectsList.isEmpty()) {
                subjectPlaceholderTextView.setVisibility(View.GONE);
                subjectRecyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //nothing is selected by the user. boo! return after setting placeholder icon again.
        if(resultCode == 0) {
            Glide.with(this).load(R.drawable.placeholder_user).
                    bitmapTransform(new CropCircleTransformation(getApplicationContext())).
                    into(profilePhotoImageView);
            return;
        }

        //else proceed
        if (requestCode == PICK_PROFILE) {
            Logger.d("data is : " + data);
            if(data != null) {
                imageCaptureUri = data.getData();
                if(imageCaptureUri != null) {
                    Glide.with(this).load(imageCaptureUri).
                            bitmapTransform(new CropCircleTransformation(getApplicationContext())).
                            into(profilePhotoImageView);
                }
            } else if(imageCaptureUri != null && imageCaptureUri.getPath() != null) {
                    File file = new File(imageCaptureUri.getPath());
                    Glide.with(this).load(Uri.fromFile(file)).
                            bitmapTransform(new CropCircleTransformation(getApplicationContext())).
                            into(profilePhotoImageView);
            }
        }
    }

    private boolean isValidInformation() {

        if(nameEditText.getText().length() == 0) {
            return false;
        } else if(getSelectedDistrictID().equals("0")) {
            return false;
        } else if(getSelectedUserTypeId().equals("0")) {
            return false;
        }
        return true;
    }

    private String getSelectedUserTypeId() {
        CommonCode userType = (CommonCode) userTypeSpinner.getSelectedItem();
        Logger.d(" name  " + userType.getCodeNameEnglish() + " " + userType.getCodeID());
        return userType.getCodeID();
    }

    private String getSelectedDistrictID() {
        CommonCode district = (CommonCode) districtSpinner.getSelectedItem();
        Logger.d(" name  " + district.getCodeNameEnglish() + " " + district.getCodeID());
        return district.getCodeID();
    }


    private List<BaseObject> getGradesList() {
        List<BaseObject> objectList = new ArrayList<>();
        RealmResults<CommonCode> gradeListResult = Realm.getDefaultInstance().where(CommonCode.class)
                .equalTo("codeGroupID", CommonCodeGroup.GRADES).findAll();
        List<CommonCode>  gradeList = new ArrayList<>(gradeListResult);
        for(CommonCode commonCode : gradeList) {
            BaseObject object= new BaseObject(commonCode,false);
            for(BaseObject selectedItem : selectedGradesList) {
                if(object.getCommonCode().getCodeID().equals(selectedItem.getCommonCode().getCodeID())) {
                    object.setChecked(true);
                }
            }
            Logger.d("object added to grade list : " + object.getCommonCode().getCodeNameForCurrentLocale());
            objectList.add(object);
        }
        return objectList;
    }

    private List<BaseObject> getSubjectsList() {
        List<BaseObject> objectList = new ArrayList<>();
        RealmResults<CommonCode> subjectListResult = Realm.getDefaultInstance().where(CommonCode.class)
                .equalTo("codeGroupID", CommonCodeGroup.SUBJECTS).findAll();

        List<CommonCode>  subjectsList = new ArrayList<>(subjectListResult);

        for(CommonCode commonCode : subjectsList) {
            BaseObject object= new BaseObject(commonCode,false);
            for(BaseObject selectedItem : selectedSubjectsList) {
                if(object.getCommonCode().getCodeID().equals(selectedItem.getCommonCode().getCodeID())) {
                    object.setChecked(true);
                }
            }
            objectList.add(object);
        }
        return objectList;
    }
}
