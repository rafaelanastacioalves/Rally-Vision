package anastasoft.rallyvision.activity.customfontdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import anastasoft.rallyvisionaluguel.R;

/**
 * Created by rafaelanastacioalves on 18/09/14.
 */

public class Led_text_view extends TextView {

    public Led_text_view(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public Led_text_view(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public Led_text_view(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {

        if (attrs!=null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,   R.styleable.Fonts);
            String fontName = a.getString(R.styleable.Fonts_font);
            if (fontName!=null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/"+fontName);
                setTypeface(myTypeface);
            }
            a.recycle();
        }
    }
}
