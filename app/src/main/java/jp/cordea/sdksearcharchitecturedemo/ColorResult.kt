package jp.cordea.sdksearcharchitecturedemo

sealed class ColorResult

sealed class ColorFetchResult : ColorResult() {
    data class Success(val colors: List<Color>) : ColorFetchResult()
    object Failure : ColorFetchResult()
}
