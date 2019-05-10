package com.shineapp.cars.errors

import android.content.Context
import com.shineapp.cars.R

open class AppException(
    val errorCode: ErrorCode = ErrorCode.UNKNOWN_ERROR,
    message: String? = null,
    cause: Throwable? = null
) : Throwable(message ?: errorCode.name, cause) {

    protected open fun getMessageInternal(context: Context): String = context.getString(errorCode.resId)

    private fun getFullMessage(context: Context): String = buildCauseMessage(this, getMessage(context))


    fun getMessage(context: Context, fullMessage: Boolean = false): String {
        return if (fullMessage) getFullMessage(context) else getMessageInternal(context)
    }
}

fun buildCauseMessage(error: Throwable, resume: String? = null): String{

    val msg = StringBuilder()
    if(resume != null){
        msg.append("$resume\n")
    }
    var m = error.message
    if (!m.isNullOrBlank()) {
        msg.append("$m\n")
    }
    var cause = error.cause
    while (cause != null) {
        m = cause.message
        if (!m.isNullOrBlank()) {
            msg.append("$m\n")
        }
        cause = cause.cause
    }
    if (msg.isEmpty()) {
        msg.append(error.toString())
    }
    return msg.toString()
}

enum class ErrorCode(
    val resId: Int
) {
    UNKNOWN_ERROR(R.string.error_unknown),
    NETWORK_ERROR(R.string.network_error),
    SERVER_ERROR(R.string.server_error),
}


