package pl.edu.agh.io.android.adapters;

/*
 * Copyright (C) 2010 Eric Harlow
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.*;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class DragNDropListView extends ListView {

    boolean mDragMode;

    int mStartPosition;
    int mEndPosition;
    int mDragPointOffset;		//Used to adjust drag view location

    ImageView mDragView;
    DragListener mDragListener;
    DragNDropAdapter adapter;

    public DragNDropListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        this.adapter=(DragNDropAdapter)adapter;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        final int x = (int) ev.getX();
        final int y = (int) ev.getY();

        if (action == MotionEvent.ACTION_DOWN && x < 3*this.getWidth()/4) {
            mDragMode = true;
        }

        if (!mDragMode)
            return super.onTouchEvent(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartPosition = pointToPosition(x,y);
                if (mStartPosition != INVALID_POSITION) {
                    int mItemPosition = mStartPosition - getFirstVisiblePosition();
                    mDragPointOffset = y - getChildAt(mItemPosition).getTop();
                    mDragPointOffset -= ((int)ev.getRawY()) - y;
                    startDrag(mItemPosition,y);
                    drag(0,y);// replace 0 with x if desired
                }
                break;
            case MotionEvent.ACTION_MOVE:
                drag(0,y);// replace 0 with x if desired
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            default:
                mDragMode = false;
                mEndPosition = pointToPosition(x,y);
                if(mEndPosition == -1){
                    if(y<this.getY())
                        mEndPosition = 0;
                    else
                        mEndPosition = adapter.getCount() - 1;
                }
                stopDrag(mStartPosition - getFirstVisiblePosition());
                adapter.onDrop(mStartPosition,mEndPosition);
                break;
        }
        return true;
    }

    // move the drag view
    private void drag(int x, int y) {
        if (mDragView != null) {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) mDragView.getLayoutParams();
            layoutParams.x = x;
            layoutParams.y = y - mDragPointOffset;
            WindowManager mWindowManager = (WindowManager) getContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            mWindowManager.updateViewLayout(mDragView, layoutParams);

            if (mDragListener != null)
                mDragListener.onDrag(x, y, null);// change null to "this" when ready to use
        }
    }

    // enable the drag view for dragging
    private void startDrag(int itemIndex, int y) {
        stopDrag(itemIndex);

        View item = getChildAt(itemIndex);
        if (item == null) return;
        item.setDrawingCacheEnabled(true);
        if (mDragListener != null)
            mDragListener.onStartDrag(item);

        // Create a copy of the drawing cache so that it does not get recycled
        // by the framework when the list tries to clean up memory
        Bitmap bitmap = Bitmap.createBitmap(item.getDrawingCache());

        WindowManager.LayoutParams mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.gravity = Gravity.TOP;
        mWindowParams.x = 0;
        mWindowParams.y = y - mDragPointOffset;

        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.windowAnimations = 0;

        Context context = getContext();
        ImageView v = new ImageView(context);
        v.setImageBitmap(bitmap);

        WindowManager mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(v, mWindowParams);
        mDragView = v;
    }

    // destroy drag view
    private void stopDrag(int itemIndex) {
        if (mDragView != null) {
            if (mDragListener != null)
                mDragListener.onStopDrag(getChildAt(itemIndex));
            mDragView.setVisibility(GONE);
            WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
            wm.removeView(mDragView);
            mDragView.setImageDrawable(null);
            mDragView = null;
        }
    }


    /**
     * Implement to handle an item being dragged.
     *
     * @author Eric Harlow
     */
    public static interface DragListener {
        /**
         * Called when a drag starts.
         * @param itemView - the view of the item to be dragged i.e. the drag view
         */
        void onStartDrag(View itemView);

        /**
         * Called when a drag is to be performed.
         * @param x - horizontal coordinate of MotionEvent.
         * @param y - verital coordinate of MotionEvent.
         * @param listView - the listView
         */
        void onDrag(int x, int y, ListView listView);

        /**
         * Called when a drag stops.
         * Any changes in onStartDrag need to be undone here
         * so that the view can be used in the list again.
         * @param itemView - the view of the item to be dragged i.e. the drag view
         */
        void onStopDrag(View itemView);
    }
}