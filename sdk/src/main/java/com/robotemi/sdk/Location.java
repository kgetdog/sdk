package com.robotemi.sdk;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Location implements Parcelable {

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
    @NonNull
    private final float x;
    @NonNull
    private final float y;
    @NonNull
    private final float azimuth;
    @NonNull
    private final String name;

    public Location(final float x, final float y, final float azimuth, @NonNull final String name) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.azimuth = azimuth;
    }

    protected Location(Parcel in) {
        x = in.readFloat();
        y = in.readFloat();
        azimuth = in.readFloat();
        name = in.readString();
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getAzimuth() {
        return azimuth;
    }

    @NonNull
    public String getName() {
        return name;
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
        dest.writeString(name);
    }
}