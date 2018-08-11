package ru.cherryperry.amiami.domain.model

fun Sequence<Item>.sortAndInsertGroups(): Sequence<Model> {
    var lastGroup = 0L
    return this.sortedByDescending { it.time }
        .flatMap {
            if (lastGroup == 0L || lastGroup != it.time) {
                lastGroup = it.time
                sequenceOf(Group(it.time), it)
            } else {
                sequenceOf(it)
            }
        }
}
