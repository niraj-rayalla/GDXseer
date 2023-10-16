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
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.EffectNode.h"

// Root node
%rename("EffectNodeRoot", fullname=1) "Effekseer::EffectNodeRoot";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.EffectNodeRoot.h"

// Sprite node
%rename("SpriteColorParameterCore", fullname=1) "Effekseer::SpriteColorParameter";
%rename("SpritePositionParameterCore", fullname=1) "Effekseer::SpritePositionParameter";
%rename("EffectNodeSprite", fullname=1) "Effekseer::EffectNodeSprite";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.EffectNodeSprite.h"
%rename("SpriteRenderer", fullname=1) "Effekseer::SpriteRenderer";
%rename("NodeParameter", fullname=1) "Effekseer::SpriteRenderer::NodeParameter";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Renderer/Effekseer.SpriteRenderer.h"

// Ribbon node
// Don't know why NodeParameter and InstanceParameter are not being wrapped
%rename("RibbonColorParameterCore", fullname=1) "Effekseer::RibbonColorParameter";
%rename("RibbonPositionParameterCore", fullname=1) "Effekseer::RibbonPositionParameter";
%rename("EffectNodeRibbon", fullname=1) "Effekseer::EffectNodeRibbon";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.EffectNodeRibbon.h"
%rename("RibbonRenderer", fullname=1) "Effekseer::RibbonRenderer";
%rename("NodeParameter", fullname=1) "Effekseer::RibbonRenderer::NodeParameter";
%rename("InstanceParameter", fullname=1) "Effekseer::RibbonRenderer::InstanceParameter";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Renderer/Effekseer.RibbonRenderer.h"

// Track node
%rename("TrackSizeParameterCore", fullname=1) "Effekseer::TrackSizeParameter";
%rename("EffectNodeTrack", fullname=1) "Effekseer::EffectNodeTrack";
%rename("InstanceGroupValues", fullname=1) "Effekseer::EffectNodeTrack::InstanceGroupValues";
%rename("Size", fullname=1) "Effekseer::EffectNodeTrack::InstanceGroupValues::Size";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.EffectNodeTrack.h"
%rename("TrackRenderer", fullname=1) "Effekseer::TrackRenderer";
%rename("NodeParameter", fullname=1) "Effekseer::TrackRenderer::NodeParameter";
%rename("InstanceParameter", fullname=1) "Effekseer::TrackRenderer::InstanceParameter";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Renderer/Effekseer.TrackRenderer.h"

// Ring node
%rename("RingSingleParameterCore", fullname=1) "Effekseer::RingSingleParameter";
%rename("RingLocationParameterCore", fullname=1) "Effekseer::RingLocationParameter";
%rename("RingColorParameterCore", fullname=1) "Effekseer::RingColorParameter";
%rename("RingSingleValuesCore", fullname=1) "Effekseer::RingSingleValues";
%rename("RingLocationValuesCore", fullname=1) "Effekseer::RingLocationValues";
%rename("RingColorValuesCore", fullname=1) "Effekseer::RingColorValues";
%rename("RingShapeParameterCore", fullname=1) "Effekseer::RingShapeParameter";
%rename("EffectNodeRing", fullname=1) "Effekseer::EffectNodeRing";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.EffectNodeRing.h"

// Model node
%rename("EffectNodeModel", fullname=1) "Effekseer::EffectNodeModel";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.EffectNodeModel.h"
%rename("ModelRenderer", fullname=1) "Effekseer::ModelRenderer";
%rename("NodeParameter", fullname=1) "Effekseer::ModelRenderer::NodeParameter";
%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Renderer/Effekseer.ModelRenderer.h"