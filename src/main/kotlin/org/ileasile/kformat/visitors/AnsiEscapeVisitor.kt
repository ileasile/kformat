package org.ileasile.kformat.visitors

import java.util.Stack
import org.ileasile.kformat.BuildAction
import org.ileasile.kformat.Format
import org.ileasile.kformat.FormatTextBlock
import org.ileasile.kformat.FormatVisitor
import org.ileasile.kformat.SimpleTextBlock
import org.ileasile.kformat.kFormat

class AnsiEscapeVisitor : FormatVisitor<String> {
    private val formatStack = Stack<Format>()

    override fun visitSimpleText(block: SimpleTextBlock) = block.content

    override fun visitFormatText(block: FormatTextBlock): String = buildString {
        addFormat(block.format)

        val parentFormat = if (formatStack.isNotEmpty()) formatStack.peek() else null
        val mergedFormat = parentFormat?.merge(block.format) ?: block.format

        formatStack.push(mergedFormat)
        for (child in block.children)
            append(child.accept(this@AnsiEscapeVisitor))
        formatStack.pop()

        append(AnsiConstants.RESET_CSI)
        if (parentFormat != null) {
            addFormat(parentFormat)
        }
    }

    private fun StringBuilder.addFormat(format: Format) = with(format) {
        addTagIf(bold, AnsiConstants.BOLD_MARK)
        addTagIf(italic, AnsiConstants.ITALIC_MARK)
        addTagIf(underlined, AnsiConstants.UNDERLINE_MARK)

        if (color != null) addTag(color.encodeFg())
        if (bgColor != null) addTag(bgColor.encodeBg())
    }

    private fun StringBuilder.addTag(tag: String) {
        append("${AnsiConstants.CSI}${tag}m")
    }

    private fun StringBuilder.addTagIf(cond: Boolean?, tag: String) {
        if (cond != null && cond)
            addTag(tag)
    }
}

object AnsiConstants {
    const val ESC = "\u001b"
    const val CSI = "$ESC["
    const val RESET_CSI = "${CSI}m"

    const val BOLD_MARK = "1"
    const val ITALIC_MARK = "3"
    const val UNDERLINE_MARK = "4"
}

fun buildAnsiFormattedString(builder: BuildAction) =
    kFormat(builder).accept(AnsiEscapeVisitor())
