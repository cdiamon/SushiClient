package net.pdmtrdv.sashimisake.model

data class MenuAddCategoryRequest(val image: Image,
                                  val name: String) {

    data class Image(val id: Int)

}