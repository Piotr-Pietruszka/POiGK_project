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
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.behaviors.keyboard.*;
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
 private Matrix4d matrix = new Matrix4d();

 public Project_POiG() {
  setLayout(new BorderLayout());
  GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

  canvas = new Canvas3D(config);
  add("Center", canvas);
  universe = new SimpleUniverse(canvas);

  canvas.addKeyListener(this);

  BranchGroup scene = createSceneGraph();
  universe.getViewingPlatform().setNominalViewingTransform();
  universe.getViewer().getView().setBackClipDistance(100.0);
  universe.addBranchGraph(scene);
 }

 private BranchGroup createSceneGraph() {
  BranchGroup objRoot = new BranchGroup();
  //BoundingSphere bounds = new BoundingSphere(new Point3d(), 10000.0);
  trans = universe.getViewingPlatform().getViewPlatformTransform();
  
  //KeyNavigatorBehavior keyNavBeh = new KeyNavigatorBehavior(trans);
  //keyNavBeh.setSchedulingBounds(bounds);
 // PlatformGeometry platformGeom = new PlatformGeometry();
 // platformGeom.addChild(keyNavBeh);
  //universe.getViewingPlatform().setPlatformGeometry(platformGeom);

  objRoot.addChild(createPrimitives());

  return objRoot;
 }

 private BranchGroup createPrimitives() {

  BranchGroup objRoot = new BranchGroup();

  base.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  base3d.setTranslation(new Vector3d(0.0, 0.0, -15.0));
  base3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
  base3d.setScale(1.0);
  base.setTransform(base3d);
  Appearance base_ap = createAppearance(new Color3f(1.0f, 0.0f, 0.0f));
  base.addChild(new Cylinder(1.0f, 5.0f, base_ap));

   arm_height_control.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
   arm_height_control3d.setTranslation(new Vector3d(2.0, 0.0, 0.0));
   arm_height_control3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 3.14f, 1.57f));
   arm_height_control3d.setScale(1);
   arm_height_control.setTransform( arm_height_control3d);
   Appearance arm_height_ap = createAppearance(new Color3f(0.0f, 1.0f, 0.0f));
   arm_height_control.addChild(new Cylinder(0.3f, 3.0f, arm_height_ap));

  TransformGroup tg_3 = new TransformGroup();
  Transform3D t3d_3 = new Transform3D();
  t3d_3.setTranslation(new Vector3d(0, -2, 0));
  t3d_3.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
  t3d_3.setScale(1);
  tg_3.setTransform(t3d_3);
  Appearance ap_white = createAppearance(new Color3f(0.0f, 0.0f, 1.0f));
  tg_3.addChild(new Sphere(0.5f, ap_white));

  arm_height_control.addChild(tg_3);
  base.addChild( arm_height_control);
  objRoot.addChild(base);

  base.addChild(createLight(10.0f, 1.0f, 1.0f, -0.3f, -0.2f, -1.0f));
  arm_height_control.addChild(createLight(0.7f, 0.7f, 5.0f, -0.3f, -0.2f, 1.0f));
  tg_3.addChild(createLight(2.0f, 3.0f, 1.5f, -0.3f, -0.2f, -1.0f));

  objRoot.compile();

  return objRoot;

 }

 private Appearance createAppearance(Color3f color) {
  Appearance ap = new Appearance();
  Material mat = new Material();
  mat.setDiffuseColor(color);
  ap.setMaterial(mat);
  return ap;
 }

 private Light createLight(float r, float g, float b, float x, float y,
   float z) {
  DirectionalLight light = new DirectionalLight(true,
    new Color3f(r, g, b), new Vector3f(x, y, z));

  light.setInfluencingBounds(new BoundingSphere(new Point3d(), 10000.0));

  return light;
 }

 public static void main(String[] args) {
  Project_POiG applet = new Project_POiG();
  Frame frame = new MainFrame(applet, 800, 600);
 }

 public void keyTyped(KeyEvent e) {
  char key = e.getKeyChar();

  if (key == 'a') {
   base3dstep.rotY(Math.PI / 32);
   base.getTransform(base3d);
   base3d.get(matrix);
   base3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
   base3d.mul(base3dstep);
   base3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
   base.setTransform(base3d);
  }

  if (key == 'd') {
   base3dstep.rotY(-Math.PI / 32);
   base.getTransform(base3d);
   base3d.get(matrix);
   base3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
   base3d.mul(base3dstep);
   base3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
   base.setTransform(base3d);
  }

  if (key == 'w') {
    //arm_height_control3d_step.rotY(Math.PI / 32);
     arm_height_control3d_step.setTranslation(new Vector3d(0.1, 0, 0.0));
    arm_height_control.getTransform( arm_height_control3d);
    arm_height_control3d.get(matrix);
    //arm_height_control3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
    arm_height_control3d.mul( arm_height_control3d_step);
   // arm_height_control3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
    arm_height_control.setTransform( arm_height_control3d);
  }

  if (key == 's') {
    //arm_height_control3d_step.rotY(Math.PI / 32);
    arm_height_control3d_step.setTranslation(new Vector3d(-0.1, 0, 0.0));
    arm_height_control.getTransform( arm_height_control3d);
    arm_height_control3d.get(matrix);
    //arm_height_control3d.setTranslation(new Vector3d(0.0, 0.0, 0.0));
    arm_height_control3d.mul( arm_height_control3d_step);
    //arm_height_control3d.setTranslation(new Vector3d(matrix.m03, matrix.m13, matrix.m23));
    arm_height_control.setTransform( arm_height_control3d);
  }

 }

 public void keyReleased(KeyEvent e) {
 }

 public void keyPressed(KeyEvent e) {
 }

}