// AllTypeColorParameter
%extend Effekseer::AllTypeColorParameter {
    Effekseer::Color getFixed() { return self->fixed.all; }
    void setFixed(Effekseer::Color value) { self->fixed.all = value; }
    Effekseer::random_color getRandom() { return self->random.all; }
    void setRandom(Effekseer::random_color value) { self->random.all = value; }
    Effekseer::easing_color getEasing() { return self->easing.all; }
    void setEasing(Effekseer::easing_color value) { self->easing.all = value; }
    Effekseer::FCurveVectorColor* getFCurveRGBA() { return self->fcurve_rgba.FCurve; }
    void setFCurveRGBA(Effekseer::FCurveVectorColor* value) { self->fcurve_rgba.FCurve = value; }
    Effekseer::Gradient* getGradient() { return self->gradient.get(); }
};

// TranslationParameter
%extend Effekseer::TranslationParameter {
    Effekseer::FCurveVector3D* getTranslationFCurve() { return self->TranslationFCurve.get(); }
};

// TranslationParameter
%extend Effekseer::RotationParameter {
    Effekseer::FCurveVector3D* getRotationFCurve() { return self->RotationFCurve.get(); }
};

// TranslationParameter
%extend Effekseer::ScalingParameter {
    Effekseer::FCurveVector3D* getScalingFCurve() { return self->ScalingFCurve.get(); }
    Effekseer::FCurveScalar* getScalingSingleFCurve() { return self->ScalingSingleFCurve.get(); }
};

// UVParameter
%extend Effekseer::UVParameter {
    // Fixed
    Effekseer::rectf getFixedPosition() { return self->Fixed.Position; }
    void setFixedPosition(Effekseer::rectf value) { self->Fixed.Position = value; }

    // Animation
    Effekseer::rectf getAnimationPosition() { return self->Animation.Position; }
    void setAnimationPosition(Effekseer::rectf value) { self->Animation.Position = value; }
    int32_t getAnimationFrameLength() { return self->Animation.FrameLength; }
    void setAnimationFrameLength(int32_t value) { self->Animation.FrameLength = value; }
    int32_t getAnimationFrameCountX() { return self->Animation.FrameCountX; }
    void setAnimationFrameCountX(int32_t value) { self->Animation.FrameCountX = value; }
    int32_t getAnimationFrameCountY() { return self->Animation.FrameCountY; }
    void setAnimationFrameCountY(int32_t value) { self->Animation.FrameCountY = value; }
    Effekseer::UVAnimationLoopType getAnimationLoopType() {
        switch (self->Animation.LoopType) {
            case 0:
                return Effekseer::UVAnimationLoopType::LOOPTYPE_ONCE;
            case 1:
                return Effekseer::UVAnimationLoopType::LOOPTYPE_LOOP;
            case 2:
                return Effekseer::UVAnimationLoopType::LOOPTYPE_REVERSELOOP;
            case 0x7fffffff:
                return Effekseer::UVAnimationLoopType::LOOPTYPE_DWORD;
            default:
                return Effekseer::UVAnimationLoopType::LOOPTYPE_DWORD;
        }
    }
    void setAnimationLoopType(Effekseer::UVAnimationLoopType value) {
        switch (value) {
            case Effekseer::UVAnimationLoopType::LOOPTYPE_ONCE:
                self->Animation.LoopType = self->Animation.LOOPTYPE_ONCE;
                break;
            case Effekseer::UVAnimationLoopType::LOOPTYPE_LOOP:
                self->Animation.LoopType = self->Animation.LOOPTYPE_LOOP;
                break;
            case Effekseer::UVAnimationLoopType::LOOPTYPE_REVERSELOOP:
                self->Animation.LoopType = self->Animation.LOOPTYPE_REVERSELOOP;
                break;
            case Effekseer::UVAnimationLoopType::LOOPTYPE_DWORD:
                self->Animation.LoopType = self->Animation.LOOPTYPE_DWORD;
                break;
        }
    }
    Effekseer::UVAnimationInterpolationType getAnimationInterpolationType() {
        switch (self->Animation.InterpolationType) {
            case 0:
                return Effekseer::UVAnimationInterpolationType::NONE;
            case 1:
                return Effekseer::UVAnimationInterpolationType::LERP;
        }
    }
    void setAnimationInterpolationType(Effekseer::UVAnimationInterpolationType value) {
        switch (value) {
            case Effekseer::UVAnimationInterpolationType::NONE:
                self->Animation.InterpolationType = self->Animation.NONE;
                break;
            case Effekseer::UVAnimationInterpolationType::LERP:
                self->Animation.InterpolationType = self->Animation.LERP;
                break;
        }
    }
    Effekseer::random_int getAnimationStartFrame() { return self->Animation.StartFrame; }
    void setAnimationStartFrame(Effekseer::random_int value) { self->Animation.StartFrame = value; }

    // Scroll
    Effekseer::random_vector2d getScrollPosition() { return self->Scroll.Position; }
    void setScrollPosition(Effekseer::random_vector2d value) { self->Scroll.Position = value; }
    Effekseer::random_vector2d getScrollSize() { return self->Scroll.Size; }
    void setScrollSize(Effekseer::random_vector2d value) { self->Scroll.Size = value; }
    Effekseer::random_vector2d getScrollSpeed() { return self->Scroll.Speed; }
    void setScrollSpeed(Effekseer::random_vector2d value) { self->Scroll.Speed = value; }

    // FCurve
    Effekseer::FCurveVector2D* getFCurvePosition() { return self->FCurve.Position; }
    void setFCurvePosition(Effekseer::FCurveVector2D* value) { self->FCurve.Position = value; }
    Effekseer::FCurveVector2D* getFCurveSize() { return self->FCurve.Size; }
    void setFCurveSize(Effekseer::FCurveVector2D* value) { self->FCurve.Size = value; }
};

