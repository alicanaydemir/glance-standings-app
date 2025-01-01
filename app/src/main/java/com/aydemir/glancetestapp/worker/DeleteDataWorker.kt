package com.aydemir.glancetestapp.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.aydemir.glancetestapp.data.StandingsRepositoryImp

class DeleteDataWorker(private val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        StandingsRepositoryImp.get(appContext).deleteStandingsNoResult()
        return Result.Success.success()
    }
}