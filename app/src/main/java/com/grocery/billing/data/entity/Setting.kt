package com.grocery.billing.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class Setting(
    @PrimaryKey
    @ColumnInfo(name = "key")
    val key: String,
    @ColumnInfo(name = "value")
    val value: String
)

object SettingsKeys {
    const val SHOP_NAME = "shop_name"
    const val SHOP_ADDRESS = "shop_address"
    const val SHOP_PHONE = "shop_phone"
    const val CURRENCY_SYMBOL = "currency_symbol"
    const val THANK_YOU_MESSAGE = "thank_you_message"
    const val SHOW_SHOP_ADDRESS = "show_shop_address"
    const val CUSTOMER_THANK_YOU = "customer_thank_you"
    const val ALLOW_PRICE_OVERRIDE = "allow_price_override"

    const val DEFAULT_SHOP_NAME = "My Shop"
    const val DEFAULT_CURRENCY_SYMBOL = "₹"
    const val DEFAULT_THANK_YOU = "Thank you!"
    const val DEFAULT_CUSTOMER_THANK_YOU = "Thank you for shopping with us!"

    val defaults: Map<String, String> = mapOf(
        SHOP_NAME to DEFAULT_SHOP_NAME,
        SHOP_ADDRESS to "",
        SHOP_PHONE to "",
        CURRENCY_SYMBOL to DEFAULT_CURRENCY_SYMBOL,
        THANK_YOU_MESSAGE to DEFAULT_THANK_YOU,
        SHOW_SHOP_ADDRESS to "true",
        CUSTOMER_THANK_YOU to DEFAULT_CUSTOMER_THANK_YOU,
        ALLOW_PRICE_OVERRIDE to "true"
    )
}
