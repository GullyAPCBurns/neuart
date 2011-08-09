// $Id: DataEntryComboFactory.as 1524 2011-01-04 01:05:27Z tom $
//
//  $Date: 2011-01-03 17:05:27 -0800 (Mon, 03 Jan 2011) $
//  $Revision: 1524 $
//
package edu.isi.bmkeg.kefed.ui
{
	import edu.isi.bmkeg.kefed.elements.KefedBaseValueTemplate;
	
	import mx.controls.ComboBox;
	import mx.controls.Label;
	import mx.core.ClassFactory;

	public class DataEntryComboFactory extends ClassFactory	{
	
		private var _template:KefedBaseValueTemplate;
				
		public function DataEntryComboFactory(vTemplate:KefedBaseValueTemplate) {
			super(DataGridComboBox);
			this._template = vTemplate;
		}
		
		override public function newInstance():* {
			var cb:ComboBox = super.newInstance();
			var tempData:Array = new Array();
			// Is this copying really needed?
			for each (var v:String in _template.allowedValues) { 
				tempData.push(v);
			}
			cb.height = 20;
			cb.editable = _template.allowFreeValueInput;
			cb.dataProvider = tempData;
			cb.itemRenderer = new ClassFactory(Label);
			return cb;
		}
		
	}

}