package edu.isi.bmkeg.neuart.atlasserver.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Repository;

import edu.isi.bmkeg.neuart.atlasserver.dao.AtlasServerDao;
import edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMap;
import edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapDescription;
import edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapPlate;
import edu.isi.bmkeg.neuart.atlasserver.model.AtlasDescription;
import edu.isi.bmkeg.neuart.atlasserver.model.AtlasPlate;
import edu.isi.bmkeg.neuart.atlasserver.model.AtlasStructure;
import edu.isi.bmkeg.neuart.atlasserver.model.BrainRegion;

@Repository
public class JpaAtlasServerDao implements AtlasServerDao {
	
	private static final Log logger = LogFactory.getLog(JpaAtlasServerDao.class);

    @PersistenceContext 
	public EntityManager em;

	public JpaAtlasServerDao() {
	}

	public AtlasStructure addUpdateAtlas(AtlasStructure atlas) {
		logger.debug("** addUpdateAtlas called...");
			
		// When passing Boolean and Number values from the Flash client to a
		// Java object, Java interprets null values as the default values for
		// primitive types; for example, 0 for double, float, long, int, short,
		// byte.
		if (atlas.getAtlasID() == null
				|| atlas.getAtlasID() == 0) {
			// New atlas is created
			logger.debug("** atlas ID is null and atlas will be added ...");
			atlas.setAtlasID(null);
		} else {
			// Existing atlas is updated - do nothing.
			logger.debug("** atlas ID is NOT null and atlas will be updated ...");
		}

		AtlasStructure result = em.merge(atlas);
		logger.debug("** atlas was merged: resulting atlasID = " + result.getAtlasID());
		
		return result;

	}
	
	public AtlasStructure findAtlas(int atlasID) {
		logger.debug("** findAtlas called...");
		
		return em.find(AtlasStructure.class, new Integer(atlasID));
	}

	public AtlasPlate findAtlasPlate(int plateID) {
		logger.debug("** findAtlasPlate called...");
		
		return em.find(AtlasPlate.class, new Integer(plateID));
	}

	public void deleteAtlas(AtlasStructure atlas) {
		logger.debug("** deleteAtlas called...");
				
		em.remove(atlas);
	}

	public AtlasStructure findAtlas(String atlasUri) {
		logger.debug("** findAtlas called: " + atlasUri);
		Query q = em.createNamedQuery("atlasstructure.findbyuri");
		q.setParameter("uri",atlasUri);
		AtlasStructure result = (AtlasStructure) q.getSingleResult();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<AtlasDescription> retrieveAtlasDescriptions() {
		Query q = em.createNamedQuery("atlasstructure.retrieveDescriptions");
		List<AtlasDescription> result = (List<AtlasDescription>) q.getResultList();
		return result;
	}

	public DataMap addUpdateDataMap(DataMap dm) {
		logger.debug("** addUpdateDataMap called...");
		
		// When passing Boolean and Number values from the Flash client to a
		// Java object, Java interprets null values as the default values for
		// primitive types; for example, 0 for double, float, long, int, short,
		// byte.
		if (dm.getId() == null
				|| dm.getId() == 0) {
			// New atlas is created
			logger.debug("** dm ID is null and dm will be added ...");
			dm.setId(null);
		} else {
			// Existing atlas is updated - do nothing.
			logger.debug("** dm ID is NOT null and dm will be updated ...");
		}

		DataMap result = em.merge(dm);
		logger.debug("** dm was merged: resulting Id = " + result.getId());
		
		return result;

	}

	public DataMap findDataMap(int id) {
		logger.debug("** findDataMap called...");
		
		return em.find(DataMap.class, new Integer(id));
	}

	public DataMapPlate findDataMapPlate(int id) {
		logger.debug("** finDataMapPlate called...");
		
		return em.find(DataMapPlate.class, new Integer(id));
	}

	public DataMap findDataMap(String uri) {
		logger.debug("** findDataMap by uri called...");
		
		Query q = em.createNamedQuery("datamap.findbyuri");
		q.setParameter("uri",uri);
		DataMap result = (DataMap) q.getSingleResult();
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<DataMapDescription> retrieveDataMapDescriptions(String atlasURI) {
		Query q = em.createNamedQuery("datamap.retrieveDescriptions");
		q.setParameter("atlas_uri",atlasURI);
		List<DataMapDescription> result = (List<DataMapDescription>) q.getResultList();
		return result;
	}

	public void deleteDataMap(DataMap dm) {
		logger.debug("** deleteDataMap called...");
		em.remove(dm);
	}

	public BrainRegion addUpdateBrainRegion(BrainRegion br) {
		logger.debug("** addUpdateBrainRegion called...");
			
		// When passing Boolean and Number values from the Flash client to a
		// Java object, Java interprets null values as the default values for
		// primitive types; for example, 0 for double, float, long, int, short,
		// byte.
		if (br.getID() == null
				|| br.getID() == 0) {
			// New atlas is created
			logger.debug("** brain region ID is null and will be added ...");
			br.setID(null);
		} else {
			// Existing brain region is updated - do nothing.
			logger.debug("** barin region ID is NOT null and will be updated ...");
		}

		BrainRegion result = em.merge(br);
		logger.debug("** brain region was merged: resulting ID = " + result.getID());
		
		return result;

	}
	
	public BrainRegion findBrainRegion(int brID) {
		logger.debug("** findBrainRegion called...");
		
		return em.find(BrainRegion.class, new Integer(brID));
	}

	public void deleteBrainRegion(BrainRegion br) {
		logger.debug("** deleteBrainRegion called...");
				
		em.remove(br);
	}
	
	public BrainRegion findBrainRegion(String atlasUri, String brAbbrev) {
		logger.debug("** findBrainRegion called: " + atlasUri + ", " + brAbbrev);
		Query q = em.createNamedQuery("brainregion.findbyabbrev");
		q.setParameter("atlasuri",atlasUri);
		q.setParameter("abbrev",brAbbrev);
		BrainRegion result = (BrainRegion) q.getSingleResult();
		return result;
	}
}
