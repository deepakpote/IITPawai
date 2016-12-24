package net.mavericklabs.mitra.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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
import net.mavericklabs.mitra.api.model.EditPhoto;
import net.mavericklabs.mitra.api.model.EditUser;
import net.mavericklabs.mitra.api.model.GenericListDataModel;
import net.mavericklabs.mitra.api.model.RegisterUser;
import net.mavericklabs.mitra.api.model.RegisterUserResponse;
import net.mavericklabs.mitra.database.model.DbGrade;
import net.mavericklabs.mitra.database.model.DbSubject;
import net.mavericklabs.mitra.database.model.DbTopic;
import net.mavericklabs.mitra.database.model.DbUser;
import net.mavericklabs.mitra.listener.OnDialogFragmentDismissedListener;
import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.BaseObject;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.ui.adapter.ChipLayoutAdapter;
import net.mavericklabs.mitra.ui.adapter.ProfileActivitySubjectsAdapter;
import net.mavericklabs.mitra.ui.adapter.SpinnerArrayAdapter;
import net.mavericklabs.mitra.ui.custom.CropCircleTransformation;
import net.mavericklabs.mitra.ui.fragment.GradeFragment;
import net.mavericklabs.mitra.ui.fragment.SubjectFragment;
import net.mavericklabs.mitra.ui.fragment.TopicFragment;
import net.mavericklabs.mitra.utils.CommonCodeGroup;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.EditProfileDialogFragment;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.MitraSharedPreferences;
import net.mavericklabs.mitra.utils.StringUtils;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    @BindView(R.id.topic_placeholder_text) TextView topicPlaceholderTextView;
    @BindView(R.id.i_am_spinner) Spinner userTypeSpinner;
    @BindView(R.id.district_spinner) Spinner districtSpinner;
    @BindView(R.id.profile_photo_image_view) ImageView profilePhotoImageView;
    @BindView(R.id.name_edit_text) EditText nameEditText;
    @BindView(R.id.udise_edit_text) EditText udiseEditText;
    @BindView(R.id.topic_recycler_view) RecyclerView topicRecyclerView;

    private Uri imageCaptureUri;
    private final int PICK_PROFILE_PHOTO = 0;
    private String otp;
    private SpinnerArrayAdapter districtAdapter;
    private List<BaseObject> selectedGradesList = new ArrayList<>();
    private List<BaseObject> selectedSubjectsList = new ArrayList<>();
    private List<BaseObject> selectedTopicsList = new ArrayList<>();
    private String profilePhotoPath;
    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 33;

    @OnClick(R.id.profile_photo_image_view)
    void pickProfilePhoto() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(EditProfileActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(EditProfileActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        } else {
            showPhotoPicker();
        }

    }

    @OnClick(R.id.add_grades)
    void showAddGradesMenu() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment gradeFragment = new GradeFragment();
        Bundle bundle = new Bundle();
        ArrayList<Integer> selectedCodeIds = getSelectedGradeCodeIds();
        bundle.putIntegerArrayList("selected_grade_code_ids",selectedCodeIds);
        gradeFragment.setArguments(bundle);

        fragmentTransaction.setCustomAnimations(R.anim.anim_in, R.anim.anim_out, R.anim.anim_in, R.anim.anim_out);
        fragmentTransaction.add(android.R.id.content,gradeFragment,"ADD_GRADES");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.add_subjects)
    void showAddSubjectsMenu() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        Fragment subjectFragment = new SubjectFragment();
        ArrayList<Integer> selectedCodeIds = getSelectedSubjectCodeIds();
        bundle.putIntegerArrayList("selected_subject_code_ids",selectedCodeIds);
        subjectFragment.setArguments(bundle);

        fragmentTransaction.setCustomAnimations(R.anim.anim_in, R.anim.anim_out, R.anim.anim_in, R.anim.anim_out);
        fragmentTransaction.add(android.R.id.content,subjectFragment,"ADD_SUBJECT");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.add_topic)
    void showAddTopicsMenu() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        Fragment topicFragment = new TopicFragment();
        ArrayList<Integer> selectedCodeIds = getSelectedTopicCodeIds();
        bundle.putIntegerArrayList("selected_topic_code_ids",selectedCodeIds);
        topicFragment.setArguments(bundle);

        fragmentTransaction.setCustomAnimations(R.anim.anim_in, R.anim.anim_out, R.anim.anim_in, R.anim.anim_out);
        fragmentTransaction.add(android.R.id.content,topicFragment,"ADD_TOPIC");
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showPhotoPicker();
                }
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        otp = MitraSharedPreferences.readFromPreferences(getApplicationContext(), "OTP", "");

        RealmResults<CommonCode> iAmList = Realm.getDefaultInstance().where(CommonCode.class)
                .equalTo("codeGroupID", CommonCodeGroup.USER_TYPE).findAll();
        List<CommonCode> userTypeList = new ArrayList<>(iAmList);

        RealmResults<CommonCode> districtList = Realm.getDefaultInstance().where(CommonCode.class)
                .equalTo("codeGroupID", CommonCodeGroup.DISTRICT).findAll();
        Logger.d(" list " + districtList.size());
        List<CommonCode> districts = new ArrayList<>(districtList);

        //Header - not a valid value
        districts.add(0, new CommonCode(0, 0,getString(R.string.select), getString(R.string.select), 0));
        userTypeList.add(0,new CommonCode(0,0,getString(R.string.select),getString(R.string.select),0));

        SpinnerArrayAdapter adapter = new SpinnerArrayAdapter(EditProfileActivity.this,R.layout.custom_spinner_item_header,userTypeList);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);

        districtAdapter = new SpinnerArrayAdapter(EditProfileActivity.this,
                R.layout.custom_spinner_dropdown_item, districts);
        districtSpinner.setAdapter(districtAdapter);
        Glide.with(this).load(R.drawable.placeholder_user)
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                .into(profilePhotoImageView);

        RealmResults<DbUser> dbUser = Realm.getDefaultInstance()
                .where(DbUser.class).findAll();
        if(dbUser.size() == 1) {
            setDefaultValues(dbUser.get(0));
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        subjectRecyclerView.setHasFixedSize(true);
        subjectRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        subjectRecyclerView.setAdapter(new ProfileActivitySubjectsAdapter(selectedSubjectsList));

        gradeRecyclerView.setHasFixedSize(true);
        gradeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        gradeRecyclerView.setAdapter(new ChipLayoutAdapter(selectedGradesList));

        topicRecyclerView.setHasFixedSize(true);
        topicRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        topicRecyclerView.setAdapter(new ChipLayoutAdapter(selectedTopicsList));
    }

    private void setDefaultValues(DbUser dbUser) {
        nameEditText.setText(dbUser.getName());
        udiseEditText.setText(dbUser.getUdise());
        Integer userTypeId = dbUser.getUserType();
        userTypeSpinner.setSelection(getIndexForUserTypeSpinner(userTypeId));
        Integer spinnerId = dbUser.getDistrict();
        districtSpinner.setSelection(getIndexForDistrictSpinner(spinnerId));

        RealmList<DbSubject> dbSubjects = dbUser.getSubjects();
        List<BaseObject> subjectList = new ArrayList<>();
        for(DbSubject subject : dbSubjects) {
            subjectList.add(new BaseObject(CommonCodeUtils.
                    getObjectFromCode(subject.getSubjectCommonCode()),
                    false));
        }
        selectedSubjectsList = subjectList;
        if(!selectedSubjectsList.isEmpty()) {
            subjectPlaceholderTextView.setVisibility(View.GONE);
        }

        RealmList<DbGrade> dbGrades = dbUser.getGrades();
        List<BaseObject> gradeList = new ArrayList<>();
        for(DbGrade grade : dbGrades) {
            gradeList.add(new BaseObject(CommonCodeUtils.getObjectFromCode(grade.getGradeCommonCode()),
                    false));
        }
        selectedGradesList = gradeList;
        if(!selectedGradesList.isEmpty()) {
            gradePlaceholderTextView.setVisibility(View.GONE);
        }

        RealmList<DbTopic> dbTopics = dbUser.getTopics();
        List<BaseObject> topicList = new ArrayList<>();
        for(DbTopic topic : dbTopics) {
            topicList.add(new BaseObject(CommonCodeUtils.getObjectFromCode(topic.getTopicCommonCode()),
                    false));
        }
        selectedTopicsList = topicList;

        if(!selectedTopicsList.isEmpty()) {
            topicPlaceholderTextView.setVisibility(View.GONE);
        }

        if(!StringUtils.isEmpty(dbUser.getProfilePhotoPath())) {
            Glide.with(this).load(dbUser.getProfilePhotoPath())
                    .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                    .into(profilePhotoImageView);
        } else {
            Glide.with(this).load(R.drawable.placeholder_user).
                    bitmapTransform(new CropCircleTransformation(getApplicationContext())).
                    into(profilePhotoImageView);
        }
    }

    private int getIndexForDistrictSpinner(Integer spinnerId) {
        int i;
        for(i = 0 ; i < districtSpinner.getCount() ; i ++) {
            Integer id =((CommonCode)districtSpinner.getItemAtPosition(i)).getCodeID();
            if(id.equals(spinnerId)) {
                break;
            }
        }
        return i;
    }

    private int getIndexForUserTypeSpinner(Integer userTypeId) {
        int i;
        for(i = 0 ; i < userTypeSpinner.getCount() ; i ++) {
            Integer id =((CommonCode)userTypeSpinner.getItemAtPosition(i)).getCodeID();
            if(id.equals(userTypeId)) {
                break;
            }
        }
        return i;
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

            if (isValidInformation()) {
                RealmResults<DbUser> dbUser = Realm.getDefaultInstance()
                        .where(DbUser.class).findAll();
                if (dbUser.size() == 1) {
                    editUser();
                } else {
                    registerUser();
                }
                return true;
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_enter_required_fields,Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void editUser() {
        String token = UserDetailUtils.getToken(getApplicationContext());
        final String phoneNumber = UserDetailUtils.getMobileNumber(getApplicationContext());
        EditUser user = new EditUser(nameEditText.getText().toString(),
                                        phoneNumber,getSelectedUserTypeId(),getSelectedDistrictID());

        //Get the current language name in English
        String currentLocale = Locale.getDefault().getDisplayLanguage(Locale.ENGLISH);
        Integer languageCode = CommonCodeUtils.getLanguageCode(currentLocale);
        user.setPreferredLanguageCodeID(languageCode);

        //set udise
        user.setUdiseCode(udiseEditText.getText().toString());


        if(!selectedGradesList.isEmpty()) {
            List<Integer> gradeCodeList = getGradeCodeList();
            //format grades to be sent to server
            String grades = StringUtils.stringify(gradeCodeList);
            user.setGradeCodeIDs(grades);
        } else {
            user.setGradeCodeIDs("");
        }

        if(!selectedSubjectsList.isEmpty()) {
            List<Integer> subjectCodeList = getSubjectCodeList();
            //format subject to be sent to server
            String subjects = StringUtils.stringify(subjectCodeList);
            user.setSubjectCodeIDs(subjects);
        } else {
            user.setSubjectCodeIDs("");
        }

        if(!selectedTopicsList.isEmpty()) {
            List<Integer> topicCodeList = getTopicCodeList();
            //format subject to be sent to server
            String topics = StringUtils.stringify(topicCodeList);
            user.setTopicCodeIDs(topics);
        } else {
            user.setTopicCodeIDs("");
        }

        final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this,
                R.style.ProgressDialog);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        RestClient.getApiService(token).updateUser(user).enqueue(new Callback<BaseModel<GenericListDataModel>>() {
            @Override
            public void onResponse(Call<BaseModel<GenericListDataModel>> call, Response<BaseModel<GenericListDataModel>> response) {
                if(response.isSuccessful()){
                    Realm realm = Realm.getDefaultInstance();
                    final DbUser dbUser = realm.where(DbUser.class).findAll().first();
                    realm.beginTransaction();

                    dbUser.setName(nameEditText.getText().toString());
                    dbUser.setUdise(udiseEditText.getText().toString());
                    dbUser.setUserType(getSelectedUserTypeId());
                    dbUser.setDistrict(getSelectedDistrictID());

                    if(!StringUtils.isEmpty(profilePhotoPath)) {
                        dbUser.setProfilePhotoPath(profilePhotoPath);
                    }

                    //format grades to store into database
                    if(!selectedGradesList.isEmpty()) {
                        RealmList<DbGrade> dbGrades = new RealmList<>();
                        List<Integer> gradeCodeList = getGradeCodeList();
                        for(Integer commonCode : gradeCodeList) {
                            DbGrade dbGrade = realm.createObject(DbGrade.class);
                            dbGrade.setGradeCommonCode(commonCode);
                            dbGrades.add(dbGrade);
                        }
                        dbUser.setGrades(dbGrades);
                    } else {
                        dbUser.setGrades(null);
                    }

                    if(!selectedSubjectsList.isEmpty()) {
                        List<Integer> subjectCodeList = getSubjectCodeList();

                        //format subject to store into database
                        RealmList<DbSubject> dbSubjects = new RealmList<>();
                        for (Integer commonCode : subjectCodeList) {
                            DbSubject dbSubject = realm.createObject(DbSubject.class);
                            dbSubject.setSubjectCommonCode(commonCode);
                            dbSubjects.add(dbSubject);
                        }
                        dbUser.setSubjects(dbSubjects);
                    } else {
                        dbUser.setSubjects(null);
                    }

                    if(!selectedTopicsList.isEmpty()) {
                        List<Integer> topicCodeList = getTopicCodeList();

                        //format subject to store into database
                        RealmList<DbTopic> dbTopics = new RealmList<>();
                        for (Integer commonCode : topicCodeList) {
                            DbTopic dbTopic = realm.createObject(DbTopic.class);
                            dbTopic.setTopicCommonCode(commonCode);
                            dbTopics.add(dbTopic);
                        }
                        dbUser.setTopics(dbTopics);
                    } else {
                        dbUser.setTopics(null);
                    }

                    realm.commitTransaction();

                    if(!StringUtils.isEmpty(profilePhotoPath)) {
                        Logger.d("1. in send profile photo..");
                        sendProfilePhoto(progressDialog,true);
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.profile_updated,Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        Intent home = new Intent(EditProfileActivity.this , HomeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("show_profile",true);
                        home.putExtras(bundle);
                        startActivity(home);
                        finishAffinity();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_enter_required_fields,Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<BaseModel<GenericListDataModel>> call, Throwable t) {
                //TODO show error
                progressDialog.dismiss();
            }
        });

    }

    private List<Integer> getSubjectCodeList() {
        List<Integer> subjectCodeList = new ArrayList<>();
        for (BaseObject subjectObject : selectedSubjectsList) {
            subjectCodeList.add(subjectObject.getCommonCode().getCodeID());
        }
        return subjectCodeList;
    }

    private List<Integer> getGradeCodeList() {
        List<Integer> gradeCodeList = new ArrayList<>();
        for(BaseObject gradeObject : selectedGradesList) {
            gradeCodeList.add(gradeObject.getCommonCode().getCodeID());
        }
        return gradeCodeList;
    }

    private List<Integer> getTopicCodeList() {
        List<Integer> topicCodeList = new ArrayList<>();
        for (BaseObject topicObject : selectedTopicsList) {
            topicCodeList.add(topicObject.getCommonCode().getCodeID());
        }
        return topicCodeList;
    }

    @Override
    public void onDialogFragmentDismissed(List<BaseObject> checkedItemsList, int fragmentType) {
        if(EditProfileDialogFragment.ADD_GRADE == fragmentType) {
            selectedGradesList.clear();
            for(BaseObject checkedItem : checkedItemsList) {
                selectedGradesList.add(checkedItem);
            }
            if(!selectedGradesList.isEmpty()) {
                gradePlaceholderTextView.setVisibility(View.GONE);
            } else {
                gradePlaceholderTextView.setVisibility(View.VISIBLE);
            }
            gradeRecyclerView.getAdapter().notifyDataSetChanged();
        } else if (EditProfileDialogFragment.ADD_SUBJECT == fragmentType) {
            selectedSubjectsList.clear();
            for(BaseObject checkedItem : checkedItemsList) {
                selectedSubjectsList.add(checkedItem);
            }
            Logger.d("selected subjects : " + selectedSubjectsList.size());
            if(!selectedSubjectsList.isEmpty()) {
                subjectPlaceholderTextView.setVisibility(View.GONE);
            } else {
                subjectPlaceholderTextView.setVisibility(View.VISIBLE);
            }
            subjectRecyclerView.getAdapter().notifyDataSetChanged();
        } else if(EditProfileDialogFragment.ADD_TOPIC == fragmentType) {
            selectedTopicsList.clear();
            for(BaseObject checkedItem : checkedItemsList) {
                selectedTopicsList.add(checkedItem);
            }
            if(!selectedTopicsList.isEmpty()) {
                topicPlaceholderTextView.setVisibility(View.GONE);
            } else {
                topicPlaceholderTextView.setVisibility(View.VISIBLE);
            }
            topicRecyclerView.getAdapter().notifyDataSetChanged();
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
        if (requestCode == PICK_PROFILE_PHOTO) {
            Logger.d("data is : " + data);
            if(data != null) {
                //case for photo from gallery
                imageCaptureUri = data.getData();
                if(imageCaptureUri != null) {
                    profilePhotoPath = imageCaptureUri.toString();
                    Glide.with(this).load(imageCaptureUri.toString()).
                            bitmapTransform(new CropCircleTransformation(getApplicationContext())).
                            into(profilePhotoImageView);
                }
            } else if(imageCaptureUri != null && imageCaptureUri.getPath() != null) {
                //case for photo from camera
                File file = new File(imageCaptureUri.getPath());
                profilePhotoPath = imageCaptureUri.getPath();
                    Glide.with(this).load(Uri.fromFile(file)).
                            bitmapTransform(new CropCircleTransformation(getApplicationContext())).
                            into(profilePhotoImageView);
            }
        }
    }

    private boolean isValidInformation() {

        if(nameEditText.getText().length() == 0) {
            return false;
        } else if(getSelectedDistrictID() == 0) {
            return false;
        } else if(getSelectedUserTypeId()== 0) {
            return false;
        }
        return true;
    }

    private Integer getSelectedUserTypeId() {
        CommonCode userType = (CommonCode) userTypeSpinner.getSelectedItem();
        return userType.getCodeID();
    }

    private Integer getSelectedDistrictID() {
        CommonCode district = (CommonCode) districtSpinner.getSelectedItem();
        return district.getCodeID();
    }

    private ArrayList<Integer> getSelectedGradeCodeIds() {
        ArrayList<Integer> selectedIds = new ArrayList<>();
        for(BaseObject grade : selectedGradesList) {
            selectedIds.add(grade.getCommonCode().getCodeID());
        }
        return selectedIds;
    }

    private ArrayList<Integer> getSelectedSubjectCodeIds() {
        ArrayList<Integer> selectedIds = new ArrayList<>();
        for(BaseObject subject : selectedSubjectsList) {
            selectedIds.add(subject.getCommonCode().getCodeID());
        }
        return selectedIds;
    }

    private ArrayList<Integer> getSelectedTopicCodeIds() {
        ArrayList<Integer> selectedIds = new ArrayList<>();
        for(BaseObject subject : selectedTopicsList) {
            selectedIds.add(subject.getCommonCode().getCodeID());
        }
        return selectedIds;
    }

    private void registerUser() {

        String phoneNumber = UserDetailUtils.getMobileNumber(getApplicationContext());
        Logger.d(" language " + Locale.getDefault().getLanguage() + " " + Locale.getDefault().getDisplayLanguage());

        //Get the current language name in English
        String currentLocale = Locale.getDefault().getDisplayLanguage(Locale.ENGLISH);
        Integer languageCode = CommonCodeUtils.getLanguageCode(currentLocale);

        RegisterUser user = new RegisterUser(nameEditText.getText().toString() ,otp, phoneNumber, getSelectedDistrictID(),
                getSelectedUserTypeId(), languageCode);
        final DbUser dbUser = new DbUser(nameEditText.getText().toString(),getSelectedUserTypeId(),getSelectedDistrictID());
        dbUser.setPreferredLanguage(languageCode);

        if(!StringUtils.isEmpty(udiseEditText.getText().toString())) {
            user.setUdiseCode(udiseEditText.getText().toString());
            dbUser.setUdise(udiseEditText.getText().toString());
        }

        if(!StringUtils.isEmpty(profilePhotoPath)) {
            dbUser.setProfilePhotoPath(profilePhotoPath);
        }

        if(!selectedGradesList.isEmpty()) {
            List<Integer> gradeCodeList = getGradeCodeList();

            //format grades to store into database
            RealmList<DbGrade> dbGrades = new RealmList<>();
            for(Integer commonCode : gradeCodeList) {
                dbGrades.add(new DbGrade(commonCode));
            }
            dbUser.setGrades(dbGrades);

            //format grades to be sent to server
            String grades = StringUtils.stringify(gradeCodeList);
            user.setGradeCodeIds(grades);
        }

        if(!selectedSubjectsList.isEmpty()) {
            List<Integer> subjectCodeList = getSubjectCodeList();

            //format subject to store into database
            RealmList<DbSubject> dbSubjects = new RealmList<>();
            for(Integer commonCode : subjectCodeList) {
                dbSubjects.add(new DbSubject(commonCode));
            }
            dbUser.setSubjects(dbSubjects);

            //format subject to be sent to server
            String subjects = StringUtils.stringify(subjectCodeList);
            user.setSubjectCodeIds(subjects);
        }

        if(!selectedTopicsList.isEmpty()) {
            List<Integer> topicCodeList = getTopicCodeList();

            //format subject to store into database
            RealmList<DbTopic> dbTopics = new RealmList<>();
            for(Integer commonCode : topicCodeList) {
                dbTopics.add(new DbTopic(commonCode));
            }
            dbUser.setTopics(dbTopics);

            //format subject to be sent to server
            String topics = StringUtils.stringify(topicCodeList);
            user.setTopicCodeIds(topics);
        }

        final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this,
                R.style.ProgressDialog);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();

        RestClient.getApiService("").registerUser(user).enqueue(new Callback<BaseModel<RegisterUserResponse>>() {
            @Override
            public void onResponse(Call<BaseModel<RegisterUserResponse>> call, Response<BaseModel<RegisterUserResponse>> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    if(response.body().getData() != null) {

                        //set flag that user has entered his information
                        UserDetailUtils.setEnteredInformation(getApplicationContext(),Boolean.TRUE);

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

                        if(!StringUtils.isEmpty(profilePhotoPath)) {
                            Logger.d("1. in send profile photo..");
                            sendProfilePhoto(progressDialog,false);
                        } else {
                            //move to next activity
                            Intent verifyOtp = new Intent(EditProfileActivity.this,HomeActivity.class);
                            startActivity(verifyOtp);
                            finishAffinity();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel<RegisterUserResponse>> call, Throwable t) {
                progressDialog.dismiss();
                Logger.d(" on fail");
            }
        });
    }

    private void sendProfilePhoto(final ProgressDialog progressDialog, final boolean isEdit) {
        InputStream in;
        final byte[] buf;
        byte[] base64ByteArray;
        try {
            String path = getPath(Uri.parse(profilePhotoPath));
            if(path == null) {
                in = new FileInputStream(new File(profilePhotoPath));
            } else {
                in = new FileInputStream(path);
            }
            Bitmap imageBitmap = BitmapFactory.decodeStream(in);
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, 64, 64, false);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            buf = baos.toByteArray();
            base64ByteArray = Base64.encode(buf,Base64.DEFAULT);
            EditPhoto photo = new EditPhoto();
            try {
                photo.setByteArray(new String(base64ByteArray,"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            String token = UserDetailUtils.getToken(getApplicationContext());
            RestClient.getApiService(token).savePhoto(photo).enqueue(new Callback<BaseModel<GenericListDataModel>>() {
                @Override
                public void onResponse(Call<BaseModel<GenericListDataModel>> call, Response<BaseModel<GenericListDataModel>> response) {
                    Logger.d("response : " + response.message());
                    Intent home = new Intent(EditProfileActivity.this , HomeActivity.class);
                    if(isEdit) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("show_profile",true);
                        home.putExtras(bundle);
                    }
                    progressDialog.dismiss();
                    startActivity(home);
                    finishAffinity();
                }

                @Override
                public void onFailure(Call<BaseModel<GenericListDataModel>> call, Throwable t) {
                    Logger.d("failure : " + t.getLocalizedMessage());
                    progressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private void showPhotoPicker() {
        File file = new File(Environment.getExternalStorageDirectory(),
                "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageCaptureUri = Uri.fromFile(file);

        try {

            Intent pickIntent = new Intent();
            pickIntent.setType("image/*");
            pickIntent.setAction(Intent.ACTION_PICK);
            Intent chooserIntent = Intent.createChooser(pickIntent, getString(R.string.choose_profile_photo));
            startActivityForResult(chooserIntent, PICK_PROFILE_PHOTO);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
