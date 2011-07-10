namespace AionQuests
{
    partial class ItemDetails
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
            this.lblTitle = new System.Windows.Forms.Label();
            this.txtDescription = new AionQuests.SizeableTextBox();
            this.lblItemId = new System.Windows.Forms.Label();
            this.picBox = new Uvs.PosApp.TransparentPictureBox();
            this.checkRandom = new System.Windows.Forms.CheckBox();
            this.lblLevel = new System.Windows.Forms.Label();
            this.cboLevel = new System.Windows.Forms.ComboBox();
            this.btnRandomize = new System.Windows.Forms.Button();
            this.lblNote = new System.Windows.Forms.Label();
            this.lblPrice = new System.Windows.Forms.Label();
            this.lblItemLevel = new System.Windows.Forms.Label();
            this.chkCanExchange = new System.Windows.Forms.CheckBox();
            this.chkCanSell = new System.Windows.Forms.CheckBox();
            this.chkPlayerWerehouse = new System.Windows.Forms.CheckBox();
            this.chkAccountWerehouse = new System.Windows.Forms.CheckBox();
            this.chkLegionWerehouse = new System.Windows.Forms.CheckBox();
            this.chkBreakable = new System.Windows.Forms.CheckBox();
            this.chkSoulBind = new System.Windows.Forms.CheckBox();
            this.chkLogOutRemove = new System.Windows.Forms.CheckBox();
            this.chkCanSplit = new System.Windows.Forms.CheckBox();
            this.chkCanEnchant = new System.Windows.Forms.CheckBox();
            ((System.ComponentModel.ISupportInitialize)(this.picBox)).BeginInit();
            this.SuspendLayout();
            // 
            // lblTitle
            // 
            this.lblTitle.AutoSize = true;
            this.lblTitle.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.lblTitle.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(255)))), ((int)(((byte)(255)))), ((int)(((byte)(192)))));
            this.lblTitle.Location = new System.Drawing.Point(12, 12);
            this.lblTitle.MaximumSize = new System.Drawing.Size(276, 0);
            this.lblTitle.Name = "lblTitle";
            this.lblTitle.Size = new System.Drawing.Size(39, 16);
            this.lblTitle.TabIndex = 0;
            this.lblTitle.Text = "Title";
            // 
            // txtDescription
            // 
            this.txtDescription.BackColor = System.Drawing.SystemColors.ControlDarkDark;
            this.txtDescription.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.txtDescription.ForeColor = System.Drawing.SystemColors.Info;
            this.txtDescription.Location = new System.Drawing.Point(74, 53);
            this.txtDescription.Multiline = true;
            this.txtDescription.Name = "txtDescription";
            this.txtDescription.Size = new System.Drawing.Size(208, 54);
            this.txtDescription.TabIndex = 1;
            this.txtDescription.TabStop = false;
            // 
            // lblItemId
            // 
            this.lblItemId.AutoSize = true;
            this.lblItemId.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.lblItemId.Location = new System.Drawing.Point(12, 124);
            this.lblItemId.Name = "lblItemId";
            this.lblItemId.Size = new System.Drawing.Size(50, 16);
            this.lblItemId.TabIndex = 2;
            this.lblItemId.Text = "Item Id:";
            // 
            // picBox
            // 
            this.picBox.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.picBox.Location = new System.Drawing.Point(15, 59);
            this.picBox.Name = "picBox";
            this.picBox.Size = new System.Drawing.Size(40, 40);
            this.picBox.TabIndex = 3;
            this.picBox.TabStop = false;
            // 
            // checkRandom
            // 
            this.checkRandom.AutoCheck = false;
            this.checkRandom.AutoSize = true;
            this.checkRandom.Location = new System.Drawing.Point(15, 159);
            this.checkRandom.Name = "checkRandom";
            this.checkRandom.Size = new System.Drawing.Size(83, 17);
            this.checkRandom.TabIndex = 4;
            this.checkRandom.Text = "Is Random?";
            this.checkRandom.UseVisualStyleBackColor = true;
            // 
            // lblLevel
            // 
            this.lblLevel.AutoSize = true;
            this.lblLevel.Location = new System.Drawing.Point(105, 160);
            this.lblLevel.Name = "lblLevel";
            this.lblLevel.Size = new System.Drawing.Size(36, 13);
            this.lblLevel.TabIndex = 5;
            this.lblLevel.Text = "Level:";
            // 
            // cboLevel
            // 
            this.cboLevel.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cboLevel.FormattingEnabled = true;
            this.cboLevel.Location = new System.Drawing.Point(142, 157);
            this.cboLevel.Name = "cboLevel";
            this.cboLevel.Size = new System.Drawing.Size(54, 21);
            this.cboLevel.TabIndex = 6;
            // 
            // btnRandomize
            // 
            this.btnRandomize.BackColor = System.Drawing.Color.Khaki;
            this.btnRandomize.ForeColor = System.Drawing.SystemColors.ControlText;
            this.btnRandomize.Location = new System.Drawing.Point(210, 155);
            this.btnRandomize.Name = "btnRandomize";
            this.btnRandomize.Size = new System.Drawing.Size(75, 23);
            this.btnRandomize.TabIndex = 7;
            this.btnRandomize.Text = "Randomize";
            this.btnRandomize.UseVisualStyleBackColor = false;
            this.btnRandomize.Click += new System.EventHandler(this.OnRandomize);
            // 
            // lblNote
            // 
            this.lblNote.AutoSize = true;
            this.lblNote.Location = new System.Drawing.Point(27, 183);
            this.lblNote.Name = "lblNote";
            this.lblNote.Size = new System.Drawing.Size(245, 13);
            this.lblNote.TabIndex = 8;
            this.lblNote.Text = "(Note: It\'s a demo, the exact algorithm is unknown)";
            // 
            // lblPrice
            // 
            this.lblPrice.AutoSize = true;
            this.lblPrice.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.lblPrice.Location = new System.Drawing.Point(12, 211);
            this.lblPrice.Name = "lblPrice";
            this.lblPrice.Size = new System.Drawing.Size(42, 16);
            this.lblPrice.TabIndex = 9;
            this.lblPrice.Text = "Price:";
            // 
            // lblItemLevel
            // 
            this.lblItemLevel.AutoSize = true;
            this.lblItemLevel.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.lblItemLevel.Location = new System.Drawing.Point(139, 211);
            this.lblItemLevel.Name = "lblItemLevel";
            this.lblItemLevel.Size = new System.Drawing.Size(72, 16);
            this.lblItemLevel.TabIndex = 10;
            this.lblItemLevel.Text = "Item Level:";
            // 
            // chkCanExchange
            // 
            this.chkCanExchange.AutoCheck = false;
            this.chkCanExchange.AutoSize = true;
            this.chkCanExchange.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.chkCanExchange.Location = new System.Drawing.Point(15, 246);
            this.chkCanExchange.Name = "chkCanExchange";
            this.chkCanExchange.Size = new System.Drawing.Size(121, 20);
            this.chkCanExchange.TabIndex = 11;
            this.chkCanExchange.Text = "Can Exchange?";
            this.chkCanExchange.UseVisualStyleBackColor = true;
            // 
            // chkCanSell
            // 
            this.chkCanSell.AutoCheck = false;
            this.chkCanSell.AutoSize = true;
            this.chkCanSell.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.chkCanSell.Location = new System.Drawing.Point(15, 272);
            this.chkCanSell.Name = "chkCanSell";
            this.chkCanSell.Size = new System.Drawing.Size(129, 20);
            this.chkCanSell.TabIndex = 12;
            this.chkCanSell.Text = "Can Sell to NPC?";
            this.chkCanSell.UseVisualStyleBackColor = true;
            // 
            // chkPlayerWerehouse
            // 
            this.chkPlayerWerehouse.AutoCheck = false;
            this.chkPlayerWerehouse.AutoSize = true;
            this.chkPlayerWerehouse.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.chkPlayerWerehouse.Location = new System.Drawing.Point(15, 298);
            this.chkPlayerWerehouse.Name = "chkPlayerWerehouse";
            this.chkPlayerWerehouse.Size = new System.Drawing.Size(218, 20);
            this.chkPlayerWerehouse.TabIndex = 13;
            this.chkPlayerWerehouse.Text = "Can store in player Warehouse?";
            this.chkPlayerWerehouse.UseVisualStyleBackColor = true;
            // 
            // chkAccountWerehouse
            // 
            this.chkAccountWerehouse.AutoCheck = false;
            this.chkAccountWerehouse.AutoSize = true;
            this.chkAccountWerehouse.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.chkAccountWerehouse.Location = new System.Drawing.Point(15, 324);
            this.chkAccountWerehouse.Name = "chkAccountWerehouse";
            this.chkAccountWerehouse.Size = new System.Drawing.Size(227, 20);
            this.chkAccountWerehouse.TabIndex = 14;
            this.chkAccountWerehouse.Text = "Can store in account Warehouse?";
            this.chkAccountWerehouse.UseVisualStyleBackColor = true;
            // 
            // chkLegionWerehouse
            // 
            this.chkLegionWerehouse.AutoCheck = false;
            this.chkLegionWerehouse.AutoSize = true;
            this.chkLegionWerehouse.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.chkLegionWerehouse.Location = new System.Drawing.Point(15, 350);
            this.chkLegionWerehouse.Name = "chkLegionWerehouse";
            this.chkLegionWerehouse.Size = new System.Drawing.Size(217, 20);
            this.chkLegionWerehouse.TabIndex = 15;
            this.chkLegionWerehouse.Text = "Can store in legion Warehouse?";
            this.chkLegionWerehouse.UseVisualStyleBackColor = true;
            // 
            // chkBreakable
            // 
            this.chkBreakable.AutoCheck = false;
            this.chkBreakable.AutoSize = true;
            this.chkBreakable.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.chkBreakable.Location = new System.Drawing.Point(16, 376);
            this.chkBreakable.Name = "chkBreakable";
            this.chkBreakable.Size = new System.Drawing.Size(97, 20);
            this.chkBreakable.TabIndex = 16;
            this.chkBreakable.Text = "Breakable?";
            this.chkBreakable.UseVisualStyleBackColor = true;
            // 
            // chkSoulBind
            // 
            this.chkSoulBind.AutoCheck = false;
            this.chkSoulBind.AutoSize = true;
            this.chkSoulBind.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.chkSoulBind.Location = new System.Drawing.Point(15, 402);
            this.chkSoulBind.Name = "chkSoulBind";
            this.chkSoulBind.Size = new System.Drawing.Size(115, 20);
            this.chkSoulBind.TabIndex = 17;
            this.chkSoulBind.Text = "Can soul bind?";
            this.chkSoulBind.UseVisualStyleBackColor = true;
            // 
            // chkLogOutRemove
            // 
            this.chkLogOutRemove.AutoCheck = false;
            this.chkLogOutRemove.AutoSize = true;
            this.chkLogOutRemove.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.chkLogOutRemove.Location = new System.Drawing.Point(16, 428);
            this.chkLogOutRemove.Name = "chkLogOutRemove";
            this.chkLogOutRemove.Size = new System.Drawing.Size(195, 20);
            this.chkLogOutRemove.TabIndex = 18;
            this.chkLogOutRemove.Text = "Removed when logged out?";
            this.chkLogOutRemove.UseVisualStyleBackColor = true;
            // 
            // chkCanSplit
            // 
            this.chkCanSplit.AutoCheck = false;
            this.chkCanSplit.AutoSize = true;
            this.chkCanSplit.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.chkCanSplit.Location = new System.Drawing.Point(16, 454);
            this.chkCanSplit.Name = "chkCanSplit";
            this.chkCanSplit.Size = new System.Drawing.Size(116, 20);
            this.chkCanSplit.TabIndex = 19;
            this.chkCanSplit.Text = "Can split slots?";
            this.chkCanSplit.UseVisualStyleBackColor = true;
            // 
            // chkCanEnchant
            // 
            this.chkCanEnchant.AutoCheck = false;
            this.chkCanEnchant.AutoSize = true;
            this.chkCanEnchant.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(186)));
            this.chkCanEnchant.Location = new System.Drawing.Point(16, 480);
            this.chkCanEnchant.Name = "chkCanEnchant";
            this.chkCanEnchant.Size = new System.Drawing.Size(108, 20);
            this.chkCanEnchant.TabIndex = 20;
            this.chkCanEnchant.Text = "Can enchant?";
            this.chkCanEnchant.UseVisualStyleBackColor = true;
            // 
            // ItemDetails
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoValidate = System.Windows.Forms.AutoValidate.Disable;
            this.BackColor = System.Drawing.SystemColors.ControlDarkDark;
            this.ClientSize = new System.Drawing.Size(294, 698);
            this.Controls.Add(this.chkCanEnchant);
            this.Controls.Add(this.chkCanSplit);
            this.Controls.Add(this.chkLogOutRemove);
            this.Controls.Add(this.chkSoulBind);
            this.Controls.Add(this.chkBreakable);
            this.Controls.Add(this.chkLegionWerehouse);
            this.Controls.Add(this.chkAccountWerehouse);
            this.Controls.Add(this.chkPlayerWerehouse);
            this.Controls.Add(this.chkCanSell);
            this.Controls.Add(this.chkCanExchange);
            this.Controls.Add(this.lblItemLevel);
            this.Controls.Add(this.lblPrice);
            this.Controls.Add(this.lblNote);
            this.Controls.Add(this.btnRandomize);
            this.Controls.Add(this.cboLevel);
            this.Controls.Add(this.lblLevel);
            this.Controls.Add(this.checkRandom);
            this.Controls.Add(this.picBox);
            this.Controls.Add(this.lblItemId);
            this.Controls.Add(this.txtDescription);
            this.Controls.Add(this.lblTitle);
            this.DoubleBuffered = true;
            this.ForeColor = System.Drawing.SystemColors.Info;
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "ItemDetails";
            this.ShowIcon = false;
            this.ShowInTaskbar = false;
            this.SizeGripStyle = System.Windows.Forms.SizeGripStyle.Hide;
            this.StartPosition = System.Windows.Forms.FormStartPosition.Manual;
            this.Load += new System.EventHandler(this.OnLoad);
            ((System.ComponentModel.ISupportInitialize)(this.picBox)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label lblTitle;
        private SizeableTextBox txtDescription;
        private System.Windows.Forms.Label lblItemId;
        private Uvs.PosApp.TransparentPictureBox picBox;
        private System.Windows.Forms.CheckBox checkRandom;
        private System.Windows.Forms.Label lblLevel;
        private System.Windows.Forms.ComboBox cboLevel;
        private System.Windows.Forms.Button btnRandomize;
        private System.Windows.Forms.Label lblNote;
        private System.Windows.Forms.Label lblPrice;
        private System.Windows.Forms.Label lblItemLevel;
        private System.Windows.Forms.CheckBox chkCanExchange;
        private System.Windows.Forms.CheckBox chkCanSell;
        private System.Windows.Forms.CheckBox chkPlayerWerehouse;
        private System.Windows.Forms.CheckBox chkAccountWerehouse;
        private System.Windows.Forms.CheckBox chkLegionWerehouse;
        private System.Windows.Forms.CheckBox chkBreakable;
        private System.Windows.Forms.CheckBox chkSoulBind;
        private System.Windows.Forms.CheckBox chkLogOutRemove;
        private System.Windows.Forms.CheckBox chkCanSplit;
        private System.Windows.Forms.CheckBox chkCanEnchant;
    }
}