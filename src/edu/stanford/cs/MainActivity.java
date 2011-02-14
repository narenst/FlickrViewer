package edu.stanford.cs;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * 
 * @author Naren
 * The activity with the search results
 * 
 */

public class MainActivity extends Activity {
//	protected Photo[] photoResults;
	protected float screenDensity;
	EditText mSearchBox;
	Button mSearchButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mSearchBox = (EditText)findViewById(R.id.searchBox);
        mSearchButton = (Button)findViewById(R.id.searchBtn);
        
        mSearchButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String query = mSearchBox.getText().toString();
				query = query.trim();
				if(query.length() > 3)
					executeQuery(query);
			}
		});
        //executeQuery("switzerland");
        //Set the density value - used later
        screenDensity = getApplicationContext().getResources().getDisplayMetrics().density;
    }
    
    void executeQuery(String query){

        Photo[] photoResults = Server.getInstance().search(query);
        
        //Get the galleries set up - Guess I will need 4 of them.
        final int galleryIds[] = {R.id.Gallery01, R.id.Gallery02, R.id.Gallery03, R.id.Gallery04};
        int photoResultPos = 0;
        for(final int gIds : galleryIds){
        	Gallery g = (Gallery) findViewById(gIds);
        	ArrayList<Photo> photoList = new ArrayList<Photo>();
        	
        	for(int i=0; i<Constants.MAX_PHOTOS/4; i++){
        		photoList.add(photoResults[photoResultPos++]);
        	}
        	
        	final ImageAdapter imgAdapter = new ImageAdapter(this, photoList, query); 
        	g.setAdapter(imgAdapter);
        	g.setSelection(1);
        	
        	g.setOnItemClickListener(new OnItemClickListener() {
        		public void onItemClick(AdapterView parent, View v, int position, long id) {
        			callPhotoActivity(imgAdapter.getPhotos().get(position));
        		}
        	});
        }
    }
    
    //From android example
    public class ImageAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;
//        private int mStartPos;
        private ArrayList<Photo> mPhotos;
        int dataThresholdPos;
        String mQuery;

        public ImageAdapter(Context c, ArrayList<Photo> photoList, String query) {
            mContext = c;
            TypedArray a = c.obtainStyledAttributes(R.styleable.Gallery1);
            mGalleryItemBackground = a.getResourceId(
                    R.styleable.Gallery1_android_galleryItemBackground, 0);
            a.recycle();
            mPhotos = photoList;
            dataThresholdPos = 0;
            mQuery = query;
//          mStartPos = startPos;
        }

        public ArrayList<Photo> getPhotos(){
        	return mPhotos;
        }
        
        public int getCount() {
        	//return mPhotos.size();
        	return Constants.GALLERY_MAX_PHOTOS;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
        	//See if over threshold, and get more data
        	if(position >= dataThresholdPos){
        		dataThresholdPos += Constants.MAX_PHOTOS;
        		getMoreData();
        	}
        	
            ImageView i = new ImageView(mContext);
            i.setImageBitmap(loadImageFromUrl(mPhotos.get(position).getThumbUrl()));

            //Support for different screen resolutions
            int imgSize = (int)(120*screenDensity);
            i.setLayoutParams(new Gallery.LayoutParams(imgSize, imgSize));
            i.setScaleType(ImageView.ScaleType.FIT_XY);
            i.setBackgroundResource(mGalleryItemBackground);
            return i;
        }

		private void getMoreData() {
			new Thread(new Runnable() {
				public void run() {
					// TODO Auto-generated method stub
					Photo[] photoResults = Server.getInstance().search("india");
					for(Photo photo : photoResults){
		        		mPhotos.add(photo);
		        	}
				}
			}).start();
		}
    }
    
    private void callPhotoActivity(Photo photo) {
    	//Photo update = Server.getInstance().getInfo(photo);
    	Intent getId = new Intent().setClass(this, PhotoActivity.class);
    	getId.putExtra("photoUrl", photo.getUrl());
    	getId.putExtra("id", photo.getId());
    	//getId.putExtra("photoTitle", update.getName());
    	//getId.putExtra("photoPageUrl", update.getFlickrUrl());
		startActivity(getId);
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