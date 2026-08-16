package com.grocery.billing.data.csv;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\"\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J(\u0010\t\u001a\u00020\n2\u0012\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\f0\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u000eR\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/grocery/billing/data/csv/CsvImportAnalyzer;", "", "()V", "COL_BARCODE", "", "COL_ID", "COL_NAME", "COL_PRICE", "COL_UNIT", "analyze", "Lcom/grocery/billing/data/csv/CsvImportAnalysis;", "rows", "", "existingIds", "", "app_debug"})
public final class CsvImportAnalyzer {
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COL_ID = "id";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COL_NAME = "product_name";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COL_PRICE = "price";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COL_UNIT = "unit";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String COL_BARCODE = "barcode";
    @org.jetbrains.annotations.NotNull()
    public static final com.grocery.billing.data.csv.CsvImportAnalyzer INSTANCE = null;
    
    private CsvImportAnalyzer() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.grocery.billing.data.csv.CsvImportAnalysis analyze(@org.jetbrains.annotations.NotNull()
    java.util.List<? extends java.util.List<java.lang.String>> rows, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> existingIds) {
        return null;
    }
}