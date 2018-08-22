package ru.cherryperry.amiami.data.mapping

import ru.cherryperry.amiami.data.db.DbHighlightRule
import ru.cherryperry.amiami.domain.model.HighlightRule
import ru.cherryperry.amiami.mapping.Mapping

class DbRuleToRuleMapping : Mapping<DbHighlightRule, HighlightRule> {

    override fun map(from: DbHighlightRule): HighlightRule =
        HighlightRule(from.id ?: 0, from.value, from.regex)
}
