using System.Xml.Serialization;
using System;
using System.Xml.Schema;
using System.Linq;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using Jamie.ParserBase;

namespace Jamie.Quests
{
	[Serializable]
	[XmlType(AnonymousType = true)]
	[XmlRoot(ElementName = "p", Namespace = "", IsNullable = false)]
	public partial class Paragraph
	{
		[XmlElement("font", Form = XmlSchemaForm.Unqualified, IsNullable = true)]
		public pFont font;

		[XmlAttribute]
		public string visible;

		[XmlText]
		public string Value;

		[XmlIgnore]
		public string Text {
			get {
				return Utility.GetParsedString(this.Value, true);
			}
		}
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public partial class pFont
	{
		[XmlAttribute]
		public string color;

		[XmlAttribute]
		public string font_xml;

		[XmlText]
		public string Value;

		[XmlElement("p")]
		public Paragraph[] p;

		[XmlIgnore]
		public string Text {
			get {
				return Utility.GetParsedString(this.Value, true);
			}
		}
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	[XmlRoot(ElementName = "HtmlPages", Namespace = "", IsNullable = false)]
	public partial class Dialogs
	{
		[XmlElement("HtmlPage", Form = XmlSchemaForm.Unqualified)]
		public List<DialogHtml> HtmlPages;

		[XmlIgnore]
		public string fileName;
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public partial class DialogHtml
	{
		[XmlElement("Contents", Form = XmlSchemaForm.Unqualified)]
		public Contents Content;

		[XmlArray(Form = XmlSchemaForm.Unqualified)]
		[XmlArrayItem("Act", typeof(SelectsAct), Form = XmlSchemaForm.Unqualified)]
		public SelectsAct[] Selects;

		[XmlElement("Voice", Form = XmlSchemaForm.Unqualified)]
		public Voice Voice;

		[XmlAttribute]
		public string name;

		[XmlIgnore]
		public bool ForceInclude { get; set; }

		[XmlIgnore]
		public string QuestTitle { get; set; }

		string _description;

		[XmlIgnore]
		public string QuestDescription {
			get {
				if (_description == null) {
					try {
						var fontSection = Content.html.body.p.Where(p => p.font != null &&
																		 p.font.font_xml == "quest_summary")
															 .Select(p => p.font)
															 .FirstOrDefault();
						_description = String.Empty;
						if (fontSection != null) {
							_description = fontSection.Text;
							if (_description == null)
								_description = String.Empty;
							else
								_description = _description.TrimEnd('\n', ' ', ':');

							if (fontSection.p != null) {
								_description += ':';
								_description = AppendParagraphs(_description, fontSection.p);
							}
							_description = _description.TrimEnd(':');
						}
						if (_description == String.Empty)
							_description = AppendParagraphs(_description, Content.html.body.p);
					} catch {
						_description = String.Empty;
					}
				}
				return _description;
			}
		}

		string AppendParagraphs(string appendTo, Paragraph[] paragraphs) {
			if (paragraphs != null) {
				foreach (Paragraph para in paragraphs) {
					appendTo += ' ';
					appendTo += para.Text.TrimEnd('\n', ' ');
				}
			}
			return appendTo;
		}
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public partial class Contents
	{
		[XmlElement(Form = XmlSchemaForm.Unqualified)]
		public ContentsHtml html;

		[XmlAttribute]
		public string cdata;
	}

	[Serializable]
	public partial class ContentsHtml
	{
		[XmlElement(Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
		public Body body;
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public partial class Body
	{
		[XmlArray(Form = XmlSchemaForm.Unqualified)]
		[XmlArrayItem("step", Form = XmlSchemaForm.Unqualified, IsNullable = false)]
		public Step[] steps;

		[XmlElement("p")]
		public Paragraph[] p;
	}

	[Serializable]
	public partial class Step
	{
		public static readonly Regex _countRegex;

		static Step() {
			_countRegex = new Regex(@".*\s+\(\[%\d+\]/(?<total>\d+)\)", RegexOptions.Compiled);
		}

		[XmlIgnore]
		public int Number;

		[XmlElementAttribute(Form = XmlSchemaForm.Unqualified)]
		public Paragraph p;

		[XmlText]
		public string Value;

		[XmlIgnore]
		public bool IsCollection {
			get { return this.Value != null && this.Value.Trim() == "[%collectitem]"; }
		}

		[XmlIgnore]
		public bool HasCount {
			get {
				if (this.p != null && this.p.font != null) {
					return _countRegex.IsMatch(p.font.Text);
				}
				return false;
			}
		}

		[XmlIgnore]
		public int Counter {
			get {
				if (HasCount) {
					foreach (Match match in _countRegex.Matches(p.font.Text)) {
						GroupCollection groups = match.Groups;
						string countValue = groups["total"].Value;
						return Int32.Parse(countValue);
					}
				}
				return 0;
			}
		}

		[XmlIgnore]
		public string Description {
			get {
				if (this.p != null && this.p.font != null) {
					return p.font.Text;
				}
				return String.Empty;
			}
		}
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public partial class SelectsAct
	{
		[XmlAttribute]
		public string href;

		[XmlText]
		public string Value;

		[XmlIgnore]
		public string ActionName {
			get {
				if (String.IsNullOrEmpty(href))
					return null;
				string[] names = href.Split(';');
				foreach (string name in names) {
					if (name.StartsWith("HACTION_", StringComparison.InvariantCultureIgnoreCase))
						return name.Remove(0, 8).ToLowerInvariant();
				}
				return null;
			}
		}

		[XmlIgnore]
		public string EmotionName {
			get {
				if (String.IsNullOrEmpty(href))
					return null;
				string[] names = href.Split(';');
				foreach (string name in names) {
					if (name.StartsWith("PLAYEMOTION_", StringComparison.InvariantCultureIgnoreCase))
						return name.Remove(0, 12).ToLowerInvariant();
				}
				return null;
			}
		}
	}

	[Serializable]
	[XmlType(AnonymousType = true)]
	public partial class Voice
	{
		[XmlAttribute]
		public string file;
	}
}
