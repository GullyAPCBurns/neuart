package edu.isi.bmkeg.neuart.atlasserver.controller;

import java.util.ArrayList;
import java.util.List;

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

	public static AtlasDescription create(edu.isi.bmkeg.neuart.atlasserver.model.AtlasDescription dataAD) {
		AtlasDescription servAD = new AtlasDescription();
		servAD.atlasName = dataAD.atlasName;
		servAD.atlasDescription = dataAD.atlasDescription;
		servAD.atlasYear = dataAD.atlasYear;
		servAD.atlasURI = dataAD.atlasURI;
		
		return servAD;
	}
	
	public static List<AtlasDescription> convertList(List<edu.isi.bmkeg.neuart.atlasserver.model.AtlasDescription> dataADs) {
		List<AtlasDescription> servADs = new ArrayList<AtlasDescription>();
		for (edu.isi.bmkeg.neuart.atlasserver.model.AtlasDescription dataAD : dataADs) {
			servADs.add(create(dataAD));
		}
		return servADs;
	}
}
