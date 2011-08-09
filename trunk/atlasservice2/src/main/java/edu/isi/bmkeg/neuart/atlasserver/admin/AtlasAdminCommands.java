/**
 * CLI to perform administrative tasks with atlases in the KB.
 * 
 */
package edu.isi.bmkeg.neuart.atlasserver.admin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import javax.xml.bind.JAXBElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.isi.bmkeg.neuart.atlasserver.dao.AtlasServerDao;
import edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMap;
import edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapDescription;
import edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapPlate;
import edu.isi.bmkeg.neuart.atlasserver.model.AtlasDescription;
import edu.isi.bmkeg.neuart.atlasserver.model.AtlasPlate;
import edu.isi.bmkeg.neuart.atlasserver.model.AtlasStructure;
import edu.isi.bmkeg.neuart.atlasserver.model.BrainRegion;
import edu.isi.bmkeg.neuart.atlasserver.ws.SchemaConversionUtils;

@Transactional
@Component
public class AtlasAdminCommands {

	private static final Object TAG_ATLAS = "Atlas";
	private static final String TAG_PLATE = "Plate";

	private static final String ATTR_ATLAS_NAME = "name";
	private static final String ATTR_ATLAS_DESC = "description";
	private static final String ATTR_ATLAS_YEAR = "year";
	private static final String ATTR_ATLAS_URI = "uri";
	private static final String ATTR_SAGITAL_URI = "sagitalImageURI";
	private static final String ATTR_SAGITAL_LENGTH = "sagitalZLength";
	private static final String ATTR_DISTANCE_BREGMA = "leftToBregma";
	private static final String ATTR_PLATE_NAME = "name";
	private static final String ATTR_CORONAL_IMG_URI = "coronalImageUri";
	private static final String ATTR_CORONAL_NAIL_URI = "coronalThumbnailUri";
	private static final String ATTR_SAGITAL_OFFSET = "zOffsetFromleft";
		
	private AtlasServerDao ac;
	private Unmarshaller unmarshaller;
	
	@Autowired
	public void setAtlasServerController(AtlasServerDao ac) {
		this.ac = ac;
	}
			
	@Autowired
	public void setUnmarshaller(Jaxb2Marshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}

//	public void setUnmarshaller(Unmarshaller unmarshaller) {
//		this.unmarshaller = unmarshaller;
//	}
			
	public void deleteAtlas(String atlasUri) {
		AtlasStructure atlas = ac.findAtlas(atlasUri);
		if (atlas == null) { 
			System.out.println("No atlas found with URI: " + atlasUri);
			return;
		}
		ac.deleteAtlas(atlas);
		System.out.println("Atlas: " + atlasUri + " was deleted");
	}

	public void listAtlases(String atlasUri) {
		AtlasStructure atlas = ac.findAtlas(atlasUri);
		if (atlas == null) { 
			System.out.println("No atlas found with URI: " + atlasUri);
			return;
		}
		System.out.println("Name: " + atlas.getAtlasName());
		System.out.println("Description: " + atlas.getAtlasDescription());
		System.out.println("URI: " + atlas.getAtlasURI());
		System.out.println("Year: " + atlas.getAtlasYear());
		System.out.println("Distance to Bregma: " + atlas.getDistanceFromLeftToBregma());
		System.out.println("Sagital image URI: " + atlas.getSagitalImageURI());
		System.out.println("Sagital Z Length: " + atlas.getSagitalZLength());
		System.out.println();
		AtlasPlate[] aps = atlas.getAtlasPlates().toArray(new AtlasPlate[0]);
		Arrays.sort(aps, new Comparator<AtlasPlate>() {

			@Override
			public int compare(AtlasPlate o1, AtlasPlate o2) {
				return (int) Math.signum(o1.getSagitalZOffsetFromLeft() - o2.getSagitalZOffsetFromLeft());
			}
		});
		
		for (AtlasPlate ap : aps) {
			System.out.println("Plate: " +ap.getPlateName());
			System.out.println("ZOffset: " + ap.getSagitalZOffsetFromLeft());
			System.out.println("Coronal Image Uri: " + ap.getCoronalImageURI());
			System.out.println("Coronal Thumbnail Uri: " + ap.getCoronalThumbnailURI());
			System.out.println();
		}
	}

