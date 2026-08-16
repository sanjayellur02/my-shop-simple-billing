package com.grocery.billing.ui.billing;

/**
 * Holds the in-progress (draft) bill plus the inline product picker state.
 * Scoped to the activity so the draft and picker survive configuration
 * changes and navigation.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0019\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ(\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u001a\u001a\u00020\u00192\u0006\u0010\u001b\u001a\u00020\u00192\u0006\u0010\u001c\u001a\u00020\u000fJ\u0006\u0010\u001d\u001a\u00020\u001eJ\u000e\u0010\u001f\u001a\u00020\u00172\u0006\u0010 \u001a\u00020\u000fJ\u0016\u0010!\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020\u000f2\u0006\u0010 \u001a\u00020\u000fJ\u0006\u0010#\u001a\u00020\u0017J\u0006\u0010$\u001a\u00020\u0017J\u0016\u0010%\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010\'J\u0006\u0010(\u001a\u00020\u0017J\u0006\u0010)\u001a\u00020\u0017J\u000e\u0010*\u001a\u00020\u00172\u0006\u0010+\u001a\u00020\u0019J\u000e\u0010,\u001a\u00020\u00172\u0006\u0010-\u001a\u00020\u0019J\u000e\u0010.\u001a\u00020\u00172\u0006\u0010-\u001a\u00020\u0019J\u000e\u0010/\u001a\u00020\u00172\u0006\u00100\u001a\u00020\u0019J\u0010\u00101\u001a\u00020\r2\u0006\u00102\u001a\u00020\rH\u0002J\u000e\u00103\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020\u000fJ\u0016\u00104\u001a\u00020\u001e2\u0006\u0010&\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010\'J\u0006\u00105\u001a\u00020\u0017J\u000e\u00106\u001a\u00020\u00172\u0006\u00107\u001a\u000208J\u000e\u00109\u001a\u00020\u00172\u0006\u0010:\u001a\u00020\u0019J\u0006\u0010;\u001a\u00020\u0017J\u0006\u0010<\u001a\u00020\u0017J\u001e\u0010=\u001a\u00020\u00172\u0006\u0010\"\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u00192\u0006\u0010\u001c\u001a\u00020\u000fR\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\r0\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006>"}, d2 = {"Lcom/grocery/billing/ui/billing/BillingViewModel;", "Landroidx/lifecycle/ViewModel;", "productRepository", "Lcom/grocery/billing/data/repository/ProductRepository;", "billRepository", "Lcom/grocery/billing/data/repository/BillRepository;", "heldBillRepository", "Lcom/grocery/billing/data/repository/HeldBillRepository;", "settingsRepository", "Lcom/grocery/billing/data/repository/SettingsRepository;", "(Lcom/grocery/billing/data/repository/ProductRepository;Lcom/grocery/billing/data/repository/BillRepository;Lcom/grocery/billing/data/repository/HeldBillRepository;Lcom/grocery/billing/data/repository/SettingsRepository;)V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/grocery/billing/ui/billing/BillingUiState;", "itemKey", "", "searchJob", "Lkotlinx/coroutines/Job;", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "addItem", "", "productId", "", "productName", "quantity", "ratePaise", "addSelectedToBill", "", "adjustPickerQuantity", "delta", "adjustQuantity", "key", "clearSearch", "consumeSaved", "deleteHeld", "id", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "discardBill", "ensureStarted", "holdBill", "reference", "onQuantityChange", "value", "onRateChange", "onSearchQueryChange", "query", "recalc", "s", "removeItem", "resumeHeld", "saveBill", "selectProduct", "product", "Lcom/grocery/billing/data/entity/Product;", "setDiscountText", "text", "startNewBill", "toggleRateEditor", "updateItem", "app_debug"})
public final class BillingViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.grocery.billing.data.repository.ProductRepository productRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.grocery.billing.data.repository.BillRepository billRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.grocery.billing.data.repository.HeldBillRepository heldBillRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.grocery.billing.data.repository.SettingsRepository settingsRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.grocery.billing.ui.billing.BillingUiState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.grocery.billing.ui.billing.BillingUiState> state = null;
    private long itemKey = 0L;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job searchJob;
    
    public BillingViewModel(@org.jetbrains.annotations.NotNull()
    com.grocery.billing.data.repository.ProductRepository productRepository, @org.jetbrains.annotations.NotNull()
    com.grocery.billing.data.repository.BillRepository billRepository, @org.jetbrains.annotations.NotNull()
    com.grocery.billing.data.repository.HeldBillRepository heldBillRepository, @org.jetbrains.annotations.NotNull()
    com.grocery.billing.data.repository.SettingsRepository settingsRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.grocery.billing.ui.billing.BillingUiState> getState() {
        return null;
    }
    
    public final void ensureStarted() {
    }
    
    public final void startNewBill() {
    }
    
    public final void discardBill() {
    }
    
    private final com.grocery.billing.ui.billing.BillingUiState recalc(com.grocery.billing.ui.billing.BillingUiState s) {
        return null;
    }
    
    public final void onSearchQueryChange(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void clearSearch() {
    }
    
    public final void selectProduct(@org.jetbrains.annotations.NotNull()
    com.grocery.billing.data.entity.Product product) {
    }
    
    public final void onQuantityChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void onRateChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void adjustPickerQuantity(long delta) {
    }
    
    public final void toggleRateEditor() {
    }
    
    public final boolean addSelectedToBill() {
        return false;
    }
    
    public final void addItem(@org.jetbrains.annotations.Nullable()
    java.lang.String productId, @org.jetbrains.annotations.NotNull()
    java.lang.String productName, @org.jetbrains.annotations.NotNull()
    java.lang.String quantity, long ratePaise) {
    }
    
    public final void updateItem(long key, @org.jetbrains.annotations.NotNull()
    java.lang.String quantity, long ratePaise) {
    }
    
    public final void adjustQuantity(long key, long delta) {
    }
    
    public final void removeItem(long key) {
    }
    
    public final void setDiscountText(@org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    public final void holdBill(@org.jetbrains.annotations.NotNull()
    java.lang.String reference) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object resumeHeld(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteHeld(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    public final void saveBill() {
    }
    
    public final void consumeSaved() {
    }
}