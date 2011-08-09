package edu.isi.bmkeg.neuart.atlasservice
{
	import edu.isi.bmkeg.neuart.atlasbrowser.utils.Utils;
	
	import flash.events.IOErrorEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.URLLoader;
	import flash.net.URLRequest;

	[Bindable]
	[RemoteClass(alias="edu.isi.bmkeg.neuart.atlasserver.controller.AtlasPlate")]
	public class AtlasPlate
	{
		public function AtlasPlate()
		{
		}

		public var plateName : String;
		public var coronalImageURI : String;
		public var coronalThumbnailURI : String;
		public var sagitalZOffsetFromLeft : Number;
		public var parent:AtlasStructure;

		public var coronalImageLoad:String;
		
		private var _waitingForLoad:Boolean = false;
		
		
		/**
		 * Initiates the loading of the CoronalImage XML data from 
		 * CoronalImageURI.
		 * It launches the Event.COMPLETE, IOErrorEvent.IO_ERROR, or 
		 * SecurityErrorEvent.SECURITY_ERROR when it is done.
		 * If it succeeds the coronalImageXML property will be binded to 
		 * the CoronalImage XML data and the Event.COMPLETE event will be triggered.
		 */
		public function loadCoronalImageXML():void {
			if (_waitingForLoad) return;
			_waitingForLoad = true;
			var urlLoader:URLLoader = new URLLoader();
			urlLoader.load(new URLRequest(Utils.insertWebContextUrl(coronalImageURI)));
			urlLoader.addEventListener(Event.COMPLETE, onAtlasComplete);
			urlLoader.addEventListener(IOErrorEvent.IO_ERROR, onIOError);
			urlLoader.addEventListener(SecurityErrorEvent.SECURITY_ERROR, onSecurityError);
		}
		
		private function onAtlasComplete(event:Event):void {
			_waitingForLoad = false;
			var urlLoader:URLLoader = URLLoader(event.target);
			coronalImageLoad = String(urlLoader.data);
			//		coronalImageXML = new XML(coronalImageLoad);
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