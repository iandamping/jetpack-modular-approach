package com.ian.junemon.spe_learning_mvvm.di

import androidx.room.Room
import com.ian.junemon.spe_learning_mvvm.data.MovieDatabase
import org.koin.dsl.module

/**
 *
Created by Ian Damping on 20/09/2019.
Github = https://github.com/iandamping
 */
val databaseModule = module {
    // Room Database instance
    single {
        Room.databaseBuilder(get(), MovieDatabase::class.java, "LocalMovieDatabase").fallbackToDestructiveMigration()
                .build()
    }
    // localDao instance (get instance from MovieDatabase)
    single { get<MovieDatabase>().movieDao() }
}