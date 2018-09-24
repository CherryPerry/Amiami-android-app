package ru.cherryperry.amiami.core

import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

/**
 * Create single thread scheduler with background priority.
 */
fun createBackgroundThreadScheduler(name: String) = Schedulers.from(Executors.newSingleThreadExecutor {
    BackgroundPriorityThread(it, name)
})
