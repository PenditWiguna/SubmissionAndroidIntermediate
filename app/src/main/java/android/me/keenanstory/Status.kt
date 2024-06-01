package android.me.keenanstory

sealed class Status <out R> private constructor() {
    data object Process : Status<Nothing>()
    data class Fail (val error: String) : Status<Nothing>()
    data class Done<out T>(val data: T) : Status<T>()
}
