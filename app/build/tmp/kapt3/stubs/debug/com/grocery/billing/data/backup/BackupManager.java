package com.grocery.billing.data.backup;

/**
 * Backs up the entire app (settings, products, bills, bill items, held bills)
 * as a JSON file.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bJ\u0018\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0002J\u0018\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\r2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0002J\u0018\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\r2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0002J\u0018\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\r2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0002J\u0018\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\r2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0002J\u001e\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001cH\u0002J\u0016\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0086@\u00a2\u0006\u0002\u0010!J\u0016\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\'"}, d2 = {"Lcom/grocery/billing/data/backup/BackupManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "database", "Lcom/grocery/billing/data/AppDatabase;", "settingsRepository", "Lcom/grocery/billing/data/repository/SettingsRepository;", "buildBackupJson", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "parseBillItems", "", "Lcom/grocery/billing/data/entity/BillItem;", "arr", "Lorg/json/JSONArray;", "parseBills", "Lcom/grocery/billing/data/entity/Bill;", "parseHeldBillItems", "Lcom/grocery/billing/data/entity/HeldBillItem;", "parseHeldBills", "Lcom/grocery/billing/data/entity/HeldBill;", "parseProducts", "Lcom/grocery/billing/data/entity/Product;", "parseSettings", "", "obj", "Lorg/json/JSONObject;", "restoreFrom", "Lcom/grocery/billing/data/backup/RestoreResult;", "reader", "Ljava/io/Reader;", "(Ljava/io/Reader;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "writeJsonTo", "", "writer", "Ljava/io/Writer;", "json", "app_debug"})
public final class BackupManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.grocery.billing.data.AppDatabase database = null;
    @org.jetbrains.annotations.NotNull()
    private final com.grocery.billing.data.repository.SettingsRepository settingsRepository = null;
    
    public BackupManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object buildBackupJson(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
    
    public final void writeJsonTo(@org.jetbrains.annotations.NotNull()
    java.io.Writer writer, @org.jetbrains.annotations.NotNull()
    java.lang.String json) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object restoreFrom(@org.jetbrains.annotations.NotNull()
    java.io.Reader reader, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.grocery.billing.data.backup.RestoreResult> $completion) {
        return null;
    }
    
    private final java.util.List<com.grocery.billing.data.entity.Product> parseProducts(org.json.JSONArray arr) {
        return null;
    }
    
    private final java.util.List<com.grocery.billing.data.entity.Bill> parseBills(org.json.JSONArray arr) {
        return null;
    }
    
    private final java.util.List<com.grocery.billing.data.entity.BillItem> parseBillItems(org.json.JSONArray arr) {
        return null;
    }
    
    private final java.util.Map<java.lang.String, java.lang.String> parseSettings(org.json.JSONObject obj) {
        return null;
    }
    
    private final java.util.List<com.grocery.billing.data.entity.HeldBill> parseHeldBills(org.json.JSONArray arr) {
        return null;
    }
    
    private final java.util.List<com.grocery.billing.data.entity.HeldBillItem> parseHeldBillItems(org.json.JSONArray arr) {
        return null;
    }
}