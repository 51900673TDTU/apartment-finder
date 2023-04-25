package com.application.housefinder.appartment.unit

class Post(
    var id: Long,
    var owner: String,
    var title: String,
    var description: String,
    var imgURLs: List<String>
) {
    var address = ""
    var price = 0
    var personNumber = 1

    constructor(
        id: Long,
        owner: String,
        title: String,
        description: String,
        imgURLs: List<String>,
        address: String,
        price: Int,
        personNumber: Int
    ) : this(id, owner, title, description, imgURLs) {
        this.address = address
        this.price = price
        this.personNumber = personNumber
    }
}