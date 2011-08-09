package edu.isi.bmkeg.neuart.atlasserver.controller;

import java.util.ArrayList;

public class AtlasStructure {
	
	public String atlasURI;
	public String atlasName;
	public String atlasDescription;
	public int atlasYear;
	public String sagitalImageURI;
	public double sagitalZLength;
	public double distanceFromLeftToBregma;
	
	private ArrayList<AtlasPlate> atlasPlates;
	
	public static AtlasStructure create(edu.isi.bmkeg.neuart.atlasserver.model.AtlasStructure dataAS) {
		AtlasStructure servAS = new AtlasStructure();
		servAS.atlasURI = dataAS.getAtlasURI();
		servAS.atlasName = dataAS.getAtlasName();
		servAS.atlasDescription = dataAS.getAtlasDescription();
		servAS.atlasYear = dataAS.getAtlasYear();
		servAS.sagitalImageURI = dataAS.getSagitalImageURI();
		servAS.sagitalZLength = dataAS.getSagitalZLength();
		servAS.distanceFromLeftToBregma = dataAS.getDistanceFromLeftToBregma();
		
		
		for (edu.isi.bmkeg.neuart.atlasserver.model.AtlasPlate dataAP : dataAS.getAtlasPlates()) {
			servAS.addPlate(AtlasPlate.create(dataAP));
		}
		
		return servAS;
	}

	public ArrayList<AtlasPlate> getAtlasPlates() {		
		if (atlasPlates == null) 
			atlasPlates = new ArrayList<AtlasPlate>();
		return atlasPlates;
	}
	
	public void setAtlasPlates(ArrayList<AtlasPlate> plates) {
		atlasPlates = plates;
	}

	public void addPlate(AtlasPlate plate) {
		plate.parent = this;
		getAtlasPlates().add(plate);
	}

}
