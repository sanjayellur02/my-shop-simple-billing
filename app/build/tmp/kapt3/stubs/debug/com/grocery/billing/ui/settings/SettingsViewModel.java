package com.grocery.billing.ui.settings;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\f\u001a\u00020\rJ\u000e\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0011\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0012J\u000e\u0010\u0013\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0014\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0015\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0016\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0010J\u000e\u0010\u0017\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0012J\u000e\u0010\u0018\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\u0010J\u0006\u0010\u0019\u001a\u00020\rR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u001a"}, d2 = {"Lcom/grocery/billing/ui/settings/SettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "settingsRepository", "Lcom/grocery/billing/data/repository/SettingsRepository;", "(Lcom/grocery/billing/data/repository/SettingsRepository;)V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/grocery/billing/ui/settings/SettingsUiState;", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "clearSaved", "", "onAddressChange", "v", "", "onAllowPriceOverrideChange", "", "onCurrencyChange", "onCustomerThankYouChange", "onPhoneChange", "onShopNameChange", "onShowAddressChange", "onThankYouChange", "save", "app_debug"})
public final class SettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.grocery.billing.data.repository.SettingsRepository settingsRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.grocery.billing.ui.settings.SettingsUiState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.grocery.billing.ui.settings.SettingsUiState> state = null;
    
    public SettingsViewModel(@org.jetbrains.annotations.NotNull()
    com.grocery.billing.data.repository.SettingsRepository settingsRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.grocery.billing.ui.settings.SettingsUiState> getState() {
        return null;
    }
    
    public final void onShopNameChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onAddressChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onPhoneChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onCurrencyChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onThankYouChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onCustomerThankYouChange(@org.jetbrains.annotations.NotNull()
    java.lang.String v) {
    }
    
    public final void onShowAddressChange(boolean v) {
    }
    
    public final void onAllowPriceOverrideChange(boolean v) {
    }
    
    public final void save() {
    }
    
    public final void clearSaved() {
    }
}