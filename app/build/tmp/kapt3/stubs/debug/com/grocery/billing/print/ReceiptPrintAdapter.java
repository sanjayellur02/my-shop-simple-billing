package com.grocery.billing.print;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J4\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0016J5\u0010\u0012\u001a\u00020\b2\u000e\u0010\u0013\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00150\u00142\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0018H\u0016\u00a2\u0006\u0002\u0010\u0019J\b\u0010\u001a\u001a\u00020\u001bH\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/grocery/billing/print/ReceiptPrintAdapter;", "Landroid/print/PrintDocumentAdapter;", "context", "Landroid/content/Context;", "view", "Landroid/view/View;", "(Landroid/content/Context;Landroid/view/View;)V", "onLayout", "", "oldAttributes", "Landroid/print/PrintAttributes;", "newAttributes", "cancellationSignal", "Landroid/os/CancellationSignal;", "callback", "Landroid/print/PrintDocumentAdapter$LayoutResultCallback;", "extras", "Landroid/os/Bundle;", "onWrite", "pages", "", "Landroid/print/PageRange;", "destination", "Landroid/os/ParcelFileDescriptor;", "Landroid/print/PrintDocumentAdapter$WriteResultCallback;", "([Landroid/print/PageRange;Landroid/os/ParcelFileDescriptor;Landroid/os/CancellationSignal;Landroid/print/PrintDocumentAdapter$WriteResultCallback;)V", "render", "Landroid/graphics/Bitmap;", "app_debug"})
final class ReceiptPrintAdapter extends android.print.PrintDocumentAdapter {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final android.view.View view = null;
    
    public ReceiptPrintAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    android.view.View view) {
        super();
    }
    
    @java.lang.Override()
    public void onLayout(@org.jetbrains.annotations.Nullable()
    android.print.PrintAttributes oldAttributes, @org.jetbrains.annotations.NotNull()
    android.print.PrintAttributes newAttributes, @org.jetbrains.annotations.NotNull()
    android.os.CancellationSignal cancellationSignal, @org.jetbrains.annotations.NotNull()
    android.print.PrintDocumentAdapter.LayoutResultCallback callback, @org.jetbrains.annotations.Nullable()
    android.os.Bundle extras) {
    }
    
    @java.lang.Override()
    public void onWrite(@org.jetbrains.annotations.NotNull()
    android.print.PageRange[] pages, @org.jetbrains.annotations.NotNull()
    android.os.ParcelFileDescriptor destination, @org.jetbrains.annotations.NotNull()
    android.os.CancellationSignal cancellationSignal, @org.jetbrains.annotations.NotNull()
    android.print.PrintDocumentAdapter.WriteResultCallback callback) {
    }
    
    private final android.graphics.Bitmap render() {
        return null;
    }
}