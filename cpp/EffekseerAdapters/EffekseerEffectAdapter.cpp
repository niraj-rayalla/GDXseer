#define NOMINMAX

#include <string>
#include "EffekseerEffectAdapter.h"
#include "EffekseerManagerAdapter.h"

static std::string convert(const char16_t* path)
{
    int start = 0;
    int end = 0;

    for (int i = 0; path[i] != 0; i++)
    {
        if (path[i] == u'/' || path[i] == u'\\')
        {
            start = i + 1;
        }
    }

    for (int i = start; path[i] != 0; i++)
    {
        if (path[i] == u'.')
        {
            end = i;
        }
    }

    std::vector<char> ret;

    for (int i = start; i < end; i++)
    {
        ret.push_back(path[i]);
    }
    ret.push_back(0);

    return std::string(ret.data());
}

EffekseerEffectAdapter::EffekseerEffectAdapter() { }
EffekseerEffectAdapter::~EffekseerEffectAdapter() { }

const char16_t* EffekseerEffectAdapter::GetName() {
    if (effect == nullptr) {
        return nullptr;
    }

    return effect->GetName();
}

void EffekseerEffectAdapter::SetName(const char16_t*name) {
    if (effect == nullptr) {
        return;
    }

    return effect->SetName(name);
}

int EffekseerEffectAdapter::GetVersion() {
    if (effect == nullptr) {
        return -1;
    }

    return effect->GetVersion();
}

bool EffekseerEffectAdapter::load(EffekseerManagerAdapter *manager, const unsigned char *data, int len, float magnification) {
    this->effect =  ::Effekseer:: Effect::Create (manager->manager, (void *) data, len, magnification);
    return this->effect != nullptr;
}

const char16_t *EffekseerEffectAdapter::GetTexturePath(int32_t index, EffekseerTextureType type) const {
    if (type == EffekseerTextureType::Color) {
        return effect->GetColorImagePath(index);
    } else if (type == EffekseerTextureType::Normal) {
        return effect->GetNormalImagePath(index);
    } else if (type == EffekseerTextureType::Distortion) {
        return effect->GetDistortionImagePath(index);
    }

    return nullptr;
}

int32_t EffekseerEffectAdapter::GetTextureCount(EffekseerTextureType type) const {
    if (type == EffekseerTextureType::Color) {
        return effect->GetColorImageCount();
    } else if (type == EffekseerTextureType::Normal) {
        return effect->GetNormalImageCount();
    } else if (type == EffekseerTextureType::Distortion) {
        return effect->GetDistortionImageCount();
    }

    return 0;
}

TextureRefWrapper EffekseerEffectAdapter::LoadTexture(const unsigned char *data, int len, int32_t index, EffekseerTextureType type, bool isMipMapEnabled) {
    auto loader = effect->GetSetting()->GetTextureLoader();
    if (loader == nullptr) {
        return TextureRefWrapper(nullptr);
    }

    auto texture = loader->Load((const void *) data, len, (Effekseer::TextureType) type, isMipMapEnabled);
    if (texture == nullptr) {
        return TextureRefWrapper(nullptr);
    }

    return TextureRefWrapper(texture);
}

void EffekseerEffectAdapter::SetTexture(int32_t index, EffekseerTextureType type, TextureRefWrapper texture) {
    effect->SetTexture(index, (Effekseer::TextureType) type, texture.textureRef);
}

bool EffekseerEffectAdapter::HasTextureLoaded(int32_t index, EffekseerTextureType type) {

    if (type != EffekseerTextureType::Color)
        return effect->GetColorImage(index) != nullptr;

    if (type != EffekseerTextureType::Normal)
        return effect->GetNormalImage(index) != nullptr;

    if (type != EffekseerTextureType::Distortion)
        return effect->GetDistortionImage(index) != nullptr;

    return false;
}

const char16_t * EffekseerEffectAdapter::GetModelPath(int32_t index) const { return effect->GetModelPath(index); }

int32_t EffekseerEffectAdapter::GetModelCount() const { return effect->GetModelCount(); }

ModelRefWrapper EffekseerEffectAdapter::LoadModel(const unsigned char* data, int len, int32_t index) {
    auto loader = effect->GetSetting()->GetModelLoader();
    if (loader == nullptr) {
        return ModelRefWrapper(nullptr);
    }

    auto model = loader->Load((const void *) data, len);
    if (model == nullptr) {
        return ModelRefWrapper(nullptr);
    }

    return ModelRefWrapper(model);
}

void EffekseerEffectAdapter::SetModel(int32_t index, ModelRefWrapper model) {
    effect->SetModel(index, model.modelRef);
}

bool EffekseerEffectAdapter::HasModelLoaded(int32_t index) { return effect->GetModel(index) != nullptr; }

MaterialRefWrapper EffekseerEffectAdapter::LoadMaterial(const unsigned char* data, int len, int32_t index) {
    auto loader = effect->GetSetting()->GetMaterialLoader();
    if (loader == nullptr) {
        return MaterialRefWrapper(nullptr);
    }

    auto material = loader->Load((const void *) data, len, Effekseer::MaterialFileType::Code);
    if (material == nullptr) {
        return MaterialRefWrapper(nullptr);
    }

    return MaterialRefWrapper(material);
}

void EffekseerEffectAdapter::SetMaterial(int32_t index, MaterialRefWrapper material) {
    effect->SetMaterial(index, material.materialRef);
}

bool EffekseerEffectAdapter::HasMaterialLoaded(int32_t index) { return effect->GetMaterial(index) != nullptr; }

int32_t EffekseerEffectAdapter::GetMaterialCount() const { return effect->GetMaterialCount(); }

int32_t EffekseerEffectAdapter::NodeCount()
{
    int i = effect->GetRoot()->GetChildrenCount();
    return i;
}


Effekseer::EffectNodeRoot* EffekseerEffectAdapter::GetRootNode() {
    return static_cast<Effekseer::EffectNodeRoot*>(effect->GetRoot());
}

Effekseer::EffectRef EffekseerEffectAdapter::GetInternal() const { return effect; }

const char16_t * EffekseerEffectAdapter::GetMaterialPath(int32_t index) const { return effect->GetMaterialPath(index); }