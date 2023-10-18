#pragma once

#include "../Effekseer/Dev/Cpp/Effekseer/Effekseer.h"
#include "GDXMatrixAdapter.h"
#include "RefWrappers.h"

class EffekseerEffectAdapter;

typedef ::Effekseer::Vector3D Vector3D;
typedef ::Effekseer::Color Color;
typedef ::Effekseer::Manager::UpdateParameter UpdateParameter;
typedef ::Effekseer::Manager::DrawParameter DrawParameter;

class EffekseerManagerAdapter {
private:
    Effekseer::SettingRef setting = nullptr;
	float remainingDeltaTime = 0.0f;
	bool hasSuccessfullyInitialized = false;

protected:
#ifndef SWIG
	EffekseerRenderer::RendererRef renderer = nullptr;
	Effekseer::ManagerRef manager = nullptr;
#endif
    virtual EffekseerRendererRefWrapper CreateRenderer() = 0;

public:
	EffekseerManagerAdapter(int32_t spriteMaxCount, bool autoFlip = true);
	~EffekseerManagerAdapter();

	bool GetHasSuccessfullyInitialized();

	void LaunchWorkerThreads(uint32_t threadCount);

    Effekseer::CoordinateSystem GetCoordinateSystem();
	void SetCoordinateSystem(Effekseer::CoordinateSystem coordinateSystem);

	int Play(EffekseerEffectAdapter *effect);
	int Play(EffekseerEffectAdapter *effect, Vector3D* position, int32_t startFrame = 0);
	void StopEffect(int handle);
	void StopAllEffects();
	void StopRoot(int handle);

	bool Exists(int handle);

	int32_t GetInstanceCount(int handle);
	int32_t GetTotalInstanceCount();

	int32_t GetCurrentLOD(int handle);

	float* GetMatrix(int handle);
	void SetMatrix(int handle,float matrix43[]);
	void SetMatrixBatch2(int handle1,float matrix43_1[], int handle2,float matrix43_2[]);
	void SetMatrixBatch4(int handle1,float matrix43_1[], int handle2,float matrix43_2[], int handle3,float matrix43_3[], int handle4,float matrix43_4[]);
	float* GetBaseMatrix(int handle);
	void SetBaseMatrix(int handle, float matrix43[]);
	Vector3D GetLocation(int handle);
	void SetLocation(int handle, float x, float y, float z);
	void SetLocation(int handle, const Vector3D& location);
	void AddLocation(int handle, const Vector3D& location);
	void SetRotation(int handle, float x, float y, float z);
	void SetRotation(int handle, const Vector3D& axis, float angle);
	void SetScale(int handle, float x, float y, float z);
	void SetAllColor(int handle, Color color);
	void SetTargetLocation(int handle, float x, float y, float z);
	void SetTargetLocation(int handle, const Vector3D& location);
	void SetProjectionMatrix(float matrix44[],float matrix44C[], bool view,float width,float height);

	float GetDynamicInput(int handle, int32_t index);
	void SetDynamicInput(int handle, int32_t index, float value);

	void SendTrigger(int handle, int32_t index);

	bool GetShown(int handle);
	void SetShown(int handle, bool shown);
	bool GetPaused(int handle);
	void SetPaused(int handle, bool paused);
	void SetPausedToAllEffects(bool paused);
	int GetLayer(int handle);
	void SetLayer(int handle, int32_t layer);
	int64_t GetGroupMask(int handle);
	void SetGroupMask(int handle, int64_t groupmask);

	float GetSpeed(int handle);
	void SetSpeed(int handle, float speed);
	void SetTimeScaleByGroup(int64_t groupmask, float timeScale);
	void SetTimeScaleByHandle(int handle, float timeScale);
	void SetAutoDrawing(int handle, bool autoDraw);

	void Flip();
	void Update(float deltaFrames);
	void Update(const UpdateParameter& parameter);
	void BeginUpdate();
	void EndUpdate();
	void UpdateHandle(int handle, float deltaFrame = 1.0f);
	void UpdateHandleToMoveToFrame(int handle, float frame);
	void SetTime(float time);
	/**
	 * Calls SetProjectionMatrix(), SetTime(), and Update(deltaFrames). This can be called to avoid a separate JNI call for each method.
	 */
	void UpdateCombined(float deltaFrames, float time, float projectionMatrix44[],float viewMatrix44C[], bool view, float width, float height);

	void BeginRendering();
	void Draw(const Effekseer::Manager::DrawParameter& drawParameter);
	void DrawBack();
	void DrawFront();
	void EndRendering();
	/**
	 * Calls BeginRendering(), Draw(drawParameter), and EndRendering(). This can be called to avoid a separate JNI call for each method.
	 */
	void DrawCombined(const Effekseer::Manager::DrawParameter& drawParameter);

	friend class EffekseerEffectAdapter;
};
