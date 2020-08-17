package RayTracing;

public class Plane  extends items{
	Vector norm;
	float offset;
	public Plane(Vector n, float offset, int indx) {
		super();
		this.indx=indx;
		this.norm = n;
		this.offset = offset;
	}
	public double intersect(Ray r) { // returns the variable setting for the intersection point, null if there is none.
		double x= -((Vector.dot(r.p0,norm)-this.offset)/Vector.dot(r.V, norm));
		return x;
	}
	
	@Override
	public Vector getNormal(Vector v) {
		return this.norm;
	}
	@Override
	protected Material getMaterial() {
		return texture;
	}
}
