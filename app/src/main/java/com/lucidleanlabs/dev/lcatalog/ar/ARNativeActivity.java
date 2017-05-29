package com.lucidleanlabs.dev.lcatalog.ar;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.lucidleanlabs.dev.lcatalog.R;

import org.artoolkit.ar.base.ARActivity;
import org.artoolkit.ar.base.rendering.ARRenderer;

public class ARNativeActivity extends ARActivity {

    private ARNativeRenderer arNativeRenderer = new ARNativeRenderer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arnative);
    }

    public void onStop() {
        ARNativeRenderer.demoShutdown();
        super.onStop();
    }

    @Override
    protected ARRenderer supplyRenderer() {
        return arNativeRenderer;
    }

    @Override
    protected FrameLayout supplyFrameLayout() {
        return (FrameLayout) this.findViewById(R.id.arFrameLayout);

    }
}
