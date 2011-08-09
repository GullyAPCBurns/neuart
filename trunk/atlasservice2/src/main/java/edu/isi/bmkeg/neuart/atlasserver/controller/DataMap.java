package edu.isi.bmkeg.neuart.atlasserver.controller;

import java.util.ArrayList;

public class DataMap {
	
	public String uri;
	public String name;
	public String description;
	public String citation;
	public String digitalLibraryKey;
	public String atlasURI;
	
	private ArrayList<DataMapPlate> dataMapPlates;

	public ArrayList<DataMapPlate> getDataMapPlates() {		
		if (dataMapPlates == null) 
			dataMapPlates = new ArrayList<DataMapPlate>();
		return dataMapPlates;
	}
	
	public void setDataMapPlates(ArrayList<DataMapPlate> plates) {
		dataMapPlates = plates;
	}
	
	public void addDataMapPlate(DataMapPlate plate) {
		plate.parent = this;
		getDataMapPlates().add(plate);
	}

	public static DataMap create(edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMap dataDM) {
		DataMap servDM = new DataMap();
		
		servDM.atlasURI = dataDM.getAtlas().getAtlasURI();
		servDM.name = dataDM.getName();
		servDM.description = dataDM.getDescription();
		servDM.uri = dataDM.getUri();
		servDM.citation = dataDM.getCitation();
		servDM.digitalLibraryKey = dataDM.getDigitalLibraryKey();		
		
		for (edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapPlate dataDP : dataDM.getDataMapPlates()) {
			servDM.addDataMapPlate(DataMapPlate.create(dataDP));
		}
		
		return servDM;
	}

}
