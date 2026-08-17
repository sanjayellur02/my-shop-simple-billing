package com.grocery.billing.data.repository

import com.grocery.billing.data.dao.DraftDao
import com.grocery.billing.data.entity.Draft
import com.grocery.billing.util.Dates

class DraftRepository(private val draftDao: DraftDao) {

    suspend fun get(key: String): Draft? = draftDao.get(key)

    suspend fun save(key: String, data: String) {
        draftDao.put(Draft(key = key, data = data, updatedAt = Dates.isoTimestamp()))
    }

    suspend fun delete(key: String) = draftDao.delete(key)

    suspend fun deleteAll() = draftDao.deleteAll()
}
