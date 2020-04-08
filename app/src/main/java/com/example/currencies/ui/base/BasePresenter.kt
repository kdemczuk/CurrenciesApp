package com.example.currencies.ui.base

abstract class BasePresenter<ViewType : BaseView> {

    var view: ViewType? = null
        private set

    fun isViewAttached(): Boolean {
        return view != null
    }


    fun attachView(view: BaseView) {
        this.view = view as ViewType
    }

    fun detachView() {
        this.view = null
    }




}