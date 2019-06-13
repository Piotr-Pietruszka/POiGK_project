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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

//====================================================================

public class Project_POiG extends Applet implements KeyListener{

  private SimpleUniverse universe = null;
  private Canvas3D canvas = null;
 //PODSTAWA
  private TransformGroup ground = new TransformGroup();
  private Transform3D ground_position = new Transform3D();
  //CYLINDRICAL ARM
  private TransformGroup base = new TransformGroup();
  private Transform3D base3d = new Transform3D();
  private Transform3D base3dstep = new Transform3D();
 
  private TransformGroup  arm_height_control = new TransformGroup();
  private Transform3D    arm_height_control3d = new Transform3D();
  private Transform3D arm_height_control3d_step = new Transform3D();
 
  private TransformGroup  arm_width_control = new TransformGroup();
  private Transform3D    arm_width_control3d = new Transform3D();
  private Transform3D arm_width_control3d_step = new Transform3D();
  //MAGNES
  private TransformGroup  magnes = new TransformGroup();
  private Transform3D    magnes3d = new Transform3D();
  //PRYMITYW
  private TransformGroup  sphere_object = new TransformGroup();
  private Transform3D  sphere_object_tr3d = new Transform3D();
  
  private TransformGroup  sphere_object_ch = new TransformGroup();
  private Transform3D  sphere_object_tr3d_ch = new Transform3D(); 
  
  private BranchGroup element;
  //POMOC W ZNALEZIENIU KONCA CHWYTAKA
  private Transform3D  tr1 = new Transform3D();
  private Transform3D  tr2 = new Transform3D();
  private Transform3D  tr3 = new Transform3D();
  private Transform3D  tr4 = new Transform3D();
  private Transform3D  tr5 = new Transform3D();
  
  private double x_desired,y_desired,z_desired;
  
  private Matrix4d matrix = new Matrix4d();
  
  //POLOZENIE POCZATKOWE PRZY NAGRYWANIU
  private Transform3D  tr_p1 = new Transform3D();
  private Transform3D  tr_p2 = new Transform3D();
  private Transform3D  tr_p3 = new Transform3D();
  private float height_p, radius_p;
  
  //ZMIENNE GLOBALNE
  private float height = 0.0f;
  private float radius = 0.0f;
  private boolean picked_up = false;
  private Vector<Integer> steps = new Vector(); // wektor ruchów
  private boolean recording = false;            //odpowiada za nagrywanie
  private float base_height = 5;
  private float sph_radius = 0.8f;
  //LIGHT
  private TransformGroup sph_tr = new TransformGroup();
  private Transform3D sph_tr_3d = new Transform3D();
 //PANEL I OBSLUGA
   private JButton aktywuj ;
   private JButton aktywuj_p ;
   private JTextArea wsp_x = new JTextArea(1,5);
   private JTextArea wsp_y = new JTextArea(1,5);
   private JTextArea wsp_z = new JTextArea(1,5);
   private JLabel wsp_x_l = new JLabel("x : ");
   private JLabel wsp_y_l = new JLabel("y : ");
   private JLabel wsp_z_l = new JLabel("z : ");
   private JFrame ref_okno;

   JPanel p = new JPanel();

   //====================================================
   //PANEL STEROWANIA I PRZEMIESZCZANIE DO ZADANEJ POZYCJI
   
   private class ObslugaPrzycisku implements ActionListener{

       ObslugaPrzycisku(JFrame okno){
            ref_okno = okno;
       }

