package com.dirror.music.foyou.sentence.foyoulibrary

import com.dirror.foyou.sentence.OptimizeHitokoto
import com.dirror.foyou.sentence.SentenceData
import com.dirror.foyou.sentence.foyoulibrary.FoyouOthers
import com.dirror.foyou.sentence.foyoulibrary.FoyouPoetry

object FoyouLibrary {
    fun getSentence(): SentenceData {
        val sentence = when ((1..3).random()) {
            1 -> FoyouPoetry.getFoyouPoetry()
            2 -> FoyouLiterature.getLiterature()
            3 -> FoyouOthers.getFoyouOthers()
            else -> FoyouPoetry.getFoyouPoetry()
        }

        val text = OptimizeHitokoto.sentenceTextNewLine(sentence.text)
        return SentenceData(text, sentence.author, sentence.source)
    }
}