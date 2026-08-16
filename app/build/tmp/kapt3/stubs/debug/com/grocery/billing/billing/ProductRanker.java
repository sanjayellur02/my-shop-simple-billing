package com.grocery.billing.billing;

/**
 * Ranks product search results for the billing screen so exact matches and
 * beginning-of-name matches appear before loose "contains" matches.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\"\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004J\u0018\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\u0007H\u0002\u00a8\u0006\r"}, d2 = {"Lcom/grocery/billing/billing/ProductRanker;", "", "()V", "rank", "", "Lcom/grocery/billing/data/entity/Product;", "query", "", "products", "rankOf", "", "product", "q", "app_debug"})
public final class ProductRanker {
    @org.jetbrains.annotations.NotNull()
    public static final com.grocery.billing.billing.ProductRanker INSTANCE = null;
    
    private ProductRanker() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.grocery.billing.data.entity.Product> rank(@org.jetbrains.annotations.NotNull()
    java.lang.String query, @org.jetbrains.annotations.NotNull()
    java.util.List<com.grocery.billing.data.entity.Product> products) {
        return null;
    }
    
    private final int rankOf(com.grocery.billing.data.entity.Product product, java.lang.String q) {
        return 0;
    }
}