package ru.cherryperry.amiami.domain.update

import org.junit.Test
import org.mockito.Mockito
import ru.cherryperry.amiami.domain.model.UpdateInfo
import ru.cherryperry.amiami.domain.model.Version
import ru.cherryperry.amiami.domain.repository.UpdateRepository
import rx.Observable

class CheckUpdateUseCaseTest {

    @Test
    fun testHasUpdateHigherThanCurrent() {
        val repository = Mockito.mock(UpdateRepository::class.java)
        val update = UpdateInfo(Version.fromString("1.1"), "", "")
        Mockito.`when`(repository.latestRelease()).thenReturn(Observable.just(update))
        val useCase = CheckUpdateUseCase(repository, "1.0")
        useCase.run(Any())
            .test()
            .assertResult(update)
            .assertCompleted()
            .awaitTerminalEvent()
    }

    @Test
    fun testHasUpdateLowerThanCurrent() {
        val repository = Mockito.mock(UpdateRepository::class.java)
        val update = UpdateInfo(Version.fromString("1.0"), "", "")
        Mockito.`when`(repository.latestRelease()).thenReturn(Observable.just(update))
        val useCase = CheckUpdateUseCase(repository, "1.1")
        useCase.run(Any())
            .test()
            .assertNoValues()
            .assertCompleted()
            .awaitTerminalEvent()
    }

    @Test
    fun testNoUpdate() {
        val repository = Mockito.mock(UpdateRepository::class.java)
        Mockito.`when`(repository.latestRelease()).thenReturn(Observable.empty())
        val useCase = CheckUpdateUseCase(repository, "1.1")
        useCase.run(Any())
            .test()
            .assertNoValues()
            .assertCompleted()
            .awaitTerminalEvent()
    }
}
