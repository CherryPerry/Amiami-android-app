package ru.cherryperry.amiami.data.export

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class ExportedDataGsonTypeAdapter : JsonDeserializer<ExportedData>, JsonSerializer<ExportedData> {

    companion object {
        private const val FIELD_VALUE = "value"
        private const val FIELD_REGEX = "regex"
        private const val FIELD_HIGHLIGHT = "highlight"
    }

    override fun serialize(src: ExportedData?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val rootObject = JsonObject()
        src?.let { data ->
            val array = JsonArray()
            data.highlight
                .map {
                    val itemObject = JsonObject()
                    itemObject.addProperty(FIELD_VALUE, it.value)
                    itemObject.addProperty(FIELD_REGEX, it.regex)
                    itemObject
                }.forEach {
                    array.add(it)
                }
            rootObject.add(FIELD_HIGHLIGHT, array)
        }
        return rootObject
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ExportedData =
        ExportedData(json.asJsonObject
            .getAsJsonArray(FIELD_HIGHLIGHT)
            .mapNotNull { itemJson ->
                when {
                    itemJson.isJsonObject ->
                        itemJson.asJsonObject.let {
                            ExportedItem(it.get(FIELD_VALUE).asString, it.get(FIELD_REGEX).asBoolean)
                        }
                    itemJson.isJsonPrimitive -> ExportedItem(itemJson.asString, false)
                    else -> null
                }
            }
        )
}
