namespace Jamie.ParserBase
{
    using System;
    using System.Xml.Serialization;
    using System.Xml.Schema;

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "Environment", Namespace = "", IsNullable = false)]
    public partial class EnvironmentData
    {
        [XmlElement("Fog", Form = XmlSchemaForm.Unqualified)]
        public EnvironmentFog[] Fog;

        [XmlElement("Shaders", Form = XmlSchemaForm.Unqualified)]
        public EnvironmentShaders[] Shaders;

        [XmlElement("EnvState", Form = XmlSchemaForm.Unqualified)]
        public EnvironmentEnvState[] EnvState;

        [XmlElement("MRTSky", Form = XmlSchemaForm.Unqualified)]
        public EnvironmentMRTSky[] MRTSky;

        [XmlElement("MRTOcean", Form = XmlSchemaForm.Unqualified)]
        public EnvironmentMRTOcean[] MRTOcean;

        [XmlElement("Ocean", Form = XmlSchemaForm.Unqualified)]
        public EnvironmentOcean[] Ocean;

        [XmlElement("HeightMap", Form = XmlSchemaForm.Unqualified)]
        public EnvironmentHeightMap[] HeightMap;

        [XmlElement("Misc", Form = XmlSchemaForm.Unqualified)]
        public EnvironmentMisc[] Misc;

        [XmlElement("Lighting", Form = XmlSchemaForm.Unqualified)]
        public EnvironmentLighting[] Lighting;

        [XmlElement("Lighting_000", Form = XmlSchemaForm.Unqualified)]
        public EnvironmentLighting_000[] Lighting_000;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EnvironmentFog
    {
        [XmlAttribute]
        public string Color;

        [XmlAttribute]
        public string ColorModify;

        [XmlAttribute]
        public string End;

        [XmlAttribute]
        public string FogAtmosphereHeight;

        [XmlAttribute]
        public string FogAtmosphereScale;

        [XmlAttribute]
        public string FogDensity;

        [XmlAttribute]
        public string FogSkyRatio;

        [XmlAttribute]
        public string HeightFalloff;

        [XmlAttribute]
        public string Start;

        [XmlAttribute]
        public string ViewDistance;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EnvironmentShaders
    {
        [XmlAttribute]
        public string Shore;

        [XmlAttribute]
        public string SkyBox;

        [XmlAttribute]
        public string SunLensFlares;

        [XmlAttribute]
        public string SunWaterRefl;

        [XmlAttribute]
        public string Water;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EnvironmentEnvState
    {
        [XmlAttribute]
        public string AmbientAmplify;

        [XmlAttribute]
        public string EnvColor;

        [XmlAttribute]
        public string OutdoorAmbientColor;

        [XmlAttribute]
        public string SkyBoxAngle;

        [XmlAttribute]
        public string SkyBoxStretching;

        [XmlAttribute]
        public string SunAmplify;

        [XmlAttribute]
        public string SunColor;

        [XmlAttribute]
        public string SunHeightScale;

        [XmlAttribute]
        public string WindForce;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EnvironmentMRTSky
    {
        [XmlAttribute]
        public string HorizonColor;

        [XmlAttribute]
        public string MieScattering;

        [XmlAttribute]
        public string RayleighScattering;

        [XmlAttribute]
        public string SunMultiplier;

        [XmlAttribute]
        public string SunWavelength_B;

        [XmlAttribute]
        public string SunWavelength_G;

        [XmlAttribute]
        public string SunWavelength_R;

        [XmlAttribute]
        public string ZenithColor;

        [XmlAttribute]
        public string ZenithShift;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EnvironmentMRTOcean
    {
        [XmlAttribute]
        public string BeachAmount;

        [XmlAttribute]
        public string FogColor;

        [XmlAttribute]
        public string FogDensity;

        [XmlAttribute]
        public string ReflectAmount;

        [XmlAttribute]
        public string RefractAmount;

        [XmlAttribute]
        public string RefractColor;

        [XmlAttribute]
        public string SunColor;

        [XmlAttribute]
        public string SunColorMultiplier;

        [XmlAttribute]
        public string SunSpecular;

        [XmlAttribute]
        public string SurfaceAmount;

        [XmlAttribute]
        public string WaveAmplitude;

        [XmlAttribute]
        public string WaveAmplitudes;

        [XmlAttribute]
        public string WaveFrequencies;

        [XmlAttribute]
        public string WavePhases;

        [XmlAttribute]
        public string WaveAngle;

        [XmlAttribute]
        public string WaveLength;

        [XmlAttribute]
        public string WaveSpeed;

        [XmlAttribute]
        public string Caustics;

        [XmlAttribute]
        public string Foam;

        [XmlAttribute]
        public string SunGlow;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EnvironmentOcean
    {
        [XmlAttribute]
        public string BorderTranspRatio;

        [XmlAttribute]
        public string BottomTexture;

        [XmlAttribute]
        public string Caustics;

        [XmlAttribute]
        public string FogColor;

        [XmlAttribute]
        public string FogDistance;

        [XmlAttribute]
        public string ShoreSize;

        [XmlAttribute]
        public string SurfaceBumpAmountX;

        [XmlAttribute]
        public string SurfaceBumpAmountY;

        [XmlAttribute]
        public string SurfaceReflectRatio;

        [XmlAttribute]
        public string SurfaceTranspRatio;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EnvironmentHeightMap
    {
        [XmlAttribute]
        public string DefaultZoomTexture;

        [XmlAttribute]
        public string GeometryLodRatio;

        [XmlAttribute]
        public string TextureLodRatio;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EnvironmentMisc
    {
        [XmlAttribute]
        public string VegetationShadowColorMin;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EnvironmentLighting
    {
        [XmlAttribute]
        public string LightingCount;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EnvironmentLighting_000
    {
        [XmlAttribute]
        public string Algorithm;

        [XmlAttribute]
        public string Ambient;

        [XmlAttribute]
        public string CurrentHour;

        [XmlAttribute]
        public string HemiSamplQuality;

        [XmlAttribute]
        public string Lighting;

        [XmlAttribute]
        public string lightmap_name;

        [XmlAttribute]
        public string ObjShadows;

        [XmlAttribute]
        public string ShadowBlur;

        [XmlAttribute]
        public string ShadowIntensity;

        [XmlAttribute]
        public string Shadows;

        [XmlAttribute]
        public string SkyColor;

        [XmlAttribute]
        public string SunColor;

        [XmlAttribute]
        public string SunHeight;

        [XmlAttribute]
        public string SunMultiplier;

        [XmlAttribute]
        public string SunRotation;

        [XmlAttribute]
        public string SunVector;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(Namespace = "", IsNullable = false)]
    public partial class points_info
    {
        [XmlElement("points", Form = XmlSchemaForm.Unqualified)]
        public points_infoPoints[] points;

        [XmlAttribute]
        public string bottom;

        [XmlAttribute]
        public string top;

        [XmlAttribute]
        public string type;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class points_infoPoints
    {
        [XmlElement("data", Form = XmlSchemaForm.Unqualified)]
        public points_infoPointsData[] data;

        [XmlAttribute]
        public string points_size;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class points_infoPointsData
    {
        [XmlAttribute]
        public string x;

        [XmlAttribute]
        public string y;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(Namespace = "", IsNullable = false)]
    public partial class Entity : ISpawnData
    {
        [XmlElement("Properties", Form = XmlSchemaForm.Unqualified)]
        public EntityProperties Properties;

        [XmlAttribute]
        public string Name { get; set; }

        [XmlAttribute]
        public string FOV;

        [XmlAttribute]
        public string Material;

        [XmlAttribute]
        public string EventType;

        [XmlAttribute]
        public string AionFXPartialGlow;

        [XmlAttribute]
        public string AionFXPostGlow;

        [XmlAttribute]
        public string Angles { get; set; }

        [XmlAttribute]
        public string CastShadowMaps;

        [XmlAttribute]
        public string CastShadows;

        [XmlAttribute]
        public string CustomHairColor;

        [XmlAttribute]
        public string CustomSkinColor;

        [XmlAttribute]
        public string EntityClass;

        [XmlAttribute]
        public string EntityGUID;

        [XmlAttribute]
        public int EntityId { get; set; }

        [XmlAttribute]
        public string EnvSkyBoxObject;

        [XmlAttribute]
        public string EnvTimeShow;

        [XmlAttribute]
        public string EnvWeatherHide;

        [XmlAttribute]
        public string EnvWeatherShow;

        [XmlAttribute]
        public string HiddenInGame;

        [XmlAttribute]
        public string IgnoreFog;

        [XmlAttribute]
        public string IgnoreViewDist;

        [XmlAttribute]
        public string Layer;

        [XmlAttribute]
        public string LodRatio;

        [XmlAttribute]
        public string Pos { get; set; }

        [XmlAttribute]
        public string PreCalcShadows;

        [XmlAttribute]
        public string ReceiveLightmap;

        [XmlAttribute]
        public string RecvShadowMaps;

        [XmlAttribute]
        public string SelfShadowing;

        [XmlAttribute]
        public string SkipOnLowSpec;

        [XmlAttribute]
        public string SyncAnimation;

        [XmlAttribute]
        public ObjectTypes Type { get; set; }

        [XmlAttribute]
        public string useCustomColors;

        [XmlAttribute]
        public string ViewDistRatio;

        [XmlAttribute]
        public string Scale;

        [XmlAttribute]
        public string ParentId;

        #region ISpawnData Members

        [XmlIgnore]
        public int dir {
            get { return 0; }
            set { }
        }

        [XmlIgnore]
        public int use_dir {
            get { return 0;  }
            set { }
        }

        #endregion
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EntityProperties
    {
        [XmlElement("Animation", Form = XmlSchemaForm.Unqualified)]
        public EntityPropertiesAnimation[] Animation;

        [XmlElement("ImpactSound", Form = XmlSchemaForm.Unqualified)]
        public EntityPropertiesImpactSound[] ImpactSound;

        [XmlElement("Physics", Form = XmlSchemaForm.Unqualified)]
        public EntityPropertiesPhysics[] Physics;

        [XmlElement("EaxReverbProperties", Form = XmlSchemaForm.Unqualified)]
        public EntityPropertiesEaxReverbProperties[] EaxReverbProperties;

        [XmlElement("Server", Form = XmlSchemaForm.Unqualified)]
        public ServerData ServerData;

        [XmlElement("Sound1", Form = XmlSchemaForm.Unqualified)]
        public EntityPropertiesSound1[] Sound1;

        [XmlElement("Sound2", Form = XmlSchemaForm.Unqualified)]
        public EntityPropertiesSound2[] Sound2;

        [XmlElement("Sound3", Form = XmlSchemaForm.Unqualified)]
        public EntityPropertiesSound3[] Sound3;

        [XmlElement("Sound4", Form = XmlSchemaForm.Unqualified)]
        public EntityPropertiesSound4[] Sound4;

        [XmlElement("Sound5", Form = XmlSchemaForm.Unqualified)]
        public EntityPropertiesSound5[] Sound5;

        [XmlElement("Sound6", Form = XmlSchemaForm.Unqualified)]
        public EntityPropertiesSound6[] Sound6;

        [XmlElement("Sound7", Form = XmlSchemaForm.Unqualified)]
        public EntityPropertiesSound7[] Sound7;

        [XmlAttribute("Animation", Form = XmlSchemaForm.Unqualified)]
        public string AnimationOptions;

        [XmlAttribute]
        public string AnimName;

        [XmlAttribute]
        public string AnimationSpeed;

        [XmlAttribute]
        public string AnimationClose;

        [XmlAttribute]
        public string AnimationOpen;

        [XmlAttribute]
        public string AnimationOpenBack;

        [XmlAttribute]
        public string bAllowRigidBodiesToOpenDoor;

        [XmlAttribute]
        public string bAutomatic;

        [XmlAttribute]
        public string bPlayerOnly;

        [XmlAttribute]
        public string bAffectsThisAreaOnly;

        [XmlAttribute]
        public string bAion3dFlare;

        [XmlAttribute]
        public string bFakeLight;

        [XmlAttribute]
        public string bHeatSource;

        [XmlAttribute]
        public string clrColor;

        [XmlAttribute]
        public string EndDist;

        [XmlAttribute]
        public string StartDist;

        [XmlAttribute]
        public string xSkyEnd;

        [XmlAttribute]
        public string clientnpc_NPCName;

        [XmlAttribute]
        public string xSkyStart;

        [XmlAttribute]
        public string bProjectInAllDirs;

        [XmlAttribute]
        public string bUseAnimation;

        [XmlAttribute]
        public string bUsedInRealTime;

        [XmlAttribute]
        public string clrDiffuse;

        [XmlAttribute]
        public string clrSpecular;

        [XmlAttribute]
        public string CoronaScale;

        [XmlAttribute]
        public string damping;

        [XmlAttribute]
        public string DiffuseMultiplier;

        [XmlAttribute]
        public string fileModel01;

        [XmlAttribute]
        public string fileModel2;

        [XmlAttribute]
        public string fileModel3;

        [XmlAttribute]
        public string LightStyle;

        [XmlAttribute]
        public string lighttype;

        [XmlAttribute]
        public string max_time_step;

        [XmlAttribute]
        public string ProjectorFov;

        [XmlAttribute]
        public string RndPosFreq;

        [XmlAttribute]
        public string shader_lightShader;

        [XmlAttribute]
        public string shakeAmount;

        [XmlAttribute]
        public string shakeRefreshTime;

        [XmlAttribute]
        public string sleep_speed;

        [XmlAttribute]
        public string SpecularMultiplier;

        [XmlAttribute]
        public string texture_ProjectorTexture;

        [XmlAttribute]
        public string vector_LightDir;

        [XmlAttribute]
        public string weight;

        [XmlAttribute]
        public string bStayOpenWhenOccupied;

        [XmlAttribute]
        public string bUsePortal;

        [XmlAttribute]
        public string CloseDelay;

        [XmlAttribute]
        public string fHitImpulse;

        [XmlAttribute]
        public string iNeededKey;

        [XmlAttribute]
        public string HeightMax;

        [XmlAttribute]
        public string HeightMin;

        [XmlAttribute]
        public string bActive;

        [XmlAttribute]
        public string bActivateOnStart;

        [XmlAttribute]
        public string bFollowPlayer;

        [XmlAttribute]
        public string bNoInclination;

        [XmlAttribute]
        public string bNoLanding;

        [XmlAttribute]
        public string bObstacleAvoidance;

        [XmlAttribute]
        public string boid_mass;

        [XmlAttribute]
        public string boid_path;

        [XmlAttribute]
        public string boid_radius;

        [XmlAttribute]
        public string BoidFOV;

        [XmlAttribute]
        public string BoidSize;

        [XmlAttribute]
        public string CollisionWithTerrain;

        [XmlAttribute]
        public string FactorAlign;

        [XmlAttribute]
        public string FactorAvoidLand;

        [XmlAttribute]
        public string FactorCohesion;

        [XmlAttribute]
        public string FactorHeight;

        [XmlAttribute]
        public string FactorOrigin;

        [XmlAttribute]
        public string FactorSeparation;

        [XmlAttribute]
        public string flockTime_FShowTime;

        [XmlAttribute]
        public string flockWeather_FHideWeather;

        [XmlAttribute]
        public string flockWeather_FShowWeather;

        [XmlAttribute]
        public string bCheckBottom;

        [XmlAttribute]
        public string fRadiusRandom;

        [XmlAttribute]
        public string fScale;

        [XmlAttribute]
        public string nParticleType;

        [XmlAttribute]
        public string viewDistType_ViewType;

        [XmlAttribute]
        public string BindingEmitterName;

        [XmlAttribute]
        public string fUpdateRadius;

        [XmlAttribute]
        public string ParticleEffect;

        [XmlAttribute]
        public string FollowDistance;

        [XmlAttribute]
        public string gravity_at_death;

        [XmlAttribute]
        public string MaxAnimSpeed;

        [XmlAttribute]
        public string MaxAttractDist;

        [XmlAttribute]
        public string MaxHeight;

        [XmlAttribute]
        public string MaxSpeed;

        [XmlAttribute]
        public string MinAttractDist;

        [XmlAttribute]
        public string MinHeight;

        [XmlAttribute]
        public string MinSpeed;

        [XmlAttribute]
        public string nNumBirds;

        [XmlAttribute]
        public string nNumFish;

        [XmlAttribute]
        public string objModel;

        [XmlAttribute]
        public string Particle;

        [XmlAttribute]
        public string ParticleAttachBone;

        [XmlAttribute]
        public string PathBehavior;

        [XmlAttribute]
        public string PathGroupLengthPoint;

        [XmlAttribute]
        public string PathGroupRadius;

        [XmlAttribute]
        public string PathGroupRatio;

        [XmlAttribute]
        public string VisibilityDist;

        [XmlAttribute]
        public string Scale;

        [XmlAttribute]
        public string bIsSpawnSource;

        [XmlAttribute]
        public string SpawnPeriod;

        [XmlAttribute]
        public string bEnabled;

        [XmlAttribute]
        public string bLoop;

        [XmlAttribute]
        public string bOnce;

        [XmlAttribute]
        public string bPlay;

        [XmlAttribute]
        public string fFadeValue;

        [XmlAttribute]
        public string InnerRadius;

        [XmlAttribute]
        public string iVolume;

        [XmlAttribute]
        public string OuterRadius;

        [XmlAttribute]
        public string sndSource;

        [XmlAttribute]
        public string bLIndoorOnly;

        [XmlAttribute]
        public string bLOutdoorOnly;

        [XmlAttribute]
        public string nEAXEnvironment;

        [XmlAttribute]
        public string fileLadderCGF;

        [XmlAttribute]
        public string gostring_Text;

        [XmlAttribute]
        public string fFontScale;

        [XmlAttribute]
        public string damage_players;

        [XmlAttribute]
        public string object_Model;

        [XmlAttribute]
        public string object_AnimatedModel;

        [XmlAttribute]
        public string TextInstruction;

        [XmlAttribute]
        public string sndAnimStart;

        [XmlAttribute]
        public string sndAnimStop;

        [XmlAttribute]
        public string Billboard_Type;

        [XmlAttribute]
        public string bPhysicalize;

        [XmlAttribute]
        public string fHAngleLimit;

        [XmlAttribute]
        public string LockDist;

        [XmlAttribute]
        public string shader_BillbardShader;

        [XmlAttribute]
        public string texture_BillbardTexture;

        [XmlAttribute]
        public string vector_AngleOffset;

        [XmlAttribute]
        public string vector_Bounds;

        [XmlAttribute]
        public string nBehaviour;

        [XmlAttribute]
        public string nNumBugs;

        [XmlAttribute]
        public string object_Character;

        [XmlAttribute]
        public string object_Model1;

        [XmlAttribute]
        public string object_Model2;

        [XmlAttribute]
        public string object_Model3;

        [XmlAttribute]
        public string object_Model4;

        [XmlAttribute]
        public string object_Model5;

        [XmlAttribute]
        public string object_TexName;

        [XmlAttribute]
        public string Radius;

        [XmlAttribute]
        public string RandomMovement;

        [XmlAttribute]
        public string SpeedMax;

        [XmlAttribute]
        public string SpeedMin;

        [XmlAttribute]
        public string TexBehavior;

        [XmlAttribute]
        public string TexRadius;

        [XmlAttribute]
        public string TexRandomColor;

        [XmlAttribute]
        public string TexRandomLife;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EntityPropertiesAnimation
    {
        [XmlAttribute]
        public string Animation;

        [XmlAttribute]
        public string bLoop;

        [XmlAttribute]
        public string bPlaying;

        [XmlAttribute]
        public string Speed;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class ServerData
    {
        [XmlAttribute]
        public bool bClickable;

        [XmlAttribute]
        public bool bCloseable;

        [XmlAttribute]
        public bool bOneWay;

        [XmlAttribute]
        public bool bOpened;

        [XmlAttribute]
        public int id;

        [XmlAttribute]
        public string Key;

        [XmlAttribute]
        public string Link;

        [XmlAttribute]
        public int Timer;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EntityPropertiesImpactSound
    {
        [XmlAttribute]
        public string InnerRadius;

        [XmlAttribute]
        public string nVolume;

        [XmlAttribute]
        public string OuterRadius;

        [XmlAttribute]
        public string sndFilename;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EntityPropertiesPhysics
    {
        [XmlElement("LowSpec", Form = XmlSchemaForm.Unqualified)]
        public EntityPropertiesPhysicsLowSpec[] LowSpec;

        [XmlAttribute]
        public string bActivateOnDamage;

        [XmlAttribute]
        public string bFixedDamping;

        [XmlAttribute]
        public string bResting;

        [XmlAttribute]
        public string bRigidBody;

        [XmlAttribute]
        public string bRigidBodyActive;

        [XmlAttribute]
        public string damping;

        [XmlAttribute]
        public string Density;

        [XmlAttribute]
        public string Mass;

        [XmlAttribute]
        public string max_time_step;

        [XmlAttribute]
        public string sleep_speed;

        [XmlAttribute]
        public string Type;

        [XmlAttribute]
        public string vector_Impulse;

        [XmlAttribute]
        public string water_damping;

        [XmlAttribute]
        public string water_density;

        [XmlAttribute]
        public string water_resistance;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EntityPropertiesPhysicsLowSpec
    {
        [XmlAttribute]
        public string bKeepMassAndWater;

        [XmlAttribute]
        public string bKeepRigidBody;

        [XmlAttribute]
        public string bRigidBody;

        [XmlAttribute]
        public string Density;

        [XmlAttribute]
        public string Mass;

        [XmlAttribute]
        public string max_time_step;

        [XmlAttribute]
        public string sleep_speed;

        [XmlAttribute]
        public string water_density;

        [XmlAttribute]
        public string water_resistance;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EntityPropertiesEaxReverbProperties
    {

        [XmlElement("fReflectionsPan", Form = XmlSchemaForm.Unqualified)]
        public EntityPropertiesEaxReverbPropertiesFReflectionsPan[] fReflectionsPan;

        [XmlElement("fReverbPan", Form = XmlSchemaForm.Unqualified)]
        public EntityPropertiesEaxReverbPropertiesFReverbPan[] fReverbPan;

        [XmlAttribute]
        public string fAirAbsorptionHF;

        [XmlAttribute]
        public string fDecayHFRatio;

        [XmlAttribute]
        public string fDecayLFRatio;

        [XmlAttribute]
        public string fDecayTime;

        [XmlAttribute]
        public string fDensity;

        [XmlAttribute]
        public string fDiffusion;

        [XmlAttribute]
        public string fEchoDepth;

        [XmlAttribute]
        public string fEchoTime;

        [XmlAttribute]
        public string fEnvDiffusion;

        [XmlAttribute]
        public string fEnvSize;

        [XmlAttribute]
        public string fHFReference;

        [XmlAttribute]
        public string fLFReference;

        [XmlAttribute]
        public string fModulationDepth;

        [XmlAttribute]
        public string fModulationTime;

        [XmlAttribute]
        public string fReflectionsDelay;

        [XmlAttribute]
        public string fReverbDelay;

        [XmlAttribute]
        public string fRoomRolloffFactor;

        [XmlAttribute]
        public string nEnvironment;

        [XmlAttribute]
        public string nFlags;

        [XmlAttribute]
        public string nReflections;

        [XmlAttribute]
        public string nReverb;

        [XmlAttribute]
        public string nRoom;

        [XmlAttribute]
        public string nRoomHF;

        [XmlAttribute]
        public string nRoomLF;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EntityPropertiesEaxReverbPropertiesFReflectionsPan
    {
        [XmlAttribute]
        public string x;

        [XmlAttribute]
        public string y;

        [XmlAttribute]
        public string z;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EntityPropertiesEaxReverbPropertiesFReverbPan
    {
        [XmlAttribute]
        public string x;

        [XmlAttribute]
        public string y;

        [XmlAttribute]
        public string z;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EntityPropertiesSound1
    {
        [XmlAttribute]
        public string bCentered;

        [XmlAttribute]
        public string bDoNotOverlap;

        [XmlAttribute]
        public string iChanceOfOccuring;

        [XmlAttribute]
        public string iVolume;

        [XmlAttribute]
        public string sndSound;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EntityPropertiesSound2
    {
        [XmlAttribute]
        public string bCentered;

        [XmlAttribute]
        public string bDoNotOverlap;

        [XmlAttribute]
        public string iChanceOfOccuring;

        [XmlAttribute]
        public string iVolume;

        [XmlAttribute]
        public string sndSound;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EntityPropertiesSound3
    {
        [XmlAttribute]
        public string bCentered;

        [XmlAttribute]
        public string bDoNotOverlap;

        [XmlAttribute]
        public string iChanceOfOccuring;

        [XmlAttribute]
        public string iVolume;

        [XmlAttribute]
        public string sndSound;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EntityPropertiesSound4
    {
        [XmlAttribute]
        public string bCentered;

        [XmlAttribute]
        public string bDoNotOverlap;

        [XmlAttribute]
        public string iChanceOfOccuring;

        [XmlAttribute]
        public string iVolume;

        [XmlAttribute]
        public string sndSound;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EntityPropertiesSound5
    {
        [XmlAttribute]
        public string bCentered;

        [XmlAttribute]
        public string bDoNotOverlap;

        [XmlAttribute]
        public string iChanceOfOccuring;

        [XmlAttribute]
        public string iVolume;

        [XmlAttribute]
        public string sndSound;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EntityPropertiesSound6
    {
        [XmlAttribute]
        public string bCentered;

        [XmlAttribute]
        public string bDoNotOverlap;

        [XmlAttribute]
        public string iChanceOfOccuring;

        [XmlAttribute]
        public string iVolume;

        [XmlAttribute]
        public string sndSound;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class EntityPropertiesSound7
    {
        [XmlAttribute]
        public string bCentered;

        [XmlAttribute]
        public string bDoNotOverlap;

        [XmlAttribute]
        public string iChanceOfOccuring;

        [XmlAttribute]
        public string iVolume;

        [XmlAttribute]
        public string sndSound;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(ElementName = "Mission", Namespace = "", IsNullable = false)]
    public partial class ClientMissionFile
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string EquipPacks;

        [XmlElement("TimeofDayGroup", Form = XmlSchemaForm.Unqualified)]
        public MissionTimeofDayEnvSystemTimeofDayGroup[] TimeofDayEnvSystem;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string EntityDescriptions;

        [XmlElement("Environment")]
        public EnvironmentData[] Environment;

        [XmlElement("EnvSetManager", Form = XmlSchemaForm.Unqualified)]
        public MissionEnvSetManager[] EnvSetManager;

        [XmlElement("Weapons", Form = XmlSchemaForm.Unqualified)]
        public MissionWeapons[] Weapons;

        [XmlElement("LevelOption", Form = XmlSchemaForm.Unqualified)]
        public MissionLevelOption[] LevelOption;

        [XmlElement("WeatherSystem", Form = XmlSchemaForm.Unqualified)]
        public MissionWeatherSystem[] WeatherSystem;

        [XmlElement("weather_zone", Form = XmlSchemaForm.Unqualified)]
        public MissionServerZoneListWeather_zone[] ServerZoneList;

        [XmlElement("TimeEnvOption", Form = XmlSchemaForm.Unqualified)]
        public MissionTimeEnvSystemTimeEnvOption[] TimeEnvSystem;

        [XmlElement("timeenv_zone", Form = XmlSchemaForm.Unqualified)]
        public MissionTimeEnvZoneListTimeenv_zone[] TimeEnvZoneList;

        [XmlElement("FxArtifactOption", Form = XmlSchemaForm.Unqualified)]
        public MissionFxArtifactSystemFxArtifactOption[] FxArtifactSystem;

        [XmlElement("CommonShapeList", Form = XmlSchemaForm.Unqualified)]
        public MissionCommonShapeList[] CommonShapeList;

        [XmlElement("Objects", Form = XmlSchemaForm.Unqualified)]
        public MissionObjects[] Objects;

        [XmlAttribute]
        public string CGFCount;

        [XmlAttribute]
        public string Description;

        [XmlAttribute]
        public string MusicScript;

        [XmlAttribute]
        public string Name;

        [XmlAttribute]
        public string PlayerEquipPack;

        [XmlAttribute]
        public string Script;

        [XmlAttribute]
        public string Time;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(Namespace = "", IsNullable = false)]
    public partial class Fog
    {
        [XmlAttribute]
        public string Color;

        [XmlAttribute]
        public string ColorModify;

        [XmlAttribute]
        public string End;

        [XmlAttribute]
        public string FogAtmosphereHeight;

        [XmlAttribute]
        public string FogAtmosphereScale;

        [XmlAttribute]
        public string FogDensity;

        [XmlAttribute]
        public string FogSkyRatio;

        [XmlAttribute]
        public string HeightFalloff;

        [XmlAttribute]
        public string Start;

        [XmlAttribute]
        public string ViewDistance;

        [XmlAttribute]
        public string FogApply;

        [XmlAttribute]
        public string FogColor;

        [XmlAttribute]
        public string FogEnd;

        [XmlAttribute]
        public string FogHeightFalloff;

        [XmlAttribute]
        public string FogStart;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionEnvSetManager
    {
        [XmlElement("Environment", Form = XmlSchemaForm.Unqualified)]
        public EnvironmentData[] EnvSet000;

        [XmlAttribute]
        public string EnvSetSize;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionWeapons
    {
        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string Used;

        [XmlElement(Form = XmlSchemaForm.Unqualified)]
        public string Ammo;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionLevelOption
    {
        [XmlElement("Initialize", Form = XmlSchemaForm.Unqualified)]
        public MissionLevelOptionInitialize[] Initialize;

        [XmlElement("Fly", Form = XmlSchemaForm.Unqualified)]
        public MissionLevelOptionFly[] Fly;

        [XmlElement("BindArea", Form = XmlSchemaForm.Unqualified)]
        public MissionLevelOptionBindArea[] BindArea;

        [XmlElement("ReCall", Form = XmlSchemaForm.Unqualified)]
        public MissionLevelOptionReCall[] ReCall;

        [XmlElement("Glide", Form = XmlSchemaForm.Unqualified)]
        public MissionLevelOptionGlide[] Glide;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionWeatherSystem
    {
        [XmlElement("WeatherOption", Form = XmlSchemaForm.Unqualified)]
        public MissionWeatherSystemWeatherOption[] WeatherOption;

        [XmlElement("WeatherSetTable", Form = XmlSchemaForm.Unqualified)]
        public MissionWeatherSystemWeatherSetTable[] WeatherSetTable;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionServerZoneListWeather_zone
    {
        [XmlElement("points_info", Form = System.Xml.Schema.XmlSchemaForm.Unqualified)]
        public MissionServerZoneListWeather_zonePoints_info[] points_info;

        [XmlAttribute]
        public string name;

        [XmlAttribute]
        public string priority;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionServerZoneListWeather_zonePoints_info
    {
        [XmlElement("points", Form = XmlSchemaForm.Unqualified)]
        public MissionServerZoneListWeather_zonePoints_infoPoints[] points;

        [XmlAttribute]
        public string bottom;

        [XmlAttribute]
        public string top;

        [XmlAttribute]
        public string type;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionServerZoneListWeather_zonePoints_infoPoints
    {
        [XmlElement("data", Form = XmlSchemaForm.Unqualified)]
        public MissionServerZoneListWeather_zonePoints_infoPointsData[] data;

        [XmlAttribute]
        public string points_size;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionServerZoneListWeather_zonePoints_infoPointsData
    {
        [XmlAttribute]
        public string x;

        [XmlAttribute]
        public string y;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionWeatherSystemWeatherOption
    {
        [XmlElement("Sky", Form = XmlSchemaForm.Unqualified)]
        public MissionWeatherSystemWeatherOptionSky[] Sky;

        [XmlElement("MRT_Sky", Form = XmlSchemaForm.Unqualified)]
        public MissionWeatherSystemWeatherOptionMRT_Sky[] MRT_Sky;

        [XmlElement("Fog")]
        public Fog[] Fog;

        [XmlElement("Env", Form = XmlSchemaForm.Unqualified)]
        public MissionWeatherSystemWeatherOptionEnv[] Env;

        [XmlElement("Effect", Form = XmlSchemaForm.Unqualified)]
        public MissionWeatherSystemWeatherOptionEffect[] Effect;

        [XmlElement("Sound", Form = XmlSchemaForm.Unqualified)]
        public MissionWeatherSystemWeatherOptionSound[] Sound;

        [XmlAttribute]
        public string WeatherName;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionWeatherSystemWeatherOptionSky
    {
        [XmlAttribute]
        public string SkyCloud2D;

        [XmlAttribute]
        public string SkyCloud3D;

        [XmlAttribute]
        public string SkySkyBox;

        [XmlAttribute]
        public string SkySunLensFlares;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionWeatherSystemWeatherOptionMRT_Sky
    {
        [XmlAttribute]
        public string HorizonColor;

        [XmlAttribute]
        public string MieScattering;

        [XmlAttribute]
        public string RayleighScattering;

        [XmlAttribute]
        public string SunMultiplier;

        [XmlAttribute]
        public string SunShaftDisplay;

        [XmlAttribute]
        public string SunShaftFogShadow;

        [XmlAttribute]
        public string SunWavelength_b;

        [XmlAttribute]
        public string SunWavelength_g;

        [XmlAttribute]
        public string SunWavelength_r;

        [XmlAttribute]
        public string ZenithColor;

        [XmlAttribute]
        public string ZenithShift;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionWeatherSystemWeatherOptionEnv
    {
        [XmlAttribute]
        public string EnvColor;

        [XmlAttribute]
        public string EnvMoistureAmount;

        [XmlAttribute]
        public string EnvRainbowAmount;

        [XmlAttribute]
        public string EnvThunderAmount;

        [XmlAttribute]
        public string EnvWindForce;

        [XmlAttribute]
        public string GammaDestMax;

        [XmlAttribute]
        public string GammaDestMin;

        [XmlAttribute]
        public string GammaInterpolate;

        [XmlAttribute]
        public string GammaSrcMax;

        [XmlAttribute]
        public string GammaSrcMin;

        [XmlAttribute]
        public string PhotoColor;

        [XmlAttribute]
        public string PhotocolorInterpolate;

        [XmlAttribute]
        public string SelectiveDest;

        [XmlAttribute]
        public string SelectiveInterpolate;

        [XmlAttribute]
        public string SelectiveSrc;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionWeatherSystemWeatherOptionEffect
    {
        [XmlAttribute]
        public string EffEarthQuake;

        [XmlAttribute]
        public string EffHeatHaze;

        [XmlAttribute]
        public string ParticleName;

        [XmlAttribute]
        public string ParticleType;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionWeatherSystemWeatherOptionSound
    {
        [XmlAttribute]
        public string sEarthQuake_Amb;

        [XmlAttribute]
        public string sEarthQuake_Syn;

        [XmlAttribute]
        public string sParticle_Amb;

        [XmlAttribute]
        public string sRain_Amb;

        [XmlAttribute]
        public string sThunder_Amb;

        [XmlAttribute]
        public string sThunder_Syn;

        [XmlAttribute]
        public string sWind_Amb;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionWeatherSystemWeatherSetTable
    {
        [XmlAttribute]
        public string att_ranking;

        [XmlAttribute]
        public string attribute;

        [XmlAttribute]
        public string name;

        [XmlAttribute]
        public string weather_zone_name;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionLevelOptionInitialize
    {
        [XmlAttribute]
        public string Render_Beaches;

        [XmlAttribute]
        public string Render_Fog;

        [XmlAttribute]
        public string Render_Ocean;

        [XmlAttribute]
        public string Render_SkyBox;

        [XmlAttribute]
        public string Render_Sun;

        [XmlAttribute]
        public string Render_Terrain;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionLevelOptionFly
    {
        [XmlAttribute]
        public string Fly_Whole_Level;

        [XmlAttribute]
        public string FRZ_NEGX;

        [XmlAttribute]
        public string FRZ_NEGY;

        [XmlAttribute]
        public string FRZ_X;

        [XmlAttribute]
        public string FRZ_Y;

        [XmlAttribute]
        public string MaxHeight;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionLevelOptionBindArea
    {
        [XmlAttribute]
        public string IsPossible;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionLevelOptionReCall
    {
        [XmlAttribute]
        public string IsPossible;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionLevelOptionGlide
    {
        [XmlAttribute]
        public string IsPossible;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionTimeEnvSystemTimeEnvOption
    {
        [XmlAttribute]
        public string cloud2d;

        [XmlAttribute]
        public string cloud3d;

        [XmlAttribute]
        public string env_color;

        [XmlAttribute]
        public string env_heathaze;

        [XmlAttribute]
        public string env_windforce;

        [XmlAttribute]
        public string fog_color;

        [XmlAttribute]
        public string fog_end;

        [XmlAttribute]
        public string fog_start;

        [XmlAttribute]
        public string fullscreenfx;

        [XmlAttribute]
        public string GammaDestMax;

        [XmlAttribute]
        public string GammaDestMin;

        [XmlAttribute]
        public string GammaInterpolate;

        [XmlAttribute]
        public string GammaSrcMax;

        [XmlAttribute]
        public string GammaSrcMin;

        [XmlAttribute]
        public string name;

        [XmlAttribute]
        public string parteffect;

        [XmlAttribute]
        public string particlefx;

        [XmlAttribute]
        public string PhotoColor;

        [XmlAttribute]
        public string PhotocolorInterpolate;

        [XmlAttribute]
        public string SelectiveDest;

        [XmlAttribute]
        public string SelectiveInterpolate;

        [XmlAttribute]
        public string SelectiveSrc;

        [XmlAttribute]
        public string sky_box_name;

        [XmlAttribute]
        public string sky_lens_check;

        [XmlAttribute]
        public string sky_lens_name;

        [XmlAttribute]
        public string sky_sun_check;

        [XmlAttribute]
        public string sky_sun_direction;

        [XmlAttribute]
        public string sky_sun_height;

        [XmlAttribute]
        public string time_hour;

        [XmlAttribute]
        public string time_min;

        [XmlAttribute]
        public string time_name;

        [XmlAttribute]
        public string view_dist;

        [XmlAttribute]
        public string zonename;
    }


    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionTimeEnvZoneListTimeenv_zone
    {
        [XmlElement("points_info")]
        public points_info[] points_info;

        [XmlAttribute]
        public string name;

        [XmlAttribute]
        public string priority;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionFxArtifactSystemFxArtifactOption
    {
        [XmlAttribute]
        public string Area;

        [XmlAttribute]
        public string Area1;

        [XmlAttribute]
        public string Area2;

        [XmlAttribute]
        public string Area3;

        [XmlAttribute]
        public string Area4;

        [XmlAttribute]
        public string Area5;

        [XmlAttribute]
        public string Fx;

        [XmlAttribute]
        public string Name;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionCommonShapeList
    {
        [XmlElement("flying_zone", Form = XmlSchemaForm.Unqualified)]
        public MissionCommonShapeListFlying_zonesFlying_zone[] flying_zones;

        [XmlElement("NonBeachArea", Form = XmlSchemaForm.Unqualified)]
        public MissionCommonShapeListBeach_zonesNonBeachArea[] beach_zones;

        [XmlElement("music_zone", Form = XmlSchemaForm.Unqualified)]
        public MissionCommonShapeListMusic_zonesMusic_zone[] music_zones;

        [XmlElement("subzone", Form = XmlSchemaForm.Unqualified)]
        public MissionCommonShapeListSub_zonesSubzone[] sub_zones;

        [XmlElement("artifact_result_area", Form = XmlSchemaForm.Unqualified)]
        public MissionCommonShapeListAttribute_zonesArtifact_result_area[] attribute_zones;

        [XmlElement("npcpath_boid", Form = XmlSchemaForm.Unqualified)]
        public Npcpath_boids[] npcpath_boids;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionCommonShapeListFlying_zonesFlying_zone
    {
        [XmlElement("points_info")]
        public points_info[] points_info;

        [XmlAttribute]
        public string name;

        [XmlText]
        public string value;
    }

    [System.SerializableAttribute()]
    [System.Diagnostics.DebuggerStepThroughAttribute()]
    [System.ComponentModel.DesignerCategoryAttribute("code")]
    [XmlTypeAttribute(AnonymousType = true)]
    public partial class MissionCommonShapeListBeach_zonesNonBeachArea
    {
        [XmlElement("points")]
        public points[] points;

        [XmlAttribute]
        public string ambient_max;

        [XmlAttribute]
        public string ambient_min;

        [XmlAttribute]
        public string beach_allsector;

        [XmlAttribute]
        public string beach_render;

        [XmlAttribute]
        public string beach_texture0;

        [XmlAttribute]
        public string beach_texture1;

        [XmlAttribute]
        public string beach_texture2;

        [XmlAttribute]
        public string name;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(Namespace = "", IsNullable = false)]
    public partial class points
    {
        [XmlElement("data")]
        public data[] data;

        [XmlAttribute]
        public string points_size;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionCommonShapeListMusic_zonesMusic_zone
    {
        [XmlElement("points_info")]
        public points_info[] points_info;

        [XmlAttribute]
        public string name;

        [XmlAttribute]
        public string priority;

        [XmlAttribute]
        public string theme;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionCommonShapeListSub_zonesSubzone
    {
        [XmlElement("points_info")]
        public points_info[] points_info;

        [XmlAttribute]
        public string name;

        [XmlAttribute]
        public string priority;

        [XmlAttribute]
        public string @string;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionCommonShapeListAttribute_zonesArtifact_result_area
    {
        [XmlElement("points_info")]
        public points_info[] points_info;

        [XmlAttribute]
        public string name;

        [XmlAttribute]
        public string priority;

        [XmlAttribute]
        public string @string;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class Npcpath_boids
    {
        [XmlElement("data")]
        public data[] data;

        [XmlAttribute]
        public string data_size;

        [XmlAttribute]
        public string name;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    [XmlRoot(Namespace = "", IsNullable = false)]
    public partial class data
    {
        [XmlAttribute]
        public string x;

        [XmlAttribute]
        public string y;

        [XmlAttribute]
        public string z;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionObjects
    {
        [XmlElement("Object", Form = XmlSchemaForm.Unqualified)]
        public MissionObject[] Object;

        [XmlElement("Entity")]
        public Entity[] Entity;
    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class MissionObject : ISpawnData
    {
        [XmlElement("Point", Form = XmlSchemaForm.Unqualified)]
        public PointList[] Points;

        [XmlElement("Entity", Form = XmlSchemaForm.Unqualified)]
        public Entity[] Entities;

        [XmlAttribute]
        public string subzone;

        [XmlAttribute]
        public string AffectedBySun;

        [XmlAttribute]
        public string AmbientColor;

        [XmlAttribute]
        public string Material;

        [XmlAttribute]
        public string Angles { get; set; }

        [XmlAttribute]
        public string AreaId;

        [XmlAttribute]
        public string Closed;

        [XmlAttribute]
        public string DisplayFilled;

        [XmlAttribute]
        public string DoubleSide;

        [XmlAttribute]
        public string DynAmbientColor;

        [XmlAttribute]
        public string GroupId;

        [XmlAttribute]
        public string Height;

        [XmlAttribute]
        public string HideEntitiesThroughPortals;

        [XmlAttribute]
        public string Name;

        [XmlAttribute]
        public string Pos { get; set; }

        [XmlAttribute]
        public string SkyOnly;

        [XmlAttribute]
        public ObjectTypes Type { get; set; }

        [XmlAttribute]
        public string UseInIndoors;

        [XmlAttribute]
        public string ViewDistRatio;

        [XmlAttribute]
        public string Width;

        [XmlAttribute]
        public int dir { get; set; }

        [XmlAttribute]
        public string Ignore_heightcheck;

        [XmlAttribute]
        public string iidle_range;

        string _npcName;

        [XmlAttribute]
        public string npc {
            get {
                if (_npcName == null)
                    return null;
                return _npcName.ToLower();
            }
            set {
                if (value == null)
                    value = String.Empty;
                _npcName = value;
            }
        }

        string ISpawnData.Name {
            get { return npc; }
            set { npc = value; }
        }

        [XmlIgnore]
        public string A {
            get {
                if (this.Type == ObjectTypes.HSP) {
                    GatherSource source = Utility.GatherSrcFile[_npcName];
                    if (source != null)
                        return Utility.StringIndex.GetString(source.desc);
                } else if (this.Type == ObjectTypes.SP) {
                    int npcId = Utility.ClientNpcIndex[_npcName];
                    if (npcId != -1) {
                        var npc = Utility.ClientNpcIndex[npcId];
                        return Utility.StringIndex.GetString(npc.desc);
                    }
                }
                return null;
            }
        }

        [XmlAttribute]
        public int use_dir { get; set; }

        [XmlAttribute]
        public string TerrainAlign;

        [XmlAttribute]
        public string GeomId;

        [XmlAttribute]
        public string LodDist;

        [XmlAttribute]
        public string LowLODId;

        [XmlAttribute]
        public string MergeGeom;

        [XmlAttribute]
        public string UseLOD;

        [XmlAttribute]
        public string Group;

        [XmlAttribute]
        public string Scale;

        [XmlAttribute]
        public string Radius;

        [XmlAttribute]
        public string FadeInZone;

        [XmlAttribute]
        public string Length;

        [XmlAttribute]
        public string UseDeepness;

        [XmlAttribute]
        public string AffectToVolFog;

        [XmlAttribute]
        public string WaterGeometry;

        [XmlAttribute]
        public string WaterRender;

        [XmlAttribute]
        public string WaterShader;

        [XmlAttribute]
        public string BoxMax;

        [XmlAttribute]
        public string BoxMin;

        [XmlAttribute]
        public string Color;

        [XmlAttribute]
        public string Shader;

        [XmlAttribute]
        public string ViewDistance;

        [XmlIgnore]
        public int EntityId {
            get { return 0; }
            set { }
        }

    }

    [Serializable]
    [XmlType(AnonymousType = true)]
    public partial class PointList
    {
        [XmlAttribute]
        public string Pos;
    }

    [Serializable]
    public enum ObjectTypes
    {
        None,
        AreaBox,
        AreaSphere,
        Camera,
        DP,
        Entity,
        FogVolume,
        Group,
        HSP,
        KP,
        Location,
        MP,
        OccluderArea,
        Portal,
        RandomAmbient,
        SeedPoint,
        Shape,
        SimpleEntity,
        SoundSpot,
        SP,
        StartPoint,
        VisArea,
        WaterVolume
    }
}
