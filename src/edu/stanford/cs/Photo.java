package edu.stanford.cs;

public class Photo {
	String url;
	String thumbUrl;
	String name;
	String flickrUrl;
	
	public Photo(String url, String thumbUrl, String name, String flickrUrl) {
		super();
		this.url = url;
		this.thumbUrl = thumbUrl;
		this.name = name;
		this.flickrUrl = flickrUrl;
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
}
