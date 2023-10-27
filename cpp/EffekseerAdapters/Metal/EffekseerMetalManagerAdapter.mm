#include "EffekseerMetalManagerAdapter.h"

EffekseerMetalManagerAdapter::EffekseerMetalManagerAdapter(int32_t spriteMaxCount, bool autoFlip): EffekseerManagerAdapter(spriteMaxCount, autoFlip) { }

EffekseerMetalManagerAdapter::~EffekseerMetalManagerAdapter() {
    delete graphics;
    delete texture;
}

EffekseerRendererRefWrapper EffekseerMetalManagerAdapter::CreateRenderer(int32_t spriteMaxCount) {
    graphics = new LLGI::GraphicsMetal();
    graphics->Initialize(nullptr);
    efkGraphicsDevice = Effekseer::MakeRefPtr<EffekseerRendererLLGI::Backend::GraphicsDevice>(graphics);

    memoryPool = LLGI::CreateSharedPtr(graphics->CreateSingleFrameMemoryPool(1024 * 1024, 128));
    commandListPool = std::make_shared<LLGI::CommandListPool>(graphics, memoryPool.get(), 3);

    EffekseerRenderer::RendererRef rendererRef = EffekseerRendererMetal::Create(efkGraphicsDevice, spriteMaxCount, MTLPixelFormatBGRA8Unorm, MTLPixelFormatInvalid, false);
    efkRenderer = rendererRef;

    efkMemoryPool = EffekseerRenderer::CreateSingleFrameMemoryPool(rendererRef->GetGraphicsDevice());
    efkCommandList = EffekseerRenderer::CreateCommandList(rendererRef->GetGraphicsDevice(), efkMemoryPool);

    texture = new LLGI::TextureMetal();
    currentDrawable = nullptr;

    return EffekseerRendererRefWrapper(rendererRef);
}

void EffekseerMetalManagerAdapter::StartFrame() {
    //if (currentDrawable != nullptr) {
        memoryPool->NewFrame();
        commandList = commandListPool->Get();

        commandList->Begin();
        // Get the render pass
        LLGI::RenderPassMetal* renderPass = graphics->GetRenderPass();
        /*
        texture->Reset(currentDrawable->texture);
        renderPass->UpdateRenderTarget((LLGI::Texture**)&texture, 1, nullptr, nullptr, nullptr);
        renderPass->SetIsColorCleared(false);
        renderPass->SetIsDepthCleared(false);
         */
        // Begin the render pass
        commandList->BeginRenderPass(static_cast<LLGI::RenderPass*>(renderPass));

        efkMemoryPool->NewFrame();

        EffekseerRendererMetal::BeginCommandList(efkCommandList, GetEncoder());
        efkRenderer->SetCommandList(efkCommandList);
    //}
}

void EffekseerMetalManagerAdapter::EndFrame() {
    //if (currentDrawable != nullptr) {
        efkRenderer->SetCommandList(nullptr);
        EffekseerRendererMetal::EndCommandList(efkCommandList);

        commandList->EndRenderPass();
        commandList->End();

        graphics->Execute(commandList);

        //currentDrawable = nullptr;
    //}
}

void EffekseerMetalManagerAdapter::DrawCombined(const Effekseer::Manager::DrawParameter& drawParameter) {
    this->StartFrame();
    EffekseerManagerAdapter::DrawCombined(drawParameter);
    this->EndFrame();
}