package org.ileasile.kformat.test

import kotlin.test.assertEquals
import org.ileasile.kformat.BasicColor
import org.ileasile.kformat.Format
import org.ileasile.kformat.RGBColor
import org.ileasile.kformat.adapters.LoggingVisitorAdapter
import org.ileasile.kformat.adapters.StringResultSavingVisitorAdapter
import org.ileasile.kformat.adapters.adapt
import org.ileasile.kformat.kFormat
import org.ileasile.kformat.visitors.AnsiEscapeVisitor
import org.ileasile.kformat.visitors.HtmlVisitor
import org.ileasile.kformat.visitors.PlainTextVisitor
import org.junit.jupiter.api.Test

class FormatTest {
    private val formatted1 = kFormat {
        color(BasicColor.Blue) {
            bold {
                +"Bold text"
                italic {
                    +" Bold italic text"
                }
                +" Bold text again"
            }
        }

        +" Normal text"
        italic {
            +" Italic "
            underlined {
                +"text"
            }
        }
        bgColor(BasicColor.Red) {
            + " Colored"
            color(RGBColor(128, 54, 22)) {

                + " text"
            }
        }
    }

    private val formatted2 = kFormat {
        +"A"; bold { +"B" }; +"C"
    }

    @Test
    fun testMerge() {
        val bold = Format(bold = true)
        val italic = Format(italic = true)
        val resetItalic = Format(italic = false)
        val boldItalic = Format(bold = true, italic = true)

        assertEquals(boldItalic, bold.merge(italic))
        assertEquals(boldItalic, italic.merge(bold))
        assertEquals(bold, Format.EMPTY.merge(bold))
        assertEquals(bold.copy(italic = false), boldItalic.merge(resetItalic))
    }

    @Test
    fun testConsole() {
        println(formatted1.accept(AnsiEscapeVisitor()))
        println(formatted1.accept(PlainTextVisitor()))
        println(formatted1.accept(HtmlVisitor(pretty = true)))
    }

    @Test
    fun testSavingAdapters() {
        val formatted1 = kFormat {
            +"A"; bold { +"B" }; +"C"
        }
        val formatted2 = kFormat {
            +"D"; bold { +"E"; italic { +"YY" }; +"Z" }; +"F"
        }

        val visitor = AnsiEscapeVisitor()
        val s1 = formatted1.accept(visitor)
        val s2 = formatted2.accept(visitor)

        val savingVisitor = visitor.adapt(StringResultSavingVisitorAdapter())
        formatted1.accept(savingVisitor)
        formatted2.accept(savingVisitor)
        val s = savingVisitor.finalize()

        println("s1: '$s1', s2: '$s2'; composed: '$s'")
        assertEquals(s, s1 + s2)
    }

    @Test
    fun testLoggingAdapter() {
        val savingAdapter = StringResultSavingVisitorAdapter()
        val visitor = AnsiEscapeVisitor()
            .adapt(savingAdapter)
            .adapt(LoggingVisitorAdapter())

        println(formatted1.accept(visitor))
        println(formatted2.accept(visitor))

        println("Whole result: ${savingAdapter.finalize()}")
    }
}
