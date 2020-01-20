package com.robotemi.sdk.map

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MapInfoModel(
    val resolution: Float,
    val height: Int,
    val width: Int,
    val origin: Origin
) : Parcelable

@Parcelize
data class Origin(val position: Position) : Parcelable

@Parcelize
data class Position(val x: Float, val y: Float) : Parcelable