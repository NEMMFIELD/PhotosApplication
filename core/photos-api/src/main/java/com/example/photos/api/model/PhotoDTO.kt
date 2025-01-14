package com.example.photos.api.model

import com.squareup.moshi.Json
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class SearchPhotosResponse(
	@Json(name="total")
	val total: Int,
	@Json(name="total_pages")
	val total_pages: Int,
	@Json(name="results")
	val results: List<Photo>
)

@Serializable
data class Photo(

	@Json(name="topic_submissions")
	val topicSubmissions: TopicSubmissions? = null,

	//@Json(name="current_user_collections")
	//@Contextual
	//val currentUserCollections: List<Any?>? = null,

	@Json(name="color")
	val color: String? = null,

	@Json(name="sponsorship")
	@Contextual
	val sponsorship: Any? = null,

	@Json(name="created_at")
	val createdAt: String? = null,

	@Json(name="description")
	@Contextual
	val description: Any? = null,

	@Json(name="public_domain")
	val publicDomain: Boolean? = null,

	@Json(name="urls")
	val urls: Urls? = null,

	@Json(name="alternative_slugs")
	val alternativeSlugs: AlternativeSlugs? = null,

	@Json(name="alt_description")
	val altDescription: String? = null,

	@Json(name="updated_at")
	val updatedAt: String? = null,

	@Json(name="downloads")
	val downloads: Int? = null,

	@Json(name="asset_type")
	val assetType: String? = null,

	@Json(name="links")
	val links: Links? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="slug")
	val slug: String? = null,

	@Json(name="views")
	val views: Int? = null,

	@Json(name="height")
	val height: Int? = null,

	@Json(name="likes")
	val likes: Int? = null,

	//@Json(name="topics")
//	@Contextual
	//val topics: List<Any?>? = null,

	@Json(name="liked_by_user")
	val likedByUser: Boolean? = null,

	@Json(name="tags")
	val tags: List<TagsItem?>? = null,

	@Json(name="meta")
	val meta: Meta? = null,

	@Json(name="width")
	val width: Int? = null,

	@Json(name="blur_hash")
	val blurHash: String? = null,

	@Json(name="location")
	val location: Location? = null,

	@Json(name="promoted_at")
	val promotedAt: String? = null,

	@Json(name="user")
	val user: User? = null,

	//@Json(name="breadcrumbs")
	//@Contextual
	//val breadcrumbs: List<Any?>? = null,

	@Json(name="exif")
	val exif: Exif? = null
)

@Serializable
data class TagsItem(

	@Json(name="type")
	val type: String? = null,

	@Json(name="title")
	val title: String? = null
)

@Serializable
data class Position(

	@Json(name="latitude")
	val latitude: Double? = null,

	@Json(name="longitude")
	val longitude: Double? = null
)

@Serializable
data class Location(

	@Json(name="country")
	val country: String? = null,

	@Json(name="city")
	@Contextual
	val city: Any? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="position")
	val position: Position? = null
)

@Serializable
data class TopicSubmissions(
	@Contextual
	val any: Any? = null
)

@Serializable
data class Urls(

	@Json(name="small")
	val small: String? = null,

	@Json(name="small_s3")
	val smallS3: String? = null,

	@Json(name="thumb")
	val thumb: String? = null,

	@Json(name="raw")
	val raw: String? = null,

	@Json(name="regular")
	val regular: String? = null,

	@Json(name="full")
	val full: String? = null
)

@Serializable
data class Meta(

	@Json(name="index")
	val index: Boolean? = null
)

@Serializable
data class ProfileImage(

	@Json(name="small")
	val small: String? = null,

	@Json(name="large")
	val large: String? = null,

	@Json(name="medium")
	val medium: String? = null
)

@Serializable
data class Links(

	@Json(name="followers")
	val followers: String? = null,

	@Json(name="portfolio")
	val portfolio: String? = null,

	@Json(name="following")
	val following: String? = null,

	@Json(name="self")
	val self: String? = null,

	@Json(name="html")
	val html: String? = null,

	@Json(name="photos")
	val photos: String? = null,

	@Json(name="likes")
	val likes: String? = null,

	@Json(name="download")
	val download: String? = null,

	@Json(name="download_location")
	val downloadLocation: String? = null
)

@Serializable
data class AlternativeSlugs(

	@Json(name="de")
	val de: String? = null,

	@Json(name="ko")
	val ko: String? = null,

	@Json(name="pt")
	val pt: String? = null,

	@Json(name="ja")
	val ja: String? = null,

	@Json(name="en")
	val en: String? = null,

	@Json(name="it")
	val it: String? = null,

	@Json(name="fr")
	val fr: String? = null,

	@Json(name="es")
	val es: String? = null
)

@Serializable
data class User(

	@Json(name="total_photos")
	val totalPhotos: Int? = null,

	@Json(name="accepted_tos")
	val acceptedTos: Boolean? = null,

	@Json(name="social")
	val social: Social? = null,

	@Json(name="twitter_username")
	@Contextual
	val twitterUsername: Any? = null,

	@Json(name="last_name")
	val lastName: String? = null,

	@Json(name="bio")
	val bio: String? = null,

	@Json(name="total_likes")
	val totalLikes: Int? = null,

	@Json(name="portfolio_url")
	@Contextual
	val portfolioUrl: Any? = null,

	@Json(name="profile_image")
	val profileImage: ProfileImage? = null,

	@Json(name="updated_at")
	val updatedAt: String? = null,

	@Json(name="total_promoted_illustrations")
	val totalPromotedIllustrations: Int? = null,

	@Json(name="for_hire")
	val forHire: Boolean? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="total_promoted_photos")
	val totalPromotedPhotos: Int? = null,

	@Json(name="location")
	@Contextual
	val location: Any? = null,

	@Json(name="links")
	val links: Links? = null,

	@Json(name="total_collections")
	val totalCollections: Int? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="total_illustrations")
	val totalIllustrations: Int? = null,

	@Json(name="first_name")
	val firstName: String? = null,

	@Json(name="instagram_username")
	val instagramUsername: String? = null,

	@Json(name="username")
	val username: String? = null
)

@Serializable
data class Exif(

	@Json(name="exposure_time")
	val exposureTime: String? = null,

	@Json(name="aperture")
	val aperture: String? = null,

	@Json(name="focal_length")
	val focalLength: String? = null,

	@Json(name="iso")
	val iso: Int? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="model")
	val model: String? = null,

	@Json(name="make")
	val make: String? = null
)

@Serializable
data class Social(

	@Json(name="twitter_username")
	@Contextual
	val twitterUsername: Any? = null,

	@Json(name="paypal_email")
	@Contextual
	val paypalEmail: Any? = null,

	@Json(name="instagram_username")
	val instagramUsername: String? = null,

	@Json(name="portfolio_url")
	@Contextual
	val portfolioUrl: Any? = null
)
