package com.stennu718.myboardgames.core.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile GameStatsDao _gameStatsDao;

  private volatile AchievementDao _achievementDao;

  private volatile DailyChallengeDao _dailyChallengeDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `game_stats` (`gameType` TEXT NOT NULL, `gamesPlayed` INTEGER NOT NULL, `gamesWon` INTEGER NOT NULL, `bestScore` INTEGER NOT NULL, `totalXp` INTEGER NOT NULL, PRIMARY KEY(`gameType`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `achievements` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT NOT NULL, `isUnlocked` INTEGER NOT NULL, `unlockedAt` INTEGER, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `daily_challenges` (`date` TEXT NOT NULL, `challengeData` TEXT NOT NULL, `isCompleted` INTEGER NOT NULL, PRIMARY KEY(`date`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'bb1848a465291657108bae18e2fb3e86')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `game_stats`");
        db.execSQL("DROP TABLE IF EXISTS `achievements`");
        db.execSQL("DROP TABLE IF EXISTS `daily_challenges`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsGameStats = new HashMap<String, TableInfo.Column>(5);
        _columnsGameStats.put("gameType", new TableInfo.Column("gameType", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameStats.put("gamesPlayed", new TableInfo.Column("gamesPlayed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameStats.put("gamesWon", new TableInfo.Column("gamesWon", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameStats.put("bestScore", new TableInfo.Column("bestScore", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGameStats.put("totalXp", new TableInfo.Column("totalXp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGameStats = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGameStats = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGameStats = new TableInfo("game_stats", _columnsGameStats, _foreignKeysGameStats, _indicesGameStats);
        final TableInfo _existingGameStats = TableInfo.read(db, "game_stats");
        if (!_infoGameStats.equals(_existingGameStats)) {
          return new RoomOpenHelper.ValidationResult(false, "game_stats(com.stennu718.myboardgames.core.database.GameStats).\n"
                  + " Expected:\n" + _infoGameStats + "\n"
                  + " Found:\n" + _existingGameStats);
        }
        final HashMap<String, TableInfo.Column> _columnsAchievements = new HashMap<String, TableInfo.Column>(5);
        _columnsAchievements.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAchievements.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAchievements.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAchievements.put("isUnlocked", new TableInfo.Column("isUnlocked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAchievements.put("unlockedAt", new TableInfo.Column("unlockedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAchievements = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAchievements = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAchievements = new TableInfo("achievements", _columnsAchievements, _foreignKeysAchievements, _indicesAchievements);
        final TableInfo _existingAchievements = TableInfo.read(db, "achievements");
        if (!_infoAchievements.equals(_existingAchievements)) {
          return new RoomOpenHelper.ValidationResult(false, "achievements(com.stennu718.myboardgames.core.database.Achievement).\n"
                  + " Expected:\n" + _infoAchievements + "\n"
                  + " Found:\n" + _existingAchievements);
        }
        final HashMap<String, TableInfo.Column> _columnsDailyChallenges = new HashMap<String, TableInfo.Column>(3);
        _columnsDailyChallenges.put("date", new TableInfo.Column("date", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyChallenges.put("challengeData", new TableInfo.Column("challengeData", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDailyChallenges.put("isCompleted", new TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDailyChallenges = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDailyChallenges = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDailyChallenges = new TableInfo("daily_challenges", _columnsDailyChallenges, _foreignKeysDailyChallenges, _indicesDailyChallenges);
        final TableInfo _existingDailyChallenges = TableInfo.read(db, "daily_challenges");
        if (!_infoDailyChallenges.equals(_existingDailyChallenges)) {
          return new RoomOpenHelper.ValidationResult(false, "daily_challenges(com.stennu718.myboardgames.core.database.DailyChallengeEntity).\n"
                  + " Expected:\n" + _infoDailyChallenges + "\n"
                  + " Found:\n" + _existingDailyChallenges);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "bb1848a465291657108bae18e2fb3e86", "32319322ad3a6e59e6532c28ccee0288");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "game_stats","achievements","daily_challenges");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `game_stats`");
      _db.execSQL("DELETE FROM `achievements`");
      _db.execSQL("DELETE FROM `daily_challenges`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(GameStatsDao.class, GameStatsDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AchievementDao.class, AchievementDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DailyChallengeDao.class, DailyChallengeDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public GameStatsDao gameStatsDao() {
    if (_gameStatsDao != null) {
      return _gameStatsDao;
    } else {
      synchronized(this) {
        if(_gameStatsDao == null) {
          _gameStatsDao = new GameStatsDao_Impl(this);
        }
        return _gameStatsDao;
      }
    }
  }

  @Override
  public AchievementDao achievementDao() {
    if (_achievementDao != null) {
      return _achievementDao;
    } else {
      synchronized(this) {
        if(_achievementDao == null) {
          _achievementDao = new AchievementDao_Impl(this);
        }
        return _achievementDao;
      }
    }
  }

  @Override
  public DailyChallengeDao dailyChallengeDao() {
    if (_dailyChallengeDao != null) {
      return _dailyChallengeDao;
    } else {
      synchronized(this) {
        if(_dailyChallengeDao == null) {
          _dailyChallengeDao = new DailyChallengeDao_Impl(this);
        }
        return _dailyChallengeDao;
      }
    }
  }
}
