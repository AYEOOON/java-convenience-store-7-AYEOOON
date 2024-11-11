package store.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.Product;
import store.model.Promotion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductUtilTest {

    private Promotion promotionA;
    private Promotion promotionB;
    private List<Promotion> promotions;
    private ProductUtil productUtil;

    @BeforeEach
    void setUp() {
        promotionA = new Promotion("프로모션A", 2, 1, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));
        promotionB = new Promotion("프로모션B", 3, 2, LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
        promotions = Arrays.asList(promotionA, promotionB);
        productUtil = new ProductUtil(promotions);
    }

    @Test
    void 프로모션이없는상품_정상적으로파싱된다() throws IOException {
        List<String> lines = Arrays.asList(
                "Name,Price,Quantity,Promotion",
                "Cherry,500,30,null"
        );

        List<Product> products = productUtil.parseProducts(lines);

        assertThat(products).hasSize(1);

        Product cherry = products.get(0);

        assertEquals("Cherry", cherry.getName());
        assertEquals(500, cherry.getPrice());
        assertEquals(30, cherry.getGeneralStock());
        assertEquals(0, cherry.getPromotionStock());
        assertNull(cherry.getActivePromotion());
    }

    @Test
    void 잘못된형식의입력시_예외가발생한다() {
        List<String> lines = Arrays.asList(
                "Name,Price,Quantity,Promotion",
                "Apple,1000,20" // Promotion 필드 누락
        );

        Exception exception = assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            productUtil.parseProducts(lines);
        });

        assertNotNull(exception);
    }

    @Test
    void loadProductData를통해파일에서데이터를읽어온다() throws IOException {
        Path tempFile = Files.createTempFile("products", ".csv");
        List<String> lines = Arrays.asList(
                "Name,Price,Quantity,Promotion",
                "Apple,1000,20,프로모션A",
                "Banana,1500,15,프로모션B",
                "Cherry,500,30,null"
        );
        Files.write(tempFile, lines);

        List<String> loadedLines = productUtil.loadProductData(tempFile.toString());

        assertThat(loadedLines).isEqualTo(lines);

        Files.deleteIfExists(tempFile);
    }

    @Test
    void 빈파일을로드하면빈리스트를반환한다() throws IOException {
        Path tempFile = Files.createTempFile("empty", ".csv");
        List<String> lines = Arrays.asList(
                "Name,Price,Quantity,Promotion"
        );
        Files.write(tempFile, lines);

        List<String> loadedLines = productUtil.loadProductData(tempFile.toString());
        List<Product> products = productUtil.parseProducts(loadedLines);

        assertThat(products).isEmpty();

        Files.deleteIfExists(tempFile);
    }

    @Test
    void null파일경로를로드하면예외가발생한다() {
        assertThrows(NullPointerException.class, () -> {
            productUtil.loadProductData(null);
        });
    }

    @Test
    void 존재하지않는파일경로를로드하면예외가발생한다() {
        String invalidPath = "non_existent_file.csv";

        assertThrows(IOException.class, () -> {
            productUtil.loadProductData(invalidPath);
        });
    }
}