package com.example.perludilindungi.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class FaskesDetail (
    @SerializedName("id"               ) var id             : Int?    = null,
    @SerializedName("kode"             ) var kode           : String? = null,
    @SerializedName("batch"            ) var batch          : String? = null,
    @SerializedName("divaksin"         ) var divaksin       : Int?    = null,
    @SerializedName("divaksin_1"       ) var divaksin1      : Int?    = null,
    @SerializedName("divaksin_2"       ) var divaksin2      : Int?    = null,
    @SerializedName("batal_vaksin"     ) var batalVaksin    : Int?    = null,
    @SerializedName("batal_vaksin_1"   ) var batalVaksin1   : Int?    = null,
    @SerializedName("batal_vaksin_2"   ) var batalVaksin2   : Int?    = null,
    @SerializedName("pending_vaksin"   ) var pendingVaksin  : Int?    = null,
    @SerializedName("pending_vaksin_1" ) var pendingVaksin1 : Int?    = null,
    @SerializedName("pending_vaksin_2" ) var pendingVaksin2 : Int?    = null,
    @SerializedName("tanggal"          ) var tanggal        : String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(kode)
        parcel.writeString(batch)
        parcel.writeValue(divaksin)
        parcel.writeValue(divaksin1)
        parcel.writeValue(divaksin2)
        parcel.writeValue(batalVaksin)
        parcel.writeValue(batalVaksin1)
        parcel.writeValue(batalVaksin2)
        parcel.writeValue(pendingVaksin)
        parcel.writeValue(pendingVaksin1)
        parcel.writeValue(pendingVaksin2)
        parcel.writeString(tanggal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FaskesDetail> {
        override fun createFromParcel(parcel: Parcel): FaskesDetail {
            return FaskesDetail(parcel)
        }

        override fun newArray(size: Int): Array<FaskesDetail?> {
            return arrayOfNulls(size)
        }
    }
}