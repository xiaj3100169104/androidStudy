package tech.gaolinfeng.imagecrop.lib;

import android.graphics.Matrix;
import android.graphics.RectF;

/**
 * Created by gaolf on 15/12/21.
 */
public class MatrixHelper {

    private RectF intrinsicRectF;
    private RectF transformedRectF;

    public void init(Matrix matrix, int intrinsicWidth, int intrinsicHeight) {
        intrinsicRectF = new RectF(0, 0, intrinsicWidth, intrinsicHeight);
        transformedRectF = new RectF();
        update(matrix);
    }

    public void update(Matrix matrix) {
        matrix.mapRect(transformedRectF, intrinsicRectF);
    }

    public RectF getTransformedRectF() {
        return transformedRectF;
    }

    public float getLeft() {
        return transformedRectF.left;
    }

    public float getRight() {
        return transformedRectF.right;
    }

    public float getWidth() {
        return transformedRectF.width();
    }

    public float getTop() {
        return transformedRectF.top;
    }

    public float getBottom() {
        return transformedRectF.bottom;
    }

    public float getHeight() {
        return transformedRectF.height();
    }

    public float getIntrinsicWidth() {
        return intrinsicRectF.right;
    }

    public float getIntrinsicHeight() {
        return intrinsicRectF.bottom;
    }
}
