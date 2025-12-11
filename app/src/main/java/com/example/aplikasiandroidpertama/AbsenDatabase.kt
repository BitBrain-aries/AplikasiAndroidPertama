import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Ignore
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aplikasiandroidpertama.MIGRATION_1_2
import com.example.aplikasiandroidpertama.UserDao
import com.example.aplikasiandroidpertama.UserEntity

// Pastikan UserEntity sudah ada dan benar
@Database(entities = [UserEntity::class], version = 2, exportSchema = false)
abstract class AbsenDatabase : RoomDatabase() {

    // Definisikan DAO Anda di sini
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AbsenDatabase? = null

        fun getDatabase(context: Context): AbsenDatabase {
            // Menggunakan operator Elvis (?:) untuk thread-safe
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AbsenDatabase::class.java,
                    "aplikasi_absen_db"
                ).addMigrations(MIGRATION_1_2).build()

                INSTANCE = instance
                instance
            }
        }
    }
}