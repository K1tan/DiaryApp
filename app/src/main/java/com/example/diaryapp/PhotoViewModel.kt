package com.example.diaryapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

//class PhotoViewModel(private val photoDao: PhotoDao) : ViewModel() {
//    private val _photosLiveData: MutableLiveData<List<Photo>> = MutableLiveData()
//    val photosLiveData: LiveData<List<Photo>> = _photosLiveData
//
//    fun insertPhoto(photo: Photo) {
//        viewModelScope.launch {
//            photoDao.insertPhoto(photo)
//        }
//    }
//
//    fun getAllPhotos() {
//        viewModelScope.launch {
//            val photos = photoDao.getAllPhotos()
//            _photosLiveData.postValue(photos)
//        }
//    }
//
//    fun deletePhoto(photo: Photo) {
//        viewModelScope.launch {
//            photoDao.deletePhoto(photo)
//        }
//    }
//}