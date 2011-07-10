using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace AionQuests
{
    public partial class RewardList : UserControl
    {
        public RewardList() {
            InitializeComponent();
            foreach (Image img in imageListQuest.Images) {
                Image clone = (Image)img.Clone();
                imageListBasic.Images.Add(clone);
                imageListSelectable.Images.Add(clone);
            }

            listBasicRewards.ItemSelectionChanged += 
                new ListViewItemSelectionChangedEventHandler(OnItemSelected);
            listSelectRewards.ItemSelectionChanged +=
                new ListViewItemSelectionChangedEventHandler(OnItemSelected);
        }

        Reward _reward = null;

        public RewardList(Reward reward) : this() {
            _reward = reward;
            if (reward.Exp > 0) {
                string strExp = Utility.StringIndex.GetString("STR_QUEST_REWARD_EXP").Replace("%0", "{0}");
                ListViewItem lvi = new ListViewItem(String.Format("    " + strExp, reward.Exp), 2);
                listBasicRewards.Items.Add(lvi);
            }
            if (reward.AbyssPoints > 0) {
                string strAbyssPt = Program.IniReader["abyssPoints"];
                ListViewItem lvi = new ListViewItem(String.Format("    {0} " + strAbyssPt, reward.AbyssPoints), 0);
                listBasicRewards.Items.Add(lvi);
            }
            if (reward.Gold > 0) {
                string strKinah = Utility.StringIndex.GetString("STR_GOLD");
                ListViewItem lvi = new ListViewItem(String.Format("    {0} " + strKinah, reward.Gold), 8);
                listBasicRewards.Items.Add(lvi);
                lvi.Tag = Utility.ItemIndex.GetItem("gold");
            }
            if (!String.IsNullOrEmpty(reward.Title)) {
                string title = Utility.StringIndex.GetString("STR_QUEST_REWARD_TITLE").Replace("%0", "{0}"); ;
                ListViewItem lvi = new ListViewItem(String.Format("    " + title, 
                    Utility.StringIndex.GetString("STR_" + reward.Title.ToUpper())), 5);
                listBasicRewards.Items.Add(lvi);
            }
            foreach (Item item in reward.BasicItems) {
                string itemName = Utility.ItemIndex[item.name];
                string iconName = item.icon_name + ".png";
                string imagePath = Path.Combine(Environment.CurrentDirectory, @".\data\items\pics\" + iconName);

                ListViewItem lvi;
                // Load picture into the imageList
                if (File.Exists(imagePath)) {
                    Image pic = Image.FromFile(imagePath);
                    string key = item.id.ToString();
                    imageListBasic.Images.Add(key, pic);
                    lvi = new ListViewItem(String.Format("    {0} (×{1})", itemName, item.Count), key);
                } else {
                    lvi = new ListViewItem(String.Format("    {0} (×{1})", itemName, item.Count), 6);
                }
                listBasicRewards.Items.Add(lvi);
                lvi.Tag = item;
                lvi.ForeColor = ItemsFile.QualityColors[item.Quality];
            }

            if (listBasicRewards.Items.Count == 0) {
                listBasicRewards.Visible = false;
                lblBasic.Visible = false;
            } else {
                listBasicRewards.Height = 41 * listBasicRewards.Items.Count;
            }

            foreach (Item item in reward.SelectItems) {
                string itemName = Utility.ItemIndex[item.name];
                string iconName = item.icon_name + ".png";
                string imagePath = Path.Combine(Environment.CurrentDirectory, @".\data\items\pics\" + iconName);

                ListViewItem lvi;
                // Load picture into the imageList
                if (File.Exists(imagePath)) {
                    Image pic = Image.FromFile(imagePath);
                    string key = item.id.ToString();
                    imageListSelectable.Images.Add(key, pic);
                    lvi = new ListViewItem(String.Format("    {0} (×{1})", itemName, item.Count), key);
                } else {
                    lvi = new ListViewItem(String.Format("    {0} (×{1})", itemName, item.Count), 6);
                }
                listSelectRewards.Items.Add(lvi);
                lvi.Tag = item;
                lvi.ForeColor = ItemsFile.QualityColors[item.Quality];
            }

            if (listSelectRewards.Items.Count == 0) {
                listSelectRewards.Visible = false;
                lblSelectable.Visible = false;
            } else {
                listSelectRewards.Height = 41 * listSelectRewards.Items.Count;
            }
        }

        public int RecommendedHeight {
            get {
                if (_reward == null)
                    return this.Height;
                int height = 41 * listBasicRewards.Items.Count + lblBasic.Height + lblBasic.Margin.Vertical;
                height += 41 * listSelectRewards.Items.Count + lblSelectable.Height + lblSelectable.Margin.Vertical;
                return height;
            }
        }

        public override Color ForeColor {
            get { return base.ForeColor; }
            set {
                base.ForeColor = value;
                lblBasic.ForeColor = value;
                lblSelectable.ForeColor = value;
            }
        }

        [Category("Appearance")]
        [DisplayName("BasicForeColor")]
        public Color BasicForeColor {
            get { return listBasicRewards.ForeColor; }
            set { listBasicRewards.ForeColor = value; }
        }

        [Category("Appearance")]
        [DisplayName("SelectableForeColor")]
        public Color SelectableForeColor {
            get { return listSelectRewards.ForeColor; }
            set { listSelectRewards.ForeColor = value; }
        }

        public delegate void RewardSelectedEventHandler(object sender, Item arg);

        public event RewardSelectedEventHandler RewardSelected;

        void OnItemSelected(object sender, ListViewItemSelectionChangedEventArgs args) {
            if (!args.IsSelected)
                return;
            Item item = args.Item.Tag as Item;
            if (item == null)
                return;
            if (RewardSelected != null)
                RewardSelected(this, item);
        }

        public void EnsureItemFocused() {
            if (listBasicRewards.SelectedItems.Count > 0) {
                listBasicRewards.FocusedItem.EnsureVisible();
                int idx = listBasicRewards.Items.IndexOf(listBasicRewards.FocusedItem);
                listBasicRewards.Items[idx].Focused = true;
            }
            if (listSelectRewards.SelectedItems.Count > 0) {
                listSelectRewards.FocusedItem.EnsureVisible();
                int idx = listSelectRewards.Items.IndexOf(listSelectRewards.FocusedItem);
                listSelectRewards.Items[idx].Focused = true;
            }
            this.Focus();
        }
    }
}
