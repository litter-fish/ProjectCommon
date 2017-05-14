package com.fish.service;

/**
 * Created by fish on 2017/5/14.
 */
public interface ICallbackService {
    void addListener(String key, ICallbackListener listener);
}
