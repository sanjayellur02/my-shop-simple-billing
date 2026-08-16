package com.grocery.billing;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0006\u001a\u00020\u0007H\u0007J\b\u0010\b\u001a\u00020\u0007H\u0007J\b\u0010\t\u001a\u00020\u0007H\u0007J\b\u0010\n\u001a\u00020\u0007H\u0007J\b\u0010\u000b\u001a\u00020\u0007H\u0007J\b\u0010\f\u001a\u00020\u0007H\u0007J$\u0010\r\u001a\u00020\u00052\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u000fH\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/grocery/billing/ProductRankerTest;", "", "()V", "products", "", "Lcom/grocery/billing/data/entity/Product;", "caseInsensitiveRanking", "", "emptyQueryKeepsOrder", "exactBarcodeMatchFirst", "exactMatchFirst", "nonMatchingProductsRankedLast", "prefixMatchesBeforeContains", "product", "id", "", "name", "barcode", "app_debugUnitTest"})
public final class ProductRankerTest {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.grocery.billing.data.entity.Product> products = null;
    
    public ProductRankerTest() {
        super();
    }
    
    private final com.grocery.billing.data.entity.Product product(java.lang.String id, java.lang.String name, java.lang.String barcode) {
        return null;
    }
    
    @org.junit.Test()
    public final void exactMatchFirst() {
    }
    
    @org.junit.Test()
    public final void exactBarcodeMatchFirst() {
    }
    
    @org.junit.Test()
    public final void prefixMatchesBeforeContains() {
    }
    
    @org.junit.Test()
    public final void nonMatchingProductsRankedLast() {
    }
    
    @org.junit.Test()
    public final void emptyQueryKeepsOrder() {
    }
    
    @org.junit.Test()
    public final void caseInsensitiveRanking() {
    }
}