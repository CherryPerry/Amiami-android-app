package ru.cherryperry.android

import org.gradle.api.Plugin
import org.gradle.api.Project

import javax.annotation.Nonnull

class AndroidLibraryPlugin extends BasePlugin implements Plugin<Project> {

    @Override
    void apply(@Nonnull Project target) {
        target.apply plugin: 'com.android.library'
        setupDefaultParams(target)
    }
}
