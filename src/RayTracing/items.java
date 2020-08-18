
public abstract class items {
	Material texture; // the material of the object
	int indx;	// the index of the object, used once to relate the item to its texture
	public  abstract double intersect (Ray r) ; 	// returns the variable setting for the intersection point, null if there is none.
	protected abstract Material getMaterial();	// returns the material of the object
	public abstract Vector getNormal(Vector v) ;// returns the normal at the point v
}
