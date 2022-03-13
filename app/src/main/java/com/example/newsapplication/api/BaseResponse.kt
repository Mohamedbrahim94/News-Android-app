package com.example.newsapplication.api

open class BaseResponse(
    var status: String? = null,
    var message: String? = null, var code: String? = null
)