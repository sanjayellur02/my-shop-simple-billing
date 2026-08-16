package com.grocery.billing.util;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\fJ\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\nJ\u0010\u0010\u000f\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\fJ\u0010\u0010\u0010\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\fR\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006\u00a8\u0006\u0011"}, d2 = {"Lcom/grocery/billing/util/Dates;", "", "()V", "DATE_FORMAT", "Ljava/time/format/DateTimeFormatter;", "getDATE_FORMAT", "()Ljava/time/format/DateTimeFormatter;", "TIME_FORMAT", "getTIME_FORMAT", "isoTimestamp", "", "now", "Ljava/time/LocalDateTime;", "shortDate", "dayMonthYear", "timeString", "todayDateString", "app_debug"})
public final class Dates {
    @org.jetbrains.annotations.NotNull()
    private static final java.time.format.DateTimeFormatter DATE_FORMAT = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.time.format.DateTimeFormatter TIME_FORMAT = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.grocery.billing.util.Dates INSTANCE = null;
    
    private Dates() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.format.DateTimeFormatter getDATE_FORMAT() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.format.DateTimeFormatter getTIME_FORMAT() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.time.LocalDateTime now() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String todayDateString(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime now) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String timeString(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime now) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String isoTimestamp(@org.jetbrains.annotations.NotNull()
    java.time.LocalDateTime now) {
        return null;
    }
    
    /**
     * "dd/MM/yyyy" -> "dd MMM yyyy" for history list display (e.g. 16 Aug 2026).
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String shortDate(@org.jetbrains.annotations.NotNull()
    java.lang.String dayMonthYear) {
        return null;
    }
}