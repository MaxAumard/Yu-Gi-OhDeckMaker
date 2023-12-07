import android.content.Context
import fr.uha.aumard.deckbuilder.database.DeckBuilderDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DatabaseInitializer {
    suspend fun initializeDatabase(context: Context) {

        withContext(Dispatchers.IO) {
            val deckBuilderDatabase = DeckBuilderDatabase.create(context)
            if (deckBuilderDatabase.cardDao.countCards() == 0) {
                deckBuilderDatabase.insertDataFromJson(context)
            }

        }
    }
}