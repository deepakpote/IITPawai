package net.mavericklabs.mitra.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.database.model.DbUser;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.ui.custom.CropCircleTransformation;
import net.mavericklabs.mitra.utils.CommonCodeUtils;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
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
        }
        Glide.with(this).load(R.drawable.placeholder_user).bitmapTransform(new CropCircleTransformation(getContext())).into(profilePhotoImageView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_action_edit,menu);
    }
}
