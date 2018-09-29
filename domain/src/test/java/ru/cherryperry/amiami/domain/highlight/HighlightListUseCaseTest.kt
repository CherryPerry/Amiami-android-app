package ru.cherryperry.amiami.domain.highlight

import io.reactivex.Flowable
import org.junit.Test
import org.mockito.Mockito
import ru.cherryperry.amiami.domain.model.HighlightConfiguration
import ru.cherryperry.amiami.domain.model.HighlightRule
import ru.cherryperry.amiami.domain.repository.HighlightRepository

class HighlightListUseCaseTest {

    @Test
    fun testAlphabeticalOrderIgnoreCase() {
        // list should be in alphabetical order
        val repository = Mockito.mock(HighlightRepository::class.java)
        Mockito.`when`(repository.configuration()).thenReturn(
            Flowable.just(
                HighlightConfiguration(listOf(
                    HighlightRule(rule = "Bbb"),
                    HighlightRule(rule = "aaa"),
                    HighlightRule(rule = "ccc")
                ))))
        val useCase = HighlightListUseCase(repository)
        useCase.run(Unit)
            .test()
            .await()
            .assertValue {
                it.map { it.rule } == listOf("aaa", "Bbb", "ccc")
            }
            .dispose()
    }
}
