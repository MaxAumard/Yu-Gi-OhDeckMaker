package fr.uha.aumard.deckbuilder.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.uha.aumard.deckbuilder.database.CardDao
import fr.uha.aumard.deckbuilder.database.CollectionCardDao
import fr.uha.aumard.deckbuilder.database.DeckBuilderDatabase
import fr.uha.aumard.deckbuilder.database.DeckDao
import fr.uha.aumard.deckbuilder.database.ExtensionDao
import fr.uha.aumard.deckbuilder.repository.CardRepository
import fr.uha.aumard.deckbuilder.repository.CollectionCardRepository
import fr.uha.aumard.deckbuilder.repository.DeckRepository
import fr.uha.aumard.deckbuilder.repository.ExtensionRepository
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
    fun provideCardDao(db: DeckBuilderDatabase) = db.cardDao

    @Singleton
    @Provides
    fun provideDeckDao(db: DeckBuilderDatabase) = db.deckDao

    @Singleton
    @Provides
    fun provideCollectionCardDao(db: DeckBuilderDatabase) = db.collectionCardDao

    @Singleton
    @Provides
    fun provideExtensionDao(db: DeckBuilderDatabase) = db.extensionDao

    @Singleton
    @Provides
    fun provideCardRepository(
//        dispatcher: CoroutineDispatcher,
        cardDao: CardDao
    ) = CardRepository(cardDao)

    @Singleton
    @Provides
    fun provideDeckRepository(
//        dispatcher: CoroutineDispatcher,
        deckDao: DeckDao,
        cardDao: CardDao
    ) = DeckRepository(deckDao, cardDao)

    @Singleton
    @Provides
    fun provideCollectionCardRepository(
//        dispatcher: CoroutineDispatcher,
        collectionCard: CollectionCardDao,
        cardDao: CardDao,
        extensionDao: ExtensionDao
    ) = CollectionCardRepository(collectionCard)

    @Singleton
    @Provides
    fun provideExtensionRepository(
//        dispatcher: CoroutineDispatcher,
        extensionDao: ExtensionDao,
        cardDao: CardDao
    ) = ExtensionRepository(extensionDao, cardDao)
}