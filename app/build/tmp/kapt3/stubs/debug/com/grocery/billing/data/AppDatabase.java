package com.grocery.billing.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\fH&J\b\u0010\r\u001a\u00020\u000eH&\u00a8\u0006\u0010"}, d2 = {"Lcom/grocery/billing/data/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "billDao", "Lcom/grocery/billing/data/dao/BillDao;", "billItemDao", "Lcom/grocery/billing/data/dao/BillItemDao;", "heldBillDao", "Lcom/grocery/billing/data/dao/HeldBillDao;", "heldBillItemDao", "Lcom/grocery/billing/data/dao/HeldBillItemDao;", "productDao", "Lcom/grocery/billing/data/dao/ProductDao;", "settingsDao", "Lcom/grocery/billing/data/dao/SettingsDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.grocery.billing.data.entity.Product.class, com.grocery.billing.data.entity.Bill.class, com.grocery.billing.data.entity.BillItem.class, com.grocery.billing.data.entity.Setting.class, com.grocery.billing.data.entity.HeldBill.class, com.grocery.billing.data.entity.HeldBillItem.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    @org.jetbrains.annotations.NotNull()
    private static final androidx.room.migration.Migration MIGRATION_1_2 = null;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.grocery.billing.data.AppDatabase instance;
    @org.jetbrains.annotations.NotNull()
    public static final com.grocery.billing.data.AppDatabase.Companion Companion = null;
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.grocery.billing.data.dao.ProductDao productDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.grocery.billing.data.dao.BillDao billDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.grocery.billing.data.dao.BillItemDao billItemDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.grocery.billing.data.dao.SettingsDao settingsDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.grocery.billing.data.dao.HeldBillDao heldBillDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.grocery.billing.data.dao.HeldBillItemDao heldBillItemDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/grocery/billing/data/AppDatabase$Companion;", "", "()V", "MIGRATION_1_2", "Landroidx/room/migration/Migration;", "getMIGRATION_1_2", "()Landroidx/room/migration/Migration;", "instance", "Lcom/grocery/billing/data/AppDatabase;", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.room.migration.Migration getMIGRATION_1_2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.grocery.billing.data.AppDatabase getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}