#define NOMINMAX

#include "EffekseerManagerAdapter.h"

using namespace std;

static ::Effekseer::Matrix44 projectionmatrix,cameramatrix;

typedef ::Effekseer::Vector3D Vector3D;
typedef ::Effekseer::Color Color;
typedef ::Effekseer::Manager::UpdateParameter UpdateParameter;
typedef ::Effekseer::Manager::DrawParameter DrawParameter;

EffekseerManagerAdapter::EffekseerManagerAdapter(int32_t spriteMaxCount, bool autoFlip) {
    // Create the Effekseer manager object
    this->manager = ::Effekseer::Manager::Create(spriteMaxCount, autoFlip);
    // Create the setting object
    this->setting = ::Effekseer:Setting::Create();
    this->setting->SetCoordinateSystem(Effekseer::CoordinateSystem::RH);
    this->manager->SetSetting(setting);
    // Create the Effekseer renderer object to use by calling this virtual method that should be implemented in sub-classes.
    this->renderer = this->CreateRenderer(squareMaxCount).rendererRef;

    // Check successful creation
    if (this->manager == nullptr || this->renderer == nullptr) {
        this->manager.Reset();
        this->renderer.Reset();
        return;
    }

    // Set to normal render mode
    this->renderer->SetRenderMode(Effekseer::RenderMode::Normal);

    // Set all renderer instances for each type by using the created renderer object
    this->manager->SetSpriteRenderer(renderer->CreateSpriteRenderer());
    this->manager->SetRibbonRenderer(renderer->CreateRibbonRenderer());
    this->manager->SetRingRenderer(renderer->CreateRingRenderer());
    this->manager->SetTrackRenderer(renderer->CreateTrackRenderer());
    this->manager->SetModelRenderer(renderer->CreateModelRenderer());

    // Set the loaders
    this->setting->SetModelLoader(renderer->CreateModelLoader());
    this->setting->SetMaterialLoader(renderer->CreateMaterialLoader());
    this->setting->SetTextureLoader(renderer->CreateTextureLoader());
    this->manager->SetCurveLoader(Effekseer::MakeRefPtr<Effekseer::CurveLoader>());

    // Done initialization
    this->hasSuccessfullyInitialized = true;
}

EffekseerManagerAdapter::~EffekseerManagerAdapter() {
    manager.Reset();
    renderer.Reset();
}

bool EffekseerManagerAdapter::GetHasSuccessfullyInitialized() {
    return this->hasSuccessfullyInitialized;
}

void EffekseerManagerAdapter::LaunchWorkerThreads(uint32_t threadCount) {
    if (manager == nullptr) {
        return;
    }

    manager->LaunchWorkerThreads(threadCount);
}

CoordinateSystem EffekseerManagerAdapter::GetCoordinateSystem(); {
    return manager->GetCoordinateSystem();
}
void EffekseerManagerAdapter::SetCoordinateSystem(CoordinateSystem coordinateSystem) {
    return manager->SetCoordinateSystem(coordinateSystem);
}

int EffekseerManagerAdapter::Play(EffekseerEffectAdapter *effect) {
    if (manager == nullptr) {
        return -1;
    }

    return manager->Play(effect->GetInternal(), 0f, 0f, 0f);
}

int EffekseerManagerAdapter::Play(EffekseerEffectAdapter *effect, Vector3D* position, int32_t startFrame = 0) {
    if (manager == nullptr) {
        return -1;
    }

    return manager->Play(effect->GetInternal(), position, startFrame);
}

void EffekseerManagerAdapter::StopEffect(int handle) {
    if (manager == nullptr) {
        return;
    }

    manager->StopEffect(handle);
}

void EffekseerManagerAdapter::StopAllEffects() {
    if (manager == nullptr) {
        return;
    }

    manager->StopAllEffects();
}

void EffekseerManagerAdapter::StopRoot(int handle) {
    if (manager == nullptr) {
        return;
    }

    manager->StopRoot(handle);
}

bool EffekseerManagerAdapter::Exists(int handle) {
    return manager->Exists(handle);
}

int32_t EffekseerManagerAdapter::GetInstanceCount(int handle) {
    if (manager == nullptr) {
        return -1;
    }

    return manager->GetInstanceCount(handle);
}

int32_t EffekseerManagerAdapter::GetTotalInstanceCount() {
    if (manager == nullptr) {
        return -1;
    }

    return manager->GetTotalInstanceCount();
}

int32_t EffekseerManagerAdapter::GetCurrentLOD(int handle) {
    if (manager == nullptr) {
        return -1;
    }

    return manager->GetCurrentLOD(handle);
}

float* EffekseerManagerAdapter::GetMatrix(int handle) {
    if (manager == nullptr) {
        return nullptr;
    }

    auto f = Vector4Map::setConvert43(manager->GetMatrix(handle));
    return f;
}

void EffekseerManagerAdapter::SetMatrix(int handle, float *matrix43) {
    if (manager == nullptr) {
        return;
    }

    auto mat43 = Vector4Map::getConvert43(matrix43);
    manager->SetMatrix(handle, mat43);
}

