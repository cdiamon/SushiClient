package net.pdmtrdv.sashimisake.view.menu

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_menu.*
import net.pdmtrdv.sashimisake.R
import net.pdmtrdv.sashimisake.model.MenuCategoryResponse
import net.pdmtrdv.sashimisake.model.Order
import net.pdmtrdv.sashimisake.view.BaseFragment


open class MenuFragment : BaseFragment(),
        CategoryRecyclerAdapter.OnCategoryClickCallback,
        DishRecyclerAdapter.OnDishClickCallback {

    private var listenerMenu: OnMenuFragmentListener? = null
    private lateinit var dishRecyclerAdapter: DishRecyclerAdapter
    private lateinit var categoryRecyclerAdapter: CategoryRecyclerAdapter
    private var menuList: List<MenuCategoryResponse>? = null
    private var dishList: ArrayList<MenuCategoryResponse.Dish> = ArrayList()
    private var inCategory = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridLayoutManager = GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)
        menuRecyclerView.layoutManager = gridLayoutManager

        compositeDisposable.add(sashimiApiService.getMenuAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            println(result)
                            fillCategoriesList(result)
                        },
                        { error -> error.printStackTrace() }
                ))
    }

    override fun onCategoryClicked(dishList: List<MenuCategoryResponse.Dish>) {
//        super.onCategoryClicked(position)
        println("CLICKED: $dishList")
        dishRecyclerAdapter = DishRecyclerAdapter(dishList, this)
        menuRecyclerView.adapter = dishRecyclerAdapter
        dishRecyclerAdapter.notifyDataSetChanged()
        inCategory = true
        listenerMenu?.onMenuFragmentCategoryPressed(inCategory)
    }

    override fun onBuyDishClicked(dish: MenuCategoryResponse.Dish) {
        dishList.add(dish)
        fabFrame.visibility = View.VISIBLE
        fabText.text = dishList.size.toString()
        activity?.title = "Меню              В корзине ${dishList.size} позиций"
        fabFrame.setOnClickListener {

            var sum = 0
            dishList.forEach {
                sum += it.price
            }

            Snackbar.make(activity!!.findViewById(android.R.id.content), "Заказать ${dishList.size} блюд на сумму $sum", Snackbar.LENGTH_LONG)
                    .setAction("Заказать", {
                        makeOrder()
                    })
                    .setActionTextColor(resources.getColor(R.color.colorAccent))
                    .show()
        }
    }

    private fun makeOrder() {

        val orderMap: HashMap<MenuCategoryResponse.Dish, Int> = HashMap()

        dishList.forEach {
            if (orderMap.containsKey(it)) {
                orderMap[it] = (orderMap[it] ?: 0) + 1
            } else {
                orderMap[it] = 1
            }
        }

        val orderList: ArrayList<Order> = ArrayList()
        orderMap.forEach {
            orderList.add(Order(it.value, it.key))
        }

        compositeDisposable.add(sashimiApiService.createNewOrder(orderList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    println(response)
                    Toast.makeText(context, "Заказ принят", Toast.LENGTH_LONG).show()
                    dishList.clear()
                    fabFrame.visibility = View.GONE
                    activity?.title = "Меню"
                }, { error -> error.printStackTrace() }))
    }

    private fun fillCategoriesList(menuList: List<MenuCategoryResponse>) {
        this.menuList = menuList
        categoryRecyclerAdapter = CategoryRecyclerAdapter(menuList, this)
        menuRecyclerView.adapter = categoryRecyclerAdapter
//                                categoryRecyclerAdapter.menuCategoriesList = result
        categoryRecyclerAdapter.notifyDataSetChanged()
    }

    open fun pressBack() {
        if (menuList != null) {
            fillCategoriesList(menuList!!)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMenuFragmentListener) {
            listenerMenu = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnMenuFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listenerMenu = null
    }

    interface OnMenuFragmentListener {
        fun onFragmentInteraction()
        fun onMenuFragmentCategoryPressed(inCategory: Boolean)
    }

    companion object {
        @JvmStatic
        fun newInstance(): MenuFragment {
            return MenuFragment()
        }
    }
}
