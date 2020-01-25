package org.frc1732scoutingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Team implements Parcelable {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
       @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };

    private Integer teamNumber;
    private Integer matchNumber;
    private Integer initLine;
    private Integer autoLower;
    private Integer autoOuter;
    private Integer autoInner;
    private Integer lower;
    private Integer outer;
    private Integer inner;
    private Integer rotation;
    private Integer position;
    private Integer park;
    private Integer hang;
    private Integer level;
    private Integer disableTime;

    public Team(Integer teamNumber, Integer matchNumber, Integer initLine, Integer autoLower, Integer autoOuter, Integer autoInner, Integer lower, Integer outer, Integer inner, Integer rotation, Integer position, Integer park, Integer hang, Integer level, Integer disableTime) {
        this.teamNumber = teamNumber;
        this.matchNumber = matchNumber;
        this.initLine = initLine;
        this.autoLower = autoLower;
        this.autoOuter = autoOuter;
        this.autoInner = autoInner;
        this.lower = lower;
        this.outer = outer;
        this.inner = inner;
        this.rotation = rotation;
        this.position = position;
        this.park = park;
        this.hang = hang;
        this.level = level;
        this.disableTime = disableTime;
    }

    public Team(Parcel in) {
        this.teamNumber = in.readInt();
        this.matchNumber = in.readInt();
        this.initLine = in.readInt();
        this.autoLower = in.readInt();
        this.autoOuter = in.readInt();
        this.autoInner = in.readInt();
        this.lower = in.readInt();
        this.outer = in.readInt();
        this.inner = in.readInt();
        this.rotation = in.readInt();
        this.position = in.readInt();
        this.park = in.readInt();
        this.hang = in.readInt();
        this.level = in.readInt();
        this.disableTime = in.readInt();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.teamNumber);
        dest.writeInt(this.matchNumber);
        dest.writeInt(this.initLine);
        dest.writeInt(this.autoLower);
        dest.writeInt(this.autoOuter);
        dest.writeInt(this.autoInner);
        dest.writeInt(this.lower);
        dest.writeInt(this.outer);
        dest.writeInt(this.inner);
        dest.writeInt(this.rotation);
        dest.writeInt(this.position);
        dest.writeInt(this.park);
        dest.writeInt(this.hang);
        dest.writeInt(this.level);
        dest.writeInt(this.disableTime);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Team) {
            return teamNumber == ((Team)obj).teamNumber;
        }
        return false;
    }

    public Integer getTeamNumber() {
        return teamNumber;
    }

    public Integer getMatchNumber() {
        return matchNumber;
    }

    public Integer getInitLine() {
        return initLine;
    }

    public Integer getAutoLower() {
        return autoLower;
    }

    public Integer getAutoOuter() {
        return autoOuter;
    }

    public Integer getAutoInner() {
        return autoInner;
    }

    public Integer getLower() {
        return lower;
    }

    public Integer getOuter() {
        return outer;
    }

    public Integer getInner() {
        return inner;
    }

    public Integer getRotation() {
        return rotation;
    }

    public Integer getPosition() {
        return position;
    }

    public Integer getPark() {
        return park;
    }

    public Integer getHang() {
        return hang;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getDisableTime() {
        return disableTime;
    }
}
