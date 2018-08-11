package ru.cherryperry.amiami.data.mapping

import ru.cherryperry.amiami.domain.model.Price
import ru.cherryperry.amiami.mapping.Mapping
import java.text.NumberFormat
import java.text.ParseException
import java.util.Locale

class JsonPriceToPriceMapping : Mapping<String, Price> {

    private val regex = Regex("\\d+(,\\d+)?")
    private val numberFormat = NumberFormat.getIntegerInstance(Locale.UK)

    override fun map(from: String): Price {
        try {
            regex.find(from)?.let {
                it.value.let {
                    return Price(numberFormat.parse(it).toDouble())
                }
            }
        } catch (parseException: ParseException) {
            // ignored
        }
        return Price(0.0)
    }
}