	public void listAtlases() {
		for (AtlasDescription ad : ac.retrieveAtlasDescriptions()) {
			System.out.println(ad.atlasName);
			System.out.println(ad.atlasDescription);
			System.out.println(ad.atlasURI);
			System.out.println();
		}
	}
	
	public void loadAtlas(File atlasXml) throws Exception {
	    AtlasStructure atlas = createAtlasStructure(atlasXml);	
	    ac.addUpdateAtlas(atlas);
	}

	public void loadDataMap(File dataMapXML) throws Exception {
	    DataMap dataMap = createDataMap(dataMapXML);	
	    ac.addUpdateDataMap(dataMap);
	}

	public void loadDataMaps(File dir) throws Exception {
		
		System.out.println("Processing files in " + dir.toString());
		for (File dataMapXML : dir.listFiles()) {
			if (!dataMapXML.toString().endsWith(".xml"))
					continue;
			System.out.println("File: " + dataMapXML.toString());
			try {
			    DataMap dataMap = createDataMap(dataMapXML);	
			    ac.addUpdateDataMap(dataMap);				
			} catch (Exception e) {
				System.out.println("Failed dataMap processing");
				e.printStackTrace();	
			}
		}
	}

	public void loadBrainRegions(File brsXml) throws Exception {
				
		DocumentBuilder builder =
	        DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
	    Document brsDoc = builder.parse(brsXml);
	    Element brsElem = brsDoc.getDocumentElement();
	    if (!"brain_regions".equals(brsElem.getTagName()))
	    	 throw new Exception("Document Element is not " + "brain_regions");
	    
	    String atlasUri = brsElem.getAttribute("atlasurl");
		
		AtlasStructure atlas = ac.findAtlas(atlasUri);
		
		if (atlas == null)
			throw new Exception("Cannot find atlas in DB: " + atlasUri);
		
	    NodeList brsNodes = brsElem.getElementsByTagName("brain_region");
	    for (int i = 0; i < brsNodes.getLength(); i++) {
	    	Element brElem = (Element) brsNodes.item(i);
	    	BrainRegion br = new BrainRegion();
	    	br.setID(null);
	    	br.setAtlas(atlas);
	    	String abbrev = brElem.getElementsByTagName("br_abbrev").item(0).getTextContent();
	    	String description = brElem.getElementsByTagName("br_description").item(0).getTextContent();
	    	String sminlevel = brElem.getElementsByTagName("atlas_minlevel").item(0).getTextContent();
	    	String smaxlevel = brElem.getElementsByTagName("atlas_maxlevel").item(0).getTextContent();
	    	System.out.println(abbrev + ", " + description + ", " + sminlevel + ", " + smaxlevel);
	    	br.setAbbreviation(abbrev);
	    	br.setDescription(description);
	    	br.setAtlasMinlevel(Integer.parseInt(sminlevel));
	    	br.setAtlasMaxlevel(Integer.parseInt(smaxlevel));
		    ac.addUpdateBrainRegion(br);
	    }
	}

