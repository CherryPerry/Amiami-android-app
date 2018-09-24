package ru.cherryperry.amiami.domain.filter

import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Test
import org.mockito.Mockito
import ru.cherryperry.amiami.domain.model.Filter
import ru.cherryperry.amiami.domain.repository.FilterRepository

class FilterUpdateUseCaseTest {

    @Test
    fun testUpdateMin() {
        val repository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(repository.filter()).thenReturn(Flowable.just(Filter(0, 2, "text")))
        Mockito.`when`(repository.setMin(1)).thenReturn(Completable.complete())
        FilterUpdateUseCase(repository).run(MinFilterUpdateParams(1))
            .test()
            .await()
            .assertComplete()
        Mockito.verify(repository).setMin(1)
        Mockito.verify(repository, Mockito.never()).setMax(Mockito.anyInt())
        Mockito.verify(repository, Mockito.never()).setTerm(Mockito.anyString())
    }

    @Test
    fun testUpdateMax() {
        val repository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(repository.filter()).thenReturn(Flowable.just(Filter(0, 2, "text")))
        Mockito.`when`(repository.setMax(3)).thenReturn(Completable.complete())
        FilterUpdateUseCase(repository).run(MaxFilterUpdateParams(3))
            .test()
            .await()
            .assertComplete()
        Mockito.verify(repository, Mockito.never()).setMin(Mockito.anyInt())
        Mockito.verify(repository).setMax(3)
        Mockito.verify(repository, Mockito.never()).setTerm(Mockito.anyString())
    }

    @Test
    fun testUpdateText() {
        val repository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(repository.filter()).thenReturn(Flowable.just(Filter(3, 4, "text")))
        Mockito.`when`(repository.setTerm("term")).thenReturn(Completable.complete())
        FilterUpdateUseCase(repository).run(TermFilterUpdateParams("term"))
            .test()
            .await()
            .assertComplete()
        Mockito.verify(repository, Mockito.never()).setMin(Mockito.anyInt())
        Mockito.verify(repository, Mockito.never()).setMax(Mockito.anyInt())
        Mockito.verify(repository).setTerm("term")
    }

    @Test
    fun testException() {
        val error = Completable.error(IllegalArgumentException("error"))
        val repository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(repository.filter()).thenReturn(Flowable.just(Filter(0, 2, "text")))
        Mockito.`when`(repository.setMin(3)).thenReturn(error)
        Mockito.`when`(repository.setMax(3)).thenReturn(error)
        FilterUpdateUseCase(repository).run(MinFilterUpdateParams(3))
            .test()
            .await()
            .assertError(IllegalArgumentException::class.java)
    }
}
