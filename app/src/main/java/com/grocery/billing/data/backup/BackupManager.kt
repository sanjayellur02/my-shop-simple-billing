package com.grocery.billing.data.backup

import android.content.Context
import com.grocery.billing.data.AppDatabase
import com.grocery.billing.data.entity.Bill
import com.grocery.billing.data.entity.BillItem
import com.grocery.billing.data.entity.HeldBill
import com.grocery.billing.data.entity.HeldBillItem
import com.grocery.billing.data.entity.Product
import com.grocery.billing.data.entity.ProductPriceOption
import com.grocery.billing.data.repository.SettingsRepository
import com.grocery.billing.util.Dates
import androidx.room.withTransaction
import org.json.JSONArray
import org.json.JSONObject
import java.io.Reader
import java.io.Writer

/**
 * Backs up the entire app (settings, products, bills, bill items, held bills)
 * as a JSON file.
 */
class BackupManager(private val context: Context) {

    private val database = AppDatabase.getInstance(context)
    private val settingsRepository = SettingsRepository(database.settingsDao())

    suspend fun buildBackupJson(): String {
        val settings = settingsRepository.getAll()
        val products = database.productDao().getAll()
        val productPrices = database.productPriceDao().getAll()
        val bills = database.billDao().getAll()
        val items = database.billItemDao().getAll()
        val heldBills = database.heldBillDao().getAll()
        val heldItems = database.heldBillItemDao().getAll()

        val root = JSONObject()
        root.put("version", 3)
        root.put("exportedAt", Dates.isoTimestamp())

        root.put("settings", JSONObject().apply {
            for ((k, v) in settings) put(k, v)
        })

        root.put("products", JSONArray().apply {
            products.forEach { p ->
                put(JSONObject()
                    .put("id", p.id)
                    .put("name", p.name)
                    .put("sellingPrice", p.sellingPricePaise)
                    .put("unit", p.unit)
                    .put("barcode", p.barcode ?: JSONObject.NULL)
                    .put("sku", p.sku ?: JSONObject.NULL)
                    .put("createdAt", p.createdAt)
                    .put("updatedAt", p.updatedAt))
            }
        })

        root.put("productPrices", JSONArray().apply {
            productPrices.forEach { p ->
                put(JSONObject()
                    .put("productId", p.productId)
                    .put("sellingPrice", p.sellingPricePaise)
                    .put("unit", p.unit))
            }
        })

        root.put("bills", JSONArray().apply {
            bills.forEach { b ->
                put(JSONObject()
                    .put("billId", b.billId)
                    .put("billNumber", b.billNumber)
                    .put("billDate", b.billDate)
                    .put("billTime", b.billTime)
                    .put("subtotal", b.subtotalPaise)
                    .put("discount", b.discountPaise)
                    .put("total", b.totalPaise)
                    .put("createdAt", b.createdAt))
            }
        })

        root.put("billItems", JSONArray().apply {
            items.forEach { it ->
                put(JSONObject()
                    .put("billItemId", it.billItemId)
                    .put("billId", it.billId)
                    .put("productId", it.productId ?: JSONObject.NULL)
                    .put("productName", it.productNameSnapshot)
                    .put("quantity", it.quantity)
                    .put("rate", it.ratePaise)
                    .put("amount", it.amountPaise))
            }
        })

        root.put("heldBills", JSONArray().apply {
            heldBills.forEach { b ->
                put(JSONObject()
                    .put("heldBillId", b.heldBillId)
                    .put("reference", b.reference)
                    .put("billNumber", b.billNumber)
                    .put("billDate", b.billDate)
                    .put("billTime", b.billTime)
                    .put("subtotal", b.subtotalPaise)
                    .put("discount", b.discountPaise)
                    .put("total", b.totalPaise)
                    .put("createdAt", b.createdAt))
            }
        })

        root.put("heldBillItems", JSONArray().apply {
            heldItems.forEach { it ->
                put(JSONObject()
                    .put("heldBillItemId", it.heldBillItemId)
                    .put("heldBillId", it.heldBillId)
                    .put("productId", it.productId ?: JSONObject.NULL)
                    .put("productName", it.productNameSnapshot)
                    .put("quantity", it.quantity)
                    .put("rate", it.ratePaise)
                    .put("amount", it.amountPaise))
            }
        })

        return root.toString(2)
    }

    fun writeJsonTo(writer: Writer, json: String) {
        writer.write(json)
        writer.flush()
    }

