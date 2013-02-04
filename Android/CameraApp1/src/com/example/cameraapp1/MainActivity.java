package com.example.cameraapp1;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity {
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int RESULT_LOAD_IMAGE = 1;
	
	private Button takePicture;
	private ImageView showPicture;
	private Button choosePicture;
	private Uri fileUri;
	private Bitmap photo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		takePicture = (Button) findViewById(R.id.takePicture);
		showPicture = (ImageView) findViewById(R.id.showPicture);
		choosePicture = (Button) findViewById(R.id.choosePicture);
		takePicture.setOnClickListener(btnTakePictureListener);
		choosePicture.setOnClickListener(btnChoosePictureListener);
	}

	private OnClickListener btnTakePictureListener = new OnClickListener()
    {
        public void onClick(View v) {
		//We maken een intent aan om gebruik te kunnen maken van de camera en terug te keren naar de applicatie
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    
	    //We maken een bestand aan om de foto op te slaan
	    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); 
	    //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

	    //De intent wordt gestart
	    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    };
    
    private OnClickListener btnChoosePictureListener = new OnClickListener()
    {
        public void onClick(View v) {
        	//Voor het opvragen van een foto uit een fotoalbum 
        	Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        	startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
    };

	private Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}
	
	/** Create a File for saving an image or video */
	private File getOutputMediaFile(int type){
		
		//Voor de foto's op te slaan gaan we een nieuwe map aanmaken
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	   
	    //We maken de directory aan als deze nog niet bestaat
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    //We maken voor de foto een file name aan 
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
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		super.onActivityResult(requestCode, resultCode, data);
		//Matrix matrix = new Matrix();
		
		//Wanneer er een foto genomen wordt
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {  
            photo = (Bitmap) data.getExtras().get("data"); 
            
            //matrix.postRotate(90);
            //photo = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
            
            //De foto wordt getoond in een ImageView
            showPicture.setImageBitmap(photo);

        }
         
		 //Wanneer er een foto uit het fotoalbum gekozen wordt
         if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {        	
        	//We slaan op welke foto er gekozen is
        	Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
    
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
    
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            
            photo = BitmapFactory.decodeFile(picturePath);
            //matrix.postRotate(90);
            //photo = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
            
            //We tonen de foto in een ImageView
            showPicture.setImageBitmap(photo);
        }
    }
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		//We slaan de foto op zodat wanneer het scherm gedraaid wordt deze nog steeds getoond wordt
		savedInstanceState.putParcelable("Bitmap", photo);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    //De foto blijft staan wanneer het scherm gedraaid wordt of de app hervat wordt
	    if (savedInstanceState != null) {
	    	photo = (Bitmap) savedInstanceState.getParcelable("Bitmap");
	    	showPicture.setImageBitmap(photo);
	    }
	}
}
