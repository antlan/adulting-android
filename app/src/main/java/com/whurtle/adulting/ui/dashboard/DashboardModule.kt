package com.whurtle.adulting.ui.dashboard

import org.koin.core.qualifier.named
import org.koin.dsl.module

enum class Scope {
    DASHBOARD_MODULE_SCOPE
}

val dashboardModule = module {

    //scoped router hehe
    scope(named(Scope.DASHBOARD_MODULE_SCOPE.name)) {
        scoped<IDashboardRouter> { DashboardRouter() }
        scoped<IDashboardPresenter> { DashboardPresenter() }
        scoped<IDashboardInteractor> { DashboardInteractor() }
    }
}