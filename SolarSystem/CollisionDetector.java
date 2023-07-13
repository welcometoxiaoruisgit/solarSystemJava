package SolarSystem;

import org.jogamp.java3d.*;

import java.util.Iterator;

public class CollisionDetector extends Behavior {
    private boolean inCollision = false;
    private Shape3D shape;
    private ColoringAttributes shapeColoring;
    private Appearance shapeAppearance;
    private WakeupOnCollisionEntry wEnter;
    private WakeupOnCollisionExit wExit;

    public CollisionDetector(Shape3D s){
        shape = s;
        shapeAppearance = shape.getAppearance();
        shapeColoring = shapeAppearance.getColoringAttributes();
        inCollision = false;
    }
    public void initialize(){
        wEnter = new WakeupOnCollisionEntry(shape);
        wExit = new WakeupOnCollisionExit(shape);
        wakeupOn(wEnter);
    }

    public void processStimulus(Iterator<WakeupCriterion> criteria){
        inCollision = !inCollision;
        if(inCollision){
            System.out.println("Collision happened!");
            wakeupOn(wExit);
        }
        else{
            shapeAppearance.setColoringAttributes(shapeColoring);
            wakeupOn(wEnter);
        }
    }
}
