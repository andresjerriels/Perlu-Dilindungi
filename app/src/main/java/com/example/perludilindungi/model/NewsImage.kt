package com.example.perludilindungi.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class NewsImage(
    @SerializedName("_url") var _url: String? = null,
    @SerializedName("_length") var _length: String? = null,
    @SerializedName("_type") var _type: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.writeString(_url)
        parcel?.writeString(_length)
        parcel?.writeString(_type)
    }

    companion object CREATOR : Parcelable.Creator<NewsImage> {
        override fun createFromParcel(parcel: Parcel): NewsImage {
            return NewsImage(parcel)
        }

        override fun newArray(size: Int): Array<NewsImage?> {
            return arrayOfNulls(size)
        }
    }

}
