package edu.stanford.cs;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Naren
 * The activity for individual image
 * 
 */

public class PhotoActivity extends Activity {
	protected Photo mPhoto;
	protected float screenDensity;
	private ImageView mImageView;
	private TextView mTitle;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo);

        mImageView = (ImageView)findViewById(R.id.imageView);
        mTitle = (TextView)findViewById(R.id.title);

        String photoUrl = getIntent().getStringExtra("photoUrl");
        String id = getIntent().getStringExtra("id");

        Bitmap bm = loadImageFromUrl(photoUrl);

        mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mImageView.setImageBitmap(bm);

        //Async task to download name, url and render title card
        new DownloadImageInfoTask().execute(id);
    }

    private class DownloadImageInfoTask extends AsyncTask<String, Integer, Long> {
    	String photoTitle;
    	String flickrUrl;
        protected Long doInBackground(String... id) {
        	Photo photo = new Photo("photoUrl", "photoUrl", "name", "flickrUrl", id[0]);
        	Photo update = Server.getInstance().getInfo(photo);
        	photoTitle = update.getName();
        	flickrUrl = update.getFlickrUrl();
			return null;
        }

        protected void onPostExecute(Long result) {
            //mImageView.setImageBitmap(result);
        	mTitle.setText(photoTitle);
        	mTitle.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(flickrUrl));
    				startActivity(browserIntent);
    			}
    		});  
        }
    }

    public Bitmap loadImageFromUrl(String imageUrl) {
    	try
    	{
    		return BitmapFactory.decodeStream((InputStream)new URL(imageUrl).getContent()); 
    	}catch (Exception e) {
    		return null;
    	}
    }
}