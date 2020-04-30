package org.ileasile.kformat

interface TextBlock {
    fun <R> accept(visitor: FormatVisitor<R>): R
}

inline class SimpleTextBlock(val content: String) : TextBlock {
    override fun <R> accept(visitor: FormatVisitor<R>) = visitor.visitSimpleText(this)
}

typealias BuildAction = (FormatTextBlock).() -> Unit

class FormatTextBlock(val format: Format = Format.EMPTY) :
    TextBlock {
    private val _children: MutableList<TextBlock> = mutableListOf()
    val children: List<TextBlock>
        get() = _children

    override fun <R> accept(visitor: FormatVisitor<R>) = visitor.visitFormatText(this)

    operator fun String.unaryPlus() = _children.add(SimpleTextBlock(this))

    fun withFormat(
        format: Format,
        builder: BuildAction = {}
    ) {
        val block = FormatTextBlock(format)
        block.builder()
        _children.add(block)
    }

    fun bold(on: Boolean = true, builder: BuildAction) = withFormat(
        Format(bold = on), builder)
    fun italic(on: Boolean = true, builder: BuildAction) = withFormat(
        Format(italic = on), builder)
    fun underlined(on: Boolean = true, builder: BuildAction) = withFormat(
        Format(underlined = on), builder)
    fun color(color: Color, builder: BuildAction) = withFormat(
        Format(color = color), builder)
    fun bgColor(color: Color, builder: BuildAction) = withFormat(
        Format(bgColor = color), builder)
}

fun kFormat(builder: BuildAction): FormatTextBlock {
    val block = FormatTextBlock(Format.DEFAULT)
    block.builder()
    return block
}
