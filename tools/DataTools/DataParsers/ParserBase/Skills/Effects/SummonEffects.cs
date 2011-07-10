namespace Jamie.Skills
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel;
    using System.Diagnostics;
    using System.Linq;
    using System.Reflection;
    using System.Xml.Serialization;
    using Jamie.ParserBase;
    using System.Text;

    [XmlInclude(typeof(SummonServantEffect))]
    [XmlInclude(typeof(SummonGroupGateEffect))]
    [XmlInclude(typeof(SummonTrapEffect))]
    [XmlInclude(typeof(SummonEffect))]
    [XmlInclude(typeof(SummonTotemEffect))]
    [XmlInclude(typeof(SummonSkillAreaEffect))]
    [Serializable]
    public abstract class AbstractSummonEffect : Effect
    {
        [XmlAttribute]
        [DefaultValue(0)]
        public int npc_id;

        [XmlAttribute]
        [DefaultValue(0)]
        public int time;
    }

    [Serializable]
    public sealed class SummonEffect : AbstractSummonEffect
    {
        public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
            base.Import(importObject, getters);

            if (importObject.reserved[3] != null)
                this.time = Int32.Parse(importObject.reserved[3].Trim());
            this.npc_id = Utility.ClientNpcIndex[importObject.reserved[8].Trim()];
        }
    }

    [Serializable]
    public class SummonServantEffect : AbstractSummonEffect
    {
        [XmlAttribute]
        public int skill_id;

        [XmlAttribute]
        [DefaultValue(0)]
        public int hp_ratio;

        [XmlAttribute]
        [DefaultValue(0)]
        public int npc_count;

        [XmlAttribute]
        [DefaultValue(0)]
        public int attack_count;

        public SummonServantEffect() {
        }

        public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
            base.Import(importObject, getters);

            // count is always equals 1 for summonservant effect and 
            // differs for summon homing
            bool isSummonHoming = importObject.reserved[4] != null;

            if (isSummonHoming) {
                this.npc_count = Int32.Parse(importObject.reserved[4].Trim());
                this.attack_count = Int32.Parse(importObject.reserved[3].Trim());
            } else
                this.hp_ratio = Int32.Parse(importObject.reserved[3].Trim());

            string skillName = importObject.reserved[8].Trim();
            this.npc_id = Utility.ClientNpcIndex[skillName];

            string[] nameParts = skillName.Split('_');
            if (nameParts[nameParts.Length - 1] == "NPC" ||
                nameParts[nameParts.Length - 1] == "N") {
                //if (nameParts.Contains("Slave"))
                //    skillName = String.Format("{0}_{1}_{2}_{3}_{4}",
                //                                             nameParts[nameParts.Length - 6],
                //                                             nameParts[nameParts.Length - 5],
                //                                             nameParts[nameParts.Length - 4],
                //                                             nameParts[nameParts.Length - 3],
                //                                             nameParts[nameParts.Length - 2]);
                //else
                    skillName = String.Format("{0}_{1}", nameParts[nameParts.Length - 3],
                                                         nameParts[nameParts.Length - 2]);
            } else {
                skillName = String.Format("{0}_{1}", nameParts[nameParts.Length - 2],
                                                     nameParts[nameParts.Length - 1]);
            }

            ClientSkill skill = Utility.SkillIndex[skillName];
            if (skill == null) {
                string origName = isSummonHoming ? "summonhoming" : "summonservant";
                Debug.Print("Unknown skill for the {0}: {1} (skillId={2})", origName, skillName,
                            importObject.Skill.id);
            } else {
                skill_id = skill.id;
            }
        }
    }

    [Serializable]
    public sealed class SummonGroupGateEffect : AbstractSummonEffect
    {
        public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
            base.Import(importObject, getters);

            this.time = Int32.Parse(importObject.reserved[1].Trim());
            this.npc_id = Utility.ClientNpcIndex[importObject.reserved[8].Trim()];
        }
    }

    [Serializable]
    public class SummonTrapEffect : AbstractSummonEffect
    {
        [XmlAttribute]
        [DefaultValue(0)]
        public int skill_id;

        public override void Import(ClientEffect importObject, IEnumerable<FieldInfo> getters) {
            base.Import(importObject, getters);

            string skillOrItem = importObject.reserved[8].Trim();

            string[] nameParts = skillOrItem.Split('_');
            if (nameParts[nameParts.Length - 1].StartsWith("Q")) {
                // do nothing, since the item summons NPC with it's own skill
            } else if (nameParts[nameParts.Length - 1] == "NPC") {
                this.npc_id = Utility.ClientNpcIndex[skillOrItem];

                string skillName;
                if (nameParts.Contains("Slave") || nameParts.Contains("CallSlave")) {
                    skillName = String.Format("{0}_{1}_{2}", nameParts[nameParts.Length - 4],
                                                             nameParts[nameParts.Length - 3],
                                                             nameParts[nameParts.Length - 2]);
                } else {
                    skillName = String.Format("{0}_{1}", nameParts[nameParts.Length - 3],
                                                         nameParts[nameParts.Length - 2]);
                }
                ClientSkill skill = Utility.SkillIndex[skillName];
                if (skill == null) {
                    // drop last NPC suffix
                    skillName = String.Join("_", nameParts, 0, nameParts.Length - 1);
                    skill = Utility.SkillIndex[skillName];
                    if (skill == null)
                        Debug.Print("Unknown skill for the {0}: {1} (skillId={2})", this.GetType().Name,
                            skillName, importObject.Skill.id);
                    else
                        skill_id = skill.id;
                } else {
                    skill_id = skill.id;
                }
            } else {
                this.npc_id = Utility.ClientNpcIndex[skillOrItem];
                ClientSkill skill = Utility.SkillIndex[skillOrItem];
                if (skill == null) {
                    if (nameParts.Contains("ExplosionTrap")) {
                        skill = Utility.SkillIndex["ExplosionTrap_G1"];
                        skill_id = skill.id;
                    } else if (nameParts.Contains("FlameShower")) {
                        skill = Utility.SkillIndex["Stigma_" + skillOrItem];
                        skill_id = skill.id;
                    } else {
                        Debug.Print("Unknown skill for the {0}: {1} (skillId={2})", this.GetType().Name,
                            skillOrItem, importObject.Skill.id);
                    }
                } else {
                    skill_id = skill.id;
                }
            }

            this.time = Int32.Parse(importObject.reserved[3].Trim());
        }
    }

    [Serializable]
    public sealed class SummonTotemEffect : SummonTrapEffect
    {
    }

    [Serializable]
    public sealed class SummonSkillAreaEffect : SummonTrapEffect
    {
    }

    [Serializable]
    public sealed class SummonHomingEffect : SummonServantEffect
    {

    }
}
