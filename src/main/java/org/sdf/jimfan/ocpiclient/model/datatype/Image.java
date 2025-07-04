package org.sdf.jimfan.ocpiclient.model.datatype;

import java.net.URL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Image {

	@JsonProperty("url")
	private URL url;
	
	@JsonProperty("thumbnail")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private URL thumbnail;
	
	@JsonProperty("image_category")
	private ImageCategory imageCategory;
	
	@JsonProperty("type")
	private String type;
	
	@JsonProperty("width")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private int width;
	
	@JsonProperty("height")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private int height;

	public Image(URL url, URL thumbnail, ImageCategory imageCategory, String type, int width, int height) {
		super();
		this.url = url;
		this.thumbnail = thumbnail;
		this.imageCategory = imageCategory;
		this.type = type;
		this.width = width;
		this.height = height;
	}

	public URL getUrl() {
		return url;
	}
	
	public URL getThumbnail() {
		return thumbnail;
	}
	
	public ImageCategory getImageCategory() {
		return imageCategory;
	}
	
	public String getType() {
		return type;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
