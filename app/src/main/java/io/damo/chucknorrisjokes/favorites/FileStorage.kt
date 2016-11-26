package io.damo.chucknorrisjokes.favorites

import io.damo.chucknorrisjokes.utils.Result

interface FileStorage {

    fun save(name: String, content: String)

    fun load(name: String): Result<String>
}
