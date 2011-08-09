package edu.isi.bmkeg.neuart.atlasbrowser.views
{
	import edu.isi.bmkeg.neuart.atlasservice.AtlasPlate;
	
	import flash.events.MouseEvent;
	
	import mx.controls.VRule;
	
	public class SagitalLevelIndicator extends VRule
	{
		private static const LEVEL_LINE_IDLE_COLOR:String = "Gray";
		private static const LEVEL_LINE_ROLLOVER_COLOR:String = "Blue";

		private var _atlasPlate:AtlasPlate;
		
		[Bindable]
		public function get atlasPlate():AtlasPlate {
			return _atlasPlate;
		}
		
		public function set atlasPlate(plate:AtlasPlate):void {
			_atlasPlate = plate;	
			toolTip = plate.plateName + " [" + plate.sagitalZOffsetFromLeft + " mm]";
		}
		
		public function SagitalLevelIndicator()
		{
			super();
		}
		
		protected function rollOverHandler(event:MouseEvent):void {
			setStyle("strokeColor",LEVEL_LINE_ROLLOVER_COLOR);
			setStyle("shadowColor",LEVEL_LINE_ROLLOVER_COLOR);
		}
		
		protected function rollOutHandler(event:MouseEvent):void
		{
			setStyle("strokeColor",LEVEL_LINE_IDLE_COLOR);
			setStyle("shadowColor",LEVEL_LINE_IDLE_COLOR);
		}

		override public function initialize():void {
			super.initialize();
			alpha = 0.5;
			setStyle("strkeWidth",2);
			setStyle("strokeColor",LEVEL_LINE_IDLE_COLOR);
			setStyle("shadowColor",LEVEL_LINE_IDLE_COLOR);
			setStyle("top",0);
			setStyle("bottom",0);
			addEventListener(MouseEvent.ROLL_OVER, rollOverHandler);
			addEventListener(MouseEvent.ROLL_OUT, rollOutHandler);			
		}
	}
}