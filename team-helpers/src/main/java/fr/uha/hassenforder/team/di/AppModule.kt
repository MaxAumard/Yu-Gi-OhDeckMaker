package fr.uha.hassenforder.team.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.uha.hassenforder.team.database.PersonDao
import fr.uha.hassenforder.team.database.TeamDatabase
import fr.uha.hassenforder.team.repository.PersonRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = TeamDatabase.create(appContext)

    @Singleton
    @Provides
    fun providePersonDao(db: TeamDatabase) = db.getPersonDao()

    @Singleton
    @Provides
    fun providePersonRepository(
//        externalScope: CoroutineScope,
//        ioDispatcher: CoroutineDispatcher,
        personDao: PersonDao
    ) = PersonRepository(personDao)

}