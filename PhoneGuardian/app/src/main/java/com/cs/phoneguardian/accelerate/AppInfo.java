package com.cs.phoneguardian.accelerate;

import android.graphics.drawable.Drawable;

/**
 * Created by SEELE on 2017/7/13.
 */
public class AppInfo {
    private String name;
    private String packageName;
    private Drawable icon;
    private long dirtyMemSize;
    private long cacheSize;
    private long externalCacheSize;
    private long totalCacheSize;
    private boolean isSystem;
    private boolean isSeleced;
    private boolean isLock;

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
}
