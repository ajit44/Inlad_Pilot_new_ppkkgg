package com.inland.pilot.unloading;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.inland.pilot.LoadImage.LoadingRequestModel;
import com.inland.pilot.Network.ApiClient_Image;
import com.inland.pilot.R;
import com.inland.pilot.Util.ImageResizer;
import com.inland.pilot.Util.PreferenceUtil;
import com.inland.pilot.databinding.ActivityRcCaptureBinding;
import com.inland.pilot.pod.PodImageCapture;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnloadingImageCapture extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    LoadingRequestModel loadingRequestModel;
    private File output;
    private Bitmap photo;
    private String file_name, content_file_name;
    private ActivityRcCaptureBinding binding;
    private Context mCon;
    private String image_no="";
    private String TripId="";
    private Uri mImageUri;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rc_capture);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rc_capture);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            image_no = bundle.getString("image_no");
            TripId = bundle.getString("TripId");
        }
        Log.e("TripId:",TripId+"");
        mCon = this;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permission = ActivityCompat.checkSelfPermission(UnloadingImageCapture.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                }
                else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        // We don't have permission so prompt the user
                        ActivityCompat.requestPermissions(
                                UnloadingImageCapture.this,
                                PERMISSIONS_STORAGE,
                                REQUEST_EXTERNAL_STORAGE
                        );
                    }
                }

                if (UnloadingImageCapture.this.getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_CAMERA))
                {
                    if (ActivityCompat.checkSelfPermission(UnloadingImageCapture.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Check Permissions Now
                        // Callback onRequestPermissionsResult interceptado na Activity MainActivity
                        ActivityCompat.requestPermissions(UnloadingImageCapture.this,
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
                    Toast.makeText(UnloadingImageCapture.this, "Camera not supported", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.uploadTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                output = new File(mImageUri.toString());
                uploadImage(output);
            }
        });
    }

    public void uploadImage(File file) {
        // create multipart
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        Call<Response<String>> call = ApiClient_Image.getNetworkService().uploadLoading(body, filename);

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
                    //Toast.makeText(mCon,"Image Uploaded Successfully",Toast.LENGTH_LONG).show();
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(UnloadingImageCapture.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("unloading_"+image_no, true);
                    editor.commit();
                    finish();
                }
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
                mImageUri = Uri.parse(output.getAbsolutePath());
                long size = output.length();
                if(size>307200) {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(output));
                    Bitmap finalBit = ImageResizer.reduceBitmapSize(bitmap, 307200);
                    Bitmap thumb_bit = ImageResizer.generateThumb(bitmap, 76800);
                    binding.imageView.setImageBitmap(thumb_bit);
                    mImageUri= Uri.parse(saveImageFile(finalBit));
                }else {

                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(output));
                    mImageUri = Uri.parse(saveImageFile(bitmap));
                    Bitmap thumb_bit = ImageResizer.generateThumb(bitmap, 76800);
                    binding.imageView.setImageBitmap(thumb_bit);
                }
                binding.uploadTextView.setVisibility(View.VISIBLE);

            }  catch (Exception e) {
                Toast.makeText(mCon,"Exception:"+ e.getMessage(), Toast.LENGTH_LONG).show();
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
        file_name=mobile_no+ "_"+TripId+"_unloading_"+image_no;
        //file_name="unloading_"+image_no;
        //loadingRequestModel = new LoadingRequestModel(mobile_no,TripId,"unloading_"+image_no+".jpg");

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
}