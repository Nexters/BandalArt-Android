package com.nexters.bandalart.android.core.data.util

class ApiException(
  val statusCode: Int,
  override val message: String,
) : RuntimeException()