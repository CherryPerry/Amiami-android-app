package ru.cherryperry.amiami.domain.update

import io.reactivex.Maybe
import org.junit.Test
import org.mockito.Mockito
import ru.cherryperry.amiami.domain.model.UpdateInfo
import ru.cherryperry.amiami.domain.model.Version
import ru.cherryperry.amiami.domain.repository.UpdateRepository

class CheckUpdateUseCaseTest {

    @Test
    fun testHasUpdateHigherThanCurrent() {
        val repository = Mockito.mock(UpdateRepository::class.java)
        val update = UpdateInfo(Version.fromString("1.1"), "", "")
        Mockito.`when`(repository.latestRelease()).thenReturn(Maybe.just(update))
        val useCase = CheckUpdateUseCase(repository, "1.0")
        useCase.run(Unit)
            .test()
            .assertResult(update)
            .assertComplete()
            .awaitTerminalEvent()
    }

    @Test
    fun testHasUpdateLowerThanCurrent() {
        val repository = Mockito.mock(UpdateRepository::class.java)
        val update = UpdateInfo(Version.fromString("1.0"), "", "")
        Mockito.`when`(repository.latestRelease()).thenReturn(Maybe.just(update))
        val useCase = CheckUpdateUseCase(repository, "1.1")
        useCase.run(Unit)
            .test()
            .assertNoValues()
            .assertComplete()
            .awaitTerminalEvent()
    }

    @Test
    fun testNoUpdate() {
        val repository = Mockito.mock(UpdateRepository::class.java)
        Mockito.`when`(repository.latestRelease()).thenReturn(Maybe.empty())
        val useCase = CheckUpdateUseCase(repository, "1.1")
        useCase.run(Unit)
            .test()
            .assertNoValues()
            .assertComplete()
            .awaitTerminalEvent()
    }
}
