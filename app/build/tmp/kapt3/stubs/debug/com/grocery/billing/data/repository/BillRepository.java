package com.grocery.billing.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\b2\u0006\u0010\u000f\u001a\u00020\u0010J\u001a\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b2\u0006\u0010\u0012\u001a\u00020\u0013J\u0018\u0010\u0014\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0015J\u0018\u0010\u0016\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000f\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0015J\u001a\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\t0\b2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0019\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u001aJP\u0010\u001b\u001a\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u00132\b\b\u0002\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010\u001d\u001a\u00020\u00132\f\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\t2\u0006\u0010 \u001a\u00020\u00102\u0006\u0010!\u001a\u00020\u00102\u0006\u0010\"\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010#J\u001a\u0010$\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b2\u0006\u0010%\u001a\u00020\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006&"}, d2 = {"Lcom/grocery/billing/data/repository/BillRepository;", "", "billDao", "Lcom/grocery/billing/data/dao/BillDao;", "billItemDao", "Lcom/grocery/billing/data/dao/BillItemDao;", "(Lcom/grocery/billing/data/dao/BillDao;Lcom/grocery/billing/data/dao/BillItemDao;)V", "bills", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/grocery/billing/data/entity/Bill;", "getBills", "()Lkotlinx/coroutines/flow/Flow;", "billWithItemsFlow", "Lcom/grocery/billing/data/model/BillWithItems;", "billId", "", "billsByDate", "date", "", "getBillWithItems", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getById", "itemsForBill", "Lcom/grocery/billing/data/entity/BillItem;", "nextBillNumber", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveBill", "billNumber", "time", "items", "Lcom/grocery/billing/data/repository/BillItemDraft;", "subtotalPaise", "discountPaise", "totalPaise", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;JJJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "search", "q", "app_debug"})
public final class BillRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.grocery.billing.data.dao.BillDao billDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.grocery.billing.data.dao.BillItemDao billItemDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.grocery.billing.data.entity.Bill>> bills = null;
    
    public BillRepository(@org.jetbrains.annotations.NotNull()
    com.grocery.billing.data.dao.BillDao billDao, @org.jetbrains.annotations.NotNull()
    com.grocery.billing.data.dao.BillItemDao billItemDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.grocery.billing.data.entity.Bill>> getBills() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.grocery.billing.data.entity.Bill>> billsByDate(@org.jetbrains.annotations.NotNull()
    java.lang.String date) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.grocery.billing.data.entity.Bill>> search(@org.jetbrains.annotations.NotNull()
    java.lang.String q) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getById(long billId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.grocery.billing.data.entity.Bill> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.grocery.billing.data.entity.BillItem>> itemsForBill(long billId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.grocery.billing.data.model.BillWithItems> billWithItemsFlow(long billId) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getBillWithItems(long billId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.grocery.billing.data.model.BillWithItems> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object nextBillNumber(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    /**
     * Persists a completed bill. Returns the new bill id.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveBill(@org.jetbrains.annotations.NotNull()
    java.lang.String billNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    java.lang.String time, @org.jetbrains.annotations.NotNull()
    java.util.List<com.grocery.billing.data.repository.BillItemDraft> items, long subtotalPaise, long discountPaise, long totalPaise, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
}