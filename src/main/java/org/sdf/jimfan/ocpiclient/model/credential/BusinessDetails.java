package org.sdf.jimfan.ocpiclient.model.credential;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BusinessDetails {

	@JsonProperty("name")
	public String name;
	
	@JsonProperty("website")
	public String websiteUrl;
	
	@JsonProperty("logo")
	public String logoUrl;

	public BusinessDetails(String name, String websiteUrl, String logoUrl) {
		this.name = name;
		this.websiteUrl = websiteUrl;
		this.logoUrl = logoUrl;
	}
}
