using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Jamie.Skills
{
    class StatData
    {
        public string Name { get; private set; }
        public string Var { get; private set; }

        public StatData(string name, string var) {
            this.Name = name;
            this.Var = var;
        }
    }
}
