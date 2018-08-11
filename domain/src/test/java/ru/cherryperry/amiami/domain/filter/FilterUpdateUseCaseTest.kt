package ru.cherryperry.amiami.domain.filter

import org.junit.Test
import org.mockito.Mockito
import ru.cherryperry.amiami.domain.model.Filter
import ru.cherryperry.amiami.domain.repository.FilterRepository
import rx.Completable
import rx.Observable

class FilterUpdateUseCaseTest {

    @Test(expected = IllegalArgumentException::class)
    fun testUpdateNothing() {
        val repository = Mockito.mock(FilterRepository::class.java)
        FilterUpdateUseCase(repository).run(FilterUpdateParams())
            .test()
            .awaitTerminalEvent()
    }

    @Test
    fun testUpdateMin() {
        val repository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(repository.filter()).thenReturn(Observable.just(Filter(0, 2, "text")))
        Mockito.`when`(repository.setMin(1)).thenReturn(Completable.complete())
        FilterUpdateUseCase(repository).run(FilterUpdateParams(min = 1))
            .test()
            .assertCompleted()
            .awaitTerminalEvent()
        Mockito.verify(repository).setMin(1)
        Mockito.verify(repository, Mockito.never()).setMax(Mockito.anyInt())
        Mockito.verify(repository, Mockito.never()).setTerm(Mockito.anyString())
    }

    @Test
    fun testUpdateMinHigherThanMax() {
        val repository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(repository.filter()).thenReturn(Observable.just(Filter(0, 2, "text")))
        Mockito.`when`(repository.setMin(3)).thenReturn(Completable.complete())
        Mockito.`when`(repository.setMax(3)).thenReturn(Completable.complete())
        FilterUpdateUseCase(repository).run(FilterUpdateParams(min = 3))
            .test()
            .assertCompleted()
            .awaitTerminalEvent()
        Mockito.verify(repository).setMin(3)
        Mockito.verify(repository).setMin(3)
        Mockito.verify(repository, Mockito.never()).setTerm(Mockito.anyString())
    }

    @Test
    fun testUpdateMax() {
        val repository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(repository.filter()).thenReturn(Observable.just(Filter(0, 2, "text")))
        Mockito.`when`(repository.setMax(3)).thenReturn(Completable.complete())
        FilterUpdateUseCase(repository).run(FilterUpdateParams(max = 3))
            .test()
            .assertCompleted()
            .awaitTerminalEvent()
        Mockito.verify(repository, Mockito.never()).setMin(Mockito.anyInt())
        Mockito.verify(repository).setMax(3)
        Mockito.verify(repository, Mockito.never()).setTerm(Mockito.anyString())
    }

    @Test
    fun testUpdateMaxLowerThanMin() {
        val repository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(repository.filter()).thenReturn(Observable.just(Filter(3, 4, "text")))
        Mockito.`when`(repository.setMin(2)).thenReturn(Completable.complete())
        Mockito.`when`(repository.setMax(2)).thenReturn(Completable.complete())
        FilterUpdateUseCase(repository).run(FilterUpdateParams(max = 2))
            .test()
            .assertCompleted()
            .awaitTerminalEvent()
        Mockito.verify(repository).setMin(2)
        Mockito.verify(repository).setMax(2)
        Mockito.verify(repository, Mockito.never()).setTerm(Mockito.anyString())
    }

    @Test
    fun testUpdateText() {
        val repository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(repository.filter()).thenReturn(Observable.just(Filter(3, 4, "text")))
        Mockito.`when`(repository.setTerm("term")).thenReturn(Completable.complete())
        FilterUpdateUseCase(repository).run(FilterUpdateParams(term = "term"))
            .test()
            .assertCompleted()
            .awaitTerminalEvent()
        Mockito.verify(repository, Mockito.never()).setMin(Mockito.anyInt())
        Mockito.verify(repository, Mockito.never()).setMax(Mockito.anyInt())
        Mockito.verify(repository).setTerm("term")
    }

    @Test
    fun testException() {
        val error = Completable.error(IllegalArgumentException("error"))
        val repository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(repository.filter()).thenReturn(Observable.just(Filter(0, 2, "text")))
        Mockito.`when`(repository.setMin(3)).thenReturn(error)
        Mockito.`when`(repository.setMax(3)).thenReturn(error)
        FilterUpdateUseCase(repository).run(FilterUpdateParams(min = 3))
            .test()
            .assertError(IllegalArgumentException::class.java)
            .awaitTerminalEvent()
    }
}
