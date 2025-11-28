import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Ignore
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aplikasiandroidpertama.UserDao // Pastikan ini benar
import com.example.aplikasiandroidpertama.UserEntity // Pastikan ini benar

// Pastikan UserEntity sudah ada dan benar
@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class AbsenDatabase : RoomDatabase() {

    // Definisikan DAO Anda di sini
    // Pastikan tipe kembalian adalah UserDao
    abstract fun userDao(): UserDao // TIDAK PERLU TANDA KURUNG TAMBAHAN () di sini

    companion object {
        @Volatile
        private var INSTANCE: AbsenDatabase? = null

        fun getDatabase(context: Context): AbsenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AbsenDatabase::class.java,
                    "aplikasi_absen_db"
                )
                    // .fallbackToDestructiveMigration() // Opsional
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}