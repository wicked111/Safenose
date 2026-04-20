package org.wickedtools.safenose.feature_fragrance.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MultiSearchResponseDto(
    val results: List<SearchResultDto>
)

// Represents one of the result blocks (for fragrances, brands, etc.)
@Serializable
data class SearchResultDto(
    val hits: List<FragranceHitDto>,
    val indexUid: String
)

// Represents a single fragrance item in the 'hits' list
@Serializable
data class FragranceHitDto(
    val id: Int,
    val name: String,
    val brand: BrandDto,
    val notes: List<NoteDto>,
    val image: ImageDto
)

// The nested objects we need
@Serializable
data class BrandDto(
    val name: String
)

@Serializable
data class NoteDto(
    val name: String
)

@Serializable
data class ImageDto(
    val url: String // This is the image URL we want!
)
