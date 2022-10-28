package net.entropiya.erestaurant

data class Person(val firstName: String, val lastName: String) {
    override fun toString(): String = "$firstName $lastName"
}



fun main(args: Array<String>) {

    val byId = dummyRestaurants.associateBy { it.id }

//    println(byId[0]?.id)

    println(favId(100))


    val scientists = listOf(Person("Grace", "Hopper"), Person("Jacob", "Bernoulli"), Person("Johann", "Bernoulli"))

    val byLastName = scientists.associateBy { it.lastName }

// Jacob Bernoulli does not occur in the map because only the last pair with the same key gets added
//    println(byLastName) // {Hopper=Grace Hopper, Bernoulli=Johann Bernoulli}
}


fun favIds(): List<Int> = dummyRestaurants.filter { it.isFavorite }.map { it.id }

fun favId(id: Int): Restaurant? = dummyRestaurants.find { it.id == id }
