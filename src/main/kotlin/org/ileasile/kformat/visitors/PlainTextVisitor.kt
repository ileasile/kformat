package org.ileasile.kformat.visitors

import org.ileasile.kformat.FormatTextBlock
import org.ileasile.kformat.SimpleTextBlock

class PlainTextVisitor : AbstractAdaptableVisitor<String>() {
    override fun visitSimpleText(block: SimpleTextBlock): String = block.content

    override fun visitFormatText(block: FormatTextBlock): String =
        block.children.joinToString("") { it.accept(this) }
}
