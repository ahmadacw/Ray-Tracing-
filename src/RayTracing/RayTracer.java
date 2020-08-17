package RayTracing;


import java.awt.Transparency;


import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {
	Camera cam;
	List<items> obj = new ArrayList<>();
	List<Light> lights = new ArrayList<>();
	List<Material> materials= new ArrayList<>();
	Settings sets;
	double epsilon = 0.001;
	public int imageWidth;
	public int imageHeight;
	private Random rand = new Random();
	
	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size as input.
	 */
	public static void main(String[] args) {

		try {

			RayTracer tracer = new RayTracer();

            // Default values:
			tracer.imageWidth = 500;
			tracer.imageHeight = 500;

			if (args.length < 2)
				throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");
			
			String sceneFileName = args[0];
			String outputFileName = args[1];
		
			if (args.length > 3)
			{
				tracer.imageWidth = Integer.parseInt(args[2]);
				tracer.imageHeight = Integer.parseInt(args[3]);
			}


			// Parse scene file:
			tracer.parseScene(sceneFileName);

			// Render scene:
			tracer.renderScene(outputFileName);

//		} catch (IOException e) {
//			System.out.println(e.getMessage());
		} catch (RayTracerException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}


	}

	/**
	 * Parses the scene file and creates the scene. Change this function so it generates the required objects.
	 */
	public void parseScene(String sceneFileName) throws IOException, RayTracerException
	{
		FileReader fr = new FileReader(sceneFileName);

		BufferedReader r = new BufferedReader(fr);
		String line = null;
		int lineNum = 0;
		System.out.println("Started parsing scene file " + sceneFileName);



		while ((line = r.readLine()) != null)
		{
			line = line.trim();
			++lineNum;

			if (line.isEmpty() || (line.charAt(0) == '#'))
			{  // This line in the scene file is a comment
				continue;
			}
			else
			{
				String code = line.substring(0, 3).toLowerCase();
				// Split according to white space characters:
				String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

				if (code.equals("cam"))
				{
					Vector p = new Vector(Double.parseDouble(params[0]),Double.parseDouble(params[1]),Double.parseDouble(params[2]));
					Vector l= new Vector(Double.parseDouble(params[3]),Double.parseDouble(params[4]),Double.parseDouble(params[5]));
					Vector u = new Vector(Double.parseDouble(params[6]),Double.parseDouble(params[7]),Double.parseDouble(params[8]));
					cam=(new Camera(p,l,u,Float.parseFloat(params[9]),Float.parseFloat(params[10])));
					cam.sc_height=cam.sc_width*(imageHeight/imageWidth);
					cam.p1=Vector.copy(l);
					System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
				}
				else if (code.equals("set"))
				{
					Vector b = new Vector(Double.parseDouble(params[0]),Double.parseDouble(params[1]),Double.parseDouble(params[2]));
					this.sets=new Settings(b,Float.parseFloat(params[3]),Float.parseFloat(params[4]),Float.parseFloat(params[5]));
					System.out.println(String.format("Parsed general settings (line %d)", lineNum));
				}
				else if (code.equals("mtl"))
				{
					Vector d = new Vector(Double.parseDouble(params[0]),Double.parseDouble(params[1]),Double.parseDouble(params[2]));
					Vector s= new Vector(Double.parseDouble(params[3]),Double.parseDouble(params[4]),Double.parseDouble(params[5]));
					Vector r_mtrl = new Vector(Double.parseDouble(params[6]),Double.parseDouble(params[7]),Double.parseDouble(params[8]));
					materials.add(new Material(d,s,r_mtrl,Float.parseFloat(params[9]),Float.parseFloat(params[10])));
					System.out.println(String.format("Parsed material (line %d)", lineNum));
				}
				else if (code.equals("sph"))
				{
					Vector c = new Vector(Double.parseDouble(params[0]),Double.parseDouble(params[1]),Double.parseDouble(params[2]));
					obj.add(new Sphere(c,Float.parseFloat(params[3]),Integer.parseInt(params[4])));
					System.out.println(String.format("Parsed sphere (line %d)", lineNum));
				}
				else if (code.equals("pln"))
				{
					Vector n = new Vector(Double.parseDouble(params[0]),Double.parseDouble(params[1]),Double.parseDouble(params[2]));
					obj.add(new Plane(n,Float.parseFloat(params[3]),Integer.parseInt(params[4])));
					System.out.println(String.format("Parsed plane (line %d)", lineNum));
				}
				else if (code.equals("lgt"))
				{
					Vector p = new Vector(Double.parseDouble(params[0]),Double.parseDouble(params[1]),Double.parseDouble(params[2]));
					Vector rgb = new Vector(Double.parseDouble(params[3]),Double.parseDouble(params[4]),Double.parseDouble(params[5]));
					lights.add(new Light(p,rgb,Float.parseFloat(params[6]),Float.parseFloat(params[7]),Float.parseFloat(params[8])));
					System.out.println(String.format("Parsed light (line %d)", lineNum));
				}
				else if(code.equals("trg")){
					Vector p = new Vector(Double.parseDouble(params[0]),Double.parseDouble(params[1]),Double.parseDouble(params[2]));
					Vector l= new Vector(Double.parseDouble(params[3]),Double.parseDouble(params[4]),Double.parseDouble(params[5]));
					Vector u = new Vector(Double.parseDouble(params[6]),Double.parseDouble(params[7]),Double.parseDouble(params[8]));
					obj.add(new Triangle(p,l,u,Integer.parseInt(params[9])));
				}
				
				else
				{
					System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
				}
			}
		}
		r.close();

                // It is recommended that you check here that the scene is valid,
                // for example camera settings and all necessary materials were defined.
		if (cam == null){
			throw new RayTracerException("your camera is not defined");
		}
		if (sets == null){
			throw new RayTracerException("your Settings are not defined");
		}
		for (items s: obj){
			if (s.indx-1 >= materials.size() || s.indx-1<0){
				throw new RayTracerException(s.getClass().getName().split("RayTracing.")[1]+" material is missing");
			}
		}
		for(items x:obj) {
			x.texture=materials.get(x.indx-1);
		}	
		
		System.out.println("Finished parsing scene file " + sceneFileName);
	}

	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	public void renderScene(String outputFileName)
	{
		long startTime = System.currentTimeMillis();
		double cons1=-cam.sc_height/(sets.SS*imageHeight);
		double cons2=cam.sc_width/(sets.SS*imageWidth);
		byte[] rgbData = new byte[this.imageWidth*this.imageHeight*3 ];
		cam.p1=Vector.add(cam.getPos(),Vector.multiply_Scal(cam.getTowards(),cam.sc_dist));//moves the look vector to the middle of the screen
		cam.p1.scale_add(cam.getRight(),-cam.getSc_width()/2.);// moves the look vector to the left most of the screen
		Vector actual_up=cam.getRealUp();// calculates a vector that is perpendicular to the look and right vector.
		actual_up.normalize();
		cam.p1.scale_add(actual_up,cam.sc_height/2);//moves the look vector to the top of the screen
		Vector v=Vector.copy(cam.getP1()); // assist
		Ray r = new Ray(cam.getPos(),Vector.copy(v)); // the current ray
		double SS=sets.SS*sets.SS; // constant
		Vector v2=Vector.copy(v); // keeps track of how far down the screen we are
		for(int i=0; i<imageHeight;i++) {
			v2=Vector.copy(v); // resets the ray to the start of a new row
			for(int j=0;j<imageWidth;j++) {
				double [] color = new double [] {0,0,0};
				for(int ss=0; ss<SS;ss++) {
					double r1=rand.nextDouble();// random numbers for super sampling
					double r2=rand.nextDouble();
					while(r1>1-epsilon||r1<0+epsilon) {
						r1=rand.nextDouble();//makes sure we don't hit the edges
					}
					while(r2>1-epsilon||r2<0+epsilon) {
						r2=rand.nextDouble();//makes sure we don't hit the edges
					}
					r1-=0.5; 
					r2-=0.5;
					// moves the range of the random number from 0 to 1 to -0,5 to 0,5
					r.V=Vector.copy(v2); // copies r.v to be the center of the pixel to the right of the pixel from the last iteration
					r.V.scale_add(cam.getRight(),(ss%sets.SS+r1)*(cons1));
					r.V.scale_add(actual_up,(ss/sets.SS+r2)*(cons2));
					// moves the look vector to a position inside the super sampling quadrant 
					r.V.scale_add( cam.getPos(), -1);
					// creates a vector from the cameras position to the point on the screen
					r.V.normalize();
					Vector currColor= getColor(r,0);// calculates the color of the super sampling quadrant
					color[0]+=currColor.getX();
					color[1]+=currColor.getY();
					color[2]+=currColor.getZ();
					//color calculation
				}
				
				rgbData[(i*this.imageWidth+j)*3]=(byte) Math.round(255*color[0]/(SS));
				rgbData[(i*this.imageWidth+j)*3+1] = (byte) Math.round(255*color[1]/(SS));
				rgbData[(i*this.imageWidth+j)*3+2]=  (byte) Math.round(255*color[2]/(SS));
				//updating the final color matrix
				v2.scale_add(cam.getRight(),cam.getSc_width()/imageWidth);
				// updating the look vector to be the middle of the pixel to the right of the last iteration.
			}
			v.scale_add(actual_up, -cam.getSc_height()/imageHeight);
			// updating the look vector to be the next row down the line.
		}

		long endTime = System.currentTimeMillis();
		Long renderTime = endTime - startTime;

                // The time is measured for your own convenience, rendering speed will not affect your score
                // unless it is exceptionally slow (more than a couple of minutes)
		System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

                // This is already implemented, and should work without adding any code.
		saveImage(this.imageWidth, rgbData, outputFileName);
		System.out.println("Saved file " + outputFileName);

	}



	
	/**
	 * 
	 * @param r - Ray of light
	 * @param recursiveDepth - the numbers of recursive steps we have done
	 * @return Vector of the color of the pixel
	 */
	private Vector getColor(Ray r,int recursiveDepth) {
		if(recursiveDepth>=sets.rec_max) {
			return new Vector(0,0,0);
		}
		Vector color = new Vector(0,0,0);
		Vector closestItem = new Vector(0,0,0);
		items nearestObject = getNearestObject(r,closestItem);// the nearest object
		if(nearestObject!=null) {
			double t= nearestObject.intersect(r);// the distance from the ray origin point to the nearest object
			closestItem=r.calculate(t);//the location of the nearest object
			Material objectMaterial = nearestObject.getMaterial();// the material of the nearest object
			float transparency=objectMaterial.getTrans();
			Vector transparencyRGB=new Vector(0,0,0);// the color vector of the transparency part
			Vector reflectionRGB=new Vector(0,0,0);// the color vector of the reflection part
			Vector nextpos;// the next origin point (for the recursion) 			
			if(transparency!=0) {
				nextpos=Vector.multiply_Scal(r.getV(),0.00001);
				nextpos=Vector.add(nextpos, closestItem);//the next ray position will be after the first transparency object
				Ray nextRay=new Ray(nextpos,r.getV());// the ray for the recursive calculation
				transparencyRGB=getColor(nextRay,recursiveDepth+1);
				transparencyRGB=Vector.multiply_Scal(transparencyRGB, transparency);
			}
			
			if(!objectMaterial.getReflection().isZero()) {
				Vector norm=nearestObject.getNormal(closestItem);
				Vector reflection=getReflectionVector(r.getV(), norm);
				nextpos=Vector.multiply_Scal(reflection,0.00001);//the next ray position will be after the first transparency object
				nextpos=Vector.add(nextpos, closestItem);
				Ray nextRay=new Ray(nextpos,reflection);// the ray for the recursive calculation
				reflectionRGB=getColor(nextRay,recursiveDepth+1);
				reflectionRGB=Vector.Mul(reflectionRGB, objectMaterial.getReflection());
			}
			
			for(Light l : lights) {
				Vector lightColor=baseColor(closestItem,nearestObject,l,r.getV());
				if (l.getShadow()!=0 && !lightColor.isZero())
					lightColor=Vector.multiply_Scal(lightColor, (1-l.getShadow()+(l.getShadow())*softShadow(closestItem, l)));
			
				color=Vector.add(color, lightColor);
					
			}
			color.fixColor();
			color=Vector.multiply_Scal(color, 1-transparency);
			color=Vector.add(color,transparencyRGB);
			color=Vector.add(color, reflectionRGB);
			color.fixColor();
		}
		else {
			color=Vector.copy(sets.bg);
		}
		return color;
	}


	/**
	 * 
	 * @param v - the ray vector
	 * @param n - the normal to the object
	 * @return R - the reflected vector
	 */
	private Vector getReflectionVector(Vector v,Vector n) {
		Vector R=Vector.multiply_Scal(v, 1);
		double temp=Vector.dot(v, n);
		R=Vector.sub(R, Vector.multiply_Scal(Vector.multiply_Scal(n, temp),2));
		return R;
	}
	
	/**
	 * calculates the first object the ray r intersects with
	 * @param width
	 * @param rgbData
	 * @param fileName
	 */
	private items getNearestObject(Ray r,Vector closestItem) {
		double minDist=Double.POSITIVE_INFINITY;
		boolean foundObject=false;
		items obj1=null;
		double t;
		for(items item:obj) {
			t=item.intersect(r);
			if(t>0 && t<minDist) {
				minDist=t;
				foundObject=true;
				obj1=item;
			}
		}
		if(foundObject) {
			return obj1;
		}
		return null;
	}


	public Vector baseColor(Vector point,items surface,Light lgt,Vector dir) {
		Vector lgt_dir= Vector.sub(lgt.p,point );// calculates the light direction
		Vector dfs=surface.texture.diff;
		Vector spc=surface.texture.spec;
		lgt_dir.normalize();
		Vector norm=surface.getNormal(point);// gets the normal of the surface at the point.
		if(Vector.dot(dir,norm)>0)
			norm=Vector.multiply_Scal(norm, -1);// flips the direction of the normal if needed
		double theta=Vector.dot(lgt_dir, norm);
		//calculates the angle of the light when it hits the surface
		if(theta<=0) {
			return new Vector(0,0,0);
		}
		dfs=Vector.multiply_Scal(dfs, theta);
		// the diffused light is proportional to the cosine of the angle of the intersection
		if(!spc.isZero()) {
			Vector ref = Vector.copy(norm);
			ref=Vector.multiply_Scal(ref, 2*Vector.dot(lgt_dir, norm));
			ref=Vector.sub(ref, lgt_dir);
			theta=Vector.dot(dir,ref);
			if(theta<0) {
				theta=Math.pow(theta, surface.texture.phong);
				spc=Vector.multiply_Scal(spc, theta);
				dfs.scale_add( spc, lgt.spec);
			}
			
		}
		dfs.fixColor();
		dfs.setRGB( lgt.rgb);
		return dfs;
		
	}
	
	public double softShadow(Vector pt,Light lgt) {
		double total=0.0;
		double cons=lgt.width/sets.sh_rays;
		if(cons==0) {// no soft shadows from the light
			return lightActual(pt,Vector.copy(lgt.p));
			//calculates the light intensity that actually gets to the point pt.
		}
		double cons1=-lgt.width/2;
		Vector dir=Vector.sub(lgt.p,pt);// creates a vector from the light position to the point
		Vector up= Vector.cross(dir, cam.getRealUp());
		//calculates the vector that would move the source of the light up the plane of the light
		Vector side= Vector.cross(dir,up);
		//calculates the vector that would move the source of the light sideways the plane of the light
		up.normalize();
		side.normalize();
		Vector movedPos=Vector.copy(lgt.p);// keeps track of how far sideways we are
		Vector lgtpos=Vector.copy(lgt.p);//keeps track of which column we are on
		lgtpos.scale_add(up, cons1);//moves the light to the top of the plane
		lgtpos.scale_add(side, cons1);// moves the light to the side of the plane
		for(int i =0; i<sets.sh_rays;i++) {
			movedPos=Vector.copy(lgtpos);
			for(int j =0; j<sets.sh_rays;j++) {
				double r1=rand.nextDouble();// random numbers for supersampling the shadow
				double r2=rand.nextDouble();
				while(r1>1-epsilon||r1<0+epsilon) {
					r1=rand.nextDouble();//makes sure we don't hit the edges
				}
				while(r2>1-epsilon||r2<0+epsilon) {
					r2=rand.nextDouble();//makes sure we don't hit the edges
				}
				r1-=0.5;
				r2-=0.5;
				// moves the range of the random number from 0 to 1 to -0,5 to 0,5
				movedPos.scale_add(up, r1*(cons));
				movedPos.scale_add(side, r2*(cons));
				//moves it inside the current quadrent
				total+=lightActual(pt,movedPos);
				//calculates how much actual light gets to the point
				movedPos.scale_add(up,-r1*(cons));
				movedPos.scale_add(side,(1-r2)*cons);
				// moves it back to the middle of the right quadrant
			}
			lgtpos.scale_add(up,cons);// moves the vector down one more column
		}
		return total/(sets.sh_rays*sets.sh_rays);// averages the light and returns the shadow constant
	}
	/*
	 * calculates the actual light getting from a ray originating in lgt_pos to the point pt
	 */
	public double lightActual(Vector pt,Vector lgt_pos) {
		Vector lgt_dir=Vector.sub(pt, lgt_pos);// calculates the direction vector
		double dist=lgt_dir.norm; // the distance between the point and the light source
		lgt_dir.normalize();
		double curr_lvl=1;
		double t;
		Ray r=new Ray(Vector.copy(lgt_pos),lgt_dir);// the ray from the light to the point
		for(items x:obj) {// for every object
			t=x.intersect(r);
			//calculates the intersection between the light and the object
			if(t<dist-0.00001&&t>0.00001) {
				//check if the intersection is behind our point or if it is smaller than epsilon
				if(x.texture.trans==0) {
					// if the hit object is in front of the point and is not transparent no light reachs the point
					return 0;
				}
				curr_lvl*=x.texture.trans; // the light intensity after passing the object
			}
		}
		return curr_lvl;// returns the actual light reaching the point

	}
	
	//////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

	
	
	/*
	 * Saves RGB data as an image in png format to the specified location.
	 */
	public static void saveImage(int width, byte[] rgbData, String fileName)
	{
		try {

			BufferedImage image = bytes2RGB(width, rgbData);
			ImageIO.write(image, "png", new File(fileName));

		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}

	}

	/*
	 * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
	 */
	public static BufferedImage bytes2RGB(int width, byte[] buffer) {
	    int height = buffer.length / width / 3;
	    ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	    ColorModel cm = new ComponentColorModel(cs, false, false,
	            Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    SampleModel sm = cm.createCompatibleSampleModel(width, height);
	    DataBufferByte db = new DataBufferByte(buffer, width * height);
	    WritableRaster raster = Raster.createWritableRaster(sm, db, null);
	    BufferedImage result = new BufferedImage(cm, raster, false, null);

	    return result;
	}

	@SuppressWarnings("serial")
	public static class RayTracerException extends Exception {
		public RayTracerException(String msg) {  super(msg); }
	}


}