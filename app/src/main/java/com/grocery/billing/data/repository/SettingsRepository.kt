package com.grocery.billing.data.repository

import com.grocery.billing.data.dao.SettingsDao
import com.grocery.billing.data.entity.Setting
import com.grocery.billing.data.entity.SettingsKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

data class ShopSettings(
    val shopName: String,
    val address: String,
    val phone: String,
    val currencySymbol: String,
    val thankYou: String,
    val customerThankYou: String,
    val showShopAddress: Boolean,
    val allowPriceOverride: Boolean
)

class SettingsRepository(private val dao: SettingsDao) {

    fun observe(key: String): Flow<String> =
        dao.observe(key).map { it ?: SettingsKeys.defaults[key] ?: "" }

    fun observeShopSettings(): Flow<ShopSettings> = combine(
        observe(SettingsKeys.SHOP_NAME),
        observe(SettingsKeys.SHOP_ADDRESS),
        observe(SettingsKeys.SHOP_PHONE),
        observe(SettingsKeys.CURRENCY_SYMBOL),
        observe(SettingsKeys.THANK_YOU_MESSAGE),
        observe(SettingsKeys.CUSTOMER_THANK_YOU),
        observe(SettingsKeys.SHOW_SHOP_ADDRESS),
        observe(SettingsKeys.ALLOW_PRICE_OVERRIDE)
    ) { values ->
        ShopSettings(
            shopName = values[0],
            address = values[1],
            phone = values[2],
            currencySymbol = values[3],
            thankYou = values[4],
            customerThankYou = values[5],
            showShopAddress = values[6].toBoolean(),
            allowPriceOverride = values[7].toBoolean()
        )
    }

    suspend fun get(key: String): String =
        dao.get(key) ?: SettingsKeys.defaults[key] ?: ""

    suspend fun set(key: String, value: String) = dao.put(Setting(key, value))

    suspend fun setAll(map: Map<String, String>) = dao.putAll(map.map { Setting(it.key, it.value) })

    suspend fun getAll(): Map<String, String> =
        dao.getAll().associate { it.key to it.value }

    suspend fun deleteAll() = dao.deleteAll()

    suspend fun ensureDefaults() {
        val existing = dao.getAll().map { it.key }.toSet()
        val missing = SettingsKeys.defaults.filterKeys { it !in existing }
        if (missing.isNotEmpty()) dao.putAll(missing.map { Setting(it.key, it.value) })
    }
}
