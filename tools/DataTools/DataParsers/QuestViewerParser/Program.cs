using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;

namespace AionQuests
{
    static class Program
    {
        public static IniReader IniReader = new IniReader();

        [STAThread]
        static void Main(string[] args) {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            string option = null;
            if (args.Length == 1) {
                string race = args[0].ToLower();
                if (race == "asmodian" || race == "elyo")
                    option = race;
            }
            Application.Run(new QuestsForm(option));
        }
    }
}
