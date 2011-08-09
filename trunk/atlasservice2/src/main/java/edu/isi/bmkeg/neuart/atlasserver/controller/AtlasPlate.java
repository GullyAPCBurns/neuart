package edu.isi.bmkeg.neuart.atlasserver.controller;

public class AtlasPlate {
	
	public String plateName;
	public double sagitalZOffsetFromLeft;
	public String coronalThumbnailURI;
	public String coronalImageURI;
	public 	AtlasStructure parent;
	
	public static AtlasPlate create(
			edu.isi.bmkeg.neuart.atlasserver.model.AtlasPlate dataAP) {
		
		AtlasPlate servAP = new AtlasPlate();
		
		servAP.plateName = dataAP.getPlateName();
		servAP.sagitalZOffsetFromLeft = dataAP.getSagitalZOffsetFromLeft();
		servAP.coronalThumbnailURI = dataAP.getCoronalThumbnailURI();
		servAP.coronalImageURI = dataAP.getCoronalImageURI();
		
		return servAP;
	}
}
