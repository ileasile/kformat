package org.ileasile.kformat.adapters

import org.ileasile.kformat.visitors.AdaptableVisitor

interface AdaptableVisitorAdapter<R> : VisitorAdapter<R>, AdaptableVisitor<R>
