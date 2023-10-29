#pragma once

#include <Effekseer/Dev/Cpp/Effekseer/Effekseer.h>
#include <Effekseer/Dev/Cpp/EffekseerRendererCommon/EffekseerRenderer.Renderer.h>

// TextureRef
class TextureRefWrapper {
private:
    Effekseer::TextureRef textureRef = nullptr;
    TextureRefWrapper(Effekseer::TextureRef textureRef);

public:
    bool hasRef = false;
    ~TextureRefWrapper();

friend class EffekseerEffectAdapter;
};

// ModelRef
class ModelRefWrapper {
private:
    Effekseer::ModelRef modelRef = nullptr;
    ModelRefWrapper(Effekseer::ModelRef modelRef);

public:
    bool hasRef = false;
    ~ModelRefWrapper();

friend class EffekseerEffectAdapter;
};

// MaterialRef
class MaterialRefWrapper {
private:
    Effekseer::MaterialRef materialRef = nullptr;
    MaterialRefWrapper(Effekseer::MaterialRef materialRef);

public:
    bool hasRef = false;
    ~MaterialRefWrapper();

friend class EffekseerEffectAdapter;
};


// RendererRef
class EffekseerRendererRefWrapper {
private:
    EffekseerRenderer::RendererRef rendererRef = nullptr;
    EffekseerRendererRefWrapper(EffekseerRenderer::RendererRef rendererRef);

public:
    bool hasRef = false;
    ~EffekseerRendererRefWrapper();

    friend class EffekseerManagerAdapter;
    friend class EffekseerGLManagerAdapter;
    friend class EffekseerMetalManagerAdapter;
};
