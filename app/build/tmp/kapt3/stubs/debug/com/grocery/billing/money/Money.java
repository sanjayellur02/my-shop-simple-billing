package com.grocery.billing.money;

/**
 * All money values in the app are stored as integer paise (1 rupee = 100 paise)
 * to avoid floating point currency errors.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006J\u000e\u0010\b\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u0006J\u000e\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0004J\u000e\u0010\f\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0004J\u0015\u0010\r\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u000e\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u000fR\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/grocery/billing/money/Money;", "", "()V", "PAISE_PER_RUPEE", "", "formatQuantity", "", "raw", "indianGrouping", "digits", "paiseToDisplay", "paise", "paiseToNumber", "parseRupeesToPaise", "input", "(Ljava/lang/String;)Ljava/lang/Long;", "app_debug"})
public final class Money {
    public static final long PAISE_PER_RUPEE = 100L;
    @org.jetbrains.annotations.NotNull()
    public static final com.grocery.billing.money.Money INSTANCE = null;
    
    private Money() {
        super();
    }
    
    /**
     * Parses a user-typed rupee amount ("50.50", "1,000") into paise. Returns null if invalid.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long parseRupeesToPaise(@org.jetbrains.annotations.NotNull()
    java.lang.String input) {
        return null;
    }
    
    /**
     * Formats paise as "₹12,450" / "₹50.50". Trailing ".00" is hidden.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String paiseToDisplay(long paise) {
        return null;
    }
    
    /**
     * Formats paise as "12,450" / "50.50" (no currency symbol).
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String paiseToNumber(long paise) {
        return null;
    }
    
    /**
     * Applies Indian digit grouping (12,34,567) to a digit-only string.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String indianGrouping(@org.jetbrains.annotations.NotNull()
    java.lang.String digits) {
        return null;
    }
    
    /**
     * Normalizes a user-typed quantity ("2.50" -> "2.5", "2.00" -> "2").
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatQuantity(@org.jetbrains.annotations.NotNull()
    java.lang.String raw) {
        return null;
    }
}