
public class Camera {
	Vector pos, towards, up, right, p1;
	float sc_dist, sc_width, sc_height;
	public Camera(Vector p, Vector l, Vector u, float sc_dist, float sc_width) {
		this.pos = p;
		this.towards=Vector.sub(l, pos);
		this.towards.normalize();
		this.up = u;
		this.up.normalize();
		this.sc_dist = sc_dist;
		this.sc_width = sc_width;
		this.right=Vector.cross(up,towards);
		this.right.normalize();
	}
	public Vector getRealUp(){// calculates the vector perpendicular to the plane of the picture
		Vector right = Vector.cross(up,towards);
		return Vector.cross(towards,right); 
	}
	public Vector getPos() {
		return Vector.copy(pos);
	}
	public Vector getTowards() {
		return Vector.copy(towards);
	}
	public Vector getUp() {
		return Vector.copy(up);
	}
	public Vector getRight() {
		return Vector.copy(right);
	}
	public Vector getP1() {
		return Vector.copy(p1);
	}
	public float getSc_dist() {
		return sc_dist;
	}
	public float getSc_width() {
		return sc_width;
	}
	public float getSc_height() {
		return sc_height;
	}
}