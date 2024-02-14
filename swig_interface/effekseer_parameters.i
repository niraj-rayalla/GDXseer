// All classes in Parameter/Effekseer.Parameters.h
%rename("TextureUVType", fullname=1) "Effekseer::TextureUVType";
%rename("NodeRendererTextureUVTypeParameter", fullname=1) "Effekseer::NodeRendererTextureUVTypeParameter";
%rename("FalloffParameter", fullname=1) "Effekseer::FalloffParameter";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Parameter/Effekseer.Parameters.h"

// All structs in Parameter/DynamicParameter.h
%rename("DynamicFactorParameter", fullname=1) "Effekseer::DynamicFactorParameter";
%rename("RefMinMax", fullname=1) "Effekseer::RefMinMax";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Parameter/DynamicParameter.h"

// All classes in Parameter/BasicSettings.h
%rename("ParameterCommonValues", fullname=1) "Effekseer::ParameterCommonValues";
//%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Parameter/BasicSettings.h"

// ParameterEasingFloat in Parameter/Easing.h
%rename("ParameterEasingFloat", fullname=1) "Effekseer::ParameterEasingFloat";
// ParameterEasingSIMDVec3 in Parameter/Easing.h
%rename("ParameterEasingSIMDVec3", fullname=1) "Effekseer::ParameterEasingSIMDVec3";

%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Parameter/Easing.h"
// InstanceEasing in Parameter/Easing.h
namespace Effekseer {
    // InstanceEasing<float>
    %rename(InstanceEasingFloat) InstanceEasing<float>;
    struct InstanceEasing<float> {
        float start;
        float middle;
        float end;
        float Rate;
    };
    // InstanceEasing<SIMD::Vec3f>
    %rename(InstanceEasingSIMDVec3f) InstanceEasing<SIMD::Vec3f>;
    struct InstanceEasing<SIMD::Vec3f> {
        SIMD::Vec3f start;
        SIMD::Vec3f middle;
        SIMD::Vec3f end;
        float Rate;
    };

    // ParameterEasing<float>
    %rename(ParameterEasingFloatBase) ParameterEasing<float>;
    struct ParameterEasing<float> {
        RefMinMax RefEqS;
        RefMinMax RefEqE;
        RefMinMax RefEqM;

        Easing3Type type_ = Easing3Type::StartEndSpeed;
        std::array<float, 4> params;

        int32_t channelCount = 0;

        bool isMiddleEnabled = false;

        bool isIndividualEnabled = false;

        virtual float GetValue(const InstanceEasing<float>& instance, float time) const = 0;
    };
    // ParameterEasing<SIMD::Vec3f>
    %rename(ParameterEasingSIMDVec3Base) ParameterEasing<SIMD::Vec3f>;
    struct ParameterEasing<SIMD::Vec3f> {
        RefMinMax RefEqS;
        RefMinMax RefEqE;
        RefMinMax RefEqM;

        Easing3Type type_ = Easing3Type::StartEndSpeed;
        std::array<float, 4> params;

        int32_t channelCount = 0;

        bool isMiddleEnabled = false;

        bool isIndividualEnabled = false;

        virtual SIMD::Vec3f GetValue(const InstanceEasing<SIMD::Vec3f>& instance, float time) const = 0;
    };
}


// All classes/structs in Parameter/AllTypeColor.h
%rename("AllTypeColorParameter", fullname=1) "Effekseer::AllTypeColorParameter";
%rename($ignore, fullname=1) "Effekseer::AllTypeColorParameter::gradient";
%rename("InstanceAllTypeColorState", fullname=1) "Effekseer::InstanceAllTypeColorState";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Parameter/AllTypeColor.h"

%inline %{
namespace Effekseer {
    enum class UVAnimationLoopType : int32_t
    {
        LOOPTYPE_ONCE = 0,
        LOOPTYPE_LOOP = 1,
        LOOPTYPE_REVERSELOOP = 2,

        LOOPTYPE_DWORD = 0x7fffffff,
    };
    enum class UVAnimationInterpolationType : int32_t
    {
        NONE = 0,
        LERP = 1,
    };
}
%}
// All classes/structs in Parameter/UV.h
%rename("UVAnimationType", fullname=1) "Effekseer::UVAnimationType";
%rename("UVParameter", fullname=1) "Effekseer::UVParameter";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Parameter/UV.h"

