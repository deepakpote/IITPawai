package net.mavericklabs.mitra.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import net.mavericklabs.mitra.listener.OnDialogFragmentDismissedListener;
import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.ui.adapter.ProfileActivityGradesAdapter;
import net.mavericklabs.mitra.ui.adapter.ProfileActivitySubjectsAdapter;
import net.mavericklabs.mitra.ui.custom.CropCircleTransformation;
import net.mavericklabs.mitra.ui.fragment.SubjectAndGradeFragment;
import net.mavericklabs.mitra.utils.Logger;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity implements OnDialogFragmentDismissedListener {

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

    private Uri imageCaptureUri;
    private final int PICK_PROFILE = 0;
    private Context context;

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

        //temp
        String[] choices = {"SELECT","Teacher","Student"};
        String[] districts = {"SELECT","Beed","Jalna"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,choices);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iAmSpinner.setAdapter(adapter);

        ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item,districts);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(districtAdapter);

        Glide.with(this).load(R.drawable.placeholder_user).bitmapTransform(new CropCircleTransformation(getApplicationContext())).into(profilePhotoImageView);

        Logger.d("glide operation complete.");
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
            Intent verifyOtp = new Intent(ProfileActivity.this,HomeActivity.class);
            startActivity(verifyOtp);
            finishAffinity();
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
        if (requestCode == PICK_PROFILE) {
            if(data != null) {
                imageCaptureUri = data.getData();
                if(imageCaptureUri != null) {
                    Glide.with(this).load(imageCaptureUri).asBitmap()
                            .into(new BitmapImageViewTarget(profilePhotoImageView) {
                                      @Override
                                      protected void setResource(Bitmap resource) {
                                          Logger.d("resource height : " + resource.getHeight());
                                          Logger.d("resource width : " + resource.getWidth());
                                          RoundedBitmapDrawable circularBitmapDrawable =
                                                  RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                          circularBitmapDrawable.setCircular(true);
                                          profilePhotoImageView.setImageDrawable(circularBitmapDrawable);
                                      }
                                  }
                            );
                }
            } else {
                if(imageCaptureUri != null && imageCaptureUri.getPath() != null) {
                    Glide.with(this).load(imageCaptureUri.getPath()).asBitmap()
                            .into(new BitmapImageViewTarget(profilePhotoImageView) {
                                      @Override
                                      protected void setResource(Bitmap resource) {
                                          Logger.d("resource height : " + resource.getHeight());
                                          Logger.d("resource width : " + resource.getWidth());
                                          RoundedBitmapDrawable circularBitmapDrawable =
                                                  RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                          circularBitmapDrawable.setCircular(true);
                                          profilePhotoImageView.setImageDrawable(circularBitmapDrawable);
                                      }
                                  }
                            );
                }
            }
        }
    }
}
