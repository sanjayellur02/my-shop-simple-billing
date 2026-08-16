# Keep Room generated code
-keep class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Entity class * { *; }

# Kotlin serialization / coroutines
-keepclassmembers class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# Composable previews / lambdas
-dontwarn androidx.compose.**

# JSON via org.json (built into Android)
