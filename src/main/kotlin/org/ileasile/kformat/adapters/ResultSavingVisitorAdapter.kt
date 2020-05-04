package org.ileasile.kformat.adapters

import java.util.Stack
import org.ileasile.kformat.FormatTextBlock
import org.ileasile.kformat.SimpleTextBlock
import org.ileasile.kformat.TextBlock

open class ResultSavingVisitorAdapter<T>(
    private val joiner: (List<T>) -> T
) : SimpleVisitorAdapter<T>() {

    constructor(reducer: (T, T) -> T, init: T) :
        this({ it.fold(init, reducer) })

    private val results = Stack<T>()

    override fun visitSimpleText(block: SimpleTextBlock): T {
        results.push(super.visitSimpleText(block))
        return results.peek()
    }

    override fun visitFormatText(block: FormatTextBlock): T {
        results.push(super.visitFormatText(block))
        return results.peek()
    }

    override fun visit(element: TextBlock): T {
        super.visit(element)
        return results.pop()
    }

    fun clear() = results.clear()

    fun finalize(): T = joiner(results)

    fun finalizeAndClear(): T {
        val finalized = finalize()
        clear()
        return finalized
    }
}

class StringResultSavingVisitorAdapter : ResultSavingVisitorAdapter<String>({ it.joinToString("") })
