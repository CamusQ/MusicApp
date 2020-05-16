package com.example.music2.base

class RecentLRU<K, V> :
    LinkedHashMap<K, V>(16, 0.75.toFloat(), true) {
    override fun removeEldestEntry(eldest: Map.Entry<K, V>?): Boolean {
        return size > BaseConst.RECENTLY_LIMIT
    }
}