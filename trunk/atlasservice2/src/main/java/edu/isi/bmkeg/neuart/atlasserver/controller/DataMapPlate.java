package edu.isi.bmkeg.neuart.atlasserver.controller;

public class DataMapPlate {
	
	public String coronalLayerImageURI;
	public String atlasPlateName;
	public DataMap parent;

	public static DataMapPlate create(
			edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapPlate dataDP) {
		
		DataMapPlate servDP = new DataMapPlate();
		
		servDP.coronalLayerImageURI = dataDP.getCoronalLayerImageURI();
		servDP.atlasPlateName = dataDP.getAtlasPlate().getPlateName();
		
		return servDP;
	}

}
