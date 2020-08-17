package RayTracing;

public class Material {
Vector diff;
Vector spec;
Vector reflection;
float phong;
float trans;
public Material(Vector d, Vector s, Vector r, float phong, float trans) {
	super();
	this.diff = d;
	this.spec = s;
	this.reflection = r;
	this.phong = phong;
	this.trans = trans;
}
public Vector getDiff() {
	return diff;
}
public Vector getSpec() {
	return spec;
}
public Vector getReflection() {
	return reflection;
}
public float getPhong() {
	return phong;
}
public float getTrans() {
	return trans;
}

}