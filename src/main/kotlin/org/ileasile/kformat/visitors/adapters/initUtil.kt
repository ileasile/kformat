package org.ileasile.kformat.visitors.adapters

import org.ileasile.kformat.visitors.AdaptableVisitor

fun <T, AdapterT : VisitorAdapter<T>> setupAdaptableVisitor(
    visitor: AdaptableVisitor<T>,
    adapter: AdapterT
): AdapterT {
    visitor.setAdapter(adapter)
    adapter.setVisitor(visitor)
    return adapter
}