void EffekseerManagerAdapter::SetMatrixBatch2(int handle1,float matrix43_1[], int handle2,float matrix43_2[]) {
    if (manager == nullptr) {
        return;
    }

    SetMatrix(handle1, matrix43_1);
    SetMatrix(handle2, matrix43_2);
}

void EffekseerManagerAdapter::SetMatrixBatch4(int handle1,float matrix43_1[], int handle2,float matrix43_2[], int handle3,float matrix43_3[], int handle4,float matrix43_4[]) {
    if (manager == nullptr) {
        return;
    }

    SetMatrix(handle1, matrix43_1);
    SetMatrix(handle2, matrix43_2);
    SetMatrix(handle3, matrix43_3);
    SetMatrix(handle4, matrix43_4);
}

float* EffekseerManagerAdapter::GetBaseMatrix(int handle) {
    if (manager == nullptr) {
        return nullptr;
    }

    auto f = Vector4Map::setConvert43(manager->GetBaseMatrix(handle));
    return f;
}

void EffekseerManagerAdapter::SetBaseMatrix(int handle, float *matrix43) {
    if (manager == nullptr) {
        return;
    }

    auto mat43 = Vector4Map::getConvert43(matrix43);
    mat43.Value[3][1] = mat43.Value[3][1] /2;
    manager->SetBaseMatrix(handle, mat43);
}

Vector3D EffekseerManagerAdapter::GetLocation(int handle) {
    if (manager == nullptr) {
        return ::Effekseer::Vector3D();
    }

    return manager->GetLocation(handle);
}

void EffekseerManagerAdapter::SetLocation(int handle, float x, float y, float z) {
    if (manager == nullptr) {
        return;
    }

    manager->SetLocation(handle, x, y, z);
}

void EffekseerManagerAdapter::SetLocation(int handle, const Vector3D& location) {
    if (manager == nullptr) {
        return;
    }

    manager->SetLocation(handle, location);
}

void EffekseerManagerAdapter::AddLocation(int handle, const Vector3D& location) {
    if (manager == nullptr) {
        return;
    }

    manager->AddLocation(handle,location);
}

void EffekseerManagerAdapter::SetRotation(int handle, float x, float y, float z) {
    if (manager == nullptr) {
        return;
    }

    manager->SetRotation(handle, x, y, z);
}

void EffekseerManagerAdapter::SetRotation(int handle, const Vector3D& axis, float angle) {
    if (manager == nullptr) {
        return;
    }

    manager->SetRotation(handle, axis, angle);
}

void EffekseerManagerAdapter::SetScale(int handle, float x, float y, float z) {
    if (manager == nullptr) {
        return;
    }

    manager->SetScale(handle, x, y, z);
}

void EffekseerManagerAdapter::SetAllColor(int handle, Color color) {
    if (manager == nullptr) {
        return;
    }

    manager->SetAllColor(handle, color);
}

void EffekseerManagerAdapter::SetTargetLocation(int handle, float x, float y, float z) {
    if (manager == nullptr) {
        return;
    }

    manager->SetTargetLocation(handle, x, y, z);
}

void EffekseerManagerAdapter::SetTargetLocation(int handle, const Vector3D& location) {
    if (manager == nullptr) {
        return;
    }

    manager->SetTargetLocation(handle, location);
}

void EffekseerManagerAdapter::SetProjectionMatrix(float *matrix44, float *matrix44C, bool view, float width, float height) {
    // disto->update();
    projectionmatrix = Vector4Map::getConvert44( matrix44 );
    cameramatrix = Vector4Map::getConvert44(matrix44C);


    if (manager == nullptr) {
        return;
    }

    if(view){
        renderer->SetProjectionMatrix(projectionmatrix);
    }else{
        renderer->SetProjectionMatrix( ::Effekseer::Matrix44().OrthographicRH(width, height, 0.0f, 100.0f));
    }

    renderer->SetCameraMatrix(cameramatrix);
}

float EffekseerManagerAdapter::GetDynamicInput(int handle, int32_t index) {
    if (manager == nullptr) {
        return 0.0f;
    }

    return manager->GetDynamicInput(handle, index);
}

void EffekseerManagerAdapter::SetDynamicInput(int handle, int32_t index, float value) {
    if (manager == nullptr) {
        return;
    }

    manager->SetDynamicInput(handle, index, value);
}

void EffekseerManagerAdapter::SendTrigger(int handle, int32_t index) {
    if (manager == nullptr) {
        return 0.0f;
    }

    return manager->SendTrigger(handle, index);
}

bool EffekseerManagerAdapter::GetShown(int handle) {
    if (manager == nullptr) {
        return false;
    }

    return manager->GetShown(handle);
}

void EffekseerManagerAdapter::SetShown(int handle, bool shown) {
    if (manager == nullptr) {
        return;
    }

    manager->SetShown(handle, shown);
}

bool EffekseerManagerAdapter::GetPaused(int handle) {
    if (manager == nullptr) {
        return false;
    }

    return manager->GetPaused(handle);
}

void EffekseerManagerAdapter::SetPaused(int handle, bool paused) {
    if (manager == nullptr) {
        return;
    }

    manager->SetPaused(handle, paused);
}

