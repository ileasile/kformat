package org.ileasile.kformat.adapters

import org.ileasile.kformat.FormatVisitor
import org.ileasile.kformat.TextBlock

/**
 * Visitor adapter interface gives an additional flavour to a visitor,
 * saving its results between runs or logging something
 *
 * @param R Visitor result type
 */
interface VisitorAdapter <R> : FormatVisitor<R> {
    fun setVisitor(visitor: FormatVisitor<R>)

    fun visit(element: TextBlock): R
}
