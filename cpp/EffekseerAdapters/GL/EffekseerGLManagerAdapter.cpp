#include "EffekseerGLManagerAdapter.h"

EffekseerGLManagerAdapter::EffekseerGLManagerAdapter(
    int32_t spriteMaxCount, EffekseerRendererGL::OpenGLDeviceType openGLDeviceType, bool autoFlip
): EffekseerManagerAdapter(spriteMaxCount, autoFlip) {
    this->openGLDeviceType = openGLDeviceType;
}

EffekseerRendererRefWrapper EffekseerGLManagerAdapter::CreateRenderer(int32_t spriteMaxCount) {
    EffekseerRenderer::RendererRef rendererRef = EffekseerRendererGL::Renderer::Create(spriteMaxCount, this->openGLDeviceType);
    return EffekseerRendererRefWrapper(rendererRef);
}