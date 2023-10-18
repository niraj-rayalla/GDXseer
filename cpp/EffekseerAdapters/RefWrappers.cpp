#include "RefWrappers.h"

TextureRefWrapper::TextureRefWrapper(Effekseer::TextureRef textureRef) {
    hasRef = textureRef != nullptr;
    this->textureRef = textureRef;
}

TextureRefWrapper::~TextureRefWrapper() {
    textureRef = nullptr;
}




ModelRefWrapper::ModelRefWrapper(Effekseer::ModelRef modelRef) {
    hasRef = modelRef != nullptr;
    this->modelRef = modelRef;
}

ModelRefWrapper::~ModelRefWrapper() {
    modelRef = nullptr;
}




MaterialRefWrapper::MaterialRefWrapper(Effekseer::MaterialRef materialRef) {
    hasRef = materialRef != nullptr;
    this->materialRef = materialRef;
}

MaterialRefWrapper::~MaterialRefWrapper() {
    materialRef = nullptr;
}




EffekseerRendererRefWrapper::EffekseerRendererRefWrapper(EffekseerRenderer::RendererRef rendererRef) {
    hasRef = rendererRef != nullptr;
    this->rendererRef = rendererRef;
}

EffekseerRendererRefWrapper::~EffekseerRendererRefWrapper() {
    rendererRef = nullptr;
}