package com.example.manciu.marketapp.data.persistence

object PersistenceConstants {

    const val DATABASE_NAME = "Products.db"

    object ProductEntity {
        const val TABLE_NAME = "product"
        const val COLUMN_INFO_NAME = "name"
        const val COLUMN_INFO_DESCRIPTION = "description"
        const val COLUMN_INFO_QUANTITY = "quantity"
        const val COLUMN_INFO_PRICE = "price"
        const val COLUMN_INFO_STATUS = "status"
    }

}