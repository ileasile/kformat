package org.ileasile.kformat.adapters

import org.ileasile.kformat.visitors.AdaptableVisitor

fun <T, AdapterT : VisitorAdapter<T>> AdaptableVisitor<T>.adapt(adapter: AdapterT): AdapterT {
    setAdapter(adapter)
    adapter.setVisitor(this)
    return adapter
}
