package com.grocery.billing.ui.waiting;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ\u0016\u0010\u000f\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0012"}, d2 = {"Lcom/grocery/billing/ui/waiting/WaitingCustomersViewModel;", "Landroidx/lifecycle/ViewModel;", "heldBillRepository", "Lcom/grocery/billing/data/repository/HeldBillRepository;", "(Lcom/grocery/billing/data/repository/HeldBillRepository;)V", "heldBills", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/grocery/billing/data/dao/HeldBillWithCount;", "getHeldBills", "()Lkotlinx/coroutines/flow/StateFlow;", "delete", "", "id", "", "updateReference", "reference", "", "app_debug"})
public final class WaitingCustomersViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.grocery.billing.data.repository.HeldBillRepository heldBillRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.grocery.billing.data.dao.HeldBillWithCount>> heldBills = null;
    
    public WaitingCustomersViewModel(@org.jetbrains.annotations.NotNull()
    com.grocery.billing.data.repository.HeldBillRepository heldBillRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.grocery.billing.data.dao.HeldBillWithCount>> getHeldBills() {
        return null;
    }
    
    public final void delete(long id) {
    }
    
    public final void updateReference(long id, @org.jetbrains.annotations.NotNull()
    java.lang.String reference) {
    }
}