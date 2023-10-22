// All needed enum classes and structs in Effekseer.EffectNode.h
%rename("TriggerType", fullname=1) "Effekseer::TriggerType";
%rename("TriggerValues", fullname=1) "Effekseer::TriggerValues";
%rename("TriggerParameter", fullname=1) "Effekseer::TriggerParameter";

%rename("SteeringBehaviorParameter", fullname=1) "Effekseer::SteeringBehaviorParameter";

%rename("ParameterRendererCommon", fullname=1) "Effekseer::ParameterRendererCommon";
// Ignore ParameterRendererCommon::UVs because ParameterRendererCommon will be extended to return an element in it at a given index
%rename($ignore, fullname=1) "Effekseer::ParameterRendererCommon::UVs";

%rename("EffectNodeImplemented", fullname=1) "Effekseer::EffectNodeImplemented";
%rename("getType", fullname=1) "Effekseer::EffectNodeImplemented::GetType";
%rename($ignore, fullname=1) "Effekseer::EffectNodeImplemented::TranslationParam";
%rename($ignore, fullname=1) "Effekseer::EffectNodeImplemented::LocalForceField";
%rename($ignore, fullname=1) "Effekseer::EffectNodeImplemented::RotationParam";
%rename($ignore, fullname=1) "Effekseer::EffectNodeImplemented::ScalingParam";
%rename($ignore, fullname=1) "Effekseer::EffectNodeImplemented::AlphaCutoff";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.EffectNode.h"

// Root node
%rename("EffectNodeRoot", fullname=1) "Effekseer::EffectNodeRoot";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.EffectNodeRoot.h"

// Sprite node
%rename("SpriteColorParameter", fullname=1) "Effekseer::SpriteColorParameter";
%rename("SpritePositionParameter", fullname=1) "Effekseer::SpritePositionParameter";
%rename("EffectNodeSprite", fullname=1) "Effekseer::EffectNodeSprite";
%rename($ignore, fullname=1) "Effekseer::EffectNodeSprite::SpriteAllColor";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.EffectNodeSprite.h"
%rename("SpriteRenderer", fullname=1) "Effekseer::SpriteRenderer";
%rename("NodeParameter", fullname=1) "Effekseer::SpriteRenderer::NodeParameter";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Renderer/Effekseer.SpriteRenderer.h"

// Ribbon node
// Don't know why NodeParameter and InstanceParameter are not being wrapped
%rename("RibbonColorParameter", fullname=1) "Effekseer::RibbonColorParameter";
%rename("RibbonPositionParameter", fullname=1) "Effekseer::RibbonPositionParameter";
%rename("EffectNodeRibbon", fullname=1) "Effekseer::EffectNodeRibbon";
%rename($ignore, fullname=1) "Effekseer::EffectNodeRibbon::RibbonAllColor";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.EffectNodeRibbon.h"
%rename("RibbonRenderer", fullname=1) "Effekseer::RibbonRenderer";
%rename("NodeParameter", fullname=1) "Effekseer::RibbonRenderer::NodeParameter";
%rename("InstanceParameter", fullname=1) "Effekseer::RibbonRenderer::InstanceParameter";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Renderer/Effekseer.RibbonRenderer.h"

// Track node
%rename("TrackSizeParameter", fullname=1) "Effekseer::TrackSizeParameter";
%rename("EffectNodeTrack", fullname=1) "Effekseer::EffectNodeTrack";
%rename($ignore, fullname=1) "Effekseer::EffectNodeTrack::TrackColorLeft";
%rename($ignore, fullname=1) "Effekseer::EffectNodeTrack::TrackColorCenter";
%rename($ignore, fullname=1) "Effekseer::EffectNodeTrack::TrackColorRight";
%rename($ignore, fullname=1) "Effekseer::EffectNodeTrack::TrackColorLeftMiddle";
%rename($ignore, fullname=1) "Effekseer::EffectNodeTrack::TrackColorCenterMiddle";
%rename($ignore, fullname=1) "Effekseer::EffectNodeTrack::TrackColorRightMiddle";
%rename("InstanceGroupValues", fullname=1) "Effekseer::EffectNodeTrack::InstanceGroupValues";
%rename("Size", fullname=1) "Effekseer::EffectNodeTrack::InstanceGroupValues::Size";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.EffectNodeTrack.h"
%rename("TrackRenderer", fullname=1) "Effekseer::TrackRenderer";
%rename("NodeParameter", fullname=1) "Effekseer::TrackRenderer::NodeParameter";
%rename("InstanceParameter", fullname=1) "Effekseer::TrackRenderer::InstanceParameter";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Renderer/Effekseer.TrackRenderer.h"

// Ring node
%rename("RingSingleParameter", fullname=1) "Effekseer::RingSingleParameter";
%rename("RingLocationParameter", fullname=1) "Effekseer::RingLocationParameter";
%rename("RingColorParameter", fullname=1) "Effekseer::RingColorParameter";
%rename("RingSingleValues", fullname=1) "Effekseer::RingSingleValues";
%rename("RingLocationValues", fullname=1) "Effekseer::RingLocationValues";
%rename("RingColorValues", fullname=1) "Effekseer::RingColorValues";
%rename("RingShapeParameter", fullname=1) "Effekseer::RingShapeParameter";
%rename("EffectNodeRing", fullname=1) "Effekseer::EffectNodeRing";
%rename($ignore, fullname=1) "Effekseer::EffectNodeRing::OuterColor";
%rename($ignore, fullname=1) "Effekseer::EffectNodeRing::CenterColor";
%rename($ignore, fullname=1) "Effekseer::EffectNodeRing::InnerColor";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.EffectNodeRing.h"

// Model node
%rename("EffectNodeModel", fullname=1) "Effekseer::EffectNodeModel";
%rename($ignore, fullname=1) "Effekseer::EffectNodeModel::AllColor";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.EffectNodeModel.h"
%rename("ModelRenderer", fullname=1) "Effekseer::ModelRenderer";
%rename("NodeParameter", fullname=1) "Effekseer::ModelRenderer::NodeParameter";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Renderer/Effekseer.ModelRenderer.h"