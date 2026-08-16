package com.grocery.billing.data.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u000e\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0005J\u0018\u0010\n\u001a\u0004\u0018\u00010\t2\u0006\u0010\u000b\u001a\u00020\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u0016\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\u0010J\u0014\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00030\u0012H\'J\u001c\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00030\u00122\u0006\u0010\u0014\u001a\u00020\u0004H\'J\u0018\u0010\u0015\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u00122\u0006\u0010\u000b\u001a\u00020\fH\'J\u001c\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00030\u00122\u0006\u0010\u0017\u001a\u00020\u0004H\'\u00a8\u0006\u0018"}, d2 = {"Lcom/grocery/billing/data/dao/BillDao;", "", "allBillNumbers", "", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAll", "", "getAll", "Lcom/grocery/billing/data/entity/Bill;", "getById", "billId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "bill", "(Lcom/grocery/billing/data/entity/Bill;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "observeAll", "Lkotlinx/coroutines/flow/Flow;", "observeByDate", "date", "observeById", "search", "q", "app_debug"})
@androidx.room.Dao()
public abstract interface BillDao {
    
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.grocery.billing.data.entity.Bill bill, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM bills ORDER BY bill_id DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.grocery.billing.data.entity.Bill>> observeAll();
    
    @androidx.room.Query(value = "SELECT * FROM bills WHERE bill_id = :billId LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getById(long billId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.grocery.billing.data.entity.Bill> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM bills WHERE bill_id = :billId LIMIT 1")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<com.grocery.billing.data.entity.Bill> observeById(long billId);
    
    @androidx.room.Query(value = "SELECT * FROM bills WHERE bill_date = :date ORDER BY bill_id DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.grocery.billing.data.entity.Bill>> observeByDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date);
    
    @androidx.room.Query(value = "SELECT bill_number FROM bills")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object allBillNumbers(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM bills ORDER BY bill_id ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.grocery.billing.data.entity.Bill>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM bills WHERE bill_number LIKE \'%\' || :q || \'%\' OR bill_date LIKE \'%\' || :q || \'%\' ORDER BY bill_id DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.grocery.billing.data.entity.Bill>> search(@org.jetbrains.annotations.NotNull()
    java.lang.String q);
    
    @androidx.room.Query(value = "DELETE FROM bills")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}