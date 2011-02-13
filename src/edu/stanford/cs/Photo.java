package edu.stanford.cs;

public class Photo {
	String url;
	String thumbUrl;
	String name;
	String flickrUrl;
	String id;
	
	public Photo(String url, String thumbUrl, String name, String flickrUrl, String id) {
		super();
		this.url = url;
		this.thumbUrl = thumbUrl;
		this.name = name;
		this.flickrUrl = flickrUrl;
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public String getName() {
		return name;
	}

	public String getFlickrUrl() {
		return flickrUrl;
	}
	
	public String getId(){
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFlickrUrl(String flickrUrl) {
		this.flickrUrl = flickrUrl;
	}
}
