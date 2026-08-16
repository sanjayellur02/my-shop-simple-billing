package com.grocery.billing.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.grocery.billing.data.entity.HeldBillItem;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class HeldBillItemDao_Impl implements HeldBillItemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HeldBillItem> __insertionAdapterOfHeldBillItem;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public HeldBillItemDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHeldBillItem = new EntityInsertionAdapter<HeldBillItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `held_bill_items` (`held_bill_item_id`,`held_bill_id`,`product_id`,`product_name_snapshot`,`quantity`,`rate`,`amount`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HeldBillItem entity) {
        statement.bindLong(1, entity.getHeldBillItemId());
        statement.bindLong(2, entity.getHeldBillId());
        if (entity.getProductId() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getProductId());
        }
        if (entity.getProductNameSnapshot() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getProductNameSnapshot());
        }
        if (entity.getQuantity() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getQuantity());
        }
        statement.bindLong(6, entity.getRatePaise());
        statement.bindLong(7, entity.getAmountPaise());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM held_bill_items";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<HeldBillItem> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHeldBillItem.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getByHeldBill(final long heldBillId,
      final Continuation<? super List<HeldBillItem>> $completion) {
    final String _sql = "SELECT * FROM held_bill_items WHERE held_bill_id = ? ORDER BY held_bill_item_id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, heldBillId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HeldBillItem>>() {
      @Override
      @NonNull
      public List<HeldBillItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfHeldBillItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "held_bill_item_id");
          final int _cursorIndexOfHeldBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "held_bill_id");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "product_id");
          final int _cursorIndexOfProductNameSnapshot = CursorUtil.getColumnIndexOrThrow(_cursor, "product_name_snapshot");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfRatePaise = CursorUtil.getColumnIndexOrThrow(_cursor, "rate");
          final int _cursorIndexOfAmountPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final List<HeldBillItem> _result = new ArrayList<HeldBillItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HeldBillItem _item;
            final long _tmpHeldBillItemId;
            _tmpHeldBillItemId = _cursor.getLong(_cursorIndexOfHeldBillItemId);
            final long _tmpHeldBillId;
            _tmpHeldBillId = _cursor.getLong(_cursorIndexOfHeldBillId);
            final String _tmpProductId;
            if (_cursor.isNull(_cursorIndexOfProductId)) {
              _tmpProductId = null;
            } else {
              _tmpProductId = _cursor.getString(_cursorIndexOfProductId);
            }
            final String _tmpProductNameSnapshot;
            if (_cursor.isNull(_cursorIndexOfProductNameSnapshot)) {
              _tmpProductNameSnapshot = null;
            } else {
              _tmpProductNameSnapshot = _cursor.getString(_cursorIndexOfProductNameSnapshot);
            }
            final String _tmpQuantity;
            if (_cursor.isNull(_cursorIndexOfQuantity)) {
              _tmpQuantity = null;
            } else {
              _tmpQuantity = _cursor.getString(_cursorIndexOfQuantity);
            }
            final long _tmpRatePaise;
            _tmpRatePaise = _cursor.getLong(_cursorIndexOfRatePaise);
            final long _tmpAmountPaise;
            _tmpAmountPaise = _cursor.getLong(_cursorIndexOfAmountPaise);
            _item = new HeldBillItem(_tmpHeldBillItemId,_tmpHeldBillId,_tmpProductId,_tmpProductNameSnapshot,_tmpQuantity,_tmpRatePaise,_tmpAmountPaise);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAll(final Continuation<? super List<HeldBillItem>> $completion) {
    final String _sql = "SELECT * FROM held_bill_items ORDER BY held_bill_item_id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HeldBillItem>>() {
      @Override
      @NonNull
      public List<HeldBillItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfHeldBillItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "held_bill_item_id");
          final int _cursorIndexOfHeldBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "held_bill_id");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "product_id");
          final int _cursorIndexOfProductNameSnapshot = CursorUtil.getColumnIndexOrThrow(_cursor, "product_name_snapshot");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfRatePaise = CursorUtil.getColumnIndexOrThrow(_cursor, "rate");
          final int _cursorIndexOfAmountPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final List<HeldBillItem> _result = new ArrayList<HeldBillItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HeldBillItem _item;
            final long _tmpHeldBillItemId;
            _tmpHeldBillItemId = _cursor.getLong(_cursorIndexOfHeldBillItemId);
            final long _tmpHeldBillId;
            _tmpHeldBillId = _cursor.getLong(_cursorIndexOfHeldBillId);
            final String _tmpProductId;
            if (_cursor.isNull(_cursorIndexOfProductId)) {
              _tmpProductId = null;
            } else {
              _tmpProductId = _cursor.getString(_cursorIndexOfProductId);
            }
            final String _tmpProductNameSnapshot;
            if (_cursor.isNull(_cursorIndexOfProductNameSnapshot)) {
              _tmpProductNameSnapshot = null;
            } else {
              _tmpProductNameSnapshot = _cursor.getString(_cursorIndexOfProductNameSnapshot);
            }
            final String _tmpQuantity;
            if (_cursor.isNull(_cursorIndexOfQuantity)) {
              _tmpQuantity = null;
            } else {
              _tmpQuantity = _cursor.getString(_cursorIndexOfQuantity);
            }
            final long _tmpRatePaise;
            _tmpRatePaise = _cursor.getLong(_cursorIndexOfRatePaise);
            final long _tmpAmountPaise;
            _tmpAmountPaise = _cursor.getLong(_cursorIndexOfAmountPaise);
            _item = new HeldBillItem(_tmpHeldBillItemId,_tmpHeldBillId,_tmpProductId,_tmpProductNameSnapshot,_tmpQuantity,_tmpRatePaise,_tmpAmountPaise);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
