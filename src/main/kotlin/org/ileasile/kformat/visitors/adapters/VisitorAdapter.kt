package org.ileasile.kformat.visitors.adapters

import org.ileasile.kformat.FormatTextBlock
import org.ileasile.kformat.FormatVisitor
import org.ileasile.kformat.SimpleTextBlock
import org.ileasile.kformat.TextBlock

interface VisitorAdapter <R> : FormatVisitor<R> {
    fun setVisitor(visitor: FormatVisitor<R>)

    fun visit(element: TextBlock): R
}
