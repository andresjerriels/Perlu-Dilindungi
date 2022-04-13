package com.example.perludilindungi.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Faskes (

    @SerializedName("id"           ) var id          : Int?              = null,
    @SerializedName("kode"         ) var kode        : String?           = null,
    @SerializedName("nama"         ) var nama        : String?           = null,
    @SerializedName("kota"         ) var kota        : String?           = null,
    @SerializedName("provinsi"     ) var provinsi    : String?           = null,
    @SerializedName("alamat"       ) var alamat      : String?           = null,
    @SerializedName("latitude"     ) var latitude    : String?           = null,
    @SerializedName("longitude"    ) var longitude   : String?           = null,
    @SerializedName("telp"         ) var telp        : String?           = null,
    @SerializedName("jenis_faskes" ) var jenisFaskes : String?           = null,
    @SerializedName("kelas_rs"     ) var kelasRs     : String?           = null,
    @SerializedName("status"       ) var status      : String?           = null,
    @SerializedName("detail"       ) var detail      : ArrayList<FaskesDetail> = arrayListOf(),
    @SerializedName("source_data"  ) var sourceData  : String?           = null

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        arrayListOf<FaskesDetail>().apply {
            parcel.readArrayList(FaskesDetail::class.java.classLoader)
        },
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(kode)
        parcel.writeString(nama)
        parcel.writeString(kota)
        parcel.writeString(provinsi)
        parcel.writeString(alamat)
        parcel.writeString(latitude)
        parcel.writeString(longitude)
        parcel.writeString(telp)
        parcel.writeString(jenisFaskes)
        parcel.writeString(kelasRs)
        parcel.writeString(status)
        parcel.writeList(detail)
        parcel.writeString(sourceData)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Faskes> {
        override fun createFromParcel(parcel: Parcel): Faskes {
            return Faskes(parcel)
        }

        override fun newArray(size: Int): Array<Faskes?> {
            return arrayOfNulls(size)
        }
    }
}