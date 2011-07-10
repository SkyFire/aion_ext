using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace Jamie.Npcs
{
    public partial class MainForm : Form
    {
        public MainForm() {
            InitializeComponent();
            btnParse.Enabled = false;
            btnToggleCheck.Enabled = false;
            cboSpawnType.SelectedIndex = 0;
            if (Directory.Exists(Settings.Default.spawnPath)) {
                txtSpawnPath.Text = lastSelectedPath = Settings.Default.spawnPath;
            }
            if (Directory.Exists(Settings.Default.levelPath)) {
                txtMissionPath.Text = lastSelectedPath = Settings.Default.levelPath;
                PopulateList();
                btnToggleCheck.Enabled = true;
            }
            btnParse.Enabled = !String.IsNullOrEmpty(txtMissionPath.Text) && 
                               !String.IsNullOrEmpty(txtSpawnPath.Text);
        }

        int selectedType = 0;
        string lastSelectedPath = String.Empty;

        private void btnSpawnPath_Click(object sender, EventArgs e) {
            folderBrowser.Reset();
            folderBrowser.Description = "Select Jamie \"spawns\" folder.";
            if (!String.IsNullOrEmpty(txtSpawnPath.Text))
                lastSelectedPath = txtSpawnPath.Text;
            folderBrowser.SelectedPath = lastSelectedPath;
            DialogResult result = folderBrowser.ShowDialog(this);
            if (result == DialogResult.Cancel)
                return;
            txtSpawnPath.Text = lastSelectedPath = folderBrowser.SelectedPath;
            btnParse.Enabled = !String.IsNullOrEmpty(txtMissionPath.Text);
        }

        private void btnMissionRoot_Click(object sender, EventArgs e) {
            folderBrowser.Reset();
            folderBrowser.Description = "Select the root folder of client level folders.";
            if (!String.IsNullOrEmpty(txtMissionPath.Text))
                lastSelectedPath = txtMissionPath.Text;
            folderBrowser.SelectedPath = lastSelectedPath;
            DialogResult result = folderBrowser.ShowDialog(this);
            if (result == DialogResult.Cancel)
                return;
            txtMissionPath.Text = lastSelectedPath = folderBrowser.SelectedPath;
            btnParse.Enabled = !String.IsNullOrEmpty(txtSpawnPath.Text);
            btnToggleCheck.Enabled = true;
            cListBox.Items.Clear();
            PopulateList();
        }

        void PopulateList() {
            string[] files = Directory.GetFiles(txtMissionPath.Text, "mission_mission0.xml", SearchOption.AllDirectories);
            List<ListItem> items = new List<ListItem>();

            foreach (string path in files) {
                DirectoryInfo info = new DirectoryInfo(path);
                string[] customPathParts = info.Parent.Name.Split('-');
                if (!ClientLevelMap.mapToId.ContainsKey(customPathParts[0]))
                    continue;
                int mapId = ClientLevelMap.mapToId[customPathParts[0]];
                ListItem item = new ListItem(mapId)
                {
                    FilePath = path,
                    DisplayName = String.Format("{0} ({1})", customPathParts[0], mapId)
                };
                items.Add(item);
            }
            cListBox.Items.AddRange(items.OrderBy(i => i.MapId).ToArray());
            btnToggleCheck_Click(this, null);
        }

        private void btnParse_Click(object sender, EventArgs args) {
            if (Directory.Exists(txtSpawnPath.Text) && Directory.Exists(txtMissionPath.Text)) {
                string[] files = cListBox.CheckedItems.Cast<ListItem>().Select(i => i.FilePath).ToArray();
                SpawnParseType type = selectedType == 0 ? SpawnParseType.Gather : 
                    selectedType == 1 ? SpawnParseType.Npcs : SpawnParseType.Monsters;
                try {
                    btnParse.Enabled = false;
                    Cursor.Current = Cursors.WaitCursor;
                    DoParse(txtSpawnPath.Text, files, type, _doFix, _exportMissing);
                    MessageBox.Show("Done.");
                } catch (Exception e) {
                    MessageBox.Show(e.ToString(), this.Text, MessageBoxButtons.OK, MessageBoxIcon.Error);
                } finally {
                    btnParse.Enabled = true;
                    Cursor.Current = Cursors.Default;
                }
            }
        }

        private void cListBox_SelectedIndexChanged(object sender, EventArgs e) {
            btnParse.Enabled = cListBox.CheckedItems.Count > 0;
        }

        bool _wasChecked = false;

        private void btnToggleCheck_Click(object sender, EventArgs e) {
            for (int i = 0; i < cListBox.Items.Count; i++) {
                cListBox.SetItemChecked(i, !_wasChecked);
            }
            _wasChecked = !_wasChecked;
            cListBox_SelectedIndexChanged(sender, e);
        }

        private void cboSpawnType_SelectedIndexChanged(object sender, EventArgs e) {
            if (selectedType == cboSpawnType.SelectedIndex)
                return;
            selectedType = cboSpawnType.SelectedIndex;
        }

        private void cListBox_DoubleClick(object sender, EventArgs e) {
            cListBox_SelectedIndexChanged(sender, e);
        }

        bool _doFix = false;

        private void chkCoords_CheckedChanged(object sender, EventArgs e) {
            _doFix = !_doFix;
        }

        bool _exportMissing = false;

        private void chkMissing_CheckedChanged(object sender, EventArgs e) {
            _exportMissing = !_exportMissing;
        }
    }
}
