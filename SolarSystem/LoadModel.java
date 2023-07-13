package SolarSystem;

import org.jogamp.java3d.Shape3D;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.java3d.loaders.IncorrectFormatException;
import org.jogamp.java3d.loaders.ParsingErrorException;
import org.jogamp.java3d.loaders.Scene;
import org.jogamp.java3d.loaders.objectfile.ObjectFile;
import org.jogamp.vecmath.Vector3f;

import java.io.FileNotFoundException;

public class LoadModel {
    private TransformGroup tg;
    public LoadModel(String str, Vector3f v3f){
        int flags = ObjectFile.RESIZE | ObjectFile.TRIANGULATE | ObjectFile.STRIPIFY; // flags of ObjectFile
        ObjectFile ring = new ObjectFile(flags,(float)(60*Math.PI/180.0)); // use flags and radians to create the ObjectFile
        Scene s = null;
        try {
            s = ring.load(str);
        }
        // catch exceptions may happen during load()
        catch(FileNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        }
        catch(ParsingErrorException e){
            System.err.println(e);
            System.exit(1);
        }
        catch(IncorrectFormatException e) {
            System.err.println(e);
            System.exit(1);
        }

        Transform3D tf = new Transform3D();
        tf.setScale(0.1f);
        tf.setTranslation(v3f);
        tg = new TransformGroup(tf);

        Shape3D myShape = (Shape3D)(s.getSceneGroup().getChild(0)); // transfer the Node to Shape3D
        tg.addChild(myShape.getParent()); // we operate on the child above, a child can only have one parent, so add the parent to tg
    }
    public TransformGroup loadModel(){
        return tg;
    }
}
