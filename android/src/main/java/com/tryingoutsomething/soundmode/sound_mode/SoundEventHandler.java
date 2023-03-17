package com.tryingoutsomething.soundmode.sound_mode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;

import java.util.Objects;

import io.flutter.plugin.common.EventChannel;

public class SoundEventHandler implements EventChannel.StreamHandler {
    SoundEventHandler(Context context) {
        this.context = context;
    }

    Context context;
    final IntentFilter filter = new IntentFilter(
            AudioManager.RINGER_MODE_CHANGED_ACTION);
    BroadcastReceiver receiver;

    @Override
    public void onListen(Object o, final EventChannel.EventSink eventSink) {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Objects.equals(intent.getAction(), AudioManager.RINGER_MODE_CHANGED_ACTION)) {
                    switch (intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, -1)) {
                        case AudioManager.RINGER_MODE_VIBRATE:
                            eventSink.success("vibration");
                        case AudioManager.RINGER_MODE_SILENT:
                            eventSink.success("silent");
                            break;
                        default:
                            eventSink.success("normal");
                            break;
                    }

                }
            }
        };
        context.registerReceiver(receiver, filter);
    }

    @Override
    public void onCancel(Object o) {
        if (receiver != null) {
            context.unregisterReceiver(receiver);
        }
    }
}
