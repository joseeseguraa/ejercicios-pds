package inf.pds.tpv.domain.model.producto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import inf.pds.tpv.domain.model.producto.ProductoId.IdentificadorProductoException;

class ProductoTest {

    @Test
    void testCoberturaTotalDescuentos() throws IdentificadorProductoException {
        ProductoId id = ProductoId.of(101L);
        
        Producto p1 = new Producto(id, "A", 1, 100.0);
        assertTrue(p1.tieneDescuento());
        assertEquals(0.20, p1.getDescuento(), 0.001);

        Producto p2 = new Producto(id, "B", 2, 100.0);
        assertTrue(p2.tieneDescuento());
        assertEquals(0.10, p2.getDescuento(), 0.001);

        Producto p3 = new Producto(id, "C", 3, 100.0);
        assertFalse(p3.tieneDescuento());
        assertEquals(0.10, p3.getDescuento(), 0.001);

        Producto p4 = new Producto(id, "D", 4, 100.0);
        assertFalse(p4.tieneDescuento());
        assertEquals(0.0, p4.getDescuento(), 0.001);
        
        Producto p0 = new Producto(id, "E", 0, 100.0);
        assertEquals(0.0, p0.getDescuento(), 0.001);
    }

    @Test
    void testGettersYSetters() throws IdentificadorProductoException {
        ProductoId id = ProductoId.of(101L);
        Producto p = new Producto(id, "Original", 5, 10.0);
        
        p.setDescripcion("Editado");
        p.setCantidad(20);
        p.setPrecio(15.5);
        
        assertEquals("Editado", p.getDescripcion());
        assertEquals(20, p.getCantidad());
        assertEquals(15.5, p.getPrecio());
        assertEquals(id, p.getIdentificador());
    }

    @Test
    void testEqualsYHashCode() throws IdentificadorProductoException {
        ProductoId id1 = ProductoId.of(101L);
        ProductoId id2 = ProductoId.of(102L);
        
        Producto p1 = new Producto(id1, "A", 1, 10.0);
        Producto p1Bis = new Producto(id1, "A", 1, 10.0);
        Producto p2 = new Producto(id2, "B", 1, 10.0);
        
        assertEquals(p1, p1);
        assertEquals(p1, p1Bis);
        assertNotEquals(p1, p2);
        assertNotEquals(p1, null);
        assertNotEquals(p1, new Object());
        
        assertEquals(p1.hashCode(), p1Bis.hashCode());
    }
}