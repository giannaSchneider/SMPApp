package com.example.inventory.data

import kotlinx.coroutines.flow.Flow


class ItemsRepositoryImpl(private val itemDao: ItemDao) : ItemsRepository {

    override fun getAllItemsStream(): Flow<List<Item>> {
        return itemDao.getAllItems()
    }

    override fun getItemStream(id: Int): Flow<Item?> {
        return itemDao.getItem(id)
    }

//    override suspend fun setTimerRoutine(item: Item) {
//        // implementation here
//    }

    override suspend fun insertItem(item: Item) {
        itemDao.insert(item)
    }

    override suspend fun deleteItem(item: Item) {
        itemDao.delete(item)
    }

    override suspend fun updateItem(item: Item) {
        itemDao.update(item)
    }
}
