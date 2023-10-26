#include "EffekseerMetalManagerAdapter.h"

EffekseerMetalManagerAdapter::EffekseerMetalManagerAdapter(int32_t spriteMaxCount, bool autoFlip): EffekseerManagerAdapter(spriteMaxCount, autoFlip) { }

EffekseerRendererRefWrapper EffekseerMetalManagerAdapter::CreateRenderer(int32_t spriteMaxCount) {
    EffekseerRenderer::RendererRef rendererRef = EffekseerRendererMetal::Create(spriteMaxCount, MTLPixelFormatBGRA8Unorm, MTLPixelFormatInvalid, false);

    memoryPool = EffekseerRenderer::CreateSingleFrameMemoryPool(rendererRef->GetGraphicsDevice());
    commandList = EffekseerRenderer::CreateCommandList(rendererRef->GetGraphicsDevice(), memoryPool);

    return EffekseerRendererRefWrapper(rendererRef);
}