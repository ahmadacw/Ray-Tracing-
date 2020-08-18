
public class Light {
	Vector p; //position 
	Vector rgb;
	float spec;
	float shadow;
	float width;
	public Light(Vector p, Vector rgb, float spec, float shadow, float width) {
		super();
		this.p = p;
		this.rgb=rgb;
		this.spec = spec;
		this.shadow = shadow;
		this.width = width;
	}
	public Vector getP() {
		return p;
	}
	public Vector getRgb() {
		return rgb;
	}
	public float getSpec() {
		return spec;
	}
	public float getShadow() {
		return shadow;
	}
	public float getWidth() {
		return width;
	}
}