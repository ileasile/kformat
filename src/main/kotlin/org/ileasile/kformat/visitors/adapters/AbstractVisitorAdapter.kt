package org.ileasile.kformat.visitors.adapters

import org.ileasile.kformat.FormatVisitor

abstract class AbstractVisitorAdapter<T> : VisitorAdapter<T> {
    protected lateinit var visitorInstance: FormatVisitor<T>
    override fun setVisitor(visitor: FormatVisitor<T>) {
        visitorInstance = visitor
    }
}
