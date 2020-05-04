package org.ileasile.kformat.adapters

import org.ileasile.kformat.FormatVisitor
import org.ileasile.kformat.visitors.AbstractAdaptableVisitor

abstract class AbstractVisitorAdapter<T> : AbstractAdaptableVisitor<T>(), AdaptableVisitorAdapter<T> {
    protected lateinit var visitorInstance: FormatVisitor<T>
    override fun setVisitor(visitor: FormatVisitor<T>) {
        visitorInstance = visitor
    }
}
