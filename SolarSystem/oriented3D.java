package SolarSystem;

import org.jogamp.java3d.*;
import org.jogamp.vecmath.Color3f;
import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3f;

import java.awt.*;

public class oriented3D {
    private TransformGroup tg1;
    public oriented3D(String str, Vector3f v3f){
        Font3D font3D = new Font3D(new Font("Helvetica", Font.PLAIN, 1), new FontExtrusion());
        Text3D textGeom = new Text3D(font3D, str, new Point3f(0.0f, 0.0f, 0.0f));
        Appearance textApp = new Appearance();
        ColoringAttributes textColor = new ColoringAttributes();
        textColor.setColor(new Color3f(0.0f, 1.0f, 1.0f));
        textApp.setColoringAttributes(textColor);
        OrientedShape3D label = new OrientedShape3D();
        label.setScale(0.3f);
        label.setGeometry(textGeom);
        label.setAppearance(textApp);
        label.setRotationPoint(new Point3f(0f, 0, 0));
        label.setBounds(new BoundingSphere());
        Transform3D tf1 = new Transform3D();
        tf1.setScale(0.1f);
        tf1.setTranslation(v3f);
        tg1 = new TransformGroup(tf1);
        tg1.addChild(label);
    }
    public TransformGroup orientedText(){
        return tg1;
    }
}
