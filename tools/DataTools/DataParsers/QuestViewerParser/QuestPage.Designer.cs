namespace AionQuests
{
    partial class QuestPage
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
            this.flowPanel = new System.Windows.Forms.FlowLayoutPanel();
            this.lblSteps = new System.Windows.Forms.Label();
            this.tableSteps = new System.Windows.Forms.TableLayoutPanel();
            this.tableDetails = new System.Windows.Forms.TableLayoutPanel();
            this.groupDetails = new System.Windows.Forms.GroupBox();
            this.lblFinish = new System.Windows.Forms.Label();
            this.listFinished = new System.Windows.Forms.ListBox();
            this.checkExtendStigma = new System.Windows.Forms.CheckBox();
            this.checkExtendInventory = new System.Windows.Forms.CheckBox();
            this.listClass = new System.Windows.Forms.ListBox();
            this.label4 = new System.Windows.Forms.Label();
            this.listGender = new System.Windows.Forms.ListBox();
            this.label3 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.listRace = new System.Windows.Forms.ListBox();
            this.lblRepeatCount = new System.Windows.Forms.Label();
            this.checkCanShare = new System.Windows.Forms.CheckBox();
            this.checkCanAbandon = new System.Windows.Forms.CheckBox();
            this.lblClientLevel = new System.Windows.Forms.Label();
            this.lblLocation = new System.Windows.Forms.Label();
            this.checkIsMission = new System.Windows.Forms.CheckBox();
            this.lblQuestId = new System.Windows.Forms.Label();
            this.lblUnfinished = new System.Windows.Forms.Label();
            this.listUnfinished = new System.Windows.Forms.ListBox();
            this.flowPanel.SuspendLayout();
            this.tableDetails.SuspendLayout();
            this.groupDetails.SuspendLayout();
            this.SuspendLayout();
            // 
            // flowPanel
            // 
            this.flowPanel.Controls.Add(this.lblSteps);
            this.flowPanel.Controls.Add(this.tableSteps);
            this.flowPanel.Controls.Add(this.tableDetails);
            this.flowPanel.Dock = System.Windows.Forms.DockStyle.Fill;
            this.flowPanel.FlowDirection = System.Windows.Forms.FlowDirection.TopDown;
            this.flowPanel.Location = new System.Drawing.Point(0, 0);
            this.flowPanel.Name = "flowPanel";
            this.flowPanel.Size = new System.Drawing.Size(521, 418);
            this.flowPanel.TabIndex = 0;
            this.flowPanel.WrapContents = false;
            // 
            // lblSteps
            // 
            this.lblSteps.AutoSize = true;
            this.lblSteps.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.lblSteps.ForeColor = System.Drawing.SystemColors.Info;
            this.lblSteps.Location = new System.Drawing.Point(3, 0);
            this.lblSteps.Name = "lblSteps";
            this.lblSteps.Size = new System.Drawing.Size(46, 16);
            this.lblSteps.TabIndex = 1;
            this.lblSteps.Text = "Steps:";
            // 
            // tableSteps
            // 
            this.tableSteps.ColumnCount = 1;
            this.tableSteps.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableSteps.Location = new System.Drawing.Point(3, 19);
            this.tableSteps.Margin = new System.Windows.Forms.Padding(3, 3, 3, 10);
            this.tableSteps.Name = "tableSteps";
            this.tableSteps.RowCount = 1;
            this.tableSteps.RowStyles.Add(new System.Windows.Forms.RowStyle());
            this.tableSteps.Size = new System.Drawing.Size(497, 16);
            this.tableSteps.TabIndex = 0;
            // 
            // tableDetails
            // 
            this.tableDetails.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)));
            this.tableDetails.ColumnCount = 1;
            this.tableDetails.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableDetails.Controls.Add(this.groupDetails, 0, 0);
            this.tableDetails.Location = new System.Drawing.Point(3, 48);
            this.tableDetails.Name = "tableDetails";
            this.tableDetails.RowCount = 1;
            this.tableDetails.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.tableDetails.Size = new System.Drawing.Size(497, 306);
            this.tableDetails.TabIndex = 3;
            // 
            // groupDetails
            // 
            this.groupDetails.Controls.Add(this.listUnfinished);
            this.groupDetails.Controls.Add(this.lblUnfinished);
            this.groupDetails.Controls.Add(this.lblFinish);
            this.groupDetails.Controls.Add(this.listFinished);
            this.groupDetails.Controls.Add(this.checkExtendStigma);
            this.groupDetails.Controls.Add(this.checkExtendInventory);
            this.groupDetails.Controls.Add(this.listClass);
            this.groupDetails.Controls.Add(this.label4);
            this.groupDetails.Controls.Add(this.listGender);
            this.groupDetails.Controls.Add(this.label3);
            this.groupDetails.Controls.Add(this.label2);
            this.groupDetails.Controls.Add(this.listRace);
            this.groupDetails.Controls.Add(this.lblRepeatCount);
            this.groupDetails.Controls.Add(this.checkCanShare);
            this.groupDetails.Controls.Add(this.checkCanAbandon);
            this.groupDetails.Controls.Add(this.lblClientLevel);
            this.groupDetails.Controls.Add(this.lblLocation);
            this.groupDetails.Controls.Add(this.checkIsMission);
            this.groupDetails.Controls.Add(this.lblQuestId);
            this.groupDetails.Dock = System.Windows.Forms.DockStyle.Fill;
            this.groupDetails.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.groupDetails.ForeColor = System.Drawing.SystemColors.Info;
            this.groupDetails.Location = new System.Drawing.Point(3, 3);
            this.groupDetails.MinimumSize = new System.Drawing.Size(491, 300);
            this.groupDetails.Name = "groupDetails";
            this.groupDetails.Size = new System.Drawing.Size(491, 300);
            this.groupDetails.TabIndex = 3;
            this.groupDetails.TabStop = false;
            this.groupDetails.Text = "Details";
            // 
            // lblFinish
            // 
            this.lblFinish.AutoSize = true;
            this.lblFinish.Location = new System.Drawing.Point(17, 195);
            this.lblFinish.Name = "lblFinish";
            this.lblFinish.Size = new System.Drawing.Size(62, 16);
            this.lblFinish.TabIndex = 16;
            this.lblFinish.Text = "Finished:";
            // 
            // listFinished
            // 
            this.listFinished.FormattingEnabled = true;
            this.listFinished.ItemHeight = 16;
            this.listFinished.Location = new System.Drawing.Point(91, 195);
            this.listFinished.Name = "listFinished";
            this.listFinished.Size = new System.Drawing.Size(65, 68);
            this.listFinished.TabIndex = 15;
            // 
            // checkExtendStigma
            // 
            this.checkExtendStigma.AutoSize = true;
            this.checkExtendStigma.Location = new System.Drawing.Point(216, 131);
            this.checkExtendStigma.Name = "checkExtendStigma";
            this.checkExtendStigma.Size = new System.Drawing.Size(127, 20);
            this.checkExtendStigma.TabIndex = 14;
            this.checkExtendStigma.Text = "Extends Stigma?";
            this.checkExtendStigma.UseVisualStyleBackColor = true;
            // 
            // checkExtendInventory
            // 
            this.checkExtendInventory.AutoSize = true;
            this.checkExtendInventory.Location = new System.Drawing.Point(216, 107);
            this.checkExtendInventory.Name = "checkExtendInventory";
            this.checkExtendInventory.Size = new System.Drawing.Size(139, 20);
            this.checkExtendInventory.TabIndex = 13;
            this.checkExtendInventory.Text = "Extends inventory?";
            this.checkExtendInventory.UseVisualStyleBackColor = true;
            // 
            // listClass
            // 
            this.listClass.FormattingEnabled = true;
            this.listClass.ItemHeight = 16;
            this.listClass.Location = new System.Drawing.Point(369, 76);
            this.listClass.Name = "listClass";
            this.listClass.Size = new System.Drawing.Size(106, 196);
            this.listClass.TabIndex = 12;
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(366, 57);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(60, 16);
            this.label4.TabIndex = 11;
            this.label4.Text = "Classes:";
            // 
            // listGender
            // 
            this.listGender.FormattingEnabled = true;
            this.listGender.ItemHeight = 16;
            this.listGender.Location = new System.Drawing.Point(91, 111);
            this.listGender.Name = "listGender";
            this.listGender.Size = new System.Drawing.Size(65, 36);
            this.listGender.TabIndex = 10;
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(17, 111);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(56, 16);
            this.label3.TabIndex = 9;
            this.label3.Text = "Gender:";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(17, 153);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(44, 16);
            this.label2.TabIndex = 8;
            this.label2.Text = "Race:";
            // 
            // listRace
            // 
            this.listRace.FormattingEnabled = true;
            this.listRace.ItemHeight = 16;
            this.listRace.Location = new System.Drawing.Point(91, 153);
            this.listRace.Name = "listRace";
            this.listRace.Size = new System.Drawing.Size(65, 36);
            this.listRace.TabIndex = 7;
            // 
            // lblRepeatCount
            // 
            this.lblRepeatCount.AutoSize = true;
            this.lblRepeatCount.Location = new System.Drawing.Point(17, 60);
            this.lblRepeatCount.Name = "lblRepeatCount";
            this.lblRepeatCount.Size = new System.Drawing.Size(91, 16);
            this.lblRepeatCount.TabIndex = 6;
            this.lblRepeatCount.Text = "Repeat count:";
            // 
            // checkCanShare
            // 
            this.checkCanShare.AutoCheck = false;
            this.checkCanShare.AutoSize = true;
            this.checkCanShare.Location = new System.Drawing.Point(216, 83);
            this.checkCanShare.Name = "checkCanShare";
            this.checkCanShare.Size = new System.Drawing.Size(95, 20);
            this.checkCanShare.TabIndex = 5;
            this.checkCanShare.Text = "Can share?";
            this.checkCanShare.UseVisualStyleBackColor = true;
            // 
            // checkCanAbandon
            // 
            this.checkCanAbandon.AutoCheck = false;
            this.checkCanAbandon.AutoSize = true;
            this.checkCanAbandon.Location = new System.Drawing.Point(216, 59);
            this.checkCanAbandon.Name = "checkCanAbandon";
            this.checkCanAbandon.Size = new System.Drawing.Size(115, 20);
            this.checkCanAbandon.TabIndex = 4;
            this.checkCanAbandon.Text = "Can abandon?";
            this.checkCanAbandon.UseVisualStyleBackColor = true;
            // 
            // lblClientLevel
            // 
            this.lblClientLevel.AutoSize = true;
            this.lblClientLevel.Location = new System.Drawing.Point(17, 85);
            this.lblClientLevel.Name = "lblClientLevel";
            this.lblClientLevel.Size = new System.Drawing.Size(76, 16);
            this.lblClientLevel.TabIndex = 3;
            this.lblClientLevel.Text = "Client level:";
            // 
            // lblLocation
            // 
            this.lblLocation.AutoSize = true;
            this.lblLocation.Location = new System.Drawing.Point(213, 27);
            this.lblLocation.Name = "lblLocation";
            this.lblLocation.Size = new System.Drawing.Size(62, 16);
            this.lblLocation.TabIndex = 2;
            this.lblLocation.Text = "Location:";
            // 
            // checkIsMission
            // 
            this.checkIsMission.AutoCheck = false;
            this.checkIsMission.AutoSize = true;
            this.checkIsMission.Location = new System.Drawing.Point(124, 26);
            this.checkIsMission.Name = "checkIsMission";
            this.checkIsMission.Size = new System.Drawing.Size(73, 20);
            this.checkIsMission.TabIndex = 1;
            this.checkIsMission.Text = "Mission";
            this.checkIsMission.UseVisualStyleBackColor = true;
            // 
            // lblQuestId
            // 
            this.lblQuestId.AutoSize = true;
            this.lblQuestId.Location = new System.Drawing.Point(17, 27);
            this.lblQuestId.Name = "lblQuestId";
            this.lblQuestId.Size = new System.Drawing.Size(60, 16);
            this.lblQuestId.TabIndex = 0;
            this.lblQuestId.Text = "Quest Id:";
            // 
            // lblUnfinished
            // 
            this.lblUnfinished.AutoSize = true;
            this.lblUnfinished.Location = new System.Drawing.Point(171, 195);
            this.lblUnfinished.Name = "lblUnfinished";
            this.lblUnfinished.Size = new System.Drawing.Size(74, 16);
            this.lblUnfinished.TabIndex = 17;
            this.lblUnfinished.Text = "Unfinished:";
            // 
            // listUnfinished
            // 
            this.listUnfinished.FormattingEnabled = true;
            this.listUnfinished.ItemHeight = 16;
            this.listUnfinished.Location = new System.Drawing.Point(262, 195);
            this.listUnfinished.Name = "listUnfinished";
            this.listUnfinished.Size = new System.Drawing.Size(69, 68);
            this.listUnfinished.TabIndex = 18;
            // 
            // QuestPage
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.ControlDarkDark;
            this.Controls.Add(this.flowPanel);
            this.DoubleBuffered = true;
            this.Name = "QuestPage";
            this.Size = new System.Drawing.Size(521, 418);
            this.Load += new System.EventHandler(this.OnResize);
            this.Resize += new System.EventHandler(this.OnResize);
            this.flowPanel.ResumeLayout(false);
            this.flowPanel.PerformLayout();
            this.tableDetails.ResumeLayout(false);
            this.groupDetails.ResumeLayout(false);
            this.groupDetails.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.FlowLayoutPanel flowPanel;
        private System.Windows.Forms.Label lblSteps;
        private System.Windows.Forms.TableLayoutPanel tableSteps;
        private System.Windows.Forms.TableLayoutPanel tableDetails;
        private System.Windows.Forms.GroupBox groupDetails;
        private System.Windows.Forms.Label lblQuestId;
        private System.Windows.Forms.CheckBox checkIsMission;
        private System.Windows.Forms.Label lblLocation;
        private System.Windows.Forms.Label lblClientLevel;
        private System.Windows.Forms.CheckBox checkCanAbandon;
        private System.Windows.Forms.Label lblRepeatCount;
        private System.Windows.Forms.CheckBox checkCanShare;
        private System.Windows.Forms.ListBox listGender;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.ListBox listRace;
        private System.Windows.Forms.ListBox listClass;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.CheckBox checkExtendStigma;
        private System.Windows.Forms.CheckBox checkExtendInventory;
        private System.Windows.Forms.Label lblFinish;
        private System.Windows.Forms.ListBox listFinished;
        private System.Windows.Forms.ListBox listUnfinished;
        private System.Windows.Forms.Label lblUnfinished;
    }
}
