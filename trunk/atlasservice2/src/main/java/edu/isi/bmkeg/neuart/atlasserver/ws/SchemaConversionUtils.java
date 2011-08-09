package edu.isi.bmkeg.neuart.atlasserver.ws;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.isi.bmkeg.neuart.atlasserver.ws.schema.AtlasDescription;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.AtlasPlate;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.AtlasStructure;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.DataMap;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.DataMapDescription;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.DataMapPlate;
import edu.isi.bmkeg.neuart.atlasserver.util.AtlasServerUtils;

public abstract class SchemaConversionUtils {

    private SchemaConversionUtils() {
    }

    public static AtlasDescription toSchemaType(edu.isi.bmkeg.neuart.atlasserver.controller.AtlasDescription domainAtlasDescription) {
    	
    	AtlasDescription schemaAtlasDescription = new AtlasDescription();
    	
    	schemaAtlasDescription.setAtlasName(domainAtlasDescription.atlasName);
    	schemaAtlasDescription.setAtlasDescription(domainAtlasDescription.atlasDescription);
    	schemaAtlasDescription.setAtlasURI(domainAtlasDescription.atlasURI);
    	schemaAtlasDescription.setAtlasYear(domainAtlasDescription.atlasYear);
    	
    	return schemaAtlasDescription;
    }

    public static AtlasPlate toSchemaType(edu.isi.bmkeg.neuart.atlasserver.controller.AtlasPlate domainAtlasPlate, URI webContextURI) {
    	
    	AtlasPlate schemaAtlasPlate = new AtlasPlate();
    	
    	schemaAtlasPlate.setPlateName(domainAtlasPlate.plateName);
    	schemaAtlasPlate.setCoronalImageURI(contextualizePath(webContextURI, domainAtlasPlate.coronalImageURI).toString());
    	schemaAtlasPlate.setCoronalThumbnailURI(contextualizePath(webContextURI, domainAtlasPlate.coronalThumbnailURI).toString());
    	schemaAtlasPlate.setSagitalZOffsetFromLeft(domainAtlasPlate.sagitalZOffsetFromLeft);
    	
    	return schemaAtlasPlate;
    }

    public static AtlasStructure toSchemaType(edu.isi.bmkeg.neuart.atlasserver.controller.AtlasStructure domainAtlasStructure, URI webContextURI) {
    	
    	AtlasStructure schemaAtlasStructure = new AtlasStructure();
    	
    	schemaAtlasStructure.setAtlasName(domainAtlasStructure.atlasName);
    	schemaAtlasStructure.setAtlasDescription(domainAtlasStructure.atlasDescription);
    	schemaAtlasStructure.setAtlasURI(domainAtlasStructure.atlasURI);
    	schemaAtlasStructure.setAtlasYear(domainAtlasStructure.atlasYear);
    	schemaAtlasStructure.setDistanceFromLeftToBregma(domainAtlasStructure.distanceFromLeftToBregma);
    	schemaAtlasStructure.setSagitalImageURI(contextualizePath(webContextURI, domainAtlasStructure.sagitalImageURI).toString());
    	schemaAtlasStructure.setSagitalZLength(domainAtlasStructure.sagitalZLength);
    	
    	schemaAtlasStructure.getAtlasPlates().addAll(toSchemaTypeAtlasPlates(domainAtlasStructure.getAtlasPlates(), webContextURI));
    	
    	return schemaAtlasStructure;
    }

    public static List<AtlasDescription> toSchemaTypeAtlasDescriptions(List<edu.isi.bmkeg.neuart.atlasserver.controller.AtlasDescription> domainAtlasDescriptions) {
        List<AtlasDescription> schemaAtlasDescriptions = new ArrayList<AtlasDescription>(domainAtlasDescriptions.size());
        for (edu.isi.bmkeg.neuart.atlasserver.controller.AtlasDescription domainAtlasDescription : domainAtlasDescriptions) {
        	schemaAtlasDescriptions.add(toSchemaType(domainAtlasDescription));
        }
        return schemaAtlasDescriptions;
    }

    public static List<AtlasPlate> toSchemaTypeAtlasPlates(List<edu.isi.bmkeg.neuart.atlasserver.controller.AtlasPlate> domainAtlasPlates, URI webContextURI) {
        List<AtlasPlate> schemaAtlasPlates = new ArrayList<AtlasPlate>(domainAtlasPlates.size());
        for (edu.isi.bmkeg.neuart.atlasserver.controller.AtlasPlate domainAtlasPlate : domainAtlasPlates) {
        	schemaAtlasPlates.add(toSchemaType(domainAtlasPlate, webContextURI));
        }
        return schemaAtlasPlates;
    }

	public static List<DataMapDescription> toSchemaTypeDataMapDescriptions(
			List<edu.isi.bmkeg.neuart.atlasserver.controller.DataMapDescription> domainDataMapDescriptions) {

		List<DataMapDescription> schemaDataMapDescriptions = new ArrayList<DataMapDescription>(domainDataMapDescriptions.size());
        for (edu.isi.bmkeg.neuart.atlasserver.controller.DataMapDescription domainDataMapDescription : domainDataMapDescriptions) {
        	schemaDataMapDescriptions.add(toSchemaType(domainDataMapDescription));
        }
        return schemaDataMapDescriptions;
	}

