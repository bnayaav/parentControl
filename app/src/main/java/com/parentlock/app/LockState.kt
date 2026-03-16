package com.parentlock.app

object LockState {
    @Volatile
    var isLocked: Boolean = true
}
