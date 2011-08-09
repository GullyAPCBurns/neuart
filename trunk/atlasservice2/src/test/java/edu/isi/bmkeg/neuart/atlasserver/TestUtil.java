package edu.isi.bmkeg.neuart.atlasserver;

import java.util.Map;

import org.junit.Assert;

import edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMap;
import edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapDescription;
import edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapPlate;
import edu.isi.bmkeg.neuart.atlasserver.model.AtlasDescription;
import edu.isi.bmkeg.neuart.atlasserver.model.AtlasPlate;
import edu.isi.bmkeg.neuart.atlasserver.model.AtlasStructure;
import edu.isi.bmkeg.neuart.atlasserver.model.BrainRegion;
import edu.isi.bmkeg.neuart.atlasserver.util.AtlasServerUtils;

public class TestUtil {

	static final public String ATLAS_XML_PATHNAME = "etc/atlases/swanson1998/Atlas.xml";
	static final public int ATLASES_SIZE = 1;

	public static final String ATLAS_DESC = "Atlas for rat brain";
	public static final String ATLAS_NAME = "brainyrat";
	public static final String ATLAS_URI = "http://bmkeg.isi.edu/brainyrat";
	public static final int ATLAS_YEAR = 2010;
	public static final String SAGITAL_URI = "atlas/brainyrat/sagital.jpg";
	public static final int ATLAS_LENGTH = 200;
	public static final int DISTANCE_TO_BREGMA = 100;
	public static final int AP_CNT = 2;
	public static final String AP1_LARGE_IMAGE_URI = "atlas/brainyrat/p1large.svg";
	public static final String AP1_NAME = "p1";
	public static final int AP1_OFFSET = 1;
	public static final String AP2_LARGE_IMAGE_URI = "atlas/brainyrat/p2large.svg";
	public static final String AP2_NAME = "p2";
	public static final int AP2_OFFSET = 2;
	public static final String AP1_THUMBNAIL_URI = "atlas/brainyrat/p1thumbnail.jpg";
	public static final String AP2_THUMBNAIL_URI = "atlas/brainyrat/p2thumbnail.jpg";

	public static final String DM_DESC = "DM1 Data Map description";
	public static final String DM_NAME = "DM1 Data Map Name";
	public static final String DM_URI = "http://bmkeg.isi.edu/datamap/dm1";
	public static final String DM_CITATION = "DM1 Citation";
	public static final String DM_DIGITAL_LIBRARY_KEY = "DM1 Digital Library Key";
	public static final String DM_P1_LAYER_URI = "datamaps/brainyrat/p1thumbnail.svg" ;
	public static final String DM_P2_LAYER_URI = "datamaps/brainyrat/p2thumbnail.svg";
	
	

	static public void assertDeepEquals(AtlasStructure a, AtlasStructure b) {

		double epsilon = 0.001;
		
		Assert.assertEquals(a.getAtlasID(), b.getAtlasID());
		Assert.assertEquals(a.getAtlasDescription(), b.getAtlasDescription());
		Assert.assertEquals(a.getAtlasName(), b.getAtlasName());
		Assert.assertEquals(a.getAtlasURI(), b.getAtlasURI());
		Assert.assertEquals(a.getAtlasYear(), b.getAtlasYear());
		Assert.assertEquals(a.getSagitalImageURI(), b.getSagitalImageURI());
		Assert.assertEquals(a.getSagitalZLength(), b.getSagitalZLength(), epsilon);
		Assert.assertEquals(a.getDistanceFromLeftToBregma(), b.getDistanceFromLeftToBregma(), epsilon);
		
		Assert.assertEquals("Number of plates", a.getAtlasPlates().size(), b.getAtlasPlates().size());
		
		Map<String, AtlasPlate> bps = AtlasServerUtils.createAtlasPlateMap(b);
		
		for (AtlasPlate ap : a.getAtlasPlates()) {
			AtlasPlate bp = bps.get(ap.getPlateName());
			Assert.assertNotNull("there should exists matching plate", bp);
			Assert.assertEquals(ap.getPlateID(), bp.getPlateID());
			Assert.assertEquals(ap.getCoronalImageURI(), bp.getCoronalImageURI());
			Assert.assertEquals(ap.getSagitalZOffsetFromLeft(), bp.getSagitalZOffsetFromLeft(), epsilon);
			Assert.assertEquals(ap.getCoronalThumbnailURI(), bp.getCoronalThumbnailURI());
			Assert.assertEquals(ap.getParent().getAtlasID(), bp.getParent().getAtlasID());
//			Assert.assertSame("Parent reference matches", b, bp.getParent());
		}
	}

	static public void assertCorresponds(AtlasStructure localAtlas,
			AtlasDescription atlasDesc) {
		Assert.assertEquals(localAtlas.getAtlasName(), atlasDesc.atlasName);
		Assert.assertEquals(localAtlas.getAtlasDescription(), atlasDesc.atlasDescription);
		Assert.assertEquals(localAtlas.getAtlasURI(), atlasDesc.atlasURI);
		Assert.assertEquals(localAtlas.getAtlasYear(), atlasDesc.atlasYear);
	}

