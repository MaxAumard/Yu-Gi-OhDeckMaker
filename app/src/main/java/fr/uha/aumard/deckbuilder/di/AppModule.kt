package fr.uha.aumard.deckbuilder.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.uha.aumard.deckbuilder.database.CardDao
import fr.uha.aumard.deckbuilder.database.DeckBuilderDatabase
import fr.uha.aumard.deckbuilder.database.DeckDao
import fr.uha.aumard.deckbuilder.repository.CardRepository
import fr.uha.aumard.deckbuilder.repository.DeckRepository
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
    fun provideDatabase(@ApplicationContext appContext: Context) =
        DeckBuilderDatabase.create(appContext)

    @Singleton
    @Provides
    fun providePersonDao(db: DeckBuilderDatabase) = db.cardDao

    @Singleton
    @Provides
    fun provideTeamDao(db: DeckBuilderDatabase) = db.deckDao

    @Singleton
    @Provides
    fun providePersonRepository(
//        dispatcher: CoroutineDispatcher,
        cardDao: CardDao
    ) = CardRepository(cardDao)

    @Singleton
    @Provides
    fun provideTeamRepository(
//        dispatcher: CoroutineDispatcher,
        deckDao: DeckDao,
        cardDao: CardDao
    ) = DeckRepository(deckDao, cardDao)

}