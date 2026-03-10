package inf.pds.tpv.domain.model.producto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import inf.pds.tpv.domain.model.producto.ProductoId.IdentificadorProductoException;

class ProductoIdTest {

    @Test
    void testCrearProductoId() throws IdentificadorProductoException {
        Long codigoValido = 101L;
        ProductoId id = ProductoId.of(codigoValido);
        assertNotNull(id);
        assertEquals(codigoValido, id.getCodigoNumerico());
    }

    @Test
    void testCrearProductoIdExcepciones() {
        assertThrows(IdentificadorProductoException.class, () -> ProductoId.of(100L));
        assertThrows(IdentificadorProductoException.class, () -> ProductoId.of(50L));
        assertThrows(IdentificadorProductoException.class, () -> ProductoId.of(null));
    }

    @Test
    void testEqualsYHashCode() throws IdentificadorProductoException {
        ProductoId id1 = ProductoId.of(101L);
        ProductoId id1Bis = ProductoId.of(101L);
        ProductoId id2 = ProductoId.of(102L);

        assertEquals(id1, id1);
        assertEquals(id1, id1Bis);
        assertNotEquals(id1, id2);
        assertNotEquals(id1, null);
        assertNotEquals(id1, new Object());
        
        assertEquals(id1.hashCode(), id1Bis.hashCode());
    }
}