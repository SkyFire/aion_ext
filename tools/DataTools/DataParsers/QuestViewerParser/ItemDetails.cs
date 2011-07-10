using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace AionQuests
{
    public partial class ItemDetails : Form
    {
        public ItemDetails() {
            InitializeComponent();
        }

        Item _item = null;
        Form _parent = null;

        Item _randomSource = null;

        public ItemDetails(Form parent, Item item) : this() {
            _item = item;
            _parent = parent;
            AdjustToParent();
            if (item.ChooseItemsFrom == null || item.ChooseItemsFrom.Count() == 0) {
                lblLevel.Enabled = false;
                btnRandomize.Enabled = false;
                lblNote.Enabled = false;
                cboLevel.Enabled = false;
            } else {
                _randomSource = item;
                Item first = null;
                for (int level = item.level; ; ) {
                    Item test = Utility.ItemIndex.GetRewardBonusForLevel(_randomSource.RandomSettings,
                                                                         _randomSource.ChooseItemsFrom, level);
                    if (test == null)
                        break;
                    if (first == null)
                        first = test;
                    cboLevel.Items.Add(level.ToString());
                    level += 10;
                }
                if (cboLevel.Items.Count == 0) {
                    // populate all
                    var levels = _randomSource.ChooseItemsFrom.Select(i => i.level)
                                              .Distinct().OrderBy(i => i);
                    first = _randomSource.ChooseItemsFrom.First();
                    foreach (int itemLevel in levels) {
                        cboLevel.Items.Add(itemLevel.ToString());
                    }
                }
                cboLevel.SelectedIndex = 0;
                checkRandom.Checked = true;
                if (cboLevel.Items.Count == 1) {
                    btnRandomize.Enabled = false;
                    _item = first;
                    SetData(first);
                }
            }
        }

        internal void AdjustToParent() {
            int b = this.Height - this.ClientSize.Height;
            int p = _parent.Height - _parent.ClientSize.Height;
            int diff = p - b;
            this.Bounds = new Rectangle(_parent.Right + SystemInformation.Border3DSize.Width + 
                                        SystemInformation.BorderSize.Width, 
                                        _parent.Top + diff / 2, this.Width, _parent.Height - diff);
        }

        private void OnLoad(object sender, EventArgs e) {
            AdjustToParent();
            SetData(_item);
        }

        void SetData(Item item) {
            lblTitle.Text = Utility.ItemIndex[item.name];
            lblTitle.ForeColor = ItemsFile.QualityColors[item.Quality];
            txtDescription.Text = Utility.StringIndex.GetString(item.desc_long ?? item.desc);
            lblItemId.Text = Program.IniReader["itemId"] + ": " + item.id.ToString();
            lblItemLevel.Text = Program.IniReader["itemLevel"] + ": " + item.level.ToString();
            lblPrice.Text = String.Format("{0}: {1}", Utility.StringIndex.GetString("STR_ITEM_PRICE"), item.price);
            chkAccountWerehouse.Checked = item.can_deposit_to_account_warehouse;
            chkBreakable.Checked = item.breakable;
            chkCanEnchant.Checked = item.can_proc_enchant;
            chkCanExchange.Checked = item.can_exchange;
            chkCanSell.Checked = item.can_sell_to_npc;
            chkCanSplit.Checked = item.can_split;
            chkLegionWerehouse.Checked = item.can_deposit_to_guild_warehouse;
            chkLogOutRemove.Checked = item.remove_when_logout;
            chkPlayerWerehouse.Checked = item.can_deposit_to_character_warehouse;
            chkSoulBind.Checked = item.soul_bind;

            string iconName = item.icon_name + ".png";
            string imagePath = Path.Combine(Environment.CurrentDirectory, @".\data\items\pics\" + iconName);
            // Load picture
            if (!File.Exists(imagePath))
                return;
            Bitmap pic = new Bitmap(imagePath);
            pic.MakeTransparent(Color.Black);
            picBox.Image = pic;
        }

        private void OnRandomize(object sender, EventArgs e) {
            string strLevel = (string)cboLevel.Items[cboLevel.SelectedIndex];
            int level = Int32.Parse(strLevel);
            if (_randomSource.RandomSettings.Level == 0)
                level = 0;
            Item item = Utility.ItemIndex.GetRewardBonusForLevel(_randomSource.RandomSettings,
                                                                 _randomSource.ChooseItemsFrom, level);
            if (item == null)
                return;
            int count = item.ChooseItemsFrom.Count();
            if (count > 0) {
                Random rnd = new Random();
                int randIdx = (int)(rnd.NextDouble() * count);
                if (randIdx > count - 1)
                    randIdx = count - 1;
                item = item.ChooseItemsFrom.ElementAt(randIdx);
                SetData(item);
            } else {
                return;
            }
        }
    }
}
