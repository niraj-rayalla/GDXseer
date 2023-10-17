#include "EffekseerGLManagerAdapter.h"

EffekseerGLManagerAdapter::EffekseerGLManagerAdapter(
    int32_t spriteMaxCount, EffekseerRendererGL::OpenGLDeviceType openGLDeviceType, bool autoFlip
): EffekseerManagerAdapter(spriteMaxCount, autoFlip) {
    this->openGLDeviceType = openGLDeviceType;
}

EffekseerRendererRefWrapper EffekseerGLManagerAdapter::CreateRenderer() {
    RendererRef rendererRef = EffekseerRendererGL::Renderer::Create(spriteMaxCount, this->openglDeviceType);
    return EffekseerRendererRefWrapper(rendererRef);
}