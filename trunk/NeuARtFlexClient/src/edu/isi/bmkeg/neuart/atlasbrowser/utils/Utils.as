package edu.isi.bmkeg.neuart.atlasbrowser.utils
	
{
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	import mx.core.FlexGlobals;
	import mx.utils.URLUtil;
	
//	import spark.components.Application;

	public class Utils
	{

		/**
		 * Returns the url from which this application was loaded.
		 * 
		 */
		public static function getAppUrl():String {
			return Application(FlexGlobals.topLevelApplication).url;
		}
		
		/**
		 *  Returns the WebApplication context (i.e., the first segment of the url path)
		 *  which is extracted from the Application's Url
		 */ 
		public static function getWebAppContext():String {
			var url:String = getAppUrl();
			var prot:String = URLUtil.getProtocol(url);
			var server:String = URLUtil.getServerNameWithPort(url);
			var protAndServer:String = prot + "://"+server + "/";
			if (url.substr(0,protAndServer.length) != protAndServer)
				return null;
			var path:String = url.substr(protAndServer.length);
			var p:int = path.indexOf("/");
			if (p <0)
				return null;
			return path.substr(0,p);			
		} 
		
		public static function getRemotingEndpoint():String {
			return '/' + Utils.getWebAppContext() + '/' + Constants.AMF_CHANNEL_PATH
		}

		public static function insertWebContextUrl(relUrl:String):String {
			return '/' + getWebAppContext() + '/' + relUrl;
		}

		public static function sameArray(ar1:Array, ar2:Array):Boolean {
			if (ar1.length != ar2.length) return false;
			for (var i:int; i < ar1.length; i++) {
				if (ar1[i] != ar2[i]) return false;
			}	
			return true;
		}
		

	}
}