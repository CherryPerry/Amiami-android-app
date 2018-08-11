package ru.cherryperry.amiami.domain.model

data class Version(
    val major: Int = 0,
    val minor: Int = 0,
    val patch: Int = 0
) : Comparable<Version> {

    companion object {
        fun fromString(string: String): Version {
            val regex = Regex("""^(\d+\.)?(\d+\.)?(\*|\d+)${'$'}""")
            if (regex.matches(string)) {
                var major = 0
                var minor = 0
                var patch = 0
                string.split('.').forEachIndexed { index, group ->
                    val value = group.trimEnd('.').toInt()
                    when (index) {
                        0 -> major = value
                        1 -> minor = value
                        2 -> patch = value
                    }
                }
                return Version(major, minor, patch)
            }
            throw IllegalArgumentException("Invalid version string ($string)")
        }
    }

    @Suppress("ReturnCount")
    override fun compareTo(other: Version): Int {
        major.compareTo(other.major).also {
            if (it != 0) return it
        }
        minor.compareTo(other.minor).also {
            if (it != 0) return it
        }
        patch.compareTo(other.patch).also {
            if (it != 0) return it
        }
        return 0
    }

    override fun toString() = "$major.$minor.$patch"
}
