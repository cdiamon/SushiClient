package net.pdmtrdv.sashimisake.model

data class MenuDishResponse(val id: Int,
                            val image: Image,
                            val ingredients: String,
                            val name: String,
                            val price: Int,
                            val weight: String) {

    data class Image(val id: Int,
                     val original_name: String,
                     val path: String)

}