	public static void assertDeepEquals(DataMap dm1, DataMap dm2) {
		Assert.assertEquals(dm1.getId(), dm2.getId());
		Assert.assertEquals(dm1.getDescription(), dm2.getDescription());
		Assert.assertEquals(dm1.getName(), dm2.getName());
		Assert.assertEquals(dm1.getUri(), dm2.getUri());
		Assert.assertEquals(dm1.getCitation(), dm2.getCitation());
		Assert.assertEquals(dm1.getDigitalLibraryKey(), dm2.getDigitalLibraryKey());
		Assert.assertEquals(dm1.getAtlas().getAtlasID(), dm2.getAtlas().getAtlasID());
		
		Assert.assertEquals("Number of plates", dm1.getDataMapPlates().size(), dm1.getDataMapPlates().size());
		
		Map<String, DataMapPlate> p2s = AtlasServerUtils.createDataMapPlateMap(dm2);
		
		for (DataMapPlate p1 : dm1.getDataMapPlates()) {
			DataMapPlate p2 = p2s.get(p1.getAtlasPlate().getPlateName());
			Assert.assertNotNull("there should exists matching plate", p2);
			Assert.assertEquals(p1.getId(), p2.getId());
			Assert.assertEquals(p1.getCoronalLayerImageURI(), p2.getCoronalLayerImageURI());
			Assert.assertEquals(p1.getAtlasPlate().getPlateID(), p2.getAtlasPlate().getPlateID());
			Assert.assertEquals(p1.getParent().getId(), p2.getParent().getId());
		}
	}

	public static void assertCorresponds(DataMap dm, DataMapDescription dmdesc) {
		Assert.assertEquals(dm.getName(), dmdesc.name);
		Assert.assertEquals(dm.getDescription(), dmdesc.description);
		Assert.assertEquals(dm.getUri(), dmdesc.uri);
		Assert.assertEquals(dm.getCitation(), dmdesc.citation);
		Assert.assertEquals(dm.getDigitalLibraryKey(), dmdesc.digitalLibraryKey);
		Assert.assertEquals(dm.getAtlas().getAtlasURI(), dmdesc.atlasUri);		
	}

	public static AtlasStructure createAtlasInstance() {
		AtlasStructure atlas = new AtlasStructure();
		atlas.setAtlasDescription(TestUtil.ATLAS_DESC);
		atlas.setAtlasName(TestUtil.ATLAS_NAME);
		atlas.setAtlasURI(TestUtil.ATLAS_URI);
		atlas.setAtlasYear(TestUtil.ATLAS_YEAR);
		atlas.setSagitalImageURI(TestUtil.SAGITAL_URI);
		atlas.setSagitalZLength(TestUtil.ATLAS_LENGTH);
		atlas.setDistanceFromLeftToBregma(TestUtil.DISTANCE_TO_BREGMA);
		
		AtlasPlate ap1 = new AtlasPlate();
		ap1.setCoronalImageURI(TestUtil.AP1_LARGE_IMAGE_URI);
		ap1.setPlateName(TestUtil.AP1_NAME);
		ap1.setSagitalZOffsetFromLeft(TestUtil.AP1_OFFSET);
		ap1.setCoronalThumbnailURI(TestUtil.AP1_THUMBNAIL_URI);
		atlas.addPlate(ap1);
		
		AtlasPlate ap2 = new AtlasPlate();
		ap2.setCoronalImageURI(TestUtil.AP2_LARGE_IMAGE_URI);
		ap2.setPlateName(TestUtil.AP2_NAME);
		ap2.setSagitalZOffsetFromLeft(TestUtil.AP2_OFFSET);
		ap2.setCoronalThumbnailURI(TestUtil.AP2_THUMBNAIL_URI);
		atlas.addPlate(ap2);
		return atlas;
	}

	public static DataMap createDataMapInstance(AtlasStructure atlasi) {
		DataMap dm = new DataMap();
		dm.setDescription(DM_DESC);
		dm.setName(DM_NAME);
		dm.setUri(DM_URI);
		dm.setCitation(DM_CITATION);
		dm.setDigitalLibraryKey(DM_DIGITAL_LIBRARY_KEY);
		dm.setAtlas(atlasi);
		
		Map<String,AtlasPlate> as = AtlasServerUtils.createAtlasPlateMap(atlasi);
	
		AtlasPlate ap1 = as.get(AP1_NAME);		
		DataMapPlate p1 =  new DataMapPlate();
		p1.setCoronalLayerImageURI(DM_P1_LAYER_URI);
		p1.setAtlasPlate(ap1);
		dm.addDataMapPlate(p1);
		
		AtlasPlate ap2 = as.get(AP2_NAME);		
		DataMapPlate p2 =  new DataMapPlate();
		p2.setCoronalLayerImageURI(DM_P2_LAYER_URI);
		p2.setAtlasPlate(ap2);
		dm.addDataMapPlate(p2);
		return dm;
	}

	public static BrainRegion createBrainRegionInstance(AtlasStructure a) {
		BrainRegion br = new BrainRegion();
		br.setAbbreviation("breg1");
		br.setDescription("breg1 Description");
		br.setAtlas(a);
		br.setAtlasMinlevel(5);
		br.setAtlasMaxlevel(15);
		return br;
	}

	static public void assertDeepEquals(BrainRegion a, BrainRegion b) {
		Assert.assertEquals(a.getAtlas().getAtlasID(), b.getAtlas().getAtlasID());
		Assert.assertEquals(a.getDescription(), b.getDescription());
		Assert.assertEquals(a.getAbbreviation(), b.getAbbreviation());
		Assert.assertEquals(a.getAtlasMinlevel(), b.getAtlasMinlevel());
		Assert.assertEquals(a.getAtlasMaxlevel(), b.getAtlasMaxlevel());
	}

}
