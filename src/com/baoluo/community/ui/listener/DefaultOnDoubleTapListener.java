package com.baoluo.community.ui.listener;

import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.baoluo.community.ui.customview.ImgViewAttacher;

public class DefaultOnDoubleTapListener implements GestureDetector.OnDoubleTapListener {

    private ImgViewAttacher imgViewAttacher;

    /**
     * Default constructor
     *
     * @param ImgViewAttacher ImgViewAttacher to bind to
     */
    public DefaultOnDoubleTapListener(ImgViewAttacher imgViewAttacher) {
        setPhotoViewAttacher(imgViewAttacher);
    }

    /**
     * Allows to change ImgViewAttacher within range of single instance
     *
     * @param newPhotoViewAttacher ImgViewAttacher to bind to
     */
    public void setPhotoViewAttacher(ImgViewAttacher newPhotoViewAttacher) {
        this.imgViewAttacher = newPhotoViewAttacher;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        if (this.imgViewAttacher == null)
            return false;

        ImageView imageView = imgViewAttacher.getImageView();

        if (null != imgViewAttacher.getOnPhotoTapListener()) {
            final RectF displayRect = imgViewAttacher.getDisplayRect();

            if (null != displayRect) {
                final float x = e.getX(), y = e.getY();

                // Check to see if the user tapped on the photo
                if (displayRect.contains(x, y)) {

                    float xResult = (x - displayRect.left)
                            / displayRect.width();
                    float yResult = (y - displayRect.top)
                            / displayRect.height();

                    imgViewAttacher.getOnPhotoTapListener().onPhotoTap(imageView, xResult, yResult);
                    return true;
                }
            }
        }
        if (null != imgViewAttacher.getOnViewTapListener()) {
            imgViewAttacher.getOnViewTapListener().onViewTap(imageView, e.getX(), e.getY());
        }

        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent ev) {
        if (imgViewAttacher == null)
            return false;

        try {
            float scale = imgViewAttacher.getScale();
            float x = ev.getX();
            float y = ev.getY();

            if (scale < imgViewAttacher.getMediumScale()) {
                imgViewAttacher.setScale(imgViewAttacher.getMediumScale(), x, y, true);
            } else if (scale >= imgViewAttacher.getMediumScale() && scale < imgViewAttacher.getMaximumScale()) {
                imgViewAttacher.setScale(imgViewAttacher.getMaximumScale(), x, y, true);
            } else {
                imgViewAttacher.setScale(imgViewAttacher.getMinimumScale(), x, y, true);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Can sometimes happen when getX() and getY() is called
        }

        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        // Wait for the confirmed onDoubleTap() instead
        return false;
    } 
}
