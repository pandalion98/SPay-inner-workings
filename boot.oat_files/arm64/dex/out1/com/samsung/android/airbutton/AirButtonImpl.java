package com.samsung.android.airbutton;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import com.samsung.android.sdk.look.airbutton.SlookAirButtonAdapter;

public final class AirButtonImpl {
    public static final int DIRECTION_AUTO = -1;
    public static final int DIRECTION_LEFT = 3;
    public static final int DIRECTION_LOWER = 2;
    public static final int DIRECTION_RIGHT = 4;
    public static final int DIRECTION_UPPER = 1;
    public static final int GLOBAL_APP_EASY_CLIP = 5;
    public static final int GLOBAL_APP_FLASH_ANNO = 2;
    public static final int GLOBAL_APP_PEN_WINDOW = 4;
    public static final int GLOBAL_APP_QUICK_MEMO = 0;
    public static final int GLOBAL_APP_RAKEINSELECT = 7;
    public static final int GLOBAL_APP_SCRAPBOOKER = 1;
    public static final int GLOBAL_APP_S_FINDER = 3;
    public static final int GLOBAL_APP_S_NOTE = 6;
    public static final int GRAVITY_AUTO = -1;
    public static final int GRAVITY_BOTTOM = 2;
    public static final int GRAVITY_HOVER_POINT = 5;
    public static final int GRAVITY_LEFT = 3;
    public static final int GRAVITY_RIGHT = 4;
    public static final int GRAVITY_TOP = 1;
    public static final int UI_TYPE_GLOBAL = 3;
    public static final int UI_TYPE_LIST = 2;
    public static final int UI_TYPE_MENU = 1;

    public interface OnItemSelectedListener {
        void onItemSelected(View view, int i, Object obj);
    }

    public interface OnStateChangedListener {
        void onDismiss();

        void onHide();

        void onShow();
    }

    public static class OnStateChangedListenerImpl implements OnStateChangedListener {
        public void onShow() {
        }

        public void onHide() {
        }

        public void onDismiss() {
        }

        public void onShow(View parentView) {
        }

        public void onHide(View parentView) {
        }

        public void onDismiss(View parentView) {
        }
    }

    public AirButtonImpl(Context context, boolean enabled) {
    }

    public AirButtonImpl(View parentView, SlookAirButtonAdapter adapter, int UIType) {
        this(parentView, adapter, UIType, true);
    }

    public AirButtonImpl(View parentView, SlookAirButtonAdapter adapter, int UIType, boolean enabled) {
    }

    public boolean onHover(View v, MotionEvent event) {
        return false;
    }

    public void onTouchDownForGA(int buttonState) {
    }

    public void onTouchUpForGA(int buttonState) {
    }

    public void onHoverEnter(MotionEvent event) {
    }

    public void onHoverMove(MotionEvent event) {
    }

    public void onHoverExit(MotionEvent event) {
    }

    public void onHoverButtonSecondary(MotionEvent event) {
    }

    public void show() {
    }

    public void show(MotionEvent event) {
    }

    public void show(float hoverX, float hoverY) {
    }

    public void hide() {
    }

    public void dismiss() {
    }

    public void enable() {
    }

    public void disable() {
    }

    public void showHoverPointer() {
    }

    public void hideHoverPointer() {
    }

    public boolean isAirButtonSettingEnabled() {
        return false;
    }

    public boolean isSpenDetachSettingEnabled() {
        return false;
    }

    public boolean isCoverViewOpened() {
        return false;
    }

    public boolean isPenWindowMode() {
        return false;
    }

    public void initSideButtonState() {
    }

    public void setEnabled(boolean enable) {
    }

    public boolean isEnabled() {
        return false;
    }

    public void setOnStateChangedListener(OnStateChangedListener listener) {
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
    }

    public void setGravity(int gravity) {
    }

    public int getGravity() {
        return -1;
    }

    public void setDirection(int direction) {
    }

    public int getDirection() {
        return -1;
    }

    public void setPosition(int x, int y) {
    }

    public void setMultiSelectionEnabled(boolean enable) {
    }

    public boolean isMultiSelectionEnabled() {
        return false;
    }

    public void setScrollEnabled(boolean enable) {
    }

    public boolean isScrollEnabled() {
        return false;
    }

    public void setBounceEffectEnabled(boolean enable) {
    }

    public boolean isBounceEffectEnabled() {
        return false;
    }

    public boolean isShowing() {
        return false;
    }

    public boolean isHoverPointerShowing() {
        return false;
    }

    public void setHoverPointerEnabled(boolean enable) {
    }

    public boolean isHoverPointerEnabled() {
        return false;
    }

    public void setParentView(View view) {
    }

    public void setAdapter(SlookAirButtonAdapter adapter) {
    }

    public SlookAirButtonAdapter getAdapter() {
        return null;
    }

    public View getParentView() {
        return null;
    }

    public void linkWithParentView() {
    }

    public void unlinkWithParentView() {
    }
}
