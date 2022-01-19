package com.inland.pilot.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.inland.pilot.Network.ApiClient_Image;
import com.inland.pilot.R;
import com.inland.pilot.Util.ImageResizer;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.ActivityAdharCaptureBinding;
import com.inland.pilot.databinding.ActivityPanBinding;
import com.inland.pilot.pod.PodImageCapture;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PanActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    boolean isImgUploadFailed=true;
    ProgressBar circularPregress;
    TextView progressText;
    CardView progressCard;
    private File output;
    private Bitmap photo;
    private String file_name, content_file_name;
    private ActivityPanBinding binding;
    private Context mCon;
    private Uri mImageUri;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_pan);
        circularPregress = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);
        progressCard = findViewById(R.id.progressCard);
        mCon = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permission = ActivityCompat.checkSelfPermission(PanActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                }
                else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        // We don't have permission so prompt the user
                        ActivityCompat.requestPermissions(
                                PanActivity.this,
                                PERMISSIONS_STORAGE,
                                REQUEST_EXTERNAL_STORAGE
                        );
                    }
                }

                if (PanActivity.this.getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_CAMERA)) {
                    if (ActivityCompat.checkSelfPermission(PanActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Check Permissions Now
                        // Callback onRequestPermissionsResult interceptado na Activity MainActivity
                        ActivityCompat.requestPermissions(PanActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                1);
                    } else {
                        // permission has been granted, continue as usual
                        // check if GPS enabled
                        try {
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                            values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            //Toast.makeText(mCon, "imageUri="+imageUri.toString(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mCon);
                            SharedPreferences.Editor editor =preferences.edit();
                            editor.putString("url",imageUri.toString());
                            editor.commit();
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, 100);
                        }
                        catch(Exception ex)
                        {
                            Toast.makeText(mCon, "Exception: "+ex.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    // Open default camera


                } else {
                    Toast.makeText(PanActivity.this, "Camera not supported", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.uploadTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grantUriPermission("com.inland.pilot", mImageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                output = new File(mImageUri.toString());
                Log.e("imggg",mImageUri.toString());
                try {
                   // uploadAdhar(output);
                    uploadImageOld(output);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //uploadImage(output);
            }
        });
    }


    public void uploadImageOld(File file) {
        // create multipart
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        Call<Response<String>> call = ApiClient_Image.getNetworkService().uploadPhoto(body, filename);

        final MaterialDialog dialog = new MaterialDialog.Builder(mCon)
                .content(R.string.loading)
                .canceledOnTouchOutside(false)
                .progress(true, 0)
                .widgetColorRes(R.color.colorPrimary)
                .show();

        call.enqueue(new Callback<Response<String>>() {
            @Override
            public void onResponse(Call<Response<String>> call, Response<Response<String>> response) {
                Log.e("file name",file.getName());
                Log.e("response server ",response.code()+"");
                if(response.code()==201)
                {
                    dialog.dismiss();
                    Dialog notFoundDialog = new Dialog(PanActivity.this);
                    notFoundDialog.setContentView(R.layout.no_internet_layout);
                    notFoundDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    notFoundDialog.setCancelable(false);
                    Window window = notFoundDialog.getWindow();
                    window.setGravity(Gravity.CENTER);
                    window.getAttributes().windowAnimations = R.style.DialogAnimation;
                    final WindowManager.LayoutParams params = window.getAttributes();
                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                    window.setAttributes((WindowManager.LayoutParams) params);
                    ImageView Icon = notFoundDialog.findViewById(R.id.dialog_icon);
                    TextView title = notFoundDialog.findViewById(R.id.dialog_title);
                    TextView msg = notFoundDialog.findViewById(R.id.dialog_msg);
                    LinearLayout backBtn = notFoundDialog.findViewById(R.id.dialog_button_positive);
                    Icon.setImageDrawable(PanActivity.this.getResources().getDrawable(R.drawable.done));
                    title.setText("Aadhaar UPLOAD SUCCESSFULLY!");
                    msg.setVisibility(View.GONE);
                    notFoundDialog.show();
                    backBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            notFoundDialog.dismiss();
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PanActivity.this);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("is_adhar_upload", true);
                            editor.commit();
                            dialog.dismiss();
                            finish();

                        }
                    });

                }else finish();
            }

            @Override
            public void onFailure(Call<Response<String>> call, Throwable t) {
                if (!call.isCanceled()) {
                    dialog.dismiss();
                    Toast.makeText(mCon, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    Log.d("check", "onFailure registerUser: " + t.getMessage());
                }
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bitmap bitmap;
            try {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mCon);
                String str_url=preferences.getString("url",null);
                imageUri = Uri.parse(str_url);
                //Toast.makeText(mCon,"imageUri pre:"+ imageUri.toString(), Toast.LENGTH_LONG).show();

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                //  imageView.setImageBitmap(bitmap);

                //  File imageFileToShare = new File(saveImageFile(bitmap));
                //  mImageUri = FileProvider.getUriForFile(getApplicationContext(), "com.inland.pilot.fileprovider", imageFileToShare);
                String path=saveImageFile(bitmap);
                output = new File(path);
                mImageUri = FileProvider.getUriForFile(getApplicationContext(), "com.inland.pilot.FileProvider", output);
                CropImage.activity(mImageUri).start(PanActivity.this);
                long size = output.length();
                binding.uploadTextView.setVisibility(View.VISIBLE);

            }  catch (Exception e) {
                Toast.makeText(mCon,"Exception:"+ e, Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {    try{
            Bitmap bitmap;
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                binding.imageView.setImageURI(mImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

            output = new File(mImageUri.getPath());
            long size = output.length();
            if(size>307200) {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(output));
                Bitmap finalBit = ImageResizer.reduceBitmapSize(bitmap, 307200);
                Bitmap thumb_bit = ImageResizer.generateThumb(bitmap, 76800);
                binding.imageView.setImageBitmap(thumb_bit);
                mImageUri= Uri.parse(saveImageFile(finalBit));
            }else {

                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(output));
                this.grantUriPermission("com.inland.pilot", mImageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                mImageUri = Uri.parse(saveImageFile(bitmap));
                Bitmap thumb_bit = ImageResizer.generateThumb(bitmap, 76800);
                binding.imageView.setImageBitmap(thumb_bit);
            }
            binding.uploadTextView.setVisibility(View.VISIBLE);
        }  catch (Exception e) {
            //Toast.makeText(mCon,"Exception:"+ e, Toast.LENGTH_LONG).show();
        }
        }
    }
    public String saveImageFile(Bitmap bitmap) {
        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }

    private String getFilename() {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "driver");
        String mobile_no= PreferenceUtil.getUser().getP_MOBILENO();
        file_name = mobile_no + "_PAN_image";
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(mCon, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return (file.getAbsolutePath() + "/" + file_name + ".jpg");
    }

    public void goBack(View view) {
        finish();
    }
}