package com.grocery.billing.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000f\n\u0002\u0010\t\n\u0002\b\u0004\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0014J\u0012\u0010\u0016\u001a\u00020\u00042\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/grocery/billing/ui/Routes;", "", "()V", "BILLING", "", "BILL_COMPLETED", "BILL_COMPLETED_ARG", "BILL_DETAIL", "BILL_DETAIL_ARG", "BILL_REVIEW", "CSV_IMPORT", "HISTORY", "HOME", "PRODUCTS", "PRODUCT_EDIT", "PRODUCT_EDIT_ARG", "SETTINGS", "WAITING_CUSTOMERS", "billCompleted", "billId", "", "billDetail", "productEdit", "id", "app_debug"})
public final class Routes {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String HOME = "home";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PRODUCTS = "products";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PRODUCT_EDIT = "product_edit?productId={productId}";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PRODUCT_EDIT_ARG = "productId";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CSV_IMPORT = "csv_import";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BILLING = "billing";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BILL_REVIEW = "bill_review";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BILL_COMPLETED = "bill_completed/{billId}";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BILL_COMPLETED_ARG = "billId";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String HISTORY = "history";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BILL_DETAIL = "bill_detail/{billId}";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BILL_DETAIL_ARG = "billId";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SETTINGS = "settings";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String WAITING_CUSTOMERS = "waiting_customers";
    @org.jetbrains.annotations.NotNull()
    public static final com.grocery.billing.ui.Routes INSTANCE = null;
    
    private Routes() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String productEdit(@org.jetbrains.annotations.Nullable()
    java.lang.String id) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String billCompleted(long billId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String billDetail(long billId) {
        return null;
    }
}