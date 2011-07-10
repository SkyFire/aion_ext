using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;

namespace AionQuests
{
    [Serializable]
    public class QuestDictionary : Dictionary<int, QuestFile>
    {
        public QuestDictionary() : base() { }

        public QuestDictionary(SerializationInfo si, StreamingContext sc) : base(si, sc) {
        }
    }
}
