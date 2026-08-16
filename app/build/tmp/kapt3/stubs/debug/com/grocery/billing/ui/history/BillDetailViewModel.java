package com.grocery.billing.ui.history;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006R\u0012\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\tR\u0019\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u000f"}, d2 = {"Lcom/grocery/billing/ui/history/BillDetailViewModel;", "Landroidx/lifecycle/ViewModel;", "billRepository", "Lcom/grocery/billing/data/repository/BillRepository;", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "(Lcom/grocery/billing/data/repository/BillRepository;Landroidx/lifecycle/SavedStateHandle;)V", "billId", "", "Ljava/lang/Long;", "billWithItems", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/grocery/billing/data/model/BillWithItems;", "getBillWithItems", "()Lkotlinx/coroutines/flow/StateFlow;", "app_debug"})
public final class BillDetailViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long billId = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.grocery.billing.data.model.BillWithItems> billWithItems = null;
    
    public BillDetailViewModel(@org.jetbrains.annotations.NotNull()
    com.grocery.billing.data.repository.BillRepository billRepository, @org.jetbrains.annotations.NotNull()
    androidx.lifecycle.SavedStateHandle savedStateHandle) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.grocery.billing.data.model.BillWithItems> getBillWithItems() {
        return null;
    }
}