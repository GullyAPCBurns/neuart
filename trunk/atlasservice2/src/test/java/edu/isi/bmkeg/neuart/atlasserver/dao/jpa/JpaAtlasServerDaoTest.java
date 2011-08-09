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
import edu.isi.bmkeg.neuart.atlasserver.model.AtlasDescription;
import edu.isi.bmkeg.neuart.atlasserver.model.AtlasPlate;
import edu.isi.bmkeg.neuart.atlasserver.model.AtlasStructure;
import edu.isi.bmkeg.neuart.atlasserver.model.BrainRegion;
import edu.isi.bmkeg.neuart.atlasserver.util.AtlasServerUtils;

@TransactionConfiguration(defaultRollback=true)
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration()
public class JpaAtlasServerDaoTest {

	private JpaAtlasServerDao atlasDao;
	
	@Resource
	public void setSrv(JpaAtlasServerDao atlasDao) {
		this.atlasDao = atlasDao;
	}

	/**
	 * saves an atlas, retrieves saved atlas, updates the atlas, and deletes it.
	 */
	@Test
	public void testCreateFindDelete() {
		Assert.assertNotNull("atlasDao shouldn't be null", atlasDao);
		
		AtlasStructure atlas = TestUtil.createAtlasInstance();

		// Saves Atlas
		
		atlas = atlasDao.addUpdateAtlas(atlas);
		Integer atlasID = atlas.getAtlasID();
		atlasDao.em.flush();
		
		Assert.assertNotNull("AtlasID shouldn't be null", atlasID);
		System.out.println("AtlasID: " + atlasID);
		Assert.assertTrue("atlasID != 0", atlasID.intValue() != 0);
		
		// Retrieves Atlas
		
		AtlasStructure a = atlasDao.findAtlas(atlasID.intValue());
		Assert.assertNotNull("Found Atlas is not null", a);
		
		TestUtil.assertDeepEquals(atlas, a);
		
		// Updates atlas
		
		a.setAtlasYear(1900);
		Map<String, AtlasPlate> ps = AtlasServerUtils.createAtlasPlateMap(a);
		AtlasPlate ap = ps.get(TestUtil.AP2_NAME);
		((AtlasPlate) ap).setSagitalZOffsetFromLeft(1000.0);
		
		atlasDao.addUpdateAtlas(a);
		atlasDao.em.flush();
		
		// Checks updates
		
		a = atlasDao.findAtlas(atlasID.intValue());
		Assert.assertNotNull("Found Atlas is not null", a);
		
		Assert.assertEquals(1900, a.getAtlasYear());
		Assert.assertEquals("Number of plates", TestUtil.AP_CNT, a.getAtlasPlates().size());
		ps = AtlasServerUtils.createAtlasPlateMap(a);
		ap = ps.get(TestUtil.AP2_NAME);		
		
		Assert.assertNotNull("plate 2 not null", ap);
		Assert.assertEquals(1000.0, ap.getSagitalZOffsetFromLeft(), 0.001);		
		
		// find atlasPlate by key
		
		int pID = ((AtlasPlate) ap).getPlateID();
		ap = atlasDao.findAtlasPlate(pID);
		Assert.assertNotNull("Found plate", ap);
		
		// Removes atlas
		
		int aID = a.getAtlasID();
		atlasDao.deleteAtlas(a);
		atlasDao.em.flush();
		
		// Checks that atlas and plate don't exist now
		
		Assert.assertNull("Atlas not found", atlasDao.findAtlas(aID));
		Assert.assertNull("Plate not found", atlasDao.findAtlasPlate(pID));
		
	}

	/**
	 * Retrieves an atlas by URI.
	 */
	@Test
	public void testFindByURI() {

		Assert.assertNotNull("controller shouldn't be null", atlasDao);
		
		AtlasStructure atlas = TestUtil.createAtlasInstance();

		// Saves Atlas
		
		atlas = atlasDao.addUpdateAtlas(atlas);
		Integer atlasID = atlas.getAtlasID();
		atlasDao.em.flush();
		
		Assert.assertNotNull("AtlasID is not null", atlasID);
		System.out.println("AtlasID: " + atlasID);
		Assert.assertTrue("atlasID != 0", atlasID.intValue() != 0);
		
		// Retrieves Atlas by URI
		
		AtlasStructure a = atlasDao.findAtlas(TestUtil.ATLAS_URI);
		Assert.assertNotNull("Found Atlas is not null", a);
		
		TestUtil.assertDeepEquals(atlas, a);
					
		// Removes atlas
		
		atlasDao.deleteAtlas(a);
		atlasDao.em.flush();
	}

