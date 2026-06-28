package com.stennu718.myboardgames.core.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AchievementDao_Impl implements AchievementDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Achievement> __insertionAdapterOfAchievement;

  private final EntityDeletionOrUpdateAdapter<Achievement> __updateAdapterOfAchievement;

  public AchievementDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAchievement = new EntityInsertionAdapter<Achievement>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `achievements` (`id`,`name`,`description`,`isUnlocked`,`unlockedAt`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Achievement entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getDescription());
        final int _tmp = entity.isUnlocked() ? 1 : 0;
        statement.bindLong(4, _tmp);
        if (entity.getUnlockedAt() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getUnlockedAt());
        }
      }
    };
    this.__updateAdapterOfAchievement = new EntityDeletionOrUpdateAdapter<Achievement>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `achievements` SET `id` = ?,`name` = ?,`description` = ?,`isUnlocked` = ?,`unlockedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Achievement entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getDescription());
        final int _tmp = entity.isUnlocked() ? 1 : 0;
        statement.bindLong(4, _tmp);
        if (entity.getUnlockedAt() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getUnlockedAt());
        }
        statement.bindString(6, entity.getId());
      }
    };
  }

  @Override
  public Object insertAll(final List<Achievement> achievements,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAchievement.insert(achievements);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Achievement achievement,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfAchievement.handle(achievement);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAll(final Continuation<? super List<Achievement>> $completion) {
    final String _sql = "SELECT * FROM achievements";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Achievement>>() {
      @Override
      @NonNull
      public List<Achievement> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfIsUnlocked = CursorUtil.getColumnIndexOrThrow(_cursor, "isUnlocked");
          final int _cursorIndexOfUnlockedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "unlockedAt");
          final List<Achievement> _result = new ArrayList<Achievement>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Achievement _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final boolean _tmpIsUnlocked;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsUnlocked);
            _tmpIsUnlocked = _tmp != 0;
            final Long _tmpUnlockedAt;
            if (_cursor.isNull(_cursorIndexOfUnlockedAt)) {
              _tmpUnlockedAt = null;
            } else {
              _tmpUnlockedAt = _cursor.getLong(_cursorIndexOfUnlockedAt);
            }
            _item = new Achievement(_tmpId,_tmpName,_tmpDescription,_tmpIsUnlocked,_tmpUnlockedAt);
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
