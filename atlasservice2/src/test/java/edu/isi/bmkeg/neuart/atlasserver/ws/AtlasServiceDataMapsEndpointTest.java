/*
 * Copyright 2005-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.isi.bmkeg.neuart.atlasserver.ws;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import edu.isi.bmkeg.neuart.atlasserver.TestUtil;
import edu.isi.bmkeg.neuart.atlasserver.controller.AtlasService;

import edu.isi.bmkeg.neuart.atlasserver.ws.schema.DataMap;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.DataMapDescription;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.DataMapPlate;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.GetDataMapRequest;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.GetDataMapResponse;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.ListAvailableDataMapsRequest;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.ListAvailableDataMapsResponse;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.ObjectFactory;
import edu.isi.bmkeg.neuart.atlasserver.ws.SchemaConversionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="AtlasServiceEndpointTest-context.xml")
public class AtlasServiceDataMapsEndpointTest {

    private AtlasServiceEndpoint endpoint;

    private AtlasService atlasServiceMock;

    private edu.isi.bmkeg.neuart.atlasserver.ws.schema.ObjectFactory objectFactory;

	@Before
    public void setUp() {
    	atlasServiceMock = EasyMock.createMock(AtlasService.class);
        endpoint = new AtlasServiceEndpoint();
        endpoint.setAtlasServerController(atlasServiceMock);
        objectFactory = new ObjectFactory();
    }
    
    @Test
    public void testGetDataMap() throws Exception {

    	// creates an instance of DataMap to be returned by the atlas service mock object
    	
        edu.isi.bmkeg.neuart.atlasserver.model.AtlasStructure domainAtlas = TestUtil.createAtlasInstance();
        edu.isi.bmkeg.neuart.atlasserver.controller.DataMap domainDataMap =  
        	edu.isi.bmkeg.neuart.atlasserver.controller.DataMap.create(TestUtil.createDataMapInstance(domainAtlas));

        // Sets atlas Service mock object

        String dataMapURI = domainDataMap.uri;
        expect(atlasServiceMock.getDataMap(dataMapURI))
                .andReturn(domainDataMap);
        
        replay(atlasServiceMock);

        // Creates a GetDataMapRequest object

        GetDataMapRequest request = objectFactory.createGetDataMapRequest();
        request.setDatamapURI(dataMapURI);
        
        // invokes the GetDataMap ednpoint
        
        GetDataMapResponse response = endpoint.getDataMap(request);
    
        // Verifies response
        
        Assert.assertNotNull("response shouldn't be null",response);
        DataMap schemaDataMap = response.getReturn();
        verifyCorrespondence(domainDataMap, schemaDataMap);
        
        verify(atlasServiceMock);
    }

	@Test
    public void testListAvailableDataMaps() throws Exception {

    	// creates an instance of DataMapDescription to be returned by the atlas service mock object
    	
        edu.isi.bmkeg.neuart.atlasserver.controller.DataMapDescription domainDataMapDescription = createDummyDataMapDescription();

        // Sets atlas Service mock object
                
        List<edu.isi.bmkeg.neuart.atlasserver.controller.DataMapDescription> 
        	domainDataMapDescs = 
        		Arrays.asList(new edu.isi.bmkeg.neuart.atlasserver.controller.DataMapDescription[] {domainDataMapDescription});

        String dataMapURI = domainDataMapDescription.uri;

        expect(atlasServiceMock.listAvailableDataMaps(dataMapURI))
                .andReturn(domainDataMapDescs);
        
        replay(atlasServiceMock);

        // Creates a ListAvailableDataMapsRequest object

        ListAvailableDataMapsRequest request = objectFactory.createListAvailableDataMapsRequest();
        request.setAtlasURI(dataMapURI);
        
        // invokes the ListAvailableDataMaps ednpoint
        
        ListAvailableDataMapsResponse response = endpoint.listAvailableDataMaps(request);
    
        // Verifies response
        
        Assert.assertNotNull("response shouldn't be null",response);
        List<DataMapDescription> schemaDataMapDescs = response.getReturn();
        DataMapDescription schemaDataMapDesc = schemaDataMapDescs.get(0);
        verifyCorrespondence(domainDataMapDescription, schemaDataMapDesc);
        
        verify(atlasServiceMock);
    }

    private edu.isi.bmkeg.neuart.atlasserver.controller.DataMapDescription createDummyDataMapDescription() {
    	edu.isi.bmkeg.neuart.atlasserver.controller.DataMapDescription dmDesc = 
    		new edu.isi.bmkeg.neuart.atlasserver.controller.DataMapDescription();
    	
    	dmDesc.uri = "http://dm1";
    	dmDesc.name = "dm 1";
    	dmDesc.description = "Dm 1 Desc";
    	dmDesc.citation = "Dm Citation";
    	dmDesc.digitalLibraryKey = "Dm digital key";
    	dmDesc.atlasUri = "http://atlas1";
    	return dmDesc;
	}

	private void verifyCorrespondence(
			edu.isi.bmkeg.neuart.atlasserver.controller.DataMapDescription domainDataMapDesc,
			DataMapDescription schemaDataMapDesc) {
    	
		Assert.assertEquals(domainDataMapDesc.description, schemaDataMapDesc.getDescription());
		Assert.assertEquals(domainDataMapDesc.name, schemaDataMapDesc.getName());
		Assert.assertEquals(domainDataMapDesc.uri, schemaDataMapDesc.getUri());
		Assert.assertEquals(domainDataMapDesc.citation, schemaDataMapDesc.getCitation());
		Assert.assertEquals(domainDataMapDesc.digitalLibraryKey, schemaDataMapDesc.getDigitalLibraryKey());
		Assert.assertEquals(domainDataMapDesc.atlasUri, schemaDataMapDesc.getAtlasURI());
	}

	public static void verifyCorrespondence(
			edu.isi.bmkeg.neuart.atlasserver.controller.DataMap domainDataMap, 
    		DataMap schemaDataMap) {
		Assert.assertEquals(domainDataMap.description, schemaDataMap.getDescription());
		Assert.assertEquals(domainDataMap.name, schemaDataMap.getName());
		Assert.assertEquals(domainDataMap.uri, schemaDataMap.getUri());
		Assert.assertEquals(domainDataMap.citation, schemaDataMap.getCitation());
		Assert.assertEquals(domainDataMap.digitalLibraryKey, schemaDataMap.getDigitalLibraryKey());
		Assert.assertEquals(domainDataMap.atlasURI, schemaDataMap.getAtlasURI());
		
		Assert.assertEquals("Should have equal number of plates", domainDataMap.getDataMapPlates().size(), schemaDataMap.getDataMapPlates().size());
		
		Map<String, edu.isi.bmkeg.neuart.atlasserver.controller.DataMapPlate> domainDataMapPlates = createDataMapPlateMap(domainDataMap);
		
		for (DataMapPlate schemaDataMapPlate : schemaDataMap.getDataMapPlates()) {
			edu.isi.bmkeg.neuart.atlasserver.controller.DataMapPlate domainDataMapPlate = domainDataMapPlates.get(schemaDataMapPlate.getAtlasPlateName());
			Assert.assertNotNull("There should be a domain plate corresponding a schema plate", domainDataMapPlate);
			Assert.assertEquals(schemaDataMapPlate.getCoronalLayerImageURI(), SchemaConversionUtils.contextualizePath(null, domainDataMapPlate.coronalLayerImageURI).toString());
			Assert.assertSame("Parent reference matches", domainDataMap, domainDataMapPlate.parent);
		}
    }

	public static Map<String, edu.isi.bmkeg.neuart.atlasserver.controller.DataMapPlate> createDataMapPlateMap(edu.isi.bmkeg.neuart.atlasserver.controller.DataMap dm) {
		HashMap<String,edu.isi.bmkeg.neuart.atlasserver.controller.DataMapPlate> ps = new HashMap<String,edu.isi.bmkeg.neuart.atlasserver.controller.DataMapPlate>();
		for (edu.isi.bmkeg.neuart.atlasserver.controller.DataMapPlate p : dm.getDataMapPlates()) {
			ps.put(p.atlasPlateName, p);
		}
		return ps;
	}
}