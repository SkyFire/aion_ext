using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Threading;
using System.IO;
using System.Xml;
using System.Xml.Serialization;
using System.Diagnostics;
using JustAgile.Html.Linq;
using System.Runtime.InteropServices;
using System.Globalization;
using System.Runtime.Serialization.Formatters.Binary;

namespace AionQuests
{
    public partial class QuestsForm : Form
    {
        public enum MessageBeepType
        {
            Default = -1,
            Ok = 0x00000000,
            Error = 0x00000010,
            Question = 0x00000020,
            Warning = 0x00000030,
            Information = 0x00000040,
        }

        [DllImport("user32.dll", SetLastError = true)]
        public static extern bool MessageBeep(
            MessageBeepType type
        );

        readonly string root = Path.Combine(AppDomain.CurrentDomain.SetupInformation.ApplicationBase,
                                            "data");

        QuestDictionary questFiles = null;
        QuestsFile questData = null;
        TreeNode rootNode = null;

        string _raceToView;

        public QuestsForm(string race) {
            InitializeComponent();
            ToggleProgressBar(false);
            lblTitle.Text = String.Empty;
            this.btnSearch.Text = Program.IniReader["search"];
            this.treeView.Nodes[0].Text = Program.IniReader["quests"];
            this.Text = Program.IniReader["aionQuests"];
            _raceToView = race;
            rootNode = treeView.Nodes[0];
        }

        void ToggleProgressBar(bool show) {
            statusSpacer.BorderSides = show ? ToolStripStatusLabelBorderSides.Right
                                            : ToolStripStatusLabelBorderSides.None;
            statusEnd.BorderSides = statusSpacer.BorderSides;
            statusProgressBar.Visible = show;
        }

        Thread thread = null;

