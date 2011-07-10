namespace Jamie.Npcs
{
    partial class MainForm
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

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent() {
            System.Windows.Forms.Label label1;
            System.Windows.Forms.Label label2;
            System.Windows.Forms.Label label3;
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MainForm));
            this.cboSpawnType = new System.Windows.Forms.ComboBox();
            this.txtSpawnPath = new System.Windows.Forms.TextBox();
            this.btnSpawnPath = new System.Windows.Forms.Button();
            this.txtMissionPath = new System.Windows.Forms.TextBox();
            this.btnMissionRoot = new System.Windows.Forms.Button();
            this.cListBox = new System.Windows.Forms.CheckedListBox();
            this.btnParse = new System.Windows.Forms.Button();
            this.folderBrowser = new System.Windows.Forms.FolderBrowserDialog();
            this.btnToggleCheck = new System.Windows.Forms.Button();
            this.chkCoords = new System.Windows.Forms.CheckBox();
            this.chkMissing = new System.Windows.Forms.CheckBox();
            label1 = new System.Windows.Forms.Label();
            label2 = new System.Windows.Forms.Label();
            label3 = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // label1
            // 
            label1.AutoSize = true;
            label1.Location = new System.Drawing.Point(13, 13);
            label1.Name = "label1";
            label1.Size = new System.Drawing.Size(70, 13);
            label1.TabIndex = 0;
            label1.Text = "Spawn Type:";
            // 
            // label2
            // 
            label2.AutoSize = true;
            label2.Location = new System.Drawing.Point(13, 51);
            label2.Name = "label2";
            label2.Size = new System.Drawing.Size(62, 13);
            label2.TabIndex = 3;
            label2.Text = "Jamie Path:";
            // 
            // label3
            // 
            label3.AutoSize = true;
            label3.Location = new System.Drawing.Point(12, 87);
            label3.Name = "label3";
            label3.Size = new System.Drawing.Size(70, 13);
            label3.TabIndex = 5;
            label3.Text = "Client Levels:";
            // 
            // cboSpawnType
            // 
            this.cboSpawnType.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cboSpawnType.FormattingEnabled = true;
            this.cboSpawnType.Items.AddRange(new object[] {
            "Gatherables",
            "NPCs",
            "Monsters"});
            this.cboSpawnType.Location = new System.Drawing.Point(89, 10);
            this.cboSpawnType.Name = "cboSpawnType";
            this.cboSpawnType.Size = new System.Drawing.Size(150, 21);
            this.cboSpawnType.TabIndex = 1;
            this.cboSpawnType.SelectedIndexChanged += new System.EventHandler(this.cboSpawnType_SelectedIndexChanged);
            // 
            // txtSpawnPath
            // 
            this.txtSpawnPath.BackColor = System.Drawing.SystemColors.ControlLightLight;
            this.txtSpawnPath.Location = new System.Drawing.Point(89, 48);
            this.txtSpawnPath.Name = "txtSpawnPath";
            this.txtSpawnPath.ReadOnly = true;
            this.txtSpawnPath.Size = new System.Drawing.Size(150, 20);
            this.txtSpawnPath.TabIndex = 2;
            // 
            // btnSpawnPath
            // 
            this.btnSpawnPath.Location = new System.Drawing.Point(245, 46);
            this.btnSpawnPath.Name = "btnSpawnPath";
            this.btnSpawnPath.Size = new System.Drawing.Size(24, 23);
            this.btnSpawnPath.TabIndex = 4;
            this.btnSpawnPath.Text = "...";
            this.btnSpawnPath.UseVisualStyleBackColor = true;
            this.btnSpawnPath.Click += new System.EventHandler(this.btnSpawnPath_Click);
            // 
            // txtMissionPath
            // 
            this.txtMissionPath.BackColor = System.Drawing.SystemColors.ControlLightLight;
            this.txtMissionPath.Location = new System.Drawing.Point(89, 84);
            this.txtMissionPath.Name = "txtMissionPath";
            this.txtMissionPath.ReadOnly = true;
            this.txtMissionPath.Size = new System.Drawing.Size(150, 20);
            this.txtMissionPath.TabIndex = 6;
            // 
            // btnMissionRoot
            // 
            this.btnMissionRoot.Location = new System.Drawing.Point(245, 82);
            this.btnMissionRoot.Name = "btnMissionRoot";
            this.btnMissionRoot.Size = new System.Drawing.Size(24, 23);
            this.btnMissionRoot.TabIndex = 7;
            this.btnMissionRoot.Text = "...";
            this.btnMissionRoot.UseVisualStyleBackColor = true;
            this.btnMissionRoot.Click += new System.EventHandler(this.btnMissionRoot_Click);
            // 
            // cListBox
            // 
            this.cListBox.ColumnWidth = 200;
            this.cListBox.FormattingEnabled = true;
            this.cListBox.Location = new System.Drawing.Point(16, 118);
            this.cListBox.MultiColumn = true;
            this.cListBox.Name = "cListBox";
            this.cListBox.Size = new System.Drawing.Size(253, 244);
            this.cListBox.TabIndex = 8;
            this.cListBox.ThreeDCheckBoxes = true;
            this.cListBox.SelectedIndexChanged += new System.EventHandler(this.cListBox_SelectedIndexChanged);
            this.cListBox.DoubleClick += new System.EventHandler(this.cListBox_DoubleClick);
            // 
            // btnParse
            // 
            this.btnParse.Location = new System.Drawing.Point(212, 378);
            this.btnParse.Name = "btnParse";
            this.btnParse.Size = new System.Drawing.Size(57, 23);
            this.btnParse.TabIndex = 9;
            this.btnParse.Text = "&Parse";
            this.btnParse.UseVisualStyleBackColor = true;
            this.btnParse.Click += new System.EventHandler(this.btnParse_Click);
            // 
            // folderBrowser
            // 
            this.folderBrowser.RootFolder = System.Environment.SpecialFolder.MyComputer;
            this.folderBrowser.SelectedPath = "C:\\";
            this.folderBrowser.ShowNewFolderButton = false;
            // 
            // btnToggleCheck
            // 
            this.btnToggleCheck.Location = new System.Drawing.Point(111, 378);
            this.btnToggleCheck.Name = "btnToggleCheck";
            this.btnToggleCheck.Size = new System.Drawing.Size(95, 23);
            this.btnToggleCheck.TabIndex = 10;
            this.btnToggleCheck.Text = "Check/Uncheck";
            this.btnToggleCheck.UseVisualStyleBackColor = true;
            this.btnToggleCheck.Click += new System.EventHandler(this.btnToggleCheck_Click);
            // 
            // chkCoords
            // 
            this.chkCoords.AutoSize = true;
            this.chkCoords.Location = new System.Drawing.Point(15, 368);
            this.chkCoords.Name = "chkCoords";
            this.chkCoords.Size = new System.Drawing.Size(58, 17);
            this.chkCoords.TabIndex = 11;
            this.chkCoords.Text = "Fix x, y";
            this.chkCoords.UseVisualStyleBackColor = true;
            this.chkCoords.CheckedChanged += new System.EventHandler(this.chkCoords_CheckedChanged);
            // 
            // chkMissing
            // 
            this.chkMissing.AutoSize = true;
            this.chkMissing.Location = new System.Drawing.Point(15, 391);
            this.chkMissing.Name = "chkMissing";
            this.chkMissing.Size = new System.Drawing.Size(85, 17);
            this.chkMissing.TabIndex = 12;
            this.chkMissing.Text = "Missing Only";
            this.chkMissing.UseVisualStyleBackColor = true;
            this.chkMissing.CheckedChanged += new System.EventHandler(this.chkMissing_CheckedChanged);
            // 
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(286, 413);
            this.Controls.Add(this.chkMissing);
            this.Controls.Add(this.chkCoords);
            this.Controls.Add(this.btnToggleCheck);
            this.Controls.Add(this.btnParse);
            this.Controls.Add(this.cListBox);
            this.Controls.Add(this.btnMissionRoot);
            this.Controls.Add(this.txtMissionPath);
            this.Controls.Add(label3);
            this.Controls.Add(this.btnSpawnPath);
            this.Controls.Add(label2);
            this.Controls.Add(this.txtSpawnPath);
            this.Controls.Add(this.cboSpawnType);
            this.Controls.Add(label1);
            this.DoubleBuffered = true;
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MaximizeBox = false;
            this.Name = "MainForm";
            this.Text = "Spawn Parser";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ComboBox cboSpawnType;
        private System.Windows.Forms.TextBox txtSpawnPath;
        private System.Windows.Forms.Button btnSpawnPath;
        private System.Windows.Forms.TextBox txtMissionPath;
        private System.Windows.Forms.Button btnMissionRoot;
        private System.Windows.Forms.CheckedListBox cListBox;
        private System.Windows.Forms.Button btnParse;
        private System.Windows.Forms.FolderBrowserDialog folderBrowser;
        private System.Windows.Forms.Button btnToggleCheck;
        private System.Windows.Forms.CheckBox chkCoords;
        private System.Windows.Forms.CheckBox chkMissing;
    }
}