       public void actionPerformed(ActionEvent e) {
            JButton bt = (JButton)e.getSource();
            if(bt==aktywuj)
            {
                try
                {
                    x_desired = Double.parseDouble(wsp_x.getText());
                    y_desired = Double.parseDouble(wsp_y.getText()) - 1.8;
                    z_desired = Double.parseDouble(wsp_z.getText());
                    //obrot
                    base3d.get(matrix);
                    for(int i = 0; i < 100 && (Math.atan2(matrix.m02, matrix.m00) > Math.atan2(z_desired, -x_desired) + Math.PI/32 || 
                            Math.atan2(matrix.m02, matrix.m00) <  Math.atan2(z_desired, -x_desired) - Math.PI/32); i++)
                    {
                        move(-1);
                        Thread.sleep(70);
                        base3d.get(matrix);
                    }
                    //wysokosc
                    arm_height_control3d.get(matrix);
                    for(int i = 0; i < 100 && ((matrix.m13 > y_desired +0.06 && matrix.m13 > -1.7) 
                            ||(matrix.m13 < y_desired -0.06&& matrix.m13 < 2.2 )); i++)
                    {
                        if(matrix.m13 < y_desired)
                            move(2);
                        else
                            move(-2);
                        Thread.sleep(70);
                        arm_height_control3d.get(matrix);
                    }
                    //promien
                    arm_width_control3d.get(matrix);
                    for(int i = 0; i < 100 && (( matrix.m13+2. > Math.sqrt(Math.pow(x_desired,2)+Math.pow(z_desired,2)) +0.06 && matrix.m13+2. > 3.2 )
                            ||( matrix.m13+2. < Math.sqrt(Math.pow(x_desired,2)+Math.pow(z_desired,2)) -0.06 && matrix.m13+2. < 4.9)); i++)
                    {
                        if(matrix.m13+2. < Math.sqrt(Math.pow(x_desired,2)+Math.pow(z_desired,2)))
                            move(3);
                        else
                            move(-3);
                        Thread.sleep(70);
                        arm_width_control3d.get(matrix);
                    }
                }
               catch(Exception exception)
               {
                    JOptionPane.showMessageDialog(ref_okno, "Błędne wartości");
               }
            }
            else if(bt==aktywuj_p)
            {
                try{
                    x_desired = Double.parseDouble(wsp_x.getText());
                    z_desired = Double.parseDouble(wsp_z.getText());
                    if(Math.sqrt(Math.pow(x_desired,2)+Math.pow(z_desired,2)) > 4.2 
                            && Math.sqrt(Math.pow(x_desired,2)+Math.pow(z_desired,2)) < 6)
                    {
                        move_sphere(x_desired, z_desired);
                    }
                }
               catch(Exception exception)
               {
                    JOptionPane.showMessageDialog(ref_okno, "Błędne wartości");
               }
            }
        }
   }
 
//===================================================
   
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
  
  aktywuj = new JButton("przemiesc chwytak");
  aktywuj.addActionListener(new ObslugaPrzycisku(ref_okno));
  
  aktywuj_p = new JButton("przemiesc przedmiot");
  aktywuj_p.addActionListener(new ObslugaPrzycisku(ref_okno));

  //dodanie elementow do panelu
  p.add(wsp_x_l);
  p.add(wsp_x);
  p.add(wsp_y_l);
  p.add(wsp_y);
  p.add(wsp_z_l);
  p.add(wsp_z);
  p.add(aktywuj);
  p.add(aktywuj_p);
  
