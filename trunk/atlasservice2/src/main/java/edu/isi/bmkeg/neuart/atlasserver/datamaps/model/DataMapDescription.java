package edu.isi.bmkeg.neuart.atlasserver.datamaps.model;

public class DataMapDescription {
	
	public String name;

	public String description;
	
	public String citation;
	
	public String digitalLibraryKey;
	
	public String atlasUri;
	
	public String uri;

	
	public DataMapDescription() {
	}
	
	public DataMapDescription(String name, String description, String citation, String digitalLibraryKey, String atlasUri, String uri) {
		this.name = name;
		this.description = description;
		this.citation = citation;
		this.digitalLibraryKey = digitalLibraryKey;
		this.atlasUri = atlasUri;
		this.uri = uri;
	}
}
