# Add project specific ProGuard rules here.
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

# Keep chess engine classes
-keep class com.stennu718.myboardgames.feature.chess.engine.** { *; }
-keep class com.stennu718.myboardgames.feature.checkers.engine.** { *; }
-keep class com.stennu718.myboardgames.feature.sudoku.engine.** { *; }
-keep class com.stennu718.myboardgames.feature.blockudoku.engine.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# Hilt
-dontwarn dagger.hilt.**
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }