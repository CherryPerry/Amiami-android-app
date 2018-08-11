// java retention is required for dagger to work
@file:Suppress("DEPRECATED_JAVA_ANNOTATION")

package ru.cherryperry.amiami.domain.di

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.RUNTIME
import javax.inject.Qualifier

@Qualifier
@Retention(RUNTIME)
annotation class ApplicationVersion
