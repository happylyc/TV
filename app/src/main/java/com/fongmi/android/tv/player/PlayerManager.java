package com.fongmi.android.tv.player;

import android.text.TextUtils;

import androidx.media3.common.MediaItem;
import androidx.media3.common.MediaMetadata;
import androidx.media3.common.Player;
import androidx.media3.common.Tracks;

import com.fongmi.android.tv.bean.Danmaku;
import com.fongmi.android.tv.player.Engine;
import com.fongmi.android.tv.player.TrackUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {

    private Player player;
    private Engine engine;
    private Spec spec;
    private Callback callback;

    public Player getPlayer() {
        return player;
    }

    public Tracks getCurrentTracks() {
        return player.getCurrentTracks();
    }

    // Media3 已移除章节功能，返回空列表即可
    public List<Object> getCurrentMediaChapters() {
        return Collections.emptyList();
    }

    // Media3 已移除版本功能，返回空列表即可
    public List<Object> getCurrentMediaEditions() {
        return Collections.emptyList();
    }

    public MediaItem getCurrentMediaItem() {
        return player.getCurrentMediaItem();
    }

    public int getPlaybackState() {
        return player.getPlaybackState();
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public boolean isReleased() {
        return player == null;
    }

    public String getUrl() {
        return spec != null ? spec.getUrl() : null;
    }

    public String getKey() {
        return spec != null ? spec.getKey() : null;
    }

    public List<Danmaku> getDanmakus() {
        return spec != null ? spec.getDanmakus() : null;
    }

    private void setDanmakus(List<Danmaku> items) {
        if (spec != null) spec.setDanmaku(getSelectedDanmaku(items));
        notifyDanmakuSourceChanged();
    }

    private void notifyDanmakuSourceChanged() {
        callback.onDanmakuSourceChanged(getSelectedDanmakuUri());
    }

    public MediaMetadata getMetadata() {
        return spec != null ? spec.getMetadata() : null;
    }

    public void setMetadata(MediaMetadata data) {
        if (spec != null) spec.setMetadata(data);
        MediaItem current = player.getCurrentMediaItem();
        if (current != null) {
            player.replaceMediaItem(
                player.getCurrentMediaItemIndex(),
                current.buildUpon().setMediaMetadata(data).build()
            );
        }
    }

    public Map<String, String> getHeaders() {
        return spec == null || spec.getHeaders() == null ? new HashMap<>() : spec.getHeaders();
    }

    public float getSpeed() {
        return player.getPlaybackParameters().speed;
    }

    public boolean isEmpty() {
        return spec == null || TextUtils.isEmpty(spec.getUrl());
    }

    public boolean isPortrait() {
        return getVideoHeight() > getVideoWidth();
    }

    public boolean isLandscape() {
        return getVideoWidth() > getVideoHeight();
    }

    public boolean isLive() {
        return engine.isLive();
    }

    public boolean isVod() {
        return engine.isVod();
    }

    public boolean haveTrack(int type) {
        return TrackUtil.count(getCurrentTracks(), type) > 0;
    }

    // Media3 不支持 Edition
    public boolean haveEdition() {
        return false;
    }

    // Media3 不支持 Chapter
    public boolean haveChapter() {
        return false;
    }

    private int getVideoWidth() {
        return player.getVideoSize().width;
    }

    private int getVideoHeight() {
        return player.getVideoSize().height;
    }
}
