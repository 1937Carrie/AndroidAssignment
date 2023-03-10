package sdumchykov.androidApp.domain.utils

data class Response<out T>(val status: Status, val data: T?, val message: Int?) {
    companion object {
        fun <T> success(data: T?) = Response(Status.SUCCESS, data, null)
        fun <T> error(message: Int?, data: T?) = Response(Status.ERROR, data, message)
        fun <T> loading(data: T?) = Response(Status.LOADING, data, null)
    }
}