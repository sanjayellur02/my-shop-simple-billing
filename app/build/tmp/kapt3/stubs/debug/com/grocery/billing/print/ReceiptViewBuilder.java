package com.grocery.billing.print;

/**
 * Builds a clean receipt layout as a classic Android View hierarchy.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002Jv\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\b2\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\bH\u0007\u00a8\u0006\u0018"}, d2 = {"Lcom/grocery/billing/print/ReceiptViewBuilder;", "", "()V", "build", "Landroid/view/View;", "context", "Landroid/content/Context;", "shopName", "", "address", "phone", "showAddress", "", "billNumber", "date", "time", "items", "", "Lcom/grocery/billing/print/BillLine;", "subtotalPaise", "", "discountPaise", "totalPaise", "thankYou", "app_debug"})
public final class ReceiptViewBuilder {
    @org.jetbrains.annotations.NotNull()
    public static final com.grocery.billing.print.ReceiptViewBuilder INSTANCE = null;
    
    private ReceiptViewBuilder() {
        super();
    }
    
    @android.annotation.SuppressLint(value = {"SetTextI18n"})
    @org.jetbrains.annotations.NotNull()
    public final android.view.View build(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String shopName, @org.jetbrains.annotations.NotNull()
    java.lang.String address, @org.jetbrains.annotations.NotNull()
    java.lang.String phone, boolean showAddress, @org.jetbrains.annotations.NotNull()
    java.lang.String billNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    java.lang.String time, @org.jetbrains.annotations.NotNull()
    java.util.List<com.grocery.billing.print.BillLine> items, long subtotalPaise, long discountPaise, long totalPaise, @org.jetbrains.annotations.NotNull()
    java.lang.String thankYou) {
        return null;
    }
}