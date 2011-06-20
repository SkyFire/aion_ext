using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using Microsoft.Win32;

namespace aionXemu_Launcher
{
    public partial class Form3 : Form
    {
        public Form3()
        {
            InitializeComponent();
        }
        private void button2_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            string regpath = "SOFTWARE\\aionXemu";
            try
            {
                RegistryKey regKey = Registry.CurrentUser.CreateSubKey(regpath);
                regKey.SetValue("AIONPath",
                  this.textBox1.Text);
                regKey.SetValue("AIONParams",
                  this.textBox2.Text);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            this.Close();
        }

        private void Form4_Load(object sender, EventArgs e)
        {
            string regpath = "SOFTWARE\\aionXemu";
            try
            {
                RegistryKey regKey = Registry.CurrentUser.CreateSubKey(regpath);
                if (regKey.GetValue("AIONPath") != null)
                {
                    string aionPath =
                      regKey.GetValue(
                      "AIONPath").ToString();
                    this.textBox1.Text = aionPath;
                }
                if (regKey.GetValue("AIONParams") != null)
                {
                    string aionParams =
                      regKey.GetValue(
                      "AIONParams").ToString();
                    this.textBox2.Text = aionParams;
                }
                else
                {
                    this.textBox2.Text = "-cc:1 -noweb -noauthgg -lang:enu";
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void button3_Click(object sender, EventArgs e)
        {
            this.openFileDialog1.ShowDialog();
            this.textBox1.Text = openFileDialog1.FileName;
        }
    }
}
