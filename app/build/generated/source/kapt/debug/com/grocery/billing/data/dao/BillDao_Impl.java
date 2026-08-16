package com.grocery.billing.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.grocery.billing.data.entity.Bill;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
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
public final class BillDao_Impl implements BillDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Bill> __insertionAdapterOfBill;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public BillDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBill = new EntityInsertionAdapter<Bill>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `bills` (`bill_id`,`bill_number`,`bill_date`,`bill_time`,`subtotal`,`discount`,`total`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Bill entity) {
        statement.bindLong(1, entity.getBillId());
        if (entity.getBillNumber() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getBillNumber());
        }
        if (entity.getBillDate() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getBillDate());
        }
        if (entity.getBillTime() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getBillTime());
        }
        statement.bindLong(5, entity.getSubtotalPaise());
        statement.bindLong(6, entity.getDiscountPaise());
        statement.bindLong(7, entity.getTotalPaise());
        if (entity.getCreatedAt() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getCreatedAt());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM bills";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Bill bill, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBill.insertAndReturnId(bill);
          __db.setTransactionSuccessful();
          return _result;
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
  public Flow<List<Bill>> observeAll() {
    final String _sql = "SELECT * FROM bills ORDER BY bill_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bills"}, new Callable<List<Bill>>() {
      @Override
      @NonNull
      public List<Bill> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final int _cursorIndexOfBillNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_number");
          final int _cursorIndexOfBillDate = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_date");
          final int _cursorIndexOfBillTime = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_time");
          final int _cursorIndexOfSubtotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfDiscountPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "discount");
          final int _cursorIndexOfTotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<Bill> _result = new ArrayList<Bill>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Bill _item;
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            final String _tmpBillNumber;
            if (_cursor.isNull(_cursorIndexOfBillNumber)) {
              _tmpBillNumber = null;
            } else {
              _tmpBillNumber = _cursor.getString(_cursorIndexOfBillNumber);
            }
            final String _tmpBillDate;
            if (_cursor.isNull(_cursorIndexOfBillDate)) {
              _tmpBillDate = null;
            } else {
              _tmpBillDate = _cursor.getString(_cursorIndexOfBillDate);
            }
            final String _tmpBillTime;
            if (_cursor.isNull(_cursorIndexOfBillTime)) {
              _tmpBillTime = null;
            } else {
              _tmpBillTime = _cursor.getString(_cursorIndexOfBillTime);
            }
            final long _tmpSubtotalPaise;
            _tmpSubtotalPaise = _cursor.getLong(_cursorIndexOfSubtotalPaise);
            final long _tmpDiscountPaise;
            _tmpDiscountPaise = _cursor.getLong(_cursorIndexOfDiscountPaise);
            final long _tmpTotalPaise;
            _tmpTotalPaise = _cursor.getLong(_cursorIndexOfTotalPaise);
            final String _tmpCreatedAt;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmpCreatedAt = null;
            } else {
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            _item = new Bill(_tmpBillId,_tmpBillNumber,_tmpBillDate,_tmpBillTime,_tmpSubtotalPaise,_tmpDiscountPaise,_tmpTotalPaise,_tmpCreatedAt);
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
  public Object getById(final long billId, final Continuation<? super Bill> $completion) {
    final String _sql = "SELECT * FROM bills WHERE bill_id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, billId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Bill>() {
      @Override
      @Nullable
      public Bill call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final int _cursorIndexOfBillNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_number");
          final int _cursorIndexOfBillDate = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_date");
          final int _cursorIndexOfBillTime = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_time");
          final int _cursorIndexOfSubtotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfDiscountPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "discount");
          final int _cursorIndexOfTotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final Bill _result;
          if (_cursor.moveToFirst()) {
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            final String _tmpBillNumber;
            if (_cursor.isNull(_cursorIndexOfBillNumber)) {
              _tmpBillNumber = null;
            } else {
              _tmpBillNumber = _cursor.getString(_cursorIndexOfBillNumber);
            }
            final String _tmpBillDate;
            if (_cursor.isNull(_cursorIndexOfBillDate)) {
              _tmpBillDate = null;
            } else {
              _tmpBillDate = _cursor.getString(_cursorIndexOfBillDate);
            }
            final String _tmpBillTime;
            if (_cursor.isNull(_cursorIndexOfBillTime)) {
              _tmpBillTime = null;
            } else {
              _tmpBillTime = _cursor.getString(_cursorIndexOfBillTime);
            }
            final long _tmpSubtotalPaise;
            _tmpSubtotalPaise = _cursor.getLong(_cursorIndexOfSubtotalPaise);
            final long _tmpDiscountPaise;
            _tmpDiscountPaise = _cursor.getLong(_cursorIndexOfDiscountPaise);
            final long _tmpTotalPaise;
            _tmpTotalPaise = _cursor.getLong(_cursorIndexOfTotalPaise);
            final String _tmpCreatedAt;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmpCreatedAt = null;
            } else {
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            _result = new Bill(_tmpBillId,_tmpBillNumber,_tmpBillDate,_tmpBillTime,_tmpSubtotalPaise,_tmpDiscountPaise,_tmpTotalPaise,_tmpCreatedAt);
          } else {
            _result = null;
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
  public Flow<Bill> observeById(final long billId) {
    final String _sql = "SELECT * FROM bills WHERE bill_id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, billId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bills"}, new Callable<Bill>() {
      @Override
      @Nullable
      public Bill call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final int _cursorIndexOfBillNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_number");
          final int _cursorIndexOfBillDate = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_date");
          final int _cursorIndexOfBillTime = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_time");
          final int _cursorIndexOfSubtotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfDiscountPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "discount");
          final int _cursorIndexOfTotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final Bill _result;
          if (_cursor.moveToFirst()) {
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            final String _tmpBillNumber;
            if (_cursor.isNull(_cursorIndexOfBillNumber)) {
              _tmpBillNumber = null;
            } else {
              _tmpBillNumber = _cursor.getString(_cursorIndexOfBillNumber);
            }
            final String _tmpBillDate;
            if (_cursor.isNull(_cursorIndexOfBillDate)) {
              _tmpBillDate = null;
            } else {
              _tmpBillDate = _cursor.getString(_cursorIndexOfBillDate);
            }
            final String _tmpBillTime;
            if (_cursor.isNull(_cursorIndexOfBillTime)) {
              _tmpBillTime = null;
            } else {
              _tmpBillTime = _cursor.getString(_cursorIndexOfBillTime);
            }
            final long _tmpSubtotalPaise;
            _tmpSubtotalPaise = _cursor.getLong(_cursorIndexOfSubtotalPaise);
            final long _tmpDiscountPaise;
            _tmpDiscountPaise = _cursor.getLong(_cursorIndexOfDiscountPaise);
            final long _tmpTotalPaise;
            _tmpTotalPaise = _cursor.getLong(_cursorIndexOfTotalPaise);
            final String _tmpCreatedAt;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmpCreatedAt = null;
            } else {
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            _result = new Bill(_tmpBillId,_tmpBillNumber,_tmpBillDate,_tmpBillTime,_tmpSubtotalPaise,_tmpDiscountPaise,_tmpTotalPaise,_tmpCreatedAt);
          } else {
            _result = null;
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
  public Flow<List<Bill>> observeByDate(final String date) {
    final String _sql = "SELECT * FROM bills WHERE bill_date = ? ORDER BY bill_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (date == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, date);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bills"}, new Callable<List<Bill>>() {
      @Override
      @NonNull
      public List<Bill> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final int _cursorIndexOfBillNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_number");
          final int _cursorIndexOfBillDate = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_date");
          final int _cursorIndexOfBillTime = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_time");
          final int _cursorIndexOfSubtotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfDiscountPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "discount");
          final int _cursorIndexOfTotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<Bill> _result = new ArrayList<Bill>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Bill _item;
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            final String _tmpBillNumber;
            if (_cursor.isNull(_cursorIndexOfBillNumber)) {
              _tmpBillNumber = null;
            } else {
              _tmpBillNumber = _cursor.getString(_cursorIndexOfBillNumber);
            }
            final String _tmpBillDate;
            if (_cursor.isNull(_cursorIndexOfBillDate)) {
              _tmpBillDate = null;
            } else {
              _tmpBillDate = _cursor.getString(_cursorIndexOfBillDate);
            }
            final String _tmpBillTime;
            if (_cursor.isNull(_cursorIndexOfBillTime)) {
              _tmpBillTime = null;
            } else {
              _tmpBillTime = _cursor.getString(_cursorIndexOfBillTime);
            }
            final long _tmpSubtotalPaise;
            _tmpSubtotalPaise = _cursor.getLong(_cursorIndexOfSubtotalPaise);
            final long _tmpDiscountPaise;
            _tmpDiscountPaise = _cursor.getLong(_cursorIndexOfDiscountPaise);
            final long _tmpTotalPaise;
            _tmpTotalPaise = _cursor.getLong(_cursorIndexOfTotalPaise);
            final String _tmpCreatedAt;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmpCreatedAt = null;
            } else {
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            _item = new Bill(_tmpBillId,_tmpBillNumber,_tmpBillDate,_tmpBillTime,_tmpSubtotalPaise,_tmpDiscountPaise,_tmpTotalPaise,_tmpCreatedAt);
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
  public Object allBillNumbers(final Continuation<? super List<String>> $completion) {
    final String _sql = "SELECT bill_number FROM bills";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<String>>() {
      @Override
      @NonNull
      public List<String> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final List<String> _result = new ArrayList<String>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final String _item;
            if (_cursor.isNull(0)) {
              _item = null;
            } else {
              _item = _cursor.getString(0);
            }
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
  public Object getAll(final Continuation<? super List<Bill>> $completion) {
    final String _sql = "SELECT * FROM bills ORDER BY bill_id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Bill>>() {
      @Override
      @NonNull
      public List<Bill> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final int _cursorIndexOfBillNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_number");
          final int _cursorIndexOfBillDate = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_date");
          final int _cursorIndexOfBillTime = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_time");
          final int _cursorIndexOfSubtotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfDiscountPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "discount");
          final int _cursorIndexOfTotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<Bill> _result = new ArrayList<Bill>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Bill _item;
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            final String _tmpBillNumber;
            if (_cursor.isNull(_cursorIndexOfBillNumber)) {
              _tmpBillNumber = null;
            } else {
              _tmpBillNumber = _cursor.getString(_cursorIndexOfBillNumber);
            }
            final String _tmpBillDate;
            if (_cursor.isNull(_cursorIndexOfBillDate)) {
              _tmpBillDate = null;
            } else {
              _tmpBillDate = _cursor.getString(_cursorIndexOfBillDate);
            }
            final String _tmpBillTime;
            if (_cursor.isNull(_cursorIndexOfBillTime)) {
              _tmpBillTime = null;
            } else {
              _tmpBillTime = _cursor.getString(_cursorIndexOfBillTime);
            }
            final long _tmpSubtotalPaise;
            _tmpSubtotalPaise = _cursor.getLong(_cursorIndexOfSubtotalPaise);
            final long _tmpDiscountPaise;
            _tmpDiscountPaise = _cursor.getLong(_cursorIndexOfDiscountPaise);
            final long _tmpTotalPaise;
            _tmpTotalPaise = _cursor.getLong(_cursorIndexOfTotalPaise);
            final String _tmpCreatedAt;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmpCreatedAt = null;
            } else {
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            _item = new Bill(_tmpBillId,_tmpBillNumber,_tmpBillDate,_tmpBillTime,_tmpSubtotalPaise,_tmpDiscountPaise,_tmpTotalPaise,_tmpCreatedAt);
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
  public Flow<List<Bill>> search(final String q) {
    final String _sql = "SELECT * FROM bills WHERE bill_number LIKE '%' || ? || '%' OR bill_date LIKE '%' || ? || '%' ORDER BY bill_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (q == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, q);
    }
    _argIndex = 2;
    if (q == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, q);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"bills"}, new Callable<List<Bill>>() {
      @Override
      @NonNull
      public List<Bill> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_id");
          final int _cursorIndexOfBillNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_number");
          final int _cursorIndexOfBillDate = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_date");
          final int _cursorIndexOfBillTime = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_time");
          final int _cursorIndexOfSubtotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfDiscountPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "discount");
          final int _cursorIndexOfTotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<Bill> _result = new ArrayList<Bill>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Bill _item;
            final long _tmpBillId;
            _tmpBillId = _cursor.getLong(_cursorIndexOfBillId);
            final String _tmpBillNumber;
            if (_cursor.isNull(_cursorIndexOfBillNumber)) {
              _tmpBillNumber = null;
            } else {
              _tmpBillNumber = _cursor.getString(_cursorIndexOfBillNumber);
            }
            final String _tmpBillDate;
            if (_cursor.isNull(_cursorIndexOfBillDate)) {
              _tmpBillDate = null;
            } else {
              _tmpBillDate = _cursor.getString(_cursorIndexOfBillDate);
            }
            final String _tmpBillTime;
            if (_cursor.isNull(_cursorIndexOfBillTime)) {
              _tmpBillTime = null;
            } else {
              _tmpBillTime = _cursor.getString(_cursorIndexOfBillTime);
            }
            final long _tmpSubtotalPaise;
            _tmpSubtotalPaise = _cursor.getLong(_cursorIndexOfSubtotalPaise);
            final long _tmpDiscountPaise;
            _tmpDiscountPaise = _cursor.getLong(_cursorIndexOfDiscountPaise);
            final long _tmpTotalPaise;
            _tmpTotalPaise = _cursor.getLong(_cursorIndexOfTotalPaise);
            final String _tmpCreatedAt;
            if (_cursor.isNull(_cursorIndexOfCreatedAt)) {
              _tmpCreatedAt = null;
            } else {
              _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            }
            _item = new Bill(_tmpBillId,_tmpBillNumber,_tmpBillDate,_tmpBillTime,_tmpSubtotalPaise,_tmpDiscountPaise,_tmpTotalPaise,_tmpCreatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
