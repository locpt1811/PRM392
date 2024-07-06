package com.example.finalproject.utils

// Preferences Keys
const val MY_PREF = "app_preferences"
const val REMEMBER_ME = "remember_me_pref"
const val MY_THEME = "my_theme_pref"
const val ACCESS_TOKEN = "access_token_pref"
const val FIRST_TIME_LAUNCH = "first_time_launch_pref"


const val BASE_URL = "https://fakestoreapi.com/"
const val DELIVERY_FEE = 2.00

object EndPoints {
    const val CATEGORY = "products/categories"
    const val PRODUCT = "products"
}

object Storage {
    const val USER_PROFILE_IMG = "user_profile_images"
}

object Firestore {
    const val COLLECTION_KEY = "user_details"
    const val FCM_COLLECTION_KEY = "fcm_tokens"
}

object NotificationChannelIds {
    const val SHOPPING_NOTIFICATION = "shopping_notification_channel"
}