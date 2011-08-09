package edu.isi.bmkeg.neuart.atlasbrowser.views.models
{
	import edu.isi.bmkeg.neuart.atlasservice.DataMap;
	
	import flash.events.EventDispatcher;
	
	public class DataMapsViewItem 
	{
		
		public var datamap:DataMap;
		public var parent:DataMapsView;
		
		[Bindable]
		public var color:uint;
		
		[Bindable]
		public var visible:Boolean;
		
		public function DataMapsViewItem(datamap:DataMap, color:uint=0, visible:Boolean=true) {
			this.datamap = datamap;
			this.visible = visible;
			this.color = color;
		}
	}
		
}