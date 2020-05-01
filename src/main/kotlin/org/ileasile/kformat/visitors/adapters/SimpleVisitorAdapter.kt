package org.ileasile.kformat.visitors.adapters

import org.ileasile.kformat.FormatTextBlock
import org.ileasile.kformat.SimpleTextBlock
import org.ileasile.kformat.TextBlock

class SimpleVisitorAdapter<T> : AbstractVisitorAdapter<T>() {

    override fun visitSimpleText(block: SimpleTextBlock): T =
        visitorInstance.visitSimpleText(block)

    override fun visitFormatText(block: FormatTextBlock): T =
        visitorInstance.visitFormatText(block)

    override fun visitChild(element: TextBlock): T = element.accept(this)
}