	static public AtlasStructure createAtlasStructure(File atlasXml)
			throws ParserConfigurationException, SAXException, IOException,
			Exception {
		
		DocumentBuilder builder =
	        DocumentBuilderFactory.newInstance().newDocumentBuilder();
    	AtlasStructure atlas = new AtlasStructure();
	    Document atlasDoc = builder.parse(atlasXml);
	    Element atlasElem = atlasDoc.getDocumentElement();
	    if (!TAG_ATLAS.equals(atlasElem.getTagName()))
	    	 throw new Exception("Document Element is not " + TAG_ATLAS);
	    atlas.setAtlasID(null);
	    atlas.setAtlasName(atlasElem.getAttribute(ATTR_ATLAS_NAME));
	    atlas.setAtlasDescription(atlasElem.getAttribute(ATTR_ATLAS_DESC));
	    atlas.setAtlasYear(Integer.parseInt(atlasElem.getAttribute(ATTR_ATLAS_YEAR)));
	    atlas.setAtlasURI(atlasElem.getAttribute(ATTR_ATLAS_URI));
	    atlas.setSagitalImageURI(atlasElem.getAttribute(ATTR_SAGITAL_URI));
	    atlas.setSagitalZLength(Double.parseDouble(atlasElem.getAttribute(ATTR_SAGITAL_LENGTH)));
	    atlas.setDistanceFromLeftToBregma(Double.parseDouble(atlasElem.getAttribute(ATTR_DISTANCE_BREGMA)));
	    
	    NodeList plateNodes = atlasElem.getElementsByTagName(TAG_PLATE);
	    for (int i = 0; i < plateNodes.getLength(); i++) {
	    	Element plateElem = (Element) plateNodes.item(i);
	    	AtlasPlate plate = new AtlasPlate();
	    	plate.setPlateID(null);
	    	plate.setPlateName(plateElem.getAttribute(ATTR_PLATE_NAME));
	    	plate.setCoronalImageURI(plateElem.getAttribute(ATTR_CORONAL_IMG_URI));
	    	plate.setCoronalThumbnailURI(plateElem.getAttribute(ATTR_CORONAL_NAIL_URI));
	    	plate.setSagitalZOffsetFromLeft(Double.parseDouble(plateElem.getAttribute(ATTR_SAGITAL_OFFSET)));
	    	atlas.addPlate(plate);
	    }
		return atlas;
	}

	@SuppressWarnings("unchecked")
	public DataMap createDataMap(File dataMapXml)
	throws ParserConfigurationException, SAXException, IOException,	Exception {
		
		edu.isi.bmkeg.neuart.atlasserver.ws.schema.DataMap schemaDataMap = 
			((JAXBElement<edu.isi.bmkeg.neuart.atlasserver.ws.schema.DataMap>) unmarshaller.unmarshal(new StreamSource(dataMapXml))).getValue();
		
		String atlasUri = schemaDataMap.getAtlasURI();
		AtlasStructure atlas = ac.findAtlas(atlasUri);
		
		if (atlas == null)
			throw new Exception("Cannot find atlas in DB: " + atlasUri);
		
		DataMap domainDataMap = SchemaConversionUtils.toDomainType(schemaDataMap, atlas);

		return domainDataMap;
	}
	
	public void listDatamaps(String atlasURI) {
		for (DataMapDescription dm : ac.retrieveDataMapDescriptions(atlasURI)) {
			System.out.println(dm.name);
			System.out.println(dm.uri);
			System.out.println(dm.description);
			System.out.println();
		}
	}

	public void listDatamap(String datamapUri) {
		DataMap dm = ac.findDataMap(datamapUri);
		if (dm == null) { 
			System.out.println("No data map found with URI: " + datamapUri);
			return;
		}
		System.out.println("Name: " + dm.getName());
		System.out.println("Description: " + dm.getDescription());
		System.out.println("URI: " + dm.getUri());
		System.out.println("Citation: " + dm.getCitation());
		System.out.println("DigitalLibraryKey: " + dm.getDigitalLibraryKey());
		System.out.println("Atlas Uri: " + dm.getAtlas().getAtlasURI());
		System.out.println();
		
		DataMapPlate[] ps = dm.getDataMapPlates().toArray(new DataMapPlate[0]);
		Arrays.sort(ps, new Comparator<DataMapPlate>() {

			@Override
			public int compare(DataMapPlate o1, DataMapPlate o2) {
				return o1.getAtlasPlate().getPlateName().compareTo(o2.getAtlasPlate().getPlateName());
			}
		});
		
		for (DataMapPlate p : ps) {
			System.out.println("Plate: " +p.getAtlasPlate().getPlateName());
			System.out.println("Coronal Layer Image Uri: " + p.getCoronalLayerImageURI());
			System.out.println();
		}
	}

	public void deleteDataMap(String datamapUri) {
		DataMap dm = ac.findDataMap(datamapUri);
		if (dm == null) { 
			System.out.println("No data map found with URI: " + datamapUri);
			return;
		}
		ac.deleteDataMap(dm);
		System.out.println("Data Map: " + datamapUri + " was deleted");
	}



}
