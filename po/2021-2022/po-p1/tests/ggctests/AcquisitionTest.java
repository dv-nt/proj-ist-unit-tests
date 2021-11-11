package ggctests;

import ggctests.utils.PoUILibTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AcquisitionTest extends PoUILibTest {

    public AcquisitionTest() {
      super(true);
    }

    protected void setupWarehouseManager() {
    }

    @Test
    @DisplayName("A-11-01-M-ok - Compra lote de produto simples existente com preço intermédio, vê transacção  ")
    void acquireSimpleProductViewTransaction() {
        // simulate esgotado.ggc
        loadFromInputFile("test034.input");
        this.interaction.addMenuOptions(5, 1, 0, 7, 3, 1, 0);
        this.interaction.addFieldValues("M1", "2", "SAL", "10", "0"); // add sell transaction
        this.interaction.addMenuOptions(5, 1, 0, 7, 4, 1, 1, 0, 5, 1, 4);
        this.interaction.addFieldValues("M1", "SAL", "100", "10", "0", "1", "SAL");

        this.runApp();

        assertNoMoreExceptions();
        assertEquals("""
                HIDROGENIO|2|5000
                ROLHA|2|500
                SAL|1|10
                VIDRO|1|500
                VENDA|0|M1|SAL|10|10|10|2
                HIDROGENIO|2|5000
                ROLHA|2|500
                SAL|1|0
                VIDRO|1|500
                VENDA|0|M1|SAL|10|10|10|2
                COMPRA|1|M1|SAL|10|1000|0
                HIDROGENIO|2|5000
                ROLHA|2|500
                SAL|100|10
                VIDRO|1|500
                SAL|M1|100|10""", this.interaction.getResult());
    }

    @Test
    @DisplayName("A-11-02-M-ok - Compra lote de produto simples existente com preço intermédio, vê parceiro e entreposto")
    void acquireSimpleProductViewPartnerAndWarehouse() {
        loadFromInputFile("test025.input");
        this.interaction.addMenuOptions(7, 4, 0);
        this.interaction.addFieldValues("M1", "ROLHA", "25", "100");
        this.interaction.addMenuOptions(5, 1, 2, 0, 6, 1);
        this.interaction.addFieldValues("M1");

        this.runApp();

        assertNoMoreExceptions();
        assertEquals("""
                HIDROGENIO|200|5000
                ROLHA|30|1100
                SAL|1000|500
                VIDRO|1000|500
                HIDROGENIO|S1|200|5000
                ROLHA|M1|20|500
                ROLHA|M1|25|100
                ROLHA|M1|30|500
                SAL|S1|1000|500
                VIDRO|M1|1000|500
                M1|Rohit Figueiredo|New Delhi, India|NORMAL|0|2500|0|0""", this.interaction.getResult());
    }

    @Test
    @DisplayName("A-11-03-M-ok - Compra lote de produto agregado existente com preço intermédio, vê transacção")
    void acquireDerivedProductViewTransaction() {
        loadFromInputFile("test035.input");
        this.interaction.addMenuOptions(7, 4, 1);
        this.interaction.addFieldValues("M1", "GARRAFA", "70", "1000", "0");

        this.runApp();

        assertNoMoreExceptions();
        assertEquals("COMPRA|0|M1|GARRAFA|1000|70000|0", this.interaction.getResult());
    }

    @Test
    @DisplayName("A-11-04-M-ok - Compra lote de produto agregado existente com preço intermédio, vê parceiro e produto")
    void acquireDerivedProductViewPartnerAndProduct() {
        loadFromInputFile("test035.input");
        this.interaction.addMenuOptions(7, 4, 0);
        this.interaction.addFieldValues("M1", "GARRAFA", "70", "1000");
        this.interaction.addMenuOptions(6, 5, 1, 0, 5, 2);
        this.interaction.addFieldValues("M1", "M1");

        this.runApp();

        assertNoMoreExceptions();
        assertEquals("""
                COMPRA|0|M1|GARRAFA|1000|70000|0
                M1|Rohit Figueiredo|New Delhi, India|NORMAL|0|70000|0|0
                GARRAFA|M1|70|1000
                GARRAFA|S1|50|1000
                GARRAFA|S1|80|1000
                ROLHA|M1|20|500
                VIDRO|M1|30|500""", this.interaction.getResult());
    }

    @Test
    @DisplayName("A-11-05-M-ok - Compra lote de produto não existente com produtos")
    void acquireBatchOfUnknownProductOnWarehouseWithOtherKnownProducts() {
        loadFromInputFile("test035.input");
        this.interaction.addMenuOptions(7, 4, 1, 0);
        this.interaction.addFieldValues("M1", "PAPEL", "12", "2000", "n", "0");
        this.interaction.addMenuOptions(5, 1, 0, 6, 1);
        this.interaction.addFieldValues("M1");

        this.runApp();

        assertNoMoreExceptions();
        assertEquals("""
                COMPRA|0|M1|PAPEL|2000|24000|0
                GARRAFA|80|2000|0.1|VIDRO:1#ROLHA:1
                PAPEL|12|2000
                ROLHA|20|500
                VIDRO|30|500
                M1|Rohit Figueiredo|New Delhi, India|NORMAL|0|24000|0|0""", this.interaction.getResult());
    }

    @Test
    @DisplayName("A-11-06-M-ok - Compra lote de produto não existente sem produtos")
    void acquireBatchOfUnknownProductOnEmptyWarehouse() {
        loadFromInputFile("test036.input");
        this.interaction.addMenuOptions(7, 4, 1, 0);
        this.interaction.addFieldValues("M1", "papel", "20", "10", "n", "0");
        this.interaction.addMenuOptions(5, 1, 0, 6, 1);
        this.interaction.addFieldValues("M1");

        this.runApp();

        assertNoMoreExceptions();
        assertEquals("""
                COMPRA|0|M1|papel|10|200|0
                M1|Rohit Figueiredo|New Delhi, India|NORMAL|0|200|0|0""", this.interaction.getResult());
    }

}
