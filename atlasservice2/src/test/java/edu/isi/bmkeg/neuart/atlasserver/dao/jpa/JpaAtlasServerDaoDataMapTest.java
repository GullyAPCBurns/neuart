package edu.isi.bmkeg.neuart.atlasserver.dao.jpa;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

//import javax.annotation.Resource;

import org.junit.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import edu.isi.bmkeg.neuart.atlasserver.TestUtil;
import edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMap;
import edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapDescription;
import edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapPlate;
import edu.isi.bmkeg.neuart.atlasserver.model.AtlasStructure;
import edu.isi.bmkeg.neuart.atlasserver.util.AtlasServerUtils;

// TODO implement TestFramework TransactionManager that automatically rolls back transactions after each test
@TransactionConfiguration(defaultRollback=true)
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="JpaAtlasServerDaoTest-context.xml")
public class JpaAtlasServerDaoDataMapTest {

	private JpaAtlasServerDao atlasDao;
	
	
	@Resource
	public void setSrv(JpaAtlasServerDao controller) {
		this.atlasDao = controller;
	}

	/**
	 * saves an atlas, retrieves saved atlas, updates the atlas, and deletes it.
	 */
	@Test
	public void testCreateFindDelete() { 

		Assert.assertNotNull("controller shouldn't be null", atlasDao);
		
		AtlasStructure atlas = TestUtil.createAtlasInstance();

		// Saves Atlas
		
		atlas = atlasDao.addUpdateAtlas(atlas);
		Integer atlasID = atlas.getAtlasID();
		atlasDao.em.flush();
		
		Assert.assertNotNull("AtlasID shouldn't be null", atlasID);
		System.out.println("AtlasID: " + atlasID);
		Assert.assertTrue("atlasID != 0", atlasID.intValue() != 0);
		
		// Creates Data Map
		
		DataMap dm = TestUtil.createDataMapInstance(atlas);
		
		// Saves DataMap

		dm = atlasDao.addUpdateDataMap(dm);		
		Integer dmID = dm.getId();
		atlasDao.em.flush();
		
		Assert.assertNotNull("dmID shouldn't be null", dmID);
		System.out.println("dmID: " + dmID);
		Assert.assertTrue("dmID != 0", dmID.intValue() != 0);
		
		// Retrieves DataMap
		
		DataMap dm2 = atlasDao.findDataMap(dmID.intValue());
		Assert.assertNotNull("Found DM is not null", dm2);
		
		TestUtil.assertDeepEquals(dm, dm2);
				
		// Updates Data Map
		
		String changedCitation = "Modified citation";
		dm2.setCitation(changedCitation);
		
		Map<String, DataMapPlate> ps = AtlasServerUtils.createDataMapPlateMap(dm2);
		DataMapPlate p2 = ps.get(TestUtil.AP2_NAME);
		Assert.assertNotNull("dm2 plate is not null",p2);

		String changedUri = "changedUri";
		p2.setCoronalLayerImageURI(changedUri);
		
		atlasDao.addUpdateDataMap(dm2);
		atlasDao.em.flush(); 
		
		// Checks updates
		
		dm2 = atlasDao.findDataMap(dmID.intValue());
		Assert.assertNotNull("Found DataMap is not null", dm2);
		
		Assert.assertEquals(changedCitation, dm2.getCitation());
		Assert.assertEquals("Number of plates", TestUtil.AP_CNT, dm2.getDataMapPlates().size());
		ps = AtlasServerUtils.createDataMapPlateMap(dm2);
		p2 = ps.get(TestUtil.AP2_NAME);		
		
		Assert.assertNotNull("dm2 plate is not null",p2);
		Assert.assertEquals(changedUri, p2.getCoronalLayerImageURI());		
		
		// find DataMapPlate by key
		
		int pID = p2.getId();
		p2 = atlasDao.findDataMapPlate(pID);
		Assert.assertNotNull("Found plate", p2);
		
		// Removes Data Map
		
		int dm2ID = dm2.getId();
		atlasDao.deleteDataMap(dm2);
		atlasDao.em.flush();
		
		// Checks that Data Map and plate don't exist now
		
		Assert.assertNull("DataMap not found", atlasDao.findDataMap(dm2ID));
		Assert.assertNull("Plate not found", atlasDao.findDataMapPlate(pID));
		
		// Cleanup: delete atlas
		
		atlasDao.deleteAtlas(atlas);
		atlasDao.em.flush();
		
	}

