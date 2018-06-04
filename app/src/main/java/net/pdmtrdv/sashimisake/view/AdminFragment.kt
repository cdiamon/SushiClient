package net.pdmtrdv.sashimisake.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.CursorLoader
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_admin.*
import net.pdmtrdv.sashimisake.R
import net.pdmtrdv.sashimisake.model.MenuAddCategoryRequest
import net.pdmtrdv.sashimisake.model.MenuAddDishRequest
import net.pdmtrdv.sashimisake.model.ReportResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.nio.ByteBuffer


class AdminFragment : BaseFragment() {

    private var listener: OnAdminFragmentListener? = null
    val IMAGE_FOR_CATEGORY = 43
    val IMAGE_FOR_DISH = 44

    companion object {
        fun newInstance(): AdminFragment {
            return AdminFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addCategoryButton.setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), IMAGE_FOR_CATEGORY)
        }
        addDishButton.setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), IMAGE_FOR_DISH)
        }
        getStatisticsButton.setOnClickListener {
            getStatistics()
        }
    }

    private fun addCategory(id: Int, name: String) {

        compositeDisposable.add(sashimiApiService.addMenuCategory(MenuAddCategoryRequest(MenuAddCategoryRequest.Image(id), name))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()
                }, {
                    Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                }))

    }

    private fun addDish(categoryId: Int, id: Int, name: String) {

        compositeDisposable.add(sashimiApiService.addDishToCategory(categoryId, MenuAddDishRequest(
                MenuAddDishRequest.Image(id), name, "тестовые ингредиенты", 135, "тестовый вес"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()
                }, {
                    Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                }))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var filePart: MultipartBody.Part? = null

        if (requestCode == IMAGE_FOR_CATEGORY || requestCode == IMAGE_FOR_DISH && resultCode == Activity.RESULT_OK) {
            val selectedImage = data!!.data
            var bitmap: Bitmap? = null
            try {
                bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedImage)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val file = File(getPath(data.data))

            val size = bitmap!!.rowBytes.times(bitmap.height)
            val byteBuffer = ByteBuffer.allocate(size)
            bitmap.copyPixelsToBuffer(byteBuffer)

            filePart = MultipartBody.Part.createFormData("file", file.name, RequestBody.create(MediaType.parse("image/jpg"), file))

            compositeDisposable.add(filePart?.let {
                sashimiApiService.uploadImage(it)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (requestCode == IMAGE_FOR_CATEGORY) {
                                addCategory(it.id, it.original_name)
                            } else if (requestCode == IMAGE_FOR_DISH) {

                                val catId = it.id
                                val catName = it.original_name

                                compositeDisposable.add(sashimiApiService.getMenuAllCategories()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({
                                            val menuIdList = ArrayList<Int>()
                                            it.forEach {
                                                menuIdList.add(it.id)
                                            }

                                            val builderSingle1 = AlertDialog.Builder(requireContext())
                                            builderSingle1.setTitle("Выберите id категории меню")

                                            val arrayAdapter = ArrayAdapter<Int>(context, android.R.layout.simple_list_item_1, menuIdList)
                                            builderSingle1.setNegativeButton("закрыть", { dialog, which ->
                                                dialog.dismiss()
                                            })

                                            builderSingle1.setAdapter(arrayAdapter, { dialog, which ->
                                                dialog.dismiss()
                                                addDish(arrayAdapter.getItem(which), catId, catName)
                                            })
                                            builderSingle1.show()
                                        }, {
                                            it.printStackTrace()
                                        }))
                            }
                        }, {
                            it.printStackTrace()
                        })
            })
        } else {
            Toast.makeText(context, "Ошибка выбора форто", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showStatisticsDialog(statList: List<ReportResponse>) {
        val builderSingle = AlertDialog.Builder(requireContext())
        builderSingle.setTitle("Статистика заказов")

        val arrayAdapter = ArrayAdapter<ReportResponse>(context, android.R.layout.simple_list_item_1, statList)
        builderSingle.setNegativeButton("закрыть", { dialog, which ->
            dialog.dismiss()
        })

        builderSingle.setAdapter(arrayAdapter, { dialog, which ->
            dialog.dismiss()
        })
        builderSingle.show()
    }

    private fun getStatistics() {

        compositeDisposable.add(sashimiApiService.getOrdersReport()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showStatisticsDialog(it)
                }, {
                    it.printStackTrace()
                }))

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnAdminFragmentListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnAdminFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnAdminFragmentListener {
        fun onFragmentInteraction()

    }

    //for correctly getting android file path from Uri
    private fun getPath(uri: Uri): String {
        val data = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(context!!, uri, data, null, null, null)
        val cursor = loader.loadInBackground()
        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }
}
