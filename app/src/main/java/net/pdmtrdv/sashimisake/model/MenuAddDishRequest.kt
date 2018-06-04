package net.pdmtrdv.sashimisake.model

data class MenuAddDishRequest(val image: Image,
                              val name: String,
                              val ingredients: String,
                              val price: Int,
                              val weight: String) {

    data class Image(val id: Int)

}