include $(CLEAR_VARS)

LOCAL_PATH:=$(call my-dir)



LOCAL_ARM_MODE  := arm
LOCAL_PATH      := $(NDK_PROJECT_PATH)
LOCAL_MODULE    := libGDXseer_Effekseer
LOCAL_CFLAGS    := -O2 -D__EFFEKSEER_RENDERER_GLES3__ -std=c++14
LOCAL_LDLIBS    := -llog -landroid -lEGL -lGLESv3




LOCAL_C_INCLUDES += \
	$(LOCAL_PATH) \
	$(LOCAL_PATH)/Effekseer/Dev/Cpp/Effekseer \
	$(LOCAL_PATH)/Effekseer/Dev/Cpp/EffekseerRendererCommon \
	$(LOCAL_PATH)/Effekseer/Dev/Cpp/EffekseerRendererGL 

LOCAL_SRC_FILES := \
	$(LOCAL_PATH)/cpp/Effekseer_Swig.cpp \
	$(LOCAL_PATH)/cpp/EffekseerAdapters/RefWrappers.cpp \
	$(LOCAL_PATH)/cpp/EffekseerAdapters/GDXMatrixAdapter.cpp \
	$(LOCAL_PATH)/cpp/EffekseerAdapters/EffekseerManagerAdapter.cpp \
	$(LOCAL_PATH)/cpp/EffekseerAdapters/EffekseerEffectAdapter.cpp \
	$(LOCAL_PATH)/cpp/Adapter_Effekseer_Swig.cpp \
	$(LOCAL_PATH)/cpp/EffekseerAdapters/GL/EffekseerGLManagerAdapter.cpp \
	$(LOCAL_PATH)/cpp/Effekseer_GL_Swig.cpp \
	
	
	
LIB_SRC_PATH := $(LOCAL_PATH)/Effekseer/Dev/Cpp/Effekseer/Effekseer
LOCAL_SRC_FILES += \
    $(LIB_SRC_PATH)/Effekseer.Color.cpp \
    $(LIB_SRC_PATH)/Effekseer.CurveLoader.cpp \
    $(LIB_SRC_PATH)/Effekseer.DefaultEffectLoader.cpp \
    $(LIB_SRC_PATH)/Effekseer.DefaultFile.cpp \
    $(LIB_SRC_PATH)/Effekseer.Effect.cpp \
    $(LIB_SRC_PATH)/Effekseer.EffectNode.cpp \
    $(LIB_SRC_PATH)/Effekseer.EffectNodeModel.cpp \
    $(LIB_SRC_PATH)/Effekseer.EffectNodeRibbon.cpp \
    $(LIB_SRC_PATH)/Effekseer.EffectNodeRing.cpp \
    $(LIB_SRC_PATH)/Effekseer.EffectNodeRoot.cpp \
    $(LIB_SRC_PATH)/Effekseer.EffectNodeSprite.cpp \
    $(LIB_SRC_PATH)/Effekseer.EffectNodeTrack.cpp \
    $(LIB_SRC_PATH)/Effekseer.FCurves.cpp \
    $(LIB_SRC_PATH)/Effekseer.Instance.cpp \
    $(LIB_SRC_PATH)/Effekseer.InstanceChunk.cpp \
    $(LIB_SRC_PATH)/Effekseer.InstanceContainer.cpp \
    $(LIB_SRC_PATH)/Effekseer.InstanceGlobal.cpp \
    $(LIB_SRC_PATH)/Effekseer.InstanceGroup.cpp \
    $(LIB_SRC_PATH)/Effekseer.InternalScript.cpp \
    $(LIB_SRC_PATH)/Effekseer.Manager.cpp \
    $(LIB_SRC_PATH)/Effekseer.Matrix43.cpp \
    $(LIB_SRC_PATH)/Effekseer.Matrix44.cpp \
    $(LIB_SRC_PATH)/Effekseer.Random.cpp \
    $(LIB_SRC_PATH)/Effekseer.RectF.cpp \
    $(LIB_SRC_PATH)/Effekseer.Resource.cpp \
    $(LIB_SRC_PATH)/Effekseer.ResourceManager.cpp \
    $(LIB_SRC_PATH)/Effekseer.Setting.cpp \
    $(LIB_SRC_PATH)/Effekseer.Vector2D.cpp \
    $(LIB_SRC_PATH)/Effekseer.Vector3D.cpp \
    $(LIB_SRC_PATH)/Effekseer.WorkerThread.cpp \
    $(LIB_SRC_PATH)/Material/Effekseer.MaterialFile.cpp \
    $(LIB_SRC_PATH)/Material/Effekseer.CompiledMaterial.cpp \
    $(LIB_SRC_PATH)/Material/Effekseer.MaterialCompiler.cpp \
    $(LIB_SRC_PATH)/IO/Effekseer.EfkEfcFactory.cpp \
    $(LIB_SRC_PATH)/Parameter/Easing.cpp \
    $(LIB_SRC_PATH)/Parameter/Effekseer.Parameters.cpp \
    $(LIB_SRC_PATH)/Parameter/Rotation.cpp \
    $(LIB_SRC_PATH)/Utils/Effekseer.CustomAllocator.cpp \
    $(LIB_SRC_PATH)/SIMD/Mat43f.cpp \
    $(LIB_SRC_PATH)/SIMD/Mat44f.cpp \
    $(LIB_SRC_PATH)/SIMD/Utils.cpp \
    $(LIB_SRC_PATH)/Noise/CurlNoise.cpp \
    $(LIB_SRC_PATH)/ForceField/ForceFields.cpp \
    $(LIB_SRC_PATH)/Model/ProceduralModelGenerator.cpp \
    $(LIB_SRC_PATH)/Model/Model.cpp \
    $(LIB_SRC_PATH)/Model/ModelLoader.cpp \
    $(LIB_SRC_PATH)/Model/SplineGenerator.cpp \
    $(LIB_SRC_PATH)/Effekseer.Client.cpp \
    $(LIB_SRC_PATH)/Effekseer.Server.cpp \
    $(LIB_SRC_PATH)/Effekseer.Socket.cpp
    #$(LIB_SRC_PATH)/Network/Effekseer.Client.cpp \
    $(LIB_SRC_PATH)/Network/Effekseer.Server.cpp \
    $(LIB_SRC_PATH)/Network/Effekseer.Session.cpp \
    $(LIB_SRC_PATH)/Network/Effekseer.Socket.cpp

