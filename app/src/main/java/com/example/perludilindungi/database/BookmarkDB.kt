package com.example.perludilindungi.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookmarkDB (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "kode"         ) var kode        : String?           = null,
    @ColumnInfo(name = "nama"         ) var nama        : String?           = null,
    @ColumnInfo(name = "kota"         ) var kota        : String?           = null,
    @ColumnInfo(name = "provinsi"     ) var provinsi    : String?           = null,
    @ColumnInfo(name = "alamat"       ) var alamat      : String?           = null,
    @ColumnInfo(name = "latitude"     ) var latitude    : String?           = null,
    @ColumnInfo(name = "longitude"    ) var longitude   : String?           = null,
    @ColumnInfo(name = "telp"         ) var telp        : String?           = null,
    @ColumnInfo(name = "jenis_faskes" ) var jenisFaskes : String?           = null,
    @ColumnInfo(name = "kelas_rs"     ) var kelasRs     : String?           = null,
    @ColumnInfo(name = "status"       ) var status      : String?           = null,
    @ColumnInfo(name = "source_data"  ) var sourceData  : String?           = null

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
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
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
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
        parcel.writeString(sourceData)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookmarkDB> {
        override fun createFromParcel(parcel: Parcel): BookmarkDB {
            return BookmarkDB(parcel)
        }

        override fun newArray(size: Int): Array<BookmarkDB?> {
            return arrayOfNulls(size)
        }
    }
}