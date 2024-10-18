package id.djaka.splitbillapp.di

import id.djaka.splitbillapp.home.HomeScreenModel
import id.djaka.splitbillapp.input.assign.InputAssignItemScreenModel
import id.djaka.splitbillapp.input.camera.InputCameraScreenModel
import id.djaka.splitbillapp.input.item.InputItemScreenModel
import id.djaka.splitbillapp.input.result.InputResultScreenModel
import id.djaka.splitbillapp.service.bill.BillRepository
import id.djaka.splitbillapp.service.datastore.DataStoreService
import id.djaka.splitbillapp.service.trip.TripRepository
import id.djaka.splitbillapp.trip.TripScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::DataStoreService)
    singleOf(::BillRepository)
    singleOf(::TripRepository)
}

val screenModelModule = module {
    factory { InputCameraScreenModel(get()) }
    factory { InputItemScreenModel(get()) }
    factory { InputAssignItemScreenModel(get()) }
    factoryOf(::InputResultScreenModel)
    factoryOf(::HomeScreenModel)
    factoryOf(::TripScreenModel)
}