// $Id: SparqlOntologyInterface.as 1610 2011-02-08 16:40:33Z tom $

package edu.isi.bmkeg.kefed.ontology.sparql {
	import edu.isi.bmkeg.kefed.ontology.OntologySearchEvent;
	import edu.isi.bmkeg.kefed.ontology.OntologySearchInterface;
	import edu.isi.bmkeg.utils.UriUtil;
	import edu.isi.bmkeg.utils.sparql.SparqlUtil;
	
	import flash.xml.XMLDocument;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.managers.CursorManager;
	import mx.rpc.CallResponder;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	import mx.utils.URLUtil;
	
	/** Search interface for RDF stores that implement a SPARQL endpoint
	 *  and store OWL ontologies.
	 *
	 * @author University of Southern California
	 * @date $Date: 2011-02-08 08:40:33 -0800 (Tue, 08 Feb 2011) $
	 * @version $Revision: 1610 $
	 */	
	public class SparqlOntologyInterface extends OntologySearchInterface {
		
		private static var PREFIXES:String = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
											+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
											+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>";
		private static var GENERAL_FILTER:String = "FILTER (?type != owl:Ontology)";							
		private static var PROPERTY_FILTER:String = "FILTER (?type != owl:Property "
													+ "&& ?type != rdf:Property "
													+ "&& ?type != owl:Property "
													+ "&& ?type != owl:AnnotationProperty "
													+ "&& ?type != owl:ObjectProperty "
													+ "&& ?type != owl:DatatypeProperty "
													+ "&& ?type != owl:FunctionalProperty "
													+ "&& ?type != owl:TransitiveProperty "
													+ "&& ?type != owl:SymmetricProperty "
													+ ")";
		private static const LIST_QUERY:String = PREFIXES 
												+ " SELECT ?ontologyId ?fullName ?versionNumber ?description "
												+ " WHERE {"
												+ "   ?ontologyId rdf:type owl:Ontology . "
												+ "   OPTIONAL { ?ontologyId rdfs:comment ?description } . "
												+ "   OPTIONAL { {?ontologyId rdfs:label ?fullName} "
												+ "              UNION {?ontologyId dc:title ?fullName}} . " 
												+ "   OPTIONAL { ?ontologyId owl:versionInfo ?versionNumber } ."
												+ "} ORDER BY (?ontologyId)";
													
		/** Hash to hold the ontologies from this endpoint.
		 *  Keeps the ontology by its URI and records
		 *  the name and namespaceUri for it.
		 */					
		private var ontologyTable:Object = new Object();
		
		
		/** Flag to indicate whether to get the ontologies from the
		 *  SPARQL endpoint or to use the supplied values.  This allows
		 *  us to use this class for both cases.
		 *  TODO: Consider splitting this into two classes?
		 */
		 
		 private var getOntologiesFromService:Boolean = true;
		
		/** Endpoint for the SPARQL query interface. */
		[Bindable]
		public var endpointUrl:String = null;
		
		private var _name:String = null;
		private var _ontologyId:String = null;
		
		private var searchService:HTTPService;
		private var listOntologiesService:HTTPService;
		
		/** Constructor for a SPARQL ontology search interface.
		 *  
		 *  TODO:  Right now we don't have a way to figure out the ontology
		 *  that a particular term belongs to, so we make the temporary
		 *  and almost certainly incorrec assumption that all of the endpoint
		 *  information is from a single ontology, and so we supply that
		 *  ontologyId when we create this endpoint.
		 * 
		 * @param ontologyId Id for the ontology of this endpoint. TEMPORARY!
		 * @param endpointUrl The url for the SPARQL endpoint of the repository.
		 * @param name The name of this service.  Optional.
		 */
		public function SparqlOntologyInterface(ontologyId:String, endpointUrl:String, name:String=null) {
			
			this.endpointUrl = endpointUrl;
			this._name = name;
			if (ontologyId==null) {
				this.getOntologiesFromService = true;
				this._ontologyId = endpointUrl;				
			} else {
				this._ontologyId = ontologyId;
				this.getOntologiesFromService = false;
			}
			initSearchService();
			initOntologiesService();
			setupOntologiesList();
		}
		
		private function createSparqlService ():HTTPService {
			var useProxy:Boolean = true;
			var service:HTTPService = new HTTPService();
		 	service.useProxy = useProxy;
		 	if (useProxy) {
				service.destination= (URLUtil.getProtocol(endpointUrl) == "https") ? "DefaultHTTPS" : "DefaultHTTP";
			}
		 	service.resultFormat = "xml";
			service.contentType = "application/javascript";
			service.method = "GET";
			return service;
		}
		
		private function initSearchService():void {
			searchService = createSparqlService();
			searchService.addEventListener(ResultEvent.RESULT, searchResultEventHandler);
			searchService.addEventListener(FaultEvent.FAULT, faultEventHandler);
		}
		
		private function initOntologiesService():void {
			if (getOntologiesFromService) {
				listOntologiesService = createSparqlService();
				trace("list query = " + LIST_QUERY);
				listOntologiesService.url = endpointUrl + "?query=" + encodeURIComponent(LIST_QUERY);
				listOntologiesService.addEventListener(ResultEvent.RESULT, listOntologiesResultEventHandler);
				listOntologiesService.addEventListener(FaultEvent.FAULT, faultEventHandler);
			}
		}
		
		private function setupOntologiesList():void {
			if (getOntologiesFromService) {
				var initialService:HTTPService = createSparqlService();
				initialService.url = endpointUrl + "?query=" + encodeURIComponent(LIST_QUERY);
				initialService.addEventListener(ResultEvent.RESULT, initialOntologiesResultEventHandler);
				initialService.addEventListener(FaultEvent.FAULT, faultEventHandler);
				initialService.send();
			}
		}
		
		override public function get name():String {
			if (_name) {
				return _name;
			} else {
				return endpointUrl;
			}
		}
		
		override public function search(term:String, exact:Boolean, prop:Boolean):void {
			if (term == null) return;
			// Need to use escape characters for the URL.
			var typeFilter:String = (prop) ? GENERAL_FILTER : GENERAL_FILTER + " " + PROPERTY_FILTER;
			var filterCode:String;
			if (exact) {
				filterCode = 'FILTER ( str(?termId) = "' + term + '" '
									+ '|| str(?displayName) = "' + term + '" '
									+ '|| str(?description) = "' + term + '" '
									+ ') ';
			} else {
				filterCode = 'FILTER (regex(str(?termId), "' + term + '", "i" ) '
									+ '|| regex(str(?displayName), "' + term + '", "i" ) '
									+ '|| regex(str(?description), "' + term + '", "i" ) '
									+ ') ';
			}
			var query:String = PREFIXES
								+ " SELECT ?termId ?displayName ?description "
								+ " WHERE {"
								+ "   ?termId rdf:type ?type . "
								+ "   OPTIONAL { ?termId rdfs:comment ?description } . "
								+ "   OPTIONAL { ?termId rdfs:label ?displayName } . "
								+ filterCode + " " + typeFilter
								+ "}";
			trace("search query = " + query);
		 	searchService.url = endpointUrl + "?query=" + encodeURIComponent(query); 	 	
		 	CursorManager.setBusyCursor();
			searchService.send();
		}

		/** Takes the results from the web service query and puts them
		 *  into an array
		 *  Takes care to select any search results that are already 
		 *  present in the current Kefed object's onologyId field.
		 * 
		 *  Receives ?termId ?displayName ?description
		 *  where there can be multiple values because ?label or ?description
		 *     can have multiple values.
		 * 
		 *  Need to turn into
		 * 		ontologyId: An ID for the ontology.  This may be a universal
		 *                    id or relative to the search engine.
		 *      termId: An ID for the term in the ontology.  This may also be a 
		 *                    universal or relative id.
		 *      shortName: The short name of the term
		 *      displayName: The name of the term for display purposes
		 *      description: A description of the term (long)
		 * 
		 * @param event The result event for the web service query
		 */
		private function searchResultEventHandler(event:ResultEvent):void {
		
			var xmlDoc:XMLDocument =  new XMLDocument(XML(event.result));	
			
		 	CursorManager.removeBusyCursor();
            var searchResults:ArrayCollection = processSearchResultsXml(XML(event.result));;
  			var outputEvent:OntologySearchEvent = 
					new OntologySearchEvent(OntologySearchEvent.FIND_ONTOLOGY_TERMS, searchResults);
			dispatchEvent(outputEvent);
		}
		
		/**
		 *  Receives ?termId ?displayName ?description
		 *  where there can be multiple values because ?label or ?description
		 *     can have multiple values.
		 * 
		 *  Need to turn into
		 *      termId:  An ID for the term.  This may be a universal
		 *                    id or relative to the ontology.
		 *      shortName:  A short version of the ID.
		 *      displayName: The preferred human readable name for the term
		 *      description: A longer description or definition of the term
		 *      ontologyId: An ID for the ontology.  This may be a universal
		 *                    id or relative to the search engine.
		 *      ontologyDisplayName: Ontology name for display purposes.
		 * 
		 * @param xmlDoc The search results as an XML document
		 * @return ArrayCollection of objects with fields filled in.
		 */
		private function processSearchResultsXml(xmlDoc:XML):ArrayCollection {
			var resultsHash:Object = new Object; // Collect the items and resolve multiple values.
			var resultCollection:ArrayCollection = new ArrayCollection();
			// Process each result item, filtering for allowedOntologies.
			// - Set the ontologyId and ontologyDisplayName the first time the term is found.
			// - Keep the shortest displayName.
			// - Combine multiple description fields into a single large field.
			for each (var result:Object in SparqlUtil.getResultsAsObjects(xmlDoc)) {
				var termOntologyId:String = getTermOntologyId(result.termId);
				if (allowedOntologyList == null || allowedOntologyList.contains(termOntologyId)) {
					var match:Object = resultsHash[result.termId];
					if (match == null) {
						result.ontologyId = termOntologyId;
						result.ontologyDisplayName = getTermOntologyName(result.termId);
						resultsHash[result.termId] = result;				
					} else {
						keepShortest(match, result, "displayName");
						concatenateFields(match, result, "description");
					}
				}
			}
			for each (result in resultsHash) {
				// Set shortName to the smaller of the displayName or the Uri name.
				var uriName:String = UriUtil.getUriName(result.termId);
				if (result.displayName && result.displayName.length  < uriName.length) {
					result.shortName = result.displayName;
				} else {
					result.shortName = uriName;
				}
				resultCollection.addItem(result);
			}
			return resultCollection;
		}
		
		private function getTermOntologyId(termId:String):String {
			// Return the ontologyId for the term.
			// TODO: This is currently a hack.
			if (getOntologiesFromService) {
				var ontology:Object  = ontologyTable[UriUtil.getUriHead(termId)];
				return (ontology) ? ontology.ontologyId : null;
			} else {
				return this._ontologyId;
			}
		}
		
		private function getTermOntologyName(termId:String):String {
			// Return the ontologyName for the term.
			// TODO: This is currently a hack.
			if (getOntologiesFromService) {
				var ontology:Object  = ontologyTable[UriUtil.getUriHead(termId)];
				return (ontology) ? ontology.fullName : null;
			} else {
				return this.name;
			}
		}
		
		
		/** Keeps the shortest real string value for field.
		 *  Executed for side effect on mainItem
		 * 
		 * @param mainItem The main item accumulating values.  Modified.
		 * @param newItem The new item to use as field source.
		 * @param field The field to update
		 */
		private function keepShortest(mainItem:Object, newItem:Object, field:String):void {
			if (mainItem[field]) {
				if (newItem[field] 
				    && mainItem[field].length > newItem[field].length
				    && newItem[field].length > 0) {
				   mainItem[field] = newItem[field];
				}
				// Otherwise the main item is smallest, so do nothing.
				
			} else {
				// No value, so use the new one (which may be null).
				mainItem[field] = newItem[field];
			}
		}
		
		/** Keeps the longest real string value for field.
		 *  Executed for side effect on mainItem
		 * 
		 * @param mainItem The main item accumulating values.  Modified.
		 * @param newItem The new item to use as field source.
		 * @param field The field to update
		 */
		private function keepLongest(mainItem:Object, newItem:Object, field:String):void {
			if (mainItem[field]) {
				if (newItem[field] 
				    && mainItem[field].length < newItem[field].length) {
				   mainItem[field] = newItem[field];
				}
				// Otherwise the main item is biggest, so do nothing.
				
			} else {
				// No value, so use the new one (which may be null).
				mainItem[field] = newItem[field];
			}
		}
		
		/** Concatenates the field values.
		 *  Executed for side effect on mainItem.
		 * 
		 * @param mainItem The main item accumulating values.  Modified.
		 * @param newItem The new item to use as field source.
		 * @param field The field to update
		 */		
		 private function concatenateFields(mainItem:Object, newItem:Object, field:String):void {
			if (mainItem[field]) {
				if (newItem[field] 
				 	 && newItem[field].length > 0
				 	 && (mainItem[field] as String).indexOf(newItem[field]) >= 0) {
					// newItem has a value that isn't already present,
					// so add it to the existing value.
					mainItem[field] += " " + newItem[field];
				}
			} else {
				// No value in mainItem, use the newItem value (which may be null)
				mainItem[field] = newItem[field];
			}
		}
		
		
		override public function listOntologies():void {
			if (getOntologiesFromService) {
			 	CursorManager.setBusyCursor();
			 	trace("Sending URL",listOntologiesService.url);
				listOntologiesService.send();
			} else {
				var searchResults:ArrayCollection = new ArrayCollection();
				var ontology:Object = new Object;
				ontology.ontologyId = this._ontologyId;
				ontology.fullName = this.name;
				ontology.shortName = this.name;
				ontology.description = "";
				ontology.versionNumber = null;
				searchResults.addItem(ontology);
				var outputEvent:OntologySearchEvent = 
					new OntologySearchEvent(OntologySearchEvent.LIST_ONTOLOGIES, searchResults);
				dispatchEvent(outputEvent);
			}
		}
		
		/** Receives ?ontologyId ?fullName ?versionNumber ?description
		 *  where there can be multiple values because ?displayName or ?description
		 *     can have multiple values.
		 *  Need to turn into
		 * 		ontologyId: An ID for the ontology.  This may be a universal
		 *                    id or relative to the search engine.
		 *      fullName: The name of the ontology for display purposes
		 *      ? shortName: An abbreviated name for the ontology (hack)
		 *      description: A description of the ontology (long)
		 *      versionNumber: Ontology version number
		 * 		nameSpace: The ontology Id (hack)
		 * 
		 *  And add to the ontologyTable for this interface.
		 *  TODO: Handle multiple return values for a single ontology!
		 */
		private function initialOntologiesResultEventHandler (event:ResultEvent):void {
			var xmlDoc:XML =  XML(event.result);	
			for each (var ontologyElement:Object in SparqlUtil.getResultsAsObjects(xmlDoc)) {
				if (!ontologyElement.fullName) ontologyElement.fullName = UriUtil.getUriName(ontologyElement.ontologyId);
				ontologyElement.nameSpace = ontologyElement.ontologyId;
				ontologyTable[ontologyElement.ontologyId] = ontologyElement;
			}
		}
		
		
		/** Receives ?ontologyId ?fullName ?versionNumber ?description
		 *  where there can be multiple values because ?displayName or ?description
		 *     can have multiple values.
		 *  Need to turn into
		 * 		ontologyId: An ID for the ontology.  This may be a universal
		 *                    id or relative to the search engine.
		 *      displayName: The name of the ontology for display purposes
		 *      shortName: An abbreviated name for the ontology
		 *      description: A description of the ontology (long)
		 *      versionNumber: Ontology version number
		 * 
		 *  TODO: Handle multiple return values for a single ontology!
		 * 
		 */
		 private function listOntologiesResultEventHandler (event:ResultEvent):void {
			CursorManager.removeBusyCursor();
			var searchResults:ArrayCollection = processListOntologiesXml(XML(event.result));
			var outputEvent:OntologySearchEvent = 
					new OntologySearchEvent(OntologySearchEvent.LIST_ONTOLOGIES, searchResults);
			dispatchEvent(outputEvent);
		}
		
		private function processListOntologiesXml(xmlDoc:XML):ArrayCollection {
			var resultsHash:Object = new Object; // Collect the items and resolve multiple values.
			var resultCollection:ArrayCollection = new ArrayCollection();
			// Process each result item, filtering for allowedOntologies.
			// - Keep the longest fullName
			// - keep the longest versionNumber
			// - Combine multiple description fields into a single large field.
			for each (var result:Object in SparqlUtil.getResultsAsObjects(xmlDoc)) {
				var match:Object = resultsHash[result.ontologyId];
				if (match == null) {
					resultsHash[result.ontologyId] = result;	
				} else {
					keepLongest(match, result, "fullName");
					keepLongest(match, result, "versionNumber");
					concatenateFields(match, result, "description");
				}

			}
			
			// Update names.
			for each (result in resultsHash) {
				// Set shortName to the Uri name.
				result.shortName = UriUtil.getUriName(result.ontologyId);
				result.elementNamespace = result.ontologyId;
				if (result.fullName == null || result.fullName.length == 0) {
					result.fullName = result.shortName;
				}
				resultCollection.addItem(result);
			}
			return resultCollection;
		}
		
		private function faultEventHandler(event:FaultEvent):void {
			Alert.show(event.toString());			
			CursorManager.removeBusyCursor();
			dispatchEvent(event);		
		}
		/** Cancel pending search or listOntologies requests..
		 */
		override public function cancel():void {
			if (this.listOntologiesService) this.listOntologiesService.cancel();
			if (this.searchService) this.searchService.cancel();
			CursorManager.removeBusyCursor();
		}		
	}
}