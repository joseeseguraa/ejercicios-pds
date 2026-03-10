package inf.pds.tpv.domain.model.producto;

import java.util.Objects;

public class Producto {

	private ProductoId identificador;
	private String descripcion;
	private int cantidad;
	private double precio;

	public Producto(ProductoId identificador, String descripcion, int cantidad, double precio) {
		this.identificador = identificador;
		this.descripcion = descripcion;
		this.cantidad = cantidad;
		this.precio = precio;
	}

	public ProductoId getIdentificador() {
		return identificador;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public boolean tieneDescuento() {
	    return this.cantidad < 3;
	}

	public double getDescuento() {
	    if (this.cantidad == 1) return 0.20;
	    if (this.cantidad == 2 || this.cantidad == 3) return 0.10;
	    return 0.0;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(identificador);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Producto)) {
			return false;
		}
		Producto other = (Producto) obj;
		return Objects.equals(identificador, other.identificador);
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;		
	}

}
