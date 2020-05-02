package org.ileasile.kformat.visitors

import org.ileasile.kformat.FormatVisitor
import org.ileasile.kformat.TextBlock
import org.ileasile.kformat.visitors.adapters.VisitorAdapter

interface AdaptableVisitor<T> : FormatVisitor<T> {
    fun setAdapter(adapter: VisitorAdapter<T>)

    fun visitChild(element: TextBlock): T {
        return element.accept(this)
    }
}
