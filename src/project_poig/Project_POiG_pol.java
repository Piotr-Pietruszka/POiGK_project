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
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;

public class Project_POiG_pol extends Applet implements KeyListener {

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
 
 private float base_height=5;
 Cylinder arm_height = null;

 public Project_POiG_pol() {
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
        Color3f ambientColor = new Color3f(200.0f,200.0f,200.0f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds);
        objRoot.addChild(ambientLightNode);
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
  Appearance base_ap = createAppearance(new Color3f(1.0f, 0.0f, 0.0f));
  base.addChild(new Cylinder(1.0f, base_height, base_ap));

   arm_height_control.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
   arm_height_control3d.setTranslation(new Vector3d(2.0, 0.0, 0.0));
   arm_height_control3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 3.14f, 1.57f));
   arm_height_control3d.setScale(1);
   arm_height_control.setTransform( arm_height_control3d);
   Appearance arm_height_ap = createAppearance(new Color3f(0.0f, 1.0f, 0.0f));
   Cylinder arm_height = new Cylinder(0.3f, 3.0f, arm_height_ap);
   arm_height_control.addChild(arm_height);

  TransformGroup arm_width_control = new TransformGroup();
  Transform3D arm_width_control3d = new Transform3D();
  arm_width_control3d.setTranslation(new Vector3d(0, -2, 0));
  arm_width_control3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
  arm_width_control3d.setScale(1);
  arm_width_control.setTransform(arm_width_control3d);
  Appearance arm_width_ap = createAppearance(new Color3f(0.0f, 0.0f, 1.0f));
  Cylinder arm_width = new Cylinder(0.2f,0.2f, arm_width_ap);
  arm_width_control.addChild(arm_width);

  arm_height_control.addChild(arm_width_control);
  base.addChild( arm_height_control);
  objRoot.addChild(base);
  objRoot.compile();

  return objRoot;

 }

 private Appearance createAppearance(Color3f color) {
  Appearance ap = new Appearance();
  Material mat = new Material();
  mat.setAmbientColor(color);
  ap.setMaterial(mat);
  return ap;
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
     arm_height_control3d_step.setTranslation(new Vector3d(0.1, 0, 0.0));
    arm_height_control.getTransform( arm_height_control3d);
    arm_height_control3d.get(matrix);
    arm_height_control3d.mul( arm_height_control3d_step);
    arm_height_control.setTransform( arm_height_control3d);
  }

  if (key == 's') {
        arm_height_control3d_step.setTranslation(new Vector3d(-0.1, 0, 0.0));
        arm_height_control.getTransform( arm_height_control3d);
        arm_height_control3d.get(matrix);
        arm_height_control3d.mul( arm_height_control3d_step);
        arm_height_control.setTransform( arm_height_control3d);
     }
   if (key == 'a') {
     arm_width_control3d_step.setTranslation(new Vector3d(0, 0.1, 0.0));
    arm_width_control.getTransform( arm_width_control3d);
    arm_width_control3d.get(matrix);
    arm_width_control3d.mul( arm_width_control3d_step);
    arm_width_control.setTransform( arm_width_control3d);
  }

  if (key == 'd') {
        arm_width_control3d_step .setTranslation(new Vector3d(0, -0.1, 0.0));
        arm_width_control .getTransform( arm_width_control3d);
        arm_width_control3d.get(matrix);
        arm_width_control3d.mul( arm_width_control3d_step);
        arm_width_control.setTransform( arm_width_control3d);
  }

 }

 public void keyReleased(KeyEvent e) {
 }

 public void keyPressed(KeyEvent e) {
 }

}