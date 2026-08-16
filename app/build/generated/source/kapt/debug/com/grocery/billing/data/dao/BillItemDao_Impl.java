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
import com.grocery.billing.data.entity.BillItem;
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
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BillItemDao_Impl implements BillItemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BillItem> __insertionAdapterOfBillItem;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public BillItemDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBillItem = new EntityInsertionAdapter<BillItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `bill_items` (`bill_item_id`,`bill_id`,`product_id`,`product_name_snapshot`,`quantity`,`rate`,`amount`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BillItem entity) {
        statement.bindLong(1, entity.getBillItemId());
        statement.bindLong(2, entity.getBillId());
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
        final String _query = "DELETE FROM bill_items";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<BillItem> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBillItem.insert(items);
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
  public Flow<List<BillItem>> observeByBill(final long billId) {
    final String _sql = "SELECT * FROM bill_items WHERE bill_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, billId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bill_items"}, new Callable<List<BillItem>>() {
      @Override
      @NonNull
      public List<BillItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBillItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_item_id");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "product_id");
          final int _cursorIndexOfProductNameSnapshot = CursorUtil.getColumnIndexOrThrow(_cursor, "product_name_snapshot");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfRatePaise = CursorUtil.getColumnIndexOrThrow(_cursor, "rate");
          final int _cursorIndexOfAmountPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final List<BillItem> _result = new ArrayList<BillItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BillItem _item;
            final long _tmpBillItemId;
            _tmpBillItemId = _cursor.getLong(_cursorIndexOfBillItemId);
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
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
            _item = new BillItem(_tmpBillItemId,_tmpBillId,_tmpProductId,_tmpProductNameSnapshot,_tmpQuantity,_tmpRatePaise,_tmpAmountPaise);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getByBill(final long billId,
      final Continuation<? super List<BillItem>> $completion) {
    final String _sql = "SELECT * FROM bill_items WHERE bill_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, billId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BillItem>>() {
      @Override
      @NonNull
      public List<BillItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBillItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_item_id");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "product_id");
          final int _cursorIndexOfProductNameSnapshot = CursorUtil.getColumnIndexOrThrow(_cursor, "product_name_snapshot");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfRatePaise = CursorUtil.getColumnIndexOrThrow(_cursor, "rate");
          final int _cursorIndexOfAmountPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final List<BillItem> _result = new ArrayList<BillItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BillItem _item;
            final long _tmpBillItemId;
            _tmpBillItemId = _cursor.getLong(_cursorIndexOfBillItemId);
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
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
            _item = new BillItem(_tmpBillItemId,_tmpBillId,_tmpProductId,_tmpProductNameSnapshot,_tmpQuantity,_tmpRatePaise,_tmpAmountPaise);
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
  public Object getAll(final Continuation<? super List<BillItem>> $completion) {
    final String _sql = "SELECT * FROM bill_items ORDER BY bill_item_id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BillItem>>() {
      @Override
      @NonNull
      public List<BillItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBillItemId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_item_id");
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "product_id");
          final int _cursorIndexOfProductNameSnapshot = CursorUtil.getColumnIndexOrThrow(_cursor, "product_name_snapshot");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfRatePaise = CursorUtil.getColumnIndexOrThrow(_cursor, "rate");
          final int _cursorIndexOfAmountPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final List<BillItem> _result = new ArrayList<BillItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BillItem _item;
            final long _tmpBillItemId;
            _tmpBillItemId = _cursor.getLong(_cursorIndexOfBillItemId);
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
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
            _item = new BillItem(_tmpBillItemId,_tmpBillId,_tmpProductId,_tmpProductNameSnapshot,_tmpQuantity,_tmpRatePaise,_tmpAmountPaise);
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
