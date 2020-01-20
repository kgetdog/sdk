package com.robotemi.sdk.map;

import android.os.Parcel;
import android.os.Parcelable;

public class MapModel implements Parcelable {

    public static final Creator<MapModel> CREATOR = new Creator<MapModel>() {
        @Override
        public MapModel createFromParcel(Parcel in) {
            return new MapModel(in);
        }

        @Override
        public MapModel[] newArray(int size) {
            return new MapModel[size];
        }
    };
    private final MapInfoModel mapInfo;
    private final CurrentPoseModel currentPose;
    private final String mapImage;

    public MapModel(CurrentPoseModel currentPoseModel, MapInfoModel mapInfoModel, String mapImage) {
        this.currentPose = currentPoseModel;
        this.mapInfo = mapInfoModel;
        this.mapImage = mapImage;
    }

    protected MapModel(Parcel in) {
        mapInfo = in.readParcelable(MapInfoModel.class.getClassLoader());
        currentPose = in.readParcelable(CurrentPoseModel.class.getClassLoader());
        mapImage = in.readString();
    }

    public CurrentPoseModel getCurrentPose() {
        return currentPose;
    }

    public MapInfoModel getMapInfo() {
        return mapInfo;
    }

    public String getMapImage() {
        return mapImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mapInfo, flags);
        dest.writeParcelable(currentPose, flags);
        dest.writeString(mapImage);
    }
}