// All classes/structs in Parameter/LOD.h
%rename("ParameterLODs", fullname=1) "Effekseer::ParameterLODs";
//%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Parameter/LOD.h"

// All classes/structs in Parameter/KillRules.h
%rename("KillRulesParameter", fullname=1) "Effekseer::KillRulesParameter";
//%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Parameter/KillRules.h"

// All classes/structs in Parameter/Translation.h
%rename("ParameterTranslationType", fullname=1) "Effekseer::ParameterTranslationType";
%rename("ParameterTranslationFixed", fullname=1) "Effekseer::ParameterTranslationFixed";
%rename("ParameterTranslationPVA", fullname=1) "Effekseer::ParameterTranslationPVA";
%rename("ParameterTranslationEasing", fullname=1) "Effekseer::ParameterTranslationEasing";
%rename("ParameterTranslationNurbsCurve", fullname=1) "Effekseer::ParameterTranslationNurbsCurve";
%rename("ParameterTranslationViewOffset", fullname=1) "Effekseer::ParameterTranslationViewOffset";
%rename("TranslationParameter", fullname=1) "Effekseer::TranslationParameter";
%rename($ignore, fullname=1) "Effekseer::TranslationParameter::TranslationFCurve";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Parameter/Translation.h"

// All classes/structs in Parameter/Rotation.h
%rename("ParameterRotationFixed", fullname=1) "Effekseer::ParameterRotationFixed";
%rename("ParameterRotationPVA", fullname=1) "Effekseer::ParameterRotationPVA";
%rename("ParameterRotationEasing", fullname=1) "Effekseer::ParameterRotationEasing";
%rename("ParameterRotationAxisPVA", fullname=1) "Effekseer::ParameterRotationAxisPVA";
%rename("ParameterRotationAxisEasing", fullname=1) "Effekseer::ParameterRotationAxisEasing";
%rename("RotationParameter", fullname=1) "Effekseer::RotationParameter";
%rename($ignore, fullname=1) "Effekseer::RotationParameter::RotationFCurve";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Parameter/Rotation.h"

// All classes/structs in Parameter/Scaling.h
%rename("ParameterScalingFixed", fullname=1) "Effekseer::ParameterScalingFixed";
%rename("ParameterScalingPVA", fullname=1) "Effekseer::ParameterScalingPVA";
%rename("ParameterScalingEasing", fullname=1) "Effekseer::ParameterScalingEasing";
%rename("ParameterScalingSinglePVA", fullname=1) "Effekseer::ParameterScalingSinglePVA";
%rename("ScalingParameter", fullname=1) "Effekseer::ScalingParameter";
%rename($ignore, fullname=1) "Effekseer::ScalingParameter::ScalingFCurve";
%rename($ignore, fullname=1) "Effekseer::ScalingParameter::ScalingSingleFCurve";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Parameter/Scaling.h"

// All classes/structs in Parameter/SpawnMethod.h
%rename("ParameterGenerationLocation", fullname=1) "Effekseer::ParameterGenerationLocation";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Parameter/SpawnMethod.h"

// All classes/structs in Parameter/DepthParameter.h
%rename("ParameterDepthValues", fullname=1) "Effekseer::ParameterDepthValues";
//%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Parameter/DepthParameter.h"

// All classes/structs in Parameter/AlphaCutoff.h
%rename("ParameterCustomDataFixed", fullname=1) "Effekseer::ParameterCustomDataFixed";
%rename("ParameterCustomDataRandom", fullname=1) "Effekseer::ParameterCustomDataRandom";
%rename("ParameterCustomDataEasing", fullname=1) "Effekseer::ParameterCustomDataEasing";
%rename("ParameterCustomDataFCurve", fullname=1) "Effekseer::ParameterCustomDataFCurve";
%rename("ParameterCustomDataFCurveColor", fullname=1) "Effekseer::ParameterCustomDataFCurveColor";
%rename("ParameterAlphaCutoff", fullname=1) "Effekseer::ParameterAlphaCutoff";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Parameter/AlphaCutoff.h"

// All classes/structs in Parameter/Sound.h
%rename("ParameterSound", fullname=1) "Effekseer::ParameterSound";
//%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Parameter/Sound.h"

// All classes/structs in Parameter/CustomData.h
%rename("ParameterCustomData", fullname=1) "Effekseer::ParameterCustomData";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Parameter/CustomData.h"