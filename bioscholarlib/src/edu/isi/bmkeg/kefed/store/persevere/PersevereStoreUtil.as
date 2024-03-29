// $Id: PersevereStoreUtil.as 2104 2011-04-06 21:02:06Z marcelo $

package edu.isi.bmkeg.kefed.store.persevere {
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	/**  Collection of static utility routines that are used by the different
	 *   Persevere-based store implementations.
	 * 
	 * @author University of Southern California
	 * @date $Date: 2011-04-06 14:02:06 -0700 (Wed, 06 Apr 2011) $
	 * @version $Revision: 2104 $
	 * 
	 */	
	public class PersevereStoreUtil {
		/**  Initializes an HTTPService of the particular type and adds the
		 *   event handlers.  Sets the proper fields in the service for
		 *   operation via a proxy.
		 * 
		 * @param method
		 * @param resultHandler
		 * @param faultHandler
		 * @return The HTTP Service for the given type.
		 * 
		 */		
		internal static function initService(method:String, resultHandler:Function, faultHandler:Function):HTTPService {
			var persvr:HTTPService = new HTTPService();
			
//			persvr.useProxy = true;
			persvr.resultFormat = "text";
			persvr.contentType = "application/javascript";
			persvr.method = method;
			persvr.addEventListener(ResultEvent.RESULT, resultHandler);
			persvr.addEventListener(FaultEvent.FAULT, faultHandler);
			persvr.showBusyCursor = true;
			return persvr;
		}
	}
}