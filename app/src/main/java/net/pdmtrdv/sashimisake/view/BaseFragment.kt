package net.pdmtrdv.sashimisake.view

import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import net.pdmtrdv.sashimisake.SashimiApiService

open class BaseFragment : Fragment() {

    open val sashimiApiService by lazy {
        SashimiApiService.create()
    }

    open var compositeDisposable = CompositeDisposable()

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

}