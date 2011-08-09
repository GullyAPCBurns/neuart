package edu.isi.bmkeg.neuart.atlasbrowser.events
{
	import flash.events.Event;
	
	public class AtlasEvent extends Event
	{
				
		public static const ATLAS_PLATE_CLICK:String = "atlasPlateClick";

		public var atlasURI:String;
		public var plateName:String;
		
		public function AtlasEvent(type:String, atlasURI:String, plateName:String = null)
		{
			super(type);
			this.atlasURI = atlasURI;
			this.plateName = plateName;
		}

		override public function clone():Event
		{
			return new AtlasEvent(type,atlasURI,plateName);
		}
	}
}