package edu.isi.bmkeg.neuart.atlasserver.ws;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.isi.bmkeg.neuart.atlasserver.ws.schema.AtlasDescription;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.AtlasStructure;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.DataMap;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.DataMapDescription;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.GetAtlasStructureRequest;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.GetAtlasStructureResponse;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.GetDataMapRequest;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.GetDataMapResponse;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.ListAvailableAtlasesRequest;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.ListAvailableAtlasesResponse;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.ListAvailableDataMapsRequest;
import edu.isi.bmkeg.neuart.atlasserver.ws.schema.ListAvailableDataMapsResponse;
import edu.isi.bmkeg.neuart.atlasserver.controller.AtlasService;
import edu.isi.bmkeg.neuart.atlasserver.ws.SchemaConversionUtils;

/**
 * Endpoint that handles the Atlas Server Web Service messages using JAXB2 marshalling.
 */
@Endpoint
public class AtlasServiceEndpoint {

    public static final String GET_ATLAS_STRUCTURE_REQUEST = "GetAtlasStructureRequest";
    public static final String LIST_AVAILABLE_ATLASES_REQUEST = "ListAvailableAtlasesRequest";
    public static final String GET_DATA_MAP_REQUEST = "GetDataMapRequest";
    public static final String LIST_AVAILABLE_DATA_MAPS_REQUEST = "ListAvailableDataMapsRequest";

    public static final String MESSAGES_NAMESPACE = "http://atlasserver.bmkeg.isi.edu/atlaswebservice/schemas/messages";

    // TODO revisit the logging platform we will adopt
	private static final Log logger = LogFactory.getLog(AtlasServiceEndpoint.class);

	@Autowired
    private AtlasService atlasService;

    public void setAtlasServerController(AtlasService controller) {
        Assert.notNull(controller, "controller must not be null");
        this.atlasService = controller;
    }
   
    /**
     * This endpoint method uses marshalling to handle message with a <code>&lt;ListAvailableAtlasesReequest&gt;</code> payload.
     *
     * @param request the JAXB2 representation of a <code>&lt;ListAvailableAtlasesReequest&gt;</code>
     */
    @Transactional(readOnly = true)
    @PayloadRoot(localPart = LIST_AVAILABLE_ATLASES_REQUEST, namespace = MESSAGES_NAMESPACE)
    public ListAvailableAtlasesResponse listAvailableAtlases(ListAvailableAtlasesRequest request) {

    	if (logger.isDebugEnabled()) {
            logger.debug("Received " + LIST_AVAILABLE_ATLASES_REQUEST);
        }
        
        ListAvailableAtlasesResponse response = new ListAvailableAtlasesResponse();

        List<edu.isi.bmkeg.neuart.atlasserver.controller.AtlasDescription> domainAtlasDescriptions =
        	atlasService.listAvailableAtlases();
        if (domainAtlasDescriptions != null) {

            List<AtlasDescription> schemaAtlasDescriptions = 
            	SchemaConversionUtils.toSchemaTypeAtlasDescriptions(domainAtlasDescriptions);

            response.getReturn().addAll(schemaAtlasDescriptions);
        }
        
        return response;
    }

    /**
     * This endpoint method uses marshalling to handle message with a <code>&lt;GetAtlasStructureRequest&gt;</code> payload.
     *
     * @param request the JAXB2 representation of a <code>&lt;GetAtlasStructureRequest&gt;</code>
     */
    @Transactional(readOnly = true)
    @PayloadRoot(localPart = GET_ATLAS_STRUCTURE_REQUEST, namespace = MESSAGES_NAMESPACE)
    public GetAtlasStructureResponse getAtlasStructure(GetAtlasStructureRequest request) {

    	//TODO URL of images should be dynamically generated to point to deployed service 
    	if (logger.isDebugEnabled()) {
            logger.debug("Received " + GET_ATLAS_STRUCTURE_REQUEST);
        }
    	
        
    	String atlasUri = request.getAtlasURI();
    	Assert.notNull(atlasUri);
    	
    	GetAtlasStructureResponse response = new GetAtlasStructureResponse();

        edu.isi.bmkeg.neuart.atlasserver.controller.AtlasStructure domainAtlasStructure =
        	atlasService.getdAtlasStructure(atlasUri);
        if (domainAtlasStructure != null) {

            AtlasStructure schemaAtlasStructure = 
            	SchemaConversionUtils.toSchemaType(domainAtlasStructure, getContextURI());

            response.setReturn(schemaAtlasStructure);
        }
        
        return response;
    }

