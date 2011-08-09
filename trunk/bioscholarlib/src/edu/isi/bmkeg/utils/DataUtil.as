// $Id: DataUtil.as 1594 2011-02-02 01:06:35Z tom $
package edu.isi.bmkeg.utils
{
	/**  Collection of utility routines for working with Data in
	 *   various formats such as XML.
	 * 
	 * @author University of Southern California
	 * @date $Date: 2011-02-01 17:06:35 -0800 (Tue, 01 Feb 2011) $
	 * @version $Revision: 1594 $
	 * 
	 */
	 public class DataUtil
	{
		/** Update the attribute on all tags of the given type in
		 *  this XML structure to have the new value.  The new value
		 *  is based on a mapping hash from the old to the new values.
		 *  The update occurs in place.
		 * 
		 * @param xml The XML object to update.
		 * @param elementName The XML tag element name to update, may be "*"
		 * @param attributeName The XML attribute on the element to update.
		 * @param map The hash mapping old to new values
		 */
		public static function updateXmlTagAttributes(xml:XML,
													  elementName:String,
												      attributeName:String,
												      map:Object):void {
			for each (var element:XML in xml.descendants(elementName)) {
       			var newValue:String = map[element.@[attributeName]];
        		if (newValue) {
        			// trace(elementName + ".@" + attributeName, element.attribute(attributeName), "  ==>  ", newValue);
         			element.@[attributeName]= newValue;
        		}
        	}
		}
	}
}