	/**
	 * Retrieves a Data Map by URI.
	 */
	@Test
	public void testFindByURI() {

		Assert.assertNotNull("controller shouldn't be null", atlasDao);
		
		AtlasStructure atlas = TestUtil.createAtlasInstance();

		// Saves Atlas
		
		atlas = atlasDao.addUpdateAtlas(atlas);
		Integer atlasID = atlas.getAtlasID();
		atlasDao.em.flush();
		
		Assert.assertNotNull("AtlasID shouldn't be null", atlasID);
		System.out.println("AtlasID: " + atlasID);
		Assert.assertTrue("atlasID != 0", atlasID.intValue() != 0);
		
		// Creates Data Map
		
		DataMap dm = TestUtil.createDataMapInstance(atlas);
		
		// Saves DataMap

		dm = atlasDao.addUpdateDataMap(dm);		
		Integer dmID = dm.getId();
		atlasDao.em.flush();
		
		Assert.assertNotNull("dmID shouldn't be null", dmID);
		System.out.println("dmID: " + dmID);
		Assert.assertTrue("dmID != 0", dmID.intValue() != 0);
		
		// Retrieves Data Map by URI
		
		DataMap dm2 = atlasDao.findDataMap(TestUtil.DM_URI);
		Assert.assertNotNull("Found DM is not null", dm2);
		
		TestUtil.assertDeepEquals(dm, dm2);
		
		// Cleanup: Removes Data Map and Atlas
		
		int dm2ID = dm2.getId();
		atlasDao.deleteDataMap(dm2);
		atlasDao.deleteAtlas(atlas);

		atlasDao.em.flush();
		
	}

	/**
	 * Retrieves the descriptions of all Data Maps for a given atlas uri
	 */
	@Test
	public void testRetrieveAllDataMapsDescriptions() {

		Assert.assertNotNull("controller shouldn't be null", atlasDao);
		
		AtlasStructure atlas = TestUtil.createAtlasInstance();

		// Saves Atlas
		
		atlas = atlasDao.addUpdateAtlas(atlas);
		Integer atlasID = atlas.getAtlasID();
		atlasDao.em.flush();
		
		Assert.assertNotNull("AtlasID shouldn't be null", atlasID);
		System.out.println("AtlasID: " + atlasID);
		Assert.assertTrue("atlasID != 0", atlasID.intValue() != 0);
		
		// Creates Data Map
		
		DataMap dm = TestUtil.createDataMapInstance(atlas);
		
		// Saves DataMap

		dm = atlasDao.addUpdateDataMap(dm);		
		Integer dmID = dm.getId();
		atlasDao.em.flush();
		
		Assert.assertNotNull("dmID shouldn't be null", dmID);
		System.out.println("dmID: " + dmID);
		Assert.assertTrue("dmID != 0", dmID.intValue() != 0);
		
		// Retrieves all Data Map Descriptions
		
		List<DataMapDescription> ds = atlasDao.retrieveDataMapDescriptions(TestUtil.ATLAS_URI);
		Assert.assertNotNull("Retrieved DataMapDescriptions is not null", ds);
		Assert.assertEquals("Number of retrieved DataMapDescriptions",1,ds.size());
		DataMapDescription d = ds.get(0);
		TestUtil.assertCorresponds(dm, d);

		// Cleanup: Removes Data Map and Atlas
		
		atlasDao.deleteDataMap(dm);
		atlasDao.deleteAtlas(atlas);

		atlasDao.em.flush();
		
	}

}
