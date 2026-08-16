package com.grocery.billing.data.entity;

/**
 * Bill items snapshot the product name at billing time so historical bills
 * keep the original name even if the product is renamed or deleted later.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001BA\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0006\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0017\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003JQ\u0010\u001c\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010 \u001a\u00020!H\u00d6\u0001J\t\u0010\"\u001a\u00020\u0006H\u00d6\u0001R\u0016\u0010\n\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0016\u0010\u0004\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u0018\u0010\u0005\u001a\u0004\u0018\u00010\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0016\u0010\u0007\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0016\u0010\b\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u0016\u0010\t\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\r\u00a8\u0006#"}, d2 = {"Lcom/grocery/billing/data/entity/BillItem;", "", "billItemId", "", "billId", "productId", "", "productNameSnapshot", "quantity", "ratePaise", "amountPaise", "(JJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;JJ)V", "getAmountPaise", "()J", "getBillId", "getBillItemId", "getProductId", "()Ljava/lang/String;", "getProductNameSnapshot", "getQuantity", "getRatePaise", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
@androidx.room.Entity(tableName = "bill_items", foreignKeys = {@androidx.room.ForeignKey(entity = com.grocery.billing.data.entity.Bill.class, parentColumns = {"bill_id"}, childColumns = {"bill_id"}, onDelete = 5)}, indices = {@androidx.room.Index(value = {"bill_id"})})
public final class BillItem {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @androidx.room.ColumnInfo(name = "bill_item_id")
    private final long billItemId = 0L;
    @androidx.room.ColumnInfo(name = "bill_id")
    private final long billId = 0L;
    @androidx.room.ColumnInfo(name = "product_id")
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String productId = null;
    @androidx.room.ColumnInfo(name = "product_name_snapshot")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String productNameSnapshot = null;
    @androidx.room.ColumnInfo(name = "quantity")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String quantity = null;
    @androidx.room.ColumnInfo(name = "rate")
    private final long ratePaise = 0L;
    @androidx.room.ColumnInfo(name = "amount")
    private final long amountPaise = 0L;
    
    public BillItem(long billItemId, long billId, @org.jetbrains.annotations.Nullable()
    java.lang.String productId, @org.jetbrains.annotations.NotNull()
    java.lang.String productNameSnapshot, @org.jetbrains.annotations.NotNull()
    java.lang.String quantity, long ratePaise, long amountPaise) {
        super();
    }
    
    public final long getBillItemId() {
        return 0L;
    }
    
    public final long getBillId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getProductId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getProductNameSnapshot() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getQuantity() {
        return null;
    }
    
    public final long getRatePaise() {
        return 0L;
    }
    
    public final long getAmountPaise() {
        return 0L;
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final long component2() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    public final long component6() {
        return 0L;
    }
    
    public final long component7() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.grocery.billing.data.entity.BillItem copy(long billItemId, long billId, @org.jetbrains.annotations.Nullable()
    java.lang.String productId, @org.jetbrains.annotations.NotNull()
    java.lang.String productNameSnapshot, @org.jetbrains.annotations.NotNull()
    java.lang.String quantity, long ratePaise, long amountPaise) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}