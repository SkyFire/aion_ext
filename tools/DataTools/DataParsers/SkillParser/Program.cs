namespace Jamie.Skills
{
	using System;
	using System.Collections.Generic;
	using System.Diagnostics;
	using System.IO;
	using System.Linq;
	using System.Reflection;
	using System.Text;
	using System.Xml;
	using System.Xml.Serialization;
	using Jamie.ParserBase;

	class Program
    {
        static string root = AppDomain.CurrentDomain.SetupInformation.ApplicationBase;
		static Assembly effectsAssembly = Assembly.GetAssembly(typeof(Effect));

        static void Main(string[] args) {

			Utility.WriteExeDetails();
            Console.WriteLine("Loading strings...");
            Utility.LoadStrings(root);

            Console.WriteLine("Loading NPCs...");
            Utility.LoadClientNpcs(root);

            Console.WriteLine("Loading items...");
            Utility.LoadItems(root);

            Console.WriteLine("Loading skills...");
            Utility.LoadSkills(root);

            StringBuilder sb1 = new StringBuilder();
            foreach (var sk in Utility.SkillIndex.SkillList)
                sb1.AppendLine(sk.id.ToString());

            Console.WriteLine("Loading Ultra Skills...");
            Utility.LoadUltraSkills(root);

            string outputPath = Path.Combine(root, @".\output");
            if (!Directory.Exists(outputPath))
                Directory.CreateDirectory(outputPath);

            var outputFile = new SkillData();

            var settings = new XmlWriterSettings()
            {
                CheckCharacters = false,
                CloseOutput = false,
                Indent = true,
                IndentChars = "\t",
                NewLineChars = "\n",
                Encoding = new UTF8Encoding(false)
            };

            #region Pet UltraSkill parsing

            PetSkillTemplates petTemplates = new PetSkillTemplates();
            List<PetSkill> ultraSkills = new List<PetSkill>();

            foreach (var ultra in Utility.UltraSkillIndex.UltraSkillList) {
                PetSkill petSkill = new PetSkill();
                petSkill.skill_id = Utility.SkillIndex[ultra.ultra_skill].id;
                if (String.Compare("Light_Summon_MagmaElemental_G1", ultra.pet_name) == 0)
                    ultra.pet_name = "Dark_Summon_MagmaElemental_G1";
                else if (String.Compare("Dark_Summon_TempestElemental_G1", ultra.pet_name) == 0)
                    ultra.pet_name = "Light_Summon_TempestElemental_G1";
                petSkill.pet_id = Utility.ClientNpcIndex[ultra.pet_name];
                if (petSkill.pet_id == -1) {
                    petSkill.missing_pet_id = ultra.pet_name;
                    petSkill.pet_id = 0;
                }
                ClientSkill skill = Utility.SkillIndex[ultra.order_skill];
                if (skill != null)
                    petSkill.order_skill = Utility.SkillIndex[ultra.order_skill].id;
                //else
                //    petSkill.missing_order_skill = ultra.order_skill;
                ultraSkills.Add(petSkill);
            }

            petTemplates.SkillList = ultraSkills.OrderBy(s => s.skill_id).ToList();

            try {
                using (var fs = new FileStream(Path.Combine(outputPath, "pet_skills.xml"),
                                               FileMode.Create, FileAccess.Write))
                using (var writer = XmlWriter.Create(fs, settings)) {
                    XmlSerializer ser = new XmlSerializer(typeof(PetSkillTemplates));
                    ser.Serialize(writer, petTemplates);
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }

            #endregion


            var skillsetEx = new Dictionary<int, int>()
            {
                { 1566, 1564 }, { 1586, 1564 }, { 2210, 1564 }, { 1574, 1564 }, { 1575, 1564 }, { 1576, 1564 },
                { 1596, 1564 }, { 1564, 1564 }, { 1565, 1564 }, { 1590, 1564 },
                { 1554, 1554 }, { 1588, 1554 },
                { 969, 969 }, { 970, 969 }, { 971, 969 }, { 989, 969 }, { 2181, 969 }, { 1220, 969 },
                { 1221, 969 }, { 1222, 969 }, { 1267, 969 }, { 1318, 969 }, { 1319, 969 }, { 1342, 969 },
                { 2183, 969 },
                { 1105, 1105 }, { 1107, 1105 }, { 1108, 1105 }, { 1187, 1105 }, { 1112, 1105 }, { 1186, 1105 }
            };

            var delayIdOverrides = new Dictionary<int, int>()
            {
                { 11885, 11885 }, { 11886, 11885 }, { 11887, 11885 }, { 11888, 11885 }, { 11889, 11885 },
                { 11890, 11890 }, { 11891, 11890 }, { 11892, 11890 }, { 11893, 11890 }, { 11894, 11890 }
            };

            //  skills which have 0 cast time or cooldown is smaller and which exclude each other
            var delayIdsForSkills = new Dictionary<int, int>()
			{
				{ 1178, 1178 }, { 2148, 1178 }, { 1177, 1178 }, { 2147, 1178 }
			};

            #region Finding the chainDelayIdsInclude

            /*
            var diffDelays = Utility.SkillIndex.SkillList.Where(s => s.delay_id > 0 &&
                                                                s.sub_type == SkillSubType.buff &&
                                                                s.chain_category_level > 0)
                                                         .ToLookup(s => s.delay_id, s => s);
            var chainCategories = new Dictionary<string, List<ClientSkill>>();
            
            foreach (var group in diffDelays) {
                var skills = diffDelays[group.Key];
                string[] chains = skills.Select(s => s.chain_category_priority).Distinct().ToArray();
                string categoryName = chains.Where(s => s != null).FirstOrDefault();
                if (categoryName == null) {
                    chains = skills.Select(s => s.prechain_skillname).Distinct().ToArray();
                    categoryName = chains.Where(s => s != null).FirstOrDefault();
                }

                if (categoryName == null)
                    continue;

                if (chainCategories.ContainsKey(categoryName)) {
                    Debug.Print("Different delay id for chain: {0}", categoryName);
                    chainCategories[categoryName].AddRange(skills.ToList());
                } else {
                    chainCategories.Add(categoryName, skills.ToList());
                }
            }

            chainCategories = chainCategories.Where(pair => pair.Value.Count > 1)
                                             .OrderBy(p => p.Value.First().delay_id)
                                             .ToDictionary(pair => pair.Key, pair => pair.Value);

            // StringBuilder sb = new StringBuilder();
            foreach (var pair in chainCategories) {
                // sb.AppendFormat("SkillSet by delay: {0}\r\n", pair.Key);
                foreach (var skill in pair.Value) {
                    // string desc = Utility.StringIndex.GetString(skill.desc);
                    if (skill.casting_delay == 0) {
                        int dur = 0;
                        if (skill.effect1_reserved_cond1 == null ||
                            skill.effect1_reserved_cond1_prob2 == 100 &&
                            skill.effect1_reserved_cond1 == "EveryHit")
                            dur = Math.Max(dur, skill.effect1_remain2);
                        if (skill.effect2_reserved_cond1 == null || 
                            skill.effect2_reserved_cond1_prob2 == 100 &&
                            skill.effect2_reserved_cond1 == "EveryHit")
                            dur = Math.Max(dur, skill.effect2_remain2);
                        if (skill.effect3_reserved_cond1 == null || 
                            skill.effect3_reserved_cond1_prob2 == 100 &&
                            skill.effect3_reserved_cond1 == "EveryHit")
                            dur = Math.Max(dur, skill.effect3_remain2);
                        if (skill.effect4_reserved_cond1 == null || 
                            skill.effect4_reserved_cond1_prob2 == 100 &&
                            skill.effect4_reserved_cond1 == "EveryHit")
                            dur = Math.Max(dur, skill.effect4_remain2);
                        skill.casting_delay = dur;
                    }

                    if (skill.casting_delay == 0 || skill.casting_delay / 1000 > skill.delay_time / 100) {
                        sb.AppendFormat("\tSkill: id={0}; name={1}; delayId={2}; r10={3}/{4}/{5}/{6}; delay={7}; cooldown={8}\r\n",
                                        skill.id, desc,
                                        skill.delay_id, skill.effect1_reserved10, skill.effect2_reserved10,
                                        skill.effect3_reserved10, skill.effect4_reserved10,
                                        skill.casting_delay, skill.delay_time / 100);
                    }
                }
            }
            */

            #endregion

            Dictionary<ClientSkill, List<ClientEffect>> list = new Dictionary<ClientSkill, List<ClientEffect>>();
            Dictionary<string, HashSet<string>> statData = new Dictionary<string, HashSet<string>>();

            const EffectType filterEffect = EffectType.None;
            const Stat filterStat = Stat.None;
            StringBuilder sb = new StringBuilder();

            //for (var filterEffect = EffectType.None + 1;
            //     filterEffect <= EffectType.XPBoost; filterEffect++) {

            foreach (var sk in Utility.SkillIndex.SkillList) {
                var template = new SkillTemplate();
                template.skill_id = sk.id;
                template.name = sk.desc;
                template.nameId = Utility.StringIndex[sk.desc] * 2 + 1;

                string stack;
                int level = -1;
                if (level == -1)
                    level = Utility.GetSkillLevelFromName(sk.name, out stack);
                //if (level == -1 && !String.IsNullOrEmpty(sk.skillicon_name))
                //    level = Utility.GetSkillLevelFromName(sk.skillicon_name, out stack); 
                if (level == -1)
                    level = 1;
                template.lvl = level;
                level = Utility.GetSkillLevelFromName(sk.desc, out stack);
                template.stack = stack.ToUpper();

                template.skilltype = (skillType)sk.type;
                template.skillsubtype = (skillSubType)sk.sub_type;
                template.tslot = (TargetSlot)sk.target_slot;
                template.tslot_level = sk.target_slot_level;
                if (sk.id == 417)
                    template.activation = activationAttribute.ACTIVE;
                else
                    template.activation = (activationAttribute)sk.activation_attribute;
                template.cooldown = sk.delay_time / 100;
                template.cancel_rate = sk.cancel_rate;
                if (sk.casting_delay > 0)
                    template.duration = sk.casting_delay;
                template.pvp_duration = sk.pvp_remain_time_ratio;
                template.chain_skill_prob = sk.chain_skill_prob2;
                template.dispel_category = (DispelCategory)sk.dispel_category;
                template.dispel_level = sk.required_dispel_level;
                template.delay_id = sk.delay_id;

                if (!String.IsNullOrEmpty(sk.penalty_skill_succ)) {
                    var penaltySkill = Utility.SkillIndex[sk.penalty_skill_succ];
                    if (penaltySkill == null) {
                        Debug.Print("Missing penalty skill: {0}", sk.penalty_skill_succ);
                    } else {
                        template.penalty_skill_id = penaltySkill.id;
                    }
                }

                if (sk.change_stance != Stance.none)
                    template.stance = true;
                template.pvp_damage = sk.pvp_damage_ratio;

                if (sk.first_target != FirstTarget.None) {
                    var properties = new Properties();
                    properties.firsttarget = new FirstTargetProperty((FirstTargetAttribute)sk.first_target);
                    if (sk.first_target_valid_distance > 0)
                        properties.firsttargetrange =
                            new FirstTargetRangeProperty(sk.first_target_valid_distance);
                    else if (sk.first_target == FirstTarget.Target)
                        properties.firsttargetrange = new FirstTargetRangeProperty();

                    if (sk.target_range != TargetRange.None)
                        properties.targetrange =
                            new TargetRangeProperty(sk.target_maxcount, sk.target_range_opt1,
                                                    (TargetRangeAttribute)sk.target_range);

                    if (sk.target_relation_restriction != RelationRestriction.None)
                        properties.targetrelation =
                            new TargetRelationProperty((TargetRelationAttribute)sk.target_relation_restriction);

					if (sk.target_species_restriction != SpeciesRestriction.None)
						properties.targetspecies =
							new TargetSpeciesProperty((TargetAttribute)sk.target_species_restriction);

                    var importUtil = Utility<ClientSkill>.Instance;
                    List<string> states = new List<string>();
                    importUtil.Export<string>(sk, "target_valid_status", states);
                    TargetState state = TargetState.NONE;
                    foreach (string s in states) {
                        TargetState s1 = (TargetState)Enum.Parse(typeof(TargetState), s, true);
                        state |= s1;
                    }
                    if (state != TargetState.NONE)
                        properties.targetstatus = new TargetStatusProperty(state);

                    template.setproperties = properties;
                }

                if (sk.add_wpn_range) {
                    template.initproperties = new Properties();
                    template.initproperties.addweaponrange = new AddWeaponRangeProperty();
                }

                template.useconditions = new Conditions();
                template.startconditions = new Conditions();
                template.actions = new Actions();

                List<Condition> useList = new List<Condition>();
                List<Condition> startList = new List<Condition>();
                List<Action> actionList = new List<Action>();

                TargetCondition targetcondition = null;

				/*
                if (sk.target_species_restriction != SpeciesRestriction.None) {
                    targetcondition = new TargetCondition((TargetAttribute)sk.target_species_restriction);
                    startList.Add(targetcondition);
                }
				*/

                FlyRestriction restriction = (FlyRestriction)sk.target_flying_restriction;
                if (restriction != FlyRestriction.NONE) {
                    if (targetcondition == null)
                        startList.Add(new TargetCondition(restriction));
                    else
                        targetcondition.restriction = restriction;
                }
                restriction = (FlyRestriction)sk.self_flying_restriction;
                if (restriction != FlyRestriction.NONE)
                    startList.Add(new SelfCondition(restriction));

                int startCost = sk.activation_attribute == Activation.Toggle ? sk.cost_toggle : sk.cost_start;
                int startLvl = sk.activation_attribute == Activation.Toggle ? sk.cost_toggle_lv : sk.cost_start_lv;
                int endCost = sk.cost_end;
                if (endCost == 0 && sk.activation_attribute == Activation.Toggle)
                    endCost = sk.cost_toggle;
                int endLvl = sk.cost_end_lv;
                if (endLvl == 0 && sk.activation_attribute == Activation.Toggle)
                    endLvl = sk.cost_toggle_lv;

                if (sk.cost_checktime > 0 || sk.cost_toggle > 0) {
                    OverTimeEffect ot = null;
                    CostType parameter = sk.cost_checktime_parameter;
                    if (parameter == CostType.NONE)
                        parameter = sk.cost_parameter;

                    if (parameter == CostType.MP || parameter == CostType.MP_RATIO) {
                        var mpEff = new MpUseOverTimeEffect();
                        ot = mpEff;
                        mpEff.checktime = sk.cost_time;
                        mpEff.percent = parameter == CostType.MP_RATIO;
                        mpEff.cost_start = sk.cost_start;
                        if (sk.cost_checktime_lv > 0 || sk.cost_checktime > 0) {
                            mpEff.value = sk.cost_checktime;
                            mpEff.delta = sk.cost_checktime_lv;
                        } else if (sk.cost_toggle_lv > 0 || sk.cost_toggle > 0) {
                            mpEff.value = sk.cost_toggle;
                            mpEff.delta = sk.cost_toggle_lv;
                        }
                        mpEff.cost_end = endCost;
                        if (endCost > 0 || startLvl > 0) {
                            if (sk.cost_parameter == CostType.MP)
                                startList.Add(new MpCondition(endCost, startLvl));
                            else if (sk.cost_parameter == CostType.HP)
                                startList.Add(new HpCondition(endCost, startLvl));
                            else {
                                Debug.Print("Cost parameter not handled: {0}", sk.cost_parameter);
                            }
                        }
                    } else if (parameter == CostType.HP || parameter == CostType.HP_RATIO) {
                        var hpEff = new HpUseOverTimeEffect();
                        ot = hpEff;
                        hpEff.checktime = sk.cost_time;
                        hpEff.percent = parameter == CostType.HP_RATIO;
                        hpEff.cost_start = sk.cost_start;
                        if (sk.cost_checktime_lv > 0 || sk.cost_checktime > 0) {
                            hpEff.value = sk.cost_checktime;
                            hpEff.delta = sk.cost_checktime_lv;
                        } else if (sk.cost_toggle_lv > 0 || sk.cost_toggle > 0) {
                            hpEff.value = sk.cost_toggle;
                            hpEff.delta = sk.cost_toggle_lv;
                        }
                        hpEff.cost_end = endCost;
                        if (endCost > 0 || startLvl > 0) {
                            if (sk.cost_parameter == CostType.MP)
                                startList.Add(new MpCondition(endCost, startLvl));
                            else if (sk.cost_parameter == CostType.HP)
                                startList.Add(new HpCondition(endCost, startLvl));
                            else {
                                Debug.Print("Cost parameter not handled: {0}", sk.cost_parameter);
                            }
                        }
                    }
                    if (ot != null) {
                        if (sk.effect1_hop_type == null) {
                            Debug.Print("Missing HOP data for {0}: id={1}", sk.cost_parameter, sk.id);
                        } else {
                            ot.hoptype = (HopType)Enum.Parse(typeof(HopType), sk.effect1_hop_type, true);
                            ot.hopb = sk.effect1_hop_b;
                        }
                        if (template.effects == null)
                            template.effects = new Effects();
                        if (template.effects.EffectList == null)
                            template.effects.EffectList = new List<Effect>();
                        template.effects.EffectList.Add(ot);
                    }
                } else {
                    // cost_checktime_parameter is always absent
                    if (sk.cost_parameter == CostType.MP || sk.cost_parameter == CostType.MP_RATIO) {
                        if (endCost != 0 || endLvl != 0) {
                            var action = new MpUseAction(endCost, endLvl);
                            action.percent = sk.cost_parameter == CostType.MP_RATIO;
                            actionList.Add(action);
                        }
                        if (sk.cost_parameter == CostType.MP) {
                            if (startCost > 0 || startLvl > 0)
                                startList.Add(new MpCondition(startCost, startLvl));
                            else if (endCost > 0 || endLvl > 0)
                                startList.Add(new MpCondition(endCost, endLvl));
                        }
                    } else if (sk.cost_parameter == CostType.HP || sk.cost_parameter == CostType.HP_RATIO) {
                        if (endCost != 0 || endLvl != 0) {
                            var action = new HpUseAction(endCost, endLvl);
                            action.percent = sk.cost_parameter == CostType.HP_RATIO;
                            actionList.Add(action);
                        }
                        if (sk.cost_parameter == CostType.HP) {
                            if (startCost > 0 || startLvl > 0)
                                startList.Add(new HpCondition(startCost, startLvl));
                            else if (endCost > 0 || endLvl > 0)
                                startList.Add(new HpCondition(endCost, endLvl));
                        }
                    }
                }

                if (sk.cost_dp > 0) {
                    startList.Add(new DpCondition(sk.cost_dp));
                    actionList.Add(new DpUseAction(sk.cost_dp, sk.cost_dp_lv));
                }

                if (sk.component != null) {
                    Item item = Utility.ItemIndex.GetItem(sk.component);
                    if (item == null) {
                        Debug.Print("Missing item for skill {0}", sk.id);
                    } else {
                        actionList.Add(new ItemUseAction(item.id, sk.component_count));
                    }
                }

                if (!sk.move_casting)
                    useList.Add(new PlayerMovedCondition(false));

                if (sk.use_arrow != null && sk.use_arrow != "0") // 3 arena skills have FX_pow, FX_HIT and weaponbody
                    startList.Add(new ArrowCheckCondition());

                #region Fill conditions and actions

                if (useList.Count == 0)
                    template.useconditions = null;
                else
                    template.useconditions.ConditionList = useList;

                if (startList.Count == 0)
                    template.startconditions = null;
                else
                    template.startconditions.ConditionList = startList;

                if (actionList.Count == 0)
                    template.actions = null;
                else
                    template.actions.ActionList = actionList;

                #endregion

                if (skillsetEx.ContainsKey(sk.id)) {
                    template.skillset_exception = skillsetEx[sk.id];
                }

                if (delayIdsForSkills.ContainsKey(sk.id)) {
                    template.delay_id = delayIdsForSkills[sk.id];
                } else if (delayIdOverrides.ContainsKey(sk.id)) {
                    template.delay_id = delayIdOverrides[sk.id];
                }            

                string descrStr = sk.desc_long == null ? sk.desc : sk.desc_long;
                var desc = Utility.StringIndex.GetStringDescription(descrStr);
                string desc_2nd;
                if (!String.IsNullOrEmpty(sk.desc_long_2nd))
                    desc_2nd = Utility.StringIndex.GetStringDescription(sk.desc_long_2nd).body;

                #region Effect processing

                var utility = Utility<ClientSkill>.Instance;
                List<ClientEffect> effects = new List<ClientEffect>();
                utility.Export(sk, "effect", effects);

                var validEffects = effects.Where(e => e.type != EffectType.None).ToArray();
                bool save = validEffects.Where(e => e.type == filterEffect).Any();
                // var validEffects = effects.Where(e => e.changeStat != Stat.None).ToArray();
                // bool save = validEffects.Where(e => e.changeStat == filterStat).Any();

                string text = desc == null ? String.Empty : desc.body;
                int idx = 0;
                var vars = (from v in Utility.GetVarStrings(text)
                            let parts = v.Split('.')
                            let parsed = Int32.TryParse(parts[0].Remove(0, 1), out idx)
                            let name = parsed ? parts[1] : v
                            let var = parsed ? parts[2] : String.Empty
                            select new { Id = idx, Data = new StatData(name, var) })
                            .ToLookup(a => a.Id, a => a);

                if (save) {
                    sb.Append("\r\n");
                    sb.AppendFormat("---Skill Id = {0}, Name = '{1}' ---\r\n",
                                    sk.id, Utility.StringIndex.GetString(sk.desc));
                    if (desc == null)
                        sb.AppendFormat("NO DESCRIPTION\r\n");
                    else
                        sb.AppendFormat("{0}: {1}\r\n", desc.name, desc.body);
                    if (sk.desc_abnormal != null) {
                        sb.AppendFormat("ABNORMAL: {0}\r\n", Utility.StringIndex.GetString(sk.desc_abnormal));
                    }
                }

                foreach (var eff in validEffects) {
                    EffectClass @class = (EffectClass)eff.type;

                    #region Overrides


                    #endregion

					Type type = effectsAssembly.GetType(String.Format("{0}.{1}", typeof(Effect).Namespace, 
														@class.ToString()));
                    Effect ourEffect = null;

                    if (save)
                        sb.AppendFormat("Data for {0}:\r\n", @class);

                    if (vars.Any()) {
                        if (vars.Contains(idx + 1)) {
                            foreach (var v in vars[idx + 1]) {
                                if (!statData.ContainsKey(v.Data.Name))
                                    statData.Add(v.Data.Name, new HashSet<string>());
                                statData[v.Data.Name].Add(v.Data.Var);
                                if (save)
                                    sb.AppendFormat("\tEffect = {0}, Var = {1}\r\n",
                                                    v.Data.Name, v.Data.Var);
                            }
                        }
                    }
                    if (save) {
                        sb.AppendFormat("\tE{0} Reserved: ", eff.e);
                        using (TextWriter wr = new StringWriter(sb)) {
                            ObjectDumper.Write(eff.reserved, wr);
                            sb.Append("\r\n");
                            if (!String.IsNullOrEmpty(eff.reserved_cond1)) {
                                sb.AppendFormat("\tCondition = {0}, Prob. = ", eff.reserved_cond1);
                                ObjectDumper.Write(eff.reserved_cond1_prob, wr);
                                sb.Append("\r\n");
                            }
                            if (!String.IsNullOrEmpty(eff.reserved_cond2)) {
                                sb.AppendFormat("\tCondition = {0}, Prob. = ", eff.reserved_cond2);
                                ObjectDumper.Write(eff.reserved_cond2_prob, wr);
                                sb.Append("\r\n");
                            }
                        }
                    }

                    if (type == null) {
                        string skillName = Utility.StringIndex.GetString(sk.desc);
                        Debug.Print("Effect {0} not handled (skillId={1}; name='{2}')", @class, sk.id, skillName);
                        continue;
                    } else {
                        ourEffect = (Effect)Activator.CreateInstance(type);
                    }

                    if (template.effects == null) {
                        template.effects = new Effects();
                        if (sk.skillicon_name != null)
                            template.effects.food = sk.skillicon_name.EndsWith("_food");
                    }

                    eff.Skill = sk;
                    eff.Template = template;
                    ourEffect.Import(eff, null);

                    template.effects.EffectList.Add(ourEffect);
                }

                #endregion

                //if (template.effects != null && template.effects.EffectList != null) {
                //    if (template.effects.EffectList.Count == 0)
                //        template.effects.EffectList = null;
                //    else if (template.activation != activationAttribute.PASSIVE) {
                //        foreach (var eff in template.effects.EffectList) {
                //            if (eff is DeformEffect || eff is PolymorphEffect || eff is ShapeChangeEffect ||
                //                eff is WeaponDualEffect) {
                //            } else
                //                eff.basiclvl = 0; // not used for active skills
                //        }
                //    }
                //}
                if (template.effects != null && template.effects.EffectList == null)
                    template.effects = null;

                outputFile.SkillList.Add(template);

                if (filterEffect != EffectType.None) {
                    string fileName = /*"stat_" + filterStat*/filterEffect.ToString() + ".txt";
                    using (var fs = new FileStream(Path.Combine(outputPath, fileName), FileMode.Create,
                                                   FileAccess.Write)) {
                        using (TextWriter wr = new StreamWriter(fs)) {
                            wr.Write(sb.ToString());
                        }
                    }
                }

                sb.Length = 0;
            }

            try {
                using (var fs = new FileStream(Path.Combine(outputPath, "skill_templates.xml"),
                                               FileMode.Create, FileAccess.Write))
                using (var writer = XmlWriter.Create(fs, settings)) {
                    XmlSerializer ser = new XmlSerializer(typeof(SkillData));
                    // ser.UnknownAttribute += new XmlAttributeEventHandler(OnUnknownAttribute);
                    // ser.UnknownElement += new XmlElementEventHandler(OnUnknownElement);
                    ser.Serialize(writer, outputFile);
                }
            } catch (Exception ex) {
                Debug.Print(ex.ToString());
            }
        }

        static void OnUnknownAttribute(object sender, XmlAttributeEventArgs e) {
            FieldInfo field = e.ObjectBeingDeserialized.GetType().GetField(e.Attr.Name);
            MethodInfo parse = field.FieldType.GetMethod("Parse");
            field.SetValue(e.ObjectBeingDeserialized, parse.Invoke(null, new object[] { e.Attr.Value }));
        }

        static void OnUnknownElement(object sender, XmlElementEventArgs e) {
            FieldInfo field = e.ObjectBeingDeserialized.GetType().GetField(e.Element.Name);
            MethodInfo parse = field.FieldType.GetMethod("Parse");
            field.SetValue(e.ObjectBeingDeserialized, parse.Invoke(null, new object[] { e.Element.Value }));
        }
    }
}
