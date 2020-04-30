package org.ileasile.kformat

import kotlin.reflect.KParameter
import kotlin.reflect.full.findParameterByName
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties

data class Format(
    val bold: Boolean? = null,
    val italic: Boolean? = null,
    val underlined: Boolean? = null,
    val color: Color? = null,
    val bgColor: Color? = null
) {
    fun merge(other: Format): Format {
        val copyF = this::class.memberFunctions.first { it.name == "copy" }
        val instanceParam = copyF.instanceParameter!!

        val paramMap = mutableMapOf<KParameter, Any>(instanceParam to this)

        for (member in Format::class.memberProperties) {
            val paramVal = member.get(other)
            if (paramVal != null) {
                val paramName = member.name.substringAfter("-").substringBefore(">")
                val param = copyF.findParameterByName(paramName)!!
                paramMap[param] = paramVal
            }
        }

        return copyF.callBy(paramMap) as Format
    }

    companion object {
        val EMPTY = Format()
        val DEFAULT = Format(
            bold = false,
            italic = false,
            underlined = false
        )
    }
}
