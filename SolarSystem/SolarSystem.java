package SolarSystem;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.jogamp.java3d.*;


import org.jdesktop.j3d.examples.sound.PointSoundBehavior;
import org.jdesktop.j3d.examples.sound.audio.JOALMixer;
import org.jogamp.java3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import org.jogamp.java3d.utils.behaviors.mouse.MouseRotate;
import org.jogamp.java3d.utils.geometry.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.java3d.utils.picking.PickResult;
import org.jogamp.java3d.utils.picking.PickTool;
import org.jogamp.java3d.utils.universe.*;
import org.jogamp.vecmath.*;

public class SolarSystem extends JPanel implements ActionListener, MouseListener, KeyListener {
    private static final long serialVersionUID = 1L;
    private static PickTool pickTool;
    Hashtable<String, KeyNavigatorBehavior> m_KeyHashtable = null;
    private Canvas3D[] canvas3D;
    Hashtable<String, MouseListener> m_MouseHashtable = null;
    static final int width = 450;                            // size of each Canvas3D
    static final int height = 450;
    private boolean hasWinner = false;
    private static TransformGroup Trans;
    private double rotateAngle = Math.PI / 60;
    private static Transform3D originalPosition = new Transform3D();
    private static BranchGroup bg = new BranchGroup();


    public BranchGroup solarSystemBG() {
        BranchGroup universe = new BranchGroup();
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        Trans = new TransformGroup();
        Trans.getTransform(originalPosition);
        Trans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Trans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        universe.addChild(Trans);

        MouseRotate behavior = new MouseRotate();
        behavior.setTransformGroup(Trans);
        behavior.setSchedulingBounds(bounds);
        universe.addChild(behavior);

        Appearance app = new Appearance();
        app.setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
        int primiflag = Primitive.GENERATE_NORMALS | Primitive.GENERATE_TEXTURE_COORDS;
        Sphere Earth = new Sphere(0.04f, primiflag, app);
        Sphere Sun = new Sphere(0.25f, primiflag, app);
        Sun.setUserData("Sun");
        Sun.setBounds(new BoundingSphere(new Point3d(0,0,0),100));
        Sun.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        Sun.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        Sun.getShape().getAppearance().setCapability(Appearance.ALLOW_TRANSPARENCY_ATTRIBUTES_WRITE);
        Sphere Moon = new Sphere(0.01f, primiflag, app);

        Sphere Mercury = new Sphere(0.01f, primiflag, app);
        Sphere Venus = new Sphere(0.04f, primiflag, app);
        Sphere Mars = new Sphere(0.035f, primiflag, app);
        Sphere Jupiter = new Sphere(0.07f, primiflag, app);
        Sphere Saturn = new Sphere(0.06f, primiflag, app);
        Sphere Uranus = new Sphere(0.04f, primiflag, app);
        Sphere Neptune = new Sphere(0.03f, primiflag, app);
        Sphere Comet = new Sphere(0.005f, primiflag, app);
        Appearance apComet = Comet.getAppearance();
        ColoringAttributes ca = new ColoringAttributes();
        ca.setColor(CommonsXZ.White);
        apComet.setColoringAttributes(ca);


        Trans.addChild(new revolutionPath(0.27f));
        Trans.addChild(new revolutionPath(0.318f));
        Trans.addChild(new revolutionPath(0.404f));
        Trans.addChild(new revolutionPath(0.48f));
        Trans.addChild(new revolutionPath(0.66f));
        Trans.addChild(new revolutionPath(0.79f));
        Trans.addChild(new revolutionPath(0.89f));
        Trans.addChild(new revolutionPath(0.956f));
        Earth.addChild(new revolutionPath(0.05f));


        // load the texture of the planet to each Sphere
        loadTexture("Sun.jpg", Sun);
        loadTexture("Mercury.jpg", Mercury);
        loadTexture("Venus.jpg", Venus);
        loadTexture("Earth.jpg", Earth);
        loadTexture("Moon.jpg", Moon);
        loadTexture("Mars.jpg", Mars);
        loadTexture("Jupiter.jpg", Jupiter);
        loadTexture("Saturn.jpg", Saturn);
        loadTexture("Uranus.jpg", Uranus);
        loadTexture("Neptune.jpg", Neptune);


        // wrap each planet into a TG with set Tf3D£¬and add it and a rotationIp to a parent TG
        // note:
        // rotation: Sun£º25.4, Mercury£º58.6, Venus£º243.0, Earth£º1.0, Moon£º27.3, Mars£º1.03, Jupiter£º0.41, Saturn£º0.44, Uranus£º0.72, Neptune£º0.67
        // revolution: 0.24 : 0.62 : 1 : 0.08 : 1.88 : 11.86 : 29.46 : 84.01 : 164.79
        TransformGroup TranSun = rotPlanet(0, selfRot(0, 0, 0, 25400, Sun));
        Trans.addChild(TranSun);

        TransformGroup TranMercury = rotPlanet(2400, selfRot(0.27f, 0, 0, 58600, Mercury));
        Trans.addChild(TranMercury);

        TransformGroup TranVenus = rotPlanet(6200, selfRot(0.318f, 0, 0, 243000, Venus));
        Trans.addChild(TranVenus);

        TransformGroup TranEarth = rotPlanet(10000, selfRot(0.4f, 0, 0, 1000, Earth));
        Trans.addChild(TranEarth);

        TransformGroup TranMoon = rotPlanet(800, selfRot(0.05f, 0, 0, 27300, Moon));
        Earth.addChild(TranMoon);

        TransformGroup TranMars = rotPlanet(18800, selfRot(0.48f, 0, 0, 1030, Mars));
        Trans.addChild(TranMars);

        TransformGroup TranJupiter = rotPlanet(118600, selfRot(-0.66f, 0, 0, 410, Jupiter));
        Trans.addChild(TranJupiter);

        TransformGroup TranSaturn = rotPlanet(294600, selfRot(0.79f, 0, 0, 440, Saturn));
        Trans.addChild(TranSaturn);

        TransformGroup TranUranus = rotPlanet(840100, selfRot(-0.89f, 0, 0, 720, Uranus));
        Trans.addChild(TranUranus);

        TransformGroup TranNeptune = rotPlanet(1647900, selfRot(0.679f, 0, 0.679f, 670, Neptune));
        Trans.addChild(TranNeptune);

        int n = 1000;
        float[] vert = new float[n * 3];
        float[] color = new float[n * 3];

        int i;
        double gamma;
        double r;
        for (i = 0; i < n * 3; vert[i++] = (float) (r * Math.sin(gamma))) {
            gamma = Math.random() * 2.0 * Math.PI;
            r = Math.random() * 0.06 + 0.52;
            color[i] = 1.0f;
            vert[i++] = (float) (r * Math.cos(gamma));
            color[i] = 1.0f;
            vert[i++] = 0.0f;
            color[i] = 1.0f;
        }

        Shape3D asteroids = new Shape3D();
        PointArray point = new PointArray(n, 5);
        point.setCoordinates(0, vert);
        point.setColors(0, color);
        PointAttributes pa = new PointAttributes();
        pa.setPointSize(1.0f);
        pa.setPointAntialiasingEnable(true);
        Appearance appearance = new Appearance();
        appearance.setPointAttributes(pa);
        asteroids.setGeometry(point);
        asteroids.setAppearance(appearance);
        Trans.addChild(asteroids);
        int n1 = 150;
        float[] vert1 = new float[n1 * 3];
        float[] color1 = new float[n1 * 3];

        for (i = 0; i < n1 * 3; ++i) {
            color1[i] = 1.0f;
            vert1[i] = (float) (Math.random() * 2.0 - 1.0);
        }

        Shape3D stars = new Shape3D();
        PointArray point1 = new PointArray(n, 5);
        point1.setCoordinates(0, vert1);
        point1.setColors(0, color1);
        PointAttributes pa1 = new PointAttributes();
        pa1.setPointSize(2.0f);
        pa1.setPointAntialiasingEnable(true);
        Appearance appearance1 = new Appearance();
        appearance1.setPointAttributes(pa1);
        stars.setGeometry(point1);
        stars.setAppearance(appearance1);
        Trans.addChild(stars);

// add OrientedShape3D
        TransformGroup t = new oriented3D("Solar System",new Vector3f(0.6f,0.35f,0.6f)).orientedText();
        TransformGroup t1 = new oriented3D("Player 1 wins!",new Vector3f(0.6f,0.35f,0.6f)).orientedText();
        TransformGroup t2 = new oriented3D("Player 2 wins!",new Vector3f(0.6f,0.35f,0.6f)).orientedText();
        Switch sw = switchString(t, t1, t2);
        sw.setCapability(Switch.ALLOW_SWITCH_WRITE);

        Trans.addChild(sw);

// collision detector
        CollisionDetector cd = new CollisionDetector(Sun.getShape());
        cd.setSchedulingBounds(new BoundingSphere());
        Trans.addChild(cd);

        // add a comet from one side to the other
        TransformGroup cometTG = new TransformGroup();
        cometTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        cometTG.addChild(Comet);

        Trans.addChild(cometTG);

        float knots[] = {0f, 0.5f, 1f};
        Point3f pos[] = {new Point3f((float) Math.random() + 1, 0, (float) Math.random() + 1),
                new Point3f(0, 0, 0), new Point3f(-(float) Math.random() - 1, 0, -(float) Math.random() - 1)};
        PositionPathInterpolator positionInterpolator = new PositionPathInterpolator(
                new Alpha(-1, 35000), cometTG, new Transform3D(), knots, pos);
        positionInterpolator.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        cometTG.addChild(positionInterpolator);


// add light
        Color3f clr = new Color3f(1.0f, 0.0f, 0.0f);
        DirectionalLight directionalLight = new DirectionalLight(clr, new Vector3f(1.0f, 0.0f, -1.0f));
        directionalLight.setInfluencingBounds(bounds);
        universe.addChild(directionalLight);
        universe.addChild(CommonsXZ.add_Lights(CommonsXZ.White, 1));

        Trans.addChild(createDistanceLOD());
        Trans.addChild(new LoadModel("SpaceStation/spaceStation.obj",new Vector3f(0.5f,0.25f,0)).loadModel());

        LoadModel lm = new LoadModel("shuttle/shuttle.obj",new Vector3f(-0.5f,0.25f,0));
        TransformGroup sh = lm.loadModel();
        RotationPathInterpolator rip = pathRotate(sh);
        Transform3D tfSh = new Transform3D();
        tfSh.setTranslation(new Vector3f(-0.5f,0.25f,0));
        tfSh.setScale(0.1f);
        TransformGroup shuttle = new TransformGroup(tfSh);
        shuttle.addChild(rip);
        shuttle.addChild(sh);
        Trans.addChild(shuttle);

        return universe;
    }

