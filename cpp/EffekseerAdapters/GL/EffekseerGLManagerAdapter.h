#pragma once

#include "../EffekseerManagerAdapter.h"
#include "../../Effekseer/Dev/Cpp/EffekseerRendererGL/EffekseerRendererGL.h"

class EffekseerGLManagerAdapter : public EffekseerManagerAdapter {
private:
    EffekseerRendererGL::OpenGLDeviceType openGLDeviceType;

protected:
    EffekseerRendererRefWrapper CreateRenderer(int32_t spriteMaxCount) override;

public:
    EffekseerGLManagerAdapter(int32_t spriteMaxCount, EffekseerRendererGL::OpenGLDeviceType openGLDeviceType, bool autoFlip = true);
};