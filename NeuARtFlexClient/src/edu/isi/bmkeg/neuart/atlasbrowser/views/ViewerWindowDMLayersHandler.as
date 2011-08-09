package edu.isi.bmkeg.neuart.atlasbrowser.views
{
	import edu.isi.bmkeg.neuart.atlasbrowser.views.models.BrowserModel;
	import edu.isi.bmkeg.neuart.atlasbrowser.views.models.DataMapsViewItem;
	import edu.isi.bmkeg.neuart.atlasservice.DataMapPlate;
	
	import flash.errors.IllegalOperationError;
	
	import mx.collections.ArrayCollection;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	
	public class ViewerWindowDMLayersHandler
	{
		// collection of ViewerWindowDMLayer corresponding to the associated ViewerWindow
		
		public var DMLayers:ArrayCollection;	 
		
		public var viewerWindow:ViewerWindow;
		
		public function ViewerWindowDMLayersHandler(vw:ViewerWindow)
		{
			viewerWindow = vw;
		}
		
		public function init():void {
			DMLayers = new ArrayCollection();
			for each (var dmi:DataMapsViewItem in BrowserModel.loadedDataMaps) {
				
				addDataMap(dmi);
			}
			BrowserModel.loadedDataMaps.addEventListener(CollectionEvent.COLLECTION_CHANGE,
				loadedDataMaps_changeHandler, false, 0, true);
		}
		
		private function addDataMap(dmi:DataMapsViewItem):void {

			
			// Look for DM levels corresponding to this ViwerWindow
			
			for each (var dmp:DataMapPlate in dmi.datamap.dataMapPlates) {
				
				if (dmp.atlasPlate == viewerWindow.atlasPlate) {
					addLayer(dmp);
					break;
				}
			}

		}

		private function removeDataMap(dmi:DataMapsViewItem):void {
			var index:int = findLayerIndex(dmi);
			if (index > -1) {
				var layer:ViewerWindowDMLayer = DMLayers[index];
				layer.detachLayer();
				DMLayers.removeItemAt(index);
			}
		}
		
		private function findLayerIndex(dmi:DataMapsViewItem):int {
			for (var i:int = 0; i < DMLayers.length; i++) {
				var layer:ViewerWindowDMLayer = DMLayers[i] as ViewerWindowDMLayer;
				if (layer && layer.dataMapsViewItem == dmi) {
					return i;
				}
			}	
			return -1;
		}
		
		private function addLayer(dmp:DataMapPlate):void {
			var layer:ViewerWindowDMLayer = new ViewerWindowDMLayer(dmp, viewerWindow);
			
			DMLayers.addItem(layer);
			
		}
		
		private function loadedDataMaps_changeHandler(event:CollectionEvent):void {
			var dmi:DataMapsViewItem;
			if (event.kind == CollectionEventKind.ADD) {
				if (event.items.length > 1)
					throw new IllegalOperationError("Only expecting single values additions");
				dmi = event.items[0] as DataMapsViewItem;
				if (dmi) {
					addDataMap(dmi);
				}
			} else if (event.kind == CollectionEventKind.REMOVE) {
				if (event.items.length > 1)
					throw new IllegalOperationError("Only expecting single values deletions");
				dmi = event.items[0] as DataMapsViewItem;
				if (dmi) {
					removeDataMap(dmi);
				}
			}

		}
	}
}