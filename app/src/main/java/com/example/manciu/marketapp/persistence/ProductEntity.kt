package com.example.manciu.marketapp.persistence

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.manciu.marketapp.remote.ProductRemoteEntity
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "product")
@Parcelize
data class ProductEntity(
    @PrimaryKey
    var id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "price")
    val price: Int,

    @ColumnInfo(name = "string")
    val status: String

) : Parcelable {
    fun convertLocalToRemote() = ProductRemoteEntity(
        id,
        name,
        description,
        quantity,
        price,
        status
    )

}