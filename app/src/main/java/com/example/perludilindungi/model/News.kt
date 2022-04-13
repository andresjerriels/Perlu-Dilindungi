package com.example.perludilindungi.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("title") var title: String? = null,
    @SerializedName("guid") var guid: String? = null,
    @SerializedName("pubDate") var pubDate: String? = null,
    @SerializedName("enclosure") var enclosure: NewsImage? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(ClassLoader.getSystemClassLoader())
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.writeString(title)
        parcel?.writeString(guid)
        parcel?.writeString(pubDate)
        parcel?.writeParcelable(enclosure, p1)
    }

    companion object CREATOR : Parcelable.Creator<News> {
        override fun createFromParcel(parcel: Parcel): News {
            return News(parcel)
        }

        override fun newArray(size: Int): Array<News?> {
            return arrayOfNulls(size)
        }
    }


}
