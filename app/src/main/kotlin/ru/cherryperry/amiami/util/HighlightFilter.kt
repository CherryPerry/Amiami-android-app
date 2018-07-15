package ru.cherryperry.amiami.util

import ru.cherryperry.amiami.model.Item
import java.util.ArrayList
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

class HighlightFilter(set: Collection<String>) {

    private val highlights = ArrayList<Any>()

    init {
        for (highlight in set)
            try {
                this.highlights.add(Pattern.compile(highlight, Pattern.CASE_INSENSITIVE or Pattern.MULTILINE))
            } catch (exception: PatternSyntaxException) {
                this.highlights.add(highlight)
            }
    }

    fun isHighlighted(item: Item): Boolean {
        for (highlight in highlights) {
            if (highlight is Pattern) {
                if (highlight.matcher(item.name!!).find())
                    return true
            } else if (highlight is String) {
                if (item.name!!.contains(highlight))
                    return true
            }
        }
        return false
    }
}