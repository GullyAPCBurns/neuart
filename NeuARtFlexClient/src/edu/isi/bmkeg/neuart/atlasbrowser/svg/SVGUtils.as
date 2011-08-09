package edu.isi.bmkeg.neuart.atlasbrowser.svg
{
	import mx.controls.Alert;
	
	import org.svgweb.core.SVGNode;
	import org.svgweb.nodes.SVGGroupNode;

	public class SVGUtils
	{
		public function SVGUtils()
		{
			
		}
		
		public static function setColor(node:SVGNode, color:uint):void {
			
			var scolor:String = color.toString();
			
			if (node.getStyle("stroke",null,false)) {
				node.setStyle("stroke",scolor);
			} 
			if (node.getAttribute("stroke",null,false,false,false)) {
				node.setAttribute("stroke",scolor);
			}
			var fillcolor:String = node.getStyle("fill",null,false);
			if (fillcolor && fillcolor != "none") {
				node.setStyle("fill",scolor);
			}
			fillcolor = node.getAttribute("fill",null,false,false,false);
			if (fillcolor && fillcolor != "none") {
				node.setAttribute("fill",scolor);
			}
			for each (var child:SVGNode in node.svgChildren) {
				setColor(child, color);
			}
		}

		public static function findgroup(node:SVGNode, searchId:String):SVGGroupNode {
			node.forceParse();
			if (node is SVGGroupNode) {
				var idValue:String = node.getAttribute("id");
				if (idValue == searchId) {
					return SVGGroupNode(node);
				}
			}
			for each (var child:SVGNode in node.svgChildren) {
				var childgroup:SVGGroupNode = findgroup(child, searchId);
				if (childgroup)
					return childgroup;
			}
			return null;	
		}

		public static function findTopGroup(node:SVGNode):SVGGroupNode {
			node.forceParse();
			if (node is SVGGroupNode) {
				return SVGGroupNode(node);
			}
			for each (var child:SVGNode in node.svgChildren) {
				var childgroup:SVGGroupNode = findTopGroup(child);
				if (childgroup)
					return childgroup;
			}
			return null;	
		}

		public static function setVisibility(node:SVGNode, visibility:Boolean):void {
			if (visibility)
				node.setAttribute("visibility","visible");
			else
				node.setAttribute("visibility","hidden");
		}		
	}
}