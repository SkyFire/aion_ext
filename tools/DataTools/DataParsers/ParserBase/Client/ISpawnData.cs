using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Jamie.ParserBase
{
    public interface ISpawnData
    {
        string Name { get; set; }

        string Angles { get; set; }

        string Pos { get; set; }

        int dir { get; set; }

        int use_dir { get; set; }

        int EntityId { get; set; }

        ObjectTypes Type { get; set; }
    }
}
