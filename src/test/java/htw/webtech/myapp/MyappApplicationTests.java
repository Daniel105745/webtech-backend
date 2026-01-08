package htw.webtech.myapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Disabled;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest

// Überschreibt die Datenbankkonfiguration nur für diesen Test.
// Verwendet eine H2-In-Memory-Datenbank anstelle von PostgreSQL.
@TestPropertySource(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb",
		"spring.datasource.driver-class-name=org.h2.Driver"
})

class MyappApplicationTests {

	@Test
	void contextLoads() {
	}

}
