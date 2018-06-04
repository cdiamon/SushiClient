package net.pdmtrdv.sashimisake.view.wait

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_wait.*
import net.pdmtrdv.sashimisake.R
import net.pdmtrdv.sashimisake.model.Model
import net.pdmtrdv.sashimisake.view.BaseFragment
import java.util.concurrent.TimeUnit

class WaitFragment : BaseFragment() {
    private var listener: OnWaitFragmentListener? = null
    private lateinit var cookingRecyclerAdapter: OrdersRecyclerAdapter
    private lateinit var readyRecyclerAdapter: OrdersRecyclerAdapter

    private var waitingOrdersList = ArrayList<Model.OrdersResponse>()
    private var readyOrdersList = ArrayList<Model.OrdersResponse>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wait, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getWaitingList()

    }

    private fun getWaitingList() {


        compositeDisposable.add(sashimiApiService.getOrdersList()
                .timeInterval(TimeUnit.SECONDS)
                .repeatWhen { result -> result.delay(7, TimeUnit.SECONDS) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    fillWaitingList(result.value())
                    Toast.makeText(context, "обновлено", Toast.LENGTH_SHORT).show()
                }, { error -> error.printStackTrace() }))

    }

    private fun fillWaitingList(waitingList: List<Model.OrdersResponse>) {

        waitingOrdersList.clear()
        readyOrdersList.clear()

        waitingList.forEach {
            if (it.status == "COOKING") {
                waitingOrdersList.add(it)
            } else if (it.status == "READY") {
                readyOrdersList.add(it)
            }
        }

        cookingRecyclerView.layoutManager = LinearLayoutManager(context)
        cookingRecyclerAdapter = OrdersRecyclerAdapter(waitingOrdersList)
        cookingRecyclerView.adapter = cookingRecyclerAdapter

        readyRecyclerView.layoutManager = LinearLayoutManager(context)
        readyRecyclerAdapter = OrdersRecyclerAdapter(readyOrdersList)
        readyRecyclerView.adapter = readyRecyclerAdapter

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnWaitFragmentListener
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnWaitFragmentListener {
        fun onFragmentInteraction()
    }

    companion object {
        @JvmStatic
        fun newInstance(): WaitFragment {
            return WaitFragment()
        }
    }
}
