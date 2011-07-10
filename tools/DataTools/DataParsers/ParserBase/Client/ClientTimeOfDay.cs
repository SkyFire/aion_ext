using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Serialization;

namespace Jamie.ParserBase
{
    [Serializable]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroup
    {
        [XmlElement("time_name", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupTime_name[] time_name;

        [XmlElement("time_hour", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupTime_hour[] time_hour;

        /// <remarks/>
        [XmlElement("time_min", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupTime_min[] time_min;

        /// <remarks/>
        [XmlElement("fog_start", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupFog_start[] fog_start;

        /// <remarks/>
        [XmlElement("fog_end", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupFog_end[] fog_end;

        /// <remarks/>
        [XmlElement("fog_color", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupFog_color[] fog_color;

        /// <remarks/>
        [XmlElement("view_dist", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupView_dist[] view_dist;

        /// <remarks/>
        [XmlElement("fog_density", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupFog_density[] fog_density;

        /// <remarks/>
        [XmlElement("fog_heightfalloff", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupFog_heightfalloff[] fog_heightfalloff;

        /// <remarks/>
        [XmlElement("fogatmospherescale", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupFogatmospherescale[] fogatmospherescale;

        /// <remarks/>
        [XmlElement("fogatmosphereheight", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupFogatmosphereheight[] fogatmosphereheight;

        /// <remarks/>
        [XmlElement("fogskyratio", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupFogskyratio[] fogskyratio;

        /// <remarks/>
        [XmlElement("env_color", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupEnv_color[] env_color;

        /// <remarks/>
        [XmlElement("outdoor_ambient", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupOutdoor_ambient[] outdoor_ambient;

        /// <remarks/>
        [XmlElement("AmbientAmplify", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupAmbientAmplify[] AmbientAmplify;

        /// <remarks/>
        [XmlElement("env_windforce", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupEnv_windforce[] env_windforce;

        /// <remarks/>
        [XmlElement("env_heathaze", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupEnv_heathaze[] env_heathaze;

        /// <remarks/>
        [XmlElement("particlefx", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupParticlefx[] particlefx;

        /// <remarks/>
        [XmlElement("fullscreenfx", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupFullscreenfx[] fullscreenfx;

        /// <remarks/>
        [XmlElement("fxEnvColorRatio", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupFxEnvColorRatio[] fxEnvColorRatio;

        /// <remarks/>
        [XmlElement("fxEnvAmbAlphaRatio", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupFxEnvAmbAlphaRatio[] fxEnvAmbAlphaRatio;

        /// <remarks/>
        [XmlElement("sky_box_name", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSky_box_name[] sky_box_name;

        /// <remarks/>
        [XmlElement("cloud2d", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupCloud2d[] cloud2d;

        /// <remarks/>
        [XmlElement("cloud3d", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupCloud3d[] cloud3d;

        /// <remarks/>
        [XmlElement("sky_lens_name", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSky_lens_name[] sky_lens_name;

        /// <remarks/>
        [XmlElement("sky_lens_check", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSky_lens_check[] sky_lens_check;

        /// <remarks/>
        [XmlElement("sky_sun_check", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSky_sun_check[] sky_sun_check;

        /// <remarks/>
        [XmlElement("sky_sun_height", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSky_sun_height[] sky_sun_height;

        /// <remarks/>
        [XmlElement("sky_sun_direction", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSky_sun_direction[] sky_sun_direction;

        /// <remarks/>
        [XmlElement("SunAmplify", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSunAmplify[] SunAmplify;

        /// <remarks/>
        [XmlElement("SunShaftDisplayRatio", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSunShaftDisplayRatio[] SunShaftDisplayRatio;

        /// <remarks/>
        [XmlElement("SunShaftFogShadowRatio", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSunShaftFogShadowRatio[] SunShaftFogShadowRatio;

        /// <remarks/>
        [XmlElement("SunMultiplier", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSunMultiplier[] SunMultiplier;

        /// <remarks/>
        [XmlElement("RayleighScattering", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupRayleighScattering[] RayleighScattering;

        /// <remarks/>
        [XmlElement("MieScattering", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupMieScattering[] MieScattering;

        /// <remarks/>
        [XmlElement("ZenithShift", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupZenithShift[] ZenithShift;

        /// <remarks/>
        [XmlElement("SunWavelength_r", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSunWavelength_r[] SunWavelength_r;

        /// <remarks/>
        [XmlElement("SunWavelength_g", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSunWavelength_g[] SunWavelength_g;

        /// <remarks/>
        [XmlElement("SunWavelength_b", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSunWavelength_b[] SunWavelength_b;

        /// <remarks/>
        [XmlElement("ZenithColor", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupZenithColor[] ZenithColor;

        /// <remarks/>
        [XmlElement("SSAO", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSSAO[] SSAO;

        /// <remarks/>
        [XmlElement("HorizonColor", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupHorizonColor[] HorizonColor;

        /// <remarks/>
        [XmlElement("PhotoColor", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupPhotoColor[] PhotoColor;

        /// <remarks/>
        [XmlElement("SelectiveSrc", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSelectiveSrc[] SelectiveSrc;

        /// <remarks/>
        [XmlElement("SelectiveDest", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSelectiveDest[] SelectiveDest;

        /// <remarks/>
        [XmlElement("GammaSrcMin", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupGammaSrcMin[] GammaSrcMin;

        /// <remarks/>
        [XmlElement("GammaSrcMax", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupGammaSrcMax[] GammaSrcMax;

        /// <remarks/>
        [XmlElement("GammaDestMin", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupGammaDestMin[] GammaDestMin;

        /// <remarks/>
        [XmlElement("GammaDestMax", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupGammaDestMax[] GammaDestMax;

        /// <remarks/>
        [XmlElement("GammaInterpolate", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupGammaInterpolate[] GammaInterpolate;

        /// <remarks/>
        [XmlElement("PhotocolorInterpolate", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupPhotocolorInterpolate[] PhotocolorInterpolate;

        /// <remarks/>
        [XmlElement("SelectiveInterpolate", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSelectiveInterpolate[] SelectiveInterpolate;

        /// <remarks/>
        [XmlElement("NoiseAmount", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupNoiseAmount[] NoiseAmount;

        /// <remarks/>
        [XmlElement("WaveLength", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupWaveLength[] WaveLength;

        /// <remarks/>
        [XmlElement("WaveSpeed", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupWaveSpeed[] WaveSpeed;

        /// <remarks/>
        [XmlElement("WaveAmplitude", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupWaveAmplitude[] WaveAmplitude;

        /// <remarks/>
        [XmlElement("WaveAngle", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupWaveAngle[] WaveAngle;

        /// <remarks/>
        [XmlElement("SunSpecular", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSunSpecular[] SunSpecular;

        /// <remarks/>
        [XmlElement("SurfaceAmount", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSurfaceAmount[] SurfaceAmount;

        /// <remarks/>
        [XmlElement("BeachAmount", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupBeachAmount[] BeachAmount;

        /// <remarks/>
        [XmlElement("RefractColor", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupRefractColor[] RefractColor;

        /// <remarks/>
        [XmlElement("RefractAmount", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupRefractAmount[] RefractAmount;

        /// <remarks/>
        [XmlElement("SunColor", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSunColor[] SunColor;

        /// <remarks/>
        [XmlElement("SunColorMultiplier", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupSunColorMultiplier[] SunColorMultiplier;

        /// <remarks/>
        [XmlElement("ReflectAmount", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupReflectAmount[] ReflectAmount;

        /// <remarks/>
        [XmlElement("FogColor", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupFogColor[] FogColor;

        /// <remarks/>
        [XmlElement("FogDensity", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupFogDensity[] FogDensity;

        /// <remarks/>
        [XmlElement("LayerIntensity", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupLayerIntensity[] LayerIntensity;

        /// <remarks/>
        [XmlElement("LayerThaw", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroupLayerThaw[] LayerThaw;

        /// <remarks/>
        [XmlAttribute]
        public string zonename;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupTime_name
    {

        /// <remarks/>
        [XmlElement(Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public string keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupTime_hour
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupTime_min
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupFog_start
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupFog_end
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupFog_color
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupView_dist
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupFog_density
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupFog_heightfalloff
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupFogatmospherescale
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupFogatmosphereheight
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupFogskyratio
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupEnv_color
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupOutdoor_ambient
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupAmbientAmplify
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupEnv_windforce
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupEnv_heathaze
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupParticlefx
    {

        /// <remarks/>
        [XmlElement(Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public string keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupFullscreenfx
    {

        /// <remarks/>
        [XmlElement(Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public string keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupFxEnvColorRatio
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupFxEnvAmbAlphaRatio
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSky_box_name
    {

        /// <remarks/>
        [XmlElement(Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public string keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupCloud2d
    {

        /// <remarks/>
        [XmlElement(Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public string keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupCloud3d
    {

        /// <remarks/>
        [XmlElement(Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public string keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSky_lens_name
    {

        /// <remarks/>
        [XmlElement(Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public string keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSky_lens_check
    {

        /// <remarks/>
        [XmlElement(Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public string keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSky_sun_check
    {

        /// <remarks/>
        [XmlElement(Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public string keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSky_sun_height
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSky_sun_direction
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSunAmplify
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSunShaftDisplayRatio
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSunShaftFogShadowRatio
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSunMultiplier
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupRayleighScattering
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupMieScattering
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupZenithShift
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSunWavelength_r
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSunWavelength_g
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSunWavelength_b
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupZenithColor
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSSAO
    {

        /// <remarks/>
        [XmlElement(Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public string keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupHorizonColor
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupPhotoColor
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSelectiveSrc
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSelectiveDest
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupGammaSrcMin
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupGammaSrcMax
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupGammaDestMin
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupGammaDestMax
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupGammaInterpolate
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupPhotocolorInterpolate
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSelectiveInterpolate
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupNoiseAmount
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupWaveLength
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupWaveSpeed
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupWaveAmplitude
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupWaveAngle
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSunSpecular
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSurfaceAmount
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupBeachAmount
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupRefractColor
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupRefractAmount
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSunColor
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupSunColorMultiplier
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupReflectAmount
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupFogColor
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupFogDensity
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    /// <remarks/>
    [System.CodeDom.Compiler.GeneratedCodeAttribute("xsd", "2.0.50727.3038")]
    [Serializable]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupLayerIntensity
    {

        /// <remarks/>
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        /// <remarks/>
        [XmlAttribute]
        public string ratio;

        /// <remarks/>
        [XmlAttribute]
        public string type;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionTimeofDayEnvSystemTimeofDayGroupLayerThaw
    {
        [XmlElement("keyvalue")]
        public keyvalue keyvalue;

        [XmlAttribute]
        public string ratio;

        [XmlAttribute]
        public string type;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(Namespace = "", IsNullable = false)]
    public partial class keyvalue
    {
        [XmlAttribute]
        public string key0;

        [XmlAttribute]
        public string key1;

        [XmlAttribute]
        public string key2;
    }
}
