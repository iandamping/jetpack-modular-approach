package com.ian.junemon.spe_learning_mvvm.api

import com.ian.app.helper.util.logE
import com.ian.junemon.spe_learning_mvvm.data.ResultToConsume
import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): ResultToConsume<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return ResultToConsume.success(body)
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): ResultToConsume<T> {
        logE(message)
        return ResultToConsume.error("Network call has failed for a following reason: $message")
    }

}