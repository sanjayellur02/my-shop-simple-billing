package com.grocery.billing.util;

/**
 * Sequential bill number helpers. Numbers never reset daily.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u0014\u0010\u0007\u001a\u00020\u00062\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\t\u00a8\u0006\n"}, d2 = {"Lcom/grocery/billing/util/BillNumbers;", "", "()V", "format", "", "number", "", "nextNumber", "existingNumbers", "", "app_debug"})
public final class BillNumbers {
    @org.jetbrains.annotations.NotNull()
    public static final com.grocery.billing.util.BillNumbers INSTANCE = null;
    
    private BillNumbers() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String format(long number) {
        return null;
    }
    
    /**
     * Computes the next sequential number from the numbers already stored.
     */
    public final long nextNumber(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> existingNumbers) {
        return 0L;
    }
}