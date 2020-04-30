package org.ileasile.kformat.util

import java.util.ArrayList

open class TagsStack<T>(private val reducer: (T, T) -> T, private val init: T) {
    private val openTags = ArrayList<T>()
    private val closeTags = ArrayList<T>()
    private var contents: T? = null

    fun add(openTag: T, closeTag: T) {
        openTags.add(openTag)
        closeTags.add(closeTag)
    }

    fun setContents(newContents: T) {
        contents = newContents
    }

    fun updateContents(addedContents: T) {
        contents = contents?.let { reducer(it, addedContents) } ?: addedContents
    }

    fun finalize(): T {
        var ret = init
        for (tag in openTags) {
            ret = reducer(ret, tag)
        }
        contents?.let { ret = reducer(ret, it) }
        for (tag in closeTags.asReversed()) {
            ret = reducer(ret, tag)
        }
        return ret
    }
}

class StringTagsStack : TagsStack<String>(String::plus, "")

fun buildStringTags(builder: (StringTagsStack).() -> Unit): String {
    val stack = StringTagsStack()
    stack.builder()
    return stack.finalize()
}