        void OnShown(object sender, EventArgs args) {
            List<string> files = new List<string>();
            try {
                files.AddRange(Directory.GetFiles(Path.Combine(root, @"dialogs"), @"*.html"));
                files.AddRange(Directory.GetFiles(Path.Combine(root, @"dialogs\10000_19999"), @"*.html"));
                files.AddRange(Directory.GetFiles(Path.Combine(root, @"dialogs\20000_29999"), @"*.html"));
                files.AddRange(Directory.GetFiles(Path.Combine(root, @"dialogs\30000_39999"), @"*.html"));
                files.AddRange(Directory.GetFiles(Path.Combine(root, @"dialogs\40000_49999"), @"*.html"));
                thread = new Thread(new ParameterizedThreadStart(LoadFiles));
                thread.Name = "XML Loader";
                thread.IsBackground = true;
                thread.Priority = ThreadPriority.BelowNormal;
                thread.SetApartmentState(ApartmentState.STA);
                thread.Start(files);
            } catch (DirectoryNotFoundException) {
                MessageBox.Show(Program.IniReader["dataMissing"], Program.IniReader["error"],
                                MessageBoxButtons.OK, MessageBoxIcon.Error);
            } catch (Exception e) {
                MessageBox.Show(String.Format(Program.IniReader["whoops"] + "\r\n\r\n{0}", e),
                                              Program.IniReader["error"],
                                MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }

        const int LOAD_STEPS = 3;

        void LoadFiles(object filePaths) {
            List<string> files = (List<string>)filePaths;

            InvokeIfRequired(() =>
            {
                this.Cursor = Cursors.WaitCursor;
                statusProgressBar.Maximum = files.Count;
                ToggleProgressBar(true);
            });

            // Load HTML files

            // Step 0
            ResetProgress(LOAD_STEPS, Program.IniReader["loading"] + " Html's...");

            if (File.Exists(Path.Combine(root, @".\dialogs\quests.dat"))) {
                try {
                    using (FileStream fs = new FileStream(Path.Combine(root, @".\dialogs\quests.dat"), FileMode.Open)) {
                        BinaryFormatter bf = new BinaryFormatter();
                        questFiles = (QuestDictionary)bf.Deserialize(fs);
                    }
                } catch { }
            }
            if (questFiles == null)
                questFiles = new QuestDictionary();

            bool addedNew = false;

            ResetProgress(questFiles.Count, String.Empty);

            for (int i = 0; i < files.Count; i++) {
                string name = Path.GetFileName(files[i]);
                if (!name.StartsWith("quest_"))
                    continue;
                string qId = Path.GetFileNameWithoutExtension(Path.GetFileName(files[i]))
                                 .Remove(0, 7).Trim();
                int id;
                if (!Int32.TryParse(qId, out id))
                    continue;
                if (_raceToView == "elyo" && id >= 2000 && id < 3000)
                    continue;
                if (_raceToView == "asmodian" && id < 2000)
                    continue;
                string msg = String.Format(Program.IniReader["loading"] + " {0}...", Path.GetFileName(files[i]));
                InvokeIfRequired(() => { statusLabel.Text = msg; });
                if (!questFiles.ContainsKey(id)) {
                    addedNew = true;
                    QuestFile questFile;
                    if (Utility.TryLoadQuestHtml(files[i], out questFile))
                        questFiles.Add(id, questFile);
                }
                InvokeIfRequired(() => { statusProgressBar.Value = i + 1; });
                Thread.Sleep(1);
            }

            try {
                using (var fs = new FileStream(Path.Combine(root, @".\dialogs\HtmlPages.xml"),
                                               FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(HtmlPageIndex));
                    HtmlPage.Index = (HtmlPageIndex)ser.Deserialize(reader);
                    HtmlPage.Index.CreateIndex();
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }

            // Step 1
            ResetProgress(LOAD_STEPS, Program.IniReader["loading"] + " HyperLinks.xml...");

            try {
                using (var fs = new FileStream(Path.Combine(root, @".\dialogs\HyperLinks.xml"),
                                               FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(HyperLinkIndex));
                    SelectsAct.Index = (HyperLinkIndex)ser.Deserialize(reader);
                    SelectsAct.Index.CreateIndex();
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }

            // Step 2
            ShowProgress(Program.IniReader["loadingStrings"] + "...");
            Utility.LoadStrings(root);

            // Step 3
            ShowProgress(Program.IniReader["loadingNpc"] + "...");
            Utility.LoadNpcs(root);

            // Step 4
            ShowProgress(Program.IniReader["loadingQuestData"] + "...");
            try {
                using (var fs = new FileStream(Path.Combine(root, @".\quest\quest.xml"),
                                               FileMode.Open, FileAccess.Read))
                using (var reader = XmlReader.Create(fs)) {
                    XmlSerializer ser = new XmlSerializer(typeof(QuestsFile));
                    questData = (QuestsFile)ser.Deserialize(reader);
                    questData.CreateIndex();
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }

            // Step 5
            ShowProgress(Program.IniReader["loadingItemData"] + "...");
            Utility.LoadItems(root);

            /*
            InvBonuses bonuses = new InvBonuses();
            bonuses.BonusItems = new List<BonusItem>();
            foreach (var q in questData.QuestList) {
                WrapItem wi = null;
                if (q.reward_item_ext_1 != null && q.reward_item_ext_1.StartsWith("wrap_")) {
                    wi = new WrapItem();
                    string[] itemData = q.reward_item_ext_1.Split(' ');
                    Item item = Utility.ItemIndex.GetItem(itemData[0]);
                    if (item == null)
                        wi.itemId = 0;
                    else
                        wi.itemId = item.id;
                    if (itemData[0].Contains("_enchant_"))
                        wi.type = BonusType.ENCHANT;
                    else
                        wi.type = BonusType.MANASTONE;
                    string lvl = itemData[0].Substring(itemData[0].Length - 3, 2);
                    wi.level = Int32.Parse(lvl);
                }
                if (q.HasRandomRaward()) {
                    BonusItem bi = new BonusItem();
                    bi.questId = q.id;
                    bi.BonusInfos = new List<BonusInfo>();
                    if (q.reward_item1_1 != null && q.reward_item1_1.StartsWith("%Quest_")) {
                        BonusInfo bii = new BonusInfo();
                        if (q.check_item1_1 != null) {
                            string[] itemData = q.check_item1_1.Split(' ');
                            bii.checkItem = Utility.ItemIndex.GetItem(itemData[0]).id;
                            bii.checkItemSpecified = true;
                            bii.count = Int32.Parse(itemData[1]);
                            bii.countSpecified = true;
                        }
                        bii.Value = q.reward_item1_1;
                        bi.BonusInfos.Add(bii);
                    }
                    if (q.reward_item1_2 != null && q.reward_item1_2.StartsWith("%Quest_")) {
                        BonusInfo bii = new BonusInfo();
                        if (q.check_item1_2 != null) {
                            string[] itemData = q.check_item1_2.Split(' ');
                            bii.checkItem = Utility.ItemIndex.GetItem(itemData[0]).id;
                            bii.checkItemSpecified = true;
                            bii.count = Int32.Parse(itemData[1]);
                            bii.countSpecified = true;
                        }
                        bii.Value = q.reward_item1_2;
                        bi.BonusInfos.Add(bii);
                    }
                    if (q.reward_item1_3 != null && q.reward_item1_3.StartsWith("%Quest_")) {
                        BonusInfo bii = new BonusInfo();
                        if (q.check_item1_3 != null) {
                            string[] itemData = q.check_item1_3.Split(' ');
                            bii.checkItem = Utility.ItemIndex.GetItem(itemData[0]).id;
                            bii.checkItemSpecified = true;
                            bii.count = Int32.Parse(itemData[1]);
                            bii.countSpecified = true;
                        }
                        bii.Value = q.reward_item1_3;
                        bi.BonusInfos.Add(bii);
                    }
                    if (wi != null) {
                        bi.wrap = wi;
                        bi.wrapSpecified = true;
                    }
                    bonuses.BonusItems.Add(bi);
                } else if (wi != null) {
                    BonusItem bi = new BonusItem();
                    bi.questId = q.id;
                    bi.wrap = wi;
                    bi.wrapSpecified = true;
                    bonuses.BonusItems.Add(bi);
                }
            }

            XmlWriterSettings set = new XmlWriterSettings()
            {
                CloseOutput = false,
                Encoding = Encoding.UTF8,
                Indent = true,
                IndentChars = "\t",
            };
            using (FileStream stream = new FileStream("bonuses.xml", FileMode.Create, FileAccess.Write)) {
                using (XmlWriter wr = XmlWriter.Create(stream, set)) {
                    XmlSerializer ser = new XmlSerializer(typeof(InvBonuses));
                    ser.Serialize(wr, bonuses);
                }
            }
            */

            // Step 6
            ResetProgress(questFiles.Count, String.Empty);

            InvokeIfRequired(() =>
            {
                try {
                    this.Cursor = Cursors.Default;

                    foreach (int level in questData.Levels) {
                        var lvlNode = rootNode.Nodes.Add(level.ToString(),
                                        String.Format(Program.IniReader["level"] + " {0}", level));
                        TreeNode raceNode = null;
                        if (String.IsNullOrEmpty(_raceToView) || _raceToView == "elyo")
                            raceNode = lvlNode.Nodes.Add("pc_light", Program.IniReader["elyo"]);
                        if (String.IsNullOrEmpty(_raceToView) || _raceToView == "asmodian")
                            raceNode = lvlNode.Nodes.Add("pc_dark", Program.IniReader["asmodian"]);
                        Application.DoEvents();
                    }

                    rootNode.Nodes.Add("0", Program.IniReader["misc"]);
                    rootNode.Expand();
                } catch { }
            });

            var writer = new StreamWriter("quests.txt");

            foreach (KeyValuePair<int, QuestFile> quest in questFiles) {
                HtmlPage summary = quest.Value.HtmlPages.Where(p => p.name == "quest_summary")
                                                        .FirstOrDefault();
                Quest questInfo = null;
                string qName = "Q" + quest.Key.ToString();

                ShowProgress(String.Format(Program.IniReader["parsing"] + " {0}", quest.Value.fileName));

                var title = Utility.StringIndex.GetStringDescription("STR_QUEST_NAME_" + qName);

                questInfo = questData["Q" + quest.Key];
                if (questInfo == null) {
                    Debug.Print("Missing data for: {0}", quest.Value.fileName);
                    questInfo = new Quest();
                }

                //if (!questInfo.HasRandomRaward())
                //    continue;

                questInfo.HtmlPages = quest.Value.HtmlPages;
                bool reconstructed = false;

                if (summary == null) {
                    Debug.Print("Quest: {0} doesn't contain summary", quest.Value.fileName);
                    if (title == null) {
                        continue;
                    }
                    Debug.Print("Quest Title: {0}", title.body);
                    summary = new HtmlPage() { name = "quest_summary" };
                    summary.Content = new Contents()
                    {
                        html = new ContentsHtml()
                        {
                            body = new Body()
                            {
                                steps = new Step[] { new Step() },
                                p = new Paragraph[] { new Paragraph() }
                            }
                        }
                    };
                    Paragraph para = summary.Content.html.body.p[0];
                    para.font = new pFont()
                    {
                        font_xml = "quest_summary",
                        Value = String.Empty
                    };

                    if (String.IsNullOrEmpty(questInfo.extra_category)) {
                        questInfo.extra_category = "devanion_quest";
                        Debug.Print("Quest: {0} extra_category is null", quest.Value.fileName);
                    }

                    CultureInfo ci = new CultureInfo(String.Empty);
                    string tit = ci.TextInfo.ToTitleCase(questInfo.extra_category.Replace('_', ' '));
                    para.font.Value = tit;

                    Step singleStep = summary.Content.html.body.steps[0];
                    singleStep.p = new Paragraph()
                    {
                        font = new pFont() { Value = Program.IniReader["step"] + " 1" }
                    };

                    // Include Quest Complete page too
                    var qc = quest.Value.HtmlPages.Where(p => p.name == "quest_complete").FirstOrDefault();
                    if (qc != null)
                        qc.ForceInclude = true;

                    quest.Value.HtmlPages.Add(summary);
                    reconstructed = true;
                }
                if (!reconstructed) {
                    if (summary.Content == null) {
                        Debug.Print("Quest: {0} summary doesn't contain content", quest.Value.fileName);
                        continue;
                    }
                    if (summary.Content.html == null) {
                        Debug.Print("Quest: {0} summary doesn't contain html", quest.Value.fileName);
                        continue;
                    }
                    if (summary.Content.html.body == null) {
                        Debug.Print("Quest: {0} summary doesn't contain body", quest.Value.fileName);
                        continue;
                    }
                    if (summary.Content.html.body.steps == null) {
                        Debug.Print("Quest: {0} summary doesn't contain steps", quest.Value.fileName);
                        continue;
                    }
                }

                if (title == null) {
                    Debug.Print("Quest: {0} has no title", quest.Value.fileName);
                    summary.QuestTitle = qName;
                } else {
                    summary.QuestTitle = title.body;
                }

                TreeNode questNode = new TreeNode(summary.QuestTitle);
                questNode.Name = qName;
                questNode.Tag = questInfo;

                writer.WriteLine(String.Format("{0}\t{1}", qName.Remove(0, 1), summary.QuestTitle));
                writer.Flush();

                int i = 0;
                foreach (Step step in summary.Content.html.body.steps) {
                    // [%collectitem]
                    // step.Value
                    i++;
                    if (String.IsNullOrEmpty(step.Value)) {
                        // ok
                    } else if (step.Value == "[%collectitem]") {
                    }

                    if (String.IsNullOrEmpty(step.p.font.Value)) {
                        Debug.Print("Empty step {0}", i);
                        continue;
                    }

                    step.Number = i;
                    var stepNode = questNode.Nodes.Add("S" + i.ToString(),
                                          String.Format(Program.IniReader["step"] + " {0}", i));
                    stepNode.Tag = step;
                    Thread.Sleep(1);
                }

                TreeNode nodeToAdd = null;

                InvokeIfRequired(() =>
                {
                    if (questInfo.minlevel_permitted == 0) {
                        nodeToAdd = rootNode.Nodes["0"];
                        nodeToAdd.Nodes.Add(questNode);
                        Application.DoEvents();
                    } else {
                        nodeToAdd = rootNode.Nodes[questInfo.minlevel_permitted.ToString()];
                        if (questInfo.race_permitted == "pc_light" &&
                            (_raceToView == null || _raceToView == "elyo")) {
                            nodeToAdd = nodeToAdd.Nodes["pc_light"];
                            AddQuestToRace(nodeToAdd, questNode);
                        } else if (questInfo.race_permitted == "pc_dark" &&
                                   (_raceToView == null || _raceToView == "asmodian")) {
                            nodeToAdd = nodeToAdd.Nodes["pc_dark"];
                            AddQuestToRace(nodeToAdd, questNode);
                        } else {
                            TreeNode node = null;
                            if (_raceToView == null || _raceToView == "elyo") {
                                node = nodeToAdd.Nodes["pc_light"];
                                AddQuestToRace(node, questNode);
                            }
                            if (_raceToView == null || _raceToView == "asmodian") {
                                node = nodeToAdd.Nodes["pc_dark"];
                                AddQuestToRace(node, questNode);
                            }
                        }
                    }
                });
                Thread.Sleep(1);
            }

            writer.Close();

            if (addedNew) {
                try {
                    using (FileStream fs = new FileStream(Path.Combine(root, @".\dialogs\quests.dat"), FileMode.Create)) {
                        BinaryFormatter bf = new BinaryFormatter();
                        bf.Serialize(fs, questFiles);
                    }
                } catch (Exception e) {
                }
            }

            // Done
            InvokeIfRequired(() =>
            {
                statusProgressBar.Value = statusProgressBar.Maximum;
                statusLabel.Text = String.Empty;
                ToggleProgressBar(false);
            });
        }

        void AddQuestToRace(TreeNode raceNode, TreeNode questNode) {
            raceNode.Nodes.Add((TreeNode)questNode.Clone());
        }

        void ShowProgress(string text) {
            InvokeIfRequired(() =>
            {
                statusProgressBar.Value++;
                statusLabel.Text = text;
            });
            Thread.Sleep(1);
        }

        void ResetProgress(int steps, string initialText) {
            InvokeIfRequired(() =>
            {
                statusProgressBar.Value = 0;
                statusProgressBar.Maximum = steps + 1;
                statusLabel.Text = initialText;
            });
        }

        void InvokeIfRequired(Action action) {
            if (this.InvokeRequired) {
                this.Invoke(action);
            } else {
                action.Invoke();
            }
        }

        private void OnClosing(object sender, FormClosingEventArgs e) {
            if (thread != null && thread.IsAlive) {
                thread.Abort();
            }
        }

        private void OnSelected(object sender, TreeViewEventArgs args) {
            using (var freezer = new FormFreezer(this, true)) {
                string name = args.Node.Name;
                TreeViewAction action = args.Action;

                if (table.Controls.Count == 3) {
                    var control = table.Controls[2];
                    table.Controls.RemoveAt(2);
                    control.Dispose();
                    GC.Collect();
                }

                if (name == "pc_light") {
                    lblTitle.Text = Program.IniReader["elyosQuests"];
                    txtDescription.Text = String.Empty;
                } else if (name == "pc_dark") {
                    lblTitle.Text = Program.IniReader["asmodiansQuests"];
                    txtDescription.Text = String.Empty;
                } else if (name.StartsWith("Q")) {
                    Quest quest = (Quest)args.Node.Tag;
                    HtmlPage summary = quest.HtmlPages.Where(p => p.name == "quest_summary")
                                      .FirstOrDefault();
                    if (summary == null) {
                        lblTitle.Text = args.Node.Text;
                        txtDescription.Text = String.Empty;
                    } else {
                        lblTitle.Text = summary.QuestTitle;
                        txtDescription.Text = summary.QuestDescription;
                        var page = new QuestPage(args.Node);
                        page.Width = table.Width - page.Margin.Horizontal;
                        page.QuestId = quest.id;
                        page.IsMission = quest.category1 == "mission";
                        page.Zone = Utility.StringIndex.GetString(quest.category2);
                        page.RepeatCount = quest.max_repeat_count;
                        page.CanAbandon = !quest.cannot_giveup;
                        page.CanShare = !quest.cannot_share;
                        page.ClientLevel = quest.client_level;
                        page.ExtendsInventory = quest.reward_extend_inventory1 > 0;
                        page.ExtendsStigma = quest.reward_extend_stigma1;
                        if (!String.IsNullOrEmpty(quest.gender_permitted)) {
                            page.Genders = quest.gender_permitted.Split(' ', ',');
                        }
                        if (!String.IsNullOrEmpty(quest.race_permitted)) {
                            page.Races = quest.race_permitted.Split(' ', ',');
                        }
                        if (!String.IsNullOrEmpty(quest.class_permitted)) {
                            page.Classes = quest.class_permitted.Split(' ', ',');
                        }

                        List<string> finished = new List<string>();
                        if (!String.IsNullOrEmpty(quest.finished_quest_cond1)) {
                            finished.AddRange(quest.finished_quest_cond1.Split(' ', ','));
                        }
                        if (!String.IsNullOrEmpty(quest.finished_quest_cond2)) {
                            finished.AddRange(quest.finished_quest_cond2.Split(' ', ','));
                        }
                        if (!String.IsNullOrEmpty(quest.finished_quest_cond3)) {
                            finished.AddRange(quest.finished_quest_cond3.Split(' ', ','));
                        }
                        if (!String.IsNullOrEmpty(quest.finished_quest_cond4)) {
                            finished.AddRange(quest.finished_quest_cond4.Split(' ', ','));
                        }
                        if (finished.Count > 0)
                            page.Finished = finished.ToArray();

                        List<string> unfinished = new List<string>();
                        if (!String.IsNullOrEmpty(quest.unfinished_quest_cond1)) {
                            unfinished.AddRange(quest.unfinished_quest_cond1.Split(' ', ','));
                        }
                        if (!String.IsNullOrEmpty(quest.unfinished_quest_cond2)) {
                            unfinished.AddRange(quest.unfinished_quest_cond2.Split(' ', ','));
                        }
                        if (!String.IsNullOrEmpty(quest.unfinished_quest_cond3)) {
                            unfinished.AddRange(quest.unfinished_quest_cond3.Split(' ', ','));
                        }
                        if (!String.IsNullOrEmpty(quest.unfinished_quest_cond4)) {
                            unfinished.AddRange(quest.unfinished_quest_cond4.Split(' ', ','));
                        }
                        if (!String.IsNullOrEmpty(quest.unfinished_quest_cond5)) {
                            unfinished.AddRange(quest.unfinished_quest_cond5.Split(' ', ','));
                        }
                        if (unfinished.Count > 0)
                            page.Unfinished = unfinished.ToArray();

                        table.Controls.Add(page, 0, 2);
                    }
                } else if (name.StartsWith("S")) {
                    var questNode = args.Node.Parent;
                    Quest quest = (Quest)questNode.Tag;
                    HtmlPage summary = quest.HtmlPages.Where(p => p.name == "quest_summary")
                                      .FirstOrDefault();
                    Step step = (Step)args.Node.Tag;
                    lblTitle.Text = summary.QuestTitle;
                    txtDescription.Text = step.Description;
                    Step[] allSteps = questNode.Nodes.Cast<TreeNode>().Select(n => (Step)n.Tag).ToArray();
                    var page = new StepPage(quest, allSteps, step.Number);
                    page.Width = table.Width - page.Margin.Horizontal;
                    table.Controls.Add(page, 0, 2);
                } else {
                    lblTitle.Text = args.Node.Text;
                    txtDescription.Text = String.Empty;
                    //var page = new StepPage(args.Node);
                    //page.Width = table.Width - page.Margin.Horizontal;
                    //table.Controls.Add(page, 0, 2);
                }

                // fix row 2 height
                OnTableSizeChanged(null, null);
            }
        }

        private void OnTableSizeChanged(object sender, EventArgs e) {
            int lines = txtDescription.LineCount;
            txtDescription.Visible = lines != 0;
            table.CellBorderStyle =
                lines == 0 ? TableLayoutPanelCellBorderStyle.None
                           : TableLayoutPanelCellBorderStyle.Single;
            Graphics g = Graphics.FromHwnd(IntPtr.Zero);
            int widthAvailable = table.Width - txtDescription.Margin.Horizontal;
            if (table.HorizontalScroll.Visible)
                widthAvailable -= SystemInformation.VerticalScrollBarWidth;
            SizeF size = g.MeasureString("gY", txtDescription.Font,
                                         widthAvailable, StringFormat.GenericTypographic);
            int newHeight = (int)Math.Ceiling(size.Height * (lines + 1));
            table.RowStyles[1] = new RowStyle(SizeType.Absolute, newHeight);
            g.Dispose();
        }

        int lastSearchMatch = -1;
        string lastSearchPhrase = String.Empty;

        private void OnSearch(object sender, EventArgs e) {
            if (String.IsNullOrEmpty(txtSearch.Text)) {
                lastSearchMatch = -1;
                lastSearchPhrase = String.Empty;
                txtSearch.Focus();
                return;
            }
            int questId = 0;
            if (Int32.TryParse(txtSearch.Text.Trim(), out questId) && questId > 0) {
                SearchByQuestId(questId);
            } else {
                SearchByText(txtSearch.Text.ToLower().Trim());
            }
        }

        private void SearchByQuestId(int id) {
            var questNodes = treeView.Nodes[0].GetNodes(3)
                                              .Select(n => new
                                              {
                                                  Quest = n.Tag as Quest,
                                                  Node = n
                                              });
            var matches = questNodes.Where(t => t.Quest != null && t.Quest.id == id);
            if (matches.Any()) {
                lastSearchPhrase = id.ToString();
                lastSearchMatch = 0;
                TreeNode nodeToShow = matches.First().Node;
                nodeToShow.TreeView.Focus();
                nodeToShow.EnsureVisible();
                nodeToShow.TreeView.SelectedNode = nodeToShow;
            } else {
                SearchByText(id.ToString());
            }
        }

        private void SearchByText(string searchPhrase) {
            var questNodes = treeView.Nodes[0].GetNodes(3)
                                              .Select(n => new
                                              {
                                                  Text = n.Text.ToLower(),
                                                  Node = n
                                              });
            var matches = questNodes.Where(t => t.Text.Contains(searchPhrase));
            if (matches.Any()) {
                TreeNode nodeToShow = null;
                if (lastSearchPhrase == searchPhrase) {
                    lastSearchMatch++;
                    var filtered = matches.Skip(lastSearchMatch);
                    if (filtered.Any())
                        nodeToShow = filtered.First().Node;
                    else {
                        lastSearchMatch = 0;
                        nodeToShow = matches.First().Node;
                        MessageBeep(MessageBeepType.Ok);
                    }
                } else {
                    lastSearchPhrase = searchPhrase;
                    lastSearchMatch = 0;
                    nodeToShow = matches.First().Node;
                }
                nodeToShow.TreeView.Focus();
                nodeToShow.EnsureVisible();
                nodeToShow.TreeView.SelectedNode = nodeToShow;
            } else {
                MessageBeep(MessageBeepType.Error);
            }
        }

        private void OnFormLoad(object sender, EventArgs e) {
            treeView.SelectedNode = treeView.Nodes[0];
        }

        private void OnSearchBoxKey(object sender, KeyEventArgs args) {

        }

        private void OnSearchBoxKey(object sender, KeyPressEventArgs args) {
            if (args.KeyChar == 13) {
                OnSearch(sender, args);
                args.Handled = true;
            }
        }

        private void OnActivated(object sender, EventArgs e) {
            this.treeView.Refresh();
            Application.DoEvents();
        }
    }
}
