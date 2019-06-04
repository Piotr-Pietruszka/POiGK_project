/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_poig;

import java.applet.*;
import java.awt.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
public class Project_POiG extends Applet implements KeyListener {

 private SimpleUniverse universe = null;
 private Canvas3D canvas = null;
 private TransformGroup trans = null;
 
 private TransformGroup base = new TransformGroup();;
 private Transform3D base3d = new Transform3D();
 private Transform3D base3dstep = new Transform3D();
 
 private TransformGroup  arm_height_control = new TransformGroup();
 private Transform3D    arm_height_control3d = new Transform3D();
 private Transform3D arm_height_control3d_step = new Transform3D();
 
  private TransformGroup  arm_width_control = new TransformGroup();
 private Transform3D    arm_width_control3d = new Transform3D();
 private Transform3D arm_width_control3d_step = new Transform3D();
 
 private Matrix4d matrix = new Matrix4d();
 
 private float height = 0.0f;
 private float radius = 0.0f;
  
 private float base_height = 5;
 Cylinder arm_height = null;
 
//-------------
 //sfera w zrodel swaitla
 private TransformGroup sph_tr = new TransformGroup();
 private Transform3D sph_tr_3d = new Transform3D();
 //------------
 
 
 public Project_POiG() {
  setLayout(new BorderLayout());
  GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

  canvas = new Canvas3D(config);
  add("Center", canvas);
  universe = new SimpleUniverse(canvas);

  canvas.addKeyListener(this);

  BranchGroup scene = createSceneGraph();
  universe.getViewingPlatform().setNominalViewingTransform();
  universe.getViewer().getView().setBackClipDistance(200.0);
  universe.addBranchGraph(scene);
 }

 private BranchGroup createSceneGraph() {
  BranchGroup objRoot = new BranchGroup();

  BoundingSphere bounds = new BoundingSphere(new Point3d(), 10000.0);
 
  objRoot.addChild(createPrimitives());

  return objRoot;
 }

 private BranchGroup createPrimitives() {

  BranchGroup objRoot = new BranchGroup();

  base.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  base3d.setTranslation(new Vector3d(0.0, 0.0, -20.0));
  base3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
  base3d.setScale(1.0);
  base.setTransform(base3d);
  Appearance base_ap = createAppearance(new Color3f(138f, 127f, 128f));

  
  Cylinder basic_cylinder = new Cylinder(0.5f, base_height, 1, 50, 10, base_ap);// cylinder głowny
                            //r, h, przezroczystość?, podział, podział(nieistotny, appearance)
  base.addChild(basic_cylinder);
  basic_cylinder.getCollisionBounds();
    
    
   arm_height_control.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
   arm_height_control3d.setTranslation(new Vector3d(-1.0, 0.0, 0.0));
   arm_height_control3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 3.14f, 1.57f));
   arm_height_control3d.setScale(1);
   arm_height_control.setTransform( arm_height_control3d);
   Appearance arm_height_ap = createAppearance(new Color3f(138f, 127f, 128f));

   Box arm_height = new Box(0.3f, 2.0f,0.5f, arm_height_ap);

   arm_height_control.addChild(arm_height);
   
  arm_width_control.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  arm_width_control3d.setTranslation(new Vector3d(0, 1.55, 0));
  arm_width_control3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
  arm_width_control3d.setScale(1);
  arm_width_control.setTransform(arm_width_control3d);
  Appearance arm_width_ap = createAppearance(new Color3f(138f, 127f, 128f));

  Box arm_width = new Box(0.2f,1f,0.3f, arm_width_ap);

  arm_width_control.addChild(arm_width);

  
  arm_height_control.addChild(arm_width_control);
  base.addChild( arm_height_control);
  objRoot.addChild(base);
  
  //Swiatlo
  //-------------------------------
  PointLight lp = new PointLight();
  lp=createPointLight(0.4f, 0.8f, 0.8f,   -4f, 3f, -17f,   5.0f, 0f, 30f);
  
 //-----
 //Sfera w zrodle swiatla punktowego
  Point3f point_l = new Point3f();
  lp.getPosition(point_l);
  sph_tr.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  sph_tr_3d.setTranslation(new Vector3f(point_l.x, point_l.y, point_l.z));
  sph_tr.setTransform(sph_tr_3d);
  
  Sphere sph1 = new Sphere(0.3f, base_ap);
  sph_tr.addChild(sph1);
  objRoot.addChild(sph_tr);
  //------
  
  objRoot.addChild(lp);//sw punktowe
  objRoot.addChild(createAmbientLight(0.5f, 1f, 1f));//sw ambient
  //-------------------------------

  objRoot.compile();

  return objRoot;

 }

 private Appearance createAppearance(Color3f color) {
  Appearance ap = new Appearance();
  Material mat = new Material();
  mat.setSpecularColor(color);
  ap.setMaterial(mat);
  
  return ap;
 }

 private PointLight createPointLight(float r, float g, float b, float x, float y,
   float z,  float x2, float y2, float z2) {

    PointLight light = new PointLight(new Color3f(r, g, b), new Point3f(x, y, z), new Point3f(-0.01f, 0.f, 0.01f));
    light.setInfluencingBounds(new BoundingSphere(new Point3d(x2, y2, z2), 10000.0));

    return light;
 }
 
