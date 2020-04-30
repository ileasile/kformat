package org.ileasile.kformat

interface FormatVisitor <R> {
    fun visitSimpleText(block: SimpleTextBlock): R
    fun visitFormatText(block: FormatTextBlock): R
}
