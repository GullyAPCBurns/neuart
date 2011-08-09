package edu.isi.bmkeg.neuart.atlasbrowser.views.models
{
	import edu.isi.bmkeg.neuart.atlasbrowser.events.AtlasEvent;
	import edu.isi.bmkeg.neuart.atlasservice.AtlasStructure;
	
	import flash.errors.IllegalOperationError;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;

	/**
	 * Events: 
	 *   AtlasEvent.ADD_ATLAS
	 *   AtlasEvent.REMOVE_ATLAS
	 */
	[Event(name="addAtlas", type="edu.isi.bmkeg.neuart.atlasbrowser.events.AtlasEvent")]
	[Event(name="removeAtlas", type="edu.isi.bmkeg.neuart.atlasbrowser.events.AtlasEvent")]
	public class Atlases extends ArrayCollection
	{
		
		public static const ADD_ATLAS:String = "addAtlas";
		public static const REMOVE_ATLAS:String = "removeAtlas";

//		private var _atlases:Object;
		
		public function Atlases()
		{
//			_atlases = new Object();
		}
		

		override public function addItem(item:Object):void {
			validateNewItem(item);
			super.addItem(item);
//			var atlasURI:String = AtlasStructure(item).atlasURI;
//			var event:AtlasEvent = new AtlasEvent(ADD_ATLAS, atlasURI);
//			dispatchEvent(event);
		}
	
		override public function addItemAt(item:Object, index:int):void {
			validateNewItem(item);
			super.addItemAt(item, index);
			// TODO [Refactor] get rid off ADD_ATLAS event and use standard List Events
			var atlasURI:String = AtlasStructure(item).atlasURI;
			var event:AtlasEvent = new AtlasEvent(ADD_ATLAS, atlasURI);
			dispatchEvent(event);
		}
	
		private function validateNewItem(item:Object):void {
			if (! (item is AtlasStructure))
				throw new ArgumentError("item should be an instance of AtlasStructure");
			if (containsAtlas(AtlasStructure(item).atlasURI))
				throw new IllegalOperationError("Atlas Already exist " + AtlasStructure(item).atlasURI);
		}
		
//		public function add(atlas:AtlasStructure):void {
//			var atlasURI:String = atlas.atlasURI;
//			if (_atlases[atlasURI] != null) {
//				throw new IllegalOperationError("Atlas Already exist: " + atlasURI);
//			}
//			_atlases[atlasURI] = atlas;
//			var event:AtlasEvent = new AtlasEvent(ADD_ATLAS, atlasURI);
//			dispatchEvent(event);
//		}
		
		public function getAtlas(atlasURI:String):AtlasStructure {
			for each (var atlas:AtlasStructure in this) {
				if (atlas.atlasURI == atlasURI)
					return atlas;
			}
			return null;
		}

		public function getAtlasIndex(atlasURI:String):int {
			for  (var i:int = 0; i < this.length; i++) {
				var atlas:AtlasStructure = AtlasStructure(getItemAt(i));
				if (atlas.atlasURI == atlasURI)
					return i;
			}
			return -1;
		}

//		public function containsAtlas(atlasURI:String):Boolean {
//			return _atlases[atlasURI] != null;
//		}
//		

		public function containsAtlas(atlasURI:String):Boolean {
			return getAtlas(atlasURI) != null;
		}
		
		public function removeAtlas(atlasURI:String):void {
			var i:int = getAtlasIndex(atlasURI);
			if (i >= 0) {
				removeItemAt(i);
				var event:AtlasEvent = new AtlasEvent(REMOVE_ATLAS, atlasURI);
				dispatchEvent(event);				
			}
		}
	}

}