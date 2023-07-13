package SolarSystem;

import org.jogamp.java3d.*;

public class revolutionPath extends Shape3D {
    public revolutionPath(float radius) {
        this.addGeometry(this.CreateLine(radius));
    }

    LineArray CreateLine(float radius) {
        int n = 1000;
        float theta = (float) (2 * Math.PI / (double) n);
        LineArray line = new LineArray(n, 5);
        float[] vert = new float[n * 3];
        float[] color = new float[n * 3];
        for (int i = 0; i < n * 3; vert[i++] = radius * (float) Math.sin((double) ((float) i * theta))) {
            color[i] = 1.0f;
            vert[i++] = radius * (float) Math.cos((double) ((float) i * theta));
            color[i] = 1.0f;
            vert[i++] = 0.0f;
            color[i] = 1.0f;
        }
        line.setCoordinates(0, vert);
        line.setColors(0, color);
        LineAttributes la = new LineAttributes();
        la.setLineWidth(2.0f);
        la.setLineAntialiasingEnable(true);
        TransparencyAttributes ta = new TransparencyAttributes();
        ta.setTransparencyMode(TransparencyAttributes.BLENDED);
        ta.setTransparency(0.8f);
        Appearance ap = new Appearance();
        ap.setLineAttributes(la);
        ap.setTransparencyAttributes(ta);
        this.setAppearance(ap);
        return line;
    }
}