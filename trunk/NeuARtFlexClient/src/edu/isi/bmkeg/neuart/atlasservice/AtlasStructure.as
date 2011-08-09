package edu.isi.bmkeg.neuart.atlasservice
{
	import edu.isi.bmkeg.neuart.atlasbrowser.utils.Utils;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;

	[Bindable]
	[RemoteClass(alias="edu.isi.bmkeg.neuart.atlasserver.controller.AtlasStructure")]
	public class AtlasStructure
	{
		public function AtlasStructure()
		{
		}

		public var atlasName : String;
		public var atlasDescription : String;
		public var atlasURI : String;
		public var atlasYear : int;
		public var sagitalZLength : Number;
		public var distanceFromLeftToBregma : Number;
		public var sagitalImageURI : String;
		public var atlasPlates : ArrayCollection;
	
		private var _sortedPlates:ArrayCollection;
		
		public static const NAME_SORT_FIELD:String = "sagitalZOffsetFromLeft";
				
		/**
		 * return a collection of AtlasPlate sorted by the Z Offset
		 */
		public function get sortedPLates():ArrayCollection {
			if (_sortedPlates == null) {
				_sortedPlates = new ArrayCollection(atlasPlates.source);
				var sort:Sort = new Sort();
				sort.fields = [new SortField(NAME_SORT_FIELD)];
				_sortedPlates.sort = sort;
				_sortedPlates.refresh();
			}
			return _sortedPlates;
		}
		
		public function plateNamed(plateName:String):AtlasPlate {
			for each (var plate:AtlasPlate in atlasPlates) {
				if (plate.plateName == plateName) {
					return plate;
				}
			}
			return null;
		}

	
	}
	
}