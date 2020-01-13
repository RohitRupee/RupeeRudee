package views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public abstract  class HTextView extends TextView {
    public HTextView(Context context) {
        this(context, null);
    }

    public HTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract void setAnimationListener(Animation.AnimationListener listener);

    public abstract void setProgress(float progress);

    public abstract void animateText(CharSequence text);
}