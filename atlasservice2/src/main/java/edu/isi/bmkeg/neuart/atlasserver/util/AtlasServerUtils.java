package edu.isi.bmkeg.neuart.atlasserver.util;

import java.util.HashMap;
import java.util.Map;

import edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMap;
import edu.isi.bmkeg.neuart.atlasserver.datamaps.model.DataMapPlate;
import edu.isi.bmkeg.neuart.atlasserver.model.AtlasPlate;
import edu.isi.bmkeg.neuart.atlasserver.model.AtlasStructure;

public class AtlasServerUtils {

	static public Map<String, AtlasPlate> createAtlasPlateMap(AtlasStructure a) {
		HashMap<String,AtlasPlate> aps = new HashMap<String,AtlasPlate>();
		for (AtlasPlate ap : a.getAtlasPlates()) {
			aps.put(ap.getPlateName(), ap);
		}
		return aps;
	}

	public static Map<String, DataMapPlate> createDataMapPlateMap(DataMap dm) {
		HashMap<String,DataMapPlate> ps = new HashMap<String,DataMapPlate>();
		for (DataMapPlate p : dm.getDataMapPlates()) {
			ps.put(p.getAtlasPlate().getPlateName(), p);
		}
		return ps;
	}

}
