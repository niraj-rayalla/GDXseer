#pragma once

#include "../EffekseerManagerAdapter.h"
#include <Effekseer/Dev/Cpp/EffekseerRendererMetal/EffekseerRendererMetal.h>
#include <LLGI.Base.h>
#include <LLGI.Compiler.h>
#include <LLGI.Graphics.h>
#include <LLGI.Platform.h>
#include <LLGI.GraphicsMetal.h>
#include <LLGI.CommandListMetal.h>
#include <LLGI.TextureMetal.h>
#include <LLGI.RenderPassMetal.h>
#include <GraphicsDevice.h>
#include <Utils/LLGI.CommandListPool.h>

class EffekseerMetalManagerAdapter : public EffekseerManagerAdapter {

protected:
    LLGI::GraphicsMetal* graphics;
    LLGI::TextureMetal* texture;
    std::shared_ptr<LLGI::SingleFrameMemoryPool> memoryPool;
    std::shared_ptr<LLGI::CommandListPool> commandListPool;
    LLGI::CommandList* commandList = nullptr;

    Effekseer::Backend::GraphicsDeviceRef efkGraphicsDevice;
    EffekseerRenderer::RendererRef efkRenderer;
    Effekseer::RefPtr<EffekseerRenderer::SingleFrameMemoryPool> efkMemoryPool;
    Effekseer::RefPtr<EffekseerRenderer::CommandList> efkCommandList;

    EffekseerRendererRefWrapper CreateRenderer(int32_t spriteMaxCount) override;

    id<MTLRenderCommandEncoder> GetEncoder()
    {
        return static_cast<LLGI::CommandListMetal*>(commandList)->GetRenderCommandEncorder();
    }

public:
    id<CAMetalDrawable>* currentDrawable = nullptr;

    EffekseerMetalManagerAdapter(int32_t spriteMaxCount, bool autoFlip = true);
    ~EffekseerMetalManagerAdapter();

    void StartFrame();
    void EndFrame();

    void DrawCombined(const Effekseer::Manager::DrawParameter& drawParameter);
};