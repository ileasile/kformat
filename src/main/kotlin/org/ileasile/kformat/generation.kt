package org.ileasile.kformat

import org.ileasile.kformat.visitors.AnsiEscapeVisitor
import org.ileasile.kformat.visitors.HtmlVisitor
import org.ileasile.kformat.visitors.PlainTextVisitor

fun TextBlock.asAnsiEscaped() =
    accept(AnsiEscapeVisitor())
fun TextBlock.asHTML(prettyPrint: Boolean = false) =
    accept(HtmlVisitor(pretty = prettyPrint))
fun TextBlock.asPlainText() =
    accept(PlainTextVisitor())
