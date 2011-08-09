package edu.isi.bmkeg.neuart.atlasserver.controller;

public class BrainRegion {
	
	public String abbreviation;
	public String description;
	public int atlasMinLevel;
	public int atlasMaxLevel;
	public String atlasURI;

	public static BrainRegion create(edu.isi.bmkeg.neuart.atlasserver.model.BrainRegion dataBR) {
		BrainRegion servBR = new BrainRegion();
		
		servBR.atlasURI = dataBR.getAtlas().getAtlasURI();
		servBR.abbreviation = dataBR.getAbbreviation();
		servBR.description = dataBR.getDescription();
		servBR.atlasMinLevel = dataBR.getAtlasMinlevel();
		servBR.atlasMaxLevel = dataBR.getAtlasMaxlevel();

		return servBR;
	}

}
