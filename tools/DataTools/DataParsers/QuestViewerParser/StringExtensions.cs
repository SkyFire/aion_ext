using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AionQuests
{
    static class StringExtensions
    {
	    public static bool Contains(this string original, string value, StringComparison comparisionType)
	    {
	        return original.IndexOf(value, comparisionType) >= 0;
	    }
    }
}
