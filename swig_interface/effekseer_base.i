// All needed enum classes and structs in Effekseer.Base.Pre.h
%rename("MaterialTextureParameter", fullname=1) "Effekseer::MaterialTextureParameter";
%rename("MaterialRenderData", fullname=1) "Effekseer::MaterialRenderData";
%rename("IRandObject", fullname=1) "Effekseer::IRandObject";
%rename("getRandInt", fullname=1) "Effekseer::IRandObject::GetRandInt";
%rename("getRand", fullname=1) "Effekseer::IRandObject::GetRand";
// std wrappers
namespace std {
   %template(VectorMaterialTextureParameter) vector<Effekseer::MaterialTextureParameter>;
};
%rename("NodeRendererDepthParameter", fullname=1) "Effekseer::NodeRendererDepthParameter";
%rename("NodeRendererFlipbookParameter", fullname=1) "Effekseer::NodeRendererFlipbookParameter";
%rename("NodeRendererBasicParameter", fullname=1) "Effekseer::NodeRendererBasicParameter";
%rename("NodeRendererBasicParameter", fullname=1) "Effekseer::NodeRendererBasicParameter";
%rename("Gradient", fullname=1) "Effekseer::Gradient";
%rename("ColorKey", fullname=1) "Effekseer::Gradient::ColorKey";
%rename("AlphaKey", fullname=1) "Effekseer::Gradient::AlphaKey";
// std wrappers
namespace Effekseer {
    //! the maximum number of texture slot including textures system specified
    const int32_t TextureSlotMax = 8;
    enum class TextureFilterType : int32_t;
    enum class TextureWrapType : int32_t;
};
namespace std {
   %template(ArrayTextureIndexes) array<int32_t, Effekseer::TextureSlotMax>;
   %template(ArrayTextureFilters) array<Effekseer::TextureFilterType, Effekseer::TextureSlotMax>;
   %template(ArrayGradientColors) array<Effekseer::Gradient::ColorKey, 8>;
   %template(ArrayGradientAlphas) array<Effekseer::Gradient::AlphaKey, 8>;
};
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.Base.Pre.h"

// All enum classes in Effekseer.Base.h
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.Base.h"

// All structs in Effekseer.InternalStruct.h
%rename("InternalStructRandomFloat", fullname=1) "Effekseer::random_float";
%rename("reset", fullname=1) "Effekseer::random_float::reset";
%rename("getValue", fullname=1) "Effekseer::random_float::getValue";
%rename("InternalStructRandomInt", fullname=1) "Effekseer::random_int";
%rename("getValue", fullname=1) "Effekseer::random_int::getValue";
%rename("reset", fullname=1) "Effekseer::random_int::reset";
%rename("InternalStructVector2D", fullname=1) "Effekseer::vector2d";
%rename("InternalStructRectF", fullname=1) "Effekseer::rectf";
%rename("reset", fullname=1) "Effekseer::rectf::reset";
%rename("InternalStructRandomVector2D", fullname=1) "Effekseer::random_vector2d";
%rename("reset", fullname=1) "Effekseer::random_vector2d::reset";
%rename("getValue", fullname=1) "Effekseer::random_vector2d::getValue";
%rename("InternalStructFloatWithoutRandom", fullname=1) "Effekseer::easing_float_without_random";
%rename("InternalStructEasingFloat", fullname=1) "Effekseer::easing_float";
%rename("getValue", fullname=1) "Effekseer::easing_float::getValue";
%rename("InternalStructEasingVector2D", fullname=1) "Effekseer::easing_vector2d";
%rename("getValue", fullname=1) "Effekseer::easing_vector2d::getValue";
%rename("InternalStructVector3D", fullname=1) "Effekseer::vector3d";
%rename("InternalStructRandomVector3D", fullname=1) "Effekseer::random_vector3d";
%rename("reset", fullname=1) "Effekseer::random_vector3d::reset";
%rename("getValue", fullname=1) "Effekseer::random_vector3d::getValue";
%rename("InternalStructEasingVector3D", fullname=1) "Effekseer::easing_vector3d";
%rename("getValue", fullname=1) "Effekseer::easing_vector3d::getValue";
%rename("InternalStructRandomColor", fullname=1) "Effekseer::random_color";
%rename("reset", fullname=1) "Effekseer::random_color::reset";
%rename("getValue", fullname=1) "Effekseer::random_color::getValue";
%rename("getDirectValue", fullname=1) "Effekseer::random_color::getDirectValue";
//%rename("load", fullname=1) "Effekseer::random_color::load";
%rename("InternalStructEasingColor", fullname=1) "Effekseer::easing_color";
%rename("setValueToArg", fullname=1) "Effekseer::easing_color::setValueToArg";
%rename("getStartValue", fullname=1) "Effekseer::easing_color::getStartValue";
%rename("getEndValue", fullname=1) "Effekseer::easing_color::getEndValue";
//%rename("load", fullname=1) "Effekseer::easing_color::load";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.InternalStruct.h"