// ParameterRendererCommon
%extend Effekseer::ParameterRendererCommon {
        Effekseer::UVParameter getUVParameter(int index) { return self->UVs[index]; }
};

// ParameterGenerationLocation
%extend Effekseer::ParameterGenerationLocation {
    // Point
    random_vector3d getPointLocation() { return self->point.location; }
    void setPointLocation(random_vector3d value) { self->point.location = value; }

    // Sphere
    random_float getSphereRadius() { return self->sphere.radius; }
    void setSphereRadius(random_float value) { self->sphere.radius = value; }
    random_float getSphereRotationX() { return self->sphere.rotation_x; }
    void setSphereRotationX(random_float value) { self->sphere.rotation_x = value; }
    random_float getSphereRotationY() { return self->sphere.rotation_y; }
    void setSphereRotationY(random_float value) { self->sphere.rotation_y = value; }

    // Model
    ModelReferenceType getModelReferenceType() { return self->model.Reference; }
    void setModelReferenceType(ModelReferenceType value) { self->model.Reference = value; }
    int32_t getModelIndex() { return self->model.index; }
    void setModelIndex(int32_t value) { self->model.index = value; }
    eModelType getModelType() { return self->model.type; }
    void setModelType(eModelType value) { self->model.type = value; }

    // Circle
    int32_t getCircleDivision() { return self->circle.division; }
    void setCircleDivision(int32_t value) { self->circle.division = value; }
    random_float getCircleRadius() { return self->circle.radius; }
    void setCircleRadius(random_float value) { self->circle.radius = value; }
    random_float getCircleAngleStart() { return self->circle.angle_start; }
    void setCircleAngleStart(random_float value) { self->circle.angle_start = value; }
    random_float getCircleAngleEnd() { return self->circle.angle_end; }
    void setCircleAngleEnd(random_float value) { self->circle.angle_end = value; }
    eCircleType getCircleType() { return self->circle.type; }
    void setCircleType(eCircleType value) { self->circle.type = value; }
    AxisType getCircleAxisDirection() { return self->circle.axisDirection; }
    void setCircleAxisDirection(AxisType value) { self->circle.axisDirection = value; }
    random_float getCircleAngleNoise() { return self->circle.angle_noize; }
    void setCircleAngleNoise(random_float value) { self->circle.angle_noize = value; }

    // Line
    int32_t getLineDivision() { return self->line.division; }
    void setLineDivision(int32_t value) { self->line.division = value; }
    random_vector3d getLinePositionStart() { return self->line.position_start; }
    void setLinePositionStart(random_vector3d value) { self->line.position_start = value; }
    random_vector3d getLinePositionEnd() { return self->line.position_end; }
    void setLinePositionEnd(random_vector3d value) { self->line.position_end = value; }
    random_float getLinePositionNoise() { return self->line.position_noize; }
    void setLinePositionNoise(random_float value) { self->line.position_noize = value; }
    LineType getLineType() { return self->line.type; }
    void setLineType(LineType value) { self->line.type = value; }
};

