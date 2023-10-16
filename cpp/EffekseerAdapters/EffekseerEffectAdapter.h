#pragma once

#include "Effekseer.h"
#include "RefWrappers.h"
// Undefine min/max function
#ifdef max
    #undef max
#endif
#ifdef min
    #undef min
#endif
#include "Effekseer/Effekseer.EffectNode.h"
#include "Effekseer/Effekseer.EffectNodeRoot.h"

class EffekseerManagerAdapter;

enum class EffekseerTextureType
{
    Color,
    Normal,
    Distortion,
};

class EffekseerEffectAdapter
{
private:
    Effekseer::EffectRef effect = nullptr;

public:
    EffekseerEffectAdapter();
    ~EffekseerEffectAdapter();

    const char16_t* GetName();
    void SetName(const char16_t*name);
    int GetVersion();

    bool load(EffekseerManagerAdapter *manager, const unsigned char* data, int len, float magnification);

    int32_t GetTextureCount(EffekseerTextureType type) const;
    const char16_t* GetTexturePath(int32_t index, EffekseerTextureType type) const;
    TextureRefWrapper LoadTexture(const unsigned char* data, int len, int32_t index, EffekseerTextureType type, bool isMipMapEnabled);
    void SetTexture(int32_t index, EffekseerTextureType type, TextureRefWrapper texture);
    // bool LoadTexturePath(const EFK_CHAR *data, EffekseerTextureType type);
    bool HasTextureLoaded(int32_t index, EffekseerTextureType type);

    int32_t GetModelCount() const;
    const char16_t* GetModelPath(int32_t index) const;
    ModelRefWrapper LoadModel(const unsigned char* data, int len, int32_t index);
    void SetModel(int32_t index, ModelRefWrapper model);
    bool HasModelLoaded(int32_t index);

    int32_t GetMaterialCount() const;
    const char16_t* GetMaterialPath(int32_t index) const;
    MaterialRefWrapper LoadMaterial(const unsigned char* data, int len, int32_t index);
    void SetMaterial(int32_t index, MaterialRefWrapper material);
    bool HasMaterialLoaded(int32_t index);

    int32_t NodeCount();
    Effekseer::EffectNodeRoot* GetRootNode();

#ifndef SWIG
    Effekseer::EffectRef GetInternal() const;
#endif

};
