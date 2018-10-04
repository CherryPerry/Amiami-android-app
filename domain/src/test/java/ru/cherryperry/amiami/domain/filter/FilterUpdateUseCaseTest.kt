package ru.cherryperry.amiami.domain.filter

import io.reactivex.Completable
import org.junit.Test
import org.mockito.Mockito
import ru.cherryperry.amiami.domain.repository.FilterRepository

class FilterUpdateUseCaseTest {

    @Test
    fun testUpdateMin() {
        val repository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(repository.setMin(1)).thenReturn(Completable.complete())
        FilterUpdateUseCase(repository)
            .run(MinFilterUpdateParams(1))
            .test()
            .await()
            .assertComplete()
            .dispose()
        Mockito.verify(repository).setMin(1)
    }

    @Test
    fun testUpdateMax() {
        val repository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(repository.setMax(3)).thenReturn(Completable.complete())
        FilterUpdateUseCase(repository)
            .run(MaxFilterUpdateParams(3))
            .test()
            .await()
            .assertComplete()
            .dispose()
        Mockito.verify(repository).setMax(3)
    }

    @Test
    fun testUpdateText() {
        val repository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(repository.setTerm("term")).thenReturn(Completable.complete())
        FilterUpdateUseCase(repository)
            .run(TermFilterUpdateParams("term"))
            .test()
            .await()
            .assertComplete()
            .dispose()
        Mockito.verify(repository).setTerm("term")
    }

    @Test
    fun testReset() {
        val repository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(repository.reset()).thenReturn(Completable.complete())
        FilterUpdateUseCase(repository)
            .run(ResetFilterUpdateParams)
            .test()
            .await()
            .assertComplete()
            .dispose()
        Mockito.verify(repository).reset()
    }

    @Test
    fun testException() {
        val error = Completable.error(IllegalArgumentException("error"))
        val repository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(repository.setMin(3)).thenReturn(error)
        FilterUpdateUseCase(repository)
            .run(MinFilterUpdateParams(3))
            .test()
            .await()
            .assertError(IllegalArgumentException::class.java)
            .dispose()
    }
}
