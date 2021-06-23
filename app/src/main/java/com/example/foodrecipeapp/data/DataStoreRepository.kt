package com.example.foodrecipeapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.*
import com.example.foodrecipeapp.util.Contants.Companion.DEFAULT_DIET_TYPE
import com.example.foodrecipeapp.util.Contants.Companion.DEFAULT_MEAL_TYPE
import com.example.foodrecipeapp.util.Contants.Companion.PREFERENCES_BACK_ONLINE
import com.example.foodrecipeapp.util.Contants.Companion.PREFERENCES_DIET_TYPE
import com.example.foodrecipeapp.util.Contants.Companion.PREFERENCES_DIET_TYPE_ID
import com.example.foodrecipeapp.util.Contants.Companion.PREFERENCES_MEAL_TYPE
import com.example.foodrecipeapp.util.Contants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.example.foodrecipeapp.util.Contants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

//since datastore repo will be used inside the recipesViewModel
@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {
    //defining all the keys which we will use for datastore preference
    private object PreferenceKeys {
        //different keys for storing the meal type id and meal type for the datastore preferences
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = stringPreferencesKey(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = intPreferencesKey(PREFERENCES_DIET_TYPE_ID)
        val backOnline = booleanPreferencesKey(PREFERENCES_BACK_ONLINE)
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCES_NAME
    )

    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        dataStore.edit { preferences ->
            //saving the preferences
            preferences[PreferenceKeys.selectedMealType] = mealType
            preferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferenceKeys.selectedDietType] = dietType
            preferences[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }

    suspend fun saveBackOnline(backOnline: Boolean) {
        dataStore.edit { preferences ->

            preferences[PreferenceKeys.backOnline] = backOnline

        }
    }

    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            //retrieving the values from the datastore preferences using keys which we have defnied
            //if no data inside the key,return the default value
            val selectedMealType = preferences[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType = preferences[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId] ?: 0
            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )

        }

      val readBackOnline:Flow<Boolean> =dataStore.data
          .catch {
                  exception ->
              if (exception is IOException) {
                  emit(emptyPreferences())
              } else {
                  throw exception
              }
          }
          .map {preferences->
              val backOnline = preferences[PreferenceKeys.backOnline]?:false
              backOnline
          }


}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)