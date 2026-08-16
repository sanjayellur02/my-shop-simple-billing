package com.grocery.billing.ui.waiting;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000D\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a:\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\tH\u0003\u001a2\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00032\u0012\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00010\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001aH\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u00122\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\u0005H\u0003\u001a \u0010\u0017\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0007\u00a8\u0006\u001e"}, d2 = {"ActionButton", "", "text", "", "onClick", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "filled", "", "destructive", "EditReferenceDialog", "initial", "onSave", "Lkotlin/Function1;", "onDismiss", "HeldBillRow", "held", "Lcom/grocery/billing/data/dao/HeldBillWithCount;", "onResume", "onEdit", "onDelete", "onComplete", "WaitingCustomersScreen", "navController", "Landroidx/navigation/NavHostController;", "factory", "Lcom/grocery/billing/ui/ViewModelFactory;", "billingViewModel", "Lcom/grocery/billing/ui/billing/BillingViewModel;", "app_debug"})
public final class WaitingCustomersScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void WaitingCustomersScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavHostController navController, @org.jetbrains.annotations.NotNull()
    com.grocery.billing.ui.ViewModelFactory factory, @org.jetbrains.annotations.NotNull()
    com.grocery.billing.ui.billing.BillingViewModel billingViewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void HeldBillRow(com.grocery.billing.data.dao.HeldBillWithCount held, kotlin.jvm.functions.Function0<kotlin.Unit> onResume, kotlin.jvm.functions.Function0<kotlin.Unit> onEdit, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete, kotlin.jvm.functions.Function0<kotlin.Unit> onComplete) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ActionButton(java.lang.String text, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier, boolean filled, boolean destructive) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EditReferenceDialog(java.lang.String initial, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSave, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }
}