package edu.isi.bmkeg.neuart.atlasserver.dao;

import java.util.List;

import edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMap;
import edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapDescription;
import edu.isi.bmkeg.neuart.atlasserver.model.AtlasDescription;
import edu.isi.bmkeg.neuart.atlasserver.model.AtlasStructure;
import edu.isi.bmkeg.neuart.atlasserver.model.BrainRegion;

public interface AtlasServerDao {

	public AtlasStructure addUpdateAtlas(AtlasStructure atlas);
	
	public AtlasStructure findAtlas(String atlasUri);
	
	public void deleteAtlas(AtlasStructure atlas);

	public List<AtlasDescription> retrieveAtlasDescriptions();

	public DataMap addUpdateDataMap(DataMap dm);

	public DataMap findDataMap(String uri);

	public List<DataMapDescription> retrieveDataMapDescriptions(String atlasURI);
	
	public void deleteDataMap(DataMap dm);
	
	public BrainRegion addUpdateBrainRegion(BrainRegion br);
	
	public void deleteBrainRegion(BrainRegion br);
	
	public BrainRegion findBrainRegion(String atlasUri, String brAbbrev);

}
