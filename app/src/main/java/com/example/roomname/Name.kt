package com.example.roomname

import android.content.Context
import androidx.room.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
// Entity
//=========================================================================
@Entity(tableName = "name")
data class Name(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)
//=========================================================================


typealias names = List<Name>

//Dao
//=========================================================================
@Dao
interface NameDao {
    @Query("SELECT * FROM name ORDER BY id ASC")
    fun getNames(): Flow<names>

    @Insert
    fun addName(name: Name)
}

//DataBase
//=========================================================================

@Database(entities = [Name::class], version = 1, exportSchema = false)
abstract class NameDb : RoomDatabase() {
    abstract fun nameDao(): NameDao
}

//Repository
//=========================================================================


interface RepositoryName {
    fun getName(): Flow<names>
    fun addName(name: Name)
}


//=========================================================================


class RepositoryNameImpl(private val nameDao: NameDao) : RepositoryName {
    override fun getName(): Flow<names> {
        return nameDao.getNames()
    }

    override fun addName(name: Name) {
        return nameDao.addName(name)
    }
}


//Module
//=========================================================================

@Module
@InstallIn(SingletonComponent::class)
class NameModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, NameDb::class.java, "name")
            .build()

    @Provides
    fun provideDao(nameDb: NameDb) = nameDb.nameDao()

    @Provides
    fun provideRepository(nameDao: NameDao): RepositoryName {
        return RepositoryNameImpl(nameDao)
    }

}

//=========================================================================