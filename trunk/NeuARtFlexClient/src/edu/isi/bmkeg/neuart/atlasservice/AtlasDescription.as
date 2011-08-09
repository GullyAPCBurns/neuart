package edu.isi.bmkeg.neuart.atlasservice
{
	[Bindable]
	[RemoteClass(alias="edu.isi.bmkeg.neuart.atlasserver.controller.AtlasDescription")]
	public class AtlasDescription
	{
		public function AtlasDescription()
		{
		}

		public var atlasName:String;		
		public var atlasDescription:String;		
		public var atlasYear:int;	
		public var atlasURI:String;
	}
}