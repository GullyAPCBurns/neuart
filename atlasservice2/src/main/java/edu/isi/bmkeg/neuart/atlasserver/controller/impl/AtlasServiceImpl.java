package edu.isi.bmkeg.neuart.atlasserver.controller.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import edu.isi.bmkeg.neuart.atlasserver.controller.AtlasService;
import edu.isi.bmkeg.neuart.atlasserver.dao.AtlasServerDao;
import edu.isi.bmkeg.neuart.atlasserver.controller.BrainRegion;
import edu.isi.bmkeg.neuart.atlasserver.controller.DataMap;
import edu.isi.bmkeg.neuart.atlasserver.controller.DataMapDescription;
import edu.isi.bmkeg.neuart.atlasserver.controller.AtlasDescription;
import edu.isi.bmkeg.neuart.atlasserver.controller.AtlasStructure;

@RemotingDestination
@Transactional
@Service
public class AtlasServiceImpl implements AtlasService  {
	
	// TODO revisit the logging platform we will adopt
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(AtlasServiceImpl.class);

	@Autowired
    private AtlasServerDao atlasDao;

    public void setAtlasServerDao(AtlasServerDao atlasDao) {
        Assert.notNull(atlasDao, "atlasDao must not be null");
        this.atlasDao = atlasDao;
    }

	@Override
	public DataMap getDataMap(String uri) {
		return DataMap.create(atlasDao.findDataMap(uri));
	}

	@Override
	public AtlasStructure getdAtlasStructure(String atlasUri) {
		edu.isi.bmkeg.neuart.atlasserver.model.AtlasStructure dataAS = atlasDao.findAtlas(atlasUri);
		if (dataAS == null)
			return null;
		else
			return AtlasStructure.create(dataAS);
	}

	@Override
	public List<AtlasDescription> listAvailableAtlases() {
		List<edu.isi.bmkeg.neuart.atlasserver.model.AtlasDescription> dataADs = atlasDao.retrieveAtlasDescriptions();
		if (dataADs == null)
			return null;
		else
			return AtlasDescription.convertList(dataADs);
	}

	@Override
	public List<DataMapDescription> listAvailableDataMaps(String atlasURI) {
		List<edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapDescription> dataDDs = atlasDao.retrieveDataMapDescriptions(atlasURI);
		if (dataDDs == null)
			return null;
		else
			return DataMapDescription.convertList(dataDDs);
	}

	@Override
	public BrainRegion getBrainRegion(String abbrev, String atlasUri) {
		return BrainRegion.create(atlasDao.findBrainRegion(atlasUri, abbrev));
	}
}
