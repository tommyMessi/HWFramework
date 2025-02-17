package ohos.agp.styles.attributes;

import ohos.agp.components.Attr;

public class ImageAttrsConstants extends ViewAttrsConstants {
    public static final String CLIP_DIRECTION = "clip_direction";
    public static final String CLIP_GRAVITY = "clip_gravity";
    public static final String IMAGE_SRC = "image_src";
    public static final String IMAGE_URI = "image_uri";
    public static final String SCALE_TYPE = "scale_type";

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // ohos.agp.styles.attributes.ViewAttrsConstants
    public Attr.AttrType getType(String str) {
        char c;
        switch (str.hashCode()) {
            case -1311260336:
                if (str.equals(CLIP_DIRECTION)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -877825792:
                if (str.equals(IMAGE_SRC)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -877823864:
                if (str.equals(IMAGE_URI)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1924300047:
                if (str.equals(SCALE_TYPE)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 2005298271:
                if (str.equals(CLIP_GRAVITY)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c == 0 || c == 1 || c == 2) {
            return Attr.AttrType.INT;
        }
        if (c == 3) {
            return Attr.AttrType.STRING;
        }
        if (c != 4) {
            return super.getType(str);
        }
        return Attr.AttrType.ELEMENT;
    }
}
