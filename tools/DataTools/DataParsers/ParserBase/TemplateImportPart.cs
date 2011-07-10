namespace Jamie.ParserBase
{
	using System;
	using System.ComponentModel;
	using System.Xml.Serialization;

	[Serializable]
	[XmlType(AnonymousType = true)]
	[XmlRoot(ElementName = "import", Namespace = "", IsNullable = false)]
	public partial class TemplateImportPart
	{
		[XmlAttribute]
		public string file;

		[XmlAttribute]
		[DefaultValue(false)]
		public bool skipRoot;

		[XmlAttribute]
		[DefaultValue(true)]
		public bool recursiveImport;

		public TemplateImportPart() {
			this.skipRoot = false;
			this.recursiveImport = true;
		}
	}
}