private Light createAmbientLight(float r, float g, float b) {
      Color3f ambientColor = new Color3f(r, g, b);
      AmbientLight ambientLight = new AmbientLight(true,ambientColor);

      ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(-5.0f, 0f, 0f), 10000.0));
      return ambientLight;
 }

 public static void main(String[] args) {
  Project_POiG applet = new Project_POiG();
  Frame frame = new MainFrame(applet, 800, 600);
 }

 public void keyTyped(KeyEvent e) {
  char key = e.getKeyChar();

  if (key == 'z') {
   base3dstep.rotY(Math.PI / 32);
   base.getTransform(base3d);
   base3d.get(matrix);
   base3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
   base3d.mul(base3dstep);
   base3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
   base.setTransform(base3d);
  }

  if (key == 'x') {
   base3dstep.rotY(-Math.PI / 32);
   base.getTransform(base3d);
   base3d.get(matrix);
   base3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
   base3d.mul(base3dstep);
   base3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
   base.setTransform(base3d);
  }

  if (key == 'w') {
      if(height < 2.2)
      {
       height+=0.1;
       arm_height_control3d_step.setTranslation(new Vector3d(0.1, 0, 0.0));
       arm_height_control.getTransform( arm_height_control3d);
       arm_height_control3d.get(matrix);
       arm_height_control3d.mul( arm_height_control3d_step);
       arm_height_control.setTransform( arm_height_control3d);
      }
   
  }

  if (key == 's') {
       if(height > -2.2)
      {
        height-=0.1;
        arm_height_control3d_step.setTranslation(new Vector3d(-0.1, 0, 0.0));
        arm_height_control.getTransform( arm_height_control3d);
        arm_height_control3d.get(matrix);
        arm_height_control3d.mul( arm_height_control3d_step);
        arm_height_control.setTransform( arm_height_control3d);
      }
  }
  if (key == 'd') {
      if(radius < 1.5)
      {
        radius += 0.1;
        arm_width_control3d_step.setTranslation(new Vector3d(0, 0.1, 0.0));
        arm_width_control.getTransform( arm_width_control3d);
        arm_width_control3d.get(matrix);
        arm_width_control3d.mul( arm_width_control3d_step);
        arm_width_control.setTransform( arm_width_control3d);
      }
  }
   if (key == 'a') {
    if(radius > -0.5)
      {
        radius -= 0.1;
        Vector3f position = new Vector3f();
        arm_width_control3d.get(position);
        arm_width_control3d_step .setTranslation(new Vector3d(0, -0.1, 0.0));
        arm_width_control .getTransform( arm_width_control3d);
        arm_width_control3d.get(matrix);
        arm_width_control3d.mul( arm_width_control3d_step);
        arm_width_control.setTransform( arm_width_control3d);
      }
  }
  

 

 }

 public void keyReleased(KeyEvent e) {
 }

 public void keyPressed(KeyEvent e) {
 }

}