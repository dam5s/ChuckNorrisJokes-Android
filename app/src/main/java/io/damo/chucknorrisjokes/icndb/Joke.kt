package io.damo.chucknorrisjokes.icndb

import java.io.Serializable

data class Joke(val id: Int, val text: String) : Serializable
