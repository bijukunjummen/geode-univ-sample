package org.bk.univ.exceptions

class EntityNotFoundException(message: String? = null, root: Throwable? = null) : RuntimeException(message, root) {
    constructor(message: String) : this(message, null)
}