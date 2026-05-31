package com.shenouda.stride.feature_auth.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.shenouda.stride.common.domain.model.Role
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val ROLE_KEY = stringPreferencesKey("user_role")
    }

    val selectedRole: Flow<Role?> = dataStore.data.catch { emit(emptyPreferences()) }.map { prefs ->
        when (prefs[ROLE_KEY]) {
            Role.TEACHER.name -> Role.TEACHER
            Role.STUDENT.name -> Role.STUDENT
            Role.NONE.name -> Role.NONE
            else -> null
        }
    }

    suspend fun saveRole(role: Role) {
        dataStore.edit { preferences -> preferences[ROLE_KEY] = role.name }
    }
    suspend fun clearRole(){
        dataStore.edit { prefs->prefs.remove(ROLE_KEY) }
    }

}