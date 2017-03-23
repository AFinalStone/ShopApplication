package com.shi.xianglixiangqin.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shi.xianglixiangqin.R;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

public class CheckStandApplyMDActivity extends MyBaseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    /**身份证正面**/
    @BindView(R.id.imageView_userCardTop)
    ImageView imageViewUserCardTop;
    /**身份证反面**/
    @BindView(R.id.imageView_userCardBottom)
    ImageView imageViewUserCardBottom;
    /**营业执照**/
    @BindView(R.id.imageView_businessLicense)
    ImageView imageView_businessLicense;

    private final int REQUESTCODE_USERCARDTOP = 1;
    private final int REQUESTCODE_USERCARDBOTTOM = 2;
    private final int REQUESTCODE_BUSINESSLICENSE = 3;

    @Override
    public void initView() {
        setContentView(R.layout.activity_check_stand_apply_md);
        ButterKnife.bind(this);
        ivTitleLeft.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.iv_titleLeft, R.id.imageView_userCardTop, R.id.imageView_userCardBottom, R.id.linearLayout_businessLicense,R.id.btn_queryApply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.imageView_userCardTop:
                pickPhoto(REQUESTCODE_USERCARDTOP);
                break;
            case R.id.imageView_userCardBottom:
                pickPhoto(REQUESTCODE_USERCARDBOTTOM);
                break;
            case R.id.linearLayout_businessLicense:
                pickPhoto(REQUESTCODE_BUSINESSLICENSE);
                break;
            case R.id.btn_queryApply:
                startActivity(new Intent(mContext,CheckStandWithdrawalActivity.class));
                break;
        }
    }

    /**
     * 从相册中取图片
     */
    void pickPhoto(int requestCode) {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(true)
                .start(this, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE_USERCARDTOP) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                long size = new File(photos.get(0)).length() / 1024;
                Log.e("图片大小", size + "");
                File file = new File(photos.get(0));

                Uri uri = Uri.parse(photos.get(0));
                imageViewUserCardTop.setImageURI(uri);

            }
            return;
        }

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE_USERCARDBOTTOM) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                long size = new File(photos.get(0)).length() / 1024;
                Log.e("图片大小", size + "");
                File file = new File(photos.get(0));

                Uri uri = Uri.parse(photos.get(0));
                imageViewUserCardBottom.setImageURI(uri);
            }
            return;
        }

        if (resultCode == RESULT_OK && requestCode == REQUESTCODE_BUSINESSLICENSE) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                long size = new File(photos.get(0)).length() / 1024;
                Log.e("图片大小", size + "");
                File file = new File(photos.get(0));

                Uri uri = Uri.parse(photos.get(0));
                imageView_businessLicense.setImageURI(uri);
            }
            return;
        }
    }

}
