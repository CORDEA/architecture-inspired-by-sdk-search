package jp.cordea.sdksearcharchitecturedemo

data class ColorState(
        val initialize: ColorSynchronizer.SyncState = ColorSynchronizer.SyncState.SYNC,
        val filter: ColorSynchronizer.SyncState = ColorSynchronizer.SyncState.SYNC
)
