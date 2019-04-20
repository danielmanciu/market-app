package com.example.manciu.marketapp.data.local.persistence

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.manciu.marketapp.data.local.persistence.PersistenceConstants.ProductEntity.COLUMN_INFO_DESCRIPTION
import com.example.manciu.marketapp.data.local.persistence.PersistenceConstants.ProductEntity.COLUMN_INFO_NAME
import com.example.manciu.marketapp.data.local.persistence.PersistenceConstants.ProductEntity.COLUMN_INFO_PRICE
import com.example.manciu.marketapp.data.local.persistence.PersistenceConstants.ProductEntity.COLUMN_INFO_QUANTITY
import com.example.manciu.marketapp.data.local.persistence.PersistenceConstants.ProductEntity.COLUMN_INFO_STATUS
import com.example.manciu.marketapp.data.local.persistence.PersistenceConstants.ProductEntity.TABLE_NAME
import com.example.manciu.marketapp.data.remote.ProductRemoteEntity
import kotlinx.android.parcel.Parcelize

@Entity(tableName = TABLE_NAME)
@Parcelize
data class ProductEntity(
        @PrimaryKey
        var id: Int,

        @ColumnInfo(name = COLUMN_INFO_NAME)
        val name: String,

        @ColumnInfo(name = COLUMN_INFO_DESCRIPTION)
        val description: String,

        @ColumnInfo(name = COLUMN_INFO_QUANTITY)
        val quantity: Int,

        @ColumnInfo(name = COLUMN_INFO_PRICE)
        val price: Int,

        @ColumnInfo(name = COLUMN_INFO_STATUS)
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