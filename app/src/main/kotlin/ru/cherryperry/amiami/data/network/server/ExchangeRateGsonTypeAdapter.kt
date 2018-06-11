package ru.cherryperry.amiami.data.network.server

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class ExchangeRateGsonTypeAdapter : TypeAdapter<ExchangeRate>() {

    override fun read(reader: JsonReader): ExchangeRate {
        val values = HashMap<String, Double>()
        if (reader.peek() != JsonToken.BEGIN_OBJECT) {
            throw IllegalArgumentException("Response is not json object")
        }
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "success" -> {
                    val success = if (reader.peek() == JsonToken.BOOLEAN) {
                        reader.nextBoolean()
                    } else {
                        reader.skipValue()
                        false
                    }
                    if (!success) {
                        throw IllegalArgumentException("Success is not TRUE")
                    }
                }
                "rates" -> {
                    reader.beginObject()
                    while (reader.hasNext()) {
                        val currency = reader.nextName()
                        val token = reader.peek()
                        if (token == JsonToken.NUMBER) {
                            val value = reader.nextDouble()
                            values[currency] = value
                        }
                    }
                    reader.endObject()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return ExchangeRate(values)
    }

    override fun write(writer: JsonWriter, value: ExchangeRate?) {
        throw NotImplementedError()
    }
}