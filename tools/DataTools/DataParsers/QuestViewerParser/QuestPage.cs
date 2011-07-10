using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace AionQuests
{
    public partial class QuestPage : UserControl
    {
        public QuestPage() {
            InitializeComponent();
            this.Dock = DockStyle.Fill;
            this.lblSteps.Text = Program.IniReader["steps"] + ':';
            this.groupDetails.Text = Program.IniReader["details"];
            this.checkExtendStigma.Text = Program.IniReader["extendsStigma"];
            this.checkExtendInventory.Text = Program.IniReader["extendsInventory"];
            this.label4.Text = Program.IniReader["classes"] + ':';
            this.label3.Text = Program.IniReader["gender"] + ':';
            this.label2.Text = Program.IniReader["races"] + ':';
            this.lblRepeatCount.Text = Program.IniReader["repeatCount"] + ':';
            this.checkCanShare.Text = Program.IniReader["canShare"];
            this.checkCanAbandon.Text = Program.IniReader["canAbandon"];
            this.lblClientLevel.Text = Program.IniReader["clientLevel"] + ':';
            this.lblLocation.Text = Program.IniReader["location"] + ':';
            this.checkIsMission.Text = Program.IniReader["mission"];
            this.lblQuestId.Text = Program.IniReader["questId"] + ':';
            this.lblFinish.Text = Program.IniReader["finished"] + ':';
            this.lblUnfinished.Text = Program.IniReader["unfinished"] + ':';
        }

        TreeNode rootNode = null;
        Quest _quest;

        public QuestPage(TreeNode questNode)
            : this() {
            rootNode = questNode;
            _quest = (Quest)rootNode.Tag;
            
            // temp disable
            _quest.StepPages = null;

            lblSteps.Text = String.Format(Program.IniReader["steps"] + 
                                          " ({0} " + Program.IniReader["pages"] + "):", 
                                          _quest.ValidHtmlPages.Count);

            if (questNode.Nodes.Count > 0) {
                int row = 0;
                var nInfo = questNode.Nodes.OfType<TreeNode>().Select(n =>
                                                        new { Node = n, Step = n.Tag as Step } )
                                           .Where(n => n.Step != null);
                tableSteps.AutoSize = true;
                tableSteps.RowCount = nInfo.Count();
                foreach (var info in nInfo) {
                    var link = new LinkLabel();
                    link.AutoSize = true;
                    link.LinkColor = Color.FromArgb(255, 255, 192);
                    link.VisitedLinkColor = Color.FromArgb(230, 230, 0);
                    link.ActiveLinkColor = Color.Yellow;
                    link.DisabledLinkColor = Color.FromArgb(224, 224, 224);
                    link.Text = info.Step.p.font.Text;
                    var vars = Utility.GetNpcIdsFromDescription(info.Step.p.font.Value);
                    if (vars.Any()) {
                        link.Text += " (NPC Id: ";
                        foreach (var id in vars)
                            link.Text += id + ", ";
                        link.Text = link.Text.TrimEnd(',', ' ');
                        link.Text += ")";
                    }
                    link.Links[0].LinkData = info.Node;
                    link.Margin = new Padding(30, 1, 3, 1);
                    link.LinkClicked += new LinkLabelLinkClickedEventHandler(OnLinkClicked);
                    tableSteps.Controls.Add(link, 0, row++);
                }
            }
        }

        void OnLinkClicked(object sender, LinkLabelLinkClickedEventArgs args) {
            TreeNode node = (TreeNode)args.Link.LinkData;
            node.EnsureVisible();
            node.TreeView.SelectedNode = node;
            args.Link.Visited = true;
        }

        private void OnResize(object sender, EventArgs e) {
            SuspendLayout();
            try {
                tableDetails.Width = this.Width - tableDetails.Margin.Horizontal;
            } finally {
                ResumeLayout();
                flowPanel.PerformLayout();
            }
        }

        int _questId = 0;

        public int QuestId {
            get { return _questId; }
            set {
                _questId = value;
                lblQuestId.Text = String.Format(Program.IniReader["questId"] + ": {0}", _questId);
            }
        }

        int _repeatCount = 1;

        public int RepeatCount {
            get { return _repeatCount; }
            set {
                _repeatCount = value;
                lblRepeatCount.Text = String.Format(Program.IniReader["repeatCount"] + ": {0}", _repeatCount);
            }
        }

        int _clientLevel = 1;

        public int ClientLevel {
            get { return _clientLevel; }
            set {
                _clientLevel = value;
                lblClientLevel.Text = String.Format(Program.IniReader["clientLevel"] + ": {0}", _clientLevel);
            }
        }

        public bool IsMission {
            get { return checkIsMission.Checked; }
            set { checkIsMission.Checked = value; }
        }

        public bool CanAbandon {
            get { return checkCanAbandon.Checked; }
            set { checkCanAbandon.Checked = value; }
        }

        public bool CanShare {
            get { return checkCanShare.Checked; }
            set { checkCanShare.Checked = value; }
        }

        public bool ExtendsInventory {
            get { return checkExtendInventory.Checked; }
            set { checkExtendInventory.Checked = value; }
        }

        public bool ExtendsStigma {
            get { return checkExtendStigma.Checked; }
            set { checkExtendStigma.Checked = value; }
        }

        string _location = String.Empty;

        public string Zone {
            get { return _location; }
            set {
                _location = value;
                lblLocation.Text = String.Format(Program.IniReader["location"] + ": {0}", _location);
            }
        }

        string[] _genders;

        public string[] Genders {
            get { return _genders; }
            set {
                _genders = value;
                listGender.Items.Clear();
                if (_genders == null || _genders.Length == 0)
                    return;
                if (_genders.Length == 1 && _genders[0] == "all")
                    _genders = new string[] { "male", "female" };
                listGender.Items.AddRange(_genders);
            }
        }

        string[] _races;

        public string[] Races {
            get { return _races; }
            set {
                _races = value;
                listRace.Items.Clear();
                if (_races == null || _races.Length == 0)
                    return;
                listRace.Items.AddRange(_races);
            }
        }

        string[] _classes;

        public string[] Classes {
            get { return _classes; }
            set {
                _classes = value;
                listClass.Items.Clear();
                if (_classes == null || _classes.Length == 0)
                    return;
                listClass.Items.AddRange(_classes);
            }
        }

        string[] _finished;

        public string[] Finished {
            get { return _finished; }
            set {
                _finished = value;
                listFinished.Items.Clear();
                if (_finished == null || _finished.Length == 0)
                    return;
                listFinished.Items.AddRange(_finished);
            }
        }

        string[] _unfinished;

        public string[] Unfinished {
            get { return _unfinished; }
            set {
                _unfinished = value;
                listUnfinished.Items.Clear();
                if (_unfinished == null || _unfinished.Length == 0)
                    return;
                listUnfinished.Items.AddRange(_unfinished);
            }
        }
    }
}
