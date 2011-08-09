package edu.isi.bmkeg.neuart.atlasbrowser.views
{
	import edu.isi.bmkeg.neuart.atlasbrowser.svg.SVGUtils;
	import edu.isi.bmkeg.neuart.atlasbrowser.views.models.BrowserModel;
	import edu.isi.bmkeg.neuart.atlasbrowser.views.models.DataMapsViewItem;
	import edu.isi.bmkeg.neuart.atlasservice.DataMapPlate;
	
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.SecurityErrorEvent;
	
	import mx.binding.utils.BindingUtils;
	
	import org.svgweb.core.SVGNode;
	import org.svgweb.core.SVGViewer;
	import org.svgweb.nodes.SVGGroupNode;
	
	public class ViewerWindowDMLayer
	{
		
		public var dataMapsViewItem:DataMapsViewItem;
		public var svgDMLayer:SVGGroupNode;
		public var viewerWindow:ViewerWindow;
		
		private var _svgAtlasLayer:SVGGroupNode;
		
		public function ViewerWindowDMLayer(dmp:DataMapPlate, vw:ViewerWindow):void {
			
			viewerWindow = vw;
			dataMapsViewItem = BrowserModel.loadedDataMaps.getDataMap(dmp.parent.uri);
			loadDataMapLayerImageXML();					
			
		}
		
		protected function loadDataMapLayerImageXML():void {
			
			var dmp:DataMapPlate = dataMapsViewItem.datamap.getDataMapForPlate(viewerWindow.atlasPlate);
			
			if (dmp) {
				dmp.addEventListener(Event.COMPLETE, onLoadComplete);
				dmp.addEventListener(IOErrorEvent.IO_ERROR, onIOError);
				dmp.addEventListener(SecurityErrorEvent.SECURITY_ERROR, onSecurityError);
				
				dmp.loadCoronalLayerImageXML();				
				
			} else {
				trace("Unable to fin data map plate corresponding to: " + viewerWindow.atlasPlate.plateName);
			}
		}

		protected function onLoadComplete(event:Event):void {
			var dmp:DataMapPlate = dataMapsViewItem.datamap.getDataMapForPlate(viewerWindow.atlasPlate);

			if (dmp) {
				dmp.removeEventListener(Event.COMPLETE, onLoadComplete);
				dmp.removeEventListener(IOErrorEvent.IO_ERROR, onIOError);
				dmp.removeEventListener(SecurityErrorEvent.SECURITY_ERROR, onSecurityError);
				
				processSVG();
				
				
			} else {
				trace("Unable to fin data map plate corresponding to: " + viewerWindow.atlasPlate.plateName);
			}
				
		}
		
		private function processSVG():void {
			var dmp:DataMapPlate = dataMapsViewItem.datamap.getDataMapForPlate(viewerWindow.atlasPlate);
			
			svgDMLayer = createSvgLayer(new XML(dmp.coronalLayerImageLoad));
			
			if (!svgDMLayer) {
				trace("Failed to find group name at DM Layer: " + dataMapsViewItem.datamap.name + 
					" -- " + viewerWindow.atlasPlate.plateName);
			} else {
				attachLayer();
			}

		}
		
		/**
		 * Attaches the DataMap SVG Group Node svgDMLayer bellow 
		 * the Atlas Plate SVG group svgAtlasLayer
		 */
		private function attachLayer():void {
			var atlasNode:SVGGroupNode = getSvgAtlasLayer();
			if (atlasNode) {
				SVGUtils.setColor(svgDMLayer,dataMapsViewItem.color);
				SVGUtils.setVisibility(svgDMLayer,dataMapsViewItem.visible);
				atlasNode.addSVGChild(svgDMLayer);

				// Binds dataMapsViewItem.color property to the color of the svgDMLayer.
				
				BindingUtils.bindSetter(updateColor, dataMapsViewItem, "color", false);
				
				// Binds dataMapsViewItem.visible property to the visibility of the svgDMLayer.
				
				BindingUtils.bindSetter(updateVisible, dataMapsViewItem, "visible", false);
				
			}
		}

		/**
		 * Detaches the DataMap SVG Group Node svgDMLayer from 
		 * the Atlas Plate SVG group svgAtlasLayer
		 */
		public function detachLayer():void {
			var atlasNode:SVGGroupNode = getSvgAtlasLayer();
			if (atlasNode) {
				atlasNode.removeSVGChild(svgDMLayer);

			}
		}
		
		private function getSvgAtlasLayer():SVGGroupNode {
			if (! _svgAtlasLayer) {
				var atlasViewer:SVGViewer = viewerWindow.svgViewer;
				if (atlasViewer) {
					_svgAtlasLayer = SVGUtils.findgroup( atlasViewer.svgRoot, "atlas");
				}
			}	
			return _svgAtlasLayer;
		}
		
		private function updateColor(color:uint):void {
			if (svgDMLayer)
				SVGUtils.setColor(svgDMLayer, color);
		}
		
		private function updateVisible(value:Boolean):void {
			if (svgDMLayer)
				SVGUtils.setVisibility(svgDMLayer, value);
		}
		
//		private function createSvgLayer(xmlData:XML):SVGGroupNode {
//			var svgViewer:SVGViewer = new SVGViewer();
//			svgViewer.xml = xmlData;
//			return SVGUtils.findgroup(svgViewer.svgRoot, dataMapsViewItem.datamap.name);
//		}

		private function createSvgLayer(xmlData:XML):SVGGroupNode {
			var svgViewer:SVGViewer = new SVGViewer();
			svgViewer.xml = xmlData;
			return SVGUtils.findTopGroup(svgViewer.svgRoot);
		}

		protected function onIOError(event:IOErrorEvent):void {
			trace("IOError: " + event.text);
		}
		
		protected function onSecurityError(event:SecurityErrorEvent):void {
			trace("SecurityError: " + event.text);
		}
		
	}
}