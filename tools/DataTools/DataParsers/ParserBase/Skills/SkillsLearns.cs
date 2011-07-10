namespace Jamie.Skills
{
    using System.Xml.Serialization;
    using System;
    using System.Xml.Schema;
    using System.Collections.Generic;
    using System.ComponentModel;
    using System.Linq;
    using Jamie.ParserBase;

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "client_skill_learns", Namespace = "", IsNullable = false)]
    public partial class SkillsLearns
    {
        [XmlElement(ElementName = "client_skill_learn", Form = XmlSchemaForm.Unqualified)]
        public List<LearnSkill> SkillList;

        ILookup<string, LearnSkill> keyToSkills = null;

        internal void CreateIndex() {
            if (this.SkillList == null)
                return;
            keyToSkills = this.SkillList.ToLookup(i => i.skill, i => i, StringComparer.InvariantCultureIgnoreCase);
        }

        public IEnumerable<LearnSkill> this[string skillKey] {
            get {
                skillKey = skillKey.ToLower();
                if (keyToSkills == null || !keyToSkills.Contains(skillKey))
                    return Enumerable.Empty<LearnSkill>();
                return keyToSkills[skillKey];
            }
        }
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class LearnSkill
    {

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int id;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int name;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string race;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string @class;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int pc_level;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string skill;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int skill_level;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        [DefaultValue(XmlBoolTypes.FALSE)]
        public XmlBool autolearn;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public bool ui_display;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public int stigma_display;

        public LearnSkill() {
            this.autolearn = new XmlBool(false);
        }
    }
}
