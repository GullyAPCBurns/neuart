package edu.isi.bmkeg.neuart.atlasbrowser.events
{
	
	import flash.events.Event;
	
	public class DataMapEvent extends Event
	{
				
		public static const DATA_MAP_CLICK:String = "dataMapClick";

		public var datamapURI:String;
		public var plateName:String;
		
		public function DataMapEvent(type:String, datamapURI:String, plateName:String = null)
		{
			super(type);
			this.datamapURI = datamapURI;
			this.plateName = plateName;
		}

		override public function clone():Event
		{
			return new DataMapEvent(type,datamapURI,plateName);
		}
	}
}