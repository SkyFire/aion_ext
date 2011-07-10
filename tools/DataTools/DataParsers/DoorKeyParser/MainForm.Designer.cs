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
            System.Windows.Forms.Label label3;
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MainForm));
            this.txtMissionPath = new System.Windows.Forms.TextBox();
            this.btnMissionRoot = new System.Windows.Forms.Button();
            this.cListBox = new System.Windows.Forms.CheckedListBox();
            this.btnParse = new System.Windows.Forms.Button();
            this.folderBrowser = new System.Windows.Forms.FolderBrowserDialog();
            label3 = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // label3
            // 
            label3.AutoSize = true;
            label3.Location = new System.Drawing.Point(11, 38);
            label3.Name = "label3";
            label3.Size = new System.Drawing.Size(70, 13);
            label3.TabIndex = 5;
            label3.Text = "Client Levels:";
            // 
            // txtMissionPath
            // 
            this.txtMissionPath.BackColor = System.Drawing.SystemColors.ControlLightLight;
            this.txtMissionPath.Location = new System.Drawing.Point(82, 35);
            this.txtMissionPath.Name = "txtMissionPath";
            this.txtMissionPath.ReadOnly = true;
            this.txtMissionPath.Size = new System.Drawing.Size(150, 20);
            this.txtMissionPath.TabIndex = 6;
            // 
            // btnMissionRoot
            // 
            this.btnMissionRoot.Location = new System.Drawing.Point(245, 33);
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
            this.cListBox.Location = new System.Drawing.Point(16, 73);
            this.cListBox.MultiColumn = true;
            this.cListBox.Name = "cListBox";
            this.cListBox.SelectionMode = System.Windows.Forms.SelectionMode.None;
            this.cListBox.Size = new System.Drawing.Size(253, 289);
            this.cListBox.TabIndex = 8;
            this.cListBox.ThreeDCheckBoxes = true;
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
            // MainForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(286, 413);
            this.Controls.Add(this.btnParse);
            this.Controls.Add(this.cListBox);
            this.Controls.Add(this.btnMissionRoot);
            this.Controls.Add(this.txtMissionPath);
            this.Controls.Add(label3);
            this.DoubleBuffered = true;
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MaximizeBox = false;
            this.Name = "MainForm";
            this.Text = "Door Keys Parser";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox txtMissionPath;
        private System.Windows.Forms.Button btnMissionRoot;
        private System.Windows.Forms.CheckedListBox cListBox;
        private System.Windows.Forms.Button btnParse;
        private System.Windows.Forms.FolderBrowserDialog folderBrowser;
    }
}