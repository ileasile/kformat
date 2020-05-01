package org.ileasile.kformat.visitors.adapters

import org.ileasile.kformat.FormatTextBlock
import org.ileasile.kformat.FormatVisitor
import org.ileasile.kformat.SimpleTextBlock
import org.ileasile.kformat.TextBlock

interface VisitorAdapter <T> : FormatVisitor<T> {
    fun setVisitor(visitor: FormatVisitor<T>)

    override fun visitSimpleText(block: SimpleTextBlock): T

    override fun visitFormatText(block: FormatTextBlock): T

    fun visitChild(element: TextBlock): T
}
