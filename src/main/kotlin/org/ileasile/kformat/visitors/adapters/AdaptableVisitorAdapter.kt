package org.ileasile.kformat.visitors.adapters

import org.ileasile.kformat.visitors.AdaptableVisitor

interface AdaptableVisitorAdapter<R>: VisitorAdapter<R>, AdaptableVisitor<R>
