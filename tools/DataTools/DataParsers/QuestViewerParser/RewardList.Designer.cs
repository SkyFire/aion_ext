namespace AionQuests
{
    partial class RewardList
    {
        /// <summary> 
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary> 
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing) {
            if (disposing && (components != null)) {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Component Designer generated code

        /// <summary> 
        /// Required method for Designer support - do not modify 
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent() {
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(RewardList));
            this.listBasicRewards = new System.Windows.Forms.ListView();
            this.columnB = new System.Windows.Forms.ColumnHeader();
            this.imageListBasic = new System.Windows.Forms.ImageList(this.components);
            this.layoutRewards = new System.Windows.Forms.FlowLayoutPanel();
            this.lblBasic = new System.Windows.Forms.Label();
            this.lblSelectable = new System.Windows.Forms.Label();
            this.listSelectRewards = new System.Windows.Forms.ListView();
            this.columnS = new System.Windows.Forms.ColumnHeader();
            this.imageListSelectable = new System.Windows.Forms.ImageList(this.components);
            this.imageListQuest = new System.Windows.Forms.ImageList(this.components);
            this.layoutRewards.SuspendLayout();
            this.SuspendLayout();
            // 
            // listBasicRewards
            // 
            this.listBasicRewards.Anchor = System.Windows.Forms.AnchorStyles.Left;
            this.listBasicRewards.BackColor = System.Drawing.SystemColors.ControlDarkDark;
            this.listBasicRewards.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.listBasicRewards.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnB});
            this.listBasicRewards.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.listBasicRewards.ForeColor = System.Drawing.Color.MintCream;
            this.listBasicRewards.FullRowSelect = true;
            this.listBasicRewards.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.None;
            this.listBasicRewards.LabelWrap = false;
            this.listBasicRewards.Location = new System.Drawing.Point(0, 16);
            this.listBasicRewards.Margin = new System.Windows.Forms.Padding(0);
            this.listBasicRewards.MinimumSize = new System.Drawing.Size(282, 0);
            this.listBasicRewards.MultiSelect = false;
            this.listBasicRewards.Name = "listBasicRewards";
            this.listBasicRewards.Scrollable = false;
            this.listBasicRewards.Size = new System.Drawing.Size(282, 41);
            this.listBasicRewards.SmallImageList = this.imageListBasic;
            this.listBasicRewards.TabIndex = 0;
            this.listBasicRewards.UseCompatibleStateImageBehavior = false;
            this.listBasicRewards.View = System.Windows.Forms.View.Details;
            // 
            // columnB
            // 
            this.columnB.Width = 320;
            // 
            // imageListBasic
            // 
            this.imageListBasic.ColorDepth = System.Windows.Forms.ColorDepth.Depth24Bit;
            this.imageListBasic.ImageSize = new System.Drawing.Size(40, 40);
            this.imageListBasic.TransparentColor = System.Drawing.Color.Black;
            // 
            // layoutRewards
            // 
            this.layoutRewards.AutoScroll = true;
            this.layoutRewards.BackColor = System.Drawing.Color.Transparent;
            this.layoutRewards.Controls.Add(this.lblBasic);
            this.layoutRewards.Controls.Add(this.listBasicRewards);
            this.layoutRewards.Controls.Add(this.lblSelectable);
            this.layoutRewards.Controls.Add(this.listSelectRewards);
            this.layoutRewards.Dock = System.Windows.Forms.DockStyle.Fill;
            this.layoutRewards.FlowDirection = System.Windows.Forms.FlowDirection.TopDown;
            this.layoutRewards.ForeColor = System.Drawing.SystemColors.Info;
            this.layoutRewards.Location = new System.Drawing.Point(0, 0);
            this.layoutRewards.Margin = new System.Windows.Forms.Padding(0);
            this.layoutRewards.Name = "layoutRewards";
            this.layoutRewards.Size = new System.Drawing.Size(339, 146);
            this.layoutRewards.TabIndex = 2;
            this.layoutRewards.WrapContents = false;
            // 
            // lblBasic
            // 
            this.lblBasic.AutoSize = true;
            this.lblBasic.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.lblBasic.Location = new System.Drawing.Point(3, 0);
            this.lblBasic.Name = "lblBasic";
            this.lblBasic.Size = new System.Drawing.Size(102, 16);
            this.lblBasic.TabIndex = 0;
            this.lblBasic.Text = "Basic Rewards:";
            // 
            // lblSelectable
            // 
            this.lblSelectable.AutoSize = true;
            this.lblSelectable.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.lblSelectable.Location = new System.Drawing.Point(3, 67);
            this.lblSelectable.Margin = new System.Windows.Forms.Padding(3, 10, 3, 0);
            this.lblSelectable.Name = "lblSelectable";
            this.lblSelectable.Size = new System.Drawing.Size(133, 16);
            this.lblSelectable.TabIndex = 1;
            this.lblSelectable.Text = "Selectable Rewards:";
            // 
            // listSelectRewards
            // 
            this.listSelectRewards.BackColor = System.Drawing.SystemColors.ControlDarkDark;
            this.listSelectRewards.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.listSelectRewards.Columns.AddRange(new System.Windows.Forms.ColumnHeader[] {
            this.columnS});
            this.listSelectRewards.Dock = System.Windows.Forms.DockStyle.Fill;
            this.listSelectRewards.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.listSelectRewards.ForeColor = System.Drawing.Color.PaleTurquoise;
            this.listSelectRewards.FullRowSelect = true;
            this.listSelectRewards.HeaderStyle = System.Windows.Forms.ColumnHeaderStyle.None;
            this.listSelectRewards.LabelWrap = false;
            this.listSelectRewards.Location = new System.Drawing.Point(0, 83);
            this.listSelectRewards.Margin = new System.Windows.Forms.Padding(0);
            this.listSelectRewards.MinimumSize = new System.Drawing.Size(282, 0);
            this.listSelectRewards.MultiSelect = false;
            this.listSelectRewards.Name = "listSelectRewards";
            this.listSelectRewards.Scrollable = false;
            this.listSelectRewards.Size = new System.Drawing.Size(282, 41);
            this.listSelectRewards.SmallImageList = this.imageListSelectable;
            this.listSelectRewards.TabIndex = 1;
            this.listSelectRewards.UseCompatibleStateImageBehavior = false;
            this.listSelectRewards.View = System.Windows.Forms.View.Details;
            // 
            // columnS
            // 
            this.columnS.Width = 320;
            // 
            // imageListSelectable
            // 
            this.imageListSelectable.ColorDepth = System.Windows.Forms.ColorDepth.Depth24Bit;
            this.imageListSelectable.ImageSize = new System.Drawing.Size(40, 40);
            this.imageListSelectable.TransparentColor = System.Drawing.Color.Black;
            // 
            // imageListQuest
            // 
            this.imageListQuest.ImageStream = ((System.Windows.Forms.ImageListStreamer)(resources.GetObject("imageListQuest.ImageStream")));
            this.imageListQuest.TransparentColor = System.Drawing.Color.Black;
            this.imageListQuest.Images.SetKeyName(0, "icon_quest_ap01.png");
            this.imageListQuest.Images.SetKeyName(1, "icon_quest_cube_expansion01.png");
            this.imageListQuest.Images.SetKeyName(2, "icon_quest_exp01.png");
            this.imageListQuest.Images.SetKeyName(3, "icon_quest_stigmaslot_expansion01.png");
            this.imageListQuest.Images.SetKeyName(4, "icon_quest_storage_expansion01.png");
            this.imageListQuest.Images.SetKeyName(5, "icon_quest_title01.png");
            this.imageListQuest.Images.SetKeyName(6, "icon_quest_undefineable01.png");
            this.imageListQuest.Images.SetKeyName(7, "Icon_quest_xp_reward.png");
            this.imageListQuest.Images.SetKeyName(8, "Icon_Item_Qina01.png");
            // 
            // RewardList
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.ControlDarkDark;
            this.Controls.Add(this.layoutRewards);
            this.ForeColor = System.Drawing.SystemColors.Info;
            this.Margin = new System.Windows.Forms.Padding(0);
            this.Name = "RewardList";
            this.Size = new System.Drawing.Size(339, 146);
            this.layoutRewards.ResumeLayout(false);
            this.layoutRewards.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.ListView listBasicRewards;
        private System.Windows.Forms.FlowLayoutPanel layoutRewards;
        private System.Windows.Forms.Label lblBasic;
        private System.Windows.Forms.Label lblSelectable;
        private System.Windows.Forms.ImageList imageListBasic;
        private System.Windows.Forms.ImageList imageListQuest;
        private System.Windows.Forms.ImageList imageListSelectable;
        private System.Windows.Forms.ColumnHeader columnB;
        private System.Windows.Forms.ColumnHeader columnS;
        private System.Windows.Forms.ListView listSelectRewards;
    }
}
