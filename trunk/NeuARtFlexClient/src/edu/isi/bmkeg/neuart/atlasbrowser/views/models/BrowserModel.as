package edu.isi.bmkeg.neuart.atlasbrowser.views.models
{	
	import flash.events.EventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	import mx.utils.URLUtil;
	
	/**
	 * This class represents the state of the AtlasBrowserFramework.
	 */
	public class BrowserModel
	{

//		public static const WSDL_PATH:String = "atlasserver.wsdl";
		
		public static const AMF_CHANNEL_PATH:String = "messagebroker/amf";
		
		//TODO move this to the AtlasService class
		public static const ATLAS_SERVICE_DEST:String = "atlasServiceImpl";

		private static var _loadedAtlases:Atlases = new Atlases();
		private static var _loadedDataMaps:DataMapsView = new DataMapsView();
		
		public static function get loadedAtlases():Atlases {
			return _loadedAtlases;
		}

		public static function get loadedDataMaps():DataMapsView {
			return _loadedDataMaps;
		}

	}
}