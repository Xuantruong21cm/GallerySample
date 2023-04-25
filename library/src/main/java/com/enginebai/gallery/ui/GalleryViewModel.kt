package com.enginebai.gallery.ui

import androidx.lifecycle.ViewModel
import com.enginebai.gallery.model.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import org.koin.core.KoinComponent
import org.koin.core.inject

class GalleryViewModel : ViewModel(), KoinComponent {
    var setting: AlbumSetting? = null
    val multipleSelectMedia: BehaviorSubject<MutableList<Media>> = BehaviorSubject.createDefault(mutableListOf())
    val singleSelectMedia: PublishSubject<Media> = PublishSubject.create()
    val currentAlbumItem: BehaviorSubject<AlbumItem> = BehaviorSubject.create()

    private val albumRepo: AlbumRepo by inject()

    fun loadAlbums(): Completable {
        return albumRepo.fetchAlbums(setting)
            .doOnComplete {
                selectAlbum(albumRepo.getAlbumItemSync(ALL_MEDIA_ALBUM_NAME, setting)!!)
            }
    }

    fun getAlbums(): Observable<List<AlbumItem>> = albumRepo.getAlbums(setting)

    fun selectAlbum(album: AlbumItem) {
        currentAlbumItem.onNext(album)
    }

    fun selectMedia(media: Media) {
        if (true == setting?.multipleSelection) {
            multipleSelectMedia.value?.run {
                if (this.contains(media))
                    this.remove(media)
                else {
                    setting?.maxSelection?.let {
                        if (this.size < it)
                            this.add(media)
                    } ?: kotlin.run {
                        this.add(media)
                    }
                }
                multipleSelectMedia.onNext(this)
            }
        } else {
            singleSelectMedia.onNext(media)
        }
    }

    fun isSelect(media: Media): Boolean = multipleSelectMedia.value!!.contains(media)
}