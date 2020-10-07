package com.sungbin.hyunnieserver.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import org.apache.commons.net.ftp.FTPClient
import javax.inject.Singleton


/**
 * Created by SungBin on 2020-08-31.
 */

@Module
@InstallIn(ApplicationComponent::class)
class FtpModule {

    @Singleton
    @Provides
    fun instance() = FTPClient().apply {
        controlEncoding = "UTF-8"
    }

}