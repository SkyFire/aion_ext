using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.ComponentModel;
using System.Drawing;

namespace AionQuests
{
    class SizeableTextBox : TextBox
    {
        const int EM_GETLINECOUNT = 0xBA;
        const int EM_LINEINDEX = 0xBB;
        const int EM_LINELENGTH = 0xC1;
        const int WS_EX_CLIENTEDGE = unchecked((int)0x00000200);
        const int WS_BORDER = unchecked((int)0x00800000);
        const int ES_MULTILINE = unchecked((int)0x0004);

        public SizeableTextBox() {
            base.SetStyle(ControlStyles.DoubleBuffer, true);
            base.SetStyle(ControlStyles.SupportsTransparentBackColor, true);
            InitializeComponent();
        }

        protected override CreateParams CreateParams {
            get {
                CreateParams cp = base.CreateParams;
                cp.ExStyle &= (~WS_EX_CLIENTEDGE);
                cp.Style &= (~WS_BORDER);
                switch (borderStyle) {
                    case BorderStyle.Fixed3D:
                        cp.ExStyle |= WS_EX_CLIENTEDGE;
                        break;
                    case BorderStyle.FixedSingle:
                        cp.Style |= WS_BORDER;
                        break;
                }
                cp.Style |= ES_MULTILINE;
                return cp;
            }
        }

        BorderStyle borderStyle;

        public new BorderStyle BorderStyle {
            get {
                return borderStyle;
            }

            set {
                if (borderStyle != value) {
                    if (!Enum.IsDefined(typeof(BorderStyle), value)) {
                        throw new InvalidEnumArgumentException("value", (int)value, typeof(BorderStyle));
                    }
                    borderStyle = value;
                    UpdateStyles();
                }
            }
        }
	
        [Browsable(true)]
        [EditorBrowsable(EditorBrowsableState.Always)]
        [DefaultValue(true)]
        [DesignerSerializationVisibility(DesignerSerializationVisibility.Visible)]
        public override bool AutoSize {
            get {
                return base.AutoSize;
            }
            set {
                base.AutoSize = value;
            }
        }

        public int LineCount {
            get {
                Message msg = Message.Create(this.Handle, EM_GETLINECOUNT, IntPtr.Zero, IntPtr.Zero);
                base.DefWndProc(ref msg);
                if (String.IsNullOrEmpty(this.Text))
                    return 0;
                return msg.Result.ToInt32();
            }
        }

        public int LineIndex(int Index) {
            Message msg = Message.Create(this.Handle, EM_LINEINDEX, (IntPtr)Index, IntPtr.Zero);
            base.DefWndProc(ref msg);
            return msg.Result.ToInt32();
        }

        public int LineLength(int Index) {
            Message msg = Message.Create(this.Handle, EM_LINELENGTH, (IntPtr)Index, IntPtr.Zero);
            base.DefWndProc(ref msg);
            return msg.Result.ToInt32();
        }

        private void InitializeComponent() {
            this.SuspendLayout();
            // 
            // SizeableTextBox
            // 
            this.Multiline = true;
            this.ResumeLayout(false);

        }
    }
}
