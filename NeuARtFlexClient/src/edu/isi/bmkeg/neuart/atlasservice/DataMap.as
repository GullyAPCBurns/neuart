package edu.isi.bmkeg.neuart.atlasservice
{
	import edu.isi.bmkeg.neuart.atlasbrowser.utils.Utils;
	import edu.isi.bmkeg.neuart.atlasbrowser.views.models.BrowserModel;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;

	[Bindable]
	[RemoteClass(alias="edu.isi.bmkeg.neuart.atlasserver.controller.DataMap")]
	public class DataMap
	{
		public function DataMap()
		{
		}

		private var _atlasURI : String;
		private var _atlas:AtlasStructure;

		public var name : String;
		public var description : String;
		public var uri : String;
		public var citation : String;
		public var digitalLibraryKey : String;
		public var dataMapPlates : ArrayCollection;

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
		
		private var _sortedPlates:ArrayCollection;
		
		public static const NAME_SORT_FIELD:String = "sagitalZOffsetFromLeft";
		
		/**
		 * return a collection of AtlasPlate sorted by the Z Offset
		 */
		public function get sortedPLates():ArrayCollection {
			if (_sortedPlates == null) {
				_sortedPlates = new ArrayCollection(dataMapPlates.source);
				var sort:Sort = new Sort();
				sort.compareFunction = compare;
				_sortedPlates.sort = sort;
				_sortedPlates.refresh();
			}
			return _sortedPlates;
		}
		
		private function compare(a:Object, b:Object, fields:Array = null):int {
			var pa:DataMapPlate = DataMapPlate(a);
			var pb:DataMapPlate = DataMapPlate(b);
			
			if (pa.atlasPlate == null) {
				if (pb.atlasPlate == null)
					return 0;
				else
					return -1;
			} else {
				if (pb.atlasPlate == null) return 1;
				else {
					var za:Number = pa.atlasPlate.sagitalZOffsetFromLeft;
					var zb:Number = pb.atlasPlate.sagitalZOffsetFromLeft;
					if (za < zb) return -1;
					else if (za > zb) return 1;
					else return 0;
				}
			}
			
		}
		
		public function getDataMapForPlate(plate:AtlasPlate):DataMapPlate {
			for each (var dm:DataMapPlate in dataMapPlates) {
				if (dm.atlasPlate == plate) {
					return dm;
				}
			}
			return null;
		}

	
	}
	
	
}