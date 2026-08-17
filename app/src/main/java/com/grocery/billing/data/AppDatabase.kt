package com.grocery.billing.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.grocery.billing.data.dao.BillDao
import com.grocery.billing.data.dao.BillItemDao
import com.grocery.billing.data.dao.DraftDao
import com.grocery.billing.data.dao.HeldBillDao
import com.grocery.billing.data.dao.HeldBillItemDao
import com.grocery.billing.data.dao.ProductDao
import com.grocery.billing.data.dao.ProductPriceDao
import com.grocery.billing.data.dao.SettingsDao
import com.grocery.billing.data.entity.Bill
import com.grocery.billing.data.entity.BillItem
import com.grocery.billing.data.entity.Draft
import com.grocery.billing.data.entity.HeldBill
import com.grocery.billing.data.entity.HeldBillItem
import com.grocery.billing.data.entity.Product
import com.grocery.billing.data.entity.ProductPriceOption
import com.grocery.billing.data.entity.Setting

@Database(
    entities = [
        Product::class,
        ProductPriceOption::class,
        Bill::class,
        BillItem::class,
        Setting::class,
        HeldBill::class,
        HeldBillItem::class,
        Draft::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun productPriceDao(): ProductPriceDao
    abstract fun billDao(): BillDao
    abstract fun billItemDao(): BillItemDao
    abstract fun settingsDao(): SettingsDao
    abstract fun heldBillDao(): HeldBillDao
    abstract fun heldBillItemDao(): HeldBillItemDao
    abstract fun draftDao(): DraftDao

    companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE products ADD COLUMN selling_price INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE products ADD COLUMN unit TEXT NOT NULL DEFAULT ''")
                db.execSQL("ALTER TABLE products ADD COLUMN barcode TEXT")
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS held_bills (
                        held_bill_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        reference TEXT NOT NULL DEFAULT '',
                        bill_number TEXT NOT NULL,
                        bill_date TEXT NOT NULL,
                        bill_time TEXT NOT NULL,
                        subtotal INTEGER NOT NULL,
                        discount INTEGER NOT NULL,
                        total INTEGER NOT NULL,
                        created_at TEXT NOT NULL
                    )
                    """
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS held_bill_items (
                        held_bill_item_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        held_bill_id INTEGER NOT NULL,
                        product_id TEXT,
                        product_name_snapshot TEXT NOT NULL,
                        quantity TEXT NOT NULL,
                        rate INTEGER NOT NULL,
                        amount INTEGER NOT NULL,
                        FOREIGN KEY(held_bill_id) REFERENCES held_bills(held_bill_id) ON DELETE CASCADE
                    )
                    """
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_held_bill_items_held_bill_id ON held_bill_items(held_bill_id)"
                )
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS product_prices (
                        option_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        product_id TEXT NOT NULL,
                        selling_price_paise INTEGER NOT NULL,
                        unit TEXT NOT NULL,
                        FOREIGN KEY(product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE
                    )
                    """
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_product_prices_product_id ON product_prices(product_id)"
                )
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS product_prices_new (
                        option_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        product_id TEXT NOT NULL,
                        selling_price_paise INTEGER NOT NULL,
                        unit TEXT NOT NULL DEFAULT '',
                        FOREIGN KEY(product_id) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE
                    )
                    """
                )
                db.execSQL(
                    "INSERT INTO product_prices_new (option_id, product_id, selling_price_paise, unit) " +
                        "SELECT option_id, product_id, selling_price_paise, unit FROM product_prices"
                )
                db.execSQL("DROP TABLE product_prices")
                db.execSQL("ALTER TABLE product_prices_new RENAME TO product_prices")
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_product_prices_product_id ON product_prices(product_id)"
                )
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS drafts (
                        draft_key TEXT NOT NULL PRIMARY KEY,
                        draft_data TEXT NOT NULL,
                        updated_at TEXT NOT NULL
                    )
                    """
                )
            }
        }

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "grocery_billing.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                    .build().also { instance = it }
            }
    }
}