    public static Switch switchString(TransformGroup tg1, TransformGroup tg2, TransformGroup tg3){
        Switch sw = new Switch();
        // set capability here does not work!!! Please set it outside this function.
        SwitchValueInterpolator originalStirng = new SwitchValueInterpolator(new Alpha(), sw,0,0);
        SwitchValueInterpolator player1Win = new SwitchValueInterpolator(new Alpha(), sw,1,1);
        SwitchValueInterpolator player2Win = new SwitchValueInterpolator(new Alpha(), sw, 2, 2);
        sw.addChild(tg1);
        sw.addChild(tg2);
        sw.addChild(tg3);
        sw.setWhichChild(0);
        return sw;
    }

    public static RotationPathInterpolator pathRotate(TransformGroup tg){
        float[] knots = {0.0f, 0.25f, 0.3f, 0.5f, 0.7f, 0.75f, 1.0f}; // time slot of rotation
        Quat4f[] quats = {
                new Quat4f(0.0f, 0.0f, 0.0f, 1.0f),
                new Quat4f(0.0f, -1.0f, 0.0f, 1.0f),
                new Quat4f(0.0f, -1.0f, 0.0f, 1.0f),
                new Quat4f(0.0f, 0.0f, 0.0f, 1.0f),
                new Quat4f(0.0f, 1.0f, 0.0f, 1.0f),
                new Quat4f(0.0f, 1.0f, 0.0f, 1.0f),
                new Quat4f(0.0f, 0.0f, 0.0f, 1.0f),
        };
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Alpha alpha = new Alpha(-1,10000); // 2 rounds per turn, 5000*2 = 10000
        alpha.setStartTime(System.currentTimeMillis());
        alpha.setDecreasingAlphaRampDuration(2500);
        RotationPathInterpolator rpi = new RotationPathInterpolator(alpha,tg,new Transform3D(),knots,quats);
        Transform3D tf = new Transform3D();
        tf.setTranslation(new Vector3f(0,1,0));
        rpi.setTransformAxis(tf);

        rpi.setSchedulingBounds(new BoundingSphere());
        return rpi;
    }

// add DistanceLOD
    private BranchGroup createDistanceLOD(){
        BranchGroup baseBG = new BranchGroup();
        TransformGroup translateTG = new TransformGroup();
        translateTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        AxisAngle4f axisTrans = new AxisAngle4f(1,0,1,(float)Math.PI/2f);
        Transform3D T3D = new Transform3D(); T3D.set(axisTrans);
        PositionInterpolator positionInt = new PositionInterpolator(new Alpha(-1,8000),translateTG,T3D,5f,-5.0f);
        positionInt.setSchedulingBounds(new BoundingSphere());
        float[] distances = {8.5f,13.5f,15.5f};
        Switch SwitchTarget = new Switch();
        SwitchTarget.setCapability(Switch.ALLOW_SWITCH_WRITE);
        Sphere sp1 = new Sphere(0.30f,Primitive.GENERATE_TEXTURE_COORDS,new Appearance());
        loadTexture("Comet.jpg", sp1);
        Sphere sp2 = new Sphere(0.20f,Primitive.GENERATE_TEXTURE_COORDS,new Appearance());
        loadTexture("supernova.jpg", sp2);
        Sphere sp3 = new Sphere(0.10f,Primitive.GENERATE_TEXTURE_COORDS,new Appearance());
        loadTexture("Comet.jpg", sp3);
        Sphere sp4 = new Sphere(0.05f,Primitive.GENERATE_TEXTURE_COORDS,new Appearance());
        loadTexture("Comet.jpg", sp4);
        SwitchTarget.addChild(sp1);
        SwitchTarget.addChild(sp2);
        SwitchTarget.addChild(sp3);
        SwitchTarget.addChild(sp4);
        DistanceLOD distanceLOD = new DistanceLOD(distances,new Point3f(5,0,0));
        distanceLOD.addSwitch(SwitchTarget);
        distanceLOD.setSchedulingBounds(new BoundingSphere());
        baseBG.addChild(translateTG);baseBG.addChild(positionInt);
        translateTG.addChild(distanceLOD); translateTG.addChild(SwitchTarget);
        return baseBG;
    }

