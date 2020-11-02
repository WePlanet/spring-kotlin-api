package com.spring.sample.order

import com.spring.sample.AuditingEntity
import com.spring.sample.book.Book
import org.hibernate.annotations.BatchSize
import java.math.BigDecimal
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.Table

@Entity
@Table(name = "orders")
class Order() : AuditingEntity() {

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "name", column = Column(name = "name", nullable = false)),
        AttributeOverride(name = "email", column = Column(name = "email", nullable = false))
    )
    lateinit var orderer: Orderer

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "order_book",
        joinColumns = [JoinColumn(name = "orders_id", nullable = false, updatable = false)]
    )
    @BatchSize(size = 2)
    var orderBooks: MutableList<OrderBook> = mutableListOf()

    @Column(name = "price", nullable = false)
    lateinit var price: BigDecimal

    constructor(orderer: Orderer, books: List<Book>) : this() {
        var totalBookPrice = BigDecimal.ZERO

        for (book in books) {
            totalBookPrice += book.price
            this.orderBooks.add(OrderBook(book.id!!, book.title, book.price))
        }

        this.orderer = orderer
        this.price = totalBookPrice
    }
}