// IRemotePlayService.aidl
package com.style.framework;

// Declare any non-default types here with import statements

interface IRemotePlayService {
    int getPid();
    void start(int a);
    void stop(String a);
    //void play(MediaBean a);
}
