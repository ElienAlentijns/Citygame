package com.example.cameraapp2;


import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private static final int TAKE_PICTURE = 0;
    private Uri mUri;
    private Bitmap mPhoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		((Button) findViewById(R.id.snap)).setOnClickListener(this);
        ((Button) findViewById(R.id.rotate)).setOnClickListener(this);
	}
	

	@Override
	public void onClick(View v) {
	        if (v.getId()== R.id.snap) {
	                Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
	        File f = new File(Environment.getExternalStorageDirectory(),  "photo.jpg");
	        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
	        mUri = Uri.fromFile(f);
	        startActivityForResult(i, TAKE_PICTURE);
	        } else {
	                if (mPhoto!=null) {
	                        Matrix matrix = new Matrix();
	           matrix.postRotate(90);
	           mPhoto = Bitmap.createBitmap(mPhoto , 0, 0, mPhoto.getWidth(), mPhoto.getHeight(), matrix, true);
	           ((ImageView)findViewById(R.id.photo_holder)).setImageBitmap(mPhoto);
	               }
	       }
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	       switch (requestCode) {
	       case TAKE_PICTURE:
	           if (resultCode == Activity.RESULT_OK) {
	               getContentResolver().notifyChange(mUri, null);
	               ContentResolver cr = getContentResolver();
	               try {
	                   mPhoto = android.provider.MediaStore.Images.Media.getBitmap(cr, mUri);
	                ((ImageView)findViewById(R.id.photo_holder)).setImageBitmap(mPhoto);
	               } catch (Exception e) {
	                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
	                  }
	             }
	       }
	}

}
