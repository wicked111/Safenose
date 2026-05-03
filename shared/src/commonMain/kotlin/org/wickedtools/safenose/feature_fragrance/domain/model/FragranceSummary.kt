package org.wickedtools.safenose.feature_fragrance.domain.model

data class FragranceSummary(
    val id: Long,
    val name: String,
    val brandName: String,
    val concentration: String?
)
