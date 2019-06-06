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
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
public class Project_POiG extends Applet implements KeyListener {

 private SimpleUniverse universe = null;
 private Canvas3D canvas = null;

 private TransformGroup ground = new TransformGroup();
 private Transform3D ground_position = new Transform3D();
 
 private TransformGroup base = new TransformGroup();
 private Transform3D base3d = new Transform3D();
 private Transform3D base3dstep = new Transform3D();
 
 private TransformGroup  arm_height_control = new TransformGroup();
 private Transform3D    arm_height_control3d = new Transform3D();
 private Transform3D arm_height_control3d_step = new Transform3D();
 
 private TransformGroup  arm_width_control = new TransformGroup();
 private Transform3D    arm_width_control3d = new Transform3D();
 private Transform3D arm_width_control3d_step = new Transform3D();
 
 //=====================================
 //obiekt do przenoszenia
  private TransformGroup  sphere_object = new TransformGroup();
  private Transform3D  sphere_object_tr3d = new Transform3D();
  
  private TransformGroup  sphere_object_ch = new TransformGroup();
  private Transform3D  sphere_object_tr3d_ch = new Transform3D();
  
  private Transform3D  tr1 = new Transform3D();
  private Transform3D  tr2 = new Transform3D();
  private Transform3D  tr3 = new Transform3D();
  private Transform3D  tr4 = new Transform3D();

  private BranchGroup element;
  private boolean picked_up = false;
 //===================================
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
  
  
  //Podstawa
  ground.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  ground_position.setTranslation(new Vector3d(0.0, -1.0, -20.0));
  ground.setTransform(ground_position);
  Appearance ground_ap = createAppearance(new Color3f(255f, 255f, 255f));
  Cylinder ground_cylinder = new Cylinder(8.25f, 0.03f, ground_ap);
  ground.addChild(ground_cylinder);
  objRoot.addChild(ground);
  
  
  //Glowny walec
  base.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  base3d.setTranslation(new Vector3d(0.0, base_height/2, 0.0));
  base3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
  base3d.setScale(1.0);
  base.setTransform(base3d);
  Appearance base_ap = createAppearance(new Color3f(138f, 127f, 128f));

  
  Cylinder basic_cylinder = new Cylinder(0.5f, base_height, 1, 50, 10, base_ap);// cylinder głowny
                                                    //r, h, przezroczystość?, podział, podział(nieistotny, appearance)
  base.addChild(basic_cylinder);
  ground.addChild(base);  
   
  //Ramie(sterowanie wysokoscia)
   arm_height_control.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
   arm_height_control3d.setTranslation(new Vector3d(-1.0, 0.0, 0.0));
   arm_height_control3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 3.14f, 1.57f));
   arm_height_control3d.setScale(1);
   arm_height_control.setTransform( arm_height_control3d);
   Appearance arm_height_ap = createAppearance(new Color3f(138f, 127f, 128f));//138f, 127f, 128f

   Box arm_height = new Box(0.3f, 2.0f,0.5f, arm_height_ap);

   arm_height_control.addChild(arm_height);
  