  add("North", p);
 }

 private BranchGroup createSceneGraph() {
    BranchGroup objRoot = new BranchGroup();
    objRoot.addChild(createPrimitives());
  return objRoot;
 }

 private BranchGroup createPrimitives() {

  BranchGroup objRoot = new BranchGroup();
    
  //PODSTAWA
  ground.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  ground_position.setTranslation(new Vector3d(0, -3.0, -23.0));
  ground_position.setRotation(new AxisAngle4f(1f, -0.5f, 0f, 0.3f));
  ground.setTransform(ground_position);
  Appearance ground_ap = createAppearance(new Color3f(1f, 3f, 1f));
  Material mat = new Material(new Color3f(0.1f,0,0.1f),new Color3f (0.1f,0,0.1f),new Color3f (1,1,1), new Color3f(3f,0,5f), 40);
  ground_ap.setMaterial(mat);
  Cylinder ground_cylinder = new Cylinder(8.25f, 0.03f, ground_ap);
  ground.addChild(ground_cylinder);
  objRoot.addChild(ground);
  
  //GŁOWNY WALEC
  base.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  base3d.setTranslation(new Vector3d(0.0, base_height/2, 0.0));
  base3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
  base3d.setScale(1.0);
  base.setTransform(base3d);
  Appearance base_ap = createAppearance(new Color3f(138f, 127f, 128f));
  
  Cylinder basic_cylinder = new Cylinder(0.5f, base_height, 1, 50, 10, base_ap);
  base.addChild(basic_cylinder);
  ground.addChild(base);  
   
  //RAMIE (STEROWANE WYSOKOSCIA)
   arm_height_control.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
   arm_height_control3d.setTranslation(new Vector3d(-1.0, 0.0, 0.0));
   arm_height_control3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 3.14f, 1.57f));
   arm_height_control3d.setScale(1);
   arm_height_control.setTransform( arm_height_control3d);
   Appearance arm_height_ap = createAppearance(new Color3f(138f, 127f, 128f));//138f, 127f, 128f

   Box arm_height = new Box(0.3f, 2.0f,0.5f, arm_height_ap);
   arm_height_control.addChild(arm_height);
  
//RAMIE WYSUWANE
  arm_width_control.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  arm_width_control.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
  arm_width_control.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
  
  arm_width_control3d.setTranslation(new Vector3d(0, 1.55, 0));
  arm_width_control3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
  arm_width_control3d.setScale(1);
  arm_width_control.setTransform(arm_width_control3d);
  Appearance arm_width_ap = createAppearance(new Color3f(138f, 127f, 128f));

  Box arm_width = new Box(0.2f,1f,0.3f, arm_width_ap);
  
  //MAGNES
  magnes.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  magnes3d.setTranslation(new Vector3f(0f, 1f, 0f));
  magnes3d.setRotation(new AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f));
  magnes3d.setScale(1);
  magnes.setTransform(magnes3d);
  
  Cylinder magnes_s = new Cylinder (0.5f,0.2f,arm_width_ap);
  
  magnes.addChild(magnes_s);
  arm_width.addChild(magnes);
  arm_width_control.addChild(arm_width);
  arm_height_control.addChild(arm_width_control);
  base.addChild( arm_height_control);
  
  //SWIATLO
  PointLight lp = new PointLight();
  lp=createPointLight(0.5f, 1f, 1f,   -4f, 7f, 3f,   5.0f, 0f, 30f);
  
    //Sfera w zrodle swiatla punktowego
    Point3f point_l = new Point3f();
    lp.getPosition(point_l);
  //sw punktowe
  ground.addChild(lp);
  objRoot.addChild(createAmbientLight(0.5f, 1f, 1f));
  
  //ROTACJA KAMERY  
  MouseRotate myMouseRotate = new MouseRotate();
        myMouseRotate.setTransformGroup(ground);
        myMouseRotate.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseRotate);

        MouseWheelZoom myMouseZoom = new MouseWheelZoom();
        myMouseZoom.setTransformGroup(universe.getViewingPlatform().getViewPlatformTransform());
        myMouseZoom.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(myMouseZoom);
  
  //PRYMITYW     
  element = new BranchGroup();
  element.setCapability(element.ALLOW_DETACH);
  element.setCapability(element.ALLOW_CHILDREN_WRITE);
  
  Sphere sph2 = new Sphere(sph_radius, base_ap); 
  element.addChild(sph2);
  sphere_object.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  sphere_object.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
  sphere_object.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
  
  sphere_object.addChild(element);
  sphere_object_tr3d.setTranslation(new Vector3f(5f, sph_radius, 0f));
  sphere_object.setTransform(sphere_object_tr3d);
  ground.addChild(sphere_object);
  
  //PRYMITYW (CHILD)
  sphere_object_ch.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
  sphere_object_ch.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
  sphere_object_ch.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
  sphere_object_ch.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
  sphere_object_ch.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
  
  sphere_object_tr3d_ch.setTranslation(new Vector3f(0f, 1.9f, 0f));
  sphere_object_ch.setTransform(sphere_object_tr3d_ch);
  arm_width_control.addChild(sphere_object_ch);
  
  objRoot.compile();

  return objRoot;

 }

 //=====================================================================
 //APPEARANCE
 private Appearance createAppearance(Color3f color) {
  Appearance ap = new Appearance();
  Material mat = new Material(new Color3f(0.4f, 0.2f, 0.4f),new Color3f (0,0,0.1f),new Color3f (1,1,1), new Color3f(0f,50f,50f), 50);
  mat.setSpecularColor(color);
  ap.setMaterial(mat);
  
  return ap;
 }

 //=======================================================================
 //POINT LIGHT
 private PointLight createPointLight(float r, float g, float b, float x, float y,
   float z,  float x2, float y2, float z2) {

    PointLight light = new PointLight(new Color3f(r, g, b), new Point3f(x, y, z), new Point3f(-0.01f, 0.f, 0.01f));
    light.setInfluencingBounds(new BoundingSphere(new Point3d(x2, y2, z2), 10000.0));

    return light;
 }
 //AMBIENT LIGHT
