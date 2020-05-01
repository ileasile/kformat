package org.ileasile.kformat.test

import kotlin.test.assertEquals
import org.ileasile.kformat.BasicColor
import org.ileasile.kformat.Format
import org.ileasile.kformat.RGBColor
import org.ileasile.kformat.kFormat
import org.ileasile.kformat.visitors.AnsiEscapeVisitor
import org.ileasile.kformat.visitors.HtmlVisitor
import org.ileasile.kformat.visitors.PlainTextVisitor
import org.ileasile.kformat.visitors.adapters.ResultSavingVisitorAdapter
import org.ileasile.kformat.visitors.adapters.setupAdaptableVisitor
import org.junit.jupiter.api.Test

class FormatTest {
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
        val formatted = kFormat {
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

        println(formatted.accept(AnsiEscapeVisitor()))
        println(formatted.accept(PlainTextVisitor()))
        println(formatted.accept(HtmlVisitor()))
    }

    @Test
    fun testSavingReducers() {
        val formatted1 = kFormat {
            +"A"; bold { +"B" }; +"C"
        }
        val formatted2 = kFormat {
            +"D"; bold { +"E" }; +"F"
        }

        val visitor = AnsiEscapeVisitor()
        val s1 = formatted1.accept(visitor)
        val s2 = formatted2.accept(visitor)

        val savingVisitor = setupAdaptableVisitor(
            visitor, ResultSavingVisitorAdapter { it.joinToString("") })
        formatted1.accept(savingVisitor)
        formatted2.accept(savingVisitor)
        val s = savingVisitor.finalize()

        println("s1: '$s1', s2: '$s2'; composed: '$s'")
        assertEquals(s, s1 + s2)
    }
}
