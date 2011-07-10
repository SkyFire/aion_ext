using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;
using System.Diagnostics;
using System.Collections;
using System.Configuration;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;

namespace QuestScriptsUpdater
{
	using Scripts = QuestScripts;

	static class Program
	{
		static Random rnd = new Random((int)DateTime.Now.ToBinary());
		const int COUNT = 1000000;
		static string rootPath = AppDomain.CurrentDomain.SetupInformation.ApplicationBase;

		[STAThread]
		static void Main() {
			Console.WriteLine("Loading strings...");
			Utility.LoadStrings(rootPath);
			Console.WriteLine("Loading quests...");
			Utility.LoadQData(rootPath);

			string scriptPath = Path.Combine(rootPath, ConfigurationManager.AppSettings["scriptPath"]);
			string handlerPath = Path.Combine(rootPath, ConfigurationManager.AppSettings["handlerPath"]);

			if (!Directory.Exists(scriptPath) || !Directory.Exists(handlerPath)) {
				Console.WriteLine("Missing data folders !!!");
				return;
			}

			Console.WriteLine();

			Dictionary<string, Scripts> scriptsByLocation = new Dictionary<string, Scripts>();
			Dictionary<string, List<int>> implIdsByLocation = new Dictionary<string, List<int>>();

			bool hadErrors = false;

			string[] scriptFiles = Directory.GetFiles(scriptPath, "*.xml");
			foreach (string scFile in scriptFiles) {
				string name = Path.GetFileNameWithoutExtension(scFile).ToLower();
				var scriptData = Utility.GetScripts(scFile);
				List<int> qIds = scriptData.scripts.Select(q => q.id).ToList();
				List<int> qIdsUnique = qIds.Distinct().ToList();
				if (qIds.Count != qIdsUnique.Count) {
					foreach (int id in qIdsUnique) {
						if (qIdsUnique.Count(i => i == id) > 1) {
							hadErrors = true;
							Console.WriteLine("Duplicate id: {0}; location: {1}", id, name);
						}
					}
				}
				var splitted = new Dictionary<string, Scripts>();
				foreach (int id in qIdsUnique) {
					var qu = Utility.QData.QList.Where(q => q.id == id).First();
					if (qu.category2 == null && qu.category1 == "task")
						name = "work_order";
					else
						name = Utility.StringIndex.GetStringDescription(qu.category2).body.ToLower();
					if (!implIdsByLocation.ContainsKey(name))
						implIdsByLocation.Add(name, new List<int>());
					implIdsByLocation[name].Add(id);
					if (!splitted.ContainsKey(name)) {
						splitted.Add(name, new Scripts() { imports = scriptData.imports });
						splitted[name].SetDefaultComments(scriptData.GetDefaultComments());
					}
                    var matchedScripts = scriptData.scripts.Where(s => s.id == id);
                    splitted[name].scripts.AddRange(matchedScripts);
				}

				foreach (var pair in splitted) {
					if (scriptsByLocation.ContainsKey(pair.Key)) {
						scriptsByLocation[pair.Key].scripts.AddRange(pair.Value.scripts);
					} else {
						scriptsByLocation.Add(pair.Key, pair.Value);
					}
				}
			}

			string[] handlerFolders = Directory.GetDirectories(handlerPath);
			Regex regex = new Regex(@"int\s+questId\s*=\s*(?<name>\d+)\s*;", RegexOptions.IgnoreCase);
			foreach (string path in handlerFolders) {
				DirectoryInfo dirInfo = new DirectoryInfo(path);

				string[] handlerFiles = Directory.GetFiles(path, "*.java");
				foreach (string handlerFile in handlerFiles) {
					string handlerName = Path.GetFileNameWithoutExtension(handlerFile).ToLower();
					handlerName = handlerName.TrimStart('_');
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < handlerName.Length; i++) {
						if (!Char.IsDigit(handlerName[i]))
							break;
						sb.Append(handlerName[i]);
					}
					int nameId = 0;
					if (sb.Length != 0) {
						nameId = Int32.Parse(sb.ToString());
					}

                    string name = String.Empty;
                    var qu = Utility.QData.QList.Where(q => q.id == nameId).First();
                    if (qu.category2 == null && qu.category1 == "task")
                        name = "work_order";
                    else
                        name = Utility.StringIndex.GetStringDescription(qu.category2).body.ToLower();

					string content;
					using (var fs = new FileStream(handlerFile, FileMode.Open, FileAccess.Read))
					using (var sr = new StreamReader(fs)) {
						content = sr.ReadToEnd();
					}
					Match match = regex.Match(content);
					if (match.Success && match.Groups.Count > 1) {
						string qIdStr = match.Groups[1].Value;
						int qIdInternal = Int32.Parse(qIdStr);
						if (nameId != qIdInternal) {
							hadErrors = true;
							Console.WriteLine("ID mismatch: file name Id={0}; register Id={1}; location: {2}",
											  nameId, qIdInternal, name);
						}
						nameId = qIdInternal;
					}

					if (implIdsByLocation.ContainsKey(name)) {
						if (implIdsByLocation[name].Contains(nameId)) {
							hadErrors = true;
							Console.WriteLine("Duplicate id: {0}; location: {1}; file: {2}", nameId, name,
								Path.GetFileNameWithoutExtension(handlerFile));
						} else
							implIdsByLocation[name].Add(nameId);
					} else { // search in QData
						var matchedQ = Utility.QData.QList.Where(q => q.id == nameId).FirstOrDefault();
						if (matchedQ == null) {
							hadErrors = true;
							Console.WriteLine("Id: {0} doesn't exist; location: {1}", nameId, name);
						} else {
							name = Utility.StringIndex.GetStringDescription(matchedQ.category2).body.ToLower();
							if (implIdsByLocation.ContainsKey(name)) {
								if (implIdsByLocation[name].Contains(nameId)) {
									hadErrors = true;
									Console.WriteLine("Duplicate id: {0}; location: {1}; file: {2}",
										nameId, name, Path.GetFileNameWithoutExtension(handlerFile));
								} else
									implIdsByLocation[name].Add(nameId);
							} else {
								implIdsByLocation.Add(name, new List<int>());
								implIdsByLocation[name].Add(nameId);
							}
						}
					}
				}
			}

			if (hadErrors) {
				Console.WriteLine();
				Console.Write("There were errors. Continue with the update? (Y/N)");
				var keyInfo = Console.ReadKey();
				Console.WriteLine();
				if (Char.ToLower(keyInfo.KeyChar) != 'y') {
					Environment.Exit(0);
					return;
				}
			}

			Console.WriteLine("Updating scripts...");
			Utility.UpdateScripts(scriptsByLocation, implIdsByLocation);
			Console.WriteLine("Press any key to exit...");
			Console.Read();
		}
	}
}