// EffectNode
%extend Effekseer::EffectNode {
    //
    // Children
    //

    Effekseer::EffectNodeImplemented* getChild(int index) {
        if (index >= self->GetChildrenCount())
            return nullptr;
        return static_cast<Effekseer::EffectNodeImplemented*>(self->GetChild(index));
    }

    Effekseer::EffectNodeSprite* getChildAsSprite(int index) {
        if (index >= self->GetChildrenCount())
            return nullptr;
        return static_cast<Effekseer::EffectNodeSprite*>(self->GetChild(index));
    }

    Effekseer::EffectNodeRibbon* getChildAsRibbon(int index) {
        if (index >= self->GetChildrenCount())
            return nullptr;
        return static_cast<Effekseer::EffectNodeRibbon*>(self->GetChild(index));
    }

    Effekseer::EffectNodeTrack* getChildAsTrack(int index) {
        if (index >= self->GetChildrenCount())
            return nullptr;
        return static_cast<Effekseer::EffectNodeTrack*>(self->GetChild(index));
    }

    Effekseer::EffectNodeRing* getChildAsRing(int index) {
        if (index >= self->GetChildrenCount())
            return nullptr;
        return static_cast<Effekseer::EffectNodeRing*>(self->GetChild(index));
    }

    Effekseer::EffectNodeModel* getChildAsModel(int index) {
        if (index >= self->GetChildrenCount())
            return nullptr;
        return static_cast<Effekseer::EffectNodeModel*>(self->GetChild(index));
    }
};

// EffectNodeImplemented
%extend Effekseer::EffectNodeImplemented {
    //
    // Parameters
    //

    Effekseer::TranslationParameter* getTranslationParam() {
        return &(self->TranslationParam);
    }

    Effekseer::LocalForceFieldParameter* getLocalForceField() {
        return &(self->LocalForceField);
    }

    Effekseer::RotationParameter* getRotationParam() {
        return &(self->RotationParam);
    }

    Effekseer::ScalingParameter* getScalingParam() {
        return &(self->ScalingParam);
    }

    Effekseer::ParameterAlphaCutoff* getAlphaCutoff() {
        return &(self->AlphaCutoff);
    }
};

// EffectNodeSprite
%extend Effekseer::EffectNodeSprite {
    Effekseer::AllTypeColorParameter* getSpriteAllColor() {
        return &(self->SpriteAllColor);
    }
};

// EffectNodeRibbon
%extend Effekseer::EffectNodeRibbon {
    Effekseer::AllTypeColorParameter* getRibbonAllColor() {
        return &(self->RibbonAllColor);
    }
};

// EffectNodeTrack
%extend Effekseer::EffectNodeTrack {
    Effekseer::AllTypeColorParameter* getTrackColorLeft() {
        return &(self->TrackColorLeft);
    }

    Effekseer::AllTypeColorParameter* getTrackColorCenter() {
        return &(self->TrackColorCenter);
    }

    Effekseer::AllTypeColorParameter* getTrackColorRight() {
        return &(self->TrackColorRight);
    }

    Effekseer::AllTypeColorParameter* getTrackColorLeftMiddle() {
        return &(self->TrackColorLeftMiddle);
    }

    Effekseer::AllTypeColorParameter* getTrackColorCenterMiddle() {
        return &(self->TrackColorCenterMiddle);
    }

    Effekseer::AllTypeColorParameter* getTrackColorRightMiddle() {
        return &(self->TrackColorRightMiddle);
    }
};

// EffectNodeRing
%extend Effekseer::EffectNodeRing {
    Effekseer::AllTypeColorParameter* getOuterColor() {
        return &(self->OuterColor);
    }

    Effekseer::AllTypeColorParameter* getCenterColor() {
        return &(self->CenterColor);
    }

    Effekseer::AllTypeColorParameter* getInnerColor() {
        return &(self->InnerColor);
    }
};

