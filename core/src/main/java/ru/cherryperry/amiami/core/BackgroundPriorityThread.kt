package ru.cherryperry.amiami.core

import android.os.Process

/**
 * [Thread] with android priority of [Process.THREAD_PRIORITY_BACKGROUND].
 */
class BackgroundPriorityThread(
    target: Runnable,
    name: String
) : Thread(target, name) {

    override fun run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
        super.run()
    }
}
