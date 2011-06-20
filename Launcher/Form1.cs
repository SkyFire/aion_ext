using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Net;
using System.Text;
using System.Windows.Forms;
using Microsoft.Win32;
using System.IO;
using System.Xml;
using System.Threading;
using System.Diagnostics;

namespace aionXemu_Launcher
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            Form2 About = new Form2();
            About.Owner = this;
            About.Show();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            string regpath = "SOFTWARE\\aionXemu";
            try
            {
                RegistryKey regKey = Registry.CurrentUser.CreateSubKey(regpath);
                string aiDir = Convert.ToString(regKey.GetValue("AIONPath"));
                string aiParams = Convert.ToString(regKey.GetValue("AIONParams"));
                string AIPath = regKey.GetValue("AIONPath").ToString();
                string corAIPath = AIPath.Replace("bin32\\aion.bin", string.Empty);
                if (File.Exists(corAIPath + "/bin32/aion.bin"))
                {
                    this.process1 = new System.Diagnostics.Process();
                    this.process1.StartInfo.Arguments = @"/c start bin32/aion.bin -ip:your.ip.in.here -port:2106 " + aiParams;
                    this.process1.StartInfo.CreateNoWindow = true;
                    this.process1.StartInfo.FileName = "cmd";
                    this.process1.StartInfo.WorkingDirectory = corAIPath;
                    this.process1.StartInfo.UseShellExecute = false;
                    this.process1.Start();
                    this.Close();
                }
                else
                {
                    Form3 Settings = new Form3();
                    Settings.Owner = this;
                    Settings.Show();
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            string regpath = "SOFTWARE\\aionXemu";
            try
            {
                RegistryKey regKey = Registry.CurrentUser.CreateSubKey(regpath);
                if (regKey.GetValue("AIONPath") == null)
                {
                    Form3 Settings = new Form3();
                    Settings.Owner = this;
                    Settings.Show();
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void button3_Click(object sender, EventArgs e)
        {
            Form3 Settings = new Form3();
            Settings.Owner = this;
            Settings.Show();
        }
    }
}
