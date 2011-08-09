package edu.isi.bmkeg.neuart.atlasserver.model;

public class AtlasDescription {
	
	public String atlasName;

	public String atlasDescription;
	
	public int atlasYear;
	
	public String atlasURI;
	
	public AtlasDescription() {
	}
	
	public AtlasDescription(String atlasName, String atlasDescription, int atlasYear, String atlasURI) {
		this.atlasName = atlasName;
		this.atlasDescription = atlasDescription;
		this.atlasYear = atlasYear;
		this.atlasURI = atlasURI;
	}
}