private Light createAmbientLight(float r, float g, float b) {
      Color3f ambientColor = new Color3f(r, g, b);
      AmbientLight ambientLight = new AmbientLight(true,ambientColor);

      ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(-5.0f, 0f, 0f), 10000.0));
      return ambientLight;
 }

//=========================================
//ARM CONTROL
private void move(int key){
    switch (key){
        case 1:       
        {
            base3dstep.rotY(Math.PI / 32);
            base.getTransform(base3d);
            base3d.mul(base3dstep);
            base.setTransform(base3d);
            break;
        }
        case -1:{
            base3dstep.rotY(-Math.PI / 32);
            base.getTransform(base3d);
            base3d.mul(base3dstep);
            base.setTransform(base3d);
            break;
        }
        case 2:{
           height+=0.1;
           arm_height_control3d_step.setTranslation(new Vector3d(0.1, 0, 0.0));
           arm_height_control.getTransform( arm_height_control3d);
           arm_height_control3d.mul( arm_height_control3d_step);
           arm_height_control.setTransform( arm_height_control3d);
           break;
        }
        case -2:{
            height-=0.1;
            arm_height_control3d_step.setTranslation(new Vector3d(-0.1, 0, 0.0));
            arm_height_control.getTransform( arm_height_control3d);
            arm_height_control3d.mul( arm_height_control3d_step);
            arm_height_control.setTransform( arm_height_control3d);
            break;
        }
        case 3:{
            radius += 0.1;
           arm_width_control3d_step.setTranslation(new Vector3d(0, 0.1, 0.0));
           arm_width_control.getTransform( arm_width_control3d);
           arm_width_control3d.mul( arm_width_control3d_step);
           arm_width_control.setTransform( arm_width_control3d);
           break;
        }
        case -3:{
            radius -= 0.1;
            Vector3f position = new Vector3f();
            arm_width_control3d.get(position);
            arm_width_control3d_step .setTranslation(new Vector3d(0, -0.1, 0.0));
            arm_width_control.getTransform( arm_width_control3d);
            arm_width_control3d.mul(arm_width_control3d_step);
            arm_width_control.setTransform( arm_width_control3d);
            break;
        }
        case 4 :
        case -4:{
          if(!picked_up)
          {       
            //polozenie sfery
            sphere_object.getTransform(tr5);
            Matrix4f m4_sphere = new Matrix4f();
            tr5.get(m4_sphere);
            
            //polozenie chwytaka
            base.getTransform(tr1);
            arm_height_control.getTransform(tr2);
            arm_width_control.getTransform(tr3);
            magnes.getTransform(tr4);

            sphere_object_tr3d.setIdentity();
            sphere_object_tr3d.mul(tr1);
            sphere_object_tr3d.mul(tr2);
            sphere_object_tr3d.mul(tr3);
            sphere_object_tr3d.mul(tr4);

            Matrix4f m4_chwytak = new Matrix4f();
            sphere_object_tr3d.get(m4_chwytak);
            
            if((Math.pow(m4_sphere.m03-m4_chwytak.m03 ,2) + Math.pow(m4_sphere.m13-m4_chwytak.m13, 2) 
                    +Math.pow(m4_sphere.m23-m4_chwytak.m23, 2)  <  1.5))
            {
              sphere_object.removeChild(element);
              sphere_object_ch.addChild(element);
              picked_up=!picked_up;
            }
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
            falling2.y += -falling1.y -base_height/2 + sph_radius;//uaktualnienie przesuniecia w y (pion)
                           // o spadek na ziemie -base_height/2  i promien kuli

            sphere_object_tr3d.setTranslation(falling2);//przesuniecie kuli do porzadanego miejca
            sphere_object.setTransform(sphere_object_tr3d);
            sphere_object.addChild(element);//dodanie kuli do transformgroupa zwiazanego z ziemia
            picked_up=!picked_up;
          
          }
          break;
        }
   default: break;
   }
}