	public static DataMapDescription toSchemaType(
			edu.isi.bmkeg.neuart.atlasserver.controller.DataMapDescription domainDataMapDescription) {
	   	
    	DataMapDescription schemaDataMapDescription = new DataMapDescription();
    	
    	schemaDataMapDescription.setName(domainDataMapDescription.name);
    	schemaDataMapDescription.setDescription(domainDataMapDescription.description);
    	schemaDataMapDescription.setUri(domainDataMapDescription.uri);
    	schemaDataMapDescription.setCitation(domainDataMapDescription.citation);
    	schemaDataMapDescription.setDigitalLibraryKey(domainDataMapDescription.digitalLibraryKey);
    	schemaDataMapDescription.setAtlasURI(domainDataMapDescription.atlasUri);
    	
    	return schemaDataMapDescription;
	}

    public static DataMap toSchemaType(edu.isi.bmkeg.neuart.atlasserver.controller.DataMap domainDataMap, URI webContextURI) {
    	
    	DataMap schemaDataMap = new DataMap();
    	
    	schemaDataMap.setName(domainDataMap.name);
    	schemaDataMap.setDescription(domainDataMap.description);
    	schemaDataMap.setUri(domainDataMap.uri);
    	schemaDataMap.setCitation(domainDataMap.citation);
    	schemaDataMap.setDigitalLibraryKey(domainDataMap.digitalLibraryKey);
    	schemaDataMap.setAtlasURI(domainDataMap.atlasURI);
    	    	
    	schemaDataMap.getDataMapPlates().addAll(toSchemaTypeDataMapPlates(domainDataMap.getDataMapPlates(), webContextURI));
    	
    	return schemaDataMap;
    }

    public static DataMapPlate toSchemaType(edu.isi.bmkeg.neuart.atlasserver.controller.DataMapPlate domainDataMapPlate, URI webContextURI) {
    	
    	DataMapPlate schemaDataMapPlate = new DataMapPlate();
    	
    	schemaDataMapPlate.setAtlasPlateName(domainDataMapPlate.atlasPlateName);
    	schemaDataMapPlate.setCoronalLayerImageURI(contextualizePath(webContextURI, domainDataMapPlate.coronalLayerImageURI).toString());

    	return schemaDataMapPlate;
    }

    public static List<DataMapPlate> toSchemaTypeDataMapPlates(List<edu.isi.bmkeg.neuart.atlasserver.controller.DataMapPlate> domainDataMapPlates, URI webContextURI) {
        List<DataMapPlate> schemaDataMapPlates = new ArrayList<DataMapPlate>(domainDataMapPlates.size());
        for (edu.isi.bmkeg.neuart.atlasserver.controller.DataMapPlate domainDataMapPlate : domainDataMapPlates) {
        	schemaDataMapPlates.add(toSchemaType(domainDataMapPlate, webContextURI));
        }
        return schemaDataMapPlates;
    }

   public static edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMap toDomainType(DataMap schemaDataMap, 
		   edu.isi.bmkeg.neuart.atlasserver.model.AtlasStructure domainAtlas) {
    	
	   edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMap domainDataMap = 
		   new edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMap();
    	
	   domainDataMap.setName(schemaDataMap.getName());
	   domainDataMap.setDescription(schemaDataMap.getDescription());
	   domainDataMap.setUri(schemaDataMap.getUri());
	   domainDataMap.setCitation(schemaDataMap.getCitation());
	   domainDataMap.setDigitalLibraryKey(schemaDataMap.getDigitalLibraryKey());
	   domainDataMap.setAtlas(domainAtlas);
	   
	   Map<String,edu.isi.bmkeg.neuart.atlasserver.model.AtlasPlate> atlasPlates = 
		   AtlasServerUtils.createAtlasPlateMap(domainAtlas);
	   
	   for (DataMapPlate schemaDataMapPlate : schemaDataMap.getDataMapPlates()) {
		   
		   String atlasPlateName = schemaDataMapPlate.getAtlasPlateName();
		   edu.isi.bmkeg.neuart.atlasserver.model.AtlasPlate domainAtlasPlate = 
			   atlasPlates.get(atlasPlateName);
		   
		   if (domainAtlasPlate == null) 
			   throw new IllegalArgumentException("Cannot find atlas plate named: " + atlasPlateName +
					   " in atlas: " + domainAtlas.getAtlasURI() + "in Data Map:" +
					   schemaDataMap.getUri());	   
		   
		   edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapPlate domainDataMapPlate = 
			   toDomainType(schemaDataMapPlate, domainAtlasPlate);
		   
		   domainDataMap.addDataMapPlate(domainDataMapPlate);		   
	   }

	   return domainDataMap;
    }

    public static edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapPlate toDomainType(DataMapPlate schemaDataMapPlate, 
    		edu.isi.bmkeg.neuart.atlasserver.model.AtlasPlate domainAtlasPlate) {
    	
    	edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapPlate domainDataMapPlate = 
    		new edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapPlate();
    	
    	domainDataMapPlate.setAtlasPlate(domainAtlasPlate);
    	domainDataMapPlate.setCoronalLayerImageURI(schemaDataMapPlate.getCoronalLayerImageURI());

    	return domainDataMapPlate;
    }
    
	  /**
	  * Returns a URI consisting on the given webapp context URI 
	  * plus the given localPath.
	  */
	 static public URI contextualizePath(URI webContextURI, String resourceLocalPath) {
		URI resourceURI; 
	 	if (resourceLocalPath == null)
	 		throw new IllegalArgumentException("reourceLocalPath cannot be null");
	 	if (! resourceLocalPath.startsWith("/"))
	 		resourceLocalPath = '/' + resourceLocalPath;
	 	if (webContextURI != null) {
	 		String webContextPath = webContextURI.getRawPath();
		 	resourceURI = webContextURI.resolve(webContextPath + resourceLocalPath);
 		} else {
 			resourceURI = URI.create("file:" + resourceLocalPath);
 		}
	 	return resourceURI;
	 }

}
