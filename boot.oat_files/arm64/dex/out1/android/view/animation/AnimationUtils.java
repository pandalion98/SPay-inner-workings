package android.view.animation;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.content.res.XmlResourceParser;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.animation.interpolator.BackEaseIn;
import android.view.animation.interpolator.BackEaseInOut;
import android.view.animation.interpolator.BackEaseOut;
import android.view.animation.interpolator.BounceEaseIn;
import android.view.animation.interpolator.BounceEaseInOut;
import android.view.animation.interpolator.BounceEaseOut;
import android.view.animation.interpolator.CircEaseIn;
import android.view.animation.interpolator.CircEaseInOut;
import android.view.animation.interpolator.CircEaseOut;
import android.view.animation.interpolator.CubicEaseIn;
import android.view.animation.interpolator.CubicEaseInOut;
import android.view.animation.interpolator.CubicEaseOut;
import android.view.animation.interpolator.ElasticEaseIn;
import android.view.animation.interpolator.ElasticEaseInOut;
import android.view.animation.interpolator.ElasticEaseOut;
import android.view.animation.interpolator.ExpoEaseIn;
import android.view.animation.interpolator.ExpoEaseInOut;
import android.view.animation.interpolator.ExpoEaseOut;
import android.view.animation.interpolator.QuadEaseIn;
import android.view.animation.interpolator.QuadEaseInOut;
import android.view.animation.interpolator.QuadEaseOut;
import android.view.animation.interpolator.QuartEaseIn;
import android.view.animation.interpolator.QuartEaseInOut;
import android.view.animation.interpolator.QuartEaseOut;
import android.view.animation.interpolator.QuintEaseIn;
import android.view.animation.interpolator.QuintEaseInOut;
import android.view.animation.interpolator.QuintEaseOut;
import android.view.animation.interpolator.QuintOut50;
import android.view.animation.interpolator.QuintOut80;
import android.view.animation.interpolator.SineEaseIn;
import android.view.animation.interpolator.SineEaseInOut;
import android.view.animation.interpolator.SineEaseOut;
import android.view.animation.interpolator.SineIn33;
import android.view.animation.interpolator.SineInOut33;
import android.view.animation.interpolator.SineInOut50;
import android.view.animation.interpolator.SineInOut60;
import android.view.animation.interpolator.SineInOut70;
import android.view.animation.interpolator.SineInOut80;
import android.view.animation.interpolator.SineInOut90;
import android.view.animation.interpolator.SineOut33;
import com.android.internal.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimationUtils {
    private static final int SEQUENTIALLY = 1;
    private static final int TOGETHER = 0;

    public static long currentAnimationTimeMillis() {
        return SystemClock.uptimeMillis();
    }

    public static Animation loadAnimation(Context context, int id) throws NotFoundException {
        NotFoundException rnf;
        XmlResourceParser parser = null;
        try {
            parser = context.getResources().getAnimation(id);
            Animation createAnimationFromXml = createAnimationFromXml(context, parser);
            if (parser != null) {
                parser.close();
            }
            return createAnimationFromXml;
        } catch (XmlPullParserException ex) {
            rnf = new NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } catch (IOException ex2) {
            rnf = new NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
            rnf.initCause(ex2);
            throw rnf;
        } catch (Throwable th) {
            if (parser != null) {
                parser.close();
            }
        }
    }

    private static Animation createAnimationFromXml(Context c, XmlPullParser parser) throws XmlPullParserException, IOException {
        return createAnimationFromXml(c, parser, null, Xml.asAttributeSet(parser));
    }

    private static Animation createAnimationFromXml(Context c, XmlPullParser parser, AnimationSet parent, AttributeSet attrs) throws XmlPullParserException, IOException {
        Animation anim = null;
        int depth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if ((type != 3 || parser.getDepth() > depth) && type != 1) {
                if (type == 2) {
                    String name = parser.getName();
                    if (name.equals("set")) {
                        anim = new AnimationSet(c, attrs);
                        createAnimationFromXml(c, parser, (AnimationSet) anim, attrs);
                    } else if (name.equals("alpha")) {
                        anim = new AlphaAnimation(c, attrs);
                    } else if (name.equals("scale")) {
                        anim = new ScaleAnimation(c, attrs);
                    } else if (name.equals("rotate")) {
                        anim = new RotateAnimation(c, attrs);
                    } else if (name.equals("translate")) {
                        anim = new TranslateAnimation(c, attrs);
                    } else {
                        throw new RuntimeException("Unknown animation name: " + parser.getName());
                    }
                    if (parent != null) {
                        parent.addAnimation(anim);
                    }
                }
            }
        }
        return anim;
    }

    public static LayoutAnimationController loadLayoutAnimation(Context context, int id) throws NotFoundException {
        NotFoundException rnf;
        XmlResourceParser parser = null;
        try {
            parser = context.getResources().getAnimation(id);
            LayoutAnimationController createLayoutAnimationFromXml = createLayoutAnimationFromXml(context, parser);
            if (parser != null) {
                parser.close();
            }
            return createLayoutAnimationFromXml;
        } catch (XmlPullParserException ex) {
            rnf = new NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } catch (IOException ex2) {
            rnf = new NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
            rnf.initCause(ex2);
            throw rnf;
        } catch (Throwable th) {
            if (parser != null) {
                parser.close();
            }
        }
    }

    private static LayoutAnimationController createLayoutAnimationFromXml(Context c, XmlPullParser parser) throws XmlPullParserException, IOException {
        return createLayoutAnimationFromXml(c, parser, Xml.asAttributeSet(parser));
    }

    private static LayoutAnimationController createLayoutAnimationFromXml(Context c, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        LayoutAnimationController controller = null;
        int depth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if ((type != 3 || parser.getDepth() > depth) && type != 1) {
                if (type == 2) {
                    String name = parser.getName();
                    if ("layoutAnimation".equals(name)) {
                        controller = new LayoutAnimationController(c, attrs);
                    } else if ("gridLayoutAnimation".equals(name)) {
                        controller = new GridLayoutAnimationController(c, attrs);
                    } else {
                        throw new RuntimeException("Unknown layout animation name: " + name);
                    }
                }
            }
        }
        return controller;
    }

    public static Animation makeInAnimation(Context c, boolean fromLeft) {
        Animation a;
        if (fromLeft) {
            a = loadAnimation(c, R.anim.slide_in_left);
        } else {
            a = loadAnimation(c, R.anim.slide_in_right);
        }
        a.setInterpolator(new DecelerateInterpolator());
        a.setStartTime(currentAnimationTimeMillis());
        return a;
    }

    public static Animation makeOutAnimation(Context c, boolean toRight) {
        Animation a;
        if (toRight) {
            a = loadAnimation(c, R.anim.slide_out_right);
        } else {
            a = loadAnimation(c, R.anim.slide_out_left);
        }
        a.setInterpolator(new AccelerateInterpolator());
        a.setStartTime(currentAnimationTimeMillis());
        return a;
    }

    public static Animation makeInChildBottomAnimation(Context c) {
        Animation a = loadAnimation(c, R.anim.slide_in_child_bottom);
        a.setInterpolator(new AccelerateInterpolator());
        a.setStartTime(currentAnimationTimeMillis());
        return a;
    }

    public static Interpolator loadInterpolator(Context context, int id) throws NotFoundException {
        NotFoundException rnf;
        XmlResourceParser parser = null;
        try {
            parser = context.getResources().getAnimation(id);
            Interpolator createInterpolatorFromXml = createInterpolatorFromXml(context.getResources(), context.getTheme(), parser);
            if (parser != null) {
                parser.close();
            }
            return createInterpolatorFromXml;
        } catch (XmlPullParserException ex) {
            rnf = new NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } catch (IOException ex2) {
            rnf = new NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
            rnf.initCause(ex2);
            throw rnf;
        } catch (Throwable th) {
            if (parser != null) {
                parser.close();
            }
        }
    }

    public static Interpolator loadInterpolator(Resources res, Theme theme, int id) throws NotFoundException {
        NotFoundException rnf;
        XmlResourceParser xmlResourceParser = null;
        try {
            xmlResourceParser = res.getAnimation(id);
            Interpolator createInterpolatorFromXml = createInterpolatorFromXml(res, theme, xmlResourceParser);
            if (xmlResourceParser != null) {
                xmlResourceParser.close();
            }
            return createInterpolatorFromXml;
        } catch (XmlPullParserException ex) {
            rnf = new NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
            rnf.initCause(ex);
            throw rnf;
        } catch (IOException ex2) {
            rnf = new NotFoundException("Can't load animation resource ID #0x" + Integer.toHexString(id));
            rnf.initCause(ex2);
            throw rnf;
        } catch (Throwable th) {
            if (xmlResourceParser != null) {
                xmlResourceParser.close();
            }
        }
    }

    private static Interpolator createInterpolatorFromXml(Resources res, Theme theme, XmlPullParser parser) throws XmlPullParserException, IOException {
        BaseInterpolator interpolator = null;
        int depth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if ((type != 3 || parser.getDepth() > depth) && type != 1) {
                if (type == 2) {
                    AttributeSet attrs = Xml.asAttributeSet(parser);
                    String name = parser.getName();
                    if (name.equals("linearInterpolator")) {
                        interpolator = new LinearInterpolator();
                    } else if (name.equals("accelerateInterpolator")) {
                        interpolator = new AccelerateInterpolator(res, theme, attrs);
                    } else if (name.equals("decelerateInterpolator")) {
                        interpolator = new DecelerateInterpolator(res, theme, attrs);
                    } else if (name.equals("accelerateDecelerateInterpolator")) {
                        interpolator = new AccelerateDecelerateInterpolator();
                    } else if (name.equals("cycleInterpolator")) {
                        interpolator = new CycleInterpolator(res, theme, attrs);
                    } else if (name.equals("anticipateInterpolator")) {
                        interpolator = new AnticipateInterpolator(res, theme, attrs);
                    } else if (name.equals("overshootInterpolator")) {
                        interpolator = new OvershootInterpolator(res, theme, attrs);
                    } else if (name.equals("anticipateOvershootInterpolator")) {
                        interpolator = new AnticipateOvershootInterpolator(res, theme, attrs);
                    } else if (name.equals("bounceInterpolator")) {
                        interpolator = new BounceInterpolator();
                    } else if (name.equals("pathInterpolator")) {
                        interpolator = new PathInterpolator(res, theme, attrs);
                    } else if (name.equals("backEaseIn")) {
                        interpolator = new BackEaseIn(res, theme, attrs);
                    } else if (name.equals("backEaseOut")) {
                        interpolator = new BackEaseOut(res, theme, attrs);
                    } else if (name.equals("backEaseInOut")) {
                        interpolator = new BackEaseInOut(res, theme, attrs);
                    } else if (name.equals("bounceEaseIn")) {
                        interpolator = new BounceEaseIn();
                    } else if (name.equals("bounceEaseOut")) {
                        interpolator = new BounceEaseOut();
                    } else if (name.equals("bounceEaseInOut")) {
                        interpolator = new BounceEaseInOut();
                    } else if (name.equals("circEaseIn")) {
                        interpolator = new CircEaseIn();
                    } else if (name.equals("circEaseOut")) {
                        interpolator = new CircEaseOut();
                    } else if (name.equals("circEaseInOut")) {
                        interpolator = new CircEaseInOut();
                    } else if (name.equals("cubicEaseIn")) {
                        interpolator = new CubicEaseIn();
                    } else if (name.equals("cubicEaseOut")) {
                        interpolator = new CubicEaseOut();
                    } else if (name.equals("cubicEaseInOut")) {
                        interpolator = new CubicEaseInOut();
                    } else if (name.equals("elasticEaseIn")) {
                        interpolator = new ElasticEaseIn(res, theme, attrs);
                    } else if (name.equals("elasticEaseOut")) {
                        interpolator = new ElasticEaseOut(res, theme, attrs);
                    } else if (name.equals("elasticEaseInOut")) {
                        interpolator = new ElasticEaseInOut(res, theme, attrs);
                    } else if (name.equals("expoEaseIn")) {
                        interpolator = new ExpoEaseIn();
                    } else if (name.equals("expoEaseOut")) {
                        interpolator = new ExpoEaseOut();
                    } else if (name.equals("expoEaseInOut")) {
                        interpolator = new ExpoEaseInOut();
                    } else if (name.equals("quadEaseIn")) {
                        interpolator = new QuadEaseIn();
                    } else if (name.equals("quadEaseOut")) {
                        interpolator = new QuadEaseOut();
                    } else if (name.equals("quadEaseInOut")) {
                        interpolator = new QuadEaseInOut();
                    } else if (name.equals("quartEaseIn")) {
                        interpolator = new QuartEaseIn();
                    } else if (name.equals("quartEaseOut")) {
                        interpolator = new QuartEaseOut();
                    } else if (name.equals("quartEaseInOut")) {
                        interpolator = new QuartEaseInOut();
                    } else if (name.equals("quintEaseIn")) {
                        interpolator = new QuintEaseIn();
                    } else if (name.equals("quintEaseOut")) {
                        interpolator = new QuintEaseOut();
                    } else if (name.equals("quintEaseInOut")) {
                        interpolator = new QuintEaseInOut();
                    } else if (name.equals("sineEaseIn")) {
                        interpolator = new SineEaseIn();
                    } else if (name.equals("sineEaseOut")) {
                        interpolator = new SineEaseOut();
                    } else if (name.equals("sineEaseInOut")) {
                        interpolator = new SineEaseInOut();
                    } else if (name.equals("quintOut50")) {
                        interpolator = new QuintOut50();
                    } else if (name.equals("quintOut80")) {
                        interpolator = new QuintOut80();
                    } else if (name.equals("sineIn33")) {
                        interpolator = new SineIn33();
                    } else if (name.equals("sineInOut33")) {
                        interpolator = new SineInOut33();
                    } else if (name.equals("sineInOut50")) {
                        interpolator = new SineInOut50();
                    } else if (name.equals("sineInOut60")) {
                        interpolator = new SineInOut60();
                    } else if (name.equals("sineInOut70")) {
                        interpolator = new SineInOut70();
                    } else if (name.equals("sineInOut80")) {
                        interpolator = new SineInOut80();
                    } else if (name.equals("sineInOut90")) {
                        interpolator = new SineInOut90();
                    } else if (name.equals("sineOut33")) {
                        interpolator = new SineOut33();
                    } else {
                        throw new RuntimeException("Unknown interpolator name: " + parser.getName());
                    }
                }
            }
        }
        return interpolator;
    }
}
