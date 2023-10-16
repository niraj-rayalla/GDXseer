#include "GDXMatrixAdapter.h"

GDXMatrixAdapter::GDXMatrixAdapter() { }
GDXMatrixAdapter::~GDXMatrixAdapter() { }

Effekseer::Matrix44 GDXMatrixAdapter::convertMatrix44ToEffekseer(float flattenedMatrix[]) {
    auto vec = Effekseer::Matrix44();

    vec.Values[0][0] = flattenedMatrix[0];
    vec.Values[0][1] = flattenedMatrix[1];
    vec.Values[0][2] = flattenedMatrix[2];
    vec.Values[0][3] = flattenedMatrix[3];

    vec.Values[1][0] = flattenedMatrix[4];
    vec.Values[1][1] = flattenedMatrix[5];
    vec.Values[1][2] = flattenedMatrix[6];
    vec.Values[1][3] = flattenedMatrix[7];

    vec.Values[2][0] = flattenedMatrix[8];
    vec.Values[2][1] = flattenedMatrix[9];
    vec.Values[2][2] = flattenedMatrix[10];
    vec.Values[2][3] = flattenedMatrix[11];

    vec.Values[3][0] = flattenedMatrix[12];
    vec.Values[3][1] = flattenedMatrix[13];
    vec.Values[3][2] = flattenedMatrix[14];
    vec.Values[3][3] = flattenedMatrix[15];

    return vec;
}

float* GDXMatrixAdapter::convertMatrix44ToGDX(Effekseer::Matrix44 matrix) {
    static float vec[16];

    auto m = matrix.Values;

    vec[0] = m[0][0];
    vec[1] = m[0][1];
    vec[2] = m[0][2];
    vec[3] = m[0][3];

    vec[4] = m[1][0];
    vec[5] = m[1][1];
    vec[6] = m[1][2];
    vec[7] = m[1][3];

    vec[8] = m[2][0];
    vec[9] = m[2][1];
    vec[10] = m[2][2];
    vec[11] = m[2][3];

    vec[12] = m[3][0];
    vec[13] = m[3][1];
    vec[14] = m[3][2];
    vec[15] = m[3][3];

    return vec;
}

Effekseer::Matrix43 GDXMatrixAdapter::convertMatrix43ToEffekseer(float flattenedMatrix[]) {
    auto vec = Effekseer::Matrix43();

    vec.Value[0][0] = flattenedMatrix[0];
    vec.Value[0][1] = flattenedMatrix[1];
    vec.Value[0][2] = flattenedMatrix[2];

    vec.Value[1][0] = flattenedMatrix[3];
    vec.Value[1][1] = flattenedMatrix[4];
    vec.Value[1][2] = flattenedMatrix[5];

    vec.Value[2][0] = flattenedMatrix[6];
    vec.Value[2][1] = flattenedMatrix[7];
    vec.Value[2][2] = flattenedMatrix[8];

    vec.Value[3][0] = flattenedMatrix[9];
    vec.Value[3][1] = flattenedMatrix[10];
    vec.Value[3][2] = flattenedMatrix[11];

    return vec;
}

float* GDXMatrixAdapter::convertMatrix43ToGDX(Effekseer::Matrix43 matrix) {
    static float vec[12];

    auto m = matrix.Value;

    vec[0] = m[0][0];
    vec[1] = m[0][1];
    vec[2] = m[0][2];

    vec[3] = m[1][0];
    vec[4] = m[1][1];
    vec[5] = m[1][2];

    vec[6] = m[2][0];
    vec[7] = m[2][1];
    vec[8] = m[2][2];

    vec[9] =  m[3][0];
    vec[10] = m[3][1];
    vec[11] = m[3][2];

    return vec;
}