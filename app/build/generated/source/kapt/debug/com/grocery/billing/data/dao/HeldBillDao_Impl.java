package com.grocery.billing.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.grocery.billing.data.entity.HeldBill;
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
public final class HeldBillDao_Impl implements HeldBillDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HeldBill> __insertionAdapterOfHeldBill;

  private final EntityDeletionOrUpdateAdapter<HeldBill> __deletionAdapterOfHeldBill;

  private final EntityDeletionOrUpdateAdapter<HeldBill> __updateAdapterOfHeldBill;

  private final SharedSQLiteStatement __preparedStmtOfUpdateReference;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public HeldBillDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHeldBill = new EntityInsertionAdapter<HeldBill>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `held_bills` (`held_bill_id`,`reference`,`bill_number`,`bill_date`,`bill_time`,`subtotal`,`discount`,`total`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HeldBill entity) {
        statement.bindLong(1, entity.getHeldBillId());
        if (entity.getReference() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getReference());
        }
        if (entity.getBillNumber() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getBillNumber());
        }
        if (entity.getBillDate() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getBillDate());
        }
        if (entity.getBillTime() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getBillTime());
        }
        statement.bindLong(6, entity.getSubtotalPaise());
        statement.bindLong(7, entity.getDiscountPaise());
        statement.bindLong(8, entity.getTotalPaise());
        if (entity.getCreatedAt() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getCreatedAt());
        }
      }
    };
    this.__deletionAdapterOfHeldBill = new EntityDeletionOrUpdateAdapter<HeldBill>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `held_bills` WHERE `held_bill_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HeldBill entity) {
        statement.bindLong(1, entity.getHeldBillId());
      }
    };
    this.__updateAdapterOfHeldBill = new EntityDeletionOrUpdateAdapter<HeldBill>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `held_bills` SET `held_bill_id` = ?,`reference` = ?,`bill_number` = ?,`bill_date` = ?,`bill_time` = ?,`subtotal` = ?,`discount` = ?,`total` = ?,`created_at` = ? WHERE `held_bill_id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HeldBill entity) {
        statement.bindLong(1, entity.getHeldBillId());
        if (entity.getReference() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getReference());
        }
        if (entity.getBillNumber() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getBillNumber());
        }
        if (entity.getBillDate() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getBillDate());
        }
        if (entity.getBillTime() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getBillTime());
        }
        statement.bindLong(6, entity.getSubtotalPaise());
        statement.bindLong(7, entity.getDiscountPaise());
        statement.bindLong(8, entity.getTotalPaise());
        if (entity.getCreatedAt() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getCreatedAt());
        }
        statement.bindLong(10, entity.getHeldBillId());
      }
    };
    this.__preparedStmtOfUpdateReference = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE held_bills SET reference = ? WHERE held_bill_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM held_bills WHERE held_bill_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM held_bills";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final HeldBill bill, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfHeldBill.insertAndReturnId(bill);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final HeldBill bill, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfHeldBill.handle(bill);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final HeldBill bill, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfHeldBill.handle(bill);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateReference(final long id, final String reference,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateReference.acquire();
        int _argIndex = 1;
        if (reference == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, reference);
        }
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfUpdateReference.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfDeleteById.release(_stmt);
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
  public Object getById(final long id, final Continuation<? super HeldBill> $completion) {
    final String _sql = "SELECT * FROM held_bills WHERE held_bill_id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<HeldBill>() {
      @Override
      @Nullable
      public HeldBill call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfHeldBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "held_bill_id");
          final int _cursorIndexOfReference = CursorUtil.getColumnIndexOrThrow(_cursor, "reference");
          final int _cursorIndexOfBillNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_number");
          final int _cursorIndexOfBillDate = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_date");
          final int _cursorIndexOfBillTime = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_time");
          final int _cursorIndexOfSubtotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfDiscountPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "discount");
          final int _cursorIndexOfTotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final HeldBill _result;
          if (_cursor.moveToFirst()) {
            final long _tmpHeldBillId;
            _tmpHeldBillId = _cursor.getLong(_cursorIndexOfHeldBillId);
            final String _tmpReference;
            if (_cursor.isNull(_cursorIndexOfReference)) {
              _tmpReference = null;
            } else {
              _tmpReference = _cursor.getString(_cursorIndexOfReference);
            }
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
            _result = new HeldBill(_tmpHeldBillId,_tmpReference,_tmpBillNumber,_tmpBillDate,_tmpBillTime,_tmpSubtotalPaise,_tmpDiscountPaise,_tmpTotalPaise,_tmpCreatedAt);
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
  public Flow<List<HeldBillWithCount>> observeAllWithCount() {
    final String _sql = "\n"
            + "        SELECT h.held_bill_id, h.reference, h.bill_number, h.bill_date, h.bill_time,\n"
            + "               h.subtotal, h.discount, h.total, h.created_at,\n"
            + "               (SELECT COUNT(*) FROM held_bill_items i WHERE i.held_bill_id = h.held_bill_id) AS item_count\n"
            + "        FROM held_bills h\n"
            + "        ORDER BY h.held_bill_id DESC\n"
            + "        ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"held_bill_items",
        "held_bills"}, new Callable<List<HeldBillWithCount>>() {
      @Override
      @NonNull
      public List<HeldBillWithCount> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfHeldBillId = 0;
          final int _cursorIndexOfReference = 1;
          final int _cursorIndexOfBillNumber = 2;
          final int _cursorIndexOfBillDate = 3;
          final int _cursorIndexOfBillTime = 4;
          final int _cursorIndexOfSubtotalPaise = 5;
          final int _cursorIndexOfDiscountPaise = 6;
          final int _cursorIndexOfTotalPaise = 7;
          final int _cursorIndexOfCreatedAt = 8;
          final int _cursorIndexOfItemCount = 9;
          final List<HeldBillWithCount> _result = new ArrayList<HeldBillWithCount>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HeldBillWithCount _item;
            final long _tmpHeldBillId;
            _tmpHeldBillId = _cursor.getLong(_cursorIndexOfHeldBillId);
            final String _tmpReference;
            if (_cursor.isNull(_cursorIndexOfReference)) {
              _tmpReference = null;
            } else {
              _tmpReference = _cursor.getString(_cursorIndexOfReference);
            }
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
            final int _tmpItemCount;
            _tmpItemCount = _cursor.getInt(_cursorIndexOfItemCount);
            _item = new HeldBillWithCount(_tmpHeldBillId,_tmpReference,_tmpBillNumber,_tmpBillDate,_tmpBillTime,_tmpSubtotalPaise,_tmpDiscountPaise,_tmpTotalPaise,_tmpCreatedAt,_tmpItemCount);
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
  public Flow<List<HeldBill>> observeAll() {
    final String _sql = "SELECT * FROM held_bills ORDER BY held_bill_id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"held_bills"}, new Callable<List<HeldBill>>() {
      @Override
      @NonNull
      public List<HeldBill> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfHeldBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "held_bill_id");
          final int _cursorIndexOfReference = CursorUtil.getColumnIndexOrThrow(_cursor, "reference");
          final int _cursorIndexOfBillNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_number");
          final int _cursorIndexOfBillDate = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_date");
          final int _cursorIndexOfBillTime = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_time");
          final int _cursorIndexOfSubtotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfDiscountPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "discount");
          final int _cursorIndexOfTotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<HeldBill> _result = new ArrayList<HeldBill>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HeldBill _item;
            final long _tmpHeldBillId;
            _tmpHeldBillId = _cursor.getLong(_cursorIndexOfHeldBillId);
            final String _tmpReference;
            if (_cursor.isNull(_cursorIndexOfReference)) {
              _tmpReference = null;
            } else {
              _tmpReference = _cursor.getString(_cursorIndexOfReference);
            }
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
            _item = new HeldBill(_tmpHeldBillId,_tmpReference,_tmpBillNumber,_tmpBillDate,_tmpBillTime,_tmpSubtotalPaise,_tmpDiscountPaise,_tmpTotalPaise,_tmpCreatedAt);
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
  public Object getAll(final Continuation<? super List<HeldBill>> $completion) {
    final String _sql = "SELECT * FROM held_bills ORDER BY held_bill_id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HeldBill>>() {
      @Override
      @NonNull
      public List<HeldBill> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfHeldBillId = CursorUtil.getColumnIndexOrThrow(_cursor, "held_bill_id");
          final int _cursorIndexOfReference = CursorUtil.getColumnIndexOrThrow(_cursor, "reference");
          final int _cursorIndexOfBillNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_number");
          final int _cursorIndexOfBillDate = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_date");
          final int _cursorIndexOfBillTime = CursorUtil.getColumnIndexOrThrow(_cursor, "bill_time");
          final int _cursorIndexOfSubtotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "subtotal");
          final int _cursorIndexOfDiscountPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "discount");
          final int _cursorIndexOfTotalPaise = CursorUtil.getColumnIndexOrThrow(_cursor, "total");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final List<HeldBill> _result = new ArrayList<HeldBill>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HeldBill _item;
            final long _tmpHeldBillId;
            _tmpHeldBillId = _cursor.getLong(_cursorIndexOfHeldBillId);
            final String _tmpReference;
            if (_cursor.isNull(_cursorIndexOfReference)) {
              _tmpReference = null;
            } else {
              _tmpReference = _cursor.getString(_cursorIndexOfReference);
            }
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
            _item = new HeldBill(_tmpHeldBillId,_tmpReference,_tmpBillNumber,_tmpBillDate,_tmpBillTime,_tmpSubtotalPaise,_tmpDiscountPaise,_tmpTotalPaise,_tmpCreatedAt);
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
