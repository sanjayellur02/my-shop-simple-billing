package com.grocery.billing.ui.billing;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\"\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u001e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a\u001e\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001a\u0018\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002\u00a8\u0006\r"}, d2 = {"ShareActionButton", "", "text", "", "onClick", "Lkotlin/Function0;", "ShareBillDialog", "onDismiss", "queryContactNumber", "context", "Landroid/content/Context;", "uri", "Landroid/net/Uri;", "app_debug"})
public final class ShareBillDialogKt {
    
    /**
     * Share dialog: enter / select a mobile number, then choose WhatsApp, SMS
     * or the Android share sheet. Nothing is sent without the user's action.
     */
    @androidx.compose.runtime.Composable()
    public static final void ShareBillDialog(@org.jetbrains.annotations.NotNull()
    java.lang.String text, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ShareActionButton(java.lang.String text, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    private static final java.lang.String queryContactNumber(android.content.Context context, android.net.Uri uri) {
        return null;
    }
}