	/**
	 * Retrieves All atlas Descriptions.
	 */
	@Test
	public void testRetrieveAllAtlasDescriptions() {

		Assert.assertNotNull("controller shouldn't be null", atlasDao);
		
		AtlasStructure a = TestUtil.createAtlasInstance();

		// Saves Atlas
		
		a = atlasDao.addUpdateAtlas(a);
		Integer atlasID = a.getAtlasID();
		atlasDao.em.flush();

		Assert.assertNotNull("AtlasID is not null", atlasID);
		System.out.println("AtlasID: " + atlasID);
		Assert.assertTrue("atlasID != 0", atlasID.intValue() != 0);
		
		// Retrieves Atlas Descriptions
		
		List<AtlasDescription> ads = atlasDao.retrieveAtlasDescriptions();
		Assert.assertNotNull("Retrieved AtlasDescriptions is not null", ads);
		Assert.assertEquals("Number of retrieved Atlas Descriptions",1,ads.size());
		AtlasDescription ad = ads.get(0);
		TestUtil.assertCorresponds(a, ad);

		// Removes atlas
		
		atlasDao.deleteAtlas(a);
		atlasDao.em.flush();

	}

	/**
	 * saves a brain region, retrieves saved brain region, updates the brain region, and deletes it.
	 */
	@Test
	public void testCreateFindDeleteBrainRegion() {
		Assert.assertNotNull("atlasDao shouldn't be null", atlasDao);
		
		
		AtlasStructure atlas = TestUtil.createAtlasInstance();
		atlas = atlasDao.addUpdateAtlas(atlas);
		atlasDao.em.flush();

		BrainRegion br = TestUtil.createBrainRegionInstance(atlas);

		// Saves 
		
		br = atlasDao.addUpdateBrainRegion(br);
		Integer brID = br.getID();
		atlasDao.em.flush();
		
		Assert.assertNotNull("brID shouldn't be null", brID);
		System.out.println("brID: " + brID);
		Assert.assertTrue("abrID != 0", brID.intValue() != 0);
		
		// Retrieves 
		
		BrainRegion b = atlasDao.findBrainRegion(brID.intValue());
		Assert.assertNotNull("Found BrainRegion is not null", b);
		
		TestUtil.assertDeepEquals(br, b);
		
		// Updates 
		
		b.setAtlasMaxlevel(30);
		atlasDao.addUpdateBrainRegion(br);
		atlasDao.em.flush();
		
		// Checks updates
		
		b = atlasDao.findBrainRegion(brID.intValue());
		Assert.assertNotNull("Found BrainRegion is not null", b);
		
		Assert.assertEquals(30, b.getAtlasMaxlevel());
		
		// Removes 
		
		int bID = b.getID();
		atlasDao.deleteBrainRegion(b);
		atlasDao.em.flush();
		
		// Checks that  doesn't exist now
		
		Assert.assertNull("BrainRegion not found", atlasDao.findBrainRegion(bID));
		
		// Deletes Atlas
		atlasDao.deleteAtlas(atlas);
		atlasDao.em.flush();

	}

	/**
	 * Retrieves a BrainRegion by abbreviation.
	 */
	@Test
	public void testFindBrainRegionByURI() {

		Assert.assertNotNull("controller shouldn't be null", atlasDao);
		
		AtlasStructure atlas = TestUtil.createAtlasInstance();
		atlas = atlasDao.addUpdateAtlas(atlas);
		String atlasUri = atlas.getAtlasURI();
		atlasDao.em.flush();

		BrainRegion br = TestUtil.createBrainRegionInstance(atlas);
		String abbrev = br.getAbbreviation();

		// Saves 
		
		br = atlasDao.addUpdateBrainRegion(br);
		Integer brID = br.getID();
		atlasDao.em.flush();
		
		Assert.assertNotNull("brID shouldn't be null", brID);
		System.out.println("brID: " + brID);
		Assert.assertTrue("abrID != 0", brID.intValue() != 0);
				
		// Retrieves BrainRegion by abbreviation
		
		BrainRegion b = atlasDao.findBrainRegion(atlasUri,abbrev);
		Assert.assertNotNull("Found BrainRegion is not null", b);
		
		TestUtil.assertDeepEquals(br, b);
					
		// Removes atlas

		// Removes 
		
		atlasDao.deleteBrainRegion(br);
		atlasDao.deleteAtlas(atlas);
		atlasDao.em.flush();
		
	}
}
