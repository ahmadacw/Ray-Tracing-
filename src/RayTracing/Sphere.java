
public class Sphere extends items{
	Vector center;
	float radius;
	public Sphere(Vector center, float radius, int indx) {
		super();
		this.indx=indx;

		this.center = center;
		this.radius = radius;
	}
	public double intersect(Ray r) { // returns the variable setting for the intersection point, null if there is none.
		Vector L= Vector.sub(this.center,r.p0);
		double t_ca=Vector.dot(L, r.V);
		if(t_ca<0)
			return -1;
		double d=Vector.dot(L, L)-((t_ca)*t_ca);
		if(d>this.radius*this.radius)
			return -1;
		double t_hc=Math.sqrt((this.radius*this.radius)-d);
		double [] x= new double[2];
		x[0]=(t_ca-t_hc);
		x[1]=(t_ca+t_hc);
		if(x[0]>x[1]) {
			return x[1];
		}
		return x[0];
		
	}
	
	@Override
	public Vector getNormal(Vector v) {
		Vector norm = Vector.sub(v, this.center);
		norm.normalize();
		return norm;
	}
	@Override
	protected Material getMaterial() {
		return texture;
	}
	
}