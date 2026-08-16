package com.grocery.billing.billing;

/**
 * Pure billing calculations. All money is in paise (Long).
 * Quantity is a decimal string; the rate is in paise.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010 \n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0004J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u000e\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\u0004J\u000e\u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\nJ\u0016\u0010\u000f\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\u0004J\u0014\u0010\u0005\u001a\u00020\u00042\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00040\u0011\u00a8\u0006\u0012"}, d2 = {"Lcom/grocery/billing/billing/BillCalculator;", "", "()V", "grandTotalPaise", "", "subtotalPaise", "discountPaise", "isValidQuantity", "", "quantity", "", "isValidRatePaise", "ratePaise", "isValidRateText", "rate", "itemAmountPaise", "amountsPaise", "", "app_debug"})
public final class BillCalculator {
    @org.jetbrains.annotations.NotNull()
    public static final com.grocery.billing.billing.BillCalculator INSTANCE = null;
    
    private BillCalculator() {
        super();
    }
    
    /**
     * amount = quantity * rate, rounded to the nearest paise.
     */
    public final long itemAmountPaise(@org.jetbrains.annotations.NotNull()
    java.lang.String quantity, long ratePaise) {
        return 0L;
    }
    
    public final long subtotalPaise(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.Long> amountsPaise) {
        return 0L;
    }
    
    /**
     * Grand total cannot go below zero.
     */
    public final long grandTotalPaise(long subtotalPaise, long discountPaise) {
        return 0L;
    }
    
    public final boolean isValidQuantity(@org.jetbrains.annotations.NotNull()
    java.lang.String quantity) {
        return false;
    }
    
    public final boolean isValidRatePaise(long ratePaise) {
        return false;
    }
    
    public final boolean isValidRateText(@org.jetbrains.annotations.NotNull()
    java.lang.String rate) {
        return false;
    }
}