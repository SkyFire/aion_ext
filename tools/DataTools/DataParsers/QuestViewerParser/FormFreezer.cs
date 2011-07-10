using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Drawing;
using System.Runtime.InteropServices;

namespace AionQuests
{
    public class FormFreezer : IDisposable
    {

        // The form being frozen
        Form form = null;

        // the auxiliary PictureBox that will cover the form
        PictureBox pictureBox = null;

        [DllImport("gdi32.dll")]
        static extern bool DeleteObject(IntPtr hObject);

        IntPtr hBitmap = IntPtr.Zero;

        // the number of times the Freeze method has been called
        int FreezeCount = 0;

        // create an instance associated with a given form
        // and freeze the form in base of flag freezeIt

        public FormFreezer(Form form, bool freezeIt) {
            this.form = form;
            if (freezeIt) this.Freeze();
        }

        // freeze the form 
        public void Freeze() {
            // Remember we have frozen the form once more
            // Do nothing if it was already frozen
            if (++FreezeCount > 1)
                return;

            Rectangle rect = form.ClientRectangle;
            if (rect.IsEmpty || form.WindowState == FormWindowState.Minimized)
                return;

            Point topLeft = form.PointToScreen(new Point(rect.Left, rect.Top));

            // Create a PictureBox that resizes with its contents
            pictureBox = new PictureBox();
            pictureBox.SizeMode = PictureBoxSizeMode.AutoSize;

            // create a bitmap as large as the form's client area and with same color depth
            using (Graphics frmGraphics = form.CreateGraphics()) {
                Bitmap bitmap = new Bitmap(rect.Width, rect.Height, frmGraphics);
                hBitmap = bitmap.GetHbitmap();
                pictureBox.Image = Image.FromHbitmap(hBitmap);
            }

            // copy the screen contents, from the form's client area to the hidden bitmap
            using (Graphics picGraphics = Graphics.FromImage(pictureBox.Image)) {
                picGraphics.CopyFromScreen(topLeft, Point.Empty, rect.Size, CopyPixelOperation.SourceCopy);
            }

            // Display the bitmap in the picture box, and show the picture box in front of all other controls
            form.Controls.Add(pictureBox);
            pictureBox.BringToFront();
        }

        // unfreeze the form
        // Note: calls to Freeze and Unfreeze must be balanced, unless force=true
        public void Unfreeze(bool force) {
            // exit if nothing to unfreeze
            if (FreezeCount == 0)
                return;

            // remember we've unfrozen the form, but exit if it is still frozen
            FreezeCount -= 1;

            // force the unfreeze if so required
            if (force)
                FreezeCount = 0;

            if (FreezeCount > 0)
                return;

            // remove the picture box control and clean up
            if (pictureBox != null) {
                pictureBox.Controls.Remove(pictureBox);
                if (pictureBox.Image != null) {
                    pictureBox.Image.Dispose();
                    DeleteObject(hBitmap);
                }
                pictureBox.Dispose();
                pictureBox = null;
            }
        }

        // return true if the form is currently frozen
        public bool IsFrozen {
            get { return (FreezeCount > 0); }
        }

        void IDisposable.Dispose() {
            this.Unfreeze(true);
        }
    }
}
