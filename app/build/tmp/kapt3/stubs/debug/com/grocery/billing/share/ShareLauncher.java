package com.grocery.billing.share;

/**
 * Launches WhatsApp / SMS / system share. Never sends anything silently -
 * the user always presses Send in the target app.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0006J\u001e\u0010\f\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006J\u001e\u0010\u000e\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006\u00a8\u0006\u000f"}, d2 = {"Lcom/grocery/billing/share/ShareLauncher;", "", "()V", "isValidPhoneNumber", "", "input", "", "openShareSheet", "", "context", "Landroid/content/Context;", "text", "openSms", "phoneNumber", "openWhatsApp", "app_debug"})
public final class ShareLauncher {
    @org.jetbrains.annotations.NotNull()
    public static final com.grocery.billing.share.ShareLauncher INSTANCE = null;
    
    private ShareLauncher() {
        super();
    }
    
    /**
     * Tries the WhatsApp app with the number and message; falls back to wa.me.
     */
    public final void openWhatsApp(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    public final void openSms(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String phoneNumber, @org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    public final void openShareSheet(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.lang.String text) {
    }
    
    public final boolean isValidPhoneNumber(@org.jetbrains.annotations.NotNull()
    java.lang.String input) {
        return false;
    }
}