package ru.cherryperry.amiami.data.network.server

import com.google.gson.stream.JsonReader
import org.junit.Assert
import org.junit.Test
import java.io.StringReader

class ExchangeRatesGsonTypeAdapterTest {

    @Test
    fun testSuccess() {
        val reader = JsonReader(StringReader("""{"success":true,"rates":{"USD":0.5,"EUR":1.0}}"""))
        val adapter = ExchangeRatesGsonTypeAdapter()
        val rates = adapter.read(reader)
        Assert.assertEquals(0.5, rates.rates["USD"])
        Assert.assertEquals(1.0, rates.rates["EUR"])
    }

    @Test(expected = IllegalArgumentException::class)
    fun testFailed() {
        val reader = JsonReader(StringReader("""{"success":false,"rates":{"USD":0.5,"EUR":1.0}}"""))
        ExchangeRatesGsonTypeAdapter().read(reader)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testInvalid() {
        val reader = JsonReader(StringReader("""["USD", "EUR"]"""))
        ExchangeRatesGsonTypeAdapter().read(reader)
    }
}
