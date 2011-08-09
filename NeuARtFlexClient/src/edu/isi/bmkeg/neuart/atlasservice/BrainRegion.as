package edu.isi.bmkeg.neuart.atlasservice
{
	import edu.isi.bmkeg.neuart.atlasbrowser.utils.Utils;
	import edu.isi.bmkeg.neuart.atlasbrowser.views.models.BrowserModel;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;

	[Bindable]
	[RemoteClass(alias="edu.isi.bmkeg.neuart.atlasserver.controller.BrainRegion")]
	public class BrainRegion
	{
		public function BrainRegion()
		{
		}

		private var _atlasURI : String;
		private var _atlas:AtlasStructure;

		public var abbreviation : String;
		public var description : String;
		public var atlasMinLevel : int;
		public var atlasMaxLevel : int;

		public function get atlasURI():String {
			return _atlasURI;
		}
		
		public function set atlasURI(aUri:String):void {
			_atlasURI = aUri;
			_atlas = null;
		}
		
		public function get atlas():AtlasStructure {
			if (_atlas == null) {
				if (_atlasURI != null &&
					BrowserModel.loadedAtlases) 
				_atlas = BrowserModel.loadedAtlases.getAtlas(_atlasURI);
			}
			return _atlas;
		}
			
	}
	
	
}