LIB_SRC_PATH := $(LOCAL_PATH)/Effekseer/Dev/Cpp/EffekseerMaterialCompiler
LOCAL_SRC_FILES += \
	$(LIB_SRC_PATH)/Common/ShaderGeneratorCommon.cpp \
	$(LIB_SRC_PATH)/GLSLGenerator/ShaderGenerator.cpp \
	$(LIB_SRC_PATH)/OpenGL/EffekseerMaterialCompilerGL.cpp

LIB_SRC_PATH := $(LOCAL_PATH)/Effekseer/Dev/Cpp/EffekseerRendererCommon
LOCAL_SRC_FILES += \
    $(LIB_SRC_PATH)/EffekseerRenderer.CommonUtils.cpp \
    $(LIB_SRC_PATH)/EffekseerRenderer.DDSTextureLoader.cpp \
    $(LIB_SRC_PATH)/EffekseerRenderer.IndexBufferBase.cpp \
    $(LIB_SRC_PATH)/EffekseerRenderer.ModelRendererBase.cpp \
    $(LIB_SRC_PATH)/EffekseerRenderer.PngTextureLoader.cpp \
    $(LIB_SRC_PATH)/EffekseerRenderer.RenderStateBase.cpp \
    $(LIB_SRC_PATH)/EffekseerRenderer.Renderer.cpp \
    $(LIB_SRC_PATH)/EffekseerRenderer.Renderer_Impl.cpp \
    $(LIB_SRC_PATH)/EffekseerRenderer.RibbonRendererBase.cpp \
    $(LIB_SRC_PATH)/EffekseerRenderer.RingRendererBase.cpp \
    $(LIB_SRC_PATH)/EffekseerRenderer.SpriteRendererBase.cpp \
    $(LIB_SRC_PATH)/EffekseerRenderer.TGATextureLoader.cpp \
    $(LIB_SRC_PATH)/EffekseerRenderer.TrackRendererBase.cpp \
    $(LIB_SRC_PATH)/EffekseerRenderer.VertexBufferBase.cpp \
    $(LIB_SRC_PATH)/ModelLoader.cpp \
    $(LIB_SRC_PATH)/TextureLoader.cpp \
    #$(LIB_SRC_PATH)/GraphicsDeviceCPU.cpp \
    $(LIB_SRC_PATH)/VertexBuffer.cpp

LIB_SRC_PATH := $(LOCAL_PATH)/Effekseer/Dev/Cpp/EffekseerRendererGL/EffekseerRenderer
LOCAL_SRC_FILES += \
	$(LIB_SRC_PATH)/EffekseerRendererGL.DeviceObject.cpp \
	$(LIB_SRC_PATH)/EffekseerRendererGL.GLExtension.cpp \
	$(LIB_SRC_PATH)/EffekseerRendererGL.IndexBuffer.cpp \
	$(LIB_SRC_PATH)/EffekseerRendererGL.MaterialLoader.cpp \
	$(LIB_SRC_PATH)/EffekseerRendererGL.ModelRenderer.cpp \
	$(LIB_SRC_PATH)/EffekseerRendererGL.RenderState.cpp \
	$(LIB_SRC_PATH)/EffekseerRendererGL.Renderer.cpp \
	$(LIB_SRC_PATH)/EffekseerRendererGL.Shader.cpp \
	$(LIB_SRC_PATH)/EffekseerRendererGL.VertexArray.cpp \
	$(LIB_SRC_PATH)/EffekseerRendererGL.VertexBuffer.cpp \
	$(LIB_SRC_PATH)/GraphicsDevice.cpp

include $(BUILD_SHARED_LIBRARY)