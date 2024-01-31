package fr.uha.aumard.deckbuilder.repository

import androidx.annotation.WorkerThread
import fr.uha.aumard.deckbuilder.database.CardDao
import fr.uha.aumard.deckbuilder.database.ExtensionDao
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.model.Extension
import kotlinx.coroutines.flow.Flow


class ExtensionRepository(private val extensionDao: ExtensionDao, private val cardDao: CardDao) {
    @WorkerThread
    fun getExtensions(): Flow<List<Extension>> {
        return extensionDao.getExtensions()
    }

    fun getCardsInExtension(name: String): Flow<List<Card>> {
        return extensionDao.getCardsInExtension(name)
    }

}