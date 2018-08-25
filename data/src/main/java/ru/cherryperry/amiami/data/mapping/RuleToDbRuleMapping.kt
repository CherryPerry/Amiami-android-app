package ru.cherryperry.amiami.data.mapping

import ru.cherryperry.amiami.data.db.DbHighlightRule
import ru.cherryperry.amiami.domain.model.HighlightRule
import ru.cherryperry.amiami.mapping.Mapping

class RuleToDbRuleMapping : Mapping<HighlightRule, DbHighlightRule> {

    override fun map(from: HighlightRule): DbHighlightRule =
        DbHighlightRule(from.id, from.rule, from.regex)
}
