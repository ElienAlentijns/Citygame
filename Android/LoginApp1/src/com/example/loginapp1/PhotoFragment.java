package com.example.loginapp1;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class PhotoFragment extends Fragment {
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int RESULT_LOAD_IMAGE = 1;
	public static final String ARG_SECTION_NUMBER = "TEST";
	
	private Button takePicture;
	private ImageView showPicture;
	private Button choosePicture;
	private Uri fileUri;
	private Bitmap photo;
	private Activity activity = new Activity();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_photo, container, false);
		
		takePicture = (Button) view.findViewById(R.id.takePicture);
		showPicture = (ImageView) view.findViewById(R.id.showPicture);
		choosePicture = (Button) view.findViewById(R.id.choosePicture);
		takePicture.setOnClickListener(btnTakePictureListener);
		choosePicture.setOnClickListener(btnChoosePictureListener);
		return view;
	}
	
	private OnClickListener btnTakePictureListener = new OnClickListener()
    {
        public void onClick(View v) {
		// create Intent to take a picture and return control to the calling application
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
	    //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

	    // start the image capture Intent
	    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    };
    
    private OnClickListener btnChoosePictureListener = new OnClickListener()
    {
        public void onClick(View v) {
        	//Voor het opvragen van een foto uit een fotoalbum
        	Intent i = new Intent(
        	Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        	startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
    };

	private Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}
	
	/** Create a File for saving an image or video */
	private File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  
		super.onActivityResult(requestCode, resultCode, data);
		Matrix matrix = new Matrix();
		
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {  
            photo = (Bitmap) data.getExtras().get("data"); 
            //matrix.postRotate(90);
            //photo = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
            showPicture.setImageBitmap(photo);
        }
            
         if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {        	
        	Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = activity.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
    
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            
            photo = BitmapFactory.decodeFile(picturePath);
            //matrix.postRotate(90);
            //photo = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
            showPicture.setImageBitmap(photo);
        }
    }
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putParcelable("Bitmap", photo);
	}
	
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onSaveInstanceState(savedInstanceState);
	    
	    if (savedInstanceState != null) {
	    	photo = (Bitmap) savedInstanceState.getParcelable("Bitmap");
	    	showPicture.setImageBitmap(photo);
	    }
	}
	
	
}
