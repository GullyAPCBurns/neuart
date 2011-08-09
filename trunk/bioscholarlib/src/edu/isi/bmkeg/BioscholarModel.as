package edu.isi.bmkeg
{
	import com.kapit.diagram.library.SVGAssetLibrary;
	
	import edu.isi.bmkeg.kefed.app.KefedAppEventDispatcher;
	import edu.isi.bmkeg.kefed.elements.KefedModel;
	import edu.isi.bmkeg.kefed.store.IDataStore;
	import edu.isi.bmkeg.kefed.store.IModelStore;
	import edu.isi.bmkeg.kefed.store.persevere.PersevereDataStore;
	import edu.isi.bmkeg.kefed.store.persevere.PersevereModelStore;
	import edu.isi.bmkeg.kefed.store.persevere.YogoModelStore;
	import edu.isi.bmkeg.kefed.ui.kapit.DiagramMappings;
	import edu.isi.bmkeg.utils.Parameters;
	
	import mx.collections.ArrayCollection;
	import mx.utils.URLUtil;

	//TODO refactor application to get global properties from this class instead of the application instance
	public class BioscholarModel
	{
		
		public static var model:BioscholarModel;
		
		[Bindable]
		public var serverUrl:String;
		
		[Bindable]
		public var modelStore:IModelStore = null;
		
		[Bindable]
		public var schemaStore:IModelStore = null;
		
		[Bindable]
		public var dataStore:IDataStore = null;
		
		public var config:Parameters;
//		public var model:KefedModel;
		public var eventDispatcher:KefedAppEventDispatcher = new KefedAppEventDispatcher;
		public var lib:SVGAssetLibrary;
		public var dataStoreUrl:String;
		public var modelStoreUrl:String;
		public var schemaStoreUrl:String;
		public var ploomWsdlUrl:String;
		
//		private var version:String = "$Revision: 1665 $";
//		private var ncbo:OntologySearchInterface = new NCBOBioPortalInterface(); 
		public var ontologyList:ArrayCollection = new ArrayCollection();
		public var selectedOntologies:Array = new Array();
		
		public static function instiantiate(appUrl:String, configuration:XML):void {
			if (model == null) {
				model = new BioscholarModel();
				model.init(appUrl, configuration);				
			}
		}
		
		private function init(appUrl:String, configuration:XML):void {
			DiagramMappings.initializeMappings();
//			UiUtil.agreeToLicense(license.key, license.license);
			if (serverUrl==null) {
				serverUrl = URLUtil.getProtocol(appUrl) + "://"
					+ URLUtil.getServerNameWithPort(appUrl) + "/";
			}
//			trace(this.name + " serverURL = " + serverUrl);
			config = new Parameters(configuration);
			
			// Setup the URLs for the various persistent storage mechanisms.
			// Since the schemaStore is optional, it gets a null default value.
			modelStoreUrl = config.getValue("modelStoreUrl", "persevere/KefedModel");
			schemaStoreUrl = config.getValue("schemaStoreUrl", null);
			dataStoreUrl = config.getValue("dataStoreUrl", "persevere/KefedModel");
			ploomWsdlUrl = config.getValue("ploomWsdlUrl", "ploom/soap-wsdl/powerloom-soap-service.wsdl");
			
			modelStoreUrl = URLUtil.getFullURL(serverUrl, modelStoreUrl);
			modelStore = new PersevereModelStore(modelStoreUrl);			
			if (schemaStoreUrl != null) {
				schemaStoreUrl = URLUtil.getFullURL(serverUrl, schemaStoreUrl);
				schemaStore = new YogoModelStore(schemaStoreUrl);
			}
			dataStoreUrl = URLUtil.getFullURL(serverUrl, dataStoreUrl);
			dataStore = new PersevereDataStore(dataStoreUrl);
			ploomWsdlUrl = URLUtil.getFullURL(serverUrl, ploomWsdlUrl);
			
			
			//  Setup up webservice for NCBO ontology lookup
//			ncbo.addEventListener(OntologySearchEvent.LIST_ONTOLOGIES, ncboListOntologiesEventHandler);
//			ncbo.addEventListener(FaultEvent.FAULT, FaultEventHandler);					
//			ncbo.listOntologies();
			
//			currentState = "Dashboard_State";
//			Application.application.setFocus();
		}

		
	}
}