// EffectNodeModel
%extend Effekseer::EffectNodeModel {
    Effekseer::AllTypeColorParameter* getAllColor() {
        return &(self->AllColor);
    }
};

// LocalForceFieldParameter
%extend Effekseer::LocalForceFieldParameter {
    Effekseer::LocalForceFieldElementParameter* getLocalForceFieldAtIndex(int index) {
        return &(self->LocalForceFields[index]);
    }
};

// SpriteColorParameter
%extend Effekseer::SpriteColorParameter {
    Effekseer::Color getFixedLowerLeftColor() { return self->fixed.ll; }
    void setFixedLowerLeftColor(Effekseer::Color value) { self->fixed.ll = value; }
    Effekseer::Color getFixedLowerRightColor() { return self->fixed.lr; }
    void setFixedLowerRightColor(Effekseer::Color value) { self->fixed.lr = value; }
    Effekseer::Color getFixedUpperLeftColor() { return self->fixed.ul; }
    void setFixedUpperLeftColor(Effekseer::Color value) { self->fixed.ul = value; }
    Effekseer::Color getFixedUpperRightColor() { return self->fixed.ur; }
    void setFixedUpperRightColor(Effekseer::Color value) { self->fixed.ur = value; }
};
// SpritePositionParameter
%extend Effekseer::SpritePositionParameter {
    Effekseer::SIMD::Vec2f getFixedLowerLeftPos() { return self->fixed.ll; }
    void setFixedLowerLeftPos(Effekseer::SIMD::Vec2f value) { self->fixed.ll = value; }
    Effekseer::SIMD::Vec2f getFixedLowerRightPos() { return self->fixed.lr; }
    void setFixedLowerRightPos(Effekseer::SIMD::Vec2f value) { self->fixed.lr = value; }
    Effekseer::SIMD::Vec2f getFixedUpperLeftPos() { return self->fixed.ul; }
    void setFixedUpperLeftPos(Effekseer::SIMD::Vec2f value) { self->fixed.ul = value; }
    Effekseer::SIMD::Vec2f getFixedUpperRightPos() { return self->fixed.ur; }
    void setFixedUpperRightPos(Effekseer::SIMD::Vec2f value) { self->fixed.ur = value; }
};
// SpriteColorParameter
%extend Effekseer::SpriteColorParameter {
    Effekseer::Color getFixedLowerLeftColor() { return self->fixed.ll; }
    void setFixedLowerLeftColor(Effekseer::Color value) { self->fixed.ll = value; }
    Effekseer::Color getFixedLowerRightColor() { return self->fixed.lr; }
    void setFixedLowerRightColor(Effekseer::Color value) { self->fixed.lr = value; }
    Effekseer::Color getFixedUpperLeftColor() { return self->fixed.ul; }
    void setFixedUpperLeftColor(Effekseer::Color value) { self->fixed.ul = value; }
    Effekseer::Color getFixedUpperRightColor() { return self->fixed.ur; }
    void setFixedUpperRightColor(Effekseer::Color value) { self->fixed.ur = value; }
};

// RibbonColorParameter
%extend Effekseer::RibbonColorParameter {
    Effekseer::Color getFixedLeftColor() { return self->fixed.l; }
    void setFixedLeftColor(Effekseer::Color value) { self->fixed.l = value; }
    Effekseer::Color getFixedRightColor() { return self->fixed.r; }
    void setFixedRightColor(Effekseer::Color value) { self->fixed.r = value; }
};
// RibbonPositionParameter
%extend Effekseer::RibbonPositionParameter {
    float getFixedLeftCoord() { return self->fixed.l; }
    void setFixedLeftCoord(float value) { self->fixed.l = value; }
    float getFixedRightCoord() { return self->fixed.r; }
    void setFixedRightCoord(float value) { self->fixed.r = value; }
};


// TrackSizeParameter
%extend Effekseer::TrackSizeParameter {
    float getFixedSize() { return self->fixed.size; }
    void setFixedSize(float value) { self->fixed.size = value; }
};