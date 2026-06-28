package com.stennu718.myboardgames.core.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
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
public final class GameStatsDao_Impl implements GameStatsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GameStats> __insertionAdapterOfGameStats;

  public GameStatsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGameStats = new EntityInsertionAdapter<GameStats>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `game_stats` (`gameType`,`gamesPlayed`,`gamesWon`,`bestScore`,`totalXp`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GameStats entity) {
        statement.bindString(1, entity.getGameType());
        statement.bindLong(2, entity.getGamesPlayed());
        statement.bindLong(3, entity.getGamesWon());
        statement.bindLong(4, entity.getBestScore());
        statement.bindLong(5, entity.getTotalXp());
      }
    };
  }

  @Override
  public Object upsert(final GameStats stats, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGameStats.insert(stats);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAll(final Continuation<? super List<GameStats>> $completion) {
    final String _sql = "SELECT * FROM game_stats";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<GameStats>>() {
      @Override
      @NonNull
      public List<GameStats> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfGameType = CursorUtil.getColumnIndexOrThrow(_cursor, "gameType");
          final int _cursorIndexOfGamesPlayed = CursorUtil.getColumnIndexOrThrow(_cursor, "gamesPlayed");
          final int _cursorIndexOfGamesWon = CursorUtil.getColumnIndexOrThrow(_cursor, "gamesWon");
          final int _cursorIndexOfBestScore = CursorUtil.getColumnIndexOrThrow(_cursor, "bestScore");
          final int _cursorIndexOfTotalXp = CursorUtil.getColumnIndexOrThrow(_cursor, "totalXp");
          final List<GameStats> _result = new ArrayList<GameStats>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GameStats _item;
            final String _tmpGameType;
            _tmpGameType = _cursor.getString(_cursorIndexOfGameType);
            final int _tmpGamesPlayed;
            _tmpGamesPlayed = _cursor.getInt(_cursorIndexOfGamesPlayed);
            final int _tmpGamesWon;
            _tmpGamesWon = _cursor.getInt(_cursorIndexOfGamesWon);
            final int _tmpBestScore;
            _tmpBestScore = _cursor.getInt(_cursorIndexOfBestScore);
            final int _tmpTotalXp;
            _tmpTotalXp = _cursor.getInt(_cursorIndexOfTotalXp);
            _item = new GameStats(_tmpGameType,_tmpGamesPlayed,_tmpGamesWon,_tmpBestScore,_tmpTotalXp);
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
  public Object getByType(final String type, final Continuation<? super GameStats> $completion) {
    final String _sql = "SELECT * FROM game_stats WHERE gameType = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, type);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<GameStats>() {
      @Override
      @Nullable
      public GameStats call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfGameType = CursorUtil.getColumnIndexOrThrow(_cursor, "gameType");
          final int _cursorIndexOfGamesPlayed = CursorUtil.getColumnIndexOrThrow(_cursor, "gamesPlayed");
          final int _cursorIndexOfGamesWon = CursorUtil.getColumnIndexOrThrow(_cursor, "gamesWon");
          final int _cursorIndexOfBestScore = CursorUtil.getColumnIndexOrThrow(_cursor, "bestScore");
          final int _cursorIndexOfTotalXp = CursorUtil.getColumnIndexOrThrow(_cursor, "totalXp");
          final GameStats _result;
          if (_cursor.moveToFirst()) {
            final String _tmpGameType;
            _tmpGameType = _cursor.getString(_cursorIndexOfGameType);
            final int _tmpGamesPlayed;
            _tmpGamesPlayed = _cursor.getInt(_cursorIndexOfGamesPlayed);
            final int _tmpGamesWon;
            _tmpGamesWon = _cursor.getInt(_cursorIndexOfGamesWon);
            final int _tmpBestScore;
            _tmpBestScore = _cursor.getInt(_cursorIndexOfBestScore);
            final int _tmpTotalXp;
            _tmpTotalXp = _cursor.getInt(_cursorIndexOfTotalXp);
            _result = new GameStats(_tmpGameType,_tmpGamesPlayed,_tmpGamesWon,_tmpBestScore,_tmpTotalXp);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
