package com.apapedia.catalogue;

import java.util.Locale;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.apapedia.catalogue.dto.CatalogueMapper;
import com.apapedia.catalogue.dto.request.CreateCatalogueRequestDTO;
import com.apapedia.catalogue.model.Category;
import com.apapedia.catalogue.model.Catalogue;
import com.github.javafaker.Faker;
import com.apapedia.catalogue.service.CatalogueService;
import com.apapedia.catalogue.service.CategoryService;
import java.util.UUID;

import jakarta.transaction.Transactional;

@SpringBootApplication
public class CatalogueApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogueApplication.class, args);
	}

	@Bean
 @Transactional
 CommandLineRunner run(CatalogueService catalogueService,  CatalogueMapper catalogueMapper, CategoryService categoryService){
  return args -> {
   var faker = new Faker(new Locale("in-ID"));
   for (int i = 0; i < 10; i++) {

    var categoryDTO = new Category();
    categoryDTO.setName(faker.food().ingredient());


    categoryService.createCategory(categoryDTO);

    var catalogueDTO = new Catalogue();

    catalogueDTO.setPrice(faker.number().randomDigitNotZero());
    catalogueDTO.setProductName(faker.commerce().productName());

    catalogueDTO.setProductDescription(faker.lorem().sentence());
    catalogueDTO.setStock(faker.number().randomDigitNotZero());
    // String imageUrl = generateRandomImageUrl();
    catalogueDTO.setImage("vamos");
    catalogueDTO.setCategory(categoryDTO);

    catalogueDTO.setIdSeller(UUID.randomUUID());
	catalogueDTO.setProductNameLower(catalogueDTO.getProductName().toLowerCase());

    
    catalogueService.saveCatalogue(catalogueDTO);
   }
  };
 }

}
