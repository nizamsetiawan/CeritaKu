package com.bangkit.intermediate.ceritaku

import com.bangkit.intermediate.ceritaku.source.response.ListStoryItem

object DummyData {
    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "author + $i",
                "story $i",
            )
            items.add(story)
        }
        return items
    }
}