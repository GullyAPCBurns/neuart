// $Id: NCBOBioPortalInterface.as 1594 2011-02-02 01:06:35Z tom $
//
//  $Date: 2011-02-01 17:06:35 -0800 (Tue, 01 Feb 2011) $
//  $Revision: 1594 $
//
package edu.isi.bmkeg.kefed.ontology.bioportal
{

	import edu.isi.bmkeg.kefed.elements.IKefedNamedObject;
	import edu.isi.bmkeg.kefed.ontology.OntologySearchEvent;
	import edu.isi.bmkeg.kefed.ontology.OntologySearchInterface;
	
	import flash.xml.XMLDocument;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.controls.Alert;
	import mx.managers.CursorManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	import mx.rpc.xml.SimpleXMLDecoder;
	import mx.utils.ArrayUtil;
	
	public class NCBOBioPortalInterface extends OntologySearchInterface {
		
		private static var BASE_URL:String = "http://rest.bioontology.org/bioportal/";
		private static var OBO_ONTOLOGIES:Array = ["CHEBI", "CL", "DOID", "FIX", "FMA",
            										  "GO", "IAO", "OBI", "OBO_REL", "PATO",
            										  "PRO", "REX", "UO"
													  /* , "BFO", "ECO", "HP" */
												  ];
		
												  
		/** Flag that limits the set of ontologies used from NCBO to
		 *  only be the OBO ontologies.
		 */
		[Bindable]
		public var oboOnly:Boolean = false;
	
		/** Email account for the NCBO restful web services call.
		 */
		[Bindable]
		public var emailAccount:String = "gully@usc.edu";
		
		override public function get name():String {
			return "NCBO BioPortal";
		}

		private var ncboSearchService:HTTPService;
		private var ncboListOntologiesService:HTTPService;
		
		private var searchResults:ArrayCollection = new ArrayCollection();

		
		private var term:XML = new XML();
		
		
		public function NCBOBioPortalInterface() {
						
			this.initNcboSearch();
			this.initNcboListOntologies();
				
		}
		
		private function createNcboService ():HTTPService {
			var service:HTTPService = new HTTPService();
		 	service.useProxy = true;
			service.destination="DefaultHTTP";
		 	service.resultFormat = "text";
			service.contentType = "application/javascript";
			service.method = "GET";
			return service;
		}

		/**
		 * ___________________________________________________________
		 * 
		 * Search 
		 * ___________________________________________________________
		 */

		private function initNcboSearch():void {

		 	ncboSearchService = createNcboService();
			ncboSearchService.addEventListener(ResultEvent.RESULT, ncboSearchResultEventHandler);
			ncboSearchService.addEventListener(FaultEvent.FAULT, faultEventHandler);
			
		}		


		override public function search(term:String, exact:Boolean, prop:Boolean):void {
			if (term == null) return;
			// Need to use escape characters for the URL

			var exactStr:String = (exact) ? "1" : "0";
			var propStr:String = (prop) ? "1" : "0";
		 	ncboSearchService.url = BASE_URL + "search/" 
		 			+ encodeURIComponent(term)
		 			+ "?isexactmatch=" + exactStr 
		 			+ "&includeproperties=" + propStr 
		 			+ "&email=" + emailAccount; 	 	
		 	CursorManager.setBusyCursor();
			ncboSearchService.send();
			
		}
		
		/** Takes the result as an NCBO returned term object and
		 *  creates a standard term object formatted the way our
		 *  editor likes to see it.
		 * 
		 * @param searchItem The search item object
		 * @returns Reformatted object for the KEfED editor.
		 */
		private function makeStandardTerm(searchItem:Object):Object {
			var term:Object = new Object();
			term.ontologyId = searchItem.ontologyId;
			term.ontologyDisplayName = searchItem.ontologyDisplayLabel;
			term.termId = searchItem.conceptId;
			term.shortName = searchItem.conceptIdShort;
			term.displayName = searchItem.preferredName;
			term.description = "";
			return term;
		}

		/** Takes the results from the web service query and puts them
		 *  into an array
		 *  Takes care to select any search results that are already 
		 *  present in the current Kefed object's onologyId field.
		 * 
		 * @param event The result event for the web service query
		 */
		private function ncboSearchResultEventHandler(event:ResultEvent):void {
		
			var xmlDoc:XMLDocument =  new XMLDocument(XML(event.result));	
			
		 	CursorManager.removeBusyCursor();
		 	
		 	var decoder:SimpleXMLDecoder = new SimpleXMLDecoder(true);
            var topXMLObject:Object = decoder.decodeXML(xmlDoc);
                
            var check:Object = topXMLObject.success.data;
            
            searchResults = new ArrayCollection();
            
            // Store a hash table of concept IDs so that we can
            // find those concepts that are already selected for
            // our variables.
            // NOTE:  This is currently done by just using the
            //  ontologyIdentifier field, which is not guaranteed
            //  unique across ontologies.  It should really be the
            //  combination of the ontology name and item id.
//            var lookup:Object = new Object();
//            for each (var i:Object in kefedObj.ontologyIds) {
//            	// Backward compatibility.  Be prepared for just a string.
//            	if (i is OntologyTermReference) {
//            		lookup[i.termId] = i;
//            	} else {
//            		lookup[i] = i;
//            	}
//            }
//            
            if( check != null ) {
            	var returnedTermsObj:Object = check.page.contents.searchResultList.searchBean;
	            for each (var j:Object in returnedTermsObj) {
	     			if (allowedOntologyList == null || allowedOntologyList.contains(j.ontologyId)) {
	    	        	searchResults.addItem(makeStandardTerm(j));
//	    	        	j.url = "";
//	    	        	if( lookup[j.conceptId] != null || lookup[j.conceptIdShort] != null) {
//	    	        		j.selected = true;
//	    	        	} else {
//	    	        		j.selected = false;
//	    	        	}
	    	   		}
        	    }
            }

			var outputEvent:OntologySearchEvent = 
					new OntologySearchEvent(OntologySearchEvent.FIND_ONTOLOGY_TERMS, searchResults);
			dispatchEvent(outputEvent);
				
		}

		/**
		 * http://rest.bioontology.org/bioportal/ontologies?email=example@example.org
		 * ___________________________________________________________
		 * 
		 * NCBO's list of active ontologies
		 * ___________________________________________________________
		 */

		private function initNcboListOntologies():void {

		 	ncboListOntologiesService = createNcboService();
		 	
		 	ncboListOntologiesService.url = BASE_URL + "ontologies?email=" + emailAccount; 	 	
			ncboListOntologiesService.addEventListener(ResultEvent.RESULT, 
					ncboListOntologiesEventHandler);
			ncboListOntologiesService.addEventListener(FaultEvent.FAULT, 
					faultEventHandler);
		}		

		override public function listOntologies():void {
		 	CursorManager.setBusyCursor();
			ncboListOntologiesService.send();
		}

		private function ncboListOntologiesEventHandler(event:ResultEvent):void {
		 	CursorManager.removeBusyCursor();
		
			var xmlDoc:XMLDocument =  new XMLDocument(XML(event.result));	
		 	var decoder:SimpleXMLDecoder = new SimpleXMLDecoder(true);
            var topXMLObject:Object = decoder.decodeXML(xmlDoc);
            var check:Object = topXMLObject.success.data;
            
			var searchResults:ArrayCollection = new ArrayCollection();
			
			if (allowedOntologyList == null) {
			    allowedOntologyList = new ArrayCollection();
			} else {
				allowedOntologyList.removeAll();
			}
			
            if( check != null ) {
            	var returnedTermsObj:Object = check.list.ontologyBean;
            	// searchResults = ArrayCollection(returnedTermsObj);
            	
            	// Special purpose HACK: Filter ontologies for OBO if flag is set.
            	searchResults = new ArrayCollection();
            	for each (var term:Object in returnedTermsObj) {
            		if (!oboOnly 
            			|| -1 != ArrayUtil.getItemIndex(term["abbreviation"], OBO_ONTOLOGIES)) {
            			// Create new object and set its fields here!
             			var newTerm:Object = new Object();
            			newTerm.ontologyId = term.ontologyId;
            			newTerm.displayName = term.displayLabel;
            			newTerm.shortName = term.abbreviation;
            			newTerm.description = term.description;
            			newTerm.versionNumber = term.versionNumber;
            			searchResults.addItem(newTerm);
            			allowedOntologyList.addItem(newTerm.ontologyId);
            		}
            	}
            	
            	// TODO: Move this into UI code!  It doesn't belong here.
            	var dataSortField:SortField = new SortField();
            	dataSortField.name = "displayName";
				dataSortField.numeric = false;
				dataSortField.descending = false;
				dataSortField.caseInsensitive = true;
				
            	/* Create the Sort object and add the SortField object created earlier 
            	to the array of fields to sort on. */
            	var ontologyNameSort:Sort = new Sort();
           	    ontologyNameSort.fields = [dataSortField];

	            /* Set the ArrayCollection object's sort property to our custom sort, 
	            and refresh the ArrayCollection. */
	            searchResults.sort = ontologyNameSort;
	            searchResults.refresh();
  	
            }

			var outputEvent:OntologySearchEvent = 
					new OntologySearchEvent(OntologySearchEvent.LIST_ONTOLOGIES, searchResults);
			
			dispatchEvent(outputEvent);
		}
		
		/**
		 * view-source:http://rest.bioontology.org/bioportal/ontologies?email=example@example.org
		 * ___________________________________________________________
		 * 
		 * LOAD A SPECIFIC TERM ACCORDING TO MIREOT PRINCIPLES
		 * ___________________________________________________________
		 */
		 
		 
		
		/**
		 * ___________________________________________________________
		 * 
		 * GENERIC INTERNAL FUNCTIONS
		 * ___________________________________________________________
		 */

		private function faultEventHandler(event:FaultEvent):void {
			
			Alert.show(event.toString());
			
			CursorManager.removeBusyCursor();

			dispatchEvent(event); 			
		
		}

		override public function cancel():void {
			this.ncboListOntologiesService.cancel();
			this.ncboSearchService.cancel();
			CursorManager.removeBusyCursor();

		}
	   
	}
}