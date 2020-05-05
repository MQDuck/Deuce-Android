/*
 * Copyright (C) 2020 Jeffrey Thomas Piercy
 *
 * This file is part of Deuce-Android.
 *
 * Deuce-Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Deuce-Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Deuce-Android.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.mqduck.deuce.common

import android.os.Parcel
import android.os.Parcelable

interface PlayTimes {
    var startTime: Long
    var endTime: Long
}

class PlayTimesData(
    override var startTime: Long = System.currentTimeMillis(),
    override var endTime: Long = -1
) : PlayTimes, Parcelable {
    constructor(parcel: Parcel) : this(parcel.readLong(), parcel.readLong())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(startTime)
        parcel.writeLong(endTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayTimesData> {
        override fun createFromParcel(parcel: Parcel): PlayTimesData {
            return PlayTimesData(parcel)
        }

        override fun newArray(size: Int): Array<PlayTimesData?> {
            return arrayOfNulls(size)
        }
    }
}