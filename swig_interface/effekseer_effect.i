// All needed enum classes and structs in Effekseer.Effect.h
%rename("EffectTerm", fullname=1) "Effekseer::EffectTerm";
%rename("EffectInstanceTerm", fullname=1) "Effekseer::EffectInstanceTerm";
%rename("EffekseerEffect", fullname=1) "Effekseer::Effect";
%rename("EffectBasicRenderParameter", fullname=1) "Effekseer::EffectBasicRenderParameter";
%rename("EffectModelParameter", fullname=1) "Effekseer::EffectModelParameter";

%rename("EffectNode", fullname=1) "Effekseer::EffectNode";
%rename("getEffect", fullname=1) "Effekseer::EffectNode::GetEffect";
%rename("getType", fullname=1) "Effekseer::EffectNode::GetType";
%rename("getGeneration", fullname=1) "Effekseer::EffectNode::GetGeneration";
%rename("getChildrenCount", fullname=1) "Effekseer::EffectNode::GetChildrenCount";
%rename("getNonImplementedChild", fullname=1) "Effekseer::EffectNode::GetChild";
%rename("getBasicRenderParameter", fullname=1) "Effekseer::EffectNode::GetBasicRenderParameter";
%rename("setBasicRenderParameter", fullname=1) "Effekseer::EffectNode::SetBasicRenderParameter";
%rename("getEffectModelParameter", fullname=1) "Effekseer::EffectNode::GetEffectModelParameter";
%rename("calculateInstanceTerm", fullname=1) "Effekseer::EffectNode::CalculateInstanceTerm";

%include "/cpp/Effekseer/Dev/Cpp/Effekseer/Effekseer/Effekseer.Effect.h"