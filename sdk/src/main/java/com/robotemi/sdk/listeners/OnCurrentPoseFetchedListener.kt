package com.robotemi.sdk.listeners

import com.robotemi.sdk.map.CurrentPoseModel

interface OnCurrentPoseFetchedListener {

    /**
     *
     *
     */
    fun onPoseFetched(currentPoseModel: CurrentPoseModel)
}
