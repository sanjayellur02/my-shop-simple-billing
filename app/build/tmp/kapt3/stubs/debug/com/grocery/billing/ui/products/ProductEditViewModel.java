package com.grocery.billing.ui.products;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000bJ\u000e\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000bJ\u000e\u0010\u0014\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000bJ\u000e\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000bJ\u000e\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000bJ\u0006\u0010\u0017\u001a\u00020\u0011R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\t0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0018"}, d2 = {"Lcom/grocery/billing/ui/products/ProductEditViewModel;", "Landroidx/lifecycle/ViewModel;", "productRepository", "Lcom/grocery/billing/data/repository/ProductRepository;", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "(Lcom/grocery/billing/data/repository/ProductRepository;Landroidx/lifecycle/SavedStateHandle;)V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/grocery/billing/ui/products/ProductEditUiState;", "productId", "", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "onBarcodeChange", "", "value", "onIdChange", "onNameChange", "onPriceChange", "onUnitChange", "save", "app_debug"})
public final class ProductEditViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.grocery.billing.data.repository.ProductRepository productRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.grocery.billing.ui.products.ProductEditUiState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.grocery.billing.ui.products.ProductEditUiState> state = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String productId = null;
    
    public ProductEditViewModel(@org.jetbrains.annotations.NotNull()
    com.grocery.billing.data.repository.ProductRepository productRepository, @org.jetbrains.annotations.NotNull()
    androidx.lifecycle.SavedStateHandle savedStateHandle) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.grocery.billing.ui.products.ProductEditUiState> getState() {
        return null;
    }
    
    public final void onIdChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void onNameChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void onPriceChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void onUnitChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void onBarcodeChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void save() {
    }
}