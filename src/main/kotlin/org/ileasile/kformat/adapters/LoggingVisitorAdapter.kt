package org.ileasile.kformat.adapters

import org.ileasile.kformat.FormatTextBlock
import org.ileasile.kformat.SimpleTextBlock

class LoggingVisitorAdapter<R> : SimpleVisitorAdapter<R>() {
    private var counter = 0
    private var indentCounter = 0
    private val indent = "    "

    override fun visitSimpleText(block: SimpleTextBlock): R {
        println("${indent.repeat(indentCounter)}Visiting simple text: ${block.content}")
        return super.visitSimpleText(block)
    }

    override fun visitFormatText(block: FormatTextBlock): R {
        val capturedCounter = counter++
        val capturedIndent = indentCounter
        val currentIndent = indent.repeat(capturedIndent)

        println("${currentIndent}Visiting formatted text [$capturedCounter]: ${block.format}")
        indentCounter++
        val res = super.visitFormatText(block)
        indentCounter--
        println("${currentIndent}Exiting formatted text [$capturedCounter]")
        return res
    }
}
