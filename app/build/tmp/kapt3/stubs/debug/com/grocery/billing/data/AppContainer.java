package com.grocery.billing.data;

/**
 * Simple manual dependency container.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006\u0017"}, d2 = {"Lcom/grocery/billing/data/AppContainer;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "billRepository", "Lcom/grocery/billing/data/repository/BillRepository;", "getBillRepository", "()Lcom/grocery/billing/data/repository/BillRepository;", "database", "Lcom/grocery/billing/data/AppDatabase;", "heldBillRepository", "Lcom/grocery/billing/data/repository/HeldBillRepository;", "getHeldBillRepository", "()Lcom/grocery/billing/data/repository/HeldBillRepository;", "productRepository", "Lcom/grocery/billing/data/repository/ProductRepository;", "getProductRepository", "()Lcom/grocery/billing/data/repository/ProductRepository;", "settingsRepository", "Lcom/grocery/billing/data/repository/SettingsRepository;", "getSettingsRepository", "()Lcom/grocery/billing/data/repository/SettingsRepository;", "app_debug"})
public final class AppContainer {
    @org.jetbrains.annotations.NotNull()
    private final com.grocery.billing.data.AppDatabase database = null;
    @org.jetbrains.annotations.NotNull()
    private final com.grocery.billing.data.repository.ProductRepository productRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.grocery.billing.data.repository.BillRepository billRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.grocery.billing.data.repository.HeldBillRepository heldBillRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.grocery.billing.data.repository.SettingsRepository settingsRepository = null;
    
    public AppContainer(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.grocery.billing.data.repository.ProductRepository getProductRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.grocery.billing.data.repository.BillRepository getBillRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.grocery.billing.data.repository.HeldBillRepository getHeldBillRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.grocery.billing.data.repository.SettingsRepository getSettingsRepository() {
        return null;
    }
}