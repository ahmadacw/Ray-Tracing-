package RayTracing;
public class Ray {
		
		Vector p0; 
		Vector V;
		public Ray(Vector p0, Vector v) {
			super();
			this.p0 = p0;
			v.normalize();
			this.V=v;
			
		}
		
	public Vector calculate(double t) {// calculates the point on the ray when we set the variable to t.
		Vector x= Vector.multiply_Scal(V, t);
		return Vector.add(p0,x);
	
	}

	public Vector getP0() {
		return p0;
	}

	public Vector getV() {
		return V;
	}

}
