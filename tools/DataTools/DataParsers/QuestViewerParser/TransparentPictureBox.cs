using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Windows.Forms;
using System.ComponentModel;

namespace Uvs.PosApp
{
    public class TransparentPictureBox : PictureBox
    {
        /// <summary>
        /// Make sure to set Parent which background will be used
        /// </summary>
        public TransparentPictureBox() {
            SetStyle(ControlStyles.UserPaint, true);
            SetStyle(ControlStyles.SupportsTransparentBackColor, true);
            SetStyle(ControlStyles.AllPaintingInWmPaint, true);
            SetStyle(ControlStyles.EnableNotifyMessage, true);
        }

        const int WM_ERASEBKGND = 0x14;
        protected override void OnNotifyMessage(Message m) {
            if (m.Msg != WM_ERASEBKGND)
                base.OnNotifyMessage(m);
        }
    }
}
