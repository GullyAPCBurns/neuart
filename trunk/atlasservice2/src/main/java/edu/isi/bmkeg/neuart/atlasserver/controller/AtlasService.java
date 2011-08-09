package edu.isi.bmkeg.neuart.atlasserver.controller;

import java.util.List;

public interface AtlasService {

	public AtlasStructure getdAtlasStructure(String atlasUri);

	public List<AtlasDescription> listAvailableAtlases();

	public DataMap getDataMap(String uri);

	public List<DataMapDescription> listAvailableDataMaps(String atlasURI);
	
	public BrainRegion getBrainRegion(String abbrev, String atlasUri);

}
