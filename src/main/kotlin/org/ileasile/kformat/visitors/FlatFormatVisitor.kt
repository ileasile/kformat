package org.ileasile.kformat.visitors

import org.ileasile.kformat.BuildAction
import org.ileasile.kformat.Format
import org.ileasile.kformat.FormatTextBlock
import org.ileasile.kformat.FormatVisitor
import org.ileasile.kformat.SimpleTextBlock
import org.ileasile.kformat.kFormat

data class TextWithFormat(
    val content: String,
    val format: Format
)

typealias PreparedFormattedText = List<TextWithFormat>

class FlatFormatVisitor : FormatVisitor<PreparedFormattedText> {
    override fun visitSimpleText(block: SimpleTextBlock): PreparedFormattedText = with(block) {
        if (content.isEmpty())
            return emptyList()

        return listOf(TextWithFormat(content, Format.EMPTY))
    }

    override fun visitFormatText(block: FormatTextBlock): PreparedFormattedText = with(block) {
        return children.flatMap { block ->
            val prepared = block.accept(this@FlatFormatVisitor)
            prepared.map { it.copy(format = format.merge(it.format)) }
        }
    }
}

fun kFormatFlatten(builder: BuildAction) = kFormat(builder).accept(FlatFormatVisitor())

fun buildFormattedString(formatter: (TextWithFormat) -> String, buildAction: BuildAction) =
    kFormatFlatten(buildAction).joinToString(separator = "", transform = formatter)
