package org.ileasile.kformat.visitors

import org.ileasile.kformat.TextBlock
import org.ileasile.kformat.visitors.adapters.VisitorAdapter

abstract class AbstractAdaptableVisitor<T> : AdaptableVisitor<T> {
    private lateinit var adapterInstance: VisitorAdapter<T>

    override fun visitChild(element: TextBlock): T {
        return if (this::adapterInstance.isInitialized)
            adapterInstance.visitChild(element)
        else
            super.visitChild(element)
    }

    override fun setAdapter(adapter: VisitorAdapter<T>) {
        adapterInstance = adapter
    }
}
