#pragma once

#include "../Effekseer/Dev/Cpp/Effekseer/Effekseer.h"

/**
 * Contains static methods that are used to convert matrix data from GDX to Effekseer and vice-versa.
 */
class GDXMatrixAdapter {
public:
    GDXMatrixAdapter();
    ~GDXMatrixAdapter();

    static Effekseer::Matrix44 convertMatrix44ToEffekseer(float flattenedMatrix[]);
    static float* convertMatrix44ToGDX(Effekseer::Matrix44 matrix);

    static Effekseer::Matrix43 convertMatrix43ToEffekseer(float flattenedMatrix[]);
    static float* convertMatrix43ToGDX(Effekseer::Matrix43 matrix);
};