package net.mavericklabs.mitra.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import net.mavericklabs.mitra.listener.OnDialogFragmentDismissedListener;
import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.ui.adapter.ProfileActivityGradesAdapter;
import net.mavericklabs.mitra.ui.adapter.ProfileActivitySubjectsAdapter;
import net.mavericklabs.mitra.ui.adapter.SpinnerArrayAdapter;
import net.mavericklabs.mitra.ui.custom.CropCircleTransformation;
import net.mavericklabs.mitra.ui.fragment.SubjectAndGradeFragment;
import net.mavericklabs.mitra.utils.Constants;
import net.mavericklabs.mitra.utils.Logger;
import net.mavericklabs.mitra.utils.MitraSharedPreferences;
import net.mavericklabs.mitra.utils.UserDetailUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
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
    @BindView(R.id.i_am_spinner) Spinner iAmSpinner;
    @BindView(R.id.district_spinner) Spinner districtSpinner;
    @BindView(R.id.profile_photo_image_view) ImageView profilePhotoImageView;
    @BindView(R.id.name_edit_text) EditText nameEditText;

    private Uri imageCaptureUri;
    private final int PICK_PROFILE = 0;
    private Context context;
    private String otp;
    private SpinnerArrayAdapter districtAdapter;

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
        FragmentManager fragmentManager = getSupportFragmentManager();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        otp = MitraSharedPreferences.readFromPreferences(getApplicationContext(), "OTP", "");
        Logger.d(" otp "  + otp);

        this.context = getApplicationContext();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        subjectRecyclerView.setVisibility(View.GONE);
        subjectRecyclerView.setHasFixedSize(true);
        subjectRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        subjectRecyclerView.setAdapter(new ProfileActivitySubjectsAdapter());

        gradeRecyclerView.setVisibility(View.GONE);
        gradeRecyclerView.setHasFixedSize(true);
        gradeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        gradeRecyclerView.setAdapter(new ProfileActivityGradesAdapter());

        //TODO temp options. these are to be fetched from server
        String[] choices = {"SELECT","Teacher","Student"};

        RealmResults<CommonCode> districtList = Realm.getDefaultInstance().where(CommonCode.class)
                .equalTo("codeGroupID", Constants.districtCodeGroup).findAll();
        Logger.d(" list " + districtList.size());
        List<CommonCode> districts = new ArrayList<>(districtList);

        //Header - not a valid value
        districts.add(0, new CommonCode("0", "0","SELECT", "SELECT", 0));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.custom_spinner_item_header,choices);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        iAmSpinner.setAdapter(adapter);

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
                RegisterUser user = new RegisterUser(nameEditText.getText().toString() ,otp, phoneNumber, getSelectedDistrictID());
                RestClient.getApiService("").registerUser(user).enqueue(new Callback<BaseModel<RegisterUserResponse>>() {
                    @Override
                    public void onResponse(Call<BaseModel<RegisterUserResponse>> call, Response<BaseModel<RegisterUserResponse>> response) {
                        Logger.d(" Succes");
                        if(response.isSuccessful()) {
                            if(response.body().getData() != null) {
                                RegisterUserResponse serverResponse = response.body().getData().get(0);
                                String token = serverResponse.getToken();
                                UserDetailUtils.saveToken(token,getApplicationContext());
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

    @Override
    public void onDialogFragmentDismissed() {
        Logger.d("dialog fragment dismissed..");
        gradeRecyclerView.getAdapter().notifyDataSetChanged();
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
//        } else if (iAmSpinner.getSelectedItem().toString().contains("SELECT")) {
//            return false;
        } else if(getSelectedDistrictID().equals("0")) {
            return false;
        }
        return true;
    }

    private String getSelectedDistrictID() {
        CommonCode district = (CommonCode) districtSpinner.getSelectedItem();
        Logger.d(" name  " + district.getCodeNameEnglish() + " " + district.getCodeID());
        return district.getCodeID();
    }
}
