package RayTracing;

public class Vector {
double x;
double y;
double z;
double norm;
public Vector() {
	
}

public Vector(double d, double e, double f) {
	super();
	this.x=d;
	this.y=e;
	this.z=f;
	if(this.x==0 && this.y==0 && this.z==0) {
		this.norm=0;
	}
	else
		this.norm=Math.sqrt(this.x*this.x+this.y*this.y+this.z*this.z);
}
public static double dot (Vector z,Vector x) {
	return (x.x*z.x+z.y*x.y+z.z*x.z);
}
public static Vector cross(Vector v,Vector u) {
	return new Vector(v.y*u.z-v.z*u.y,v.z*u.x-v.x*u.z,v.x*u.y-v.y*u.x);
}

/**
 * sub the vector u from the vector v
 * @param v
 * @param u
 * @return a new vector v-u
 */
// subtracts two vectors
public static Vector sub(Vector v, Vector u) {
	return new Vector(v.x-u.x,v.y-u.y,v.z-u.z);
}
//adds two vectors
public static Vector add( Vector v, Vector u) {
	return new Vector(v.x+u.x,v.y+u.y,v.z+u.z);
}
//finds the angle between two vectors
public static double angle(Vector v, Vector u) {
	double dot =dot(v,u);
	double cos_theta=dot/(u.norm*v.norm);
	return Math.atan(cos_theta);
}
// multiplies the vector by a scaler
public static Vector multiply_Scal(Vector v, double x) {
	return new Vector(v.x*x,v.y*x,v.z*x);
}
public static Vector copy(Vector v) {
	return new Vector (v.x,v.y,v.z);
}
// scales vector v by x and adds it to the this vector.
public void scale_add(Vector v, double x) {
	this.x+=v.x*x;
	this.y+=v.y*x;
	this.z+=v.z*x;
	if(this.x==0 && this.y==0 && this.z==0) {
		this.norm=0;
	}
	else
		this.norm=Math.sqrt(this.x*this.x+this.y*this.y+this.z*this.z);

}
// normalizes a vector
public void normalize() {
	x=x/norm;
	y=y/norm;
	z=z/norm;
	norm=1;
	return;
}

/**
 * check if the vector coordinates values are 0
 * @return zero - true if the vector value is 0 false otherwise
 */
public boolean isZero() {
	return this.x==0 && this.y==0 && this.z==0;
}

public double getX() {
	return x;
}

public double getY() {
	return y;
}

public double getZ() {
	return z;
}
// multiplies each coordinate of vector v with the coordinate of vector u
public static Vector Mul(Vector v, Vector u) {
	return new Vector(v.x*u.x,v.y*u.y,v.z*u.z);
}
// makes sure that the rgb color doesn't go out of range
public void fixColor() {
	this.x=Math.min(this.x, 1);
	this.y=Math.min(this.y, 1);
	this.z=Math.min(this.z, 1);
	if(this.x==0 && this.y==0 && this.z==0) {
		this.norm=0;
	}
	else
		this.norm=Math.sqrt(this.x*this.x+this.y*this.y+this.z*this.z);

}
// calculates the actual rgb color from the rgb of light an the amout of light that gets to the point
public void setRGB(Vector v) {
	this.x*=v.x;
	this.y*=v.y;
	this.z*=v.z;
	if(this.x==0 && this.y==0 && this.z==0) {
		this.norm=0;
	}
	else
		this.norm=Math.sqrt(this.x*this.x+this.y*this.y+this.z*this.z);
	
}

}