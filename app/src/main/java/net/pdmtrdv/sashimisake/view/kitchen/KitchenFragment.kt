package net.pdmtrdv.sashimisake.view.kitchen

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_kitchen.*
import net.pdmtrdv.sashimisake.R
import net.pdmtrdv.sashimisake.model.Model
import net.pdmtrdv.sashimisake.view.BaseFragment

class KitchenFragment : BaseFragment(), KitchenRecyclerAdapter.OnKitchenClickCallback {

    private var listener: OnKitchenFragmentListener? = null
    private lateinit var kitchenRecyclerAdapter: KitchenRecyclerAdapter
    private var kitchenOrdersList = ArrayList<Model.OrdersResponse>()
    private var waiterOrdersList = ArrayList<Model.OrdersResponse>()
    private var isWaiter = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_kitchen, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(context)
        kitchenRecyclerView.layoutManager = linearLayoutManager
        toggleKitchenButton.setOnCheckedChangeListener { buttonView, isWaiter ->
            this.isWaiter = isWaiter
            fillKitchenList()
            println(isWaiter)
        }

        getKitchenList()
    }

    private fun getKitchenList() {

        compositeDisposable.add(sashimiApiService.getOrdersList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    kitchenOrdersList.clear()
                    waiterOrdersList.clear()
                    result.forEach {
                        if (it.status == "COOKING") {
                            kitchenOrdersList.add(it)
                        } else if (it.status == "READY") {
                            waiterOrdersList.add(it)
                        }
                    }

                    fillKitchenList()
                }, { error -> error.printStackTrace() }))

    }

    override fun onKitchenOrderClicked() {
        Toast.makeText(context, "Для смены статуса нужно долгое нажатие", Toast.LENGTH_SHORT).show()
    }

    override fun onKitchenOrderLongClicked(ordersResponse: Model.OrdersResponse) {
        compositeDisposable.add(sashimiApiService.changeOrderStatus(ordersResponse.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    Toast.makeText(context, "Готово", Toast.LENGTH_LONG).show()
                    getKitchenList()
                }, {
                    it.printStackTrace()
                }))
    }

    private fun fillKitchenList() {
        if (toggleKitchenButton.isChecked) {
            kitchenRecyclerAdapter = KitchenRecyclerAdapter(waiterOrdersList, this)
            kitchenRecyclerView.adapter = kitchenRecyclerAdapter
            kitchenRecyclerAdapter.notifyDataSetChanged()
        } else {
            kitchenRecyclerAdapter = KitchenRecyclerAdapter(kitchenOrdersList, this)
            kitchenRecyclerView.adapter = kitchenRecyclerAdapter
            kitchenRecyclerAdapter.notifyDataSetChanged()
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnKitchenFragmentListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnKitchenFragmentListener {
        fun onFragmentInteraction()
    }

    companion object {
        @JvmStatic
        fun newInstance(): KitchenFragment {
            return KitchenFragment()
        }
    }
}
