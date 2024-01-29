package fr.uha.aumard.deckbuilder.database

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import fr.uha.aumard.android.database.DatabaseTypeConverters
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.model.CollectionCard
import fr.uha.aumard.deckbuilder.model.CollectionCardAssociation
import fr.uha.aumard.deckbuilder.model.Deck
import fr.uha.aumard.deckbuilder.model.DeckCardAssociation
import fr.uha.aumard.deckbuilder.model.Extension
import fr.uha.aumard.deckbuilder.model.ExtensionCardAssociation
import fr.uha.aumard.deckbuilder.model.Type

@Database(
    entities = [
        Card::class,
        Deck::class,
        DeckCardAssociation::class,
        CollectionCard::class,
        CollectionCardAssociation::class,
        Extension::class,
        ExtensionCardAssociation::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatabaseTypeConverters::class)
abstract class DeckBuilderDatabase : RoomDatabase() {

    abstract val cardDao: CardDao
    abstract val deckDao: DeckDao
    abstract val collectionCardDao: CollectionCardDao
    abstract val extensionDao: ExtensionDao

    companion object {
        private lateinit var instance: DeckBuilderDatabase

        @Synchronized
        fun create(context: Context): DeckBuilderDatabase {
            instance =
                Room.databaseBuilder(context, DeckBuilderDatabase::class.java, "deckbuilder.db")
                    .build()
            return instance
        }

        @Synchronized
        fun get(): DeckBuilderDatabase {
            return instance
        }

    }

    suspend fun insertDataFromJson(context: Context) {
        try {
            val jsonString =
                context.assets.open("ygo_data_cards.json").bufferedReader().use { it.readText() }
            val gson = Gson()
            val jsonArray = gson.fromJson(jsonString, JsonArray::class.java)
            val cards = mutableListOf<Card>()
            val extensions = mutableSetOf<Extension>()
            val extensionCardAssociations = mutableListOf<ExtensionCardAssociation>()

            var cid: Long = 0
            for (jsonElement in jsonArray) {
                if (jsonElement is JsonObject) {
                    val name = jsonElement.get("name")?.takeIf { !it.isJsonNull }?.asString ?: ""
                    val description =
                        jsonElement.get("desc")?.takeIf { !it.isJsonNull }?.asString ?: ""
                    val level = jsonElement.get("level")?.takeIf { !it.isJsonNull }?.asString
                    val rawType = jsonElement.get("type")?.takeIf { !it.isJsonNull }?.asString ?: ""
                    val isExtraDeck =
                        jsonElement.get("is_extra_deck")?.takeIf { !it.isJsonNull }?.asBoolean
                            ?: false
                    val pictureUri = jsonElement.get("image")
                        ?.takeIf { !it.isJsonNull }?.asString?.let { Uri.parse(it) }
                    val attack = jsonElement.get("attack")?.takeIf { !it.isJsonNull }?.asString
                    val defense = jsonElement.get("defense")?.takeIf { !it.isJsonNull }?.asString
                    val type = when {
                        rawType.contains("Monster", ignoreCase = true) -> Type.MONSTER
                        rawType.contains("Spell", ignoreCase = true) -> Type.MAGIC
                        rawType.contains("Trap", ignoreCase = true) -> Type.TRAP
                        else -> Type.ALL
                    }

                    val newCard = Card(
                        cid = cid,
                        name = name,
                        description = description,
                        level = level,
                        type = type,
                        isExtraDeck = isExtraDeck,
                        picture = pictureUri,
                        attack = attack,
                        defense = defense
                    )
                    cards.add(newCard)


                    val setsArray = jsonElement.getAsJsonArray("sets")
                    setsArray?.forEach { setElement ->
                        if (setElement is JsonObject) {
                            val setName = setElement.get("set_name").asString
                            if (
                                "tournament" !in setName.lowercase()
                                && "DEM" !in setElement.get("set_code").asString
                                && "tin" !in setName.lowercase()
                                && "structure" !in setName.lowercase()
                                && "starter" !in setName.lowercase()
                                && "legendary" !in setName.lowercase()
                                && "participation" !in setName.lowercase()
                                && "duel" !in setName.lowercase()
                                && "jump" !in setName.lowercase()
                                && "promo" !in setName.lowercase()
                                ) {
                                val newExtension = Extension(setName = setName)
                                extensions.add(newExtension)

                                val newAssociation =
                                    ExtensionCardAssociation(setName = setName, cid = cid)
                                extensionCardAssociations.add(newAssociation)
                            }
                        }
                    }
                    cid++;
                }
            }
            cardDao.insertAll(cards)
            extensionDao.insertAll(extensions.toList())
            extensionDao.addExtensionCard(extensionCardAssociations)
            Log.d("DeckBuilderDatabase", "extensionCardAssociations: $extensionCardAssociations")

        } catch (e: Exception) {
            Log.e("DeckBuilderDatabase", "Error inserting data from JSON", e)
        }
    }
}