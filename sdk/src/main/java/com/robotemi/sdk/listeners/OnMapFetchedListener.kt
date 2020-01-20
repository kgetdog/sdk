package com.robotemi.sdk.listeners

import com.robotemi.sdk.map.MapModel

interface OnMapFetchedListener {

    /**
     *
     *
     */
    fun onMapFetched(mapModel: MapModel?)
}