//Ramie (wysuwanie) 
  arm_width_control.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  arm_width_control.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
  arm_width_control.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
  arm_width_control.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
  
  arm_width_control3d.setTranslation(new Vector3d(0, 1.55, 0));
  arm_width_control3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
  arm_width_control3d.setScale(1);
  arm_width_control.setTransform(arm_width_control3d);
  Appearance arm_width_ap = createAppearance(new Color3f(138f, 127f, 128f));

  Box arm_width = new Box(0.2f,1f,0.3f, arm_width_ap);

  arm_width_control.addChild(arm_width);

  
  arm_height_control.addChild(arm_width_control);
  base.addChild( arm_height_control);
  
  
  //Swiatlo
  //-------------------------------
  PointLight lp = new PointLight();
  lp=createPointLight(0.5f, 1f, 1f,   -4f, 7f, 3f,   5.0f, 0f, 30f);
  
 
    //Sfera w zrodle swiatla punktowego
    //-----
    Point3f point_l = new Point3f();
    lp.getPosition(point_l);
    sph_tr.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    sph_tr_3d.setTranslation(new Vector3f(point_l.x, point_l.y, point_l.z));
    sph_tr.setTransform(sph_tr_3d);

    Sphere sph1 = new Sphere(0.3f, base_ap);
    sph_tr.addChild(sph1);
    ground.addChild(sph_tr);
    //------
  
  ground.addChild(lp);//sw punktowe
  objRoot.addChild(createAmbientLight(0.5f, 1f, 1f));//sw ambient 0.5f, 1f, 1f     100f, 200f, 200f    1.1f, 2.2f, 2.2f  
  
  //-------------------------------
  
  
  MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(ground);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseRotate);

        MouseWheelZoom myMouseZoom = new MouseWheelZoom();
        myMouseZoom.setTransformGroup(universe.getViewingPlatform().getViewPlatformTransform());
        myMouseZoom.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseZoom);
  
        
  element = new BranchGroup();
  element.setCapability(element.ALLOW_DETACH);
  element.setCapability(element.ALLOW_CHILDREN_WRITE);
  element.setCapability(element.ALLOW_CHILDREN_READ);
  
  Sphere sph2 = new Sphere(0.8f, base_ap); 
  element.addChild(sph2);
  sphere_object.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  sphere_object.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  sphere_object.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
         
  sphere_object.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
  sphere_object.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
  sphere_object.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
  
  
  
          
  sphere_object.addChild(element);
  sphere_object_tr3d.setTranslation(new Vector3f(1.5f, 0.8f, 0f));
  sphere_object.setTransform(sphere_object_tr3d);
  ground.addChild(sphere_object);
  
  //----------
  sphere_object_ch.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  sphere_object_ch.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  sphere_object_ch.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
  sphere_object_ch.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
  sphere_object_ch.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
  sphere_object_ch.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
  
  sphere_object_tr3d_ch.setTranslation(new Vector3f(0f, 2f, 0f));
  sphere_object_ch.setTransform(sphere_object_tr3d_ch);
  arm_width_control.addChild(sphere_object_ch);
  
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
  if (key == 'p')
  {
      if(!picked_up)
      {
          sphere_object.removeChild(element);
          sphere_object_ch.addChild(element);
          picked_up = !picked_up;
      }
      else
      {
          sphere_object_ch.removeChild(element);//usuniecie przenoszonego elementu
          
          // polozenie chwytaka - zdobycie informacji
          base.getTransform(tr1);
          arm_height_control.getTransform(tr2);
          arm_width_control.getTransform(tr3);
          sphere_object_ch.getTransform(tr4);
          
          //ustawienie transformacji dajacej polozenie chwytaka
          sphere_object_tr3d.setIdentity();
          sphere_object_tr3d.mul(tr1);
          sphere_object_tr3d.mul(tr2);
          sphere_object_tr3d.mul(tr3);
          sphere_object_tr3d.mul(tr4);
          
          //Ustawienie nowego polozenia kulki po opadnieciu
          Vector3f falling1 = new Vector3f();//przesunięcie arm_height_control wzgledem polozenia poczatkowego 
                                             //- potrzenbe tylko by wiedziec o ile opuscic
          tr2.get(falling1);//translacja arm_heigt_control
          
          //setTranslation nadpisuje stary wektor translacji, wiec dajemy nowy (falling2)
          Vector3f falling2 = new Vector3f();//laczne przesuniecie wzgledem srodka podloza
          Matrix4f m4 = new Matrix4f();
          sphere_object_tr3d.get(m4);
          
          falling2.x = m4.m03;//ustawienie wartosci przesuniec x, y, z
          falling2.y = m4.m13;
          falling2.z = m4.m23;
          falling2.y += -falling1.y -base_height/2 + 0.8f;//uaktualnienie przesuniecia w y (pion)
                         // o spadek na ziemie -base_height/2  i promien kuli
                                                           
          sphere_object_tr3d.setTranslation(falling2);//przesuniecie kuli do porzadanego miejca
          sphere_object.setTransform(sphere_object_tr3d);
          sphere_object.addChild(element);//dodanie kuli do transformgroupa zwiazanego z ziemia
          
          picked_up = !picked_up;
      }
      
      //ground.removeChild(sphere_object);
      //objRoot.removechild()
  }
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