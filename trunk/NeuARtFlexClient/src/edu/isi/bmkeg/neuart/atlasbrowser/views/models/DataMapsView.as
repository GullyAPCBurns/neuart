package edu.isi.bmkeg.neuart.atlasbrowser.views.models
{
	import edu.isi.bmkeg.neuart.atlasbrowser.events.DataMapEvent;
	import edu.isi.bmkeg.neuart.atlasservice.AtlasStructure;
	import edu.isi.bmkeg.neuart.atlasservice.DataMap;
	
	import flash.errors.IllegalOperationError;
	import flash.events.EventDispatcher;
	import flash.events.IEventDispatcher;
	
	import mx.collections.ArrayCollection;
	import mx.utils.ArrayUtil;
	
	public class DataMapsView extends ArrayCollection
	{
					
		public function addDataMap(datamap:DataMap):DataMapsViewItem {
			var c:uint = pickDefaultColor();
			var dmi:DataMapsViewItem = new DataMapsViewItem(datamap,c);
			addItem(dmi);
			return dmi;
		}
		
		public function removeDataMap(datamap:DataMap):void {
		
			var index:int = getDataMapIndex(datamap.uri);
			removeItemAt(index);

		}
		
		public function getDataMapIndex(datamapURI:String):int {
			for (var i:int ; i < this.length; i++) {
				var dmi:DataMapsViewItem = DataMapsViewItem(this[i]);	
				if (dmi.datamap.uri == datamapURI)
					return i;
			}
			return -1;
		}

		public function getDataMap(datamapURI:String):DataMapsViewItem {
			var i:int = getDataMapIndex(datamapURI);
			if (i != -1)
				return this[i] as DataMapsViewItem;
			return null;
		}
		
		override public function contains(item:Object):Boolean {
			return getItemIndex(item) != -1;
		}
		
		override public function getItemIndex(item:Object):int {
			if (!(item is DataMapsViewItem)) 
				return -1;
			return getDataMapIndex(DataMapsViewItem(item).datamap.uri);			
		}	
		
		override public function addItem(item:Object):void {
			validateNewItem(item);
			super.addItem(item);
			DataMapsViewItem(item).parent = this;
		}
		
		override public function addItemAt(item:Object, index:int):void {
			validateNewItem(item);
			super.addItemAt(item, index);
			DataMapsViewItem(item).parent = this;
		}
		
		private function validateNewItem(item:Object):void {
			if (! (item is DataMapsViewItem))
				throw new ArgumentError("item should be an instance of DataViewsItem");
			if (contains(item))
				throw new IllegalOperationError("DataMap Already exist " + DataMapsViewItem(item).datamap.uri);
		}

		/**
		 * Get a default layer color which has not been used by the system.
		 * 
		 * @return A default layer color.
		 */
		public function pickDefaultColor():uint {
			
			//
			//  Predefined color array from which we will pick a default color.
			//
			var defaultColors:Array = [0x0000FF, 0x00FF00, 0x00FFFF, 0xFF0000,
				0xFF00FF, 0xFFFF00];
			
			var usedColors:Array = getUsedColors();
			
			
			//
			//  If the any color from the predefined color array is not used
			//  by any layer, then return it as the default color.
			//
			for (var i:int = 0; i < defaultColors.length; i++) {
				if (ArrayUtil.getItemIndex(defaultColors[i], usedColors) == -1) {
					return defaultColors[i];
				}
			}
			
			//
			//  If all the colors from the predefined color array have been
			//  used, then randomly pick a Color.
			//
			while (true) {
				var r:int = Math.round( Math.random() * 255);
				var g:int = Math.round( Math.random() * 255);
				var b:int = Math.round( Math.random() * 255);
				
				var c:uint = r * 0x10000 + g * 0x100 + b
				if (c != 0 && 
					c != 0xFFFFFF &&
					ArrayUtil.getItemIndex(c, usedColors) == -1) {
					
					return c;
				}
			}
			return 0; // Never reaches this step but required a return statement by the compiler
		}
		
		public function getUsedColors():Array {
			
			var colors:Array = [];
			for each (var dmi:DataMapsViewItem in this) {
				colors.push(dmi.color);
			}
			return colors;
		}

	}
}
