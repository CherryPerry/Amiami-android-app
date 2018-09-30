package ru.cherryperry.amiami.data.network.server

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import ru.cherryperry.amiami.domain.model.ExchangeRates

/**
 * Parse exchange rates json directly to domain entity.
 * No need to use annotations and separate nullable data entity.
 */
class ExchangeRatesGsonTypeAdapter : TypeAdapter<ExchangeRates>() {

    companion object {
        private const val FIELD_SUCCESS = "success"
        private const val FIELD_RATES = "rates"
    }

    override fun read(reader: JsonReader): ExchangeRates {
        val values = HashMap<String, Double>()
        if (reader.peek() != JsonToken.BEGIN_OBJECT) {
            throw IllegalArgumentException("Response is not json object")
        }
        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                FIELD_SUCCESS -> readSuccess(reader)
                FIELD_RATES -> readRates(reader, values)
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return ExchangeRates(values)
    }

    /**
     * Read [FIELD_SUCCESS] and check if it is really success.
     * Throw exception if otherwise.
     */
    private fun readSuccess(reader: JsonReader) {
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

    /**
     * Read [FIELD_RATES] and skip any non-number pairs.
     */
    private fun readRates(reader: JsonReader, values: HashMap<String, Double>) {
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

    override fun write(writer: JsonWriter, value: ExchangeRates?) {
        // there is no need in write implementation
        throw NotImplementedError()
    }
}
