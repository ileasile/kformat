package org.ileasile.kformat.visitors

import org.ileasile.kformat.AnsiAwareColor
import org.ileasile.kformat.BasicColor
import org.ileasile.kformat.ColorConverter
import org.ileasile.kformat.Format
import org.ileasile.kformat.FormatTextBlock
import org.ileasile.kformat.RGB8
import org.ileasile.kformat.RgbAwareColor
import org.ileasile.kformat.SimpleTextBlock
import org.ileasile.kformat.util.StringTagsStack
import org.ileasile.kformat.util.buildStringTags

class HtmlVisitor(private val colorConverter: HtmlColorConverter = BasicHtmlColorConverter()) :
    AbstractAdaptableVisitor<String>() {
    override fun visitSimpleText(block: SimpleTextBlock): String = block.content

    override fun visitFormatText(block: FormatTextBlock): String =
        buildStringTags {
            addFormat(block.format)
            val contents = buildString {
                for (child in block.children) {
                    append(child.accept(this@HtmlVisitor))
                }
            }
            setContents(contents)
        }

    private fun StringTagsStack.addFormat(format: Format) = with(format) {
        addTagIf(bold, "b")
        addTagIf(italic, "i")
        addTagIf(underlined, "u")

        val styles = ArrayList<Pair<String, String>>().apply {
            if (color != null) add("color" to colorConverter.convert(color))
            if (bgColor != null) add("background-color" to colorConverter.convert(bgColor))
        }

        if (styles.isNotEmpty()) {
            addTag(
                "span",
                listOf("style" to styles.joinToString(";") { "${it.first}:${it.second}" })
            )
        }
    }

    private fun StringTagsStack.addTag(tag: String, attributes: Attributes = emptyList()) {
        val attrString = attributes.joinToString("") { " ${it.first}=\"${it.second}\"" }
        add("<$tag$attrString>", "</$tag>")
    }

    private fun StringTagsStack.addTagIf(cond: Boolean?, tag: String, attributes: Attributes = emptyList()) {
        if (cond != null && cond)
            addTag(tag, attributes)
    }
}

abstract class HtmlColorConverter : ColorConverter<String> {
    override fun convertRgb(rgbAwareColor: RgbAwareColor): String =
        encRgb(rgbAwareColor.rgb)

    companion object {
        fun encRgb(r: Int, g: Int, b: Int): String =
            "#${encHex(r)}${encHex(g)}${encHex(b)}"

        fun encRgb(rgb: RGB8): String =
            encRgb(rgb.r, rgb.g, rgb.b)

        private fun encHex(v: Int): String =
            "${(v / 16).toString(16)}${(v % 16).toString(16)}"
    }
}

class BasicHtmlColorConverter : HtmlColorConverter() {
    override fun convertAnsi(ansiAwareColor: AnsiAwareColor): String {
        val ansiInfo = ansiAwareColor.ansiInfo
        val v = if (ansiInfo.isBright) 255 else 128
        return when (ansiInfo.color) {
            BasicColor.Black -> encRgb(0, 0, 0)
            BasicColor.Red -> encRgb(v, 0, 0)
            BasicColor.Green -> encRgb(0, v, 0)
            BasicColor.Yellow -> encRgb(v, v, 0)
            BasicColor.Blue -> encRgb(0, 0, v)
            BasicColor.Magenta -> encRgb(v, 0, v)
            BasicColor.Cyan -> encRgb(0, v, v)
            BasicColor.White -> encRgb(v, v, v)
        }
    }
}

private typealias Attributes = List<Pair<String, String>>
