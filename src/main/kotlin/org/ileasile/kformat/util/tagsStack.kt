package org.ileasile.kformat.util

import java.nio.CharBuffer
import java.util.ArrayList

open class TagsStack<T>(
    protected val transformer: (List<T>) -> T,
    private val reducer: (T, T) -> T = { a, b -> transformer(listOf(a, b)) }
) {
    protected val openTags = ArrayList<T>()
    protected val closeTags = ArrayList<T>()
    var contents: T? = null

    fun add(openTag: T, closeTag: T) {
        openTags.add(openTag)
        closeTags.add(closeTag)
    }

    fun updateContents(addedContents: T) {
        contents = contents?.let { reducer(it, addedContents) } ?: addedContents
    }

    fun finalize(): T =
        transformer(ArrayList<T>().apply {
            addAll(openTags)
            contents?.let { add(it) }
            addAll(closeTags.asReversed())
        })
}

class StringTagsStack : TagsStack<String>({ it.joinToString ("") }) {
    fun finalizeIndent(indent: String = " ".repeat(4)) =
        transformer(ArrayList<String>().apply {
            val n = openTags.size
            val indentSize = indent.length
            val buffer = CharBuffer.wrap(indent.repeat(n))
            fun String.indented(i: Int) = prependIndent(buffer.subSequence(0, i * indentSize).toString())

            for ((i, tag) in openTags.withIndex()) {
                add(tag.indented(i))
                add("\n")
            }

            contents?.let { add(it.indented(n)) }

            for ((i, tag) in closeTags.reversed().withIndex()) {
                add("\n")
                add(tag.indented(n - 1 - i))
            }
        })
}
