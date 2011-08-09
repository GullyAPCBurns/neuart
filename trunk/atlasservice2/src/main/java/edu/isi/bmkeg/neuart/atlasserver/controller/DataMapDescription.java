package edu.isi.bmkeg.neuart.atlasserver.controller;

import java.util.ArrayList;
import java.util.List;

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
	
	public static DataMapDescription create(edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapDescription dataDD) {
		DataMapDescription servDD = new DataMapDescription();
		servDD.name = dataDD.name;
		servDD.description = dataDD.description;
		servDD.citation = dataDD.citation;
		servDD.digitalLibraryKey = dataDD.digitalLibraryKey;
		servDD.atlasUri = dataDD.atlasUri;
		servDD.uri = dataDD.uri;
		
		return servDD;
	}
	
	public static List<DataMapDescription> convertList(List<edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapDescription> dataDDs) {
		List<DataMapDescription> servDDs = new ArrayList<DataMapDescription>();
		for (edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapDescription dataDD : dataDDs) {
			servDDs.add(create(dataDD));
		}
		return servDDs;
	}


}
