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

import edu.isi.bmkeg.neuart.atlasserver.ws.schema.AtlasDescription;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.AtlasPlate;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.AtlasStructure;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.GetAtlasStructureRequest;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.GetAtlasStructureResponse;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.ListAvailableAtlasesRequest;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.ListAvailableAtlasesResponse;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.ObjectFactory;
import edu.isi.bmkeg.neuart.atlasserver.ws.SchemaConversionUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration()
public class AtlasServiceEndpointTest {

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
    public void testGetAtlasStructure() throws Exception {

    	// creates an instance of AtlasStructure to be returned by the atlas service mock object
    	
        edu.isi.bmkeg.neuart.atlasserver.controller.AtlasStructure domainAtlas = edu.isi.bmkeg.neuart.atlasserver.controller.AtlasStructure.create(TestUtil.createAtlasInstance());

        // Sets atlas Service mock object

        String atlasURI = domainAtlas.atlasURI;
        expect(atlasServiceMock.getdAtlasStructure(atlasURI))
                .andReturn(domainAtlas);
        
        replay(atlasServiceMock);

        // Creates a GetAtlasStructureRequest object

        GetAtlasStructureRequest request = objectFactory.createGetAtlasStructureRequest();
        request.setAtlasURI(atlasURI);
        
        // invokes the GetAtlasStructure ednpoint
        
        GetAtlasStructureResponse response = endpoint.getAtlasStructure(request);
    
        // Verifies response
        
        Assert.assertNotNull("response shouldn't be null",response);
        AtlasStructure schemaAtlas = response.getReturn();
        verifyCorrespondence(domainAtlas, schemaAtlas);
        
        verify(atlasServiceMock);
    }

	@Test
    public void testListAvailableAtlases() throws Exception {

    	// creates an instance of AtlasDescription to be returned by the atlas service mock object
    	
        edu.isi.bmkeg.neuart.atlasserver.controller.AtlasDescription domainAtlasDescription = createDummyAtlasDescription();

        // Sets atlas Service mock object
        
        
        List<edu.isi.bmkeg.neuart.atlasserver.controller.AtlasDescription> 
        	domainAtlasDescs = 
        		Arrays.asList(new edu.isi.bmkeg.neuart.atlasserver.controller.AtlasDescription[] {domainAtlasDescription});
        expect(atlasServiceMock.listAvailableAtlases())
                .andReturn(domainAtlasDescs);
        
        replay(atlasServiceMock);

        // Creates a ListAvailableAtlasesRequest object

        ListAvailableAtlasesRequest request = objectFactory.createListAvailableAtlasesRequest();
        
        // invokes the ListAvailableAtlases ednpoint
        
        ListAvailableAtlasesResponse response = endpoint.listAvailableAtlases(request);
    
        // Verifies response
        
        Assert.assertNotNull("response shouldn't be null",response);
        List<AtlasDescription> schemaAtlasDescs = response.getReturn();
        AtlasDescription schemaAtlasDesc = schemaAtlasDescs.get(0);
        verifyCorrespondence(domainAtlasDescription, schemaAtlasDesc);
        
        verify(atlasServiceMock);
    }

    private edu.isi.bmkeg.neuart.atlasserver.controller.AtlasDescription createDummyAtlasDescription() {
    	edu.isi.bmkeg.neuart.atlasserver.controller.AtlasDescription atlasDesc = 
    		new edu.isi.bmkeg.neuart.atlasserver.controller.AtlasDescription();
    	
    	atlasDesc.atlasURI = "http://atlas1";
    	atlasDesc.atlasName = "atlas 1";
    	atlasDesc.atlasDescription = "Atlas 1 Desc";
    	atlasDesc.atlasYear = 1999;
    	return atlasDesc;
	}

	private void verifyCorrespondence(
			edu.isi.bmkeg.neuart.atlasserver.controller.AtlasDescription domainAtlasDesc,
			AtlasDescription schemaAtlasDesc) {
    	
		Assert.assertEquals(domainAtlasDesc.atlasDescription, schemaAtlasDesc.getAtlasDescription());
		Assert.assertEquals(domainAtlasDesc.atlasName, schemaAtlasDesc.getAtlasName());
		Assert.assertEquals(domainAtlasDesc.atlasURI, schemaAtlasDesc.getAtlasURI());
		Assert.assertEquals(domainAtlasDesc.atlasYear, schemaAtlasDesc.getAtlasYear().longValue());
	}

	public static void verifyCorrespondence(
    		edu.isi.bmkeg.neuart.atlasserver.controller.AtlasStructure domainAtlas, 
    		AtlasStructure schemaAtlas) {
    	Double epsilon = 0.001;
		Assert.assertEquals(domainAtlas.atlasDescription, schemaAtlas.getAtlasDescription());
		Assert.assertEquals(domainAtlas.atlasName, schemaAtlas.getAtlasName());
		Assert.assertEquals(domainAtlas.atlasURI, schemaAtlas.getAtlasURI());
		Assert.assertEquals(domainAtlas.atlasYear, schemaAtlas.getAtlasYear().longValue());
		Assert.assertEquals(SchemaConversionUtils.contextualizePath(null, domainAtlas.sagitalImageURI).toString(), schemaAtlas.getSagitalImageURI());
		Assert.assertEquals(domainAtlas.sagitalZLength, schemaAtlas.getSagitalZLength(), epsilon);
		Assert.assertEquals(domainAtlas.distanceFromLeftToBregma, schemaAtlas.getDistanceFromLeftToBregma(), epsilon);
		
		Assert.assertEquals("Should have equal number of plates", domainAtlas.getAtlasPlates().size(), domainAtlas.getAtlasPlates().size());
		
		Map<String, edu.isi.bmkeg.neuart.atlasserver.controller.AtlasPlate> domainPlates = createAtlasPlateMap(domainAtlas);
		
		for (AtlasPlate schemaPlate : schemaAtlas.getAtlasPlates()) {
			edu.isi.bmkeg.neuart.atlasserver.controller.AtlasPlate domainPlate = domainPlates.get(schemaPlate.getPlateName());
			Assert.assertNotNull("There should be a domain plate corresponding a schema plate", domainPlate);
			Assert.assertEquals(schemaPlate.getCoronalImageURI(), SchemaConversionUtils.contextualizePath(null, domainPlate.coronalImageURI).toString());
			Assert.assertEquals(schemaPlate.getSagitalZOffsetFromLeft(), domainPlate.sagitalZOffsetFromLeft, epsilon);
			Assert.assertEquals(schemaPlate.getCoronalThumbnailURI(), SchemaConversionUtils.contextualizePath(null, domainPlate.coronalThumbnailURI).toString());
			Assert.assertSame("Parent reference matches", domainAtlas, domainPlate.parent);
		}
    }

	static public Map<String, edu.isi.bmkeg.neuart.atlasserver.controller.AtlasPlate> createAtlasPlateMap(edu.isi.bmkeg.neuart.atlasserver.controller.AtlasStructure a) {
		HashMap<String,edu.isi.bmkeg.neuart.atlasserver.controller.AtlasPlate> aps = new HashMap<String,edu.isi.bmkeg.neuart.atlasserver.controller.AtlasPlate>();
		for (edu.isi.bmkeg.neuart.atlasserver.controller.AtlasPlate ap : a.getAtlasPlates()) {
			aps.put(ap.plateName, ap);
		}
		return aps;
	}

}