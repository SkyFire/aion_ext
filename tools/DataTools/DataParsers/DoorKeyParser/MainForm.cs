using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Globalization;
using System.Xml;
using Jamie.ParserBase;
using System.Xml.Serialization;
using System.Diagnostics;

namespace Jamie.Npcs
{
    public partial class MainForm : Form
    {
        public MainForm() {
            InitializeComponent();
            btnParse.Enabled = false;
            if (Directory.Exists(Settings.Default.levelPath)) {
                txtMissionPath.Text = lastSelectedPath = Settings.Default.levelPath;
                PopulateList();
            }
            for (int i = 0; i < cListBox.Items.Count; i++) {
                cListBox.SetItemChecked(i, true);
            }
            btnParse.Enabled = !String.IsNullOrEmpty(txtMissionPath.Text);
        }

        string lastSelectedPath = String.Empty;

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
            btnParse.Enabled = true;
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
        }

        private void btnParse_Click(object sender, EventArgs args) {
            if (Directory.Exists(txtMissionPath.Text)) {
                string[] files = cListBox.CheckedItems.Cast<ListItem>().Select(i => i.FilePath).ToArray();
                try {
                    btnParse.Enabled = false;
                    Cursor.Current = Cursors.WaitCursor;
                    DoParse(files);
                    MessageBox.Show("Done.");
                } catch (Exception e) {
                    MessageBox.Show(e.ToString(), this.Text, MessageBoxButtons.OK, MessageBoxIcon.Error);
                } finally {
                    btnParse.Enabled = true;
                    Cursor.Current = Cursors.Default;
                }
            }
        }

        static string root = AppDomain.CurrentDomain.SetupInformation.ApplicationBase;

        void DoParse(string[] missionFiles) {
            Utility.LoadStrings(root);
            Utility.LoadItems(root);

            Dictionary<string, MissionObjects> objects = new Dictionary<string, MissionObjects>();

            foreach (string file in missionFiles) {
                DirectoryInfo levelDir = new DirectoryInfo(file);
                Utility.LoadMissionFile(file);
                string folderName = levelDir.Parent.Name;
                string[] customNameParts = folderName.Split('-');
                objects.Add(customNameParts[0], Utility.MissionFile.Objects[0]);
            }

            CultureInfo ci = new CultureInfo("");
            DoorTemplates templates = new DoorTemplates();

            foreach (var pair in objects) {
                if (pair.Value.Entity == null)
                    continue;

                IEnumerable<Entity> spawns = pair.Value.Entity.Where(e => e.Properties != null &&
                                                                          e.Properties.ServerData != null &&
                                                                          !String.IsNullOrEmpty(e.Properties.ServerData.Key))
                                                              .Select(e => e);
                foreach (Entity entity in spawns) {
                    string key = entity.Properties.ServerData.Key;
                    Item item = Utility.ItemIndex.GetItem(key);

                    var obj = new Door();
                    /*
                    string[] coords = entity.Pos.Split(',');
                    obj.x = Decimal.Parse(coords[0], ci);
                    obj.y = Decimal.Parse(coords[1], ci);
                    obj.z = Decimal.Parse(coords[2], ci);
                    */
                    obj.worldId = ClientLevelMap.mapToId[pair.Key];
                    obj.id = entity.EntityId;
                    obj.closeable = entity.Properties.ServerData.bCloseable;

                    obj.doorKey = new DoorKey()
                    {
                        itemId = item.id,
                        name = Utility.StringIndex.GetString(item.desc),
                        nameId = Utility.StringIndex[item.desc]
                        /*
                        desc = Utility.StringIndex.GetString(item.desc_long)
                        */
                    };
                    string desc = Utility.StringIndex.GetString(item.desc_long);
                    bool removeLogout = (bool)item.remove_when_logout;
                    if (removeLogout) {
                    }
                    if (desc.IndexOf("remove", StringComparison.InvariantCultureIgnoreCase) != -1) {
                    }

                    templates.Doors.Add(obj);
                }
            }

            string outputPath = Path.Combine(root, @"output");
            if (!Directory.Exists(outputPath))
                Directory.CreateDirectory(outputPath);

            var settings = new XmlWriterSettings()
            {
                CheckCharacters = false,
                CloseOutput = false,
                Indent = true,
                IndentChars = "\t",
                NewLineChars = "\n",
                Encoding = new UTF8Encoding(false)
            };

            StringBuilder sb = new StringBuilder();
            foreach (var t in templates.Doors) {
                sb.Append(t.doorKey.itemId.ToString() + ",");
            }

            try {
                using (var fs = new FileStream(Path.Combine(outputPath, "door_templates.xml"),
                                               FileMode.Create, FileAccess.Write))
                using (var writer = XmlWriter.Create(fs, settings)) {
                    XmlSerializer ser = new XmlSerializer(typeof(DoorTemplates));
                    // ser.UnknownAttribute += new XmlAttributeEventHandler(OnUnknownAttribute);
                    // ser.UnknownElement += new XmlElementEventHandler(OnUnknownElement);
                    ser.Serialize(writer, templates);
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
        }
    }
}
