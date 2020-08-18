
public class Triangle extends items{
Vector T_1,T_2,T_3,norm;
private Vector x;
private Vector y;
double d;
double offset;
public Triangle(Vector v_1, Vector v_2, Vector v_3,int indx) {
	super();
	this.T_1 = v_1;
	this.T_2 = v_2;
	this.T_3 = v_3;
	this.x= Vector.sub(v_2,v_1);
	this.y= Vector.sub(v_3, v_1);
	norm=Vector.cross(x, y);
	norm.normalize();
	//x.normalize();
	//y.normalize();
	this.offset=Vector.dot(this.norm, v_2);
	
	this.indx=indx;

}

@Override
public double intersect(Ray r) {// returns the variable setting for the intersection point, null if there is none.
	double t=-((Vector.dot(r.p0, this.norm))-this.offset)/Vector.dot(r.V, this.norm);
	if (t<0)
		return -1;
	Vector P=Vector.add(Vector.multiply_Scal(r.V, t), r.p0);
	P.scale_add(T_1, -1);
	double beta = (P.getY()*x.getX() - P.getX()*x.getY())/(-y.getX()*x.getY()+y.getY()*x.getX());
	double alpha = (P.getX()-beta*y.getX())/x.getX();
	if (!(alpha>=0 && alpha <=1 && beta >=0 && beta <=1 && alpha + beta <=1)) 
		return -1;
	return t;	
}

@Override
public Vector getNormal(Vector v) {
	return Vector.copy(this.norm);
}
@Override
protected Material getMaterial() {
	return texture;
}

}