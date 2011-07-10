namespace AionQuests
{
    partial class StepPage
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
            this.lblTask = new System.Windows.Forms.Label();
            this.tableItems = new System.Windows.Forms.TableLayoutPanel();
            this.tabPages = new AionQuests.FlatTabControl();
            this.tabPage1 = new System.Windows.Forms.TabPage();
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this.tableLayoutPanel2 = new System.Windows.Forms.TableLayoutPanel();
            this.transparentRichTextBox1 = new AionQuests.TransparentRichTextBox();
            this.flowPanel.SuspendLayout();
            this.tabPages.SuspendLayout();
            this.tabPage1.SuspendLayout();
            this.tableLayoutPanel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // flowPanel
            // 
            this.flowPanel.BackColor = System.Drawing.SystemColors.ControlDarkDark;
            this.flowPanel.Controls.Add(this.lblTask);
            this.flowPanel.Controls.Add(this.tableItems);
            this.flowPanel.Controls.Add(this.tabPages);
            this.flowPanel.Dock = System.Windows.Forms.DockStyle.Fill;
            this.flowPanel.FlowDirection = System.Windows.Forms.FlowDirection.TopDown;
            this.flowPanel.Location = new System.Drawing.Point(0, 0);
            this.flowPanel.Name = "flowPanel";
            this.flowPanel.Size = new System.Drawing.Size(483, 551);
            this.flowPanel.TabIndex = 1;
            this.flowPanel.WrapContents = false;
            // 
            // lblTask
            // 
            this.lblTask.AutoSize = true;
            this.lblTask.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.lblTask.ForeColor = System.Drawing.SystemColors.Info;
            this.lblTask.Location = new System.Drawing.Point(3, 0);
            this.lblTask.Name = "lblTask";
            this.lblTask.Size = new System.Drawing.Size(39, 16);
            this.lblTask.TabIndex = 1;
            this.lblTask.Text = "Task";
            // 
            // tableItems
            // 
            this.tableItems.ColumnCount = 1;
            this.tableItems.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableItems.Location = new System.Drawing.Point(3, 19);
            this.tableItems.Margin = new System.Windows.Forms.Padding(3, 3, 3, 10);
            this.tableItems.Name = "tableItems";
            this.tableItems.RowCount = 1;
            this.tableItems.RowStyles.Add(new System.Windows.Forms.RowStyle());
            this.tableItems.Size = new System.Drawing.Size(456, 16);
            this.tableItems.TabIndex = 0;
            // 
            // tabPages
            // 
            this.tabPages.Appearance = System.Windows.Forms.TabAppearance.Buttons;
            this.tabPages.BackColor = System.Drawing.SystemColors.ControlDarkDark;
            this.tabPages.Controls.Add(this.tabPage1);
            this.tabPages.Dock = System.Windows.Forms.DockStyle.Fill;
            this.flowPanel.SetFlowBreak(this.tabPages, true);
            this.tabPages.ItemSize = new System.Drawing.Size(40, 23);
            this.tabPages.Location = new System.Drawing.Point(50, 48);
            this.tabPages.Margin = new System.Windows.Forms.Padding(50, 3, 3, 3);
            this.tabPages.MinimumSize = new System.Drawing.Size(409, 480);
            this.tabPages.Name = "tabPages";
            this.tabPages.Padding = new System.Drawing.Point(0, 0);
            this.tabPages.SelectedIndex = 0;
            this.tabPages.ShowToolTips = true;
            this.tabPages.Size = new System.Drawing.Size(409, 480);
            this.tabPages.TabIndex = 2;
            // 
            // tabPage1
            // 
            this.tabPage1.BackColor = System.Drawing.SystemColors.ControlDarkDark;
            this.tabPage1.BackgroundImage = global::AionQuests.Properties.Resources.dialogBackground;
            this.tabPage1.Controls.Add(this.tableLayoutPanel1);
            this.tabPage1.ForeColor = System.Drawing.SystemColors.Info;
            this.tabPage1.Location = new System.Drawing.Point(4, 27);
            this.tabPage1.Margin = new System.Windows.Forms.Padding(0);
            this.tabPage1.Name = "tabPage1";
            this.tabPage1.Size = new System.Drawing.Size(401, 449);
            this.tabPage1.TabIndex = 0;
            this.tabPage1.Text = "tabPage1";
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.BackColor = System.Drawing.Color.Transparent;
            this.tableLayoutPanel1.ColumnCount = 1;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel1.Controls.Add(this.tableLayoutPanel2, 0, 1);
            this.tableLayoutPanel1.Controls.Add(this.transparentRichTextBox1, 0, 0);
            this.tableLayoutPanel1.Location = new System.Drawing.Point(31, 23);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 2;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 76.05985F));
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 23.94015F));
            this.tableLayoutPanel1.Size = new System.Drawing.Size(339, 401);
            this.tableLayoutPanel1.TabIndex = 0;
            // 
            // tableLayoutPanel2
            // 
            this.tableLayoutPanel2.ColumnCount = 1;
            this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel2.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel2.Location = new System.Drawing.Point(0, 305);
            this.tableLayoutPanel2.Margin = new System.Windows.Forms.Padding(0);
            this.tableLayoutPanel2.Name = "tableLayoutPanel2";
            this.tableLayoutPanel2.RowCount = 4;
            this.tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 25F));
            this.tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 25F));
            this.tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 25F));
            this.tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 25F));
            this.tableLayoutPanel2.Size = new System.Drawing.Size(339, 96);
            this.tableLayoutPanel2.TabIndex = 0;
            // 
            // transparentRichTextBox1
            // 
            this.transparentRichTextBox1.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.transparentRichTextBox1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.transparentRichTextBox1.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F);
            this.transparentRichTextBox1.Location = new System.Drawing.Point(0, 0);
            this.transparentRichTextBox1.Margin = new System.Windows.Forms.Padding(0);
            this.transparentRichTextBox1.Name = "transparentRichTextBox1";
            this.transparentRichTextBox1.ReadOnly = true;
            this.transparentRichTextBox1.ScrollBars = System.Windows.Forms.RichTextBoxScrollBars.None;
            this.transparentRichTextBox1.Size = new System.Drawing.Size(339, 305);
            this.transparentRichTextBox1.TabIndex = 1;
            this.transparentRichTextBox1.Text = "";
            // 
            // StepPage
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.BackColor = System.Drawing.SystemColors.ControlDarkDark;
            this.Controls.Add(this.flowPanel);
            this.DoubleBuffered = true;
            this.Name = "StepPage";
            this.Size = new System.Drawing.Size(483, 551);
            this.flowPanel.ResumeLayout(false);
            this.flowPanel.PerformLayout();
            this.tabPages.ResumeLayout(false);
            this.tabPage1.ResumeLayout(false);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TableLayoutPanel tableItems;
        private System.Windows.Forms.FlowLayoutPanel flowPanel;
        private System.Windows.Forms.Label lblTask;
        private FlatTabControl tabPages;
        private System.Windows.Forms.TabPage tabPage1;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel2;
        private TransparentRichTextBox transparentRichTextBox1;
    }
}
