package io.damo.chucknorrisjokes.favorites

import android.content.Context
import io.damo.chucknorrisjokes.utils.Result
import io.damo.chucknorrisjokes.utils.Result.Error
import io.damo.chucknorrisjokes.utils.Result.Success
import java.io.File


class AndroidFileStorage(val context: Context) : FileStorage {

    override fun save(name: String, content: String) = file(name).writeText(content)

    override fun load(name: String): Result<String> {
        val file = file(name)

        if (file.isFile) {
            return Success(file.readText())
        } else {
            return Error("Could not read file")
        }
    }

    private fun file(name: String) = File(context.filesDir, name)
}
