package edu.isi.bmkeg.neuart.atlasservice
{
	[Bindable]
	[RemoteClass(alias="edu.isi.bmkeg.neuart.atlasserver.controller.DataMapDescription")]
	public class DataMapDescription
	{
		public function DataMapDescription()
		{
		}

		public var name : String;
		public var uri : String;
		public var description : String;
		public var citation : String;
		public var digitalLibraryKey : String;
		public var atlasURI : String;
	}
}