package edu.isi.bmkeg.neuart.atlasservice
{
	import edu.isi.bmkeg.neuart.atlasbrowser.utils.Utils;
	
	import flash.events.IOErrorEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.URLLoader;
	import flash.net.URLRequest;

	[Bindable]
	[RemoteClass(alias="edu.isi.bmkeg.neuart.atlasserver.controller.DataMapPlate")]
	public class DataMapPlate
	{
		public function DataMapPlate()
		{
		}

		public var coronalLayerImageURI : String;
		public var parent:DataMap;

		private var _atlasPlateName : String;		
		private var _atlasPlate:AtlasPlate;
		
		public var coronalLayerImageLoad:String;
		
		private var _waitingForLoad:Boolean = false;
		
		public function get atlasPlateName():String {
			return _atlasPlateName;
		}
		
		public function set atlasPlateName(apName:String):void {
			_atlasPlateName = apName;
			_atlasPlate = null;
		}

		public function get atlasPlate():AtlasPlate {
			if (_atlasPlate == null) {
				if (_atlasPlateName != null &&
						parent != null && 
						parent.atlas != null)
					_atlasPlate = parent.atlas.plateNamed(_atlasPlateName);
			} 
			return _atlasPlate;
		}
		
		/**
		 * Initiates the loading of the CoronalLayerImage XML data from 
		 * CoronalLayerImageURI.
		 * It launches the Event.COMPLETE, IOErrorEvent.IO_ERROR, or 
		 * SecurityErrorEvent.SECURITY_ERROR when it is done.
		 * If it succeeds the coronalLayerImageXML property will be binded to 
		 * the CoronalLayerImage XML data and the Event.COMPLETE event will be triggered.
		 */
		public function loadCoronalLayerImageXML():void {
			if (_waitingForLoad) return;
			_waitingForLoad = true;
			var urlLoader:URLLoader = new URLLoader();
			urlLoader.load(new URLRequest(Utils.insertWebContextUrl(coronalLayerImageURI)));
			urlLoader.addEventListener(Event.COMPLETE, onLoadLayerComplete);
			urlLoader.addEventListener(IOErrorEvent.IO_ERROR, onIOError);
			urlLoader.addEventListener(SecurityErrorEvent.SECURITY_ERROR, onSecurityError);
		}
		
		/**
		 * Returns the level number (extracted from the atlasPlateName)
		 * For example, from the atlasPlateName "level 1" it returns the number 1
		 */
		public function getAtlasPlateLevel():int {
			if (atlasPlateName) {
				if (atlasPlateName.substr(0,6) == "level ") {
					return parseInt(atlasPlateName.substr(6));
				}
			}
			return -1;
		}
		
		private function onLoadLayerComplete(event:Event):void {
			_waitingForLoad = false;
			var urlLoader:URLLoader = URLLoader(event.target);
			coronalLayerImageLoad = String(urlLoader.data);
			//		coronalLayerImageXML = new XML(coronalLayerImageLoad);
			dispatchEvent(event);
		}
		
		private function onIOError(event:IOErrorEvent):void {
			_waitingForLoad = false;
			dispatchEvent(event);
		}
		
		private function onSecurityError(event:SecurityErrorEvent):void {
			_waitingForLoad = false;
			dispatchEvent(event);
		}
		

	}
}