package jp.cordea.sdksearcharchitecturedemo

sealed class ColorAction {
    object Initialize : ColorAction()
    class Filter(val query: String) : ColorAction()
}
