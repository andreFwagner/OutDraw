package com.example.android.outdraw

import android.app.Application
import com.example.android.outdraw.database.LocalDB
import com.example.android.outdraw.gallery.GalleryViewModel
import com.example.android.outdraw.repository.Repository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * ApplicationClass introducing Koin Modules for further use
 */

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val myModule = module {
            // Declare a ViewModel - be later inject into Fragment with dedicated injector using by viewModel()
            viewModel {
                GalleryViewModel(
                    get(),
                    get() as Repository
                )
            }
            single { Repository(get()) }
            single { LocalDB.createDatabaseDao(this@MyApp) }
        }

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(myModule))
        }
    }
}