    public void loadTexture(String name, Sphere planet) {
        try {
            Texture texture = new TextureLoader(ImageIO.read(this.getClass().getResource(name)), this).getTexture();
            TextureAttributes text = new TextureAttributes();
            text.setTextureMode(TextureAttributes.REPLACE);
            //text.setTextureMode(TextureAttributes.BLEND); //blue sun...
            Appearance app = new Appearance();
            app.setTexture(texture);
            app.setTextureAttributes(text);
            planet.setAppearance(app);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TransformGroup selfRot(float x, float y, float z, long timePerRound, Sphere planet) {
        Transform3D tf = new Transform3D();
        tf.setTranslation(new Vector3f(x, y, z));

        TransformGroup tg1 = new TransformGroup();
        tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg1.addChild(planet);

        RotationInterpolator rot = new RotationInterpolator(new Alpha(-1, timePerRound), tg1);
        rot.setSchedulingBounds(new BoundingSphere());

        TransformGroup tg2 = new TransformGroup(tf);
        tg2.addChild(tg1);
        tg2.addChild(rot);
        return tg2;
    }

    public TransformGroup rotPlanet(long timePerRound, TransformGroup tg1) {
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        tg.addChild(tg1);

        RotationInterpolator rip = new RotationInterpolator(new Alpha(-1, timePerRound), tg);
        rip.setSchedulingBounds(new BoundingSphere());
        tg.addChild(rip);

        return tg;
    }

    public SolarSystem() {
        m_KeyHashtable = new Hashtable<String, KeyNavigatorBehavior>( );
        m_MouseHashtable = new Hashtable<String, MouseListener>( );

        canvas3D = new Canvas3D[3];


        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration( );
        for (int i = 0; i < 3; i++) {
            canvas3D[i] = new Canvas3D( config );
            canvas3D[i].setSize( width, height );
            add( canvas3D[i] );                            // add 3 Canvas3D to Frame
            canvas3D[i].addKeyListener(this);
        }
        canvas3D[1].setName("Canvas3D 1");
        canvas3D[2].setName("Canvas3D 2");
        ViewingPlatform vp = new ViewingPlatform(2);       // a VP with 2 TG about it
        Viewer viewer = new Viewer( canvas3D[0] );         // point 1st Viewer to c3D[0]
        Transform3D t3d = new Transform3D( );
        t3d.rotX( Math.PI / 2.0 );                         // rotate and position the 1st ~
        t3d.setTranslation( new Vector3d( 0, 0, -13 ) );   // viewer looking down from top
        t3d.invert( );
        MultiTransformGroup mtg = vp.getMultiTransformGroup( );
        mtg.getTransformGroup(0).setTransform( t3d );

        SimpleUniverse su = new SimpleUniverse(vp, viewer); // a SU with one Vp and 3 Viewers
        enableAudio(su);
        Locale lcl = su.getLocale();                        // point 2nd/3rd Viewer to c3D[1,2]
        lcl.addBranchGraph( createViewer( canvas3D[1] , "  TOP ", CommonsXZ.Cyan, .5, 2, -.5 ) );
        lcl.addBranchGraph( createViewer( canvas3D[2], "  Bottom ", CommonsXZ.Orange, -.5, -2, .5 ) );

        initialSound();
        bg = solarSystemBG();
        bg.addChild(createBackground("Universe.jpg"));
        bg.addChild(key_Navigation(su));
        bg.addChild(pointSound("sounds/Relieved wind.wav"));
        bg.addChild(CommonsXZ.add_Lights(CommonsXZ.White, 1));
        pickTool = new PickTool(bg);
        pickTool.setMode(PickTool.GEOMETRY);
        bg.compile();
        su.addBranchGraph(bg);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Solar System");
        frame.getContentPane().add(new SolarSystem()); // create an instance of the class
        frame.setSize(1910, height + 40);                         // set the size of the JFrame
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static KeyNavigatorBehavior key_Navigation(SimpleUniverse simple_U) {
        ViewingPlatform view_platfm = simple_U.getViewingPlatform();
        TransformGroup view_TG = view_platfm.getViewPlatformTransform();
        KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(view_TG);
        keyNavBeh.setSchedulingBounds(new BoundingSphere());
        return keyNavBeh;
    }

    public Background createBackground(String name) {
        Background bg = new Background();
        try {
            bg.setImage(new TextureLoader(ImageIO.read(this.getClass().getResource(name)), this).getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        bg.setImageScaleMode(Background.SCALE_FIT_MAX);
        bg.setApplicationBounds(new BoundingSphere(new Point3d(0, 0, 0), Double.MAX_VALUE));
        bg.setColor(new Color3f());
        return bg;
    }

    private void enableAudio(SimpleUniverse simple_U) {
        JOALMixer mixer = null;                                 // create a null mixer as a joalmixer
        Viewer viewer = simple_U.getViewer();
        viewer.getView().setBackClipDistance(20.0f);         // make object(s) disappear beyond 20f
        if (mixer == null && viewer.getView().getUserHeadToVworldEnable()) {
            mixer = new JOALMixer(viewer.getPhysicalEnvironment());
            if (!mixer.initialize()) {                       // add mixer as audio device if successful
                System.out.println("Open AL failed to init");
                viewer.getPhysicalEnvironment().setAudioDevice(null);
            }
        }
    }

    private PointSound pointSound(String filename) {
        URL url = null;
        try {
            url = new URL("file", "localhost", filename);
        } catch (Exception e) {
            System.out.println("Can't open " + filename);
        }
        PointSound ps = new PointSound();                    // create and position a point sound
        ps.setCapability(PointSound.ALLOW_INITIAL_GAIN_WRITE);
        ps.setInitialGain(0.02f);
        PointSoundBehavior player = new PointSoundBehavior(ps, url, new Point3f(0.0f, 0.0f, 0.0f));
        player.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        return ps;
    }

    public static SoundUtilityJOAL soundJOAL;
    public static void initialSound() {
        soundJOAL = new SoundUtilityJOAL();
        if (!soundJOAL.load("laser2", 0f, 0f, 10f, true))
            System.out.println("Could not load " + "laser2");
        if (!soundJOAL.load("magic_bells", 0f, 0f, 10f, true))
            System.out.println("Could not load " + "magic_bells");
    }

    public static void playSound(int key) {
        String snd_pt = "laser2";
        if (key > 1)
            snd_pt = "magic_bells";
        soundJOAL.play(snd_pt);
        try {
            Thread.sleep(500); // sleep for 0.5 secs
        } catch (InterruptedException ex) {}
        soundJOAL.stop(snd_pt);
    }

    ViewingPlatform createViewer(Canvas3D canvas3D, String name, Color3f clr,
                                 double x, double y, double z) {
        // a Canvas3D can only be attached to a single Viewer
        Viewer viewer = new Viewer( canvas3D );	             // attach a Viewer to its canvas
        ViewingPlatform vp = new ViewingPlatform( 1 );       // 1 VP with 1 TG above
        // assign PG to the Viewer
        vp.setPlatformGeometry( labelPlatformGeometry( name ) );
        viewer.setAvatar( createViewerAvatar( name, clr ) ); // assign VA to the Viewer

        Point3d center = new Point3d(0, 0, 0);               // define where the eye looks at
        Vector3d up = new Vector3d(0, 1, 0);                 // define camera's up direction
        Transform3D viewTM = new Transform3D();
        Point3d eye = new Point3d(x, y, z);                  // define eye's location
        viewTM.lookAt(eye, center, up);
        viewTM.invert();
        vp.getViewPlatformTransform().setTransform(viewTM);  // set VP with 'viewTG'

        // set TG's capabilities to allow KeyNavigatorBehavior modify the Viewer's position
        vp.getViewPlatformTransform( ).setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
        vp.getViewPlatformTransform( ).setCapability( TransformGroup.ALLOW_TRANSFORM_READ );
        KeyNavigatorBehavior key = new KeyNavigatorBehavior( vp.getViewPlatformTransform( ) );
        key.setSchedulingBounds( new BoundingSphere() );          // enable viewer navigation
        key.setEnable( true );
        vp.addChild( key );                                   // add KeyNavigatorBehavior to VP
        viewer.setViewingPlatform( vp );                      // set VP for the Viewer
        m_KeyHashtable.put( name, key );                      // label the Viewer
        Button button = new Button( name );
        button.addActionListener( this );                     // button to switch the Viewer ON
        add( button );

        return vp;
    }

    /* a function to create and position a simple Cone to represent the Viewer */
    ViewerAvatar createViewerAvatar(String szText, Color3f objColor ) {
        ViewerAvatar viewerAvatar = new ViewerAvatar( );
        // lay down the Cone, pointing sharp-end towards the Viewer's field of view
        TransformGroup tg = new TransformGroup( );
        Transform3D t3d = new Transform3D( );
        t3d.setEuler( new Vector3d( Math.PI / 2.0, Math.PI, 0 ) );
        tg.setTransform( t3d );

        Appearance app = CommonsXZ.obj_Appearance(objColor);

        tg.addChild( new Cone( 0.2f, 0.5f, Primitive.GENERATE_NORMALS, app ) );
        viewerAvatar.addChild( tg );                         // add Cone to parent BranchGroup

        return viewerAvatar;
    }

    PlatformGeometry labelPlatformGeometry(String szText ) {
        PlatformGeometry pg = new PlatformGeometry( );
        pg.addChild( createLabel( szText, 0f, 0.5f, 0f ) );    // label the PlatformGeometry ~
        return pg;                                           // to help identify the viewer
    }

    // creates a simple Raster text label (similar to Text2D)
    private Shape3D createLabel( String szText, float x, float y, float z )	{
        BufferedImage bufferedImage = new BufferedImage( 25, 14, BufferedImage.TYPE_INT_RGB );
        Graphics g = bufferedImage.getGraphics( );
        g.setColor( Color.white );
        g.drawString( szText, 2, 12 );

        ImageComponent2D img2D = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bufferedImage);
        Raster renderRaster = new Raster(new Point3f( x, y, z ), Raster.RASTER_COLOR,
                0, 0, bufferedImage.getWidth( ), bufferedImage.getHeight( ), img2D,	null );
        return new Shape3D( renderRaster );                  // create the Raster for the image
    }

    public void actionPerformed( ActionEvent event ) {
        KeyNavigatorBehavior key = (KeyNavigatorBehavior)m_KeyHashtable.get(event.getActionCommand());
        Object[] keysArray = m_KeyHashtable.values( ).toArray( );
        for( int n = 0; n < keysArray.length; n++ )	{
            KeyNavigatorBehavior keyAtIndex = (KeyNavigatorBehavior) keysArray[n];
            keyAtIndex.setEnable( keyAtIndex == key );
            if( keyAtIndex == key ) {
                if (n == 1) {
                    canvas3D[1].addMouseListener(this);
                    playSound(1);
                    canvas3D[2].removeMouseListener(this);
                }
                else {
                    canvas3D[2].addMouseListener(this);
                    playSound(2);
                    canvas3D[1].removeMouseListener(this);
                }
            }
        }
    }

// mouse picking: Who clicks on the farthest planet from the horizontal will win the game.
    @Override
    public void mouseClicked(MouseEvent event) {
        int x = event.getX(); int y = event.getY();        // mouse coordinates
        Point3d point3d = new Point3d(), center = new Point3d();
        Transform3D transform3D = new Transform3D();       // matrix to relate ImagePlate coordinates~
        Component source = event.getComponent();
        if (source instanceof Canvas3D) {
            if (source.getName().equals("Canvas3D 1")) {
                canvas3D[1].getPixelLocationInImagePlate(x, y, point3d);// obtain AWT pixel in ImagePlate coordinates
                canvas3D[1].getCenterEyeInImagePlate(center);           // obtain eye's position in IP coordinates
                canvas3D[1].getImagePlateToVworld(transform3D);         // to Virtual World coordinates
            } else if (source.getName().equals("Canvas3D 2")) {
                canvas3D[2].getPixelLocationInImagePlate(x, y, point3d);
                canvas3D[2].getCenterEyeInImagePlate(center);
                canvas3D[2].getImagePlateToVworld(transform3D);
            }
        }
        transform3D.transform(point3d);                    // transform 'point3d' with 'transform3D'
        transform3D.transform(center);                     // transform 'center' with 'transform3D'
        Vector3d mouseVec = new Vector3d();
        mouseVec.sub(point3d, center);
        mouseVec.normalize();
        pickTool.setShapeRay(point3d, mouseVec);           // send a PickRay for intersection

        if (pickTool.pickClosest() != null) {
            PickResult pickResult = pickTool.pickClosest();// obtain the closest hit
            Shape3D nd = (Shape3D) pickResult.getObject();
            Switch sw = (Switch)Trans.getChild(19);
                if (source.getName().equals("Canvas3D 1") && hasWinner == false) {
                    playSound(1);
                    sw.setWhichChild(1);
                    hasWinner = true;
                }
                else if (source.getName().equals("Canvas3D 2") && hasWinner == false) {
                    playSound(2);
                    sw.setWhichChild(2);
                    hasWinner = true;
                }

        }
    }
    public void mouseEntered(MouseEvent arg0) { }
    public void mouseExited(MouseEvent arg0) { }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }

// navigation
    @Override
    public void keyPressed(KeyEvent ke){
        System.out.println("I pressed the "+ke.getKeyChar()+" key£¡");

        Vector3d translation = new Vector3d();
        switch (ke.getKeyCode()) {
            case KeyEvent.VK_Y:
                translation.set(0.1, 0.0, 0.0);
                break;
            case KeyEvent.VK_H:
                translation.set(0.0, 0.0, 0.1);
                break;
            case KeyEvent.VK_N:
                translation.set(0.0, 0.1, 0.0);
                break;
            case KeyEvent.VK_T:
                translation.set(-0.1, 0.0, 0.0);
                break;
            case KeyEvent.VK_G:
                translation.set(0.0, 0.0, -0.1);
                break;
            case KeyEvent.VK_B:
                translation.set(0.0, -0.1, 0.0);
                break;
            case KeyEvent.VK_R:
                Transform3D tf = new Transform3D();
                Trans.getTransform(tf);
                Transform3D tf1 = new Transform3D();
                tf1.rotY(rotateAngle);
                tf.mul(tf1);
                rotateAngle += Math.PI / 60;
                Trans.setTransform(tf);
                break;
            case KeyEvent.VK_O:
                Trans.setTransform(originalPosition);
                rotateAngle = Math.PI / 60;
                break;
        }
        if(ke.getKeyCode() != KeyEvent.VK_R && ke.getKeyCode() != KeyEvent.VK_O)
            updateScene(translation);
    }
    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent ke){}
    private void updateScene(Vector3d translation) {
        Transform3D sceneTransform = new Transform3D();
        Trans.getTransform(sceneTransform);
        Transform3D translationTransform = new Transform3D();
        translationTransform.setTranslation(translation);
        sceneTransform.mul(translationTransform);
        Trans.setTransform(sceneTransform);
    }
}