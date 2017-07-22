package com.cs.phoneguardian.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by SEELE on 2017/7/13.
 */
public class AppInfo {
    /**
     * 应用名
     */
    private String name;
    /**
     * 包名
     */
    private String packageName;
    /**
     * 图标
     */
    private Drawable icon;
    /**
     * RAM占用大小
     */
    private long dirtyMemSize;
    /**
     * 内部缓存大小
     */
    private long cacheSize;
    /**
     * SD缓存大小
     */
    private long externalCacheSize;
    /**
     * 总缓存大小
     */
    private long totalCacheSize;
    /**
     * 是否为系统应用
     */
    private boolean isSystem;
    /**
     * 是否被选中
     */
    private boolean isSeleced;
    /**
     * 是否加速锁定
     */
    private boolean isLock;
    /**
     * 是否加应用锁
     */
    private boolean isAppLock;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getDirtyMemSize() {
        return dirtyMemSize;
    }

    public void setDirtyMemSize(long dirtyMemSize) {
        this.dirtyMemSize = dirtyMemSize;
    }

    public boolean isSeleced() {
        return isSeleced;
    }

    public void setSeleced(boolean seleced) {
        isSeleced = seleced;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }

    public long getExternalCacheSize() {
        return externalCacheSize;
    }

    public void setExternalCacheSize(long externalCacheSize) {
        this.externalCacheSize = externalCacheSize;
    }

    public long getTotalCacheSize() {
        return totalCacheSize;
    }

    public void setTotalCacheSize(long totalCacheSize) {
        this.totalCacheSize = totalCacheSize;
    }

    public boolean isAppLock() {
        return isAppLock;
    }

    public void setAppLock(boolean appLock) {
        isAppLock = appLock;
    }
}
