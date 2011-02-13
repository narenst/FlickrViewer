package edu.stanford.cs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public final class Server {
	public static Photo[] search(String query){
		String url = Constants.flickrAPI + "?method=flickr.photos.search&api_key=" 
			+ Constants.API_KEY + "&format=json&text=" + query + "&per_page=" + Constants.MAX_PHOTOS;
		Log.d("Server", "Flickr URL : " + url);
		JSONObject jsonObj = RestClient.connect(url);
		Photo[] results = new Photo[Constants.MAX_PHOTOS];
		try {
//			Log.d("Server", "JSON : " + jsonObj.toString());
			JSONArray photos = (JSONArray) jsonObj.getJSONObject("photos").getJSONArray("photo");
			Log.d("Server : ", "Number of photos " + photos.length());
			for (int i=0; i<photos.length() && i<Constants.MAX_PHOTOS; i++){
				JSONObject photo = photos.getJSONObject(i);
				String fullUrl = "http://farm" + photo.getString("farm") + 
					".static.flickr.com/" + photo.getString("server") + "/" +  
					photo.getString("id") + "_" + photo.getString("secret");
				
				//TODO: Got to change this
				results[i] = new Photo(fullUrl + ".jpg", fullUrl + "_s.jpg", 
						"name", "www.flickr.com", photo.getString("id")); 
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return results;
	}
	
	public static Photo getInfo(Photo photo){
		String url = Constants.flickrAPI + "?method=flickr.photos.getInfo&api_key=" 
			+ Constants.API_KEY + "&format=json&photo_id=" + photo.getId();
		Log.d("Server", "Flickr getInfo URL : " + url);
		
		//String photoName, flickrUrl;
		try {
			JSONObject jsonObj = RestClient.connect(url);
			JSONObject photoObj = jsonObj.getJSONObject("photo");
			String title = photoObj.getJSONObject("title").getString("_content");
			String url1 = photoObj.getJSONObject("urls").getJSONArray("url").getJSONObject(0).getString("_content");
			photo.setName(title);
			photo.setFlickrUrl(url1);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return photo;
	}
}