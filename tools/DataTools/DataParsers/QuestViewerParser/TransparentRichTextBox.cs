using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace AionQuests
{
    class TransparentRichTextBox : RichTextBox
    {
        public TransparentRichTextBox() {
            base.ScrollBars = RichTextBoxScrollBars.None;
            // this.MouseWheel += new MouseEventHandler(OnMouseWheel);
        }

        override protected CreateParams CreateParams {
            get {
                CreateParams cp = base.CreateParams;
                cp.ExStyle |= 0x20;
                return cp;
            }
        }

        override protected void OnPaintBackground(PaintEventArgs e) {
        }

        //int currentDelta = 0;

        //void OnMouseWheel(object sender, MouseEventArgs args) {
        //    currentDelta += args.Delta;
        //    float linesToMove = (currentDelta * ((float)SystemInformation.MouseWheelScrollLines / 120));
        //    float zoom = this.ZoomFactor + 1f / linesToMove;
        //    if (zoom > 3) {
        //        zoom = 3;
        //    } else if (zoom < 1) {
        //        zoom = 1;
        //        currentDelta = 0;
        //    }
        //    this.ZoomFactor = zoom;
        //    this.Invalidate(true);
        //}
    }
}
