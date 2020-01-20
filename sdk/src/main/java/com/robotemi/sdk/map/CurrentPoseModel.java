package com.robotemi.sdk.map;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrentPoseModel implements Parcelable {

    public static final Creator<CurrentPoseModel> CREATOR = new Creator<CurrentPoseModel>() {
        @Override
        public CurrentPoseModel createFromParcel(Parcel in) {
            return new CurrentPoseModel(in);
        }

        @Override
        public CurrentPoseModel[] newArray(int size) {
            return new CurrentPoseModel[size];
        }
    };
    private float x;
    private float y;
    private float azimuth;

    public CurrentPoseModel(float x, float y, float azimuth) {
        this.x = x;
        this.y = y;
        this.azimuth = azimuth;
    }

    protected CurrentPoseModel(Parcel in) {
        x = in.readFloat();
        y = in.readFloat();
        azimuth = in.readFloat();
    }

    public float getAzimuth() {
        return azimuth;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setAzimuth(float azimuth) {
        this.azimuth = azimuth;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(x);
        dest.writeFloat(y);
        dest.writeFloat(azimuth);
    }
}
