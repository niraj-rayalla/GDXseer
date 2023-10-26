#pragma once

#include "../EffekseerManagerAdapter.h"
#include "../../Effekseer/Dev/Cpp/EffekseerRendererMetal/EffekseerRendererMetal.h"

class EffekseerMetalManagerAdapter : public EffekseerManagerAdapter {

protected:
    Effekseer::RefPtr<EffekseerRenderer::SingleFrameMemoryPool> memoryPool;
    Effekseer::RefPtr<EffekseerRenderer::CommandList> commandList;

    EffekseerRendererRefWrapper CreateRenderer(int32_t spriteMaxCount) override;

public:
    EffekseerMetalManagerAdapter(int32_t spriteMaxCount, bool autoFlip = true);
};