    /**
     * This endpoint method uses marshalling to handle message with a <code>&lt;ListAvailableDataMapsRequest&gt;</code> payload.
     *
     * @param request the JAXB2 representation of a <code>&lt;ListAvailableDataMapsRequest&gt;</code>
     */
    @Transactional(readOnly = true)
    @PayloadRoot(localPart = LIST_AVAILABLE_DATA_MAPS_REQUEST, namespace = MESSAGES_NAMESPACE)
    public ListAvailableDataMapsResponse listAvailableDataMaps(ListAvailableDataMapsRequest request) {

    	if (logger.isDebugEnabled()) {
            logger.debug("Received " + LIST_AVAILABLE_DATA_MAPS_REQUEST);
        }
        
        ListAvailableDataMapsResponse response = new ListAvailableDataMapsResponse();

        List<edu.isi.bmkeg.neuart.atlasserver.controller.DataMapDescription> domainDataMapDescriptions =
        	atlasService.listAvailableDataMaps(request.getAtlasURI());
        if (domainDataMapDescriptions != null) {

            List<DataMapDescription> schemaDataMapDescriptions = 
            	SchemaConversionUtils.toSchemaTypeDataMapDescriptions(domainDataMapDescriptions);

            response.getReturn().addAll(schemaDataMapDescriptions);
        }
        
        return response;
    }

    /**
     * This endpoint method uses marshalling to handle message with a <code>&lt;GetAtlasStructureRequest&gt;</code> payload.
     *
     * @param request the JAXB2 representation of a <code>&lt;GetAtlasStructureRequest&gt;</code>
     */
    @Transactional(readOnly = true)
    @PayloadRoot(localPart = GET_DATA_MAP_REQUEST, namespace = MESSAGES_NAMESPACE)
    public GetDataMapResponse getDataMap(GetDataMapRequest request) {

    	//TODO URL of images should be dynamically generated to point to deployed service 
    	if (logger.isDebugEnabled()) {
            logger.debug("Received " + GET_DATA_MAP_REQUEST);
        }
        
    	String dmUri = request.getDatamapURI();
    	Assert.notNull(dmUri);
    	
    	GetDataMapResponse response = new GetDataMapResponse();

        edu.isi.bmkeg.neuart.atlasserver.controller.DataMap domainDataMap =
        	atlasService.getDataMap(dmUri);
        if (domainDataMap != null) {

        	DataMap schemaDataMap = 
            	SchemaConversionUtils.toSchemaType(domainDataMap, getContextURI());

            response.setReturn(schemaDataMap);
        }
        
        return response;
    }

    /**
     * Returns the context URI corresponding to this webapp.
     */
    private URI getContextURI() {
		URI webContextURI = null;
		try {
			URI wsContextURI = null;
			String webContextPath = "/";
			TransportContext transportContext = TransportContextHolder.getTransportContext();
			if (transportContext != null) {
				WebServiceConnection webServiceConnection = transportContext.getConnection();
				wsContextURI =  webServiceConnection.getUri();
				
				if (wsContextURI != null) {
					String wsPath = wsContextURI.getRawPath();

					if (wsPath == null || wsPath.charAt(0) != '/')
						wsPath = "/" + wsPath;

					int p0 = wsPath.indexOf('/', 1);
					if (p0 > 0) 
						webContextPath = wsPath.substring(0,p0); 
						webContextURI = wsContextURI.resolve(webContextPath);
				}
			}
		} catch (URISyntaxException e) {
    		logger.warn("Error while contextualizing resourceLocalPath", e) ;
		}
		return webContextURI;			
    }
}