public void move_sphere(double x_des, double z_des){
    
    sphere_object_tr3d.setTranslation(new Vector3d(x_des, sph_radius, z_des));
    sphere_object.setTransform(sphere_object_tr3d);
}


////=================================================================
//MAIN
 public static void main(String[] args) {
  Project_POiG applet = new Project_POiG();
  Frame frame = new MainFrame(applet, 800, 600);
 }
 
//====================================================
//OBSŁUGA KLAWIATURY
 public void keyTyped(KeyEvent e) {
  char key = e.getKeyChar();
  if (key == 'p')
  {
          move(4);
          if (recording)
            steps.add(4);
  }
  
  if (key == 'z') {
   move(1);
   if (recording)
       steps.add(1);
  }

  if (key == 'x') {
   move(-1);
    if (recording)
       steps.add(-1);
  }

  if (key == 'w') {
      if(height < 2.2)
      {
       move(2);
       if (recording)
            steps.add(2);
      }
  }

  if (key == 's') {
       if(height > -1.7)
      {
        move(-2);
        if (recording)
       steps.add(-2);
      }
  }
  if (key == 'd') {
      if(radius < 1.4)
      {
        move(3);
        if (recording)
         steps.add(3);
      }
       
  }
   if (key == 'a') {
    if(radius > -0.4)
      {
        move(-3);
         if (recording)
       steps.add(-3);
      }
   }
    if (key == 'k'){
        //---rozpoczęcie/zakonczenie nagrywania
        recording = !recording;
        if(recording)
        {
            steps.removeAllElements (); //---czyszczenie elementów wektora
            base.getTransform(tr_p1);
            arm_height_control.getTransform(tr_p2);
            arm_width_control.getTransform(tr_p3);
            height_p = height;
            radius_p = radius;
        }
            
    }
    
    if (key == 'l'&& !recording){
        //-----------przywracanie do pozycji sprzed nagrywania
        base.setTransform(tr_p1);
        arm_height_control.setTransform(tr_p2);
        arm_width_control.setTransform(tr_p3);
        height = height_p;
        radius = radius_p;
        
        //odtwarzanie ruchu robota 
        for(int k = 0;k<steps.size();k++)
        {
            move(steps.get(k));
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(Project_POiG.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    if (key=='r'){ //resetowanie pozycji
         ground_position.setTranslation(new Vector3d(0, -3.0, -23.0));
         ground_position.setRotation(new AxisAngle4f(1f, -0.5f, 0f, 0.3f));
         ground.setTransform(ground_position);
    }

 }
 
 public void keyReleased(KeyEvent e) {
 }

 public void keyPressed(KeyEvent e) {
 }

}

