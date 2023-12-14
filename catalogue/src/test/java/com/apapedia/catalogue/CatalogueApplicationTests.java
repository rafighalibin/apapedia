package com.apapedia.catalogue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import com.apapedia.catalogue.model.Catalogue;
import com.apapedia.catalogue.model.Category;
import com.apapedia.catalogue.repository.CategoryDb;
import com.apapedia.catalogue.service.CategoryService;

@SpringBootTest
class CatalogueApplicationTests {

	@Mock
	private CategoryDb categoryDb;

	@InjectMocks
	private CategoryService categoryService;

	@BeforeEach
	void setUp() {
		List<Category> categories = new ArrayList<>();

		var category1 = new Category();
		category1.setName("category1");
		category1.setListCatalogue(new ArrayList<Catalogue>());

		categories.add(category1);

		var category2 = new Category();
		category2.setName("category2");
		category2.setListCatalogue(new ArrayList<Catalogue>());

		categories.add(category2);

		Mockito.when(categoryDb.findAll()).thenReturn(categories);
	}

	@Test
	public void testFindAll() {

		var listCategory = categoryService.getAllCategories();

		assertEquals(2, listCategory.size());
	}

}
