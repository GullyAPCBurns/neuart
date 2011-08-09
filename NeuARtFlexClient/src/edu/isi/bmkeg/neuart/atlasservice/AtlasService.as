package edu.isi.bmkeg.neuart.atlasservice
{
	import edu.isi.bmkeg.neuart.atlasbrowser.utils.Utils;
	import edu.isi.bmkeg.neuart.atlasbrowser.views.models.BrowserModel;
	
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	/**
	 * Uility methods for accessing the AtlasService through Remoting
	 */
	public class AtlasService
	{
		
		private var _atlasServiceRO:RemoteObject;

		public function get remoteObject():RemoteObject {
			return _atlasServiceRO;
		}
		
		public function AtlasService()
		{
			_atlasServiceRO = new RemoteObject(); 
			_atlasServiceRO.destination = 	BrowserModel.ATLAS_SERVICE_DEST; 
			_atlasServiceRO.endpoint = Utils.getRemotingEndpoint();
		}
		
		/**
		 * Adds an event listener to the underlined RemoteObject instance.
		 * parameters: type and listener should correspond with the events supported by RemoteObject
		 */
		public function addEventListener(type:String, listener:Function):void {
			_atlasServiceRO.addEventListener(type, listener);
		} 
			
		/**
		 * Sends a request to the getDataMap operation.
		 * 
		 * Before calling this method the getaDataMapEventHandler has to be set
		 * by calling the addGetDataMapEventHandler method
		 */
		public function getDataMap(dataMapUri:String):void {
			_atlasServiceRO.getDataMap(dataMapUri);
		}
		
		/** 
		 * Adds a RemoteObject event listener for the getDataMap operation
		 */
		public function addGetDataMapEventHandler(type:String, listener:Function):void {
			_atlasServiceRO.getDataMap.addEventListener(ResultEvent.RESULT, listener); 
		}

		/**
		 * Sends a request to the listAvailableDataMaps operation.
		 * 
		 * Before calling this method the listAvailableDataMapsEventHandler has to be set
		 * by calling the addListAvailableDataMapsEventHandler method
		 */
		public function listAvailableDataMaps(atlasURI:String):void {
			_atlasServiceRO.listAvailableDataMaps(atlasURI);
		}
		
		/** 
		 * Adds a RemoteObject event listener for the listAvailableDataMaps operation
		 */
		public function addListAvailableDataMapsEventHandler(type:String, listener:Function):void {
			_atlasServiceRO.listAvailableDataMaps.addEventListener(ResultEvent.RESULT, listener); 
		}
		
		/**
		 * Sends a request to the getDataMap operation.
		 * 
		 * Before calling this method the getaDataMapEventHandler has to be set
		 * by calling the addGetDataMapEventHandler method
		 */
		public function getBrainRegion(abbrev:String, atlasUri:String):void {
			_atlasServiceRO.getBrainRegion(abbrev, atlasUri);
		}
		
		/** 
		 * Adds a RemoteObject event listener for the getBrainRegion operation
		 */
		public function addGetBrainRegionEventHandler(type:String, listener:Function):void {
			_atlasServiceRO.getBrainRegion.addEventListener(ResultEvent.RESULT, listener); 
		}
	}
}