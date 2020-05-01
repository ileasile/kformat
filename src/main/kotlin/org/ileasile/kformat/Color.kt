package org.ileasile.kformat

import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.math.roundToInt

interface Color {
    val fgCode: Int?
        get() = null
    val bgCode: Int?
        get() = null
    val extCode: Int?
        get() = null
    val rgb: RGB8?
        get() = null

    fun encodeFg(): String {
        val simple = fgCode
        if (simple != null)
            return simple.toString()

        val ext = extCode
        if (ext != null)
            return "38;5;$ext"

        val (r, g, b) = rgb ?: throw IllegalStateException("Wrong color definition")
        return "38;2;$r;$g;$b"
    }

    fun encodeBg(): String {
        val simple = bgCode
        if (simple != null)
            return simple.toString()

        val ext = extCode
        if (ext != null)
            return "48;5;$ext"

        val (r, g, b) = rgb ?: throw IllegalStateException("Wrong color definition")
        return "48;2;$r;$g;$b"
    }
}

interface RgbAwareColor : Color {
    override val rgb: RGB8
}

interface AnsiAwareColor : Color {
    val ansiInfo: AnsiColorInfo
}

data class AnsiColorInfo(
    val color: BasicColor,
    val isBright: Boolean = false
)

interface ColorConverter <T> {
    fun convertAnsi(ansiAwareColor: AnsiAwareColor): T
    fun convertRgb(rgbAwareColor: RgbAwareColor): T

    fun convert(color: Color): T {
        return when (color) {
            is AnsiAwareColor -> convertAnsi(color)
            is RgbAwareColor -> convertRgb(color)
            else -> {
                val names = listOf(
                    AnsiAwareColor::class,
                    RgbAwareColor::class
                ).joinToString { it.qualifiedName!! }
                throw IllegalArgumentException("Color should implement one of the following interfaces: $names.")
            }
        }
    }
}

enum class BasicColor(val code: Int) : AnsiAwareColor {
    Black(0),
    Red(1),
    Green(2),
    Yellow(3),
    Blue(4),
    Magenta(5),
    Cyan(6),
    White(7);

    override val fgCode: Int
        get() = code + 30
    override val bgCode: Int
        get() = code + 40
    override val extCode: Int
        get() = code

    val htmlName: String
        get() = name.toLowerCase()

    override val ansiInfo: AnsiColorInfo
        get() = AnsiColorInfo(this)
}

inline class BrightColor(
    private val baseColor: BasicColor
) : AnsiAwareColor {
    override val ansiInfo: AnsiColorInfo
        get() = AnsiColorInfo(baseColor, true)
    override val fgCode: Int
        get() = baseColor.code + 90
    override val bgCode: Int
        get() = baseColor.code + 100
    override val extCode: Int
        get() = baseColor.code + 8
}

data class RGB6Color(
    val red: Int,
    val green: Int,
    val blue: Int
) : RgbAwareColor {
    override val extCode: Int?
        get() = 16 + 36 * red + 6 * green + blue

    override val rgb: RGB8
        get() = RGB8(red * RGB6_TO_RGB8, green * RGB6_TO_RGB8, blue * RGB6_TO_RGB8)

    companion object {
        const val RGB6_TO_RGB8 = 255 / 5
    }
}

data class GrayShadeColor(val level: Double) : RgbAwareColor {
    override val extCode: Int?
        get() = 232 + (level * 24).roundToInt()

    override val rgb: RGB8
        get() {
            val intLevel = (level * 255).roundToInt()
            return RGB8(intLevel, intLevel, intLevel)
        }
}

inline class RGBColor(
    override val rgb: RGB8
) : RgbAwareColor {
    constructor(red: Int, green: Int, blue: Int) : this(RGB8(red, green, blue))
}

data class RGB8(
    val r: Int,
    val g: Int,
    val b: Int
)
