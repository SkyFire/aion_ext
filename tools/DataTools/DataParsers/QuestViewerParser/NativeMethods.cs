using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.InteropServices;

namespace AionQuests
{
    #region NativeMethods

    internal sealed class NativeMethods
    {
        [DllImport("kernel32.dll", EntryPoint = "GetPrivateProfileStringW",
                    SetLastError = true, CharSet = CharSet.Unicode, ExactSpelling = true,
                    CallingConvention = CallingConvention.StdCall)]
        public static extern int GetPrivateProfileString(string lpAppName, string lpKeyName, string lpDefault,
                                                         string lpReturnString, int nSize, string lpFilename);

        [DllImport("kernel32.dll", EntryPoint = "WritePrivateProfileStringW",
                    SetLastError = true, CharSet = CharSet.Unicode, ExactSpelling = true,
                    CallingConvention = CallingConvention.StdCall)]
        public static extern int WritePrivateProfileString(string lpAppName, string lpKeyName, string lpString,
                                                           string lpFilename);
    }

    #endregion
}
