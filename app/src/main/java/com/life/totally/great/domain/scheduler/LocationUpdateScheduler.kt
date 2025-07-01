package com.life.totally.great.domain.scheduler

interface LocationUpdateScheduler {
    fun schedulePeriodic()
    fun cancel() // not Used now, but may be used in future
}