void EffekseerManagerAdapter::SetPausedToAllEffects(bool paused) {
    if (manager == nullptr) {
        return;
    }

    manager->SetPausedToAllEffects(paused);
}

int EffekseerManagerAdapter::GetLayer(int handle) {
    if (manager == nullptr) {
        return -1;
    }

    return manager->GetLayer(handle);
}

void EffekseerManagerAdapter::SetLayer(int handle, int32_t layer) {
    if (manager == nullptr) {
        return;
    }

    manager->SetLayer(handle, layer);
}

int64_t EffekseerManagerAdapter::GetGroupMask(int handle) {
    if (manager == nullptr) {
        return -1;
    }

    return manager->GetGroupMask(handle);
}

void EffekseerManagerAdapter::SetGroupMask(int handle, int64_t groupmask) {
    if (manager == nullptr) {
        return;
    }

    manager->SetGroupMask(handle, groupmask);
}

float EffekseerManagerAdapter::GetSpeed(int handle) {
    if (manager == nullptr) {
        return -1.0f;
    }

    return manager->GetSpeed(handle);
}

void EffekseerManagerAdapter::SetSpeed(int handle, float speed) {
    if (manager == nullptr) {
        return;
    }

    manager->SetSpeed(handle, speed);
}

void EffekseerManagerAdapter::SetTimeScaleByGroup(int64_t groupmask, float timeScale) {
    if (manager == nullptr) {
        return;
    }

    manager->SetTimeScaleByGroup(groupmask, timeScale);
}

void EffekseerManagerAdapter::SetTimeScaleByHandle(int handle, float timeScale) {
    if (manager == nullptr) {
        return;
    }

    manager->SetTimeScaleByHandle(handle, timeScale);
}

void EffekseerManagerAdapter::SetAutoDrawing(int handle, bool autoDraw) {
    if (manager == nullptr) {
        return;
    }

    manager->SetAutoDrawing(handle, autoDraw);
}

/*
void* EffekseerManagerAdapter::GetUserData(int handle) {
    if (manager_ == nullptr) {
        return nullptr;
    }

    return manager_->GetUserData(handle);
}

void EffekseerManagerAdapter::SetUserData(int handle, void* userData) {
    if (manager_ == nullptr) {
        return;
    }

    manager_->SetUserData(handle, userData);
}
*/

void EffekseerManagerAdapter::Flip() {
    if (manager == nullptr) {
        return;
    }

    manager->Flip();
}

void EffekseerManagerAdapter::Update(float deltaFrames) {
    if (manager == nullptr) {
        return;
    }

    deltaFrames += remainingDeltaTime;
    remainingDeltaTime = deltaFrames - int(deltaFrames);
    for (int loop = 0; loop < int(deltaFrames); loop++) {
        manager->Update(1);
    }
}

void EffekseerManagerAdapter::Update(const UpdateParameter& parameter) {
    if (manager == nullptr) {
        return;
    }

    manager->Update(parameter);
}

void EffekseerManagerAdapter::BeginUpdate() {
    if (manager == nullptr) {
        return;
    }

    manager->BeginUpdate();
}

void EffekseerManagerAdapter::EndUpdate() {
    if (manager == nullptr) {
        return;
    }

    manager->EndUpdate();
}

void EffekseerManagerAdapter::UpdateHandle(int handle, float deltaFrame) {
    if (manager == nullptr) {
        return;
    }

    manager->UpdateHandle(handle, deltaFrame);
}

void EffekseerManagerAdapter::UpdateHandleToMoveToFrame(int handle, float frame) {
    if (manager == nullptr) {
        return;
    }

    manager->UpdateHandleToMoveToFrame(handle, frame);
}

void EffekseerManagerAdapter::SetTime(float time) {
    renderer->SetTime(time);
}

void EffekseerManagerAdapter::UpdateCombined(float deltaFrames, float time, float projectionMatrix44[],float viewMatrix44C[], bool view, float width, float height) {
    SetProjectionMatrix(projectionMatrix44, viewMatrix44C, view, width, height);
    SetTime(time);
    Update(deltaFrames);
}

void EffekseerManagerAdapter::BeginRendering() {
    renderer->BeginRendering();
}

void EffekseerManagerAdapter::Draw(const Effekseer::Manager::DrawParameter& drawParameter) {
    manager->Draw(drawParameter);
}

void EffekseerManagerAdapter::DrawBack() {
    if (manager == nullptr) {
        return;
    }


    renderer->BeginRendering();

    manager->DrawBack();

    renderer->EndRendering();
}

void EffekseerManagerAdapter::DrawFront() {
    if (manager == nullptr) {
        return;
    }

    renderer->BeginRendering();
    manager->DrawFront();
    renderer->EndRendering();
}

void EffekseerManagerAdapter::EndRendering() {
    renderer->EndRendering();
}

void EffekseerManagerAdapter::DrawCombined(const Effekseer::Manager::DrawParameter& drawParameter) {
    BeginRendering();
    Draw(drawParameter);
    EndRendering();
}