    suspend fun restoreFrom(reader: Reader): RestoreResult {
        val text = reader.readText()
        val root = try {
            JSONObject(text)
        } catch (e: Exception) {
            return RestoreResult(false, "Not a valid backup file.")
        }
        if (root.optInt("version", -1) !in setOf(1, 2, 3)) {
            return RestoreResult(false, "Unsupported backup version.")
        }

        return try {
            database.withTransaction {
                val products = parseProducts(root.optJSONArray("products"))
                val productPrices = parseProductPrices(root.optJSONArray("productPrices"))
                val bills = parseBills(root.optJSONArray("bills"))
                val items = parseBillItems(root.optJSONArray("billItems"))
                val heldBills = parseHeldBills(root.optJSONArray("heldBills"))
                val heldItems = parseHeldBillItems(root.optJSONArray("heldBillItems"))
                val settings = parseSettings(root.optJSONObject("settings"))

                if (products.isEmpty() && bills.isEmpty() && settings.isEmpty()) {
                    return@withTransaction RestoreResult(false, "Backup file appears empty or invalid.")
                }

                database.billItemDao().deleteAll()
                database.billDao().deleteAll()
                database.heldBillItemDao().deleteAll()
                database.heldBillDao().deleteAll()
                database.productDao().deleteAll()
                database.productPriceDao().deleteAll()
                database.settingsDao().deleteAll()

                database.productDao().insertAll(products)
                if (productPrices.isNotEmpty()) database.productPriceDao().insertAll(productPrices)
                bills.forEach { database.billDao().insert(it) }
                if (items.isNotEmpty()) database.billItemDao().insertAll(items)
                heldBills.forEach { database.heldBillDao().insert(it) }
                if (heldItems.isNotEmpty()) database.heldBillItemDao().insertAll(heldItems)
                database.settingsDao().putAll(
                    settings.map { com.grocery.billing.data.entity.Setting(it.key, it.value) }
                )
            }
            RestoreResult(true, null)
        } catch (e: OutOfMemoryError) {
            RestoreResult(false, "Backup file is too large to restore.")
        } catch (e: Exception) {
            RestoreResult(false, "Restore failed: ${e.message}")
        }
    }

    private fun parseProducts(arr: JSONArray?): List<Product> {
        if (arr == null) return emptyList()
        return (0 until arr.length()).mapNotNull { i ->
            val o = arr.getJSONObject(i)
            Product(
                id = o.getString("id"),
                name = o.getString("name"),
                sellingPricePaise = o.optLong("sellingPrice", 0),
                unit = o.optString("unit", ""),
                barcode = if (o.isNull("barcode")) null else o.optString("barcode", ""),
                sku = if (o.isNull("sku")) null else o.optString("sku", ""),
                createdAt = o.optString("createdAt", ""),
                updatedAt = o.optString("updatedAt", "")
            )
        }
    }

    private fun parseProductPrices(arr: JSONArray?): List<ProductPriceOption> {
        if (arr == null) return emptyList()
        return (0 until arr.length()).mapNotNull { i ->
            val o = arr.getJSONObject(i)
            ProductPriceOption(
                productId = o.getString("productId"),
                sellingPricePaise = o.optLong("sellingPrice", 0),
                unit = o.optString("unit", "")
            )
        }
    }

    private fun parseBills(arr: JSONArray?): List<Bill> {
        if (arr == null) return emptyList()
        return (0 until arr.length()).mapNotNull { i ->
            val o = arr.getJSONObject(i)
            Bill(
                billId = o.optLong("billId", 0),
                billNumber = o.getString("billNumber"),
                billDate = o.optString("billDate", ""),
                billTime = o.optString("billTime", ""),
                subtotalPaise = o.optLong("subtotal", 0),
                discountPaise = o.optLong("discount", 0),
                totalPaise = o.optLong("total", 0),
                createdAt = o.optString("createdAt", "")
            )
        }
    }

    private fun parseBillItems(arr: JSONArray?): List<BillItem> {
        if (arr == null) return emptyList()
        return (0 until arr.length()).mapNotNull { i ->
            val o = arr.getJSONObject(i)
            BillItem(
                billItemId = o.optLong("billItemId", 0),
                billId = o.getLong("billId"),
                productId = if (o.isNull("productId")) null else o.optString("productId"),
                productNameSnapshot = o.getString("productName"),
                quantity = o.optString("quantity", "1"),
                ratePaise = o.optLong("rate", 0),
                amountPaise = o.optLong("amount", 0)
            )
        }
    }

    private fun parseSettings(obj: JSONObject?): Map<String, String> {
        if (obj == null) return emptyMap()
        val map = HashMap<String, String>()
        val keys = obj.keys()
        while (keys.hasNext()) {
            val k = keys.next()
            map[k] = obj.optString(k, "")
        }
        return map
    }

    private fun parseHeldBills(arr: JSONArray?): List<HeldBill> {
        if (arr == null) return emptyList()
        return (0 until arr.length()).mapNotNull { i ->
            val o = arr.getJSONObject(i)
            HeldBill(
                heldBillId = o.optLong("heldBillId", 0),
                reference = o.optString("reference", ""),
                billNumber = o.getString("billNumber"),
                billDate = o.optString("billDate", ""),
                billTime = o.optString("billTime", ""),
                subtotalPaise = o.optLong("subtotal", 0),
                discountPaise = o.optLong("discount", 0),
                totalPaise = o.optLong("total", 0),
                createdAt = o.optString("createdAt", "")
            )
        }
    }

    private fun parseHeldBillItems(arr: JSONArray?): List<HeldBillItem> {
        if (arr == null) return emptyList()
        return (0 until arr.length()).mapNotNull { i ->
            val o = arr.getJSONObject(i)
            HeldBillItem(
                heldBillItemId = o.optLong("heldBillItemId", 0),
                heldBillId = o.getLong("heldBillId"),
                productId = if (o.isNull("productId")) null else o.optString("productId"),
                productNameSnapshot = o.getString("productName"),
                quantity = o.optString("quantity", "1"),
                ratePaise = o.optLong("rate", 0),
                amountPaise = o.optLong("amount", 0)
            )
        }
    }
}

data class RestoreResult(val success: Boolean, val error: String?)
