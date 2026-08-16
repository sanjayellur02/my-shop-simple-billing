package com.grocery.billing.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.grocery.billing.data.dao.BillDao;
import com.grocery.billing.data.dao.BillDao_Impl;
import com.grocery.billing.data.dao.BillItemDao;
import com.grocery.billing.data.dao.BillItemDao_Impl;
import com.grocery.billing.data.dao.HeldBillDao;
import com.grocery.billing.data.dao.HeldBillDao_Impl;
import com.grocery.billing.data.dao.HeldBillItemDao;
import com.grocery.billing.data.dao.HeldBillItemDao_Impl;
import com.grocery.billing.data.dao.ProductDao;
import com.grocery.billing.data.dao.ProductDao_Impl;
import com.grocery.billing.data.dao.SettingsDao;
import com.grocery.billing.data.dao.SettingsDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ProductDao _productDao;

  private volatile BillDao _billDao;

  private volatile BillItemDao _billItemDao;

  private volatile SettingsDao _settingsDao;

  private volatile HeldBillDao _heldBillDao;

  private volatile HeldBillItemDao _heldBillItemDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `products` (`product_id` TEXT NOT NULL, `product_name` TEXT NOT NULL, `selling_price` INTEGER NOT NULL, `unit` TEXT NOT NULL, `barcode` TEXT, `created_at` TEXT NOT NULL, `updated_at` TEXT NOT NULL, PRIMARY KEY(`product_id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `bills` (`bill_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bill_number` TEXT NOT NULL, `bill_date` TEXT NOT NULL, `bill_time` TEXT NOT NULL, `subtotal` INTEGER NOT NULL, `discount` INTEGER NOT NULL, `total` INTEGER NOT NULL, `created_at` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `bill_items` (`bill_item_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bill_id` INTEGER NOT NULL, `product_id` TEXT, `product_name_snapshot` TEXT NOT NULL, `quantity` TEXT NOT NULL, `rate` INTEGER NOT NULL, `amount` INTEGER NOT NULL, FOREIGN KEY(`bill_id`) REFERENCES `bills`(`bill_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_bill_items_bill_id` ON `bill_items` (`bill_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `settings` (`key` TEXT NOT NULL, `value` TEXT NOT NULL, PRIMARY KEY(`key`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `held_bills` (`held_bill_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `reference` TEXT NOT NULL, `bill_number` TEXT NOT NULL, `bill_date` TEXT NOT NULL, `bill_time` TEXT NOT NULL, `subtotal` INTEGER NOT NULL, `discount` INTEGER NOT NULL, `total` INTEGER NOT NULL, `created_at` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `held_bill_items` (`held_bill_item_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `held_bill_id` INTEGER NOT NULL, `product_id` TEXT, `product_name_snapshot` TEXT NOT NULL, `quantity` TEXT NOT NULL, `rate` INTEGER NOT NULL, `amount` INTEGER NOT NULL, FOREIGN KEY(`held_bill_id`) REFERENCES `held_bills`(`held_bill_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_held_bill_items_held_bill_id` ON `held_bill_items` (`held_bill_id`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '1caf8fb0f650dec1d2cff6fe2e2d161c')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `products`");
        db.execSQL("DROP TABLE IF EXISTS `bills`");
        db.execSQL("DROP TABLE IF EXISTS `bill_items`");
        db.execSQL("DROP TABLE IF EXISTS `settings`");
        db.execSQL("DROP TABLE IF EXISTS `held_bills`");
        db.execSQL("DROP TABLE IF EXISTS `held_bill_items`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsProducts = new HashMap<String, TableInfo.Column>(7);
        _columnsProducts.put("product_id", new TableInfo.Column("product_id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("product_name", new TableInfo.Column("product_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("selling_price", new TableInfo.Column("selling_price", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("unit", new TableInfo.Column("unit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("barcode", new TableInfo.Column("barcode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("created_at", new TableInfo.Column("created_at", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsProducts.put("updated_at", new TableInfo.Column("updated_at", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysProducts = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesProducts = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoProducts = new TableInfo("products", _columnsProducts, _foreignKeysProducts, _indicesProducts);
        final TableInfo _existingProducts = TableInfo.read(db, "products");
        if (!_infoProducts.equals(_existingProducts)) {
          return new RoomOpenHelper.ValidationResult(false, "products(com.grocery.billing.data.entity.Product).\n"
                  + " Expected:\n" + _infoProducts + "\n"
                  + " Found:\n" + _existingProducts);
        }
        final HashMap<String, TableInfo.Column> _columnsBills = new HashMap<String, TableInfo.Column>(8);
        _columnsBills.put("bill_id", new TableInfo.Column("bill_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("bill_number", new TableInfo.Column("bill_number", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("bill_date", new TableInfo.Column("bill_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("bill_time", new TableInfo.Column("bill_time", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("subtotal", new TableInfo.Column("subtotal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("discount", new TableInfo.Column("discount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("total", new TableInfo.Column("total", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBills.put("created_at", new TableInfo.Column("created_at", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBills = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBills = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBills = new TableInfo("bills", _columnsBills, _foreignKeysBills, _indicesBills);
        final TableInfo _existingBills = TableInfo.read(db, "bills");
        if (!_infoBills.equals(_existingBills)) {
          return new RoomOpenHelper.ValidationResult(false, "bills(com.grocery.billing.data.entity.Bill).\n"
                  + " Expected:\n" + _infoBills + "\n"
                  + " Found:\n" + _existingBills);
        }
        final HashMap<String, TableInfo.Column> _columnsBillItems = new HashMap<String, TableInfo.Column>(7);
        _columnsBillItems.put("bill_item_id", new TableInfo.Column("bill_item_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillItems.put("bill_id", new TableInfo.Column("bill_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillItems.put("product_id", new TableInfo.Column("product_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillItems.put("product_name_snapshot", new TableInfo.Column("product_name_snapshot", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillItems.put("quantity", new TableInfo.Column("quantity", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillItems.put("rate", new TableInfo.Column("rate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBillItems.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBillItems = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBillItems.add(new TableInfo.ForeignKey("bills", "CASCADE", "NO ACTION", Arrays.asList("bill_id"), Arrays.asList("bill_id")));
        final HashSet<TableInfo.Index> _indicesBillItems = new HashSet<TableInfo.Index>(1);
        _indicesBillItems.add(new TableInfo.Index("index_bill_items_bill_id", false, Arrays.asList("bill_id"), Arrays.asList("ASC")));
        final TableInfo _infoBillItems = new TableInfo("bill_items", _columnsBillItems, _foreignKeysBillItems, _indicesBillItems);
        final TableInfo _existingBillItems = TableInfo.read(db, "bill_items");
        if (!_infoBillItems.equals(_existingBillItems)) {
          return new RoomOpenHelper.ValidationResult(false, "bill_items(com.grocery.billing.data.entity.BillItem).\n"
                  + " Expected:\n" + _infoBillItems + "\n"
                  + " Found:\n" + _existingBillItems);
        }
        final HashMap<String, TableInfo.Column> _columnsSettings = new HashMap<String, TableInfo.Column>(2);
        _columnsSettings.put("key", new TableInfo.Column("key", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("value", new TableInfo.Column("value", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSettings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSettings = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSettings = new TableInfo("settings", _columnsSettings, _foreignKeysSettings, _indicesSettings);
        final TableInfo _existingSettings = TableInfo.read(db, "settings");
        if (!_infoSettings.equals(_existingSettings)) {
          return new RoomOpenHelper.ValidationResult(false, "settings(com.grocery.billing.data.entity.Setting).\n"
                  + " Expected:\n" + _infoSettings + "\n"
                  + " Found:\n" + _existingSettings);
        }
        final HashMap<String, TableInfo.Column> _columnsHeldBills = new HashMap<String, TableInfo.Column>(9);
        _columnsHeldBills.put("held_bill_id", new TableInfo.Column("held_bill_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHeldBills.put("reference", new TableInfo.Column("reference", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHeldBills.put("bill_number", new TableInfo.Column("bill_number", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHeldBills.put("bill_date", new TableInfo.Column("bill_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHeldBills.put("bill_time", new TableInfo.Column("bill_time", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHeldBills.put("subtotal", new TableInfo.Column("subtotal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHeldBills.put("discount", new TableInfo.Column("discount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHeldBills.put("total", new TableInfo.Column("total", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHeldBills.put("created_at", new TableInfo.Column("created_at", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHeldBills = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHeldBills = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHeldBills = new TableInfo("held_bills", _columnsHeldBills, _foreignKeysHeldBills, _indicesHeldBills);
        final TableInfo _existingHeldBills = TableInfo.read(db, "held_bills");
        if (!_infoHeldBills.equals(_existingHeldBills)) {
          return new RoomOpenHelper.ValidationResult(false, "held_bills(com.grocery.billing.data.entity.HeldBill).\n"
                  + " Expected:\n" + _infoHeldBills + "\n"
                  + " Found:\n" + _existingHeldBills);
        }
        final HashMap<String, TableInfo.Column> _columnsHeldBillItems = new HashMap<String, TableInfo.Column>(7);
        _columnsHeldBillItems.put("held_bill_item_id", new TableInfo.Column("held_bill_item_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHeldBillItems.put("held_bill_id", new TableInfo.Column("held_bill_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHeldBillItems.put("product_id", new TableInfo.Column("product_id", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHeldBillItems.put("product_name_snapshot", new TableInfo.Column("product_name_snapshot", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHeldBillItems.put("quantity", new TableInfo.Column("quantity", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHeldBillItems.put("rate", new TableInfo.Column("rate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHeldBillItems.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHeldBillItems = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysHeldBillItems.add(new TableInfo.ForeignKey("held_bills", "CASCADE", "NO ACTION", Arrays.asList("held_bill_id"), Arrays.asList("held_bill_id")));
        final HashSet<TableInfo.Index> _indicesHeldBillItems = new HashSet<TableInfo.Index>(1);
        _indicesHeldBillItems.add(new TableInfo.Index("index_held_bill_items_held_bill_id", false, Arrays.asList("held_bill_id"), Arrays.asList("ASC")));
        final TableInfo _infoHeldBillItems = new TableInfo("held_bill_items", _columnsHeldBillItems, _foreignKeysHeldBillItems, _indicesHeldBillItems);
        final TableInfo _existingHeldBillItems = TableInfo.read(db, "held_bill_items");
        if (!_infoHeldBillItems.equals(_existingHeldBillItems)) {
          return new RoomOpenHelper.ValidationResult(false, "held_bill_items(com.grocery.billing.data.entity.HeldBillItem).\n"
                  + " Expected:\n" + _infoHeldBillItems + "\n"
                  + " Found:\n" + _existingHeldBillItems);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "1caf8fb0f650dec1d2cff6fe2e2d161c", "d5f5304369b6b3eda6c7193a45d538b2");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "products","bills","bill_items","settings","held_bills","held_bill_items");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `products`");
      _db.execSQL("DELETE FROM `bills`");
      _db.execSQL("DELETE FROM `bill_items`");
      _db.execSQL("DELETE FROM `settings`");
      _db.execSQL("DELETE FROM `held_bills`");
      _db.execSQL("DELETE FROM `held_bill_items`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ProductDao.class, ProductDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BillDao.class, BillDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(BillItemDao.class, BillItemDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SettingsDao.class, SettingsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HeldBillDao.class, HeldBillDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(HeldBillItemDao.class, HeldBillItemDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ProductDao productDao() {
    if (_productDao != null) {
      return _productDao;
    } else {
      synchronized(this) {
        if(_productDao == null) {
          _productDao = new ProductDao_Impl(this);
        }
        return _productDao;
      }
    }
  }

  @Override
  public BillDao billDao() {
    if (_billDao != null) {
      return _billDao;
    } else {
      synchronized(this) {
        if(_billDao == null) {
          _billDao = new BillDao_Impl(this);
        }
        return _billDao;
      }
    }
  }

  @Override
  public BillItemDao billItemDao() {
    if (_billItemDao != null) {
      return _billItemDao;
    } else {
      synchronized(this) {
        if(_billItemDao == null) {
          _billItemDao = new BillItemDao_Impl(this);
        }
        return _billItemDao;
      }
    }
  }

  @Override
  public SettingsDao settingsDao() {
    if (_settingsDao != null) {
      return _settingsDao;
    } else {
      synchronized(this) {
        if(_settingsDao == null) {
          _settingsDao = new SettingsDao_Impl(this);
        }
        return _settingsDao;
      }
    }
  }

  @Override
  public HeldBillDao heldBillDao() {
    if (_heldBillDao != null) {
      return _heldBillDao;
    } else {
      synchronized(this) {
        if(_heldBillDao == null) {
          _heldBillDao = new HeldBillDao_Impl(this);
        }
        return _heldBillDao;
      }
    }
  }

  @Override
  public HeldBillItemDao heldBillItemDao() {
    if (_heldBillItemDao != null) {
      return _heldBillItemDao;
    } else {
      synchronized(this) {
        if(_heldBillItemDao == null) {
          _heldBillItemDao = new HeldBillItemDao_Impl(this);
        }
        return _heldBillItemDao;
      }
    }
  }
}
