package net.pdmtrdv.sashimisake.model

data class DishUpdateRequest(val image: Image,
                             val ingredients: String,
                             val name: String,
                             val price: Int,
                             val weight: String) {

    data class Image(val id: Int)

}