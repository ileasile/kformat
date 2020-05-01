package org.ileasile.kformat.visitors.adapters

import java.util.Stack
import org.ileasile.kformat.FormatTextBlock
import org.ileasile.kformat.SimpleTextBlock
import org.ileasile.kformat.TextBlock

class ResultSavingVisitorAdapter<T>(
    private val joiner: (List<T>) -> T
) : AbstractVisitorAdapter<T>() {

    constructor(reducer: (T, T) -> T, init: T) :
        this({ it.fold(init, reducer) })

    private val results = Stack<T>()

    override fun visitSimpleText(block: SimpleTextBlock): T {
        results.push(visitorInstance.visitSimpleText(block))
        return results.peek()
    }

    override fun visitFormatText(block: FormatTextBlock): T {
        results.push(visitorInstance.visitFormatText(block))
        return results.peek()
    }

    override fun visitChild(element: TextBlock): T {
        element.accept(this)
        return results.pop()
    }

    fun clear() = results.clear()

    fun finalize(): T = joiner(results)

    fun finalizeAndClear(): T {
        val finalized = joiner(results)
        clear()
        return finalized
    }
}
