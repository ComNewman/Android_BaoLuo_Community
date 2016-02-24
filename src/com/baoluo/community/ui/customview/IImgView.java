package com.baoluo.community.ui.customview;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageView;

public interface IImgView {
	public static final float DEFAULT_MAX_SCALE = 3.0f;
	public static final float DEFAULT_MID_SCALE = 1.75f;
	public static final float DEFAULT_MIN_SCALE = 1.0f;
	public static final int DEFAULT_ZOOM_DURATION = 200;

	boolean canZoom();

	RectF getDisplayRect();

	boolean setDisplayMatrix(Matrix finalMatrix);

	Matrix getDisplayMatrix();

	float getMinimumScale();

	float getMediumScale();

	float getMaximumScale();

	float getScale();

	ImageView.ScaleType getScaleType();

	void setAllowParentInterceptOnEdge(boolean allow);

	void setMinimumScale(float minimumScale);

	void setMediumScale(float mediumScale);

	void setMaximumScale(float maximumScale);

	void setOnLongClickListener(View.OnLongClickListener listener);

	void setOnMatrixChangeListener(
			ImgViewAttacher.OnMatrixChangedListener listener);

	void setOnPhotoTapListener(ImgViewAttacher.OnPhotoTapListener listener);

	ImgViewAttacher.OnPhotoTapListener getOnPhotoTapListener();

	void setOnViewTapListener(ImgViewAttacher.OnViewTapListener listener);

	/**
	 * Enables rotation via PhotoView internal functions.
	 * 
	 * @param rotationDegree
	 *            - Degree to rotate PhotoView to, should be in range 0 to 360
	 */
	void setRotationTo(float rotationDegree);

	/**
	 * Enables rotation via PhotoView internal functions.
	 * 
	 * @param rotationDegree
	 *            - Degree to rotate PhotoView by, should be in range 0 to 360
	 */
	void setRotationBy(float rotationDegree);

	/**
	 * Returns a callback listener to be invoked when the View is tapped with a
	 * single tap.
	 * 
	 * @return PhotoViewAttacher.OnViewTapListener currently set, may be null
	 */
	ImgViewAttacher.OnViewTapListener getOnViewTapListener();

	/**
	 * Changes the current scale to the specified value.
	 * 
	 * @param scale
	 *            - Value to scale to
	 */
	void setScale(float scale);

	/**
	 * Changes the current scale to the specified value.
	 * 
	 * @param scale
	 *            - Value to scale to
	 * @param animate
	 *            - Whether to animate the scale
	 */
	void setScale(float scale, boolean animate);

	/**
	 * Changes the current scale to the specified value, around the given focal
	 * point.
	 * 
	 * @param scale
	 *            - Value to scale to
	 * @param focalX
	 *            - X Focus Point
	 * @param focalY
	 *            - Y Focus Point
	 * @param animate
	 *            - Whether to animate the scale
	 */
	void setScale(float scale, float focalX, float focalY, boolean animate);

	void setScaleType(ImageView.ScaleType scaleType);

	void setZoomable(boolean zoomable);

	void setPhotoViewRotation(float rotationDegree);

	Bitmap getVisibleRectangleBitmap();

	void setZoomTransitionDuration(int milliseconds);

	IImgView getIPhotoViewImplementation();

	public void setOnDoubleTapListener(
			GestureDetector.OnDoubleTapListener newOnDoubleTapListener);

}
