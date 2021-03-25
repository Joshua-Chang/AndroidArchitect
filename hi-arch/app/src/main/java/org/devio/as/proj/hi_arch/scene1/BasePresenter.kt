package org.devio.`as`.proj.hi_arch.scene1

open class BasePresenter <IView :BaseView>{
    protected var view: IView? = null
    fun attach(view: IView) {
        this.view = view
    }

    fun detach() {